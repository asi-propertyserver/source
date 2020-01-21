package at.freebim.db.service;

import java.util.HashMap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public abstract class AbstractBaseTest {

	@Autowired
	protected Session session;

	@Before
	public void setup() {
		// delete everything
		session.query("Match (n) detach delete n;", new HashMap<>());

		// create user
		String query = "Create (f:FreebimUser {username: {username}, password:{password}, validFrom: {validFrom}, roles: [\"ROLE_USERMANAGER\",\"ROLE_EDIT\",\"ROLE_ADMIN\"]});";
		HashMap<String, Object> params = new HashMap<>();
		params.put("username", "admin2");
		params.put("password", "$2a$10$oENOheEeOHhrqV7R FOtJv.xJosWU/AadoMuIodHtYtKHZCPB51EnG");
		params.put("validFrom", 1556697501675L);
		session.query(query, params);
	}

}
