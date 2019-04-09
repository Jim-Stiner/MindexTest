package com.mindex.challenge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

@RestController
public class CompensationController {
	 private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

	    @Autowired
	    private CompensationService compensationService;

	   // post to create a compensation object and store in repo
	   // Payload = Compensation
	   // Response = Compensation
	    
	    @PostMapping("/compensation")
	    public Compensation create(@RequestBody Compensation comp) {
	        LOG.debug("Received Compensation create request for [{}]", comp);
	        try{
	        	return compensationService.create(comp);
	        }catch (Exception e){
	        	throw new RuntimeException("Error trying to create compensation object ");
	        }
	        
	    }

	    // get used to gather compensation object based on employee id
	    // Response = Compensation
	    // id variable is the employee id 
	    @GetMapping("/compensation/{id}")
	    public Compensation read(@PathVariable String id) {
	        LOG.debug("Received compensation read requst for id [{}]", id);	 
	        try{
	        	return compensationService.read(id);
	        }catch (Exception e){
	        	throw new RuntimeException("Invalid Id " + id);
	        }
	        
	    }
	    
	    // endpoint to get all the compensation objects
	    // used for testing 
	    @GetMapping("/compensation")
	    public List<Compensation> read() {
	        LOG.debug("Received ALL compensation read requst");	        
	        return compensationService.read();
	    }
}
