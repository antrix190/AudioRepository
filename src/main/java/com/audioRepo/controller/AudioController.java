package com.audioRepo.controller;

import java.io.IOException;
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

import com.audioRepo.entity.ResponseObject;
import com.audioRepo.service.IAudioService;

@RestController
public class AudioController {
	
	final static Logger logger = LoggerFactory.getLogger(AudioController.class);
	
	@Autowired
	IAudioService audioService;
	
	@RequestMapping(value="/hello",method = RequestMethod.GET)
	@ResponseBody
	public String Hello()
	{
		return "Welcome to Sonetel Audio Repository";
	}
	
	@RequestMapping(value = "/uploadAudioFile", method = RequestMethod.POST)
	public @ResponseBody ResponseObject uploadAudio(@RequestParam("file") MultipartFile file) throws IOException {
		logger.info("uploadAudioFile Request");
		return audioService.uploadAudio(file);
	}

	@RequestMapping(value = "/downloadAudioFile/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseObject uploadAudio(HttpServletResponse response,@PathVariable("id") String  id) throws IOException {
		logger.info("Download Request for: "+id);
		return audioService.downloadAudio(response,id);
	}
	
	@RequestMapping(value="/dir",method=RequestMethod.GET)
	public @ResponseBody Map<Long,String> getDirectory(){
		return audioService.getDirectory();
	} 
}
