package com.mindex.challenge.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

	private String employeeUrl;
	private String employeeIdUrl;
	private String reportingStructureUrl;

	@Autowired
	private ReportingStructureService reportingStructureService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		reportingStructureUrl = "http://localhost:" + port + "/reportingstructure/{id}";
		employeeUrl = "http://localhost:" + port + "/employee";
		employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
	}

	@Test
	public void testReportingService() {
		ArrayList<Employee> coachList = new ArrayList<Employee>();
		Employee coach = new Employee();
		coach.setFirstName("Bill");
		coach.setLastName("Belly");
		coach.setDepartment("Cheaters");
		coach.setPosition("Coach");

		ArrayList<Employee> qbList = new ArrayList<Employee>();
		Employee quarterBack = new Employee();
		quarterBack.setFirstName("Tom");
		quarterBack.setLastName("Brady");
		quarterBack.setDepartment("Cheaters");
		quarterBack.setPosition("QB");

		// post qb
		Employee qbEmployee = restTemplate.postForEntity(employeeUrl, quarterBack, Employee.class).getBody();
		assertNotNull(qbEmployee.getEmployeeId());
		assertEmployeeEquivalence(quarterBack, qbEmployee);

		// get qb
		Employee qbEmployee1 = restTemplate.getForEntity(employeeIdUrl, Employee.class, qbEmployee.getEmployeeId())
				.getBody();
		assertEquals(qbEmployee1.getEmployeeId(), qbEmployee.getEmployeeId());
		assertEmployeeEquivalence(qbEmployee, qbEmployee1);
		
		coachList.add(qbEmployee1);
		coach.setDirectReports(coachList);

		// post coach
		Employee createdEmployee = restTemplate.postForEntity(employeeUrl, coach, Employee.class).getBody();
		assertNotNull(createdEmployee.getEmployeeId());
		assertEmployeeEquivalence(coach, createdEmployee);

		// get coach
		Employee readEmployee = restTemplate
				.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
		assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
		assertEmployeeEquivalence(createdEmployee, readEmployee);
		
		ReportingStructure reportingstructure = new ReportingStructure();
		reportingstructure.setNumberOfReports(readEmployee.getDirectReports().size());
		reportingstructure.setEmployee(readEmployee);

		// create reporting structure
		ReportingStructure reportingstructure1 = restTemplate.getForEntity(reportingStructureUrl,
				ReportingStructure.class, reportingstructure.getEmployee().getEmployeeId()).getBody();
		assertEquals(reportingstructure1.getEmployee().getEmployeeId(),
				reportingstructure.getEmployee().getEmployeeId());
		assertEquals(reportingstructure1.getNumberOfReports(), reportingstructure.getNumberOfReports());
	}

	private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getDepartment(), actual.getDepartment());
		assertEquals(expected.getPosition(), actual.getPosition());
	}

}
