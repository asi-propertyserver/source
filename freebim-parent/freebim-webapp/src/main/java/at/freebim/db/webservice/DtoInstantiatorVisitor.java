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

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Discipline;
import at.freebim.db.domain.Document;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.webservice.dto.Base;

/**
 * 
 * @see at.freebim.db.domain.base.UUidIdentifyableVisitor
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class DtoInstantiatorVisitor implements UUidIdentifyableVisitor {

	/**
	 * The current visited instance of a class that extends
	 * {@link UuidIdentifyable}.
	 */
	private Base<? extends UuidIdentifyable> instance;

	/**
	 * The DTO helper.
	 */
	private DtoHelper dtoHelper;

	/**
	 * Create a new instance.
	 * 
	 * @param dtoHelper the dto helper
	 */
	public DtoInstantiatorVisitor(DtoHelper dtoHelper) {
		this.dtoHelper = dtoHelper;
	}

	/**
	 * Get the current visited instance.
	 * 
	 * @return the instance
	 */
	public Base<? extends UuidIdentifyable> getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * DataType)
	 */
	@Override
	public void visit(DataType dataType) {
		this.instance = new at.freebim.db.webservice.dto.DataType(dataType, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Discipline)
	 */
	@Override
	public void visit(Discipline discipline) {
		this.instance = new at.freebim.db.webservice.dto.Discipline(discipline, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Measure)
	 */
	@Override
	public void visit(Measure measure) {
		this.instance = new at.freebim.db.webservice.dto.Measure(measure, this.dtoHelper, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Phase)
	 */
	@Override
	public void visit(Phase phase) {
		this.instance = new at.freebim.db.webservice.dto.Phase(phase, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Parameter)
	 */
	@Override
	public void visit(Parameter parameter) {
		this.instance = new at.freebim.db.webservice.dto.Parameter(parameter, this.dtoHelper, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Component)
	 */
	@Override
	public void visit(Component component) {
		this.instance = new at.freebim.db.webservice.dto.Component(component, null, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Library)
	 */
	@Override
	public void visit(Library library) {
		this.instance = new at.freebim.db.webservice.dto.Library(library, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * ParameterSet)
	 */
	@Override
	public void visit(ParameterSet parameterSet) {
		this.instance = new at.freebim.db.webservice.dto.ParameterSet(parameterSet, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * ValueList)
	 */
	@Override
	public void visit(ValueList valueList) {
		this.instance = new at.freebim.db.webservice.dto.ValueList(valueList, this.dtoHelper, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Unit)
	 */
	@Override
	public void visit(Unit unit) {
		this.instance = new at.freebim.db.webservice.dto.Unit(unit, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.webservice.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * ValueListEntry)
	 */
	@Override
	public void visit(ValueListEntry valueListEntry) {
		this.instance = new at.freebim.db.webservice.dto.ValueListEntry(valueListEntry, this.dtoHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * BigBangNode)
	 */
	@Override
	public void visit(BigBangNode bigBangNode) {
		throw new RuntimeException("BigBangNode not implemented yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * Document)
	 */
	@Override
	public void visit(Document document) {
		throw new RuntimeException("Document not implemented yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.domain.base.UUidIdentifyableVisitor#visit(at.freebim.db.domain.
	 * SimpleNamedNode)
	 */
	@Override
	public void visit(SimpleNamedNode simpleNamedNode) {
		throw new RuntimeException("SimpleNamedNode not implemented yet.");
	}
}
