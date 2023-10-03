package com.raf.pc.transactionservice.model;

import com.raf.transactionalcore.dto.TransactionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode
@Data
@Document(collection = "distributed_transaction")
public class DistributedTransaction {

    @Id
    private String id;
    @Indexed(unique = true)
    private UUID transactionUUID = UUID.randomUUID();

    private List<Participant> participants = new ArrayList<>();

    private LocalDateTime started = LocalDateTime.now();

    private LocalDateTime ended = null;

    private Map<String, Object> metadata = new HashMap<>();

    private TransactionStatus transactionStatus = TransactionStatus.PREPARING;

    private RollbackInformation rollbackInformation;

    @Version
    private Long version;
}
