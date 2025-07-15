
package com.narvee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.RepaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.MemberReportDTO;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/payment")
public class RepaymentController {

	private static final Logger logger = LoggerFactory.getLogger(RepaymentController.class);

	@Autowired
	private RepaymentService paymentService;

	@RequestMapping(value = "/savePayment", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> savePayement(@RequestBody PaymentDTO paymentDTO) {
		logger.info("!!! inside class : RepaymentController , method : savepayement() ");
		paymentService.saveRepayment(paymentDTO);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully saved payment"),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/updatePayment", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updatePayement(@RequestBody PaymentDTO paymentDTO) {
		logger.info("!!! inside class : RepaymentController , method : updatePayement() ");
		paymentService.saveRepayment(paymentDTO);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully Updated payment"),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getPayment/{paymentId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long paymentId) {
		logger.info("!!! inside class : RepaymentController , method : getById() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully fetchedBy paymentId  payment", paymentService.getRepaymentById(paymentId)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllPaymentList", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllPaymentList(@RequestBody SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class : RepaymentController , method : getAllPaymentList() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched payment",
				paymentService.getAllRepaymentList(sortingRequestDto)), HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteById/{paymentId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deletedById(@PathVariable Long paymentId) {
		logger.info("!!! inside class : RepaymentController , method : deletedById() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully deleteBy Id payment",
				paymentService.deleteById(paymentId)), HttpStatus.OK);
	}

	@GetMapping("/payment-statement/{disbursmentId}")
	public ResponseEntity<List<PaymentStatementDTO>> getRepaymentSchedule(@PathVariable Long disbursmentId) {
		logger.info("!!! inside class : RepaymentController , method : getRepaymentSchedule() ");
		List<PaymentStatementDTO> statements = paymentService.getRepaymentStatement(disbursmentId);
		return ResponseEntity.ok(statements);
	}

	@PostMapping("/getMonthlyDueList")
	public ResponseEntity<Page<PaymentStatementResponseDTO>> getMonthlyDueList(
			@RequestBody SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class : RepaymentController , method : getMonthlyDueList() ");
		Page<PaymentStatementResponseDTO> statements = paymentService.getMonthlyDueList(sortingRequestDto);
		return ResponseEntity.ok(statements);
	}


	@RequestMapping(value = "/getAllRepaymentTransactions/{disbursmentId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRepaymentTransactionsByDisbursmentId(
			@PathVariable Long disbursmentId) {
		logger.info("!!! inside class : RepaymentController , method : getAllRepaymentTransactionsByDisbursmentId() ");
		MemberReportDTO report = paymentService.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully repayamentTransactions by disubursmentid", report),
				HttpStatus.OK);

	}

	@GetMapping("/generateRepayment/{disbursmentId}")
	public ResponseEntity<String> generateRepayment(@PathVariable Long disbursmentId) {
		logger.info("!!! inside class : RepaymentController , method : generateRepayment() ");
		paymentService.generateRepayment(disbursmentId);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/generate")
	public ResponseEntity<String> generate() {
		logger.info("!!! inside class : RepaymentController , method : generateRepayment() ");
		paymentService.generate();
		return ResponseEntity.ok("success");
	}
	

}
