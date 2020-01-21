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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.freebim.db.service.AsyncNgramCreator;
import at.freebim.db.service.AsyncNgramLoader;

/**
 * The service to load persisted nodes back in the {@link AsyncNgramCreator}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.AsyncNgramCreator
 * @see at.freebim.db.service.impl.AsyncNgramCreatorImpl
 */
@Service
public class AsyncNgramLoaderImpl implements AsyncNgramLoader {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AsyncNgramLoaderImpl.class);

	/**
	 * The service for creating the n-grams.
	 */
	@Autowired
	private AsyncNgramCreator asyncNgramCreator;

	/**
	 * Read the data for the n-grams, that has been persisted, from the file back in
	 * the {@link AsyncNgramCreator}.
	 */
	@PostConstruct
	public void init() {
		try {
			FileReader r = new FileReader(AsyncNgramLoader.persitentQueueFile);
			LineNumberReader lnr = new LineNumberReader(r);
			String line;
			try {
				while ((line = lnr.readLine()) != null) {
					Long id;
					try {
						id = Long.parseLong(line);
						this.asyncNgramCreator.add(id);
					} catch (NumberFormatException e) {
						logger.error("Can't parse Long from [{}]", line);
					}
				}
				lnr.close();
				FileWriter w = new FileWriter(AsyncNgramLoader.persitentQueueFile);
				w.write("");
				w.close();
			} catch (IOException e) {
				logger.error("Error reading persistent file [{}].", AsyncNgramLoader.persitentQueueFile);
			}
		} catch (FileNotFoundException e) {
			logger.info("No file [{}] found.", AsyncNgramLoader.persitentQueueFile);
		}
	}

}
