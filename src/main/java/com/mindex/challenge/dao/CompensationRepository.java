
package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mindex.challenge.data.Compensation;

public interface CompensationRepository extends MongoRepository<Compensation, String> {
	/**
	 * Custom repository function to fetch compensation details based on employee id.
	 * 
	 * @param employeeId
	 * @return
	 */
	@Query(value ="{'employee.employeeId' : ?0 }")
	Compensation findByEmployeeId(String employeeId);
}
