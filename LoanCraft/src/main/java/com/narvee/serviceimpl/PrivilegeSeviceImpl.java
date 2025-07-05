package com.narvee.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.IPrivilegeService;
import com.narvee.entity.Privilege;
import com.narvee.entity.Roles;
import com.narvee.repository.IPrivilegeRepository;
import com.narvee.repository.IRolesRepository;
import com.narvee.request.dto.PrivilegedDTO;
import com.narvee.vo.DropdownVO;
import com.narvee.vo.PrivilegeVO;
import com.narvee.vo.RoleToPrivilegesVO;

@Service
public class PrivilegeSeviceImpl implements IPrivilegeService {

	@Autowired
	private IRolesRepository roleRepo;

	@Autowired
	private IPrivilegeRepository privRepo;

	@Override
	public Privilege savePrevileges(PrivilegedDTO privilege) {
		Privilege priv = new Privilege();
		priv.setCardType(privilege.getCardType());
		priv.setName(privilege.getName());
		priv.setType(privilege.getType());

		return privRepo.save(priv);
	}

	@Override
	public List<Privilege> allprev() {
		return privRepo.findAll();
	}

	@Override
	public PrivilegeVO getAllPrivileges() {

		PrivilegeVO privilegeVO = new PrivilegeVO();

		List<DropdownVO> users = new ArrayList<DropdownVO>();

		List<DropdownVO> roles = new ArrayList<DropdownVO>();

		List<DropdownVO> dashboard = new ArrayList<DropdownVO>();

		List<DropdownVO> home = new ArrayList<DropdownVO>();

		List<DropdownVO> hrm = new ArrayList<DropdownVO>();

		List<DropdownVO> customerinfo = new ArrayList<DropdownVO>();

		List<DropdownVO> customer_dashboard = new ArrayList<DropdownVO>();

		List<DropdownVO> applications = new ArrayList<DropdownVO>();

		List<DropdownVO> loans = new ArrayList<DropdownVO>();

		List<DropdownVO> approved = new ArrayList<DropdownVO>();

		List<DropdownVO> sanctioned = new ArrayList<DropdownVO>();

		List<DropdownVO> disbursed = new ArrayList<DropdownVO>();

		List<DropdownVO> rejected = new ArrayList<DropdownVO>();

		List<DropdownVO> receivables = new ArrayList<DropdownVO>();

		List<DropdownVO> currentDues = new ArrayList<DropdownVO>();

		List<DropdownVO> receivable = new ArrayList<DropdownVO>();

		List<DropdownVO> applicationTracking = new ArrayList<DropdownVO>();

		List<DropdownVO> physicalVerification = new ArrayList<DropdownVO>();

		List<DropdownVO> chequeDeposit = new ArrayList<DropdownVO>();

		List<DropdownVO> lowChequeRequests = new ArrayList<DropdownVO>();

		List<Privilege> privileges = privRepo.findAll();

		for (Privilege singlePrivilege : privileges) {

			// USER
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.USER.name())) {
				users.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setUser(users);
			}

