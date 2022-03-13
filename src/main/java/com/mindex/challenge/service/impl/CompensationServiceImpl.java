/**
 * Compensation Service Implementation
 * 
 * @author Harsh Bhansali
 *
 */
package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	@Autowired
	private CompensationRepository compensationRepository;

	/**
	 * Function responsible for fetching compensation based on employee id
	 */
	@Override
	public Compensation getCompensation(String id) {
		LOG.debug("Fetching compensation for employee with id [{}]", id);
		Compensation compensation = compensationRepository.findByEmployeeId(id);
		if (compensation == null) {
			throw new RuntimeException("Invalid employeeId: " + id);
		}
		return compensation;
	}

	/**
	 * Function responsible for creating Compensation
	 */
	@Override
	public Compensation createCompensation(Compensation compensation) {
		LOG.debug("Creating compenstaion [{}]", compensation);
		compensationRepository.insert(compensation);
		return compensation;
	}

}
