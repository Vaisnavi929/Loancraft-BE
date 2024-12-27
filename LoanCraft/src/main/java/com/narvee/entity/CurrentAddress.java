package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "LmsCurrentAdress")
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cAddressId;
	private String address;
	private String city;
	private String state;
	private String country;
	private String location;
	private String landmark;
	private Long stayedOfYears;
	private Long pincode;
	private Long statecode;
	
	
	@OneToOne
	@JoinColumn(name="customerId")
	@ToString.Exclude
	@JsonBackReference
	private Customers customer;


	
//	@OneToOne
//    @JsonManagedReference
//    private States states;

}
