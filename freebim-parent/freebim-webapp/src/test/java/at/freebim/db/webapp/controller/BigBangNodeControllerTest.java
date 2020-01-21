package at.freebim.db.webapp.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import at.freebim.db.Application;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.service.BigBangNodeService;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = Application.class)
//@WebMvcTest(BigBangNodeController.class)
//@Profile({"test"})
public class BigBangNodeControllerTest extends AbstractControllerTest {

/*	@MockBean
	private BigBangNodeService bigBangNodeService;

	@Test
	public void test() throws Exception {
		BigBangNode bNode = new BigBangNode();
		bNode.setNodeId(1L);
		bNode.setUuid("test");
		bNode.setAppVersion("1.0");

		when(bigBangNodeService.getBigBangNode()).thenReturn(bNode);

		MvcResult result = this.mockMvc.perform(get("/bbn").header("Authorization", "Bearer " + this.token))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		BigBangNode bReturn = (BigBangNode) this.extractResultFromAjaxResponse(contentAsString, BigBangNode.class);

		assertEquals(bNode.getNodeId(), bReturn.getNodeId());
		assertEquals(bNode.getUuid(), bReturn.getUuid());
		assertEquals(bNode.getAppVersion(), bReturn.getAppVersion());

	}
*/
}
