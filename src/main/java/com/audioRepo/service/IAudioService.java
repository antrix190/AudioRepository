package com.audioRepo.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.audioRepo.entity.ResponseObject;

public interface IAudioService {
	
	ResponseObject uploadAudio(MultipartFile File);
	
	ResponseObject downloadAudio(	HttpServletResponse response,String id) throws IOException;
	
	Map<Long,String> getDirectory();
}
