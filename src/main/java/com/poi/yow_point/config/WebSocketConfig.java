package com.poi.yow_point.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Active le broker de messages WebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Préfixe pour les messages sortants (du serveur vers le client)
        // Les clients s'abonneront à des destinations commençant par /topic ou /queue
        config.enableSimpleBroker("/topic", "/queue"); // SimpleBroker gère les abonnements et la diffusion en mémoire.
                                                      // Pour la prod, on peut utiliser un broker externe (RabbitMQ, ActiveMQ, Redis)
                                                      // avec config.enableStompBrokerRelay(...)

        // Préfixe pour les messages entrants (du client vers le serveur)
        // Les messages envoyés par les clients à des destinations commençant par /app
        // seront routés vers les méthodes @MessageMapping dans les contrôleurs.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point de terminaison que les clients utiliseront pour se connecter au serveur WebSocket.
        // "/ws-poi" est l'URL de handshake HTTP.
        registry.addEndpoint("/ws-poi")
                .setAllowedOriginPatterns("*") // Autoriser les connexions de toutes origines (à ajuster en prod)
                .withSockJS(); // SockJS est utilisé comme fallback si WebSocket n'est pas disponible.
    }
}