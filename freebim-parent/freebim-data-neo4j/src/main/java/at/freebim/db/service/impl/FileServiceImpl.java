/******************************************************************************
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import at.freebim.db.service.FileService;

/**
 * The service for things regarding files.
 * This service implements {@link FileService}.
 * 
 * @see at.freebim.db.service.FileService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class FileServiceImpl implements FileService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	/**
	 * The maximum size of a file.
	 */
	private static final long MAX_FILE_SIZE = 1024L * 1024L * 2L;
	
	/**
	 * The directory where uploaded files are stored.
	 * This value is set by a property in the freebim.properties file.
	 */
	@Value("${props.file_upload_dir}")
	protected String uploadDir;

	/* (non-Javadoc)
	 * @see at.freebim.db.service.FileService#uploadFile(org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public String uploadFile(MultipartFile file) {
		
        if (!file.isEmpty()) {
        	
        	logger.info("file ContentType=[{}]", file.getContentType());
        	logger.info("file Size=[{}]", file.getSize());
        	if (file.getSize() > MAX_FILE_SIZE) {
        		logger.error("file Size=[{}] not supported, max file size = [{}].", file.getSize(), MAX_FILE_SIZE);
        		return null;
        	}
	    	String fileName = UUID.randomUUID().toString();
	    	
	    	String name = this.uploadDir + fileName;
	    	
	        try {
	        	File f = new File(this.uploadDir);
	        	if (f.mkdirs()) {
	        		logger.info("upload directory [{}] created. ", this.uploadDir);
	        	}
	        	
	        	f = new File(name);
	        	
	        	logger.info("writing file to [{}] ... ", f.getAbsolutePath());
	            byte[] bytes = file.getBytes();
	            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
	            stream.write(bytes);
	            stream.close();

	            logger.info("[{}] saved. ", f.getAbsolutePath());
	            return fileName;
	            
			} catch (Exception e) {
				logger.error("Error uploading file to dir=[" + this.uploadDir + "]", e);
			}
        }
        return null;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.FileService#getFile(java.lang.String)
	 */
	@Override
	public FileSystemResource getFile(String file) {
		
		logger.info("getImage dir=[{}], file=[{}]", this.uploadDir, file);
		try {
			File f = new File(this.uploadDir + file);
			if (f.exists() && f.canRead()) {
				return new FileSystemResource(f);
			}
			return new FileSystemResource(this.uploadDir + "nd.png");
		} catch (Exception e) {
			return null;
		}
	}

	
}
