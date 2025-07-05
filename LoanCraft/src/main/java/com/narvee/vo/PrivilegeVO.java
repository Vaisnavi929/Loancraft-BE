package com.narvee.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class PrivilegeVO {

	private List<DropdownVO> user;

	private List<DropdownVO> role;

	private List<DropdownVO> dashboard;

	private List<DropdownVO> home;

	private List<DropdownVO> hrm;

	private List<DropdownVO> customerinfo;

	private List<DropdownVO> customer_dashboard;

	private List<DropdownVO> applications;

	private List<DropdownVO> loans;

	private List<DropdownVO> approved;

	private List<DropdownVO> sanctioned;

	private List<DropdownVO> disbursed;

	private List<DropdownVO> rejected;

	private List<DropdownVO> receivables;

	private List<DropdownVO> currentDues;

	private List<DropdownVO> receivable;

	private List<DropdownVO> applicationTracking;

	private List<DropdownVO> physicalVerification;
	
	private List<DropdownVO> chequeDeposit;

	private List<DropdownVO> lowChequeRequests;

}
