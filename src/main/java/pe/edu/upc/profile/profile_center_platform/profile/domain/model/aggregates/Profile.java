package pe.edu.upc.profile.profile_center_platform.profile.domain.model.aggregates;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.EmailAddress;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects.StreetAddress;
import pe.edu.upc.profile.profile_center_platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
  @Embedded
  private EmailAddress email;

  @Embedded
  private StreetAddress address;

  public Profile updateAddress(StreetAddress newAddress) {
    if (newAddress == null) {
      throw new IllegalArgumentException("La dirección no puede ser nula");
    }
    this.address = newAddress;
    return this;
  }

  public Profile updateEmail(EmailAddress newEmail) {
    if (newEmail == null) {
      throw new IllegalArgumentException("El email no puede ser nulo");
    }
    this.email = newEmail;
    return this;
  }
}
