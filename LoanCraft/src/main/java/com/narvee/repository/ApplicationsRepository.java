package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Applications;
import com.narvee.request.dto.ApplicationDTO;
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
	
	@Query(value = "select max(amaxnum) as max from lms_applications", nativeQuery = true)
	public Long appmaxNumber();
	
//	public boolean existsByAadharNumber(String aadharNumber);

	@Query(value =" Select a.loan_id,application_reference_id as referenceId ,a.updateddate,a.createddate, c.first_name,c.last_name,c.email,a.status,c.mobile_number,a.repayment_mode,a.loan_amount FROM lms_customers_info c join lms_applications a on a.customer_id=c.customer_id where a.status != 'Approved' ",nativeQuery = true)
	public Page<ApplicationDTO> getAllLoanApplication(Pageable pageable);
	
	@Query(value ="SELECT a.loan_id,a.application_reference_id  as referenceId, c.first_name,a.updateddate,a.createddate,c.last_name,c.email,a.status,c.mobile_number FROM lms_customers_info c join lms_applications a on a.customer_id=c.customer_id  "
			+ "	where ( c.first_name LIKE CONCAT('%', :keyword,'%') OR c.last_name LIKE CONCAT('%',:keyword, '%') OR c.email LIKE CONCAT('%',:keyword, '%') OR a.createddate LIKE CONCAT('%',:keyword, '%')  OR "
			+ "	a.status LIKE CONCAT('%', :keyword, '%') OR c.mobile_number LIKE CONCAT('%',:keyword, '%')) AND a.status != 'Approved' ",nativeQuery = true)
	public Page<ApplicationDTO> getAllLoanApplicationWithKeyword(Pageable pageable,@Param("keyword")String keyword);

	

	@Query(value ="SELECT a.loan_id,application_reference_id, c.first_name,c.last_name,a.updateddate,a.createddate,c.email,a.status,c.mobile_number,a.repayment_mode,a.loan_amount FROM lms_customers_info c join lms_applications a on a.customer_id=c.customer_id WHERE a.status =:status",nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListApproved(Pageable pageable, @Param("status") String status);

	
	@Query(value ="SELECT a.loan_id,a.application_reference_id c.first_name,c.last_name,a.updateddate,a.createddate,c.email,a.status,c.mobile_number,a.repayment_mode,a.loan_amount FROM lms_customers_info c join lms_applications a on a.customer_id=c.customer_id \n"
			+ "where c.first_name LIKE CONCAT('%', :keyword,'%') OR c.last_name LIKE CONCAT('%',:keyword, '%') OR c.email LIKE CONCAT('%',:keyword, '%') OR a.createddate LIKE CONCAT('%',:keyword, '%')  OR \n"
			+ "a.status LIKE CONCAT('%', :keyword, '%') OR c.mobile_number LIKE CONCAT('%',:keyword, '%') AND  a.status = :status",nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListWithKewordApproved(Pageable pageable,@Param("keyword")String keyword, @Param("status") String status);

}

 






