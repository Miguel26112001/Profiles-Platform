package pe.edu.upc.profile.profile_center_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProfileCenterPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileCenterPlatformApplication.class, args);
	}

}
