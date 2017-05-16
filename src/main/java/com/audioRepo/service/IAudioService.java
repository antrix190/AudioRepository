package com.audioRepo.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.audioRepo.entity.ResponseObject;
import com.audioRepo.exception.FileNotFoundException;
import com.audioRepo.exception.InvalidFileException;

public interface IAudioService {
	
	ResponseObject uploadAudio(MultipartFile File) throws InvalidFileException, IOException;
	
	ResponseObject downloadAudio(	HttpServletResponse response,String id) throws IOException, FileNotFoundException;
	
	Map<String, String> getDirectory() throws IOException;
}
