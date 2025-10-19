package com.investxpert.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawCompanyDto(
        @JsonProperty("symbol") String symbol,
        @JsonProperty("company_name") String companyName,
        @JsonProperty("sector") String sector,
        @JsonProperty("industry") LocalizedField industry,
        @JsonProperty("exchange") String exchange,
        @JsonProperty("exchange_short_name") String exchangeShortName,
        @JsonProperty("country") String country,
        @JsonProperty("currency") String currency,
        @JsonProperty("price") Double price,
        @JsonProperty("market_cap") Double marketCap,
        @JsonProperty("beta") Double beta,
        @JsonProperty("vol_avg") Double volAvg,
        @JsonProperty("last_div") Double lastDiv,
        @JsonProperty("changes") Double changes,
        @JsonProperty("price_range") String priceRange,
        @JsonProperty("description") LocalizedField description,
        @JsonProperty("website") String website,
        @JsonProperty("ceo") String ceo,
        @JsonProperty("phone") String phone,
        @JsonProperty("address") String address,
        @JsonProperty("city") String city,
        @JsonProperty("state") String state,
        @JsonProperty("zip") String zip,
        @JsonProperty("image") String image,
        @JsonProperty("ipo_date") String ipoDate,
        @JsonProperty("cik") String cik,
        @JsonProperty("isin") String isin,
        @JsonProperty("cusip") String cusip,
        @JsonProperty("full_time_employees") Double fullTimeEmployees,
        @JsonProperty("dcf") Double dcf,
        @JsonProperty("dcf_diff") Double dcfDiff,
        @JsonProperty("default_image") Boolean defaultImage,
        @JsonProperty("indexed_at") String indexedAt,
        @JsonProperty("etf") Boolean etf,
        @JsonProperty("fund") Boolean fund,
        @JsonProperty("adr") Boolean adr,
        @JsonProperty("actively_trading") Boolean activelyTrading
) {
    public RawCompanyDto() {
        this(
                "", "", "", new LocalizedField(), "", "", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "", new LocalizedField(), "", "", "", "", "", "", "", "", "", "", "", "", 0.0, 0.0, 0.0, false, "", false, false, false, false
        );
    }

    public String getType() {
        if (Boolean.TRUE.equals(etf)) {
            return "ETF";
        }
        if (Boolean.TRUE.equals(fund)) {
            return "FUND";
        }
        return "STOCK";
    }
}
