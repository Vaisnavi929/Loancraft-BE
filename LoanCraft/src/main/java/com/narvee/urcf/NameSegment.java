package com.narvee.urcf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NameSegment {

	private static final String NAME_SEGMENT = "PN03N01";

	public static String formatNameSegment(String consumerName, String dateOfBirth, String gender) {
		String[] nameFields = splitConsumerName(consumerName);
		String dob = "0708" + dateOfBirth;
		String genderCode = formatGender(gender);
		System.err.println(genderCode);
		return NAME_SEGMENT + nameFields[0] + nameFields[1] + nameFields[2] + nameFields[3] + nameFields[4] + dob
				+ genderCode;
	}

	private static String[] splitConsumerName(String consumerName) {
		int maxLength = 26;
		String[] fields = new String[5];
		String remainingText = consumerName;

		for (int i = 0; i < fields.length && !remainingText.isEmpty(); i++) {
			if (remainingText.length() > maxLength) {
				int lastSpaceIndex = remainingText.substring(0, maxLength).lastIndexOf(" ");
				if (lastSpaceIndex == -1) {
					lastSpaceIndex = maxLength;
				}
				String part = remainingText.substring(0, lastSpaceIndex);
				remainingText = remainingText.substring(lastSpaceIndex).trim();

				fields[i] = String.format("0%d%02d%s", i + 1, part.length(), part);
			} else {
				fields[i] = String.format("0%d%02d%s", i + 1, remainingText.length(), remainingText);
				remainingText = "";
			}
		}

		for (int i = 0; i < fields.length; i++) {
			if (fields[i] == null) {
				fields[i] = "";
			}
		}

		return fields;
	}

	private static String formatDob(LocalDate dob) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		return "0708" + dob.format(formatter);
	}

	private static String formatGender(String gender) {
		switch (gender.toLowerCase()) {
		case "female":
		case "1":
			return "08011"; // Both map to the same code
		case "male":
		case "2":
			return "08012"; // Both map to the same code
		case "transgender":
		case "3":
			return "08013"; // Both map to the same code
		default:
			throw new IllegalArgumentException("Invalid gender: " + gender);
		}
	}
}
