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
package at.freebim.db.domain;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.neo4j.annotation.NodeEntity;

import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.UUidIdentifyableVisitor;
import at.freebim.db.domain.json.BigBangNodeDeserializer;
import at.freebim.db.domain.json.BigBangNodeSerializer;
import net.spectroom.neo4j.backup.annotation.NodeBackup;

/**
 * The one and only absolute root element of all hierarchies.
 * 
 * This is the point where all libraries are connected, and all paths will be
 * calculated to this node. 
 * This nodes extends {@link HierarchicalBaseNode}.
 * 
 * @see at.freebim.db.domain.base.HierarchicalBaseNode
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@NodeBackup
@NodeEntity
@JsonSerialize(using = BigBangNodeSerializer.class)
@JsonDeserialize(using = BigBangNodeDeserializer.class)
public class BigBangNode extends HierarchicalBaseNode {

	private static final long serialVersionUID = 1160424768673213674L;
	
	/**
	 * The app version.
	 */
	private String appVersion;

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.HierarchicalBaseNode#equalsData(java.lang.Object)
	 */
	@Override
	public boolean equalsData(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return super.equalsData(obj);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.UUidIdentifyableVistitable#accept(at.freebim.db.domain.base.UUidIdentifyableVisitor)
	 */
	@Override
	public void accept(UUidIdentifyableVisitor visitor) {
		if (visitor != null)
			visitor.visit(this);
	}

	/**
	 * Get the version of the app.
	 * 
	 * @return the version of the app
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * Set the version of the app.
	 * 
	 * @param appVersion the version of the app to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((appVersion == null) ? 0 : appVersion.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigBangNode other = (BigBangNode) obj;
		if (appVersion == null) {
			if (other.appVersion != null)
				return false;
		} else if (!appVersion.equals(other.appVersion))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.domain.base.Named#getName()
	 */
	@Override
	public String getName() {
		return "*";
	}

}
