package com.narvee.urcf;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationSegment {

	private Map<String, String> ids;

	public static String generateIdentities(Map<String, String> ids) {
		StringBuilder idNumbers = new StringBuilder();

		int i = 1;
		// ID03I010102010210BESPA5756AID03I0201060212692734330610
		for (Map.Entry<String, String> entry : ids.entrySet()) {
			idNumbers.append("ID03");
			idNumbers.append("I0" + i + "0102" + getIdTypeCode(entry.getKey()) + "02" + entry.getValue().length()
					+ entry.getValue());
			i++;
		}
		return idNumbers.toString();

	}

	private static String getIdTypeCode(String idType) {

		switch (idType.toLowerCase()) {
		case "income tax id number":
		case "pan":
			return "01";
		case "passport":
			return "02";
		case "voter id":
			return "03";
		case "driver’s license":
		case "driver license":
			return "04";
		case "ration card":
			return "05";
		case "universal id number":
		case "uid":
			return "06";
		case "additional id #1":
			return "07";
		case "additional id #2":
			return "08";
		default:
			throw new IllegalArgumentException("Invalid ID type: " + idType);
		}

	}

}