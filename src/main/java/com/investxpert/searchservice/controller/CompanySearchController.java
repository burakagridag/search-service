package com.investxpert.searchservice.controller;

import com.investxpert.searchservice.dto.*;
import com.investxpert.searchservice.service.CompanySearchService;
import com.investxpert.searchservice.service.StockScreenerService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequestMapping("/api/v1/search")
public class CompanySearchController {

    private final CompanySearchService searchService;
    private final StockScreenerService stockScreenerService;

    public CompanySearchController(CompanySearchService searchService, StockScreenerService stockScreenerService) {
        this.searchService = searchService;
        this.stockScreenerService = stockScreenerService;
    }

    @GetMapping("/companies")
    public Mono<SearchResponseDto> searchCompanies(@RequestParam String query) {
        return searchService.searchCompaniesWithDto(query);
    }

    @PostMapping("/companies")
    public Mono<SearchResponseDto> searchCompaniesPost(@RequestBody SearchRequest request) {
        return searchService.searchCompaniesWithDto(request.query());
    }
    
    @GetMapping("/company/{symbol}/{exchange}")
    public Mono<ResponseEntity<?>> getCompanyBySymbolAndExchange(
            @PathVariable String symbol, 
            @PathVariable String exchange) {
        
        // Validate input parameters
        if (symbol == null || symbol.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        if (exchange == null || exchange.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        
        return searchService.getCompanyBySymbolAndExchange(symbol, exchange)
                .map(company -> {
                    if (isBlank(company.symbol())) {
                        return ResponseEntity.notFound().build();
                    }
                    return ResponseEntity.ok(company);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(throwable -> {
                    System.err.println("Controller error for symbol: " + symbol + ", exchange: " + exchange + ". Error: " + throwable.getMessage());
                    
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @PostMapping("/stocks/filter")
    public Mono<StockScreenerResponse> filterStocks(@RequestBody StockFilterRequest request) {
        return stockScreenerService.filterStocks(request);
    }

    @GetMapping("/stocks/filter-options")
    public Mono<FilterOptionsResponse> getFilterOptions() {
        return stockScreenerService.getFilterOptions();
    }

    public record SearchRequest(String query) {}
}
