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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.graphdb.NotFoundException;
import org.neo4j.kernel.DeadlockDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import at.freebim.db.service.AsyncNgramCreator;
import at.freebim.db.service.AsyncNgramLoader;
import at.freebim.db.service.NgramService;

/**
 * The service to create n-gram nodes.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.AsyncNgramCreator
 * @see java.lang.Runnable
 */
@Service
public class AsyncNgramCreatorImpl implements Runnable, AsyncNgramCreator {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AsyncNgramCreatorImpl.class);

	/**
	 * The queue for the node id's.
	 */
	private Queue<Long> queue;

	/**
	 * The thread.
	 */
	private Thread thread;

	/**
	 * The maximum time to wait for the {@link ConcurrentLinkedQueue}.
	 */
	private long waitInterval = 1000L * 60L * 60L; // one hour

	/**
	 * Determines if this thread is running.
	 */
	private boolean running;

	/**
	 * The service that handles n-grams.
	 */
	@Autowired
	private NgramService ngramService;

	/**
	 * Initialize the private fields.
	 */
	@PostConstruct
	public void init() {
		if (this.queue == null) {
			this.queue = new ConcurrentLinkedQueue<>();
		}
		this.thread = new Thread(this);
		this.thread.start();
	}

	/**
	 * This method should be called before the object is destroyed to gently stop
	 * the threads and save nodes that are still in the queue.
	 */
	@PreDestroy
	public void stop() {

		logger.info("stopping ...");

		this.running = false;

		if (this.queue != null) {
			synchronized (this.queue) {
				this.queue.notifyAll();
			}
			synchronized (this.queue) {
				this.queue.notifyAll();
			}
		}

		if (this.thread != null && this.thread.isAlive()) {
			try {
				logger.debug("joining thread ...");
				this.thread.join(60000L); // wait a minute max. to die ...
				logger.debug("thread joined.");
				this.thread = null;
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}
		}
		logger.info("stopped, persisting {} nodes ...", this.queue.size());
		if (this.queue != null) {
			try {
				FileWriter w = new FileWriter(AsyncNgramLoader.persitentQueueFile);
				Iterator<Long> iter = this.queue.iterator();
				while (iter.hasNext()) {
					Long nodeId = iter.next();
					w.write(String.valueOf(nodeId));
					w.write("\n");
				}
				w.close();
			} catch (IOException e) {
				logger.error("Error persisting nodeId's, ", e);
			}

		}
		logger.info("finished.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this.running = true;
		while (this.running) {

			logger.info("still alive :-)");
			Long nodeId = null;

			do {
				nodeId = this.poll();
				if (nodeId != null) {
					logger.debug("next node = {}", nodeId);

					try {
						this.ngramService.createForNode(nodeId);
					} catch (DataRetrievalFailureException e) {
						logger.error("Error creating Ngram's for node, nodeId = " + nodeId);
					} catch (NotFoundException e) {
						logger.error("Error creating Ngram's for node, nodeId = " + nodeId);
					} catch (IllegalStateException e) {
						// do it again, sam!
						logger.info("Error creating Ngram's for node, nodeId = {}, try it again after a second ...",
								nodeId);
						try {
							Thread.sleep(1000L);
							this.add(nodeId);
						} catch (InterruptedException e1) {
							logger.info("Interrupted before try it again ...", nodeId);
						}
					} catch (DeadlockDetectedException e) {
						logger.info(e.getMessage());
					} catch (Exception e) {
						logger.error("Error creating Ngram's for node, nodeId = " + nodeId, e);
					}
				}
			} while (nodeId != null && this.running);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.AsyncNgramCreator#add(at.freebim.db.domain.base.
	 * BaseNode)
	 */
	@Override
	public boolean add(Long nodeId) {
		logger.debug("adding node, nodeId = {}.", nodeId);
		if (nodeId != null) {

			synchronized (this.queue) {
				boolean res = queue.add(nodeId);
				this.queue.notifyAll();
				return res;
			}
		}
		return true;
	}

	/**
	 * Polls a element from the queue. If there is no element in the queue and
	 * <code>running</code> is true then the {@link ConcurrentLinkedQueue} will
	 * wait. The method returns <code>null</code> if no element is in the list or
	 * <code>running</code> is false.
	 *
	 * @return the head of the queue. if <code>null</code> is returned the queue is
	 *         empty or <code>running</code> is false.
	 * @see java.util.Queue#poll()
	 */
	public Long poll() {
		logger.debug("polling ...");
		boolean doIt = this.running;
		Long nodeId = null;

		nodeId = queue.poll();

		if (nodeId == null && doIt) {
			try {
				logger.debug("waiting in poll ...");
				synchronized (this.queue) {
					this.queue.wait(this.waitInterval);
				}
				// wake up slowly, SDN needs some time to fully persist the entity
				Thread.sleep(1000L);
				logger.debug("continue.");
			} catch (InterruptedException e) {
				this.running = false;
				logger.info(e.getMessage());
			}
		} else {
			logger.debug("polled, nodeId = {}", nodeId);
		}
		return nodeId;

	}

}
