/**
 *
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdName implements Serializable {

	private static final long serialVersionUID = -6081072568461502348L;

	private String languageGuid;
	private String name;
	private String nameType;

	/**
	 *
	 */
	public IfdName() {
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameType
	 */
	public String getNameType() {
		return nameType;
	}

	/**
	 * @param nameType the nameType to set
	 */
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
}
