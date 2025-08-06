package com.dcc.studies.model; // Sugiro renomear o pacote de 'entity' para 'model' ou 'document'

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.Instant;

@Data
public class AuditMetadata {

    @CreatedDate
    private Instant createdAt;

    // @CreatedBy
    // private String createdBy;

    @LastModifiedDate
    private Instant updatedAt;

    // @LastModifiedBy
    // private String updatedBy;
}