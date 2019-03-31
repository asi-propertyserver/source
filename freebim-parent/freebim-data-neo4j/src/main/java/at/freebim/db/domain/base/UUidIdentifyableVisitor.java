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
package at.freebim.db.domain.base;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Discipline;
import at.freebim.db.domain.Document;
//import at.freebim.db.domain.Document;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ParameterSet;
import at.freebim.db.domain.Phase;
import at.freebim.db.domain.SimpleNamedNode;
//import at.freebim.db.domain.SimpleNamedNode;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.ValueListEntry;

/**
 * This interface represents a unique identifiable visitor. 
 *
 * @author rainer.breuss@uibk.ac.at
 */
public interface UUidIdentifyableVisitor {

	public abstract void visit(BigBangNode bigBangNode);
	public abstract void visit(Component component);
	public abstract void visit(DataType dataType);
	public abstract void visit(Discipline discipline);
	public abstract void visit(Document document);
	public abstract void visit(Library library);
	public abstract void visit(Measure measure);
	public abstract void visit(Parameter parameter);
	public abstract void visit(ParameterSet parameterSet);
	public abstract void visit(Phase phase);
	public abstract void visit(SimpleNamedNode simpleNamedNode);
	public abstract void visit(Unit unit);
	public abstract void visit(ValueList valueList);
	public abstract void visit(ValueListEntry valueListEntry);
}
