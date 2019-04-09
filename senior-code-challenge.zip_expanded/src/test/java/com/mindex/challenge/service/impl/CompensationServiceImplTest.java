package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.ReportingStructureService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

	private String employeeUrl;
	private String employeeIdUrl;
	private String compensationUrl;
	private String compensationIdUrl;

	@Autowired
	private CompensationService compensationService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
		compensationUrl = "http://localhost:" + port + "/compensation";
		employeeUrl = "http://localhost:" + port + "/employee";
		employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
	}
	
	@Test
	public void Test()
	{
		// test post
		Employee coach = new Employee();
		coach.setFirstName("Bill");
		coach.setLastName("Belly");
		coach.setDepartment("Cheaters");
		coach.setPosition("Coach");
		
		Compensation comp = new Compensation();
		comp.setEffectiveDate(new Date());
		comp.setSalary("10,000");
		
		Employee createdEmployee = restTemplate.postForEntity(employeeUrl, coach, Employee.class).getBody();
		assertNotNull(createdEmployee.getEmployeeId());
		assertEmployeeEquivalence(coach, createdEmployee);

		// get coach
		Employee readEmployee = restTemplate
				.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
		assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
		assertEmployeeEquivalence(createdEmployee, readEmployee);
		
		comp.setEmployee(readEmployee);
		
		//post to comp
		Compensation compEmployee = restTemplate.postForEntity(compensationUrl, comp, Compensation.class).getBody();		
		assertEquals(compEmployee.getSalary(), comp.getSalary());
		assertEmployeeEquivalence(coach, compEmployee.getEmployee());
		
		// test get
		Compensation compemp = restTemplate.getForEntity(compensationIdUrl,
				Compensation.class, compEmployee.getEmployee().getEmployeeId()).getBody();
		
		assertEquals(compemp.getEmployee().getEmployeeId(), compEmployee.getEmployee().getEmployeeId());
		assertEquals(compemp.getSalary(), comp.getSalary());
		assertEmployeeEquivalence(coach, compemp.getEmployee());
		
		
	}
	
	  private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
	        assertEquals(expected.getFirstName(), actual.getFirstName());
	        assertEquals(expected.getLastName(), actual.getLastName());
	        assertEquals(expected.getDepartment(), actual.getDepartment());
	        assertEquals(expected.getPosition(), actual.getPosition());
	    }	  

}
