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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.Contributor;
import at.freebim.db.service.AdminService;
import net.spectroom.neo4j.backup.service.SpectroomBackupService;

/**
 * The service for admin specific tasks.
 * 
 * @see at.freebim.db.service.AdminService
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@Service
public class AdminServiceImpl implements AdminService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	/**
	 * The service that handles the backups.
	 */
	@Autowired
	private SpectroomBackupService spectroomBackupService;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	/**
	 * The directory in which the backup will be created.
	 * This directory is set in the freebim.properties file.
	 */
	@Value("${freebim.backup.dir}")
	private String freebimBackupDir;
	
	

	/* (non-Javadoc)
	 * @see at.freebim.db.service.AdminService#createBackup()
	 */
	@Override
	public boolean createBackup() {
		logger.info("createBackup ...");
		final File dir = new File(freebimBackupDir);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception  e) {
				logger.error("Error creating backup directory [{}].", freebimBackupDir);
				return false;
			}
		}
		if (!dir.canWrite()) {
			logger.error("Can't write to backup directory [{}].", freebimBackupDir);
			return false;
		}
		
		this.taskExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				long count = spectroomBackupService.createBackup(dir, Contributor.class.getPackage().getName());
				logger.info("createBackup finished, backup created of [{}] nodes and relationships.", count);
			}
		});
		
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.AdminService#getAvailableBackups()
	 */
	@Override
	public List<String> getAvailableBackups() {
		logger.info("getAvailableBackups ...");
		File dir = new File(freebimBackupDir);
		if (!dir.exists() || !dir.canRead() || !dir.isDirectory()) {
			logger.error("Error reading backup directory [{}].", freebimBackupDir);
			return null;
		}
		return this.spectroomBackupService.getAvailableBackups(dir);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.AdminService#restoreBackup(java.lang.String)
	 */
	@Override
	public boolean restoreBackup(final String backup) {
		logger.info("restoreBackup [{}] ...", backup);
		if (backup == null || backup.length() == 0) {
			logger.error("passed backup is empty.");
			return false;
		}
		final File dir = new File(freebimBackupDir);
		if (!dir.exists() || !dir.canRead() || !dir.isDirectory()) {
			logger.error("Error reading backup to restore backup from, directory [{}].", freebimBackupDir);
			return false;
		}
		
		this.taskExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				Map<Long, Long> relationshipIdMap = new HashMap<>();
				Map<Long, Long> nodeIdMap = new HashMap<>();
				File backupDir = new File(dir + File.separator + backup);
				try {
					spectroomBackupService.restoreBackup(backupDir, Contributor.class.getPackage().getName(), nodeIdMap, relationshipIdMap);
				} catch (Exception e) {
					logger.error("Error restoring backup: ", e);
				}
			}
		});

		return true;
	}
	
}
