package com.narvee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.emi.calculator.EmiCalculatorUtill;
import com.narvee.request.dto.EmiCalResponseDTO;
import com.narvee.request.dto.RepaymentScheduleRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class RepaymentScheduleController {

	@PostMapping("/calculateschedule")
	public ResponseEntity<EmiCalResponseDTO> calculateRepaymentSchedule(@RequestBody RepaymentScheduleRequest request) {
		EmiCalResponseDTO schedule = EmiCalculatorUtill.calculateRepaymentSchedule(request);
		return ResponseEntity.ok(schedule);
	}

}
