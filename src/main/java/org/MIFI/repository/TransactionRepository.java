package org.MIFI.repository;

import org.MIFI.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByProfileIdAndCreatedBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findAllByProfileId(Long profileId);
    void deleteByIdAndProfileId(Long id, Long profileId);
}
