package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Applications;
import com.narvee.request.dto.LoanApplicationDTOS;
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
	
	@Query(value = "select max(amaxnum) as max from lms_loan_application", nativeQuery = true)
	public Long appmaxNumber();
	
//	public boolean existsByAadharNumber(String aadharNumber);

	@Query(value ="SELECT fullname,mail_id,mobile_number,application_reference_id,date_of_birth,status FROM lms_loan_application",nativeQuery = true)
	public Page<LoanApplicationDTOS> getAllLoanApplication(Pageable pageable);
	
	@Query(value ="SELECT fullname,mail_id,mobile_number,application_reference_id,date_of_birth,status FROM lms_loan_application where  fullname LIKE CONCAT('%',:keyword, '%') OR mail_id LIKE CONCAT('%',:keyword, '%')  OR mobile_number LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR status LIKE CONCAT('%',:keyword, '%') OR application_reference_id LIKE CONCAT('%',:keyword, '%') OR date_of_birth LIKE CONCAT('%',:keyword, '%')",nativeQuery = true)
	public Page<LoanApplicationDTOS> getAllLoanApplicationWithKeyword(Pageable pageable,@Param("keyword")String keyword);

}
