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
package at.freebim.db.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for file handling.
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface FileService {

	/**
	 * Upload a file to a specified directory.
	 *
	 * @param file The file.
	 * @return Name of the saved file.
	 */
	@Secured({ "ROLE_EDIT", "ROLE_ADMIN" })
	String uploadFile(MultipartFile file);

	/**
	 * Get the file with the provided file name.
	 *
	 * @param file the filename
	 * @return the {@link FileSystemResource} representing the file
	 */
	FileSystemResource getFile(String file);
}
