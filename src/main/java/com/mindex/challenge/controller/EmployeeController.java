package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.helper.EmployeeHelper;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeHelper employeeHelper;

	@Autowired
	private CompensationService compensationService;

	@PostMapping("")
	public Employee create(@RequestBody Employee employee) {
		LOG.debug("Received employee create request for [{}]", employee);

		return employeeService.create(employee);
	}

	@GetMapping("/{id}")
	public Employee read(@PathVariable String id) {
		LOG.debug("Received employee create request for id [{}]", id);

		return employeeService.read(id);
	}

	@PutMapping("/{id}")
	public Employee update(@PathVariable String id, @RequestBody Employee employee) {
		LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

		employee.setEmployeeId(id);
		return employeeService.update(employee);
	}

	/**
	 * Get Mapping for getting report structure for a given employee id.
	 * Following is the execution plan:
	 * - A helper layer is created to assist the controller with logical flow
	 * - EmployeeId is passed to a function which recursively calls the getEmployee details endpoint to fetch the details of the reports.
	 * - Updated member object with reports is returned
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}/reports")
	public ReportingStructure getReportStructure(@PathVariable String id) {
		LOG.debug("Received report structure request from id [{}]", id);
		//Passing employee id to a method of helper class.
		return employeeHelper.fillReportingStructure(id);
	}

	/**
	 * Get Mapping to fetch compensation details for an employee
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/compensation/{id}")
	public Compensation getEmployeeCompensation(@PathVariable String id) {
		LOG.debug("Received request to fetch compensation for id [{}]", id);
		return compensationService.getCompensation(id);
	}

	/**
	 * Post Mapping to insert compensation details for an employee. Passing salary and effective date is compulsory.
	 * Following is the execution plan:
	 * - The employee details for the passed employeeId is fetched
	 * - These employee details are then merged along with the salary and effective date information into an object of type Compensation.
	 * - This newly created object is saved.
	 * 
	 * @param id
	 * @param compenstaion
	 * @return
	 */
	@PostMapping("/compensation/{id}")
	public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compenstaion) {
		LOG.debug("Received request to create compensation for id [{}]", id);
		if (null == compenstaion || null == compenstaion.getEffectiveDate() || 0 >= compenstaion.getSalary()) {
			throw new RuntimeException("Compensation data not passed properly");
		}
		return compensationService.createCompensation(employeeHelper.fetchEmployeeById(id, compenstaion));
	}
}
