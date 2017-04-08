package com.audioRepo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.audioRepo.ServiceStartup;
import com.audioRepo.controller.AudioController;
import com.audioRepo.entity.ResponseObject;

@Service("audioService")
public class AudioServiceImpl implements IAudioService {
	final static Logger logger = LoggerFactory.getLogger(AudioServiceImpl.class);

	@Value("#{'${file.extension}'.split(',')}")
	public List<String> allowedExtension;
	
	//Base location path
	final static String LocationPath = System.getProperty("catalina.home");
	File dir = new File(LocationPath+ File.separator+"uploads");
	
	//Directory Map which stores id->filename
	static Map<Long,String> directoryMap = new HashMap<Long,String>();

	ResponseObject responseObj = null;

	@Override
	public ResponseObject uploadAudio(MultipartFile file) {
		// TODO Auto-generated method stub
		responseObj =  new ResponseObject();
		//Unique Id for each upload
		long key = new Date().getTime();

		if (!file.isEmpty()) {
			try {
				logger.info("File Content Type:"+file.getContentType());
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				logger.info("Allowed Extensions : "+allowedExtension.size()+" Extension: "+ext);
				if(allowedExtension.contains(ext))
				{
					InputStream isRef = file.getInputStream();            	
					byte[] bytes = file.getBytes();
					String name = file.getOriginalFilename();

					//Create uploads directory if it doesn't exist.
					if(!dir.exists())
						dir.mkdirs();

					File serverFile = new File(dir.getAbsolutePath() + File.separator+ key);
					logger.info(serverFile.getPath().toString());
					if (serverFile.exists())						
						serverFile.createNewFile();
					
					FileOutputStream fop = new FileOutputStream(serverFile);
					fop.write(bytes);

					directoryMap.put(key, name);
					responseObj.setCode(HttpStatus.ACCEPTED.value());
					responseObj.setStatus(Boolean.TRUE);
					responseObj.setMessage("ID: "+key);

					fop.flush();
					fop.close();
					isRef.close();
				}
				else{
					logger.info("Not an audio File");
					responseObj.setCode(HttpStatus.BAD_REQUEST.value());
					responseObj.setMessage("Not a valid audio file format");
					responseObj.setStatus(Boolean.FALSE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				responseObj.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				responseObj.setStatus(Boolean.FALSE);
				responseObj.setMessage("FAILURE");
			}

		} else {
			logger.info("Invalid path");
			responseObj.setCode(HttpStatus.BAD_REQUEST.value());
			responseObj.setStatus(Boolean.FALSE);
			responseObj.setMessage("No such file or directory");
		}
		return responseObj;
	}

	@Override
	public ResponseObject downloadAudio(HttpServletResponse response, String id) throws IOException {
		// TODO Auto-generated method stub

		String filename =  directoryMap.get(Long.parseLong(id));
		responseObj =  new ResponseObject();

		logger.info("Request id {}, filename {}",id,filename);
		
		if(filename != null)
		{
			File file = new File(dir.getAbsolutePath()+File.separator+id);
			InputStream in = new FileInputStream(file);
			logger.info("File path for download: "+file.getPath());
			
			response.setContentType("audio/mpeg");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setHeader("Content-Length", String.valueOf(file.length()));
			FileCopyUtils.copy(in, response.getOutputStream());
		}
		else
		{
			logger.info("File not found");
			responseObj.setCode(HttpStatus.BAD_REQUEST.value());
			responseObj.setStatus(Boolean.FALSE);
			responseObj.setMessage("Invalid Id");

		}
		return responseObj;
	}

	@Override
	public Map<Long, String> getDirectory() {
		// TODO Auto-generated method stub
		return directoryMap;
	}
}