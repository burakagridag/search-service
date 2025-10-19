package com.investxpert.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record SearchResponseDto(
    List<StockDto> stocks,
    List<AnalysisDto> analyses
) {
    
    public record StockDto(
        @JsonProperty("companyName")
        String companyName,
        
        @JsonProperty("listings")
        List<ListingDto> listings
    ) {}
    
    public record ListingDto(
        @JsonProperty("symbol")
        String symbol,
        
        @JsonProperty("exchange")
        String exchange,
        
        @JsonProperty("currency")
        String currency,
        
        @JsonProperty("type")
        String type,
        
        @JsonProperty("country")
        String country,
        
        @JsonProperty("exchange_country")
        String exchangeCountry
    ) {}
    
    public record AnalysisDto(
        @JsonProperty("id")
        String id,
        
        @JsonProperty("title")
        String title,
        
        @JsonProperty("author")
        String author,
        
        @JsonProperty("stockSymbol")
        String stockSymbol,
        
        @JsonProperty("createdAt")
        LocalDateTime createdAt
    ) {}
}
