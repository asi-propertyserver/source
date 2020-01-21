/**
 * 
 */
package at.freebim.db.websocket.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import at.freebim.db.websocket.controller.MessageController;
import at.freebim.db.websocket.controller.MessageSender;
import at.freebim.db.websocket.message.MessageRequest;
import at.freebim.db.websocket.message.MessageResponse;

/**
 * @author rainer
 *
 */
@Controller
public class MessageControllerImpl implements MessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageControllerImpl.class);
	
	@Value("${app.version}")
	private String appVersion;

	@Autowired
	private MessageSender messageSender;
	
	/* (non-Javadoc)
	 * @see net.spectroom.websocket.controller.MessageController#hello(net.spectroom.websocket.message.MessageRequest)
	 */
	@Override
	public void hello(MessageRequest rq) {
		logger.info("hello: uuid=[{}]", ((rq == null) ? "null" : rq.getUuid()));
		MessageResponse rs = new MessageResponse(HtmlUtils.htmlEscape(rq.getUuid()));
		rs.setVersion(appVersion);
		this.messageSender.sendMessage("init", rs);
	}

}
