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
package at.freebim.db.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.base.ContributionType;
import at.freebim.db.service.StatisticService;

/**
 * The service for statistics.
 * This service implements {@link StatisticService}.
 * 
 * @see at.freebim.db.service.StatisticService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class StatisticServiceImpl implements StatisticService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);


	/**
	 * The date format.
	 */
	private DateFormat df;
	
	
	/**
	 * Mediator class for services of neo4j.
	 */
	@Autowired
	private Neo4jTemplate template;
	

	/* (non-Javadoc)
	 * @see at.freebim.db.service.StatisticService#get(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ArrayList<StatPoint> get(Long fromTs, Long toTs) {
		
		logger.info("getAdded {}, {}", fromTs, toTs);
		
		if (df == null) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
		    df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    df.setTimeZone(tz);
		}
	    

		ArrayList<StatPoint> res = new ArrayList<StatPoint>();
		Long sum = 0L;
		
		Long mods = 0L;
		
		final Map<String, Object> params = new HashMap<String, Object>();
		Long delta = 1000L * 60L * 60L * 24L; 
		params.put( "fromTs", fromTs );
		params.put( "toTs", toTs);
		params.put( "delta", delta);

		final String query = "MATCH (n)-[cb:CONTRIBUTED_BY]->(c:Contributor) "
				+ "WHERE cb.ts >= {fromTs}"
				+ " AND cb.ts <= {toTs}"
				+ " RETURN distinct(TOINT((cb.ts - {toTs}) / {delta}) * {delta} + {toTs}) as ts, count(n) AS val, cb.contributionType AS type order by ts";
		
		logger.info("query=[{}]", query);
		
		final Result<Map<String, Object>> result = this.template.query(query, params);
		final Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			final Map<String, Object> map = iter.next();
			Long ts = (Long) map.get("ts");
			Long val = (Long) map.get("val");
			String type = (String) map.get("type");
			StatPoint pt = new StatPoint();
			pt.t = df.format(new Date(ts));
			ContributionType t = ContributionType.valueOf(type);
			switch (t) {
			case CREATE:
				sum += val;
				pt.s = sum;
				pt.m = mods;
				break;
			case DELETE:
				sum -= val;
				pt.s = sum;
				pt.m = mods;
				break;
			case MODIFY:
				pt.s = sum;
				mods += val;
				pt.m = mods;
				break;
			default:
			case UNDEFINED:
				pt.s = sum;
				pt.m = mods;
				continue;
			}
			res.add(pt);
		}			
		
		logger.info("res count = [{}]", res.size());
		return res;
	}
}
