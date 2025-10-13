package pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform;

import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.CreateProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
  public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
    return new CreateProfileCommand(
        resource.email(),
        resource.street(),
        resource.number(),
        resource.city(),
        resource.postalCode(),
        resource.country());
  }
}
