/**
 *
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdComment implements Serializable {

	private static final long serialVersionUID = -860844572265014296L;

	private String languageGuid;
	private String comment;

	/**
	 *
	 */
	public IfdComment() {
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
