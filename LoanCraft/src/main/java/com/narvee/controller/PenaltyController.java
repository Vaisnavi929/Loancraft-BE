package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.PenaltyService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.PenaltyDto;
import com.narvee.serviceimpl.PenaltyServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/penalty")
public class PenaltyController {
	
	private final static Logger logger=LoggerFactory.getLogger(PenaltyServiceImpl.class);
	
	@Autowired
	private PenaltyService penaltyService;
	
	@RequestMapping(value ="/savePenalty",method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<RestAPIResponse> savePenalty(@RequestBody PenaltyDto penalty){
		logger.info("!!! inside class : PenaltyController,method : savePenalty");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Succssfully saved penalty Details", penaltyService.savePenalty(penalty)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/updatePenalty",method = RequestMethod.PUT,produces = "application/json")
	public ResponseEntity<RestAPIResponse> upatePenalty(@RequestBody PenaltyDto penalty){
		logger.info("!!! inside class : PenaltyController,method : savePenalty");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Succssfully updeted penalty Details", penaltyService.updatePenalty(penalty)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/getById/{penaltyId}",method =RequestMethod.GET,produces ="application/json")
	public  ResponseEntity<RestAPIResponse> getByPenaltyId(@PathVariable Long penaltyId){
		logger.info("!!! inside class : PenaltyController,method : getByPenaltyId");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Succssfully fetched penaltyid", penaltyService.getByPenaltyId(penaltyId)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/getAll",method =RequestMethod.GET,produces ="application/json")
	public  ResponseEntity<RestAPIResponse> getAllPenalty(){
		logger.info("!!! inside class : PenaltyController,method : getAllPenalty");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Succssfully fetched all penalty Details", penaltyService.getAllPenalty()), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/delete/{penaltyId}",method =RequestMethod.DELETE,produces ="application/json")
	public  ResponseEntity<RestAPIResponse> deleteByPenaltyId(@PathVariable Long penaltyId){
		logger.info("!!! inside class : PenaltyController,method : getByPenaltyId");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Succssfully deleted  penalty Details", penaltyService.delateById(penaltyId)), HttpStatus.OK);
		
	}
}
