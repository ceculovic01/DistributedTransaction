package com.raf.pc.transactionservice.model;

import lombok.Data;

@Data
public class RollbackInformation {

    private String serviceName;
    private String statusCode;
    private String responseBody;
}
