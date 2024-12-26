package org.MIFI.service;

import org.MIFI.entity.Transaction;
import org.MIFI.entity.enums.TypeOfTransaction;
import org.MIFI.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setCreated(new Date().getTime());
        transaction.setType(transaction.getMoney() < 0 ? TypeOfTransaction.OUT : TypeOfTransaction.IN);
        transactionRepository.save(transaction);
    }
}
