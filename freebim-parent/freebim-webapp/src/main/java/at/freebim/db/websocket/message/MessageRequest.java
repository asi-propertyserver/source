/**
 * 
 */
package at.freebim.db.websocket.message;

/**
 * @author rainer
 *
 */
public class MessageRequest extends BaseMessage {

	public MessageRequest() {
		this(null);
	}

	public MessageRequest(String uuid) {
		super(uuid);
	}
}
