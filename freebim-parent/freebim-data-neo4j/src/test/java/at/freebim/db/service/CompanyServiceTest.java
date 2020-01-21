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
import at.freebim.db.domain.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class CompanyServiceTest extends AbstractBaseTest {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private DateService dateService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getTest() {
		Company company = new Company();
		company.setName("test");
		company = companyService.save(company);

		assertNotNull(company.getNodeId());

		Company cTemp = companyService.get("test");

		assertEquals(company.getNodeId(), cTemp.getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		Company company = new Company();
		company.setName("TESTC");
		company.setValidFrom(dateService.getMillis() - 100);

		company = companyService.save(company);
		assertNotNull(company.getNodeId());

		List<Company> cTemp = companyService.getAllRelevant();

		assertNotNull(cTemp);
		assertEquals(1, cTemp.size());
		assertEquals(company.getNodeId(), cTemp.get(0).getNodeId());
	}
}
