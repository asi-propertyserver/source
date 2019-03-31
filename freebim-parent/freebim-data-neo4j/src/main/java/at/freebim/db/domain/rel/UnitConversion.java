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

import at.freebim.db.domain.Unit;
import at.freebim.db.domain.base.rel.QualifiedBaseRel;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.json.rel.UnitConversionDeserializer;
import at.freebim.db.domain.json.rel.UnitConversionSerializer;
import net.spectroom.neo4j.backup.annotation.RelationshipBackup;

/** 
 * The node to node relation between nodes or classes {@link Unit}.
 * It denotes that a node {@link Unit} has been converted into another unit/node ({@link Unit}).
 * This relation extends {@link QualifiedBaseRel}.
 * 
 * @see at.freebim.db.domain.Unit
 * @see at.freebim.db.domain.base.rel.QualifiedBaseRel
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RelationshipBackup
@RelationshipEntity (type=RelationType.UNIT_CONVERSION)
@JsonSerialize(using = UnitConversionSerializer.class)
@JsonDeserialize(using = UnitConversionDeserializer.class)
public class UnitConversion extends QualifiedBaseRel<Unit, Unit> {

	private static final long serialVersionUID = 2797970162175266871L;

	/**
	 * Creates a new instance of the relation with the type of the relation. 
	 */
	public UnitConversion() {
		super(RelationType.UNIT_CONVERSION);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.rel.BaseRel#getType()
	 */
	@Override
	public String getType() {
		return RelationTypeEnum.UNIT_CONVERSION.name();
	}


}
