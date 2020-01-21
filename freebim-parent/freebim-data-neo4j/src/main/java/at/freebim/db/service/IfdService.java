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

	Map<?, ?> currentUserCanEdit();

	Map<?, ?> currentUserIsOwner();

	Map<?, ?> getIfdLanguage();

	Map<?, ?> sessionLogin(IfdLoginData loginData);

	Map<?, ?> sessionLogout();

	Map<?, ?> searchFilterLanguageType(String languageGuid, String type, String search);

	Map<?, ?> putIfdName(IfdName ifdName);

	Map<?, ?> putIfdDescription(IfdDescription ifdDescription);

	Map<?, ?> putIfdNameForGuid(String guid, IfdName ifdName);

	Map<?, ?> putIfdDefinitionForGuid(String guid, IfdDefinition ifdDefinition);

	Map<?, ?> putIfdConcept(IfdConcept ifdConcept);

	Map<?, ?> getIfdConceptParents(String guid, IfdPageInfo pageInfo);

	Map<?, ?> getIfdConceptChildren(String guid, IfdPageInfo pageInfo);

	Map<?, ?> getIfdConcept(String guid);

	Map<?, ?> searchForDuplicates(String type, IfdSearchInfo searchInfo);

	Map<?, ?> putIfdCommentForGuid(String guid, IfdComment ifdComment);

}
