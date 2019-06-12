package begine.server.note;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import begine.util.FileUtil;
import begine.util.Util;

@Component
public class NoteFileServer {
	
	@Value("${server.storefullpath}")
	private String storeFullPath;

	public  void saveOrUpdate(String id, String text) {
		check();
		File tmp = Util.findFile(new File(storeFullPath),id);
		if(tmp == null) {
			String filepath = storeFullPath+File.pathSeparator+id;
			FileUtil.instanct().saveValueToFile(filepath , text, true);
		}else {
			FileUtil.instanct().saveValueToFile(tmp , text, false);
		}
	}
	

	private void check() {
		assert(storeFullPath != null);
		File file = new File(storeFullPath);
		assert(file.exists() && file.isDirectory());
	}

}
