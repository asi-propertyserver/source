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
package at.freebim.db.webservice;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;
import static org.neo4j.ogm.annotation.Relationship.UNDIRECTED;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.NodeIdentifyable;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.service.DateService;
import at.freebim.db.service.RelationService;
import java.util.Iterator;
import org.neo4j.ogm.annotation.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is a helper class for all DTO.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class DtoHelper {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DtoHelper.class);

	/**
	 * This service handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * This service handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * Fully load a node in a {@link BaseRel}. Which site is loaded is defined by
	 * the last parameter.
	 * 
	 * @param src the node that is used as starting point if the relation direction
	 *            is bidirectional
	 * @param rel the {@link BaseRel}
	 * @param dir the direction of the relation
	 * @return the fully loaded node
	 */
	public <R extends BaseRel<? extends NodeIdentifyable, ? extends NodeIdentifyable>> BaseNode getRelatedNode(
			BaseNode src, R rel, String dir) {
		if (src != null && rel != null) {
			BaseNode child = null;
			if (rel != null) {
				switch (dir) {
				case INCOMING:
					child = (BaseNode) rel.getN1();
					break;
				case OUTGOING:
					child = (BaseNode) rel.getN2();
					break;
				case UNDIRECTED:
					if (src.getNodeId().equals(rel.getN1().getNodeId())) {
						child = (BaseNode) rel.getN2();
					} else if (src.getNodeId().equals(rel.getN2().getNodeId())) {
						child = (BaseNode) rel.getN1();
					}
					break;
				default:
					logger.debug("Direction of relationship not found");
				}
				return this.relationService.fetch(child, child.getClass());
			}
		}
		return null;
	}

	/**
	 * Replace newline or carriage return with newline.
	 * 
	 * @param s the string
	 * @return the string with the replaced strings or <code>null</code> if the
	 *         string length was 0
	 */
	public String getString(String s) {
		if (s != null) {
			s = s.trim().replaceAll("[\r|\n]", "\n");
			return ((s.length() > 0) ? s : null);
		} else {
			return null;
		}
	}

	/**
	 * Check if a node owner belongs to the provided
	 * {@link at.freebim.db.domain.Library}.
	 * 
	 * @param owner      the {@link HierarchicalBaseNode}
	 * @param ifcLibrary the {@link at.freebim.db.domain.Library}
	 * @return true if the owner belongs to the library false otherwise
	 */
	public boolean belongsToLibrary(HierarchicalBaseNode owner, at.freebim.db.domain.Library ifcLibrary) {
		if (ifcLibrary != null) {
			if (owner != null) {
				if (ifcLibrary.getNodeId().equals(owner.getNodeId())) {
					return true;
				}
				Iterable<ParentOf> iterable = owner.getParents();
				if (iterable != null) {
					Iterator<ParentOf> iter = iterable.iterator();
					while (iter.hasNext()) {
						ParentOf rel = iter.next();
						HierarchicalBaseNode parent = (HierarchicalBaseNode) this.getRelatedNode(owner, rel,
								Relationship.INCOMING);
						if (this.belongsToLibrary(parent, ifcLibrary)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Get the uuid of the library that references the provided node.
	 * 
	 * @param node the node
	 * @return the uuid of the {@link at.freebim.db.domain.Library}
	 */
	public String getLibraryId(UuidIdentifyable node) {
		if (node != null) {
			Iterable<References> iterable = node.getRef();
			if (iterable != null) {
				Iterator<References> iter = iterable.iterator();
				if (iter != null) {
					while (iter.hasNext()) {
						References ref = iter.next();
						at.freebim.db.domain.Library lib = (at.freebim.db.domain.Library) this.getRelatedNode(node, ref,
								Relationship.OUTGOING);
						if (lib != null) {
							return lib.getUuid();
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Check if a node is still valid by checking its lifetime time stamps.
	 * 
	 * @param node the node that will be checked if it is still valid
	 * @return true if valid false otherwise
	 */
	public boolean validNode(BaseNode node) {
		if (node != null) {
			if (LifetimeBaseNode.class.isAssignableFrom(node.getClass())) {
				Long now = this.dateService.getMillis();
				LifetimeBaseNode ltbn = (LifetimeBaseNode) node;
				if (ltbn.getValidFrom() < now) {
					if (ltbn.getValidTo() == null || ltbn.getValidTo() >= now) {
						return true;
					}
				}
				return false;
			}
			return true;
		}
		return false;
	}

}
