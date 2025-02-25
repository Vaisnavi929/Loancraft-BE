package com.narvee.urcf;

import lombok.Data;

@Data
public class HeaderSegment {
	

	public static void processerUserId(String currentNewMemberCode) {
		processerUserId=currentNewMemberCode;
	}

	public static void processerShortName(String currentNewMemberShortName) {
		processerShortName=currentNewMemberShortName;
		
	}
	
	public static void dateReported(String reportedDate) {
		dateReported=reportedDate;
		
	}
	

	private  static  String headerSegmentTag = "TUDF"; // 4 bytes
	private  static String version = "12"; // 2 bytes
	private  static String processerUserId; // 30 bytes
	private  static String processerShortName; // 16 bytes
	private  static String cycleIdentification; // 2 bytes
	private  static String dateReported; // 8 bytes (formatted)
	private  static String reportingPassword; // 30 bytes
	private  static String authenticationMethod="L"; // 1 byte
	private  static String futureUse = "00000"; // 5 bytes
	private  static String memberData; // 48 bytes

	public static String headerSegmentFixedSegment() {
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		return padRight(headerSegmentTag, 4) + padRight(version, 2) + padRight(processerUserId, 30)
				+ padRight(processerShortName, 16) + padRight(cycleIdentification, 2)
				+ padRight(dateReported , 8)
				+ padRight(reportingPassword, 30) + padRight(authenticationMethod, 1) + padRight(futureUse, 5)
				+ padRight(memberData, 48);
	}
	
	

	public static String padRight(String value, int length) {
		if (value == null) {
			value = "";
		}
		return String.format("%-" + length + "s", value);
	}





}
