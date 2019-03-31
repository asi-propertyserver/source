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
package at.freebim.db.service;

import java.util.ArrayList;

/**
 * The service for statistics.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
public interface StatisticService {
	
	public class StatPoint {
		
		/**
		 * time stamp
		 */
		public String t; 
		
		/**
		 * modified
		 */
		public Number m; 
		
		/**
		 * sum
		 */
		public Number s; 
		
		
		/**
		 * Create a new instance.
		 */
		public StatPoint() {
			this.m = 0L;
			this.s = 0L;
		}
	}

	/**
	 * Get the statistic in a certain time interval.
	 * How much contributions/modifications have been made.
	 * 
	 * @param fromTs the start of the time interval
	 * @param toTs the end of the time interval
	 * 
	 * @return the statistics {@link StatPoint}
	 */
	public ArrayList<StatPoint> get(Long fromTs, Long toTs);

}
