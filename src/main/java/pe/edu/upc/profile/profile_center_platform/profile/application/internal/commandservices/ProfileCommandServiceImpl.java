package pe.edu.upc.profile.profile_center_platform.profile.application.internal.commandservices;

import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.CreateProfileCommand;
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

  public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
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
    if (!profileRepository.existsById(command.profileId())) {
      throw new IllegalArgumentException("The profile does not exist");
    }

    try {
      profileRepository.deleteById(command.profileId());
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
    EmailAddress emailAddress = new EmailAddress(command.address());
    StreetAddress streetAddress = new StreetAddress(
        command.street(),
        command.number(),
        command.city(),
        command.postalCode(),
        command.country());

    try {
      var updatedProfile = profileToUpdate.updateEmail(emailAddress);
      updatedProfile = profileToUpdate.updateAddress(streetAddress);

      var resultProfile = profileRepository.save(updatedProfile);

      return Optional.of(resultProfile);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while updating profile: " + e.getMessage());
    }
  }
}
