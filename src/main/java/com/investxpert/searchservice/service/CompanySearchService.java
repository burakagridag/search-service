package com.investxpert.searchservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investxpert.searchservice.dto.RawCompanyDto;
import com.investxpert.searchservice.dto.SearchResponseDto;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanySearchService {

    private final OpenSearchClient client;
    private final ObjectMapper objectMapper;

    public CompanySearchService(OpenSearchClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }
    
    private String getExchangeCountry(String exchangeShortName) {
        if (exchangeShortName == null) {
            return null;
        }
        
        switch (exchangeShortName.toUpperCase()) {
            case "AMEX":
                return "US";
            case "AMS":
                return "NL";
            case "AQS":
                return "AE";
            case "ASX":
                return "AU";
            case "ATH":
                return "GR";
            case "BER":
                return "DE";
            case "BME":
                return "ES";
            case "BRU":
                return "BE";
            case "BSE":
                return "IN";
            case "BUD":
                return "HU";
            case "BUE":
                return "AR";
            case "BVC":
                return "CO";
            case "CBOE":
                return "US";
            case "CNQ":
                return "CA";
            case "CPH":
                return "DK";
            case "DFM":
                return "AE";
            case "DOH":
                return "QA";
            case "DUB":
                return "IE";
            case "DUS":
                return "DE";
            case "DXE":
                return "DE";
            case "EGX":
                return "EG";
            case "HAM":
                return "DE";
            case "HEL":
                return "FI";
            case "HKSE":
                return "HK";
            case "HOSE":
                return "VN";
            case "ICE":
                return "IS";
            case "IOB":
                return "GB";
            case "IST":
                return "TR";
            case "JKT":
                return "ID";
            case "JNB":
                return "ZA";
            case "JPX":
                return "JP";
            case "KLS":
                return "MY";
            case "KOE":
                return "KR";
            case "KSC":
                return "KR";
            case "KUW":
                return "KW";
            case "LIS":
                return "PT";
            case "LSE":
                return "GB";
            case "MCX":
                return "RU";
            case "MEX":
                return "MX";
            case "MIL":
                return "IT";
            case "MUN":
                return "DE";
            case "NASDAQ":
                return "US";
            case "NEO":
                return "CA";
            case "NSE":
                return "IN";
            case "NYSE":
                return "US";
            case "NZE":
                return "NZ";
            case "OSL":
                return "NO";
            case "OTC":
                return "US";
            case "PAR":
                return "FR";
            case "PNK":
                return "US";
            case "PRA":
                return "CZ";
            case "RIS":
                return "RU";
            case "SAO":
                return "BR";
            case "SAU":
                return "SA";
            case "SES":
                return "SG";
            case "SET":
                return "TH";
            case "SGO":
                return "CL";
            case "SHH":
                return "CN";
            case "SHZ":
                return "CN";
            case "SIX":
                return "CH";
            case "STO":
                return "SE";
            case "STU":
                return "DE";
            case "TAI":
                return "TW";
            case "TAL":
                return "EE";
            case "TLV":
                return "IL";
            case "TSX":
                return "CA";
            case "TSXV":
                return "CA";
            case "TWO":
                return "TW";
            case "VIE":
                return "AT";
            case "WSE":
                return "PL";
            case "XETRA":
                return "DE";
            default:
                return null;
        }
    }

    public Mono<SearchResponse<Object>> searchCompanies(String query) {
        return Mono.fromCallable(() -> {
            SearchRequest request = SearchRequest.of(s -> s
                .index("companies")
                .query(q -> q
                    .bool(b -> b
                        .should(should -> should
                            .matchPhrasePrefix(mpp -> mpp
                                .field("company_name")
                                .query(query)
                            )
                        )
                        .should(should -> should
                            .matchPhrasePrefix(mpp -> mpp
                                .field("symbol")
                                .query(query)
                            )
                        )
                    )
                )
            );
            
            return client.search(request, Object.class);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<SearchResponseDto> searchCompaniesWithDto(String query) {
        return searchCompanies(query)
            .map(response -> {
                List<RawCompanyDto> companies = response.hits().hits().stream()
                    .map(hit -> {
                        Map<String, Object> source = (Map<String, Object>) hit.source();
                        return objectMapper.convertValue(source, RawCompanyDto.class);
                    })
                    .toList();
                
                // Group companies by companyName
                Map<String, List<RawCompanyDto>> groupedByCompany = companies.stream()
                    .collect(Collectors.groupingBy(RawCompanyDto::companyName));
                
                // Convert to StockDto list
                List<SearchResponseDto.StockDto> stocks = groupedByCompany.entrySet().stream()
                    .map(entry -> {
                        String companyName = entry.getKey();
                        List<SearchResponseDto.ListingDto> listings = entry.getValue().stream()
                            .map(company -> new SearchResponseDto.ListingDto(
                                company.symbol(),
                                company.exchangeShortName(),
                                company.currency(),
                                company.getType(),
                                company.country(),
                                getExchangeCountry(company.exchangeShortName())
                            ))
                            .toList();
                        
                        return new SearchResponseDto.StockDto(companyName, listings);
                    })
                    .toList();
                
                // For now, analyses is empty - this can be extended later
                List<SearchResponseDto.AnalysisDto> analyses = List.of();
                
                return new SearchResponseDto(stocks, analyses);
            });
    }
    
    public Mono<RawCompanyDto> getCompanyBySymbolAndExchange(String symbol, String exchangeShortName) {
        return Mono.fromCallable(() -> {
            SearchRequest request = SearchRequest.of(s -> s
                .index("companies")
                .query(q -> q
                    .bool(b -> b
                        .must(must -> must
                            .term(t -> t
                                .field("symbol.keyword")
                                .value(FieldValue.of(symbol.toUpperCase()))
                            )
                        )
                        .must(must -> must
                            .term(t -> t
                                .field("exchange_short_name.keyword")
                                .value(FieldValue.of(exchangeShortName.toUpperCase()))
                                .caseInsensitive(true)
                            )
                        )
                    )
                )
            );
            
            return client.search(request, Object.class);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(response -> {
            List<Hit<Object>> hits = response.hits().hits();

            if (hits.isEmpty()) {
                return new RawCompanyDto();
            }
            
            Map<String, Object> source = (Map<String, Object>) hits.get(0).source();
            return objectMapper.convertValue(source, RawCompanyDto.class);
        });
    }
}
