package pe.edu.upc.profile.profile_center_platform.profile.application.events.resources;

// Contrato de evento para notificar la eliminación de un perfil
public record ProfileDeletedEvent(
    Long profileId) {
}