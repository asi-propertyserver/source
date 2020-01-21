/**
 * 
 */
package at.freebim.db.websocket.controller;

import at.freebim.db.websocket.message.BaseMessage;

/**
 * @author rainer
 *
 */
public interface MessageSender {

	public void sendMessage(String address, BaseMessage message);
}
