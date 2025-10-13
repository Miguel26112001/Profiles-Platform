package pe.edu.upc.profile.profile_center_platform.profile.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StreetAddress(
    String street,
    String number,
    String city,
    String postalCode,
    String country
) {
  // Constructor nulo
  public StreetAddress() {
    this(null, null, null, null, null);
  }

  // Constructor con todos los parámetros
  public StreetAddress(String street, String number, String city, String postalCode, String country) {
    // Validaciones
    if (street != null && street.isBlank()) {
      throw new IllegalArgumentException("Street must not be blank");
    }
    if (number != null && number.isBlank()) {
      throw new IllegalArgumentException("Number must not be blank");
    }
    if (city != null && city.isBlank()) {
      throw new IllegalArgumentException("City must not be blank");
    }
    if (postalCode != null && postalCode.isBlank()) {
      throw new IllegalArgumentException("Postal code must not be blank");
    }
    if (country != null && country.isBlank()) {
      throw new IllegalArgumentException("Country must not be blank");
    }

    // Asignación de campos (esto es necesario en el constructor explícito)
    this.street = street;
    this.number = number;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }

  public String getStreetAddress() {
    if (street == null || number == null || city == null || postalCode == null || country == null) {
      return "Invalid address";
    }
    return String.format("%s %s, %s, %s, %s", street, number, city, postalCode, country);
  }
}