package pe.edu.upc.profile.profile_center_platform.profile.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetAllProfilesQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByEmailQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByIdQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.EmailAddress;
import pe.edu.upc.profile.profile_center_platform.profile.domain.services.ProfileQueryService;
import pe.edu.upc.profile.profile_center_platform.profile.infrastructure.persistence.jpa.ProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
  private final ProfileRepository profileRepository;

  public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  @Override
  public List<Profile> handle(GetAllProfilesQuery query) {
    return profileRepository.findAll();
  }

  @Override
  public Optional<Profile> handle(GetProfileByIdQuery query) {
    return profileRepository.findById(query.profileId());
  }

  @Override
  public Optional<Profile> handle(GetProfileByEmailQuery query) {
    EmailAddress emailAddress = new EmailAddress(query.address());
    return profileRepository.findByEmail(emailAddress);
  }
}
