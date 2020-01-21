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
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class PhaseServiceTest extends AbstractBaseTest {

	@Autowired
	private PhaseService phaseService;

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
	public void getByCodeTest() {
		Phase phase = new Phase();
		phase.setName("TEST");
		phase.setCode("TESTTEST");

		phase = phaseService.save(phase);

		Phase pTemp = phaseService.getByCode("TESTTEST");

		assertNotNull(pTemp);
		assertEquals("TEST", pTemp.getName());
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

		Phase phase = new Phase();
		phase.setName("TEST");
		phase.setCode("TESTTEST");
		phase.setUuid("TESTUUID");

		phase = phaseService.save(phase);
		assertNotNull(phase.getNodeId());

		List<Phase> pTemp = phaseService.getAllRelevant();

		assertNotNull(pTemp);
		assertEquals(1, pTemp.size());
		assertEquals(phase.getNodeId(), pTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Phase phase = new Phase();
		phase.setName("TEST");
		phase.setCode("TESTTEST");
		phase.setBsddGuid("TESTBSDD");

		phase = phaseService.save(phase);
		assertNotNull(phase.getNodeId());

		List<Phase> pTemp = phaseService.findByBsddGuid("TESTBSDD");

		assertNotNull(pTemp);
		assertEquals(1, pTemp.size());
		assertEquals(phase.getNodeId(), pTemp.get(0).getNodeId());
	}
}
