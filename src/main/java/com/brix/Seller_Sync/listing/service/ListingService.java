package com.brix.Seller_Sync.listing.service;

import java.util.List;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.listing.Listing;
import com.brix.Seller_Sync.listing.payload.CreateListingRequest;

public interface ListingService {
    
    Listing addListing(Listing listing);

    Listing upsertListing(CreateListingRequest createListingRequest);

    List<CreateListingRequest> parseListingDocument(ReportDocument reportDocument);
}
