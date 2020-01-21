package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

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
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.service.ProblemService.IdPair;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ProblemServiceTest extends AbstractBaseTest {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private MeasureService measureService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private PhaseService phaseService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getMissingMeasureTest() {
		Parameter p = new Parameter();
		p.setName("TEST");
		p = parameterService.save(p);

		ArrayList<Long> mTemp = problemService.getMissingMeasure();

		assertEquals(1, mTemp.size());
		assertEquals(p.getNodeId(), mTemp.get(0));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getEmptyMeasureTest() {
		Measure measure = new Measure();
		measure.setName("TESTNAME");

		measure = measureService.save(measure);

		ArrayList<Long> mTemp = problemService.getEmptyMeasure();

		assertEquals(1, mTemp.size());
		assertEquals(measure.getNodeId(), mTemp.get(0));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getComponentWithoutParametersTest() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Component component = new Component();
		component.setName("TEST");

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(bigBangNode);
		parentOf.setN2(library);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(library);
		parentOf1.setN2(component);

		session.save(parentOf1);

		ArrayList<Long> mTemp = problemService.getComponentWithoutParameters();

		assertNotNull(mTemp);
		assertEquals(1, mTemp.size());
		assertEquals(component.getNodeId(), mTemp.get(0));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void deletePhaseTest() {
		String bsdd = java.util.UUID.randomUUID().toString();

		Phase phase = new Phase();
		phase.setName("TESTTEST");
		phase.setValidFrom(dateService.getMillis() + 1000);
		phase.setValidTo(dateService.getMillis() - 1000);
		phase.setUuid(bsdd);
		phase.setBsddGuid(bsdd);

		phase = phaseService.save(phase);

		Component component = new Component();
		component.setName("TEST");
		component.setDesc("TEST");

		component = componentService.save(component);

		Parameter parameter = new Parameter();
		parameter.setName("TEST");
		parameter.setDesc("TEST");

		parameter = parameterService.save(parameter);

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component);
		hasParameter.setN2(parameter);
		hasParameter.setPhaseUuid(bsdd);

		session.save(hasParameter);

		ArrayList<Long> mTemp = problemService.deletedPhase();

		assertEquals(1, mTemp.size());
		assertEquals(parameter.getNodeId(), mTemp.get(0));
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void specializeableParametersTest() {
		Library library = new Library();
		library.setName("TESTLIBR");
		library.setDesc("TESTLIBDESC");

		library = libraryService.save(library);

		Component component = new Component();
		component.setName("TESTCOMP");
		component.setDesc("TESTDESC");
		component.setValidFrom(dateService.getMillis() - 1000);
		component.setValidTo(dateService.getMillis() + 1000);

		component = componentService.save(component);

		References references = new References();
		references.setN1(component);
		references.setN2(library);

		session.save(references);

		Component component1 = new Component();
		component1.setName("TEST2NAME");
		component1.setDesc("TESTDESC2");
		component1.setValidFrom(dateService.getMillis() - 1000);
		component1.setValidTo(dateService.getMillis() + 1000);

		component1 = componentService.save(component1);

		Component component2 = new Component();
		component2.setName("TEST3COMP");
		component2.setDesc("TESTDESCTEST");
		component2.setValidFrom(dateService.getMillis() - 1000);
		component2.setValidTo(dateService.getMillis() + 1000);

		component2 = componentService.save(component2);

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(component);
		parentOf.setN2(component1);

		session.save(parentOf);

		ParentOf parentOf1 = new ParentOf();
		parentOf1.setN1(component);
		parentOf1.setN2(component2);

		session.save(parentOf1);

		Parameter parameter = new Parameter();
		parameter.setName("TESTPARAM");
		parameter.setDesc("TESTDESC");
		parameter.setValidFrom(dateService.getMillis() - 1000);
		parameter.setValidTo(dateService.getMillis() + 1000);

		parameter = parameterService.save(parameter);

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component1);
		hasParameter.setN2(parameter);

		session.save(hasParameter);

		HasParameter hasParameter1 = new HasParameter();
		hasParameter1.setN1(component2);
		hasParameter1.setN2(parameter);

		session.save(hasParameter1);

		ArrayList<IdPair> pTemp = problemService.specializableParameters(library.getNodeId());

		assertEquals(2, pTemp.size());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void multipleParameterAssignmentTest() {
		Component component = new Component();
		component.setName("TESTCOMP");
		component.setDesc("TESTDESC");
		component.setValidFrom(dateService.getMillis() - 1000);
		component.setValidTo(dateService.getMillis() + 1000);

		component = componentService.save(component);

		Component component1 = new Component();
		component1.setName("TEST2NAME");
		component1.setDesc("TESTDESC2");
		component1.setValidFrom(dateService.getMillis() - 1000);
		component1.setValidTo(dateService.getMillis() + 1000);

		component1 = componentService.save(component1);

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(component);
		parentOf.setN2(component1);

		session.save(parentOf);

		Parameter parameter = new Parameter();
		parameter.setName("TESTPARAM");
		parameter.setDesc("TESTDESC");
		parameter.setValidFrom(dateService.getMillis() - 1000);
		parameter.setValidTo(dateService.getMillis() + 1000);

		parameter = parameterService.save(parameter);

		HasParameter hasParameter = new HasParameter();
		hasParameter.setN1(component);
		hasParameter.setN2(parameter);

		session.save(hasParameter);

		HasParameter hasParameter1 = new HasParameter();
		hasParameter1.setN1(component1);
		hasParameter1.setN2(parameter);

		session.save(hasParameter1);

		ArrayList<ProblemService.IdTriple> pTemp = problemService.multipleParameterAssignment();

		assertEquals(1, pTemp.size());
	}

}
