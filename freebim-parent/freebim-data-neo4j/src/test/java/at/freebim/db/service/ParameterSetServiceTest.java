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
import at.freebim.db.domain.ParameterSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class ParameterSetServiceTest extends AbstractBaseTest {

	@Autowired
	private ParameterSetService parameterSetService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		ParameterSet parameterSet = new ParameterSet();
		parameterSet.setName("TEST");
		parameterSet.setValidFrom(dateService.getMillis() - 1000);

		parameterSet = parameterSetService.save(parameterSet);
		assertNotNull(parameterSet.getNodeId());

		List<ParameterSet> pTemp = parameterSetService.getAllRelevant();

		assertEquals(1, pTemp.size());
		assertEquals(parameterSet.getNodeId(), pTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		ParameterSet parameterSet = new ParameterSet();
		parameterSet.setName("TEST");
		parameterSet.setBsddGuid("TESTBSDD");
		parameterSet.setValidFrom(dateService.getMillis() - 1000);

		parameterSet = parameterSetService.save(parameterSet);
		assertNotNull(parameterSet.getNodeId());

		List<ParameterSet> pTemp = parameterSetService.findByBsddGuid("TESTBSDD");

		assertEquals(1, pTemp.size());
		assertEquals(parameterSet.getNodeId(), pTemp.get(0).getNodeId());
	}
}
