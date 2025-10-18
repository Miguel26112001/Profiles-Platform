package pe.edu.upc.profile.profile_center_platform.profile.interfaces.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.profile.profile_center_platform.profile.application.events.resources.AuthorDeletedEvent;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.DeleteProfileByProfileIdCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.services.ProfileCommandService;
import pe.edu.upc.profile.profile_center_platform.profile.infrastructure.config.RabbitMQConfig;

@Component
public class AuthorDeletedEventHandler {
  private final ProfileCommandService profileCommandService;

  public AuthorDeletedEventHandler(ProfileCommandService profileCommandService) {
    this.profileCommandService = profileCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_AUTHOR_DELETED)
  public void handleAuthorDeletedEvent(AuthorDeletedEvent event) {
    System.out.println("LOG: Received Author Deleted Event for Profile ID: " + event.profileId());

    // Mapear el evento al comando de eliminación local
    var command = new DeleteProfileByProfileIdCommand(event.profileId());

    // Ejecutar el comando
    profileCommandService.handle(command);
  }
}
