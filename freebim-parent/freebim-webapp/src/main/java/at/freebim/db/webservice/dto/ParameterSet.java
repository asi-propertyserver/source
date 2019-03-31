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

import org.neo4j.graphdb.Direction;

import at.freebim.db.domain.base.ParameterSetType;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.webservice.DtoHelper;
import at.freebim.db.webservice.dto.rel.OrderedRel;
import at.freebim.db.webservice.dto.rel.Rel;


/**
 * DTO of a {@link at.freebim.db.domain.ParameterSet}.
 * The class extends {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.ParameterSet
 * @see at.freebim.db.webservice.dto.StatusBase
 *
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class ParameterSet extends StatusBase<at.freebim.db.domain.ParameterSet> {

	private boolean isIfcPropertySet;

	/**
	 * Creates a new instance.
	 * 
	 * @param pset The original ParameterSet.
	 * @param dtoHelper The helper.
	 */
	public ParameterSet(at.freebim.db.domain.ParameterSet pset, DtoHelper dtoHelper) {
		super(pset, dtoHelper);
	}

	/**
	 * Get the local name.
	 * 
	 * @return The name.
	 */
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
	 * Get the name in international English.
	 * 
	 * @return the name in international English.
	 */
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
	 * Get the referenced {@link Parameter} relations.
	 * 
	 * @return the {@link Parameter} relations.
	 */
	public List<OrderedRel> getParameters() {
		if (node.getParameters() != null) {
			List<OrderedRel> parameters = new ArrayList<OrderedRel>();
			Iterable<ContainsParameter> i = node.getParameters();
			Iterator<ContainsParameter> iter = i.iterator();
			while (iter.hasNext()) {
				ContainsParameter r = iter.next();
				at.freebim.db.domain.Parameter param = (at.freebim.db.domain.Parameter) this.dtoHelper.getRelatedNode(node, r, Direction.OUTGOING);
				if (param != null) {
					parameters.add(new OrderedRel(param.getUuid(), r.getOrdering(), r.getInfo(), this.dtoHelper));
				}
			}
			return parameters;
		}
		return null;
	}

	/**
	 * Get the referenced {@link Component} relations.
	 * 
	 * @return The {@link Component} relations.
	 */
	public List<Rel> getOwners() {
		if (node.getOwners() != null) {
			List<Rel> owners = new ArrayList<Rel>();
			Iterable<HasParameterSet> i = node.getOwners();
			Iterator<HasParameterSet> iter = i.iterator();
			while (iter.hasNext()) {
				HasParameterSet r = iter.next();
				at.freebim.db.domain.base.HierarchicalBaseNode owner = (at.freebim.db.domain.base.HierarchicalBaseNode) this.dtoHelper.getRelatedNode(node, r, Direction.INCOMING);
				if (owner != null) {
					owners.add(new Rel(owner.getUuid(), r.getInfo(), this.dtoHelper));
				}
			}
			return owners;
		}
		return null;
	}
	
	/**
	 * Get the ParamaterSetType.
	 * 
	 * @return The ParamaterSetType.
	 */
	public ParameterSetType getType() {
		return this.node.getType();
	}
	
	/**
	 * Set the ParamaterSetType.
	 * 
	 * @param psetType The ParamaterSetType to set.
	 */
	public void setType(ParameterSetType psetType) {
		this.node.setType(psetType);
	}

	/**
	 * Test if this ParameterSet is an original IFC PropertySet.
	 * 
	 * @return <code>true</code> if this ParamaterSet is an original IFC PropertySet.
	 */
	public boolean isIfcPropertySet() {
		return isIfcPropertySet;
	}

	/**
	 * Get the bsDD-Guid.
	 * 
	 * @return The bsDD-Guid.
	 */
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

}
