package com.raf.pc.transactionservice.repository;

import com.raf.pc.transactionservice.model.DistributedTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistributedTransactionRepository extends MongoRepository<DistributedTransaction, String> {

    Optional<DistributedTransaction> findByTransactionUUID(UUID uuid);
}
