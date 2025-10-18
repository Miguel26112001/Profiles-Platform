package pe.edu.upc.profile.profile_center_platform.profile.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  // El nombre del Exchange debe ser el mismo que el configurado en Authors
  public static final String EXCHANGE_NAME = "profile-updates-exchange";

  // Este servicio SOLO NECESITA EL EXCHANGE para enviar mensajes (enrutar).
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  // ------------------------------------------------------------------
  // SOLUCIÓN AL ERROR: Configurar el conversor de JSON
  // ------------------------------------------------------------------
  @Bean
  public MessageConverter jsonMessageConverter() {
    // Usa Jackson (la biblioteca de serialización por defecto de Spring) para convertir objetos a JSON y viceversa.
    return new Jackson2JsonMessageConverter();
  }
}
