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
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.rel.ContributedBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class StatisticServiceTest extends AbstractBaseTest {

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private ComponentService componentService;

	@Autowired
	private ContributorService contributorService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getTest() {
		Component component = new Component();
		component.setName("TEST");

		Contributor contributor = new Contributor();
		contributor.setLastName("TEST");

		contributor = contributorService.save(contributor);
		assertNotNull(contributor.getNodeId());

		component = componentService.save(component);
		assertNotNull(component.getNodeId());

		ContributedBy contributedBy = new ContributedBy();
		contributedBy.setN1(component);
		contributedBy.setN2(contributor);
		contributedBy.setTs(dateService.getMillis() - 100);

		session.save(contributedBy);

		ArrayList<StatisticService.StatPoint> stats = statisticService.get(0L, dateService.getMillis());

		assertEquals(1, stats.size());
		assertEquals(1L, stats.get(0).m);
	}
}
