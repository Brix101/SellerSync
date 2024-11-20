package com.brix.Seller_Sync.amzn.payload;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    private CompressionAlgorithm compressionAlgorithm;

    private String url;


    public String getDecodedUrl() {
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
