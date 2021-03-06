package com.repo.service;

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

import com.repo.Validation.FileValidator;
import com.repo.entity.ResponseObject;
import com.repo.exception.FileNotFoundException;
import com.repo.exception.InvalidFileException;

@Service("fileService")
public class FileService {
	final static Logger logger = LoggerFactory.getLogger(FileService.class);

	@Value("${file.path.fallback}")
	private String folder;

	@Value("${file.path}")
	public String path;
	
	@Value("${file.content.type}")
	public String contentType;

	//Directory Map which stores id->filename
	//Change the implementation.
	public static Map<String,String> directoryMap = new HashMap<String,String>();

	public File dir;
	public String key;
	public File serverFile;
	public String filename;

	@PostConstruct
	private void initPath() throws Exception{
		if(path==null || path.length()==0){
			path = System.getProperty("catalina.base")+File.separator+folder;
		}
		
		dir = new File(path+ File.separator+"uploads");

		//Create uploads directory if it doesn't exist.
		if(!dir.exists()){
			if(!dir.mkdirs())
				throw new Exception("Cannot create directory ‘uploads’: Permission denied");
		}
	}

	public ResponseObject uploadAudio(MultipartFile file) throws InvalidFileException, IOException {
		// TODO Auto-generated method stub
		//Unique Id for each upload
		key = UUID.randomUUID().toString();

		if (FileValidator.isValid(file)) { 
			serverFile = new File(dir.getAbsolutePath() + File.separator+ file.getOriginalFilename());
			FileOutputStream fop = new FileOutputStream(serverFile);
			fop.write(file.getBytes());
			directoryMap.put(key, file.getOriginalFilename());
			fop.flush();
			fop.close();
		}
		return new ResponseObject(Boolean.TRUE, HttpStatus.ACCEPTED.value(), "ID:"+key);
	}

	public void downloadAudio(HttpServletResponse response, String id) throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub

		filename =  directoryMap.get(id);	
		logger.info("Request id {}, filename {}",id,filename);

		if(filename != null)
		{
			serverFile = new File(dir+File.separator+id);
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setHeader("Content-Length", String.valueOf(serverFile.length()));
			FileCopyUtils.copy(new FileInputStream(serverFile), response.getOutputStream());
		}
		else{ 
			throw new FileNotFoundException("File not found.");
		}
	}

	public File[] getDirectory() throws IOException {
		// TODO Auto-generated method stub
		//Change for Directory
		File folder = new File(dir.getAbsoluteFile().toString());
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}
}