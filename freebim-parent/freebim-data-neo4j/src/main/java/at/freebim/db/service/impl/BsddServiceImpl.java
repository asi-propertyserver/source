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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.BsddObject;
import at.freebim.db.service.BsddService;
import at.freebim.db.service.RelationService;

/**
 * The service for bsdd things. It extends {@link BsddService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.BsddService
 */
@Service
public class BsddServiceImpl implements BsddService {

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.BsddService#didBsddGuidChange(at.freebim.db.domain.base
	 * .BsddObject)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean didBsddGuidChange(BsddObject obj) {
		if (obj != null) {
			BaseNode node = this.relationService.getTemplate().load(BaseNode.class, obj.getNodeId());
			if (node != null && BsddObject.class.isAssignableFrom(node.getClass())) {
				BsddObject savedObj = (BsddObject) node;
				if (savedObj.getBsddGuid() != null) {
					if (!savedObj.getBsddGuid().equals(obj.getBsddGuid())) {
						return true;
					}
				} else {
					if (obj.getBsddGuid() != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
