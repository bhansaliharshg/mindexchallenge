package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

/**
 * CompensationService Interface
 *
 */
public interface CompensationService {

	public Compensation getCompensation(String employeeId);

	public Compensation createCompensation(Compensation compensation);

}
