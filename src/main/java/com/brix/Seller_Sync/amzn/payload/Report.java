
package com.brix.Seller_Sync.amzn.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    public enum ProcessingStatus {
        CANCELLED,
        DONE,
        FATAL,
        IN_PROGRESS,
        IN_QUEUE,
    }

    private String[] marketplaceIds; // Optional
    private String reportId; // Required
    private String reportType; // Required
    private String dataStartTime; // Optional
    private String dataEndTime; // Optional
    private String reportScheduleId; // Optional
    private String createdTime; // Required
    private ProcessingStatus processingStatus; // Required
    private String processingStartTime; // Optional
    private String processingEndTime; // Optional
    private String reportDocumentId; // Optional
}