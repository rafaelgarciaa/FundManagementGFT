package com.fondosGFT.fondosGFT.repository.transaction;


import com.fondosGFT.fondosGFT.model.transaction.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByClientIdOrderByDateDesc(String clienteId);
}
