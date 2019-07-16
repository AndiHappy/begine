package begine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartedAddActionBean implements CommandLineRunner {

    public void run(String... args) {
    	// apploiacation 启动之后，就会执行CommandLineRunner，可以在此处添加具体的逻辑
        System.out.println("执行的时机。。。。。。");
    }

}