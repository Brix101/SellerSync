package com.brix.Seller_Sync.common.utils;

import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.common.exception.BadRequestException;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new BadRequestException( "Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
