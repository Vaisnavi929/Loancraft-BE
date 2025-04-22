package com.narvee.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.VerificationService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Verification;
import com.narvee.serviceimpl.VerificationServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/verification")
public class VerificationController {

	
	private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);
	
	@Autowired
	private VerificationService verificationService; 
	
	@RequestMapping(value="/saveVerification",method =RequestMethod.POST,produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveVerification(@RequestBody Verification verification){
		logger.info("!!! inside class: VerificationController, method: saveVerification");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully saved Verification Details ", verificationService.saveVerification(verification)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/updateVerification",method=RequestMethod.PUT,produces="application/json")
	public ResponseEntity<RestAPIResponse> updateVerification(@RequestBody Verification verification){
		logger.info("!!! inside class: VerificationController, method: updateVerification");
	return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated verification daetails", verificationService.updaterificationVerification(verification)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/getById/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long verifiedId){
		logger.info("!!! inside class: VerificationController, method: getById");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",  "successfully fetched By id", verificationService.getById(verifiedId)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/getAll",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> getAllverification(){
		logger.info("!!! inside class: VerificationController, method: getAllverification");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched all verification ", verificationService.getAllVerification()), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/delete/{verifiedId}",method=RequestMethod.DELETE,produces="application/json")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long verifiedId){
		logger.info("!!! inside class: VerificationController, method: deleteById");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully delete By id ", verificationService.deleteById(verifiedId)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/uploaDocument/{verifiedId}",method=RequestMethod.POST,produces="application/json")
    public ResponseEntity<RestAPIResponse> uploadDocumentToVerification( @PathVariable("verifiedId") Long verifiedId,   @RequestParam("files") List<MultipartFile> files) throws IOException{
		logger.info("!!! inside class: VerificationController, method: uploadDocumentToVerification");
	return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully uploaded document ", verificationService.uploadDocumentsToVerification(verifiedId, files)), HttpStatus.OK);
	
}
	@RequestMapping(value ="/updateReviewed/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> updateReviewed(Long verifiedId, Boolean value){
		logger.info("!!! inside class: VerificationController, method: updateReviewed");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update reviewed ", verificationService.updateReviewed(verifiedId, value)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateCibil/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> updateCibil(Long verifiedId, Boolean value){
		logger.info("!!! inside class: VerificationController, method: updateReviewed");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update reviewed ", verificationService.updateCibil(verifiedId, value)), HttpStatus.OK);
		
	}

	@RequestMapping(value ="/updatePhysicalLocationVerified/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> updatePhysicalLocationVerified(Long verifiedId, Boolean value){
		logger.info("!!! inside class: VerificationController, method: updateReviewed");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update reviewed ", verificationService.updatePhysicalLocationVerified(verifiedId, value)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateSanction/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> updateSanction(Long verifiedId, Boolean value){
		logger.info("!!! inside class: VerificationController, method: updateReviewed");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update reviewed ", verificationService.updateSanction(verifiedId, value)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateDisbursed/{verifiedId}",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestAPIResponse> updateDisbursed(Long verifiedId, Boolean value){
		logger.info("!!! inside class: VerificationController, method: updateReviewed");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update reviewed ", verificationService.updateDisbursed(verifiedId, value)), HttpStatus.OK);
		
	}
}
