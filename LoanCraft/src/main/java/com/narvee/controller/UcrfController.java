package com.narvee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.commons.RestAPIResponse;
import com.narvee.urcf.AccountSegment;
import com.narvee.urcf.AddressSegment;
import com.narvee.urcf.ConsumerInfoDTO;
import com.narvee.urcf.ExcelHelper;
import com.narvee.urcf.HeaderSegment;
import com.narvee.urcf.IdentificationSegment;
import com.narvee.urcf.NameSegment;
import com.narvee.urcf.TelephoneSegment;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ucrf")
public class UcrfController {

	@PostMapping("/convertUrcfFormat")
	public ResponseEntity<RestAPIResponse> ucrfDownload(@RequestParam("file") MultipartFile file) throws IOException {

		StringBuilder UcrfFormatting = new StringBuilder();
		List<ConsumerInfoDTO> consumerList = ExcelHelper.parseExcel(file.getInputStream());

		ConsumerInfoDTO headerSegment = consumerList.get(0);
		HeaderSegment.processerUserId(headerSegment.getCurrentNewMemberCode());
		HeaderSegment.processerShortName(headerSegment.getCurrentNewMemberShortName());
		HeaderSegment.dateReported(headerSegment.getDateReported());
		UcrfFormatting.append(HeaderSegment.headerSegmentFixedSegment());
		for (ConsumerInfoDTO consumerInfo : consumerList) {
			UcrfFormatting.append(NameSegment.formatNameSegment(consumerInfo.getConsumerName(),
					consumerInfo.getDateOfBirth(), consumerInfo.getGender()));

			Map<String, String> ids = new HashMap<>();
			ids.put("Universal ID Number", consumerInfo.getUniversalIdNumber());
			ids.put("Income Tax ID Number", consumerInfo.getIncomeTaxIdNumber());
			UcrfFormatting.append(IdentificationSegment.generateIdentities(ids));

			TelephoneSegment telephoneSegment = new TelephoneSegment();
			Map<String, String> tel = new HashMap<>();
			tel.put("mobile phone", consumerInfo.getTelephoneNoMobile());
			if (consumerInfo.getTelephoneNoResidence() != null && !consumerInfo.getTelephoneNoResidence().isEmpty()) {
				tel.put("home phone", consumerInfo.getTelephoneNoResidence());
			}
			
			UcrfFormatting.append(telephoneSegment.telephoneNumberGenerator(tel));
			String addressSeg = AddressSegment.splitAddress(consumerInfo.getAddress1(), consumerInfo.getStateCode1(),
					consumerInfo.getPinCode1(), consumerInfo.getAddressCategory1(), consumerInfo.getResidenceCode1());
			UcrfFormatting.append(addressSeg);
			AccountSegment acSeg = AccountSegment.mapConsumerDTOToAccountSegment(consumerInfo);

			AccountSegment finalAcSegment = new AccountSegment();
			String fAccount = finalAcSegment.accountSegment(acSeg);
			UcrfFormatting.append(fAccount);
			UcrfFormatting.append("ES02**");
		}
		UcrfFormatting.append("TRLR");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "converted successfully", UcrfFormatting.toString()), HttpStatus.OK);
	}

}
