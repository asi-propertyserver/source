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
import at.freebim.db.domain.Discipline;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.OfDiscipline;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class DisciplineServiceTest extends AbstractBaseTest {

	@Autowired
	private DisciplineService disciplineService;

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
	public void findByBsddGuidTest() {
		Discipline discipline = new Discipline();
		discipline.setName("TEST");
		discipline.setBsddGuid("TESTBSDD");

		discipline = disciplineService.save(discipline);
		assertNotNull(discipline.getNodeId());

		List<Discipline> dTemp = disciplineService.findByBsddGuid("TESTBSDD");

		assertEquals(1, dTemp.size());
		assertEquals(discipline.getNodeId(), dTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelvantTest() {
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

		Discipline discipline = new Discipline();
		discipline.setName("TESTD");
		discipline.setValidFrom(dateService.getMillis() - 100);

		discipline = disciplineService.save(discipline);
		assertNotNull(discipline.getNodeId());

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

		OfDiscipline ofDiscipline = new OfDiscipline();
		ofDiscipline.setN1(parameter);
		ofDiscipline.setN2(discipline);

		session.save(ofDiscipline);

		List<Discipline> dTemp = disciplineService.getAllRelevant();

		assertNotNull(dTemp);
		assertEquals(1, dTemp.size());
		assertEquals(discipline.getNodeId(), dTemp.get(0).getNodeId());

	}
}
