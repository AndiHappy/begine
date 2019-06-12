package begine.controller.note;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import begine.server.note.NoteFileServer;
import begine.util.BResult;

/**
 * @author zhailz
 *
 * @version 2018年8月14日 下午5:07:13
 */

@RestController
@RequestMapping("/note")
public class NoteController {

	private static Logger logger = LoggerFactory.getLogger(NoteController.class);

	//	private WeakHashMap<String, String> cache = new WeakHashMap<String, String>(200); 

	@Autowired
	private NoteFileServer noteFileServer;
	
	@RequestMapping("/write")
	@PostMapping
	public BResult test(HttpServletRequest req, @RequestParam(value = "id") String id,
			@RequestParam(value = "content") String text) {
		logger.info("url:{},id:{},content:{}", req.getRequestURI(), id, text);
		noteFileServer.saveOrUpdate(id,text);
		return new BResult(0, id, "success");
	}

}
