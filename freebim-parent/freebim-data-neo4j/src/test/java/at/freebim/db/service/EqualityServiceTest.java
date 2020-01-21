package at.freebim.db.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.Company;
import at.freebim.db.domain.rel.CompanyCompany;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class EqualityServiceTest extends AbstractBaseTest {

	@Autowired
	private EqualityService equalityService;

	@Autowired
	private CompanyService companyService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void relatedEqualsTest() {
		Company company = new Company();
		company.setName("TESTTEST");
		company.setCode("TESTETST");

		Company company1 = new Company();
		company1.setName("TEST!");
		company1.setCode("TEST124");

		company = companyService.save(company);
		company1 = companyService.save(company1);

		CompanyCompany companyCompany = new CompanyCompany();
		companyCompany.setN1(company);
		companyCompany.setN2(company1);

		session.save(companyCompany);

		Company cTemp = companyService.getByNodeId(company.getNodeId());

		assertTrue(equalityService.relatedEquals(cTemp.getCompany(), company1));

	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void relatedEqualsListTest() {
		Company company = new Company();
		company.setName("TESTTEST");
		company.setCode("TESTETST");

		Company company1 = new Company();
		company1.setName("TEST!");
		company1.setCode("TEST124");

		company = companyService.save(company);
		company1 = companyService.save(company1);

		CompanyCompany companyCompany = new CompanyCompany();
		companyCompany.setN1(company);
		companyCompany.setN2(company1);

		session.save(companyCompany);

		Company cTemp = companyService.getByNodeId(company.getNodeId());

		ArrayList<Company> list = new ArrayList<>();
		list.add(company1);
		assertTrue(equalityService.relatedEquals(cTemp.getCompany(), list));
	}
}
