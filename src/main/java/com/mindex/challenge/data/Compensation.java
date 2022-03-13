package com.mindex.challenge.data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Compensation")
public class Compensation {
	private Employee employee;
	private double salary;
	private String effectiveDate;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
