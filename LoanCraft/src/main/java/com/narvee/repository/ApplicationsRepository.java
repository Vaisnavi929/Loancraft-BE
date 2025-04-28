package com.narvee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Applications;
import com.narvee.request.dto.ApplicationDTO;
import com.narvee.request.dto.DisbursmentIdDTO;

public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

	@Query(value = "select max(amaxnum) as max from lms_applications", nativeQuery = true)
	public Long appmaxNumber();

//	public boolean existsByAadharNumber(String aadharNumber);

	@Query(value = " SELECT \r\n" + "    a.updateddate as updateddate,\r\n" + "    a.loan_id,\r\n"
			+ "    a.application_reference_id AS referenceId,\r\n" + "    a.createddate,\r\n" + "    c.first_name,\r\n"
			+ "    c.last_name,\r\n" + "    c.email,\r\n" + "    a.status,\r\n" + "    c.mobile_number,\r\n"
			+ "    a.repayment_mode,\r\n" + "    a.loan_amount\r\n" + "FROM \r\n" + "    lms_customers_info c\r\n"
			+ "JOIN \r\n" + "    lms_applications a \r\n" + "    ON a.customer_id = c.customer_id\r\n"
			+ "WHERE a.status NOT IN ('Approved', 'sanctioned', 'rejected')", nativeQuery = true)
	public Page<ApplicationDTO> getAllLoanApplication(Pageable pageable);

	@Query(value = "SELECT  a.updateddate as updateddate,\r\n" + "    a.loan_id,\r\n"
			+ "    a.application_reference_id AS referenceId,\r\n" + "    a.createddate,\r\n" + "    c.first_name,\r\n"
			+ "    c.last_name,\r\n" + "    c.email,\r\n" + "    a.status,\r\n" + "    c.mobile_number,\r\n"
			+ "    a.repayment_mode,\r\n" + "    a.loan_amount\r\n" + "FROM \r\n" + "    lms_customers_info c\r\n"
			+ "JOIN \r\n" + "    lms_applications a \r\n" + "    ON a.customer_id = c.customer_id\r\n" + "WHERE "
			+ "    ( " + "        c.first_name ILIKE '%' || :keyword || '%' OR "
			+ "        c.last_name ILIKE '%' || :keyword || '%' OR "
			+ "        c.email ILIKE '%' || :keyword || '%' OR "
			+ "        TO_CHAR(a.createddate, 'YYYY-MM-DD') ILIKE '%' || :keyword || '%' OR "
			+ "        a.status ILIKE '%' || :keyword || '%' OR "
			+ "        c.mobile_number ILIKE '%' || :keyword || '%'  OR "
			+ " a.application_reference_id  ILIKE '%' || :keyword || '%'  ) "
			+ "    AND a.status NOT IN ('Approved', 'sanctioned', 'rejected')", nativeQuery = true)
	public Page<ApplicationDTO> getAllLoanApplicationWithKeyword(Pageable pageable, @Param("keyword") String keyword);

	@Query(value = "SELECT a.loan_id,application_reference_id \r\n"
			+ "as referenceId, c.first_name,c.last_name,a.updateddate,a.createddate,c.email,\r\n"
			+ "a.status,c.mobile_number,a.repayment_mode as repaymentmode,a.loan_amount as \r\n"
			+ "loanamount FROM lms_customers_info c \r\n" + "join lms_applications a on a.customer_id=c.customer_id\r\n"
			+ "WHERE a.status =:status", nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListApproved(Pageable pageable, @Param("status") String status);

	@Query(value = "SELECT \r\n" + "  a.loan_id,\r\n" + "  a.application_reference_id AS referenceId,\r\n"
			+ "  c.first_name,\r\n" + "  c.last_name,\r\n" + "  a.updateddate as updateddate,\r\n"
			+ "  a.createddate,\r\n" + "  c.email,\r\n" + "  a.status,\r\n" + "  c.mobile_number,\r\n"
			+ "  a.repayment_mode AS repaymentmode,\r\n" + "  a.loan_amount AS loanamount\r\n"
			+ "FROM lms_customers_info c\r\n" + "JOIN lms_applications a ON a.customer_id = c.customer_id\r\n"
			+ "WHERE \r\n" + "  (\r\n" + "    c.first_name ILIKE '%' || :keyword || '%' OR\r\n"
			+ "    c.last_name ILIKE '%' || :keyword || '%' OR\r\n" + "    c.email ILIKE '%' || :keyword || '%' OR\r\n"
			+ "    CAST(a.createddate AS TEXT) ILIKE '%' || :keyword || '%' OR\r\n"
			+ "    a.status ILIKE '%' || :keyword || '%' OR\r\n"
			+ "    c.mobile_number ILIKE '%' || :keyword || '%' OR "
			+ "a.application_reference_id  ILIKE '%' || :keyword || '%' \r\n" + "  )\r\n"
			+ "  AND a.status = :status\r\n", nativeQuery = true)
	public Page<ApplicationDTO> getallApplicationListWithKewordApproved(Pageable pageable,
			@Param("keyword") String keyword, @Param("status") String status);

	@Query("SELECT la.applicationReferenceId AS applicationReferenceId, " +
		       "la.loanId AS loanid, " +
		       "c.mobileNumber AS mobileNumber, " +
		       "c.firstName AS firstName, " +
		       "c.email AS email " +
		       "FROM Applications la " +
		       "JOIN la.customers c")
		public List<DisbursmentIdDTO> getReviewedAppDropDown();



}
