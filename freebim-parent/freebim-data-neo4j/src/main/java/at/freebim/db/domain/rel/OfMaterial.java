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
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.OfMaterialDeserializer;
import at.freebim.db.json.rel.OfMaterialSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between nodes or classes {@link Component}. It
 * denotes that a {@link Component} is made out of the material
 * ({@link Component}). This relation extends {@link BaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.Component
 * @see at.freebim.db.domain.base.rel.BaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.OF_MATERIAL)
@JsonSerialize(using = OfMaterialSerializer.class)
@JsonDeserialize(using = OfMaterialDeserializer.class)
public class OfMaterial extends BaseRel<Component, Component> {

	private static final long serialVersionUID = -2624029924903770699L;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public OfMaterial() {
		super(RelationType.OF_MATERIAL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.OF_MATERIAL.name();
	}

}
