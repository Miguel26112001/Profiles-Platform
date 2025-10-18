package pe.edu.upc.profile.profile_center_platform.profile.application.internal.outboundservices;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.events.ProfileEmailUpdatedEvent;
import pe.edu.upc.profile.profile_center_platform.profile.infrastructure.config.RabbitMQConfig;

@Service
public class ProfileMessagingService {
  private final RabbitTemplate rabbitTemplate;
  // La clave de enrutamiento DEBE ser la misma que la que escucha Authors
  private static final String ROUTING_KEY_EMAIL = "profile.email.updated";

  public ProfileMessagingService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Envía un evento al broker de RabbitMQ indicando que el email del perfil ha sido actualizado.
   */
  public void sendEmailUpdatedEvent(Long profileId, String newEmail) {
    var event = new ProfileEmailUpdatedEvent(profileId, newEmail);

    System.out.println("LOG: Sending Profile Email Updated Event for Profile ID: " + profileId);

    // El método convertAndSend envía el mensaje al Exchange, con la clave de enrutamiento.
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE_NAME, // El Exchange de destino
        ROUTING_KEY_EMAIL,           // La clave de enrutamiento
        event                        // El DTO que será serializado a JSON
    );
  }
}
