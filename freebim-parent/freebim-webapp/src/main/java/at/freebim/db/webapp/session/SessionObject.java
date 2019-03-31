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
package at.freebim.db.webapp.session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class represents a session.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public class SessionObject {

	/**
	 * A thread-safe queue that holds {@link NodeInfo}.
	 */
	private ConcurrentLinkedQueue<NodeInfo> infos;
	
	/**
	 * Add a element to the queue.
	 * 
	 * @param info the element that will be added to the queue.
	 */
	public void addInfo(NodeInfo info) {
		if (this.infos == null) {
			this.infos = new ConcurrentLinkedQueue<NodeInfo>();
		}
		this.infos.add(info);
	}

	/**
	 * Poll all elements from the {@link ConcurrentLinkedQueue} and return them
	 * as {@link ArrayList}.
	 * 
	 * @return the infos as {@link ArrayList}
	 */
	public ArrayList<NodeInfo> getInfos() {
		if (this.infos != null) {
			ArrayList<NodeInfo> transmit = new ArrayList<NodeInfo>();
			while (this.infos.peek() != null) {
				transmit.add(this.infos.poll());
			}
			return transmit;
		}
		return null;
	}

}
