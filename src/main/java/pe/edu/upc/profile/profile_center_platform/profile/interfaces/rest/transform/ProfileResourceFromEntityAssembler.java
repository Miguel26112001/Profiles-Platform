package pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform;

import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.EmailAddress;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.StreetAddress;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
  public static ProfileResource toResourceFromEntity(Profile entity) {
    EmailAddress emailAddress = entity.getEmail();
    StreetAddress streetAddress = entity.getAddress();

    return new ProfileResource(
        entity.getId(),
        emailAddress.address(),
        streetAddress.street(),
        streetAddress.number(),
        streetAddress.city(),
        streetAddress.postalCode(),
        streetAddress.country());
  }
}
