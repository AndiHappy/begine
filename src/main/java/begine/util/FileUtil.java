package begine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class FileUtil {

	private static Logger log = org.slf4j.LoggerFactory.getLogger(FileUtil.class);
	
	private Properties hosts = new  Properties();
	private FileWriter hostsWriter = null;

	private FileUtil(){
		try {
			String value = "host.properties";
			File file  = new File(value);
			if (!file.exists()) {
				file.createNewFile();
			}
			setHostsWriter(new FileWriter(file,true));
			hosts.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class FileUtilHolder{
		private static  FileUtil instance = new FileUtil();
	}
	
	public static FileUtil instanct(){
		return FileUtilHolder.instance;
	}
	
	/**
	 * 写入文件，不存在则创建文件后保存
	 * 
	 */
	public  boolean saveValueToFile(File file, String value, boolean append) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileUtils.write(file, value, "UTF-8", append);
			return true;
		} catch (IOException e) {
			log.error("存储数据出现错误：{}", e);
			return false;
		}
	}
	
	/**
	 * 写入文件，不存在则创建文件后保存
	 * 
	 */
	public  boolean saveValueToFile(String filefullPath, String value, boolean append) {
		try {
			File file = new File(filefullPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileUtils.write(file, value, "UTF-8", append);
			return true;
		} catch (IOException e) {
			log.error("存储数据出现错误：{}", e);
			return false;
		}
	}

	public  boolean saveHost(String host) {
		try {
			if(hosts.getProperty(host) == null){
				hosts.put(host, "true");
				hostsWriter.write(host+"=true\n");
				hostsWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public FileWriter getHostsWriter() {
		return hostsWriter;
	}

	public void setHostsWriter(FileWriter hostsWriter) {
		this.hostsWriter = hostsWriter;
	}

	public boolean hasHost(String host) {
		if(this.hosts.containsKey(host)){
			return true;
		}
		return false;
	}

}
