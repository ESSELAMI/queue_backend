package dz.me.filleattente;

import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import dz.me.filleattente.repositories.UserRepository;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@SpringBootApplication
public class FilleAttenteApplication {

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(FilleAttenteApplication.class, args);

	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer init() {
		userRepository.updatePassword("file1");
		return builder -> builder.timeZone(TimeZone.getTimeZone("Europe/Paris"));

	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
