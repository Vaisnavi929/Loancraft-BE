package com.narvee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.IRolesService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Roles;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.RolesDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/roles")
public class RolesController {
	public static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	private IRolesService Service;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> AddRoles(@RequestBody Roles roles) {
		logger.info("!!! inside class: RolesController, method: AddRoles");
		boolean flg = Service.saveRole(roles);
		if (flg) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Role Saved", "Success"), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("Fail", "Role Already Exist", "Data not Saved"),
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles() {
		logger.info("!!! inside class: RolesController, method: getAllRoles");
		List<Roles> saveroles = Service.getAllRoles();
		return new ResponseEntity<>(new RestAPIResponse("success", "All Roles Fetched", saveroles), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllRoles", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRoles(@RequestBody RequestDTO request) {
		logger.info("!!! inside class: RolesController, method: getRoles");
		Page<RolesDTO> saveroles = Service.getAllRoles(request);
		return new ResponseEntity<>(new RestAPIResponse("success", "All Roles Fetched", saveroles), HttpStatus.OK);
	}

	@RequestMapping(value = "/getrole/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRole(@PathVariable Long id) {
		Roles saveroles = Service.getRole(id);
		return new ResponseEntity<>(new RestAPIResponse("success", "Role Feteched By ID", saveroles), HttpStatus.OK);
	}

	@RequestMapping(value = "/updaterole", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> UpdateRoles(@RequestBody Roles roles) {
		boolean flg = Service.updateRole(roles);
		if (flg) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Role Updated", "Updated"), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("Fail", "Role Already Exist", "Data not Saved"),
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteRole(@PathVariable Long id) {
		boolean val = Service.deleteRole(id);
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Role Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("fail", "Role Assigned to User, Role Not Deleted", ""),
					HttpStatus.OK);
		}
	}

}
