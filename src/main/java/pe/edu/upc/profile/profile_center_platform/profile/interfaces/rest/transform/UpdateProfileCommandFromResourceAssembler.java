package pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform;

import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.UpdateProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
  public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
    return new UpdateProfileCommand(
        profileId,
        resource.email(),
        resource.street(),
        resource.number(),
        resource.city(),
        resource.postalCode(),
        resource.country());
  }
}