			// Role
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.ROLES.name())) {
				roles.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setRole(roles);
			}

			// Dashboard
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DASHBOARD.name())) {
				dashboard.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setDashboard(dashboard);
			}

			// Home
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.HOME.name())) {
				home.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setHome(home);
			}

			// Hrm
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.HRM.name())) {
				hrm.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setHrm(hrm);
			}

			// Customer Info
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CUSTOMER_INFO.name())) {
				customerinfo.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setCustomerinfo(customerinfo);
			}

			// Pricing
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CUSTOMER_DASHBOARD.name())) {
				customer_dashboard.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setCustomer_dashboard(customer_dashboard);
			}

			// Applications
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPLICATIONS.name())) {
				applications.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setApplications(applications);
			}

			// Loans
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.LOANS.name())) {
				loans.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setLoans(loans);
			}

			// Approved
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPROVED.name())) {
				approved.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setApproved(approved);
			}

			// Sanctioned
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.SANCTIONED.name())) {
				sanctioned.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setSanctioned(sanctioned);
			}

			// Disbursed
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DISBURSED.name())) {
				disbursed.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setDisbursed(disbursed);
			}

			// Rejected
			if (singlePrivilege.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.REJECTED.name())) {
				rejected.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setRejected(rejected);
			}

			// Rejected
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.RECEIVABLES.name())) {
				receivables.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setReceivables(receivables);
			}

			// currentDues
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CURRENTDUES.name())) {
				currentDues.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setCurrentDues(currentDues);
			}

			// RECEIVABLE
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.RECEIVABLE.name())) {
				receivable.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setReceivable(receivable);
			}

			// APPLICATION_TRACKING
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPLICATION_TRACKING.name())) {
				applicationTracking.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setApplicationTracking(applicationTracking);
			}

			// PHYSICAL_VERIFICATION
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.PHYSICAL_VERIFICATION.name())) {
				physicalVerification.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setPhysicalVerification(physicalVerification);
			}
			// CHEQUE_DEPOSIT
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CHEQUE_DEPOSIT.name())) {
				chequeDeposit.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setChequeDeposit(chequeDeposit);
			}
			// LOW_CHEQUE_REQUESTS
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.LOW_CHEQUE_REQUESTS.name())) {
				lowChequeRequests.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setLowChequeRequests(lowChequeRequests);
			}
		}

		return privilegeVO;
	}

	@Override
	public void addPrivilegeToRole(RoleToPrivilegesVO rolesPrivileges) {

		Optional<Roles> opt = roleRepo.findById(rolesPrivileges.getRoleId());
		Roles role = opt.get();
		Set<Privilege> allPrevPrivileges = new HashSet<Privilege>();
		Privilege privlig = null;
		for (Long privilegeId : rolesPrivileges.getPrivilegeIds()) {
			Optional<Privilege> priv = privRepo.findById(privilegeId);
			privlig = priv.get();
			allPrevPrivileges.add(privlig);
		}
		role.setPrivileges(allPrevPrivileges);

		roleRepo.save(role);
	}

	@Override
	public PrivilegeVO getPrivilegesById(Long roleId) {

		PrivilegeVO privilegeVO = new PrivilegeVO();
		Optional<Roles> opt = roleRepo.findById(roleId);
		Roles roles = opt.get();

		List<DropdownVO> users = new ArrayList<DropdownVO>();

		List<DropdownVO> role = new ArrayList<DropdownVO>();

		List<DropdownVO> dashboard = new ArrayList<DropdownVO>();

		List<DropdownVO> home = new ArrayList<DropdownVO>();

		List<DropdownVO> hrm = new ArrayList<DropdownVO>();

		List<DropdownVO> customerinfo = new ArrayList<DropdownVO>();

		List<DropdownVO> customer_dashboard = new ArrayList<DropdownVO>();

		List<DropdownVO> applications = new ArrayList<DropdownVO>();

		List<DropdownVO> loans = new ArrayList<DropdownVO>();

		List<DropdownVO> approved = new ArrayList<DropdownVO>();

		List<DropdownVO> sanctioned = new ArrayList<DropdownVO>();

		List<DropdownVO> disbursed = new ArrayList<DropdownVO>();

		List<DropdownVO> rejected = new ArrayList<DropdownVO>();

		List<DropdownVO> receivables = new ArrayList<DropdownVO>();

		List<DropdownVO> currentDues = new ArrayList<DropdownVO>();

		List<DropdownVO> receivable = new ArrayList<DropdownVO>();

		List<DropdownVO> applicationTracking = new ArrayList<DropdownVO>();

		List<DropdownVO> physicalVerificaion = new ArrayList<DropdownVO>();

		List<DropdownVO> chequeDeposit = new ArrayList<DropdownVO>();

		List<DropdownVO> lowChequeRequests = new ArrayList<DropdownVO>();

		Set<Privilege> privileges = roles.getPrivileges();

		boolean flg = false;
		for (Privilege single : privileges) {

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.ROLES.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				role.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.USER.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				users.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DASHBOARD.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				dashboard.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.HOME.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				home.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.HRM.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				hrm.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CUSTOMER_INFO.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				customerinfo.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CUSTOMER_DASHBOARD.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				customer_dashboard.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPLICATIONS.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				applications.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.LOANS.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				loans.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPROVED.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				approved.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.SANCTIONED.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				sanctioned.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DISBURSED.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				disbursed.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.REJECTED.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				rejected.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.RECEIVABLES.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				receivables.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CURRENTDUES.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				currentDues.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.RECEIVABLE.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				receivable.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.APPLICATION_TRACKING.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				applicationTracking.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.PHYSICAL_VERIFICATION.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				physicalVerificaion.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.CHEQUE_DEPOSIT.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				chequeDeposit.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.LOW_CHEQUE_REQUESTS.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				lowChequeRequests.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

		}

		privilegeVO.setUser(users);

		privilegeVO.setRole(role);

		privilegeVO.setDashboard(dashboard);

		privilegeVO.setHome(home);

		privilegeVO.setHrm(hrm);

		privilegeVO.setCustomerinfo(customerinfo);

		privilegeVO.setCustomer_dashboard(customer_dashboard);

		privilegeVO.setApplications(applications);

		privilegeVO.setLoans(loans);

		privilegeVO.setApproved(approved);

		privilegeVO.setSanctioned(sanctioned);

		privilegeVO.setDisbursed(disbursed);

		privilegeVO.setRejected(rejected);

		privilegeVO.setReceivables(receivables);

		privilegeVO.setCurrentDues(currentDues);

		privilegeVO.setReceivable(receivable);

		privilegeVO.setApplicationTracking(applicationTracking);

		privilegeVO.setPhysicalVerification(physicalVerificaion);

		privilegeVO.setChequeDeposit(chequeDeposit);

		privilegeVO.setLowChequeRequests(lowChequeRequests);

		return privilegeVO;
	}

}
