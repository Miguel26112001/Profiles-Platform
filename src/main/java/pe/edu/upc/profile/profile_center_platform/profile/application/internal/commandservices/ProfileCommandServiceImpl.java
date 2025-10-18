package pe.edu.upc.profile.profile_center_platform.profile.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.profile.profile_center_platform.profile.application.internal.outboundservices.ProfileMessagingService;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.CreateProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.DeleteProfileByProfileIdCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.DeleteProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.UpdateProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.EmailAddress;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.StreetAddress;
import pe.edu.upc.profile.profile_center_platform.profile.domain.services.ProfileCommandService;
import pe.edu.upc.profile.profile_center_platform.profile.infrastructure.persistence.jpa.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
  public final ProfileRepository profileRepository;
  private final ProfileMessagingService messagingService; // <-- NUEVO

  public ProfileCommandServiceImpl(ProfileRepository profileRepository, ProfileMessagingService messagingService) { // <-- ACTUALIZADO
    this.profileRepository = profileRepository;
    this.messagingService = messagingService; // <-- ASIGNACIÓN
  }

  @Override
  public Long handle(CreateProfileCommand command) {
    EmailAddress emailAddress = new EmailAddress(command.address());
    StreetAddress streetAddress = new StreetAddress(
        command.street(),
        command.number(),
        command.city(),
        command.postalCode(),
        command.country());
    Profile profile = new Profile(emailAddress, streetAddress);

    profileRepository.save(profile);

    return profile.getId();
  }

  @Override
  public void handle(DeleteProfileCommand command) {
    var profile = profileRepository.findById(command.profileId());
    if (profile.isEmpty()) {
      throw new IllegalArgumentException("The profile does not exist");
    }

    Long profileIdToDelete = command.profileId(); // Obtener la ID

    try {
      profileRepository.deleteById(profileIdToDelete); // Eliminar localmente

      // 1. Notificación Asíncrona: Borrar el autor en el otro servicio
      messagingService.sendProfileDeletedEvent(profileIdToDelete); // <-- PUNTO CLAVE

    } catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting profile: " + e.getMessage());
    }
  }

  @Override
  public Optional<Profile> handle(UpdateProfileCommand command) {
    var profile = profileRepository.findById(command.profileId());
    if (profile.isEmpty()) {
      return Optional.empty();
    }
    var profileToUpdate = profile.get();

    // Convertimos el nuevo email de String a Value Object (EmailAddress)
    EmailAddress emailAddress = new EmailAddress(command.address());

    // La dirección de la calle puede ser la misma o cambiada.
    StreetAddress streetAddress = new StreetAddress(
        command.street(),
        command.number(),
        command.city(),
        command.postalCode(),
        command.country());

    try {
      // 1. Actualizar email y dirección en el agregado
      var updatedProfile = profileToUpdate.updateEmail(emailAddress);
      updatedProfile = profileToUpdate.updateAddress(streetAddress);

      // 2. Persistencia: Guardar el cambio en la BD de Profiles
      var resultProfile = profileRepository.save(updatedProfile);

      // 3. COMUNICACIÓN ASÍNCRONA SIMPLE (Después de la persistencia):
      // Enviamos el evento con el ID del perfil y el nuevo email
      Long profileId = resultProfile.getId();
      String newEmail = resultProfile.getEmail().address(); // Obtenemos el email del agregado

      messagingService.sendEmailUpdatedEvent(profileId, newEmail); // <-- PUNTO CLAVE

      return Optional.of(resultProfile);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while updating profile: " + e.getMessage());
    }
  }

  @Override
  public void handle(DeleteProfileByProfileIdCommand command) {
    // 1. Buscar el Profile por su ID
    var profileOptional = profileRepository.findById(command.profileId());
    if (profileOptional.isEmpty()) {
      // En EDA, si el Perfil ya fue borrado o no existe, ignoramos.
      System.out.println("LOG: Profile not found for ID " + command.profileId() + ". Deletion event ignored.");
      return;
    }
    var profileToDelete = profileOptional.get();

    try {
      // 2. Eliminar el perfil localmente
      profileRepository.delete(profileToDelete);

      // NOTA: No enviamos un evento aquí. El flujo Profile -> Author debe ser iniciado
      // por el comando DeleteProfileCommand que recibe de la API, no por este.

    } catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting profile by command: " + e.getMessage());
    }
  }
}
