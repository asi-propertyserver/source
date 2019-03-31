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

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.rel.OrderedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.ResponsibleDeserializer;
import at.freebim.db.domain.json.rel.ResponsibleSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/**
 * The node to node relation between nodes or classes {@link Contributor} and {@link Library}.
 * It denotes that a {@link Contributor} is responsible for a {@link Library}.
 * This relation extends {@link OrderedBaseRel}.
 * 
 * @see at.freebim.db.domain.Contributor
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.domain.base.rel.OrderedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.RESPONSIBLE)
@JsonSerialize(using = ResponsibleSerializer.class)
@JsonDeserialize(using = ResponsibleDeserializer.class)
public class Responsible extends OrderedBaseRel<Contributor, Library> {

	private static final long serialVersionUID = -1278091727357108830L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public Responsible() {
		super(RelationType.RESPONSIBLE);
	}
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.RESPONSIBLE.name();
	}

}
