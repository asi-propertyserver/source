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

import at.freebim.db.domain.Company;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.rel.BaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.json.rel.WorksForDeserializer;
import at.freebim.db.json.rel.WorksForSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between nodes or classes {@link Contributor} and
 * {@link Company}. It denotes that a {@link Contributor} works for a
 * {@link Company}. This relation extends {@link BaseRel}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see org.neo4j.ogm.annotation.RelationshipEntity
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.domain.base.rel.BaseRel
 */
@RelationshipBackup
@RelationshipEntity(type = RelationType.WORKS_FOR)
@JsonSerialize(using = WorksForSerializer.class)
@JsonDeserialize(using = WorksForDeserializer.class)
public class WorksFor extends BaseRel<Contributor, Company> {

	private static final long serialVersionUID = 1146732822230918760L;

	/**
	 * Creates a new instance of the relation with the type of the relation.
	 */
	public WorksFor() {
		super(RelationType.WORKS_FOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.WORKS_FOR.name();
	}
}
