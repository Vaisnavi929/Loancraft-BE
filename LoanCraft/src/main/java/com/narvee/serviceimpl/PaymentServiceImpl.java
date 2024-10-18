package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.PaymentService;
import com.narvee.entity.Ledger;
import com.narvee.entity.LoanSanction;
import com.narvee.entity.Payment;
import com.narvee.repository.LedgerRepository;
import com.narvee.repository.LoanSanctionRepository;
import com.narvee.repository.PaymentRepository;
import com.narvee.request.dto.PaymentDTO;

import jakarta.transaction.Transactional;
@Service
public class PaymentServiceImpl implements PaymentService{
	
	private static final Logger logger=LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LoanSanctionRepository loanSanctionRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public Payment savePayment(PaymentDTO paymentDTO) {
        logger.info("!!! inside class : PaymentServiceImpl, !! method : savePayment");
        Payment payment = mapper.map(paymentDTO, Payment.class);
        LoanSanction loanSanction = loanSanctionRepository.findById(paymentDTO.getLoanSactionId())
                .orElseThrow(() -> new RuntimeException("LoanSanction not found"));
        payment.setLoanSanction(loanSanction);
        Payment savedPayment = paymentRepository.save(payment);
        Ledger ledger = new Ledger();
        ledger.setPayment(savedPayment);  
        ledger.setDate(savedPayment.getPaymentDate()); 
        ledger.setLoanAmount(loanSanction.getAmountSanctioned()); 
        ledger.setPrincipleAmount(loanSanction.getAmountDisbursed()); 
        ledger.setAmountPaid(savedPayment.getAmount());  
        ledger.setBalance(loanSanction.getAmountDisbursed() - savedPayment.getAmount());
        ledger.setPaid(savedPayment.getAmount());
        ledgerRepository.save(ledger);
        loanSanction.setAmountDisbursed(loanSanction.getAmountDisbursed() - savedPayment.getAmount());
        loanSanctionRepository.save(loanSanction);
        return savedPayment;
    }
	@Override
	public Payment updatePayment(PaymentDTO paymentDto) {
		logger.info("!!! inside class : PaymentServiceImpl, !! method : updatePayment ");
		Payment payment=mapper.map(paymentDto, Payment.class);
		return paymentRepository.save(payment);
	}

	@Override
	public Payment getById(Long payamentId) {
		logger.info("!!! inside class : PaymentServiceImpl, !! method : getById");
		return paymentRepository.findById(payamentId).get();
	}

	@Override
	public List<Payment> getAll() {
		logger.info("!!! inside class : PaymentServiceImpl,!! method : getAll ");
		return paymentRepository.findAll();
	}

	@Override
	public Boolean deleteById(Long payamentId) {
		logger.info("!!! inside class : PaymentServiceImpl , !! method : deleteById ");
		 paymentRepository.deleteById(payamentId);
		 return true;
	}

	
}
