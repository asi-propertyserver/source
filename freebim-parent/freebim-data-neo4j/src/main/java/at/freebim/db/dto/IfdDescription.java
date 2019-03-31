/**
 * 
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdDescription implements Serializable {

	private static final long serialVersionUID = -3623486192907979829L;

	private String languageGuid;
	private String description;
	private String descriptionType;
	
	/**
	 * 
	 */
	public IfdDescription() {
	}

	/**
	 * @return the languageGuid
	 */
	public String getLanguageGuid() {
		return languageGuid;
	}

	/**
	 * @param languageGuid the languageGuid to set
	 */
	public void setLanguageGuid(String languageGuid) {
		this.languageGuid = languageGuid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the descriptionType
	 */
	public String getDescriptionType() {
		return descriptionType;
	}

	/**
	 * @param descriptionType the descriptionType to set
	 */
	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
	}


}
