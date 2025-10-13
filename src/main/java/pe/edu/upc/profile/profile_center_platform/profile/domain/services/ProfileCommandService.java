package pe.edu.upc.profile.profile_center_platform.profile.domain.services;

import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.CreateProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.DeleteProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

public interface ProfileCommandService {
  Long handle(CreateProfileCommand command);

  void handle(DeleteProfileCommand command);

  Optional<Profile> handle(UpdateProfileCommand command);
}
