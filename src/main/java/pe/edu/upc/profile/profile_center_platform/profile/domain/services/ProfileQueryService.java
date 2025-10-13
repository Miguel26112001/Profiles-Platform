package pe.edu.upc.profile.profile_center_platform.profile.domain.services;

import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetAllProfilesQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByEmailQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
  List<Profile> handle(GetAllProfilesQuery query);

  Optional<Profile> handle(GetProfileByIdQuery query);

  Optional<Profile> handle(GetProfileByEmailQuery query);
}
