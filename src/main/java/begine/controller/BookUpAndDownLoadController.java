package begine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhailz
 *
 * @version 2018年8月30日 上午10:28:01
 */
@RestController
public class BookUpAndDownLoadController {

	@RequestMapping(value = "/up", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView uploadFileAction(@RequestParam("uploadFile") MultipartFile uploadFile) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("uploadAndDownload");
		InputStream fis = null;
		OutputStream outputStream = null;
		try {
			fis = uploadFile.getInputStream();
			outputStream = new FileOutputStream("/data/logs/data/" + uploadFile.getOriginalFilename());
			IOUtils.copy(fis, outputStream);
			modelAndView.addObject("sucess", "上传成功");
			return modelAndView;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		modelAndView.addObject("sucess", "上传失败!");
		return modelAndView;
	}

	@RequestMapping(value="/down", method = {RequestMethod.POST,RequestMethod.GET})
	public void downloadFileAction(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(name = "fName", required = true) String fName) {
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		FileInputStream fis = null;
		try {
			File file = new File(fName);
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
