
package com.brix.Seller_Sync.reportqueue;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification;
import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;
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
    private ReportSpecification reportSpecification;

    public String getReportKey(){
        ReportType reportType = reportSpecification.getReportType();

        return reportType.toString() + ":" + client.getClientId() + ":" + reportSpecification.hashCode();
    }
}