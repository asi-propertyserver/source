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
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class DataTypeServiceTest extends AbstractBaseTest {

	@Autowired
	private DataTypeService dataTypeService;

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
	public void getByNameTest() {
		DataType dataType = new DataType();
		dataType.setName("ABCDEF");

		dataType = dataTypeService.save(dataType);

		assertNotNull(dataType.getNodeId());

		DataType dTemp = dataTypeService.getByName("ABCDEF");

		assertNotNull(dTemp);
		assertEquals(dataType.getNodeId(), dTemp.getNodeId());

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		DataType dataType = new DataType();
		dataType.setBsddGuid("TEST1");

		dataType = dataTypeService.save(dataType);
		assertNotNull(dataType.getNodeId());

		List<DataType> dTemp = dataTypeService.findByBsddGuid("TEST1");

		assertEquals(1, dTemp.size());
		assertEquals(dataType.getNodeId(), dTemp.get(0).getNodeId());

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

		DataType dataType = new DataType();
		dataType.setName("TESTD");
		dataType.setValidFrom(dateService.getMillis() - 100);

		dataType = dataTypeService.save(dataType);
		assertNotNull(dataType.getNodeId());

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

		OfDataType ofDataType = new OfDataType();
		ofDataType.setN1(measure);
		ofDataType.setN2(dataType);

		session.save(ofDataType);

		List<DataType> dTemp = dataTypeService.getAllRelevant();

		assertNotNull(dTemp);
		assertEquals(1, dTemp.size());
		assertEquals(dataType.getNodeId(), dTemp.get(0).getNodeId());

	}
}
