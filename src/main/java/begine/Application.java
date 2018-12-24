package begine;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {
	
	@Value("${server.port}")
	public static int port;

	public static void main(String[] args) {
		HashMap<String, Object> props = new HashMap<>();
		props.put("server.port", port);
		new SpringApplicationBuilder().sources(Application.class).properties(props).run(args);
	}
}