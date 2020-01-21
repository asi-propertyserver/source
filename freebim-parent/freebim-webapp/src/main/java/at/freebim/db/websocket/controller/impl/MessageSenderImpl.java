/**
 * 
 */
package at.freebim.db.websocket.controller.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import at.freebim.db.websocket.controller.MessageSender;
import at.freebim.db.websocket.message.BaseMessage;

/**
 * @author rainer
 *
 */
@Controller
public class MessageSenderImpl implements MessageSender {

	private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);
	
	@Value("${app.version}")
	private String appVersion;
	
	@Value("${websocket.clientprefix}")
	private String clientPrefix;

	@Autowired
	private SimpMessagingTemplate template;
	
	@PostConstruct
	public void init() {
		this.clientPrefix = "/" + this.clientPrefix + "/";
		logger.info("init clientPrefix=[{}] ...", this.clientPrefix);
	}

	/* (non-Javadoc)
	 * @see net.spectroom.websocket.controller.MessageSender#sendMessage(net.spectroom.websocket.message.BaseMessage)
	 */
	@Override
	public void sendMessage(String address, BaseMessage message) {
		logger.info("sendMessage address=[{}] ...", address);
		message.setVersion(appVersion);
		this.template.convertAndSend(this.clientPrefix + address, message);
	}

}
