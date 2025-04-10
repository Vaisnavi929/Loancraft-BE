package com.narvee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.StateCodeService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.States;
import com.narvee.request.dto.StateCodeDTO;

@RestController
@RequestMapping("/auth/statecode")
@CrossOrigin("*")
public class StatesController {
	
	
	@Autowired
	private StateCodeService stateCodeService;
	
	
	@PostMapping(value ="/saveStatecode")
	public ResponseEntity<RestAPIResponse> saveStateCode(@RequestBody StateCodeDTO stateCodeDTO ){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully", stateCodeService.saveStatecode(stateCodeDTO)), HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/updateStatecode")
	public ResponseEntity<RestAPIResponse> updateStateCode(@RequestBody States stateCode){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully", stateCodeService.updateStatedCode(stateCode)), HttpStatus.OK);
		
	}
	
	@GetMapping(value ="/getById/{stateId}")
	public ResponseEntity<RestAPIResponse> getById(@RequestParam Long stateId){
	return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetchedById", stateCodeService.getById(stateId)), HttpStatus.OK);
		
	}
	
	@GetMapping(value ="/All")
	public ResponseEntity<RestAPIResponse> getAllStateCode(){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success","successfully fetched all statedcodes ", stateCodeService.getAll()), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/deleteById/{stateId}")
	public ResponseEntity<RestAPIResponse> deleteById(@RequestParam Long stateId){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully deleteById", stateCodeService.deltedById(stateId)), HttpStatus.OK);
		
		
	}

}
