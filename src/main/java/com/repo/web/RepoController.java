package com.repo.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.repo.entity.ResponseObject;
import com.repo.exception.FileNotFoundException;
import com.repo.exception.InvalidFileException;
import com.repo.service.FileService;

@RestController
public class RepoController {
	
	final static Logger logger = LoggerFactory.getLogger(RepoController.class);
	
	@Autowired
	FileService service;
	
	@RequestMapping(value = "/uploadAudioFile", method = RequestMethod.POST)
	public @ResponseBody ResponseObject uploadAudio(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileException {
		logger.info("uploadAudioFile Request");
		return service.uploadAudio(file);
	}

	@RequestMapping(value = "/downloadAudioFile/{id}", method = RequestMethod.GET)
	public @ResponseBody void downloadAudio(HttpServletResponse response,@PathVariable("id") String  id) throws IOException, FileNotFoundException {
		logger.info("Download Request for: "+id);
		service.downloadAudio(response,id);
	}
	
	@RequestMapping(value="/dir",method=RequestMethod.GET)
	public @ResponseBody File[] getDirectory() throws IOException{
		return service.getDirectory();
	} 
}
