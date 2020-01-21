package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ValueListEntryServiceTest extends AbstractBaseTest {

	@Autowired
	private ValueListEntryService valueListEntryService;

	@Autowired
	private ValueListService valueListService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private MeasureService measureService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void correctNumberFormatTest() {
		ValueListEntry valueListEntry = new ValueListEntry();
		valueListEntry.setName("10,0");

		valueListEntryService.correctNumberFormat(valueListEntry);

		assertEquals("10.0", valueListEntry.getName());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByNameTest() {
		ValueListEntry valueListEntry = new ValueListEntry();
		valueListEntry.setName("TESTNAMETEST");

		valueListEntry = valueListEntryService.save(valueListEntry);
		assertNotNull(valueListEntry.getNodeId());

		List<ValueListEntry> vTemp = valueListEntryService.getByName("TESTNAMETEST");

		assertEquals(1, vTemp.size());
		assertEquals(valueListEntry.getNodeId(), vTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		ValueListEntry valueListEntry = new ValueListEntry();
		valueListEntry.setName("TESTNAMETEST");
		valueListEntry.setBsddGuid("TESTBSDD");

		valueListEntry = valueListEntryService.save(valueListEntry);
		assertNotNull(valueListEntry.getNodeId());

		List<ValueListEntry> vTemp = valueListEntryService.findByBsddGuid("TESTBSDD");

		assertEquals(1, vTemp.size());
		assertEquals(valueListEntry.getNodeId(), vTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Library library = new Library();
		library.setName("TEST");
		library.setValidFrom(dateService.getMillis() - 100);

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TESTC");
		component.setValidFrom(dateService.getMillis() - 100);

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		Parameter parameter = new Parameter();
		parameter.setName("TESTP");
		parameter.setValidFrom(dateService.getMillis() - 100);

		parameter = parameterService.save(parameter);
		assertNotNull(parameter.getNodeId());

		Measure measure = new Measure();
		measure.setName("TESTM");
		measure.setValidFrom(dateService.getMillis() - 100);

		measure = measureService.save(measure);
		assertNotNull(measure.getNodeId());

		ValueList valueList = new ValueList();
		valueList.setName("TEST");
		valueList.setValidFrom(dateService.getMillis() - 100);

		valueList = valueListService.save(valueList);
		assertNotNull(valueList.getNodeId());

		ValueListEntry valueListEntry = new ValueListEntry();
		valueListEntry.setName("TESTVL");
		valueListEntry.setValidFrom(dateService.getMillis() - 100);

		valueListEntry = valueListEntryService.save(valueListEntry);
		assertNotNull(valueListEntry.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(bigBangNode);
		parentOf.setN2(library);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(library);
		parentOf1.setN2(component);

		session.save(parentOf1);

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component);
		hasParameter.setN2(parameter);
		hasParameter.setPhaseUuid("TESTUUID");

		session.save(hasParameter);

		HasMeasure hasMeasure = new HasMeasure();
		hasMeasure.setN1(parameter);
		hasMeasure.setN2(measure);

		session.save(hasMeasure);

		HasValue hasValue = new HasValue();
		hasValue.setN1(measure);
		hasValue.setN2(valueList);

		session.save(hasValue);

		HasEntry hasEntry = new HasEntry();
		hasEntry.setN1(valueList);
		hasEntry.setN2(valueListEntry);

		session.save(hasEntry);

		List<ValueListEntry> vTemp = valueListEntryService.getAllRelevant();

		assertEquals(1, vTemp.size());
		assertEquals(valueListEntry.getNodeId(), vTemp.get(0).getNodeId());
	}
}
