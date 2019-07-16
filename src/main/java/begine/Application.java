package begine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableScheduling
@EnableAdminServer
public class Application {
	
	@Value("${server.port}")
	public static int port;

	public static void main(String[] args) {
//		HashMap<String, Object> props = new HashMap<>();
//		props.put("server.port", port);
//		new SpringApplicationBuilder().sources(Application.class).properties(props).run(args);
//		SpringApplication.run(Application.class);
		
		 SpringApplication app = new SpringApplication(Application.class);
	   app.setBannerMode(Banner.Mode.OFF);
	   app.run(args);
	}
	
	
}