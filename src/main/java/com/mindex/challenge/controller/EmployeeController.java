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

	@GetMapping("/{id}/reportee")
	public ReportingStructure getReportStructure(@PathVariable String id) {
		LOG.debug("Received report structure request from id [{}]", id);

		ReportingStructure reportingStructure = employeeHelper.fillReportingStructure(id);
		return reportingStructure;
	}

	@GetMapping("/compensation/{id}")
	public Compensation getEmployeeCompensation(@PathVariable String id) {
		LOG.debug("Received request to fetch compensation for id [{}]", id);
		return compensationService.getCompensation(id);
	}

	@PostMapping("/compensation/{id}")
	public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compenstaion) {
		LOG.debug("Received request to create compensation for id [{}]", id);
		if (null == compenstaion || null == compenstaion.getEffectiveDate() || 0 >= compenstaion.getSalary()) {
			throw new RuntimeException("Compensation data not passed properly");
		}
		return compensationService.createCompensation(employeeHelper.fetchEmployeeById(id, compenstaion));
	}
}
