package com.narvee.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.emi.calculator.EmiCalculatorUtill;
import com.narvee.emi.calculator.RepaymentSchedule;
import com.narvee.request.dto.RepaymentScheduleRequest;


@RestController
@CrossOrigin("*")
@RequestMapping("/emi")
public class RepaymentScheduleController {
	
    @PostMapping("/calculateschedule")
    public ResponseEntity<List<RepaymentSchedule>> calculateRepaymentSchedule(@RequestBody RepaymentScheduleRequest request) {
       List<RepaymentSchedule> schedule = EmiCalculatorUtill.calculateRepaymentSchedule(request);
        return ResponseEntity.ok(schedule);
    }
	

}
