package com.investxpert.searchservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investxpert.searchservice.dto.*;
import com.investxpert.searchservice.repository.CompanyProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.*;
import org.opensearch.client.opensearch._types.query_dsl.*;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.json.JsonData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockScreenerService {

    private final OpenSearchClient openSearchClient;
    private final CompanyProfileRepository companyProfileRepository;
    private final ObjectMapper objectMapper;

    @Cacheable(value = "filter-options", key = "'all'")
    public Mono<FilterOptionsResponse> getFilterOptions() {
        log.info("Fetching filter options from database");
        return Mono.fromCallable(() -> {
            List<String> industries = companyProfileRepository.findDistinctIndustries();
            List<String> countries = companyProfileRepository.findDistinctCountries();
            List<String> sectors = companyProfileRepository.findDistinctSectors();
            List<String> exchanges = companyProfileRepository.findDistinctExchanges();
            List<String> currencies = companyProfileRepository.findDistinctCurrencies();

            // Get range values
            Object[] marketCapRange = companyProfileRepository.findMarketCapRange();
            Object[] priceRange = companyProfileRepository.findPriceRange();
            Object[] betaRange = companyProfileRepository.findBetaRange();

            FilterOptionsResponse.MarketCapRange marketCap = new FilterOptionsResponse.MarketCapRange(
                extractLongValue(marketCapRange, 0),
                extractLongValue(marketCapRange, 1)
            );

            FilterOptionsResponse.PriceRange price = new FilterOptionsResponse.PriceRange(
                extractDoubleValue(priceRange, 0),
                extractDoubleValue(priceRange, 1)
            );

            FilterOptionsResponse.BetaRange beta = new FilterOptionsResponse.BetaRange(
                extractDoubleValue(betaRange, 0),
                extractDoubleValue(betaRange, 1)
            );

            FilterOptionsResponse.RangeOptions rangeOptions = new FilterOptionsResponse.RangeOptions(
                marketCap, price, beta
            );

            return new FilterOptionsResponse(industries, countries, sectors, exchanges, currencies, rangeOptions);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<StockScreenerResponse> filterStocks(StockFilterRequest request) {
        log.info("Filtering stocks with request: {}", request);
        return Mono.fromCallable(() -> {
            SearchRequest searchRequest = buildSearchRequest(request);
            return openSearchClient.search(searchRequest, Object.class);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(response -> mapToStockScreenerResponse(response, request));
    }

    private SearchRequest buildSearchRequest(StockFilterRequest request) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // Add filter conditions
        if (request.industries() != null && !request.industries().isEmpty()) {
            boolQueryBuilder.filter(f -> f.terms(t -> t
                .field("industry.keyword")
                .terms(TermsQueryField.of(tqf -> tqf.value(
                    request.industries().stream().map(FieldValue::of).toList()
                )))
            ));
        }

        if (request.countries() != null && !request.countries().isEmpty()) {
            boolQueryBuilder.filter(f -> f.terms(t -> t
                .field("country.keyword")
                .terms(TermsQueryField.of(tqf -> tqf.value(
                    request.countries().stream().map(FieldValue::of).toList()
                )))
            ));
        }

        if (request.sectors() != null && !request.sectors().isEmpty()) {
            boolQueryBuilder.filter(f -> f.terms(t -> t
                .field("sector.keyword")
                .terms(TermsQueryField.of(tqf -> tqf.value(
                    request.sectors().stream().map(FieldValue::of).toList()
                )))
            ));
        }

        if (request.exchanges() != null && !request.exchanges().isEmpty()) {
            boolQueryBuilder.filter(f -> f.terms(t -> t
                .field("exchange.keyword")
                .terms(TermsQueryField.of(tqf -> tqf.value(
                    request.exchanges().stream().map(FieldValue::of).toList()
                )))
            ));
        }

        if (request.currencies() != null && !request.currencies().isEmpty()) {
            boolQueryBuilder.filter(f -> f.terms(t -> t
                .field("currency.keyword")
                .terms(TermsQueryField.of(tqf -> tqf.value(
                    request.currencies().stream().map(FieldValue::of).toList()
                )))
            ));
        }

        // Market cap range
        if (request.minMarketCap() != null || request.maxMarketCap() != null) {
            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field("market_cap");
            if (request.minMarketCap() != null) {
                rangeBuilder.gte(JsonData.of(request.minMarketCap()));
            }
            if (request.maxMarketCap() != null) {
                rangeBuilder.lte(JsonData.of(request.maxMarketCap()));
            }
            boolQueryBuilder.filter(f -> f.range(rangeBuilder.build()));
        }

        // Price range
        if (request.minPrice() != null || request.maxPrice() != null) {
            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field("price");
            if (request.minPrice() != null) {
                rangeBuilder.gte(JsonData.of(request.minPrice()));
            }
            if (request.maxPrice() != null) {
                rangeBuilder.lte(JsonData.of(request.maxPrice()));
            }
            boolQueryBuilder.filter(f -> f.range(rangeBuilder.build()));
        }

        // Beta range
        if (request.minBeta() != null || request.maxBeta() != null) {
            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field("beta");
            if (request.minBeta() != null) {
                rangeBuilder.gte(JsonData.of(request.minBeta()));
            }
            if (request.maxBeta() != null) {
                rangeBuilder.lte(JsonData.of(request.maxBeta()));
            }
            boolQueryBuilder.filter(f -> f.range(rangeBuilder.build()));
        }

        // Boolean filters
        if (request.isEtf() != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t
                .field("is_etf")
                .value(FieldValue.of(request.isEtf()))
            ));
        }

        if (request.isActivelyTrading() != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t
                .field("is_actively_trading")
                .value(FieldValue.of(request.isActivelyTrading()))
            ));
        }

        if (request.isFund() != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t
                .field("is_fund")
                .value(FieldValue.of(request.isFund()))
            ));
        }

        if (request.isAdr() != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t
                .field("is_adr")
                .value(FieldValue.of(request.isAdr()))
            ));
        }

        // Build search request
        return SearchRequest.of(s -> s
            .index("companies")
            .query(q -> q.bool(boolQueryBuilder.build()))
            .from(request.page() * request.size())
            .size(request.size())
            .sort(buildSortOptions(request))
        );
    }

    private List<SortOptions> buildSortOptions(StockFilterRequest request) {
        List<SortOptions> sortOptions = new ArrayList<>();

        String sortField = request.sortBy();
        SortOrder sortOrder = "asc".equalsIgnoreCase(request.sortOrder()) ?
            SortOrder.Asc : SortOrder.Desc;

        // Map common sort fields
        switch (sortField) {
            case "market_cap":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("market_cap").order(sortOrder))));
                break;
            case "price":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("price").order(sortOrder))));
                break;
            case "company_name":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("company_name.keyword").order(sortOrder))));
                break;
            case "symbol":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("symbol.keyword").order(sortOrder))));
                break;
            case "changes":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("changes").order(sortOrder))));
                break;
            case "beta":
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("beta").order(sortOrder))));
                break;
            default:
                sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("market_cap").order(SortOrder.Desc))));
        }

        return sortOptions;
    }

    private StockScreenerResponse mapToStockScreenerResponse(SearchResponse<Object> response, StockFilterRequest request) {
        List<StockScreenerResponse.StockItem> stocks = response.hits().hits().stream()
            .map(this::mapHitToStockItem)
            .toList();

        long totalHits = response.hits().total().value();
        int totalPages = (int) Math.ceil((double) totalHits / request.size());

        StockScreenerResponse.PaginationInfo pagination = new StockScreenerResponse.PaginationInfo(
            request.page(),
            totalPages,
            request.size(),
            totalHits,
            request.page() < totalPages - 1,
            request.page() > 0
        );

        List<String> appliedFilters = buildAppliedFiltersList(request);
        StockScreenerResponse.FilterSummary filterSummary = new StockScreenerResponse.FilterSummary(
            (int) totalHits,
            appliedFilters
        );

        return new StockScreenerResponse(stocks, pagination, filterSummary);
    }

    private StockScreenerResponse.StockItem mapHitToStockItem(Hit<Object> hit) {
        Map<String, Object> source = (Map<String, Object>) hit.source();

        return new StockScreenerResponse.StockItem(
            getStringValue(source, "symbol"),
            getStringValue(source, "company_name"),
            getStringValue(source, "sector"),
            getStringValue(source, "industry"),
            getStringValue(source, "country"),
            getStringValue(source, "exchange"),
            getStringValue(source, "exchange_short_name"),
            getStringValue(source, "currency"),
            getBigDecimalValue(source, "price"),
            getBigDecimalValue(source, "market_cap"),
            getBigDecimalValue(source, "beta"),
            getBigDecimalValue(source, "changes"),
            calculateChangesPercentage(source),
            getStringValue(source, "price_range"),
            getBooleanValue(source, "is_etf"),
            getBooleanValue(source, "is_actively_trading"),
            getBooleanValue(source, "is_fund"),
            getBooleanValue(source, "is_adr")
        );
    }

    private BigDecimal calculateChangesPercentage(Map<String, Object> source) {
        Object changes = source.get("changes");
        Object price = source.get("price");

        if (changes != null && price != null) {
            BigDecimal changesVal = new BigDecimal(changes.toString());
            BigDecimal priceVal = new BigDecimal(price.toString());

            if (priceVal.compareTo(BigDecimal.ZERO) != 0) {
                return changesVal.divide(priceVal, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            }
        }
        return null;
    }

    private List<String> buildAppliedFiltersList(StockFilterRequest request) {
        List<String> filters = new ArrayList<>();

        if (request.industries() != null && !request.industries().isEmpty()) {
            filters.add("Industries: " + String.join(", ", request.industries()));
        }
        if (request.countries() != null && !request.countries().isEmpty()) {
            filters.add("Countries: " + String.join(", ", request.countries()));
        }
        if (request.sectors() != null && !request.sectors().isEmpty()) {
            filters.add("Sectors: " + String.join(", ", request.sectors()));
        }
        if (request.exchanges() != null && !request.exchanges().isEmpty()) {
            filters.add("Exchanges: " + String.join(", ", request.exchanges()));
        }
        if (request.currencies() != null && !request.currencies().isEmpty()) {
            filters.add("Currencies: " + String.join(", ", request.currencies()));
        }
        if (request.minMarketCap() != null) {
            filters.add("Min Market Cap: " + request.minMarketCap());
        }
        if (request.maxMarketCap() != null) {
            filters.add("Max Market Cap: " + request.maxMarketCap());
        }
        if (request.minPrice() != null) {
            filters.add("Min Price: " + request.minPrice());
        }
        if (request.maxPrice() != null) {
            filters.add("Max Price: " + request.maxPrice());
        }
        if (request.isEtf() != null) {
            filters.add("ETF: " + request.isEtf());
        }
        if (request.isActivelyTrading() != null) {
            filters.add("Actively Trading: " + request.isActivelyTrading());
        }

        return filters;
    }

    private long extractLongValue(Object[] array, int index) {
        if (array != null && array.length > index && array[index] != null) {
            if (array[index] instanceof BigDecimal) {
                return ((BigDecimal) array[index]).longValue();
            } else if (array[index] instanceof Number) {
                return ((Number) array[index]).longValue();
            }
        }
        return 0L;
    }

    private double extractDoubleValue(Object[] array, int index) {
        if (array != null && array.length > index && array[index] != null) {
            if (array[index] instanceof BigDecimal) {
                return ((BigDecimal) array[index]).doubleValue();
            } else if (array[index] instanceof Number) {
                return ((Number) array[index]).doubleValue();
            }
        }
        return 0.0;
    }

    private String getStringValue(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return value.toString();
        }
        return null;
    }

    private BigDecimal getBigDecimalValue(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        } else if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Boolean getBooleanValue(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        return null;
    }
}