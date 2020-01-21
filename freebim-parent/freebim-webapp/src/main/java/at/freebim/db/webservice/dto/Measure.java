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
package at.freebim.db.webservice.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.neo4j.ogm.annotation.Relationship;

import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.webservice.DtoHelper;
import at.freebim.db.webservice.dto.rel.Rel;
import at.freebim.db.webservice.dto.rel.ValueListRel;

/**
 * DTO of a {@link at.freebim.db.domain.Measure}. The class extends
 * {@link Base}.
 * 
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.webservice.dto.Base
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class Measure extends Base<at.freebim.db.domain.Measure> {

	/**
	 * The helper class.
	 */
	private DtoHelper dtoHelper;

	/**
	 * Determines if results should be loaded from the database.
	 */
	private boolean fetch;

	/**
	 * Creates a new instance.
	 * 
	 * @param node      The original node.
	 * @param dtoHelper The helper.
	 * @param fetch     If set to <code>true</code> all referenced nodes will be
	 *                  fetched too. If set to <code>false</code> only relations to
	 *                  referenced nodes will be returned.
	 */
	public Measure(at.freebim.db.domain.Measure node, DtoHelper dtoHelper, boolean fetch) {
		super(node, dtoHelper);
		this.dtoHelper = dtoHelper;
		this.fetch = fetch;
	}

	/**
	 * Get the local name.
	 * 
	 * @return The name.
	 */
	@XmlElement
	public String getName() {
		return this.dtoHelper.getString(node.getName());
	}

	/**
	 * Set the local name.
	 * 
	 * @param name The local name to set.
	 */
	public void setName(String name) {
		node.setName(name);
	}

	/**
	 * Get the local description.
	 * 
	 * @return The local description.
	 */
	@XmlElement
	public String getDesc() {
		return this.dtoHelper.getString(node.getDesc());
	}

	/**
	 * Set the local description.
	 * 
	 * @param desc The local description to set.
	 */
	public void setDesc(String desc) {
		node.setDesc(desc);
	}

	/**
	 * Get the description in international English.
	 * 
	 * @return The description in international English.
	 */
	@XmlElement
	public String getDescEn() {
		return this.dtoHelper.getString(node.getDescEn());
	}

	/**
	 * Set the description in international English.
	 * 
	 * @param desc The description to set (in international English).
	 */
	public void setDescEn(String desc) {
		node.setDescEn(desc);
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
	 * @param name The name to set (in international English).
	 */
	public void setNameEn(String name) {
		node.setNameEn(name);
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
	 * Get the referenced {@link Unit} (only if <code>fetch</code> is set to
	 * <code>true</code>).
	 * 
	 * @return The {@link Unit}
	 */
	@XmlElement
	public Unit getUnit() {
		if (this.fetch) {
			Iterable<OfUnit> iterable = this.node.getUnit();
			if (iterable != null) {
				Iterator<OfUnit> iter = iterable.iterator();
				if (iter != null && iter.hasNext()) {
					OfUnit rel = iter.next();
					BaseNode node = this.dtoHelper.getRelatedNode(this.node, rel, Relationship.OUTGOING);
					Unit u = new Unit((at.freebim.db.domain.Unit) node, this.dtoHelper);
					return u;
				}
			}
		}
		return null;
	}

	/**
	 * Get the referenced {@link Unit} relation. (only if <code>fetch</code> is set
	 * to <code>false</code>).
	 * 
	 * @return The {@link Unit} relation.
	 */
	@XmlElement
	public Rel getUnitRelation() {
		if (!this.fetch && node.getUnit() != null) {
			Iterable<OfUnit> i = node.getUnit();
			Iterator<OfUnit> iter = i.iterator();
			if (iter.hasNext()) {
				OfUnit r = iter.next();
				at.freebim.db.domain.Unit u = (at.freebim.db.domain.Unit) this.dtoHelper.getRelatedNode(node, r,
						Relationship.OUTGOING);
				if (u != null) {
					return new Rel(u.getUuid(), r.getInfo(), this.dtoHelper);
				}
			}
		}
		return null;
	}

	/**
	 * Get the referenced {@link DataType} (only if <code>fetch</code> is set to
	 * <code>true</code>).
	 * 
	 * @return the {@link DataType}.
	 */
	@XmlElement
	public DataType getDataType() {
		if (this.fetch) {
			Iterable<OfDataType> iterable2 = this.node.getDataType();
			if (iterable2 != null) {
				Iterator<OfDataType> iter = iterable2.iterator();
				if (iter != null && iter.hasNext()) {
					OfDataType rel = iter.next();
					BaseNode node = this.dtoHelper.getRelatedNode(this.node, rel, Relationship.OUTGOING);
					DataType dt = new DataType((at.freebim.db.domain.DataType) node, this.dtoHelper);
					return dt;
				}
			}
		}
		return null;
	}

	/**
	 * Get the referenced {@link DataType} relation. (only if <code>fetch</code> is
	 * set to <code>false</code>).
	 * 
	 * @return The {@link DataType} relation.
	 */
	@XmlElement
	public Rel getDataTypeRelation() {
		if (!this.fetch && node.getDataType() != null) {
			Iterable<OfDataType> i = node.getDataType();
			Iterator<OfDataType> iter = i.iterator();
			if (iter.hasNext()) {
				OfDataType r = iter.next();
				at.freebim.db.domain.DataType dt = (at.freebim.db.domain.DataType) this.dtoHelper.getRelatedNode(node,
						r, Relationship.OUTGOING);
				if (dt != null) {
					return new Rel(dt.getUuid(), r.getInfo(), this.dtoHelper);
				}
			}
		}
		return null;
	}

	/**
	 * Get the referenced ValueList relations (only if <code>fetch</code> is set to
	 * <code>false</code>).
	 * 
	 * @return the ValueList relation.
	 */
	@XmlElement
	public List<ValueListRel> getValueListRelation() {
		if (!this.fetch && node.getValue() != null) {
			List<ValueListRel> res = new ArrayList<ValueListRel>();
			Iterable<HasValue> i = node.getValue();
			Iterator<HasValue> iter = i.iterator();
			if (iter.hasNext()) {
				HasValue r = iter.next();
				at.freebim.db.domain.ValueList vl = (at.freebim.db.domain.ValueList) this.dtoHelper.getRelatedNode(node,
						r, Relationship.OUTGOING);
				if (vl != null) {
					ValueListRel vlr = new ValueListRel(vl.getUuid(), r.getComponentUuid(), r.getInfo(),
							this.dtoHelper);
					res.add(vlr);
				}
			}
			return res;
		}
		return null;
	}

}
