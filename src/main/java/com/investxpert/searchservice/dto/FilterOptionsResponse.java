package com.investxpert.searchservice.dto;

import java.util.List;

public record FilterOptionsResponse(
    List<String> industries,
    List<String> countries,
    List<String> sectors,
    List<String> exchanges,
    List<String> currencies,
    RangeOptions rangeOptions
) {
    public record RangeOptions(
        MarketCapRange marketCap,
        PriceRange price,
        BetaRange beta
    ) {}

    public record MarketCapRange(
        long min,
        long max
    ) {}

    public record PriceRange(
        double min,
        double max
    ) {}

    public record BetaRange(
        double min,
        double max
    ) {}
}