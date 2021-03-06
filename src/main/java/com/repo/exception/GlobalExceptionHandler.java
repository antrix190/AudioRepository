package com.repo.exception;

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

import com.repo.entity.ResponseObject;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	ResponseObject errorInfo;
	
	@ExceptionHandler(FileNotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> FileNotFoundExceptionHandler(FileNotFoundException ex) {
		logger.info("Caught FileNotFoundException");
	    return getResponseEntity(Boolean.FALSE,ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidFileException.class)
	@ResponseBody
	public ResponseEntity<Object> InvalidFileExceptionHandler(InvalidFileException ex) {
		logger.error("Caught InvalidFileException");
	    return getResponseEntity(Boolean.FALSE,ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> exceptionHandler(HttpServletRequest req, Exception ex) {
		logger.error("Caught Exception type {}, message {}",ex.getClass().getSimpleName(),ex.getMessage());
	    return getResponseEntity(Boolean.FALSE,ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * @param status
	 * @param code
	 * @param message
	 * @param httpStatus
	 * @return
	 */
	private ResponseEntity<Object> getResponseEntity(Boolean status, String message,HttpStatus httpStatus) {
		// TODO Auto-generated method stub
		errorInfo = new ResponseObject(status,httpStatus.value(),message);
		return new ResponseEntity<Object>(errorInfo,httpStatus);
	}
	 }
