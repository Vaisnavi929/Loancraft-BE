package com.narvee.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="LmsPayment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long payamentId;
	private String vocherNumber;
	private Date paymentDate;
	private String branchName;
	private String perticulars;
	private Double amount;
	private String description;
	private String bankDetails;

    @ManyToOne
    @JoinColumn(name = "loanSactionId")
	private LoanSanction loanSanction;
	
    
    @OneToMany(mappedBy ="payment", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ledger> ledger;

}
