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
package at.freebim.db.domain.rel;

import org.neo4j.ogm.annotation.RelationshipEntity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import at.freebim.db.domain.Component;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.ComponentComponentDeserializer;
import at.freebim.db.json.rel.ComponentComponentSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between two nodes or classes {@link Component}. A
 * {@link Component} can be part of other {@link Component}s. This relation
 * extends {@link OrderedBaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.COMP_COMP)
@JsonSerialize(using = ComponentComponentSerializer.class)
@JsonDeserialize(using = ComponentComponentDeserializer.class)
public class ComponentComponent extends OrderedBaseRel<Component, Component> {

	private static final long serialVersionUID = 4487840702660968536L;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public ComponentComponent() {
		super(RelationType.COMP_COMP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.COMP_COMP.name();
	}

}
