package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {

	public Compensation getCompensation(String employeeId);

	public Compensation createCompensation(Compensation compensation);

}
