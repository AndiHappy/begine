package begine.springredis.client;

import org.springframework.core.io.FileUrlResource;

public class FileUrlResourcePathValue {
	
	public static void main(String[] args) throws Exception {
		FileUrlResource fileUrl = new FileUrlResource("rate_limiter.lua");
		System.out.println(fileUrl.getFile().getAbsolutePath());
		
		FileUrlResource fileUrl1 = new FileUrlResource("src/main/resources/rate_limiter.lua");
		System.out.println(fileUrl1.getFile().getAbsolutePath());
	}

}
