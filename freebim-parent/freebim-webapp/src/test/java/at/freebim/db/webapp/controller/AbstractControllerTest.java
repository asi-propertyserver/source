package at.freebim.db.webapp.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import at.freebim.db.Application;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.webapp.models.request.LoginRequestModel;
import at.freebim.db.webapp.models.response.LoginResponseModel;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = Application.class)
//@Profile({"test"})
public abstract class AbstractControllerTest {

	/*protected final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	protected String token;

	@Autowired
	protected MockMvc mockMvc;
	
	@MockBean
	protected FreebimUserService freebimUserService;
	
	@MockBean
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected ObjectMapper objectMapper;

	protected String objectToString(Object o) throws JsonProcessingException {
		ObjectWriter ow = this.objectMapper.writer().withDefaultPrettyPrinter();

		return ow.writeValueAsString(o);
	}

	@SuppressWarnings("unchecked")
	protected BaseNode extractResultFromAjaxResponse(String ajaxResponse, Class<? extends BaseNode> clazz) {
		@SuppressWarnings("rawtypes")
		HashMap hashReturn = null;
		;
		try {
			hashReturn = this.objectMapper.readValue(ajaxResponse, HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BaseNode baseNode = null;
		try {
			baseNode = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		if (hashReturn == null) {
			return null;
		}

		Object temp = hashReturn.get("result");
		HashMap<String, Object> result = null;

		if (temp instanceof LinkedHashMap) {
			result = (HashMap<String, Object>) temp;
		}

		if (result == null || baseNode == null) {
			return null;
		}

		baseNode.setNodeId(new Long((int) result.get("i")));

		if (baseNode.getClass().isAssignableFrom(BigBangNode.class)) {
			((BigBangNode) baseNode).setAppVersion((String) result.get("appVersion"));
		}

		if (baseNode instanceof UuidIdentifyable) {
			((UuidIdentifyable) baseNode).setUuid((String) result.get("u"));
		}

		return baseNode;
	}

	@Before
	public void setup() throws Exception {
		
		LoginRequestModel request = new LoginRequestModel();
		request.setPassword("password");
		request.setUsername("admin");

		MvcResult result = this.mockMvc.perform(
				post("/login/rest").contentType(this.APPLICATION_JSON_UTF8).content(this.objectToString(request)))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		LoginResponseModel bReturn = this.objectMapper.readValue(contentAsString, LoginResponseModel.class);

		assertNotNull(bReturn);
		assertNotNull(bReturn.getToken());
		this.token = bReturn.getToken();
	}*/

}
