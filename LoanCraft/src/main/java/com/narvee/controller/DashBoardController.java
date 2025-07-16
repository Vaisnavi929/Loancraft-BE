package com.narvee.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.DashBoardService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.serviceimpl.ReceiptServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/dashboard")
public class DashBoardController {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	@Autowired
	private DashBoardService dashBoardService;

	@RequestMapping(value = "/getDashboardSummary", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getDashboardSummary() {
		logger.info("!!! inside class:DashBoardController ,!! method:getTotalDisbursmentAmount() ");
		Map<String, Object> totalDisbursmentAmount = dashBoardService.getDashboardSummary();
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully getTotalDisbursmentAmount", totalDisbursmentAmount),
				HttpStatus.OK);

	}

}
