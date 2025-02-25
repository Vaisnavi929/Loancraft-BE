package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "LmsCurrentAddress")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrentAddress extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cAddressId;
	private String address;
	private String city;
//	private String country;
	private String location;
	private String landmark;
	private Long stayedOfYears;
	private Long pincode;

	@Column(name = "addedby")
	private Long addedBy;

	@Column(name = "updatedby")
	private Long updatedBy;

	@OneToOne
	@JoinColumn(name = "customerId")
	@ToString.Exclude
	@JsonBackReference
	private CustomersInfo customer;

	@ManyToOne
	@JoinColumn(name = "stateId")
	private States state;

}
