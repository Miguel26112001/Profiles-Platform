package pe.edu.upc.profile.profile_center_platform.profile.application.events.resources;

// DTO del evento que viene de Authors (solo necesita el ProfileId)
public record AuthorDeletedEvent(
    Long profileId) {
}