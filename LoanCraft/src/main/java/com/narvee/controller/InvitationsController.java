package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.InvitationsService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Invitations;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth/invitations")
@CrossOrigin("*")
public class InvitationsController {
	
	private static final Logger logger = LoggerFactory.getLogger(InvitationsController.class);
    
	@Autowired
	InvitationsService invitationsService;
	
	@RequestMapping(value = "/invite",method = RequestMethod.POST,produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveInvitations(@RequestBody Invitations  invitations) throws MessagingException{
		logger.info("!!! inside class : InvitationsController , method : saveInvitations");
		invitationsService.saveInvititions(invitations);
		return new ResponseEntity<>(new RestAPIResponse("Success","Invitation Saved Succesfully"), HttpStatus.OK);
	}
}
