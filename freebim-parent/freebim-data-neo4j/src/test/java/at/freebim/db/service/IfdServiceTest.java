package at.freebim.db.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-config.xml"})
@Transactional
public class IfdServiceTest {

	@Autowired
	private IfdService ifdService;
	
	@Test
	public void testGetIfdLanguage() {
		Map<?,?> res = this.ifdService.getIfdLanguage();
		assertNotNull(res);
		assertTrue(res.size() > 0);
	}

}
