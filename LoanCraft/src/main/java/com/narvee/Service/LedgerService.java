package com.narvee.Service;

import java.util.List;

import com.narvee.entity.Ledger;
import com.narvee.request.dto.LedgerDTO;

public interface LedgerService {
	
	public Ledger saveLedger(LedgerDTO ledgerDTO);
    public Ledger updateLedger(Ledger ledger);
    public Ledger getById(Long ledgerId);
    public List<Ledger> getAll();
    public Boolean deleteById(Long ledgerId) ;
    
}
