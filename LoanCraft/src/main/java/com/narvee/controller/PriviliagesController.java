package com.narvee.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.IPrivilegeService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Privilege;
import com.narvee.vo.PrivilegeVO;
import com.narvee.vo.RoleToPrivilegesVO;


@RestController
@RequestMapping("/priviliges")
@CrossOrigin
public class PriviliagesController {

	private static final Logger logger = LoggerFactory.getLogger(PriviliagesController.class);
	
	@Autowired
	private IPrivilegeService privilegeService;
	
	@RequestMapping(value = "/savePrevileges", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> savePrevg(@RequestBody Privilege previleges) {
		logger.info("!!! inside class: PriviliagesController , !! method: savePrevg");
		privilegeService.savePrevileges(previleges);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Previleges Saved"), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> listAllPrev() {
		logger.info("!!! inside class: PriviliagesController , !! method: listAllPrev");
		List<Privilege> privileges = privilegeService.allprev();
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", privileges), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPrivileges", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles() {
		logger.info("!!! inside class: PriviliagesController , !! method: getAllRoles");
		PrivilegeVO privileges = privilegeService.getAllPrivileges();
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", privileges), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addprevtorole", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> addprevtorole(@RequestBody RoleToPrivilegesVO previleges) {
		logger.info("!!! inside class: PriviliagesController , !! method: addprevtorole");
		System.err.println("roleid:"+previleges.getRoleId());
		privilegeService.addPrivilegeToRole(previleges);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Previleges Saved"), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getPrivilegesById/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<RestAPIResponse> checkPriviliage(@PathVariable long roleId) {
		logger.info("!!! inside class: PriviliagesController , !! method: checkPriviliage");
		PrivilegeVO privileges = privilegeService.getPrivilegesById(roleId);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "priviliges feteched", privileges),
				HttpStatus.OK);
	}
	
	
}
