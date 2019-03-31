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
package at.freebim.db.dto;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.json.rel.RelationsDeserializer;

/**
 * @author rainer.breuss@uibk.ac.at
 *
 */
@JsonDeserialize(using = RelationsDeserializer.class)
public class Relations {

	
	/**
	 * The class name.
	 */
	public String c;
	
	/**
	 * The type of the relation.
	 * 
	 * @see at.freebim.db.domain.base.rel.RelationTypeEnum
	 */
	public int t;
	
	/**
	 * The direction of the relations.
	 */
	public String dir;
	
	/**
	 * The relations.
	 */
	public BaseRel<?,?>[] relations;
}
