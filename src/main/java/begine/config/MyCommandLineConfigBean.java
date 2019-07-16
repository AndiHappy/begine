package begine.config;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * 获取配置型的信息
 * */
@Component
public class MyCommandLineConfigBean {
	
		public Logger log = LoggerFactory.getLogger(MyCommandLineConfigBean.class);
		
    @Autowired
    public MyCommandLineConfigBean(ApplicationArguments args) {
        boolean debug = args.containsOption("debug");
        log.info("debug is : {}",debug);
        List<String> files = args.getNonOptionArgs();
        // if run with "--debug logfile.txt" debug=true, files=["logfile.txt"]
    }

}