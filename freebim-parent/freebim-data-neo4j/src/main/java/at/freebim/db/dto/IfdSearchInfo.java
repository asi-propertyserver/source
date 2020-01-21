/**
 *
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdSearchInfo implements Serializable {

	private static final long serialVersionUID = 162135451827970234L;
	boolean cache;
	private String[] nameList;
	private String[] languageFamilyList;

	/**
	 * @return the nameList
	 */
	public String[] getNameList() {
		return nameList;
	}

	/**
	 * @param nameList the nameList to set
	 */
	public void setNameList(String[] nameList) {
		this.nameList = nameList;
	}

	/**
	 * @return the languageFamilyList
	 */
	public String[] getLanguageFamilyList() {
		return languageFamilyList;
	}

	/**
	 * @param languageFamilyList the languageFamilyList to set
	 */
	public void setLanguageFamilyList(String[] languageFamilyList) {
		this.languageFamilyList = languageFamilyList;
	}

	/**
	 * @return the cache
	 */
	public boolean isCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(boolean cache) {
		this.cache = cache;
	}

}
