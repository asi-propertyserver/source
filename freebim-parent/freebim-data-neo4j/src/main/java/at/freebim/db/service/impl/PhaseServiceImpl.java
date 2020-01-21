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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.Phase;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.repository.PhaseRepository;
import at.freebim.db.service.DateService;
import at.freebim.db.service.PhaseService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link Phase}. This service extends
 * {@link ContributedBaseNodeServiceImpl} and implements {@link PhaseService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.Phase
 * @see at.freebim.db.service.PhaseService
 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl
 */
@Service
public class PhaseServiceImpl extends ContributedBaseNodeServiceImpl<Phase> implements PhaseService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PhaseServiceImpl.class);

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.
	 * springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(Neo4jRepository<Phase, Long> r) {
		this.repository = r;
	}

	@PostConstruct
	@Transactional
	public void init() {
		// create the UNDEFINED Phase, if not present ...
		Phase p = ((PhaseRepository) this.repository).getByCode(CODE_UNDEFINED);
		if (p == null) {
			p = new Phase();
			p.setCode(CODE_UNDEFINED);
			p.setDesc("Nicht definiert, oder nicht bekannt");
			p.setName("Nicht definiert");
			p.setHexColor("ff00ff");
			p = this.save(p);
			logger.info("Phase '{}' saved, nodeId={}", CODE_UNDEFINED, p.getNodeId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.PhaseService#getByCode(java.lang.String)
	 */
	@Override
	public Phase getByCode(String code) {
		Phase p = ((PhaseRepository) this.repository).getByCode(code);
		return this.filterResponse(p, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BaseNodeService#getAllRelevant()
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<Phase> getAllRelevant() {

		ArrayList<Phase> res = new ArrayList<>();
		StringBuilder b = new StringBuilder();

		final Long now = this.dateService.getMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("now", now);

		b.append("MATCH (n:BigBangNode) WITH n AS bbn MATCH path = ");
		b.append(" (bbn)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("]->(:Library)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("*]->(c:Component)-[r:");
		b.append(RelationTypeEnum.HAS_PARAMETER);
		b.append("]->(p:Parameter)");
		b.append(
				" WHERE all(y in nodes(path) WHERE y.validFrom < {now} AND ((y.validTo IS NULL) OR y.validTo > {now}))");
		b.append(" WITH r AS rel MATCH (ph:Phase) WHERE ph.uuid = rel.phaseUuid");
		b.append(" RETURN distinct ph AS n");

		String query = b.toString();

		Iterable<Map<String, Object>> result = this.relationService.getTemplate().query(query, params, true);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Phase n = (Phase) map.get("n");
			// BaseNode pathNode = this.relationService.createTreeNode(n);
			res.add(n);
			logger.debug("    found node {} ", n.getNodeId());
		}
		if (logger.isInfoEnabled())
			logger.info("found {} relevant Phase objects in {} ms.", res.size(), this.dateService.getMillis() - now);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.
	 * lang.StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {

		String with = " WITH y AS x, count(*) AS cnt MATCH";
		String where = " WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})";

		b.append("MATCH (y:BigBangNode)");

		b.append(with);
		b.append(" path=");
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("*]->(y)");
		b.append(
				" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");

		b.append(with);
		b.append(" (x)-[r:");
		b.append(RelationTypeEnum.HAS_PARAMETER);
		b.append("]->(y)");
		b.append(where);

		b.append(" WITH r AS r, count(*) AS cnt MATCH");
		b.append(" (y:Phase)");
		b.append(" WHERE y.uuid = r.phaseUuid");
		b.append(" AND y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");

		b.append(returnStatement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<Phase> findByBsddGuid(String bsddGuid) {
		return ((PhaseRepository) this.repository).findByBsddGuid(bsddGuid);
	}

}
