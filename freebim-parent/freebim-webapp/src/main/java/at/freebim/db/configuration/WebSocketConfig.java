/**
 * 
 */
package at.freebim.db.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author rainer
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Value("${websocket.clientprefix}")
	private String clientPrefix;

	@Value("${websocket.serverprefix}")
	private String serverPrefix;

	@Value("${websocket.endpoint}")
	private String endpoint;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/" + this.clientPrefix);
		config.setApplicationDestinationPrefixes("/" + this.serverPrefix);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/" + this.endpoint).withSockJS();
	}
}