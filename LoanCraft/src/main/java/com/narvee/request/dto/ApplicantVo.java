package com.narvee.request.dto;

import java.util.List;

import com.narvee.entity.CurrentAddress;
import com.narvee.entity.IdentityProof;
import com.narvee.entity.PermanentAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantVo {
	
	private String firstName;
	private String lastname;
	private String mobileNo;
	private String email;
	private String password;

	private CurrentAddress currentAddress;
	
	private PermanentAddress permanentAddress;
	
	private List<IdentityProof> identityProofs;

}
