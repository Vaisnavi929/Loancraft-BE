package com.narvee.response;
import java.util.List;

import lombok.Data;

@Data
public class Response {
	private String fullname;
	private long userid;
	private String token;
	private String roles;
	private long roleno;
	private String department;
	private String designation;
	private List<String> rolePrivileges;
}
