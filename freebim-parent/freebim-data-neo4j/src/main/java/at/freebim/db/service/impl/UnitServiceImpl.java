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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.Unit;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.UnitRepository;
import at.freebim.db.service.UnitService;

/**
 * The service for the node/class {@link Unit}. This service extends
 * {@link ContributedBaseNodeServiceImpl} and implements {@link UnitService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Unit
 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl
 * @see at.freebim.db.service.UnitService
 */
@Service
public class UnitServiceImpl extends ContributedBaseNodeServiceImpl<Unit> implements UnitService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<Unit, Long> r) {
		this.repository = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.UnitService#standardize(java.lang.String)
	 */
	@Override
	public String standardize(String name) {
		if (name == null)
			return "";
		String orig = name;
		name = name.trim();

		name = name.replaceAll("[ ]", "");
		name = name.replaceAll("[\\(]", "");
		name = name.replaceAll("[\\)]", "");
		name = name.replaceAll("[\\[]", "");
		name = name.replaceAll("[\\]]", "");
		name = name.replaceAll("[\\{]", "");
		name = name.replaceAll("[\\}]", "");

		name = name.replaceAll("[Mm][Mm]", "mm");
		name = name.replaceAll("[Cc][Mm]", "cm");
		name = name.replaceAll("[Dd][Mm]", "dm");
		name = name.replaceAll("[M](\\d)", "m$1");
		name = name.replaceAll("[Kk][Mm]", "km");

		name = name.replaceAll("[m][2]", "m²");
		name = name.replaceAll("[m][3]", "m³");
		name = name.replaceAll("[m][4]", "m⁴");
		name = name.replaceAll("[m][5]", "m⁵");
		name = name.replaceAll("[m][6]", "m⁶");

		name = name.replaceAll("[\\^][1]", "¹");
		name = name.replaceAll("[\\^][2]", "²");
		name = name.replaceAll("[\\^][3]", "³");
		name = name.replaceAll("[\\^][4]", "⁴");
		name = name.replaceAll("[\\^][5]", "⁵");
		name = name.replaceAll("[\\^][6]", "⁶");
		name = name.replaceAll("[\\^][7]", "⁷");
		name = name.replaceAll("[\\^][8]", "⁸");
		name = name.replaceAll("[\\^][9]", "⁹");

		name = name.replaceAll("[\\^][\\-][1]", "⁻¹");
		name = name.replaceAll("[\\^][\\-][2]", "⁻²");
		name = name.replaceAll("[\\^][\\-][3]", "⁻³");
		name = name.replaceAll("[\\^][\\-][4]", "⁻⁴");
		name = name.replaceAll("[\\^][\\-][5]", "⁻⁵");
		name = name.replaceAll("[\\^][\\-][6]", "⁻⁶");
		name = name.replaceAll("[\\^][\\-][7]", "⁻⁷");
		name = name.replaceAll("[\\^][\\-][8]", "⁻⁸");
		name = name.replaceAll("[\\^][\\-][9]", "⁻⁹");

		name = name.replaceAll("[S]", "s");
		name = name.replaceAll("[Kk][Nn]", "kN");
		name = name.replaceAll("[Kk][Gg]", "kg");
		name = name.replaceAll("[Kk][J]", "kJ");
		name = name.replaceAll("[Mm][Kk]", "mK");

		if (!orig.equals(name)) {
			logger.info("Unit: [{}] standardized to [{}].", orig, name);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.ContributedBaseNodeServiceImpl#filterBeforeInsert(
	 * at.freebim.db.domain.base.ContributedBaseNode)
	 */
	@Override
	public Unit filterBeforeInsert(Unit node) {
		try {
			if (node != null) {
				node.setUnitCode(this.standardize(node.getUnitCode()));
			}
		} catch (Exception e) {
			logger.error("Error in standardize:", e);
		}
		return super.filterBeforeInsert(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.
	 * lang.StringBuilder, java.lang.String)
	 */
	@Override
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {

		String with = " WITH y AS x, count(*) AS cnt MATCH";
		String where = " WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})";

		b.append("MATCH (y:BigBangNode)");

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" path=");
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("*]->(y)");
		b.append(
				" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_PARAMETER);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.HAS_MEASURE);
		b.append("]->(y)");
		b.append(where);

		b.append(with);
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.OF_UNIT);
		b.append("]->(y)");
		b.append(where);

		b.append(returnStatement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<Unit> findByBsddGuid(String bsddGuid) {
		return ((UnitRepository) this.repository).findByBsddGuid(bsddGuid);
	}

}
