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
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Unit;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.References;
import at.freebim.db.repository.MeasureRepository;
import at.freebim.db.service.DateService;
import at.freebim.db.service.EqualityService;
import at.freebim.db.service.MeasureService;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link Measure}.
 * This service extends {@link ContributedBaseNodeServiceImpl} and
 * implements {@link MeasureService}.
 * 
 * @see at.freebim.db.domain.Measure
 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl
 * @see at.freebim.db.service.MeasureService
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class MeasureServiceImpl extends ContributedBaseNodeServiceImpl<Measure> implements MeasureService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MeasureServiceImpl.class);

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/**
	 * The service that handles equality.
	 */
	@Autowired
	private EqualityService equalityService;
	
	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<Measure> r) {
		this.repository = r;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.MeasureService#getMeasureFor(at.freebim.db.domain.DataType, at.freebim.db.domain.Unit, at.freebim.db.domain.ValueList, java.lang.String, java.util.List, at.freebim.db.domain.Library)
	 */
	@Override
	public Measure getMeasureFor(DataType dataType, Unit unit,
			ValueList valueList, String prefix, List<Measure> existingMeasures, Library library) {
		Measure m = null;
		for (final Measure existingMeasure : existingMeasures) {
			if (!this.equalityService.relatedEquals(existingMeasure.getDataType(), dataType))
				continue;
			if (!this.equalityService.relatedEquals(existingMeasure.getUnit(), unit))
				continue;
			if (!this.equalityService.relatedEquals(existingMeasure.getValue(), valueList))
				continue;
			if (prefix != null && !prefix.equals(existingMeasure.getPrefix()))
				continue;
			if (existingMeasure.getPrefix() != null && !existingMeasure.getPrefix().equals(prefix))
				continue;
			logger.info("using existing Measure: {}", existingMeasure.getName());
			m = existingMeasure;
			break;
		}
		if (m == null) {
			m = new Measure();
			StringBuilder b = new StringBuilder();
			if (prefix != null) {
				b.append(prefix);
				b.append(" ");
			}
			if (dataType != null) 
				b.append(dataType.getName());
			b.append(" ");
			if (unit != null) {
				b.append("[");
				b.append(unit.getName());
				b.append("]");
			}
			b.append(" ");
			if (valueList != null) {
				b.append(valueList.getName());
			}
			m.setPrefix(prefix);
			String name = b.toString().trim();
			m.setName((name == null || name.length() == 0) ? "???" : name);

			m = this.save(m);
			if (m == null) {
				throw new RuntimeException("saved Measure is null: " + name);
			}
			logger.info("new Measure: {} saved, nodeId={}", m.getName(), m.getNodeId());
			
			if (dataType != null) {
				OfDataType dtRel = new OfDataType();
				dtRel.setN1(m);
				dtRel.setN2(dataType);
				dtRel = (OfDataType) this.relationService.save(dtRel);
				logger.debug("OF_DATATYPE relation saved: id={}", dtRel.getId());
			}

			if (unit != null) {
				OfUnit uRel = new OfUnit();
				uRel.setN1(m);
				uRel.setN2(unit);
				uRel = (OfUnit) this.relationService.save(uRel);
				logger.debug("OF_UNIT relation saved: id={}", uRel.getId());
			}

			if (valueList != null) {
				HasValue hv = new HasValue();
				hv.setN1(m);
				hv.setN2(valueList);
				hv = (HasValue) this.relationService.save(hv);
				logger.debug("HAS_VALUE relation saved: id={}", hv.getId());
			}
			References libRef = new References();
			libRef.setN1(m);
			libRef.setN2(library);
			libRef.setTs(this.dateService.getMillis());
			this.relationService.save(libRef);

			m = this.getByNodeId(m.getNodeId());
			existingMeasures.add(m);
		}
		return m;
	}


	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
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
		b.append(" WHERE ALL(y IN nodes(path) WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now}) )");

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

		b.append(returnStatement);
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.BsddObjectService#findByBsddGuid(java.lang.String)
	 */
	@Override
	public List<Measure> findByBsddGuid(String bsddGuid) {
		return ((MeasureRepository) this.repository).findByBsddGuid(bsddGuid);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.MeasureService#getByName(java.lang.String)
	 */
	@Override
	public List<Measure> getByName(String name) {
		return ((MeasureRepository) this.repository).findByName(name);
	}
	
}
