/**
 * 
 */
package at.freebim.db.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;

import at.freebim.db.websocket.message.MessageRequest;

/**
 * @author rainer
 *
 */
public interface MessageController {

	@MessageMapping("/hello")
	public void hello(MessageRequest rq);
}
