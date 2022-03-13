package com.mindex.challenge.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@Component
public class EmployeeHelper {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeHelper.class);

	@Autowired
	private EmployeeService employeeService;

	private int noOfReports = 0;

	public ReportingStructure fillReportingStructure(String id) {
		LOG.debug("Creating employee structure and counting reports for id [{}]", id);
		noOfReports = 0;
		Employee employee = employeeService.read(id);
		return new ReportingStructure(fillReportingStructureRec(employee), noOfReports);
	}

	private Employee fillReportingStructureRec(Employee employee) {
		if (null != employee && null != employee.getDirectReports()) {
			List<Employee> reportees = employee.getDirectReports();
			for (int index = 0; index < reportees.size(); index++) {
				noOfReports++;
				reportees.set(index,
						fillReportingStructureRec(employeeService.read(reportees.get(index).getEmployeeId())));
			}
		}
		return employee;
	}
	
	public Compensation fetchEmployeeById(String id, Compensation compensation) {
		compensation.setEmployee(employeeService.read(id));
		return compensation;
	}

}
