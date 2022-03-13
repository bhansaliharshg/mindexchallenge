package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mindex.challenge.data.Compensation;

public interface CompensationRepository extends MongoRepository<Compensation, String> {
	@Query(value ="{'employee.employeeId' : ?0 }")
	Compensation findByEmployeeId(String employeeId);
}
