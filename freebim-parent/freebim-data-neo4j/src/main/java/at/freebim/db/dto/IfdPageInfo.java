/**
 *
 */
package at.freebim.db.dto;

import java.io.Serializable;

/**
 * @author rainer
 *
 */
public class IfdPageInfo implements Serializable {

	private static final long serialVersionUID = -3112464178596683719L;

	private int page_size;
	private int page;
	private boolean cache;

	/**
	 * @return the page_size
	 */
	public int getPage_size() {
		return page_size;
	}

	/**
	 * @param page_size the page_size to set
	 */
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IfdPageInfo [page_size=").append(page_size).append(", page=").append(page).append(", cache=")
				.append(cache).append("]");
		return builder.toString();
	}

}
