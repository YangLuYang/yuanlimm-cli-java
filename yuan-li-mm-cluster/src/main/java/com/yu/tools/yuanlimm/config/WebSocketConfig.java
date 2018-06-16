package com.yu.tools.yuanlimm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.annotation.Resource;

/**
 * Created by haoyuyang on 2016/11/25.
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Resource(name = "costumeChannelInterceptorAdapter")
    private ChannelInterceptor interceptor;

    /**
     * 将"/hello"路径注册为STOMP端点，这个路径与发送和接收消息的目的路径有所不同，这是一个端点，客户端在订阅或发布消息到目的地址前，要连接该端点，
     * 即用户发送请求url="/applicationName/hello"与STOMP server进行连接。之后再转发到订阅url；
     * PS：端点的作用——客户端在订阅或发布消息到目的地址前，要连接该端点。
     *
     * @param stompEndpointRegistry stompEndpointRegistry
     */
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
        stompEndpointRegistry.addEndpoint("/portfolio")
                .withSockJS();

        stompEndpointRegistry.addEndpoint("/portfolio")
                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                .setAllowedOrigins("*");
    }

    /**
     * 配置了一个简单的消息代理，如果不重载，默认情况下回自动配置一个简单的内存消息代理，用来处理以"/topic"为前缀的消息。
     * 这里重载configureMessageBroker()方法，
     * 消息代理将会处理前缀为"/topic"和"/queue"的消息。
     *
     * @param registry registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1.订阅模块定义，可以多个,如："/topic","/user"
        // 2.就是前端订阅了那个模块，当服务器要向那个模块发送信息时就从模块中取出对应的session,(session表明了是那个前端用户)
        // 3.就是那些前缀的URL可以
        registry.enableSimpleBroker("/topic", "/user");//客户端订阅消息前缀, 服务端发送消息前缀
        // 这句表示客户端向服务端发送时的主题上面需要加"/app"作为前缀,如：/app/hello
        registry.setApplicationDestinationPrefixes("/app", "/topic");//客户端发送消息前缀
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(this.interceptor);
    }
}
