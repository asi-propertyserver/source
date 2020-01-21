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
 * along with this program.  If not, see
 *{@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.webservice.dto;

import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.webservice.DtoHelper;
import at.freebim.db.webservice.dto.rel.OrderedRel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.neo4j.ogm.annotation.Relationship;

/**
 * DTO of a {@link at.freebim.db.domain.ValueList}. The class extends
 * {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.ValueList
 * @see at.freebim.db.webservice.dto.StatusBase
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ValueList extends StatusBase<at.freebim.db.domain.ValueList> {

	/**
	 * Determines if the nodes should be loaded from the database.
	 */
	private boolean fetch;

	/**
	 * Creates a new instance.
	 * 
	 * @param node      The original node.
	 * @param dtoHelper The helper.
	 * @param fetch     determines if the nodes should be loaded from the database
	 */
	public ValueList(at.freebim.db.domain.ValueList node, DtoHelper dtoHelper, boolean fetch) {
		super(node, dtoHelper);
		this.fetch = fetch;
	}

	/**
	 * Get the local name.
	 * 
	 * @return The name.
	 */
	@XmlElement
	public String getName() {
		return this.dtoHelper.getString(this.node.getName());
	}

	/**
	 * Set the local name.
	 * 
	 * @param name The local name to set.
	 */
	public void setName(String name) {
		this.node.setName(name);
	}

	/**
	 * Get the bsDD-Guid.
	 * 
	 * @return The bsDD-Guid.
	 */
	@XmlElement
	public String getBsddGuid() {
		return this.dtoHelper.getString(this.node.getBsddGuid());
	}

	/**
	 * Set the bsDD-Guid.
	 * 
	 * @param guid The bsDD-Guid to set.
	 */
	public void setBsddGuid(String guid) {
		this.node.setBsddGuid(guid);
	}

	/**
	 * Get the name in international English.
	 * 
	 * @return the name in international English.
	 */
	@XmlElement
	public String getNameEn() {
		return this.dtoHelper.getString(node.getNameEn());
	}

	/**
	 * Set the name in international English.
	 * 
	 * @param nameEn The name to set (in international English).
	 */
	public void setNameEn(String nameEn) {
		node.setNameEn(nameEn);
	}

	/**
	 * Get the referenced ValueListEntry relations. (only if <code>fetch</code> is
	 * set to <code>false</code>).
	 * 
	 * @return the ValueListEntry relations.
	 */
	@XmlElement
	public List<OrderedRel> getValueListEntries() {
		if (!this.fetch) {
			Iterable<HasEntry> i = this.node.getEntries();
			if (i != null) {
				Iterator<HasEntry> iter = i.iterator();
				if (iter != null) {
					List<OrderedRel> entries = new ArrayList<OrderedRel>();
					while (iter.hasNext()) {
						HasEntry rel = iter.next();
						at.freebim.db.domain.ValueListEntry entry = (at.freebim.db.domain.ValueListEntry) this.dtoHelper
								.getRelatedNode(this.node, rel, Relationship.OUTGOING);
						OrderedRel r = new OrderedRel(entry.getUuid(), rel.getOrdering(), rel.getInfo(),
								this.dtoHelper);
						entries.add(r);
					}
					return entries;
				}
			}
		}
		return null;
	}

	/**
	 * Get the referenced ValueListEntry objects. (only if <code>fetch</code> is set
	 * to <code>true</code>).
	 * 
	 * @return the ValueListEntry objects.
	 */
	@XmlElement
	public List<ValueListEntry> getEntries() {
		if (this.fetch) {
			Iterable<HasEntry> i = this.node.getEntries();
			if (i != null) {
				Iterator<HasEntry> iter = i.iterator();
				if (iter != null) {
					List<ValueListEntry> entries = new ArrayList<ValueListEntry>();
					List<HasEntry> rels = new ArrayList<HasEntry>();
					while (iter.hasNext()) {
						HasEntry rel = iter.next();
						rels.add(rel);
					}
					Collections.sort(rels, new Comparator<HasEntry>() {

						@Override
						public int compare(HasEntry o1, HasEntry o2) {
							return o1.getOrdering() - o2.getOrdering();
						}

					});
					for (HasEntry rel : rels) {
						at.freebim.db.domain.ValueListEntry entry = (at.freebim.db.domain.ValueListEntry) this.dtoHelper
								.getRelatedNode(this.node, rel, Relationship.OUTGOING);

						entries.add(new ValueListEntry(entry, this.dtoHelper));
					}
					return entries;
				}
			}
		}
		return null;
	}
}
