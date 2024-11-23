
package com.brix.Seller_Sync.reportqueue;

import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification.ReportType;
import com.brix.Seller_Sync.client.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportQueue {

    @NonNull
    private Client client;

    @NonNull
    private CreateReportSpecification createReportSpecification;

    public String getReportKey(){
        ReportType reportType = createReportSpecification.getReportType();

        return reportType.toString() + ":" + client.getClientId() + ":" + createReportSpecification.hashCode();
    }
}