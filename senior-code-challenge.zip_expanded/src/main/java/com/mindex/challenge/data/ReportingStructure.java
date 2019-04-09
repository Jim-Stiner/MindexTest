package com.mindex.challenge.data;

import java.util.List;

public class ReportingStructure{
	// full number of reports(direct and non direct) for employee id passed in
	private int numberOfReports;
	
	private Employee employee;
	
	public int getNumberOfReports() {
		return numberOfReports;
	}
	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	
	
}
