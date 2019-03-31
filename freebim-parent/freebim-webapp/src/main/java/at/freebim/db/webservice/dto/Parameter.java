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

import at.freebim.db.domain.base.ParameterType;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.OfDiscipline;
import at.freebim.db.webservice.DtoHelper;
import at.freebim.db.webservice.dto.rel.OrderedRel;
import at.freebim.db.webservice.dto.rel.Rel;

/**
 * DTO of a {@link at.freebim.db.domain.Parameter}.
 * The class extends {@link StatusBase}.
 * 
 * @see at.freebim.db.domain.Parameter
 * @see at.freebim.db.webservice.dto.StatusBase
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class Parameter extends StatusBase<at.freebim.db.domain.Parameter> {

	/**
	 * Determines if a result should be loaded from the database.
	 */
	private boolean fetch;

	/**
	 * Creates a new instance.
	 * @param param The original Parameter.
	 * @param dtoHelper The helper.
	 * @param fetch If set to <code>true</code> all referenced nodes will be fetched too. If set to <code>false</code> only relations to referenced nodes will be returned.
	 */
	public Parameter(at.freebim.db.domain.Parameter param, DtoHelper dtoHelper, boolean fetch) {
		super(param, dtoHelper);
		this.fetch = fetch;
	}

	/**
	 * Get the code.
	 * @return The code.
	 */
	public String getCode() {
		return this.dtoHelper.getString(node.getCode());
	}

	/**
	 * Set the code.
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		node.setCode(code);
	}

	/**
	 * Get the bsDD-Guid.
	 * @return The bsDD-Guid.
	 */
	public String getBsddGuid() {
		return this.dtoHelper.getString(node.getBsddGuid());
	}

	/**
	 * Set the bsDD-Guid.
	 * @param guid The bsDD-Guid to set.
	 */
	public void setBsddGuid(String guid) {
		node.setBsddGuid(guid);
	}

	/**
	 * Get the local name.
	 * @return The name.
	 */
	public String getName() {
		return this.dtoHelper.getString(node.getName());
	}

	/** 
	 * Set the local name.
	 * @param name The local name to set.
	 */
	public void setName(String name) {
		node.setName(name);
	}

	/**
	 * Get the local description.
	 * @return The local description.
	 */
	public String getDesc() {
		return this.dtoHelper.getString(node.getDesc());
	}

	/**
	 * Set the local description.
	 * @param desc The local description to set.
	 */
	public void setDesc(String desc) {
		node.setDesc(desc);
	}

	/**
	 * Get the name in international English.
	 * @return the name in international English.
	 */
	public String getNameEn() {
		return this.dtoHelper.getString(node.getNameEn());
	}

	/**
	 * Set the name in international English.
	 * @param nameEn The name to set (in international English).
	 */
	public void setNameEn(String nameEn) {
		node.setNameEn(nameEn);
	}

	/**
	 * Get the description in international English.
	 * @return The description in international English.
	 */
	public String getDescEn() {
		return this.dtoHelper.getString(this.node.getDescEn());
	}

	/**
	 * Set the description in international English.
	 * @param descEn The description to set (in international English).
	 */
	public void setDescEn(String descEn) {
		this.node.setDescEn(descEn);
	}

	/**
	 * Get the default value for this Parameter.
	 * @return The default value.
	 */
	public String getDefaultString() {
		return this.dtoHelper.getString(node.getDefaultString());
	}

	/**
	 * Set the default value for this Parameter.
	 * @param defaultString The default value to set.
	 */
	public void setDefaultString(String defaultString) {
		node.setDefaultString(defaultString);
	}

	/**
	 * Get the referenced Discipline 
	 * (only if <code>fetch</code> is set to <code>true</code>).
	 * @return The Discipline.
	 */
	public List<Discipline> getDisciplines() {
		if (this.fetch && node.getDiscipline() != null) {
			List<Discipline> disciplines = new ArrayList<Discipline>();
			Iterable<OfDiscipline> i = node.getDiscipline();
			Iterator<OfDiscipline> iter = i.iterator();
			while (iter.hasNext()) {
				OfDiscipline r = iter.next();
				at.freebim.db.domain.Discipline dis = (at.freebim.db.domain.Discipline) this.dtoHelper.getRelatedNode(node, r, Direction.OUTGOING);
				if (dis != null) {
					disciplines.add(new Discipline(dis, this.dtoHelper));
				}
			}
			return disciplines;
		}
		return null;
	}

	/**
	 * Get the referenced Discipline relations
	 * (only if <code>fetch</code> is set to <code>false</code>).
	 * @return The Disciplines.
	 */
	public List<Rel> getDisciplineRelations() {
		if (!this.fetch && node.getDiscipline() != null) {
			List<Rel> disciplines = new ArrayList<Rel>();
			Iterable<OfDiscipline> i = node.getDiscipline();
			Iterator<OfDiscipline> iter = i.iterator();
			while (iter.hasNext()) {
				OfDiscipline r = iter.next();
				at.freebim.db.domain.Discipline dis = (at.freebim.db.domain.Discipline) this.dtoHelper.getRelatedNode(node, r, Direction.OUTGOING);
				if (dis != null) {
					disciplines.add(new Rel(dis.getUuid(), r.getInfo(), this.dtoHelper));
				}
			}
			return disciplines;
		}
		return null;
	}

	/**
	 * Get the referenced {@link Measure}s 
	 * (only if <code>fetch</code> is set to <code>true</code>).
	 * @return The {@link Measure}s.
	 */
	public List<Measure> getMeasures() {
		if (this.fetch && node.getMeasures() != null) {
			List<Measure> measures = new ArrayList<Measure>();
			Iterable<HasMeasure> i = node.getMeasures();
			Iterator<HasMeasure> iter = i.iterator();
			while (iter.hasNext()) {
				HasMeasure r = iter.next();
				at.freebim.db.domain.Measure m = (at.freebim.db.domain.Measure) this.dtoHelper.getRelatedNode(node, r, Direction.OUTGOING);
				if (m != null) {
					measures.add(new Measure(m, this.dtoHelper, this.fetch));
				}
			}
			return measures;
		}
		return null;
	}

	/**
	 * Get the referenced {@link Measure} relations
	 * (only if <code>fetch</code> is set to <code>false</code>).
	 * @return The {@link Measure} relations.
	 */
	public List<OrderedRel> getMeasureRelations() {
		if (!this.fetch && node.getMeasures() != null) {
			List<OrderedRel> measures = new ArrayList<OrderedRel>();
			Iterable<HasMeasure> i = node.getMeasures();
			Iterator<HasMeasure> iter = i.iterator();
			while (iter.hasNext()) {
				HasMeasure r = iter.next();
				at.freebim.db.domain.Measure m = (at.freebim.db.domain.Measure) this.dtoHelper.getRelatedNode(node, r, Direction.OUTGOING);
				if (m != null) {
					measures.add(new OrderedRel(m.getUuid(), r.getOrdering(), r.getInfo(), this.dtoHelper));
				}
			}
			return measures;
		}
		return null;
	}


	/**
	 * Get the ParameterType.
	 * @return The ParameterType.
	 */
	public ParameterType getPtype() {
		return node.getPtype();
	}

	/**
	 * Set the ParameterType.
	 * @param ptype The ParameterType to set.
	 */
	public void setPtype(ParameterType ptype) {
		node.setPtype(ptype);
	}
}
