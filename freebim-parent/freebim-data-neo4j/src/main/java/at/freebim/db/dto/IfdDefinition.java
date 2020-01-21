/**
 *
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdDefinition implements Serializable {

	private static final long serialVersionUID = -8796841228369650461L;

	private String languageGuid;
	private String definition;

	/**
	 *
	 */
	public IfdDefinition() {
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
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

}
