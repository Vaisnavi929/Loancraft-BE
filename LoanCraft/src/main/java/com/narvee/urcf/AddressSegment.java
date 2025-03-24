package com.narvee.urcf;

import lombok.Data;

@Data
public class AddressSegment {

	private static final String SEGMENT_ID = "PA03A01";
	private static String addressline1 = "";
	private static String addressline2 = "";
	private static String addressline3 = "";
	private static String addressline4 = "";
	private static String addressline5 = "";

	public static String splitAddress(String address, String stateCode, String pinCode, String addressCategory,
			String residenceCode) {
		StringBuilder finalAddress = new StringBuilder();

		int maxLength = 40;
		String[] fields = new String[5]; // Holds values for each field
		address = address.replaceAll("[^a-zA-Z0-9 ]", " ");
		address=address.replaceAll("\\s{2,}", " ");

		String remainingText = address;

		for (int i = 0; i < fields.length && !remainingText.isEmpty(); i++) {
			if (remainingText.length() > maxLength) {
				int lastSpaceIndex = remainingText.substring(0, maxLength).lastIndexOf(" ");
				if (lastSpaceIndex == -1) {
					lastSpaceIndex = maxLength; // If no space, force split
				}
				String part = remainingText.substring(0, lastSpaceIndex);
				remainingText = remainingText.substring(lastSpaceIndex).trim();
				fields[i] = String.format("0%d%02d%s", i + 1, part.length(), part);
			} else {
				fields[i] = String.format("0%d%02d%s", i + 1, remainingText.length(), remainingText);
				remainingText = "";
			}
		}

		addressline1 = fields[0] != null ? fields[0] : "";
		addressline2 = fields[1] != null ? fields[1] : "";
		addressline3 = fields[2] != null ? fields[2] : "";
		addressline4 = fields[3] != null ? fields[3] : "";
		addressline5 = fields[4] != null ? fields[4] : "";

		finalAddress.append(SEGMENT_ID).append(addressline1).append(addressline2).append(addressline3)
				.append(addressline4).append(addressline5).append("0602").append(stateCode).append("07")
				.append(String.format("%02d", pinCode.length())).append(pinCode).append("0802")
				.append(getAddressCategory(addressCategory)).append("0902").append(getOwnershipTypeCode(residenceCode));

		return finalAddress.toString();
	}

	public static String getOwnershipTypeCode(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}
		switch (input) {
		case "01":
		case "Owned":
			return "01";
		case "02":
		case "Rented":
			return "02";
		default:
			return "";
		}
	}

	public static String getAddressCategory(String addressTag) {
		if (addressTag == null || addressTag.isEmpty()) {
			return "04";
		}
		switch (addressTag) {
		case "01":
		case "Permanent Address":
			return "01";
		case "02":
		case "Residence Address":
			return "02";
		case "03":
		case "Office Address":
			return "03";
		default:
			return "04";
		}
	}

	public static String getStateCode(String input) {
		if (input == null || input.isEmpty()) {
			return "Unknown";
		}
		switch (input) {
		case "01":
		case "Jammu & Kashmir":
			return "01";
		case "02":
		case "Himachal Pradesh":
			return "02";
		case "03":
		case "Punjab":
			return "03";
		case "04":
		case "Chandigarh":
			return "04";
		case "05":
		case "Uttaranchal":
			return "05";
		case "06":
		case "Haryana":
			return "06";
		case "07":
		case "Delhi":
			return "07";
		case "08":
		case "Rajasthan":
			return "08";
		case "09":
		case "Uttar Pradesh":
			return "09";
		case "10":
		case "Bihar":
			return "10";
		case "11":
		case "Sikkim":
			return "11";
		case "12":
		case "Arunachal Pradesh":
			return "12";
		case "13":
		case "Nagaland":
			return "13";
		case "14":
		case "Manipur":
			return "14";
		case "15":
		case "Mizoram":
			return "15";
		case "16":
		case "Tripura":
			return "16";
		case "17":
		case "Meghalaya":
			return "17";
		case "18":
		case "Assam":
			return "18";
		case "19":
		case "West Bengal":
			return "19";
		case "20":
		case "Jharkhand":
			return "20";
		case "21":
		case "Orissa":
			return "21";
		case "22":
		case "Chhattisgarh":
			return "22";
		case "23":
		case "Madhya Pradesh":
			return "23";
		case "24":
		case "Gujarat":
			return "24";
		case "25":
		case "Daman & Diu":
			return "25";
		case "26":
		case "Dadra & Nagar Haveli and Daman & Diu":
			return "26";
		case "27":
		case "Maharashtra":
			return "27";
		case "28":
		case "Andhra Pradesh":
			return "28";
		case "29":
		case "Karnataka":
			return "29";
		case "30":
		case "Goa":
			return "30";
		case "31":
		case "Lakshadweep":
			return "31";
		case "32":
		case "Kerala":
			return "32";
		case "33":
		case "Tamil Nadu":
			return "33";
		case "34":
		case "Pondicherry":
			return "34";
		case "35":
		case "Andaman & Nicobar Islands":
			return "35";
		case "36":
		case "Telangana":
			return "36";
		case "38":
		case "Ladakh":
			return "38";
		case "99":
		case "APO Address":
			return "99";
		default:
			return "Unknown";
		}
	}
}
