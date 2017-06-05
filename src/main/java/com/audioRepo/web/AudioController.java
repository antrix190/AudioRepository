package com.audioRepo.web;

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

import com.audioRepo.entity.ResponseObject;
import com.audioRepo.exception.FileNotFoundException;
import com.audioRepo.exception.InvalidFileException;
import com.audioRepo.service.IAudioService;

@RestController
public class AudioController {
	
	final static Logger logger = LoggerFactory.getLogger(AudioController.class);
	
	@Autowired
	IAudioService audioService;
	
	@RequestMapping(value = "/uploadAudioFile", method = RequestMethod.POST)
	public @ResponseBody ResponseObject uploadAudio(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileException {
		logger.info("uploadAudioFile Request");
		return audioService.uploadAudio(file);
	}

	@RequestMapping(value = "/downloadAudioFile/{id}", method = RequestMethod.GET)
	public @ResponseBody void downloadAudio(HttpServletResponse response,@PathVariable("id") String  id) throws IOException, FileNotFoundException {
		logger.info("Download Request for: "+id);
		audioService.downloadAudio(response,id);
	}
	
	@RequestMapping(value="/dir",method=RequestMethod.GET)
	public @ResponseBody Map<String, String> getDirectory() throws IOException{
		return audioService.getDirectory();
	} 
}
