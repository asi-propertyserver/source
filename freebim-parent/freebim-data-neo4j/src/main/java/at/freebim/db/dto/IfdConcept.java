/**
 * 
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdConcept implements Serializable {

	private static final long serialVersionUID = 2089171567685021612L;

	private String fullNameGuids;
	private String definitionGuids;
	private String commentGuids;
	private String conceptType;
	private String shortNameGuids;
	private String lexemeGuids;
	private String illustrationGuids;
	private String ownerGuid;
	
	/**
	 * 
	 */
	public IfdConcept() {
	}

	/**
	 * @return the fullNameGuids
	 */
	public String getFullNameGuids() {
		return fullNameGuids;
	}

	/**
	 * @param fullNameGuids the fullNameGuids to set
	 */
	public void setFullNameGuids(String fullNameGuids) {
		this.fullNameGuids = fullNameGuids;
	}

	/**
	 * @return the definitionGuids
	 */
	public String getDefinitionGuids() {
		return definitionGuids;
	}

	/**
	 * @param definitionGuids the definitionGuids to set
	 */
	public void setDefinitionGuids(String definitionGuids) {
		this.definitionGuids = definitionGuids;
	}

	/**
	 * @return the commentGuids
	 */
	public String getCommentGuids() {
		return commentGuids;
	}

	/**
	 * @param commentGuids the commentGuids to set
	 */
	public void setCommentGuids(String commentGuids) {
		this.commentGuids = commentGuids;
	}

	/**
	 * @return the conceptType
	 */
	public String getConceptType() {
		return conceptType;
	}

	/**
	 * @param conceptType the conceptType to set
	 */
	public void setConceptType(String conceptType) {
		this.conceptType = conceptType;
	}

	/**
	 * @return the shortNameGuids
	 */
	public String getShortNameGuids() {
		return shortNameGuids;
	}

	/**
	 * @param shortNameGuids the shortNameGuids to set
	 */
	public void setShortNameGuids(String shortNameGuids) {
		this.shortNameGuids = shortNameGuids;
	}

	/**
	 * @return the lexemeGuids
	 */
	public String getLexemeGuids() {
		return lexemeGuids;
	}

	/**
	 * @param lexemeGuids the lexemeGuids to set
	 */
	public void setLexemeGuids(String lexemeGuids) {
		this.lexemeGuids = lexemeGuids;
	}

	/**
	 * @return the illustrationGuids
	 */
	public String getIllustrationGuids() {
		return illustrationGuids;
	}

	/**
	 * @param illustrationGuids the illustrationGuids to set
	 */
	public void setIllustrationGuids(String illustrationGuids) {
		this.illustrationGuids = illustrationGuids;
	}

	/**
	 * @return the ownerGuid
	 */
	public String getOwnerGuid() {
		return ownerGuid;
	}

	/**
	 * @param ownerGuid the ownerGuid to set
	 */
	public void setOwnerGuid(String ownerGuid) {
		this.ownerGuid = ownerGuid;
	}

}
