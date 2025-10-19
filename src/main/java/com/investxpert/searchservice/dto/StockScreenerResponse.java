package com.investxpert.searchservice.dto;

import java.math.BigDecimal;
import java.util.List;

public record StockScreenerResponse(
    List<StockItem> stocks,
    PaginationInfo pagination,
    FilterSummary filterSummary
) {

    public record StockItem(
        String symbol,
        String companyName,
        String sector,
        String industry,
        String country,
        String exchange,
        String exchangeShortName,
        String currency,
        BigDecimal price,
        BigDecimal marketCap,
        BigDecimal beta,
        BigDecimal changes,
        BigDecimal changesPercentage,
        String priceRange,
        Boolean isEtf,
        Boolean isActivelyTrading,
        Boolean isFund,
        Boolean isAdr
    ) {}

    public record PaginationInfo(
        int currentPage,
        int totalPages,
        int pageSize,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious
    ) {}

    public record FilterSummary(
        int totalMatches,
        List<String> appliedFilters
    ) {}
}