/**
 * 
 */
package com.repo.Validation;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.repo.exception.InvalidFileException;

/**
 * @author antariksh.singh
 *
 * May 16, 2017
 */
@Component
public class FileValidator {
	
	public static List<String> allowedExtension;

	@Value("#{'${file.extension}'.split(',')}")
	public void setAllowedExtension(List<String> extension) {
		FileValidator.allowedExtension = extension;
	}

	public static boolean isValid(MultipartFile file) throws InvalidFileException{
		if (!file.isEmpty()){
			String ext = FilenameUtils.getExtension(file.getOriginalFilename());
			if(allowedExtension.contains(ext))
				return true;
			else
				throw new InvalidFileException("Invalid file type");
		}
		else{
			throw new InvalidFileException("File can't be empty");
		}
	}
}
