package pe.edu.upc.profile.profile_center_platform.profile.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates.Profile;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.EmailAddress;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  Optional<Profile> findByEmail(EmailAddress email);
}