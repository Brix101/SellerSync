package com.brix.Seller_Sync.amzn.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDocument {

    public enum CompressionAlgorithm {
        GZIP,
        ZIP,
        NONE
    }
    
    private String reportDocumentId;
    private String url;
    private CompressionAlgorithm compressionAlgorithm;

}
