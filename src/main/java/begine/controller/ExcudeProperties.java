package begine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class ExcudeProperties {	
	private Properties hostSetting = new  Properties();

	private ExcudeProperties(){
		try {
			String value = "exclude.properties";
			File file  = new File(value);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileReader reader = new FileReader(file);
			hostSetting.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class PatternUtilHolder{
		private static  ExcudeProperties instance = new ExcudeProperties();
	}
	
	public static ExcudeProperties instanct(){
		return PatternUtilHolder.instance;
	}
	

	public boolean hasHost(String host) {
		if(this.hostSetting.containsKey(host)){
			return true;
		}
		return false;
	}

}
