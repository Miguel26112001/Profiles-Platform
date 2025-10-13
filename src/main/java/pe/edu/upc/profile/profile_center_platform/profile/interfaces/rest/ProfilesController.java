package pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.commands.DeleteProfileCommand;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetAllProfilesQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByEmailQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.model.queries.GetProfileByIdQuery;
import pe.edu.upc.profile.profile_center_platform.profile.domain.services.ProfileCommandService;
import pe.edu.upc.profile.profile_center_platform.profile.domain.services.ProfileQueryService;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.CreateProfileResource;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.ProfileResource;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.resources.UpdateProfileResource;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import pe.edu.upc.profile.profile_center_platform.profile.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profiles Management Endpoints")
public class ProfilesController {
  private final ProfileCommandService profileCommandService;
  private final ProfileQueryService profileQueryService;

  public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
    this.profileCommandService = profileCommandService;
    this.profileQueryService = profileQueryService;
  }

  @PostMapping()
  public ResponseEntity<ProfileResource> createProfile(
      @RequestBody CreateProfileResource createProfileResource
  ) {
    var createProfileCommand = CreateProfileCommandFromResourceAssembler
        .toCommandFromResource(createProfileResource);

    var profileId = profileCommandService.handle(createProfileCommand);

    if (profileId == null ) {
      return ResponseEntity.badRequest().build();
    }

    var getProfileByIdQuery = new GetProfileByIdQuery(profileId);
    var profile = profileQueryService.handle(getProfileByIdQuery);

    if (profile.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var createdProfileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profile.get());

    return new ResponseEntity<>(createdProfileResource, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfileResource> getProfileById(
      @PathVariable Long id
  ) {
    var getProfileByIdQuery = new GetProfileByIdQuery(id);
    var profile = profileQueryService.handle(getProfileByIdQuery);

    if (profile.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profile.get());

    return ResponseEntity.ok(profileResource);
  }

  @GetMapping
  public ResponseEntity<List<ProfileResource>> getAllProfiles() {
    var getAllProfilesQuery = new GetAllProfilesQuery();
    var profiles = profileQueryService.handle(getAllProfilesQuery);

    var profileResources = profiles.stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity).toList();

    return ResponseEntity.ok(profileResources);
  }

  @GetMapping("?email={email}")
  public ResponseEntity<ProfileResource> getProfileByEmail(
      @PathVariable String email
  ) {
    var getProfileByEmailQuery = new GetProfileByEmailQuery(email);
    var profile = profileQueryService.handle(getProfileByEmailQuery);

    if (profile.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    var profileResource = ProfileResourceFromEntityAssembler
        .toResourceFromEntity(profile.get());

    return ResponseEntity.ok(profileResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProfileResource> updateProfile(
      @PathVariable Long id,
      @RequestBody UpdateProfileResource updateProfileResource
  ) {
    var updateProfileCommand = UpdateProfileCommandFromResourceAssembler
        .toCommandFromResource(id, updateProfileResource);
    var updatedProfile = profileCommandService.handle(updateProfileCommand);

    if (updatedProfile.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile.get());

    return ResponseEntity.ok(profileResource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProfile(
      @PathVariable Long id)
  {
    var deleteProfileCommand = new DeleteProfileCommand(id);
    profileCommandService.handle(deleteProfileCommand);

    return ResponseEntity.ok("Profile with given id was successfully deleted");
  }
}
