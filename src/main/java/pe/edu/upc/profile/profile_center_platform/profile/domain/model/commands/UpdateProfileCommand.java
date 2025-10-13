package pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands;

public record UpdateProfileCommand(Long profileId,
                                   String address,
                                   String street,
                                   String number,
                                   String city,
                                   String postalCode,
                                   String country) {
}
