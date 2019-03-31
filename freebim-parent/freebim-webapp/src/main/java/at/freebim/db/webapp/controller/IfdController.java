/******************************************************************************
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.webapp.controller;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * @author rainer.breuss@uibk.ac.at
 */
@Controller
@RequestMapping("/ifd/")
public class IfdController  {

	private static final Logger logger = LoggerFactory.getLogger(IfdController.class);

	@Autowired
	private IfdService ifdService;
	
    public IfdController() {
	}

    @RequestMapping(value = "IfdContext/currentUserCanEdit", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse currentUserCanEdit(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("currentUserCanEdit ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.currentUserCanEdit();
	    	logger.debug("currentUserCanEdit returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdContext/currentUserIsOwner", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse currentUserIsOwner(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("currentUserIsOwner ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.currentUserIsOwner();
	    	logger.debug("currentUserIsOwner returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdLanguage", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse getIfdLanguage(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("getIfdLanguage ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.getIfdLanguage();
	    	logger.debug("getIfdLanguage returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "session/login", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse sessionLogin(@RequestBody IfdLoginData loginData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("sessionLogin ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.sessionLogin(loginData);
	    	logger.debug("sessionLogin returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "session/logout", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse sessionLogout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("sessionLogout ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.sessionLogout();
	    	logger.debug("sessionLogout returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdName/search/filter/language/{languageGuid}/nametype/{type}/{search}", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse searchFilterLanguageType(@PathVariable("languageGuid") String languageGuid,
    		@PathVariable("type") String type,
    		@PathVariable("search") String search,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("searchFilterLanguageType ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.searchFilterLanguageType(languageGuid, type, search);
	    	logger.debug("searchFilterLanguageType returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdName", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdName(@RequestBody IfdName ifdName,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdName ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdName(ifdName);
	    	logger.debug("putIfdName returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdDescription", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdName(@RequestBody IfdDescription ifdDescription,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdDescription ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdDescription(ifdDescription);
	    	logger.debug("putIfdDescription returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}/name", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdNameForGuid(@PathVariable("guid") String guid, 
    		@RequestBody IfdName ifdName,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdNameForGuid ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdNameForGuid(guid, ifdName);
	    	logger.debug("putIfdNameForGuid returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}/comment", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdCommentForGuid(@PathVariable("guid") String guid, 
    		@RequestBody IfdComment ifdComment,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdCommentForGuid ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdCommentForGuid(guid, ifdComment);
	    	logger.debug("putIfdCommentForGuid returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}/definition", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdDefinitionForGuid(@PathVariable("guid") String guid, 
    		@RequestBody IfdDefinition ifdDefinition,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdDefinitionForGuid ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdDefinitionForGuid(guid, ifdDefinition);
	    	logger.debug("putIfdDefinitionForGuid returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse putIfdConcept(@RequestBody IfdConcept ifdConcept,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("putIfdConcept ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.putIfdConcept(ifdConcept);
	    	logger.debug("putIfdConcept returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}/parents", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse getIfdConceptParents(@PathVariable("guid") String guid, @ModelAttribute IfdPageInfo pageInfo,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("getIfdConceptParents guid=[{}], pageInfo=[{}] ...", guid, pageInfo);
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.getIfdConceptParents(guid, pageInfo);
	    	logger.debug("getIfdConceptParents returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}/children", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse getIfdConceptChildren(@PathVariable("guid") String guid, @ModelAttribute IfdPageInfo pageInfo,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("getIfdConceptChildren ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.getIfdConceptChildren(guid, pageInfo);
	    	logger.debug("getIfdConceptChildren returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/{guid}", method = RequestMethod.GET)
    public @ResponseBody AjaxResponse getIfdConcept(@PathVariable("guid") String guid,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("getIfdConcept ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.getIfdConcept(guid);
	    	logger.debug("getIfdConcept returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    @RequestMapping(value = "IfdConcept/searchForDuplicates/filter/type/{type}", method = RequestMethod.POST)
    public @ResponseBody AjaxResponse searchForDuplicates(@PathVariable("type") String type,
    		@RequestBody IfdSearchInfo searchInfo,
    		HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    	logger.info("searchForDuplicates ...");
		AjaxResponse response = null;
		try {
			// Delegate to ifdService 
			Map<?,?> res = this.ifdService.searchForDuplicates(type, searchInfo);
	    	logger.debug("searchForDuplicates returned [{}].", res);
			response = new AjaxResponse((Serializable) res);
			
		} catch (AccessDeniedException e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}
		return response;
    }

    
    
    


}
