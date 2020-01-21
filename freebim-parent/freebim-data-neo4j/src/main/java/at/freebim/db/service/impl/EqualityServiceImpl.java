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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.service.EqualityService;

/**
 * The service for equality. It extends {@link EqualityService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.EqualityService
 */
@Service
public class EqualityServiceImpl implements EqualityService {

	/*
	 * (non-Javadoc)
	 *
	 * @see at.freebim.db.service.EqualityService#relatedEquals(java.lang.Iterable,
	 * at.freebim.db.domain.base.NodeIdentifyable)
	 */
	@Override
	public <T extends NodeIdentifyable, R extends BaseRel<?, T>> boolean relatedEquals(Iterable<R> iterable, T obj) {
		if (iterable != null) {
			final Iterator<R> iter = iterable.iterator();
			if (iter != null && iter.hasNext()) {
				final R rel = iter.next();
				final T val = rel.getN2();
				if (val != null && obj != null) {
					if (val.getNodeId().equals(obj.getNodeId())) {
						return true;
					} else {
						return false;
					}
				} else {
					return (val == null && obj == null);
				}
			} else {
				return (obj == null);
			}
		} else {
			return (obj == null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.EqualityService#relatedEquals(java.lang.Iterable,
	 * java.util.List)
	 */
	@Override
	public <T extends NodeIdentifyable, R extends BaseRel<?, T>> boolean relatedEquals(final Iterable<R> iterable,
			final List<T> list) {

		if (iterable == null) {
			if (list != null)
				return false;
		} else {
			if (list == null)
				return false;
			else {
				final ArrayList<Long> rels = new ArrayList<>();
				for (T item : list) {
					final Long relatedNodeId = item.getNodeId();
					rels.add(relatedNodeId);
				}
				final Iterator<R> otherIter = iterable.iterator();
				while (otherIter.hasNext()) {
					final R rel = otherIter.next();
					final Long relatedNodeId = rel.getN2().getNodeId();
					if (!rels.contains(relatedNodeId))
						return false;
					rels.remove(relatedNodeId);
				}
				if (rels.size() > 0)
					return false;
			}
		}
		return true;
	}

}
