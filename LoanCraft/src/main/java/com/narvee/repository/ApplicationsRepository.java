package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Applications;
import com.narvee.request.dto.ApplicationDTO;
import com.narvee.request.dto.LoanApplicationDTOS;
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
	
	@Query(value = "select max(amaxnum) as max from lms_applications", nativeQuery = true)
	public Long appmaxNumber();
	
//	public boolean existsByAadharNumber(String aadharNumber);

	@Query(value ="SELECT amaxnum,application_reference_id,employment_type,salary,yearsin_employment,company_name,company_address,pin_code,loan_amount,repayment_mode,loan_tenure ,status FROM lms_applications",nativeQuery = true)
	public Page<LoanApplicationDTOS> getAllLoanApplication(Pageable pageable);
	
	@Query(value ="SELECT amaxnum,application_reference_id,employment_type,salary,yearsin_employment,company_name,company_address,pin_code,loan_amount,repayment_mode,loan_tenure ,status FROM lms_applications "
			+ " where  amaxnum LIKE CONCAT('%',:keyword, '%') OR application_reference_id LIKE CONCAT('%',:keyword, '%')  OR employment_type LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR company_name LIKE CONCAT('%',:keyword, '%') OR company_address LIKE CONCAT('%',:keyword, '%') OR pin_code LIKE CONCAT('%',:keyword, '%') OR loan_amount LIKE CONCAT('%',:keyword, '%')"
			+ "OR repayment_mode LIKE CONCAT('%',:keyword, '%') OR loan_tenure LIKE CONCAT('%',:keyword, '%')",nativeQuery = true)
	public Page<LoanApplicationDTOS> getAllLoanApplicationWithKeyword(Pageable pageable,@Param("keyword")String keyword);

	

	@Query(value ="SELECT c.first_name,c.last_name,c.email,a.status,c.mobile_number,a.repayment_mode,a.loan_amount FROM lms.lms_customers_info c join lms_applications a on a.customer_id=c.customer_id WHERE a.status = 'Approved'",nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListApproved(Pageable pageable);

	
	@Query(value ="SELECT a.loan_id,a.application_reference_id c.first_name,c.last_name,c.email,a.status,c.mobile_number FROM lms_customers_info c join lms_applications a on a.customer_id=c.customer_id \n"
			+ "where c.first_name LIKE CONCAT('%', :keyword,'%') OR c.last_name LIKE CONCAT('%',:keyword, '%') OR c.email LIKE CONCAT('%',:keyword, '%') OR \n"
			+ "a.status LIKE CONCAT('%', :keyword, '%') OR c.mobile_number LIKE CONCAT('%',:keyword, '%') AND  a.status = 'Approved'",nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListWithKewordApproved(Pageable pageable,@Param("keyword")String keyword);
}

 