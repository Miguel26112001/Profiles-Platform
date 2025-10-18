package pe.edu.upc.profile.profile_center_platform.profile.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  // El nombre del Exchange debe ser el mismo que el configurado en Authors
  public static final String EXCHANGE_NAME = "profile-updates-exchange";

  // -----------------------------------------------------------------
  // CONSTANTES PARA EVENTOS PRODUCIDOS (Profile actualiza/borra)
  // -----------------------------------------------------------------
  // Clave para la actualización de email (enviado a Authors)
  public static final String ROUTING_KEY_EMAIL = "profile.email.updated";
  // Clave para el borrado de perfil (enviado a Authors)
  public static final String ROUTING_KEY_PROFILE_DELETED = "profile.deleted";

  // -----------------------------------------------------------------
  // CONSTANTES PARA EVENTOS CONSUMIDOS (Profiles recibe de Authors)
  // -----------------------------------------------------------------
  // Clave que Authors usa al borrar un autor
  public static final String ROUTING_KEY_AUTHOR_DELETED = "author.deleted";
  // La cola que Profiles escucha para el borrado de Authors
  public static final String QUEUE_AUTHOR_DELETED = "profiles.author-deleted";

  // Bean del Exchange (Punto de entrada/salida de mensajes)
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    // Usa Jackson (la biblioteca de serialización por defecto de Spring) para convertir objetos a JSON y viceversa.
    return new Jackson2JsonMessageConverter();
  }

  // -----------------------------------------------------------------
  // Configuración del Consumidor: Recibir AuthorDeletedEvent
  // -----------------------------------------------------------------

  // NUEVO: Cola para recibir el evento de borrado de Author
  @Bean
  public Queue authorDeletedQueue() {
    return new Queue(QUEUE_AUTHOR_DELETED, true); // durable: true
  }

  // NUEVO: Binding que conecta la cola a la clave que Authors utiliza ("author.deleted")
  @Bean
  public Binding authorDeletedBinding(Queue authorDeletedQueue, TopicExchange exchange) {
    return BindingBuilder.bind(authorDeletedQueue)
        .to(exchange)
        .with(ROUTING_KEY_AUTHOR_DELETED);
  }
}
