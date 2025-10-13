package pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources;

public record CreateProfileResource(
    String email,
    String street,
    String number,
    String city,
    String postalCode,
    String country) {
}
