package com.audioRepo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.audioRepo.Validation.FileValidator;
import com.audioRepo.entity.ResponseObject;
import com.audioRepo.exception.FileNotFoundException;
import com.audioRepo.exception.InvalidFileException;

@Service("audioService")
public class AudioServiceImpl implements IAudioService {
	final static Logger logger = LoggerFactory.getLogger(AudioServiceImpl.class);

	//Static fallback path.
	final private String folder = "AudioRepo";

	@Value("${file.path}")
	public String path;

	//Directory Map which stores id->filename
	//Change the implementation.
	public static Map<String,String> directoryMap = new HashMap<String,String>();

	public ResponseObject responseObj = null;
	public File dir = null;
	public String key;
	public File serverFile;
	public FileOutputStream fop;
	public String filename;

	@PostConstruct
	private void initPath(){
		if(path==null || path.length()==0)
			path = System.getProperty("catalina.base")+"/"+folder;

		dir = new File(path+ File.separator+"uploads");
		
		//Create uploads directory if it doesn't exist.
		if(!dir.exists())
			dir.mkdirs();
	}

	@Override
	public ResponseObject uploadAudio(MultipartFile file) throws InvalidFileException, IOException {
		// TODO Auto-generated method stub
		//Unique Id for each upload
		key = UUID.randomUUID().toString();

		if (FileValidator.isValid(file)) {      	
			byte[] bytes = file.getBytes();
			filename = file.getOriginalFilename();

			serverFile = new File(dir.getAbsolutePath() + File.separator+ key);
			fop = new FileOutputStream(serverFile);
			fop.write(bytes);
			directoryMap.put(key, filename);
			responseObj = new ResponseObject(Boolean.TRUE, HttpStatus.ACCEPTED.value(), "ID:"+key);

			fop.flush();
			fop.close();
		}
		return responseObj;
	}

	@Override
	public ResponseObject downloadAudio(HttpServletResponse response, String id) throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub

		filename =  directoryMap.get(id);	
		logger.info("Request id {}, filename {}",id,filename);

		if(filename != null)
		{
			serverFile = new File(dir+File.separator+id);
			InputStream in = new FileInputStream(serverFile);
			logger.info("File path for download: "+serverFile.getPath());

			response.setContentType("audio/mpeg");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setHeader("Content-Length", String.valueOf(serverFile.length()));
			FileCopyUtils.copy(in, response.getOutputStream());
		}
		else throw new FileNotFoundException("File not found.");
		return responseObj;
	}

	@Override
	public Map<String,String> getDirectory() throws IOException {
		// TODO Auto-generated method stub
		return directoryMap;
	}
}