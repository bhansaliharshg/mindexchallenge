package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

	private String employeeUrl;
	private String employeeIdUrl;
	private String reportingEmployeesUrl;
	private String compensationUrl;

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		employeeUrl = "http://localhost:" + port + "/employee";
		employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
		reportingEmployeesUrl = "http://localhost:" + port + "/employee/{id}/reports";
		compensationUrl = "http://localhost:" + port + "/employee/compensation/{id}";
	}

	@Test
	public void testCreateReadUpdate() {
		Employee testEmployee = new Employee();
		testEmployee.setFirstName("John");
		testEmployee.setLastName("Doe");
		testEmployee.setDepartment("Engineering");
		testEmployee.setPosition("Developer");

		// Create checks
		Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

		assertNotNull(createdEmployee.getEmployeeId());
		assertEmployeeEquivalence(testEmployee, createdEmployee);

		// Read checks
		Employee readEmployee = restTemplate
				.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
		assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
		assertEmployeeEquivalence(createdEmployee, readEmployee);

		// Update checks
		readEmployee.setPosition("Development Manager");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Employee updatedEmployee = restTemplate.exchange(employeeIdUrl, HttpMethod.PUT,
				new HttpEntity<Employee>(readEmployee, headers), Employee.class, readEmployee.getEmployeeId())
				.getBody();

		assertEmployeeEquivalence(readEmployee, updatedEmployee);
	}

	@Test
	public void testEmployeeReports() {
		Employee employeeOne = new Employee();
		employeeOne.setEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
		employeeOne.setDepartment("Engineering");
		employeeOne.setFirstName("George");
		employeeOne.setLastName("Harrison");
		employeeOne.setPosition("Developer III");
		Employee employeeTwo = new Employee();
		employeeTwo.setEmployeeId("62c1084e-6e34-4630-93fd-9153afb65309");
		employeeTwo.setDepartment("Engineering");
		employeeTwo.setFirstName("Pete");
		employeeTwo.setLastName("Best");
		employeeTwo.setPosition("Developer II");
		List<Employee> reportees = new ArrayList<Employee>();
		reportees.add(employeeOne);
		reportees.add(employeeTwo);
		Employee testEmployee = new Employee();
		testEmployee.setEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
		testEmployee.setDepartment("Engineering");
		testEmployee.setFirstName("Ringo");
		testEmployee.setLastName("Starr");
		testEmployee.setPosition("Developer V");
		testEmployee.setDirectReports(reportees);
		ReportingStructure testReportingStructure = new ReportingStructure(testEmployee, 2);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ReportingStructure actualReportingStructure = restTemplate
				.getForEntity(reportingEmployeesUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();
		assertEquals(actualReportingStructure.getNumberOfReports(), testReportingStructure.getNumberOfReports());
		assertEmployeeEquivalence(actualReportingStructure.getEmployee(), testReportingStructure.getEmployee());

	}

	@Test
	public void testCreateReadCompensation() {
		Employee testEmployee = new Employee();
		testEmployee.setEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
		testEmployee.setDepartment("Engineering");
		testEmployee.setFirstName("George");
		testEmployee.setLastName("Harrison");
		testEmployee.setPosition("Developer III");
		Compensation testCompensation = new Compensation();
		testCompensation.setEmployee(testEmployee);
		testCompensation.setSalary(123456.0);
		testCompensation.setEffectiveDate("03/12/2022");

		Compensation requestCompensation = new Compensation();
		requestCompensation.setSalary(123456.0);
		requestCompensation.setEffectiveDate("03/12/2022");

		Compensation postCompensation = restTemplate
				.postForEntity(compensationUrl, requestCompensation, Compensation.class, testEmployee.getEmployeeId())
				.getBody();

		assertCompensationEquvalance(postCompensation, testCompensation);

		Compensation getCompensation = restTemplate
				.getForEntity(compensationUrl, Compensation.class, testEmployee.getEmployeeId()).getBody();

		assertCompensationEquvalance(getCompensation, testCompensation);

	}

	private static void assertCompensationEquvalance(Compensation expected, Compensation actual) {
		assertEquals(expected.getSalary(), actual.getSalary(), 0);
		assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
		assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
	}

	private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getDepartment(), actual.getDepartment());
		assertEquals(expected.getPosition(), actual.getPosition());
	}
}
