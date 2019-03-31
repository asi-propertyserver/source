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
package at.freebim.db.domain.base.rel;

import at.freebim.db.domain.NgramNode;
import at.freebim.db.domain.base.Ngramed;


/**
 * Abstract base class for all relations concerning NGRAM.
 * It extends {@link QualifiedBaseRel}.
 * 
 * @see at.freebim.db.domain.base.rel.QualifiedBaseRel
 * @see at.freebim.db.domain.NgramNode
 * @see at.freebim.db.domain.base.Ngramed
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 * @param <TO> Type that represents the end node of the relation. Start node of the relation will always be a NgramNode.
 */
public abstract class NgramRel<TO extends Ngramed> extends QualifiedBaseRel<NgramNode, TO> {

	private static final long serialVersionUID = -3716999185988607102L;

	/**
	 * Initialize the relation with a type.
	 * 
	 * @param type the type
	 */
	public NgramRel(String type) {
		super(type);
	}


}
