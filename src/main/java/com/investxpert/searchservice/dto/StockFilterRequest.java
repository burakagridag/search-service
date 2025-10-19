package com.investxpert.searchservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record StockFilterRequest(
    List<String> industries,
    List<String> countries,
    List<String> sectors,
    List<String> exchanges,
    List<String> currencies,
    BigDecimal minMarketCap,
    BigDecimal maxMarketCap,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    BigDecimal minBeta,
    BigDecimal maxBeta,
    Boolean isEtf,
    Boolean isActivelyTrading,
    Boolean isFund,
    Boolean isAdr,
    String sortBy,
    String sortOrder,
    int page,
    int size
) {
    public StockFilterRequest {
        // Default values
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
        if (size > 100) size = 100; // Max limit
        if (sortBy == null) sortBy = "market_cap";
        if (sortOrder == null) sortOrder = "desc";
    }
}