package com.brix.Seller_Sync.taskqueue.entity;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReportTask extends BaseEntity {

    @ManyToOne
    private Client client;

    // @Embedded
    // private CreateReportSpecification specification;

    @Column()
    private String status;
}
