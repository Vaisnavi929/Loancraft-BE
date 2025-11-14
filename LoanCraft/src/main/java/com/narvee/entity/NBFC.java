package com.narvee.entity;

import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_nbfc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class NBFC extends AuditModel {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nbfc_name")
	private String nbfcName;
	@Column(name = "regionalOffice")
	private String regionalOffice;
	
	@Column(name = "classification")
	private String classification;
	
	@Column(name = "corporateIdentificationNo")
	private String corporateIdentificationNo;
	
	@Column(name = "layer")
	private String layer;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "email")
	private String email;

	@Column(name = "products")
	private String products;
	
	@Column(name = "partners")
	private String partners;
	
	@Column(name = "typeOfOrganization")
	private String typeOfOrganization;
	
    @Override
    public String toString() {
        return "NBFC{" +
                "id=" + id +
                ", nbfcName='" + nbfcName + '\'' +
                ", regionalOffice='" + regionalOffice + '\'' +
                ", classification='" + classification + '\'' +
                ", corporateIdentificationNo='" + corporateIdentificationNo + '\'' +
                ", layer='" + layer + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", products='" + products + '\'' +
                ", partners='" + partners + '\'' +
                ", typeOfOrganization='" + typeOfOrganization + '\'' +
                "} " + super.toString();
    }
}
