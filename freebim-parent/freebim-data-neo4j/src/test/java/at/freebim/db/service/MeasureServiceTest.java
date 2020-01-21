package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class MeasureServiceTest extends AbstractBaseTest {

	@Autowired
	private MeasureService measureService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getMeasuresForTest() {
		DataType dataType = new DataType();
		dataType.setName("TESTDATATYPE");
		dataType.setDesc("TESTDESCDATATYPE");
		session.save(dataType);

		Unit unit = new Unit();
		unit.setName("TESTUNIT");
		unit.setDesc("TESTDESCUNIT");
		session.save(unit);

		ValueList valueList = new ValueList();
		valueList.setName("VALULISTNAME");
		session.save(valueList);

		Library library = new Library();
		library.setName("TESTLIB");
		session.save(library);

		Measure m = measureService.getMeasureFor(dataType, unit, valueList, "test", new ArrayList<>(), library);

		assertNotNull(m);
		assertNotNull(m.getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getByNameTest() {
		Measure measure = new Measure();
		measure.setName("TESTMEASURE");
		measure = measureService.save(measure);

		List<Measure> mTemp = measureService.getByName("TESTMEASURE");

		assertNotNull(mTemp);
		assertEquals(1, mTemp.size());
		assertEquals(measure.getNodeId(), mTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Measure measure = new Measure();
		measure.setName("TESTMEASURE");
		measure.setBsddGuid("TESTBSDD");
		measure = measureService.save(measure);

		List<Measure> mTemp = measureService.findByBsddGuid("TESTBSDD");

		assertNotNull(mTemp);
		assertEquals(1, mTemp.size());
		assertEquals(measure.getNodeId(), mTemp.get(0).getNodeId());
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

		List<Measure> mTemp = measureService.getAllRelevant();

		assertNotNull(mTemp);
		assertEquals(1, mTemp.size());
		assertEquals(measure.getNodeId(), mTemp.get(0).getNodeId());
	}
}
