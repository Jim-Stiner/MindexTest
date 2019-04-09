package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
	List<Employee> directReportsList = new ArrayList<Employee>();
	int numberOfReports;

	@Autowired
	private EmployeeRepository employeeRepository;

	private Employee buildDirectReport(Employee employee) {

		// populate direct report list
		List<Employee> directReportList = directReports(employee);

		// have running counter of reports
		numberOfReports += directReportList.size();
		
		employee.setDirectReports(directReportList);
		// see if direct report employees have direct reports of their own
		for (Employee e : directReportList) {
			buildDirectReport(e);
		}

		return employee;
	}

	// returns a list of populated employee objects/resources
	private List<Employee> directReports(Employee emp) {
		List<Employee> directReportsList = new ArrayList<Employee>();
		if (null != emp.getDirectReports()) {
			for (Employee employee : emp.getDirectReports()) {
				Employee child = employeeRepository.findByEmployeeId(employee.getEmployeeId());
				directReportsList.add(child);
			}
		}
		return directReportsList;
	}

	@Override
	public ReportingStructure read(String id) throws RuntimeException {

		Employee employee = employeeRepository.findByEmployeeId(id);
		if (employee == null) {
			throw new RuntimeException("Cannot find employeeId: " + id);
		}
		ReportingStructure rs = new ReportingStructure();
		Employee employeez = buildDirectReport(employee);
		rs.setNumberOfReports(numberOfReports);
		rs.setEmployee(employeez);
		numberOfReports = 0;
		return rs;
	}

}
