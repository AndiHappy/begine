package begine.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import begine.util.ConfigUtil;
import begine.util.Util;

@Component
public class ScheduledController {

	private static Logger logger = LoggerFactory.getLogger(ScheduledController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(initialDelay=1000, fixedRate=5000)
    public void reportCurrentTime() {
//    	File file = new File(ConfigUtil.fileStorePath);
//    	long value = Util.getDirSize(file);
//    	logger.info("current:{}, long:{} , size:{} M ",dateFormat.format(new Date()),value,value/1024/1024);
//    	Util.ensureSpace();
    }

    
}