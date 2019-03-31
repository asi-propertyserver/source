/**
 * 
 */
package at.freebim.db.service;

import java.util.Map;

import at.freebim.db.dto.IfdComment;
import at.freebim.db.dto.IfdConcept;
import at.freebim.db.dto.IfdDefinition;
import at.freebim.db.dto.IfdDescription;
import at.freebim.db.dto.IfdLoginData;
import at.freebim.db.dto.IfdName;
import at.freebim.db.dto.IfdPageInfo;
import at.freebim.db.dto.IfdSearchInfo;

/**
 * @author rainer
 *
 */
public interface IfdService {

	public Map<?,?> currentUserCanEdit();

	public Map<?,?> currentUserIsOwner();

	public Map<?,?> getIfdLanguage();

	public Map<?,?> sessionLogin(IfdLoginData loginData);

	public Map<?,?> sessionLogout();

	public Map<?,?> searchFilterLanguageType(String languageGuid, String type, String search);

	public Map<?,?> putIfdName(IfdName ifdName);

	public Map<?,?> putIfdDescription(IfdDescription ifdDescription);

	public Map<?,?> putIfdNameForGuid(String guid, IfdName ifdName);

	public Map<?,?> putIfdDefinitionForGuid(String guid, IfdDefinition ifdDefinition);

	public Map<?,?> putIfdConcept(IfdConcept ifdConcept);

	public Map<?,?> getIfdConceptParents(String guid, IfdPageInfo pageInfo);

	public Map<?,?> getIfdConceptChildren(String guid, IfdPageInfo pageInfo);

	public Map<?,?> getIfdConcept(String guid);

	public Map<?,?> searchForDuplicates(String type, IfdSearchInfo searchInfo);

	public Map<?,?> putIfdCommentForGuid(String guid, IfdComment ifdComment);

}
