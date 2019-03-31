/**
 * 
 */
package at.freebim.db.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import at.freebim.db.dto.IfdComment;
import at.freebim.db.dto.IfdConcept;
import at.freebim.db.dto.IfdDefinition;
import at.freebim.db.dto.IfdDescription;
import at.freebim.db.dto.IfdLoginData;
import at.freebim.db.dto.IfdName;
import at.freebim.db.dto.IfdPageInfo;
import at.freebim.db.dto.IfdSearchInfo;
import at.freebim.db.service.IfdService;

/**
 * @author rainer
 *
 */
@Service
public class IfdServiceImpl implements IfdService {

	private static final Logger logger = LoggerFactory.getLogger(IfdServiceImpl.class);
	
	private RestTemplate restTemplate;
	
	@Value("${bsdd.url}")
	private String bsddUrl;
	
	@PostConstruct
	public void init() {
		logger.info("init ...");
		this.restTemplate = new RestTemplate();
	}
	
	private String getUrl(String part) {
		return this.bsddUrl + part;
	}
	
	private Map<?,?> call(String urlPart, HttpMethod method) {
		String url = this.getUrl(urlPart);
		logger.info("calling [{}] on url=[{}] ...", method, url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, method, entity, HashMap.class);
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#currentUserCanEdit()
	 */
	@Override
	public Map<?,?> currentUserCanEdit() {
		logger.debug("currentUserCanEdit ...");
		Map<?,?> res = call("IfdContext/currentUserCanEdit", HttpMethod.GET);
		logger.debug("currentUserCanEdit finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#currentUserIsOwner()
	 */
	@Override
	public Map<?,?> currentUserIsOwner() {
		logger.debug("currentUserIsOwner ...");
		Map<?,?> res = call("IfdContext/currentUserIsOwner", HttpMethod.GET);
		logger.debug("currentUserIsOwner finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#getIfdLanguage()
	 */
	@Override
	public Map<?,?> getIfdLanguage() {
		logger.debug("getIfdLanguage ...");
		Map<?,?> res = call("IfdLanguage", HttpMethod.GET);
		logger.debug("getIfdLanguage finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#sessionLogin(at.freebim.db.dto.IfdLoginData)
	 */
	@Override
	public Map<?, ?> sessionLogin(IfdLoginData loginData) {
		logger.debug("sessionLogin ...");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdLoginData> entity = new HttpEntity<IfdLoginData>(loginData, headers);
		String url = this.getUrl("session/login");
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("sessionLogin finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#sessionLogout()
	 */
	@Override
	public Map<?,?> sessionLogout() {
		logger.debug("sessionLogout ...");
		Map<?,?> res = call("session/logout", HttpMethod.POST);
		logger.debug("sessionLogout finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#searchFilterLanguageType(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<?,?> searchFilterLanguageType(String languageGuid, String type, String search) {
		String url = String.format("IfdName/search/filter/language/%s/nametype/%s/%s", languageGuid, type, search);
		logger.debug("searchFilterLanguageType ...");
		Map<?,?> res = call(url, HttpMethod.GET);
		logger.debug("searchFilterLanguageType finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdName(at.freebim.db.dto.IfdName)
	 */
	@Override
	public Map<?,?> putIfdName(IfdName ifdName) {
		logger.debug("putIfdName ...");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdName> entity = new HttpEntity<IfdName>(ifdName, headers);
		String url = this.getUrl("IfdName");
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdName finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdDescription(at.freebim.db.dto.IfdDescription)
	 */
	@Override
	public Map<?,?> putIfdDescription(IfdDescription ifdDescription) {
		logger.debug("putIfdDescription ...");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdDescription> entity = new HttpEntity<IfdDescription>(ifdDescription, headers);
		String url = this.getUrl("IfdDescription");
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdDescription finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdNameForGuid(java.lang.String, at.freebim.db.dto.IfdName)
	 */
	@Override
	public Map<?,?> putIfdNameForGuid(String guid, IfdName ifdName) {
		logger.debug("putIfdNameForGuid ...");
		String url = String.format("IfdConcept/%s/name", guid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdName> entity = new HttpEntity<IfdName>(ifdName, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdNameForGuid finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdDefinitionForGuid(java.lang.String, at.freebim.db.dto.IfdDefinition)
	 */
	@Override
	public Map<?,?> putIfdDefinitionForGuid(String guid, IfdDefinition ifdDefinition) {
		logger.debug("putIfdDefinitionForGuid ...");
		String url = String.format("IfdConcept/%s/definition", guid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdDefinition> entity = new HttpEntity<IfdDefinition>(ifdDefinition, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdDefinitionForGuid finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdConcept(at.freebim.db.dto.IfdConcept)
	 */
	@Override
	public Map<?,?> putIfdConcept(IfdConcept ifdConcept) {
		logger.debug("putIfdConcept ...");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdConcept> entity = new HttpEntity<IfdConcept>(ifdConcept, headers);
		String url = this.getUrl("IfdConcept");
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdConcept finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#getIfdConceptParents(java.lang.String, at.freebim.db.dto.IfdPageInfo)
	 */
	@Override
	public Map<?,?> getIfdConceptParents(String guid, IfdPageInfo pageInfo) {
		logger.debug("getIfdConceptParents ...");
		String url = String.format("IfdConcept/%s/parents", guid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdPageInfo> entity = new HttpEntity<IfdPageInfo>(pageInfo, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.GET, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.GET, entity, HashMap.class);
		logger.debug("getIfdConceptParents finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#getIfdConceptChildren(java.lang.String, at.freebim.db.dto.IfdPageInfo)
	 */
	@Override
	public Map<?,?> getIfdConceptChildren(String guid, IfdPageInfo pageInfo) {
		logger.debug("getIfdConceptChildren ...");
		String url = String.format("IfdConcept/%s/children", guid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdPageInfo> entity = new HttpEntity<IfdPageInfo>(pageInfo, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.GET, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.GET, entity, HashMap.class);
		logger.debug("getIfdConceptChildren finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#getIfdConcept(java.lang.String)
	 */
	@Override
	public Map<?,?> getIfdConcept(String guid) {
		logger.debug("getIfdConcept ...");
		Map<?,?> res = this.call("IfdConcept/" + guid, HttpMethod.GET);
		logger.debug("getIfdConcept finished.");
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#searchForDuplicates(java.lang.String, at.freebim.db.dto.IfdSearchInfo)
	 */
	@Override
	public Map<?,?> searchForDuplicates(String type, IfdSearchInfo searchInfo) {
		logger.debug("searchForDuplicates ...");
		String url = "IfdConcept/searchForDuplicates/filter/type/" + type;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdSearchInfo> entity = new HttpEntity<IfdSearchInfo>(searchInfo, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("searchForDuplicates finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.IfdService#putIfdCommentForGuid(java.lang.String, at.freebim.db.dto.IfdComment)
	 */
	@Override
	public Map<?,?> putIfdCommentForGuid(String guid, IfdComment ifdComment) {
		logger.debug("putIfdCommentForGuid ...");
		String url = String.format("IfdConcept/%s/comment", guid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<IfdComment> entity = new HttpEntity<IfdComment>(ifdComment, headers);
		url = this.getUrl(url);
		logger.info("call [{}] on url=[{}]", HttpMethod.POST, url);
		ResponseEntity<HashMap> res = this.restTemplate.exchange(url, HttpMethod.POST, entity, HashMap.class);
		logger.debug("putIfdCommentForGuid finished.");
		return ((res != null && res.hasBody()) ? res.getBody() : null);
	}

}
