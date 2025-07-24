package com.narvee.request.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReportDTO {
	private Map<String, Object> memberDetails;
	private List<Map<String, Object>> memberReports;
	private Map<String, Object> summaryDetails;
	private Map<String, Object> disbursedDetails;
	private Map<String, Object> dueDetails;
	private Map<String, Object> referencesDetails;

}
