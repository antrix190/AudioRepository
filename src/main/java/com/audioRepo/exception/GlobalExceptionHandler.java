package com.audioRepo.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.audioRepo.entity.ResponseObject;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	ResponseObject errorInfo;
	
	@ExceptionHandler(FileNotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> FileNotFoundExceptionHandler(FileNotFoundException ex) {
	    ResponseObject errorInfo = new ResponseObject();
	    errorInfo.setMessage(ex.getMessage());
	    errorInfo.setStatus(Boolean.FALSE);
	    errorInfo.setCode(HttpStatus.NOT_FOUND.value());
	    return new ResponseEntity<Object>(errorInfo, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidFileException.class)
	@ResponseBody
	public ResponseEntity<Object> InvalidFileExceptionHandler(InvalidFileException ex) {
	    ResponseObject errorInfo = new ResponseObject();
	    errorInfo.setMessage(ex.getMessage());
	    errorInfo.setStatus(Boolean.FALSE);
	    errorInfo.setCode(HttpStatus.BAD_REQUEST.value());
	    return new ResponseEntity<Object>(errorInfo, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> exceptionHandler(HttpServletRequest req, Exception ex) {
	    ResponseObject errorInfo = new ResponseObject();
	    errorInfo.setMessage(ex.getMessage());
	    errorInfo.setStatus(Boolean.FALSE);
	    errorInfo.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    return new ResponseEntity<Object>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	 }
