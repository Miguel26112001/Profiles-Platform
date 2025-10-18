package pe.edu.upc.profile.profile_center_platform.profile.domain.model.events;

public record ProfileEmailUpdatedEvent(
    Long profileId,
    String newEmail) {
}