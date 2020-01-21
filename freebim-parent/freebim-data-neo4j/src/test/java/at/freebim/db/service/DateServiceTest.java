package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class DateServiceTest extends AbstractBaseTest {

	@Autowired
	private DateService dateService;

	@Test
	public void getNowTest() {
		String now = dateService.getNow();
		Date dateNow = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		assertEquals(simpleDateFormat.format(dateNow).substring(0, 20), now.substring(0, 20));
	}

	@Test
	public void getMillisTest() {
		Long now = dateService.getMillis();

		assertTrue(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - 100 < now
				&& Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() + 100 > now);
	}

	@Test
	public void getMillisFromTest() {
		Long millis = dateService.getMillisFrom("2000-01-01 01:01:01.10000");

		assertNotEquals((Long) 0L, millis);
		assertEquals((Long) 946684871000L, millis);

	}
}
