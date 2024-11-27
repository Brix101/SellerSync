package com.brix.Seller_Sync.sale.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.Money;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.ReportDocument.CompressionAlgorithm;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficByAsin;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficByDate;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesByAsin;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesByDate;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.sale.AsinSale;
import com.brix.Seller_Sync.sale.Sale;
import com.brix.Seller_Sync.sale.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument) {
        String url = reportDocument.getDecodedUrl();
        RestTemplate restTemplate = new RestTemplate();
        byte[] reportData = restTemplate.getForObject(url, byte[].class);

        try (InputStream inputStream = new ByteArrayInputStream(reportData);
             InputStream decompressedStream = getDecompressedStream(inputStream, reportDocument.getCompressionAlgorithm());
             InputStreamReader reader = new InputStreamReader(decompressedStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(sb.toString(), SalesAndTrafficReport.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getDecompressedStream(InputStream inputStream, CompressionAlgorithm compressionAlgorithm) throws IOException {
        switch (compressionAlgorithm) {
            case GZIP:
                return new GZIPInputStream(inputStream);
            case ZIP:
                return new ZipInputStream(inputStream);
            case NONE:
                return inputStream;
            default:
                throw new IllegalArgumentException("Unsupported compression algorithm: " + compressionAlgorithm);
        }
    }

    @Override
    public Sale saveSalesReport(Client client, SalesAndTrafficReport salesAndTrafficReport) {
        SalesAndTrafficByDate salesAndTrafficByDate = salesAndTrafficReport.getSalesAndTrafficByDate().get(0);
        String reportDate = salesAndTrafficByDate.getDate();
        SalesByDate salesByDate = salesAndTrafficByDate.getSalesByDate();
        Money orderedProductSales = salesByDate.getOrderedProductSales();
        Money averageSalesPerOrderItem = salesByDate.getAverageSalesPerOrderItem();
        Money shippedProductSales = salesByDate.getShippedProductSales();

        String currencyCode = orderedProductSales.getCurrencyCode();

        Sale sale = saleRepository.findByClientIdAndDateAndCurrencyCode(client.getId(), LocalDate.parse(reportDate), currencyCode);

        List<AsinSale> asinSales = new ArrayList<>();

        if (sale != null) {
            // Replace old data with new data
            sale.setUnitsOrdered(salesByDate.getUnitsOrdered());
            sale.setTotalOrderItems(salesByDate.getTotalOrderItems());
            sale.setUnitsRefunded(salesByDate.getUnitsRefunded());
            sale.setRefundRate(salesByDate.getRefundRate());
            sale.setUnitsShipped(salesByDate.getUnitsShipped());
            sale.setOrdersShipped(salesByDate.getOrdersShipped());
            sale.setOrderedProductSalesAmount(orderedProductSales.getAmount());
            sale.setAverageSalesPerOrderItem(averageSalesPerOrderItem.getAmount());
            sale.setShippedProductSales(shippedProductSales.getAmount());

            // Clear existing AsinSales and add new ones
            sale.getAsinSales().clear();
        } else {
            sale = new Sale();
            sale.setClient(client);
            sale.setDate(LocalDate.parse(reportDate));
            sale.setCurrencyCode(currencyCode);

            sale.setUnitsOrdered(salesByDate.getUnitsOrdered());
            sale.setTotalOrderItems(salesByDate.getTotalOrderItems());
            sale.setUnitsRefunded(salesByDate.getUnitsRefunded());
            sale.setRefundRate(salesByDate.getRefundRate());
            sale.setUnitsShipped(salesByDate.getUnitsShipped());
            sale.setOrdersShipped(salesByDate.getOrdersShipped());
            sale.setOrderedProductSalesAmount(orderedProductSales.getAmount());
            sale.setAverageSalesPerOrderItem(averageSalesPerOrderItem.getAmount());
            sale.setShippedProductSales(salesByDate.getShippedProductSales().getAmount());
        }

        for (SalesAndTrafficByAsin salesAndTrafficByAsin : salesAndTrafficReport.getSalesAndTrafficByAsin()) {

            AsinSale asinSale = new AsinSale();
            asinSale.setSale(sale);

            SalesByAsin salesByAsin = salesAndTrafficByAsin.getSalesByAsin();
            Money orderedProductSalesByAsin = salesByAsin.getOrderedProductSales();

            asinSale.setParentAsin(salesAndTrafficByAsin.getParentAsin());
            asinSale.setChildAsin(salesAndTrafficByAsin.getChildAsin());
            asinSale.setSku(salesAndTrafficByAsin.getSku());
            asinSale.setUnitsOrdered(salesByAsin.getUnitsOrdered());
            asinSale.setOrderedProductSalesAmount(orderedProductSalesByAsin.getAmount());
            asinSale.setCurrencyCode(orderedProductSalesByAsin.getCurrencyCode());
            asinSale.setTotalOrderItems(salesByAsin.getTotalOrderItems());

            asinSales.add(asinSale);
        }

        sale.setAsinSales(asinSales);

        return saleRepository.save(sale);
    }
}
