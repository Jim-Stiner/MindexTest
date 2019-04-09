package com.mindex.challenge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@RestController
public class ReportingStructureController {
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

	@Autowired
	private ReportingStructureService reportingService;

	@GetMapping("/reportingstructure/{id}")
	public ReportingStructure read(@PathVariable String id) {
		LOG.debug("Gathering reporting structure for [{}]", id);		
		try {
			return reportingService.read(id);
		} catch (Exception e) {			
			throw new RuntimeException("Invalid employeeId: " + id);
		}

	}	
}
