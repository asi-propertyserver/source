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

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.BelongsToDeserializer;
import at.freebim.db.json.rel.BelongsToSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between the nodes or classes {@link FreebimUser}
 * and {@link Contributor}. It denotes that a {@link FreebimUser} corresponds to
 * a {@link Contributor}. The relation extends {@link BaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.FreebimUser
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.base.rel.BaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.BELONGS_TO)
@JsonSerialize(using = BelongsToSerializer.class)
@JsonDeserialize(using = BelongsToDeserializer.class)
public class BelongsTo extends BaseRel<FreebimUser, Contributor> {

	private static final long serialVersionUID = 7893411863476944495L;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public BelongsTo() {
		super(RelationType.BELONGS_TO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.BELONGS_TO.name();
	}
}
