package com.narvee.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ReceiptService;
import com.narvee.commons.RestAPIResponse;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {
	private final static Logger logger = LoggerFactory.getLogger(ReceiptController.class);

	@Autowired
	private ReceiptService receiptService;

	@RequestMapping(value = "/getEmiInfo/{dId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getReceiptData(@PathVariable Long dId) {
		logger.info("!!! inside class : ReceiptController,method : getReceiptData");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully fetched Emi Info", receiptService.getReceiptInfo(dId)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getEmiInfoByReceiptDate/{dId}/{date}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getReceiptDataByReceiptDate(@PathVariable Long dId,
			@PathVariable LocalDate date) {
		logger.info("!!! inside class : ReceiptController,method : getEmiInfoByReceiptDate");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully fetched Emi Info with Receipt date",
						receiptService.getReceiptInfoByReceiptDate(dId, date)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/payLoan", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> payLoan(@RequestBody PayRequest request) {
		logger.info("!!! inside class : ReceiptController,method : payLoan{}", request);

		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully fetched Emi Info with Receipt date", receiptService.payLoan(request)), HttpStatus.OK);

	}

}
