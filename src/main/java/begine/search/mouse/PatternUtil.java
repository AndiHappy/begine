package begine.search.mouse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;


public class PatternUtil {

private static Logger log = org.slf4j.LoggerFactory.getLogger(PatternUtil.class);
	
	private Properties hostSetting = new  Properties();
	private Map<String,HostSetting> usehostSetting = new HashMap<>();
	private FileWriter hostSettingWriter = null;

	private PatternUtil(){
		try {
			String value = "hostsetting.properties";
			File file  = new File(value);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer=new FileWriter(file,true);
			setHostsWriter(writer);
			FileReader reader = new FileReader(file);
			hostSetting.load(reader);
			for (Object host : hostSetting.keySet()) {
				String pv = hostSetting.getProperty((String) host);
				HostSetting setting = new HostSetting((String) host,pv);
				usehostSetting.put((String)host, setting);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class PatternUtilHolder{
		private static  PatternUtil instance = new PatternUtil();
	}
	
	public static PatternUtil instanct(){
		return PatternUtilHolder.instance;
	}
	
	
	public  boolean saveHostSetting(String host,String setting) {
		try {
			if(hostSetting.getProperty(host) == null){
				hostSetting.put(host, setting);
				hostSettingWriter.append(host+"="+setting+"\n");
				hostSettingWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public FileWriter getHostsWriter() {
		return hostSettingWriter;
	}

	public void setHostsWriter(FileWriter hostsWriter) {
		this.hostSettingWriter = hostsWriter;
	}

	public boolean hasHost(String host) {
		if(this.hostSetting.containsKey(host)){
			return true;
		}
		return false;
	}

	public int getHostPriority(String host) {
		if(!StringUtil.isBlank(host)){
			HostSetting setting =  this.usehostSetting.get(host);
			if(setting != null){
				return setting.getPriority();
			}
		}
		return 100;
	}
	
	public HostSetting getHostSetting(String host) {
		if(!StringUtil.isBlank(host)){
			HostSetting setting =  this.usehostSetting.get(host);
			return setting;
		}
		return null;
	}


	public int getHostStartIndex(String host) {
	      return this.usehostSetting.get(host).getStartIndex();
	}
	
	public int getHostEndIndex(String host) {
	      return this.usehostSetting.get(host).getEndIndex();
	}

}
