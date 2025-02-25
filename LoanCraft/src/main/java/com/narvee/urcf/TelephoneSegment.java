package com.narvee.urcf;

import java.util.Map;

import lombok.Data;

@Data
public class TelephoneSegment {

	private String segmentTag = "PT03";

	public String telephoneNumberGenerator(Map<String, String> telephoneNumbers) {

		StringBuilder tNumbers = new StringBuilder();
		int i = 1;
		for (Map.Entry<String, String> entry : telephoneNumbers.entrySet()) {
			String formattedIndex = String.format("T%02d", i);
			// Ensures T01, T02, ..., T10
			tNumbers.append(segmentTag).append(formattedIndex).append("01").append(entry.getValue().length())
					.append(entry.getValue()).append("0302").append(getTelephoneTypeCode(entry.getKey()));
			i++;
		}

		return tNumbers.toString();
	}

	private String getTelephoneTypeCode(String telephoneType) {
		if (telephoneType == null || telephoneType.trim().isEmpty()) {
			return "00"; // Default to Not Classified
		}

		switch (telephoneType.toLowerCase().trim()) {
		case "mobile phone":
			return "01";
		case "home phone":
			return "02";
		case "office phone":
			return "03";
		default:
			return "00"; // Default for invalid type
		}
	}

}
