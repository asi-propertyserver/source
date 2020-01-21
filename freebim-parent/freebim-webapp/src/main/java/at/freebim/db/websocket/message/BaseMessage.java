/**
 * 
 */
package at.freebim.db.websocket.message;

/**
 * @author rainer
 *
 */
public class BaseMessage {

	private String uuid;
	private String version;

	public BaseMessage(String uuid) {
		this.uuid = uuid;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
