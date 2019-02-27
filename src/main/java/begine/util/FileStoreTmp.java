/**
 * 
 */
package begine.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

/**
 * @author zhailz
 *
 */
public class FileStoreTmp {
	
	private static Map<String ,FileStoreTmp> store = new WeakHashMap<String ,FileStoreTmp>();
	private FileWriter writer = null;
	private Properties setting = new  Properties();

	private FileStoreTmp(String fileName){
		try {
			File file  = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter writer=new FileWriter(file,true);
			setWriter(writer);
			FileReader reader = new FileReader(file);
			setting.load(reader);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FileStoreTmp getInstance(String fileName){
		if(store.get(fileName) == null){
			FileStoreTmp filenametmp = new FileStoreTmp(fileName);
			store.put(fileName, filenametmp);
		}
		
		return store.get(fileName);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

	public FileWriter getWriter() {
		return writer;
	}

	public void setWriter(FileWriter writer) {
		this.writer = writer;
	}

}
