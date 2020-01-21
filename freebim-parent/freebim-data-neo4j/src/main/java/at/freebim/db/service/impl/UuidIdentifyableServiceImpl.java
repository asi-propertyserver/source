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

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.UuidIdentifyableService;

/**
 * This service defines the basics for all services that define functionality
 * for a class <b>T</b> that extends {@link UuidIdentifyable}. This service
 * extends {@link LifetimeBaseNodeServiceImpl} and implements
 * {@link UuidIdentifyableService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.UuidIdentifyable
 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl
 * @see at.freebim.db.service.UuidIdentifyableService
 */
public class UuidIdentifyableServiceImpl<T extends UuidIdentifyable> extends LifetimeBaseNodeServiceImpl<T>
		implements UuidIdentifyableService<T> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UuidIdentifyableServiceImpl.class);

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#filterBeforeInsert(at.
	 * freebim.db.domain.base.LifetimeBaseNode)
	 */
	@Override
	public T filterBeforeInsert(T node) {
		try {
			setUuid(node);
		} catch (Exception e) {
			logger.error("Error in setUuid:", e);
		}
		return super.filterBeforeInsert(node);
	}

	/**
	 * Set the uuid for the provided node.
	 *
	 * @param node the node for which the uuid will be set
	 */
	private void setUuid(T node) {

		if (node != null) {
			if (node.getUuid() == null || node.getUuid().length() == 0) {
				logger.debug("setUuid");
				String uuid;
				do {
					uuid = UUID.randomUUID().toString();
				} while (this.getByUuid(uuid) != null);

				node.setUuid(uuid);
				logger.debug("UUID: {} set to node {}", uuid, node.getNodeId());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.BaseNodeServiceImpl#filterBeforeSave(at.freebim.db
	 * .domain.base.BaseNode)
	 */
	@Override
	public T filterBeforeSave(T node) {
		setUuid(node);
		return super.filterBeforeSave(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.UuidIdentifyableService#getByUuid(java.lang.String)
	 */
	@Override
	public UuidIdentifyable getByUuid(String uuid) {
		return this.relationService.getByUuid(uuid);
	}

}
