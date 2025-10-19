package com.investxpert.searchservice.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "company_profiles", schema = "stockmarket_fmp",
       indexes = {
           @Index(name = "idx_company_profile_symbol", columnList = "symbol"),
           @Index(name = "idx_company_profile_company_name", columnList = "company_name"),
           @Index(name = "idx_company_profile_exchange", columnList = "exchange"),
           @Index(name = "idx_company_profile_sector", columnList = "sector"),
           @Index(name = "idx_company_profile_country", columnList = "country"),
           @Index(name = "idx_company_profile_is_etf", columnList = "is_etf"),
           @Index(name = "idx_company_profile_is_actively_trading", columnList = "is_actively_trading"),
           @Index(name = "idx_company_profile_mkt_cap", columnList = "mkt_cap")
       })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyProfileModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "symbol", nullable = false, length = 20)
	private String symbol;

	@Column(name = "price", columnDefinition = "DECIMAL(12,4)")
	private BigDecimal price;

	@Column(name = "beta", columnDefinition = "DECIMAL(10,6)")
	private BigDecimal beta;

	@Column(name = "vol_avg", columnDefinition = "DECIMAL(20,2)")
	private BigDecimal volAvg;

	@Column(name = "mkt_cap", columnDefinition = "DECIMAL(20,2)")
	private BigDecimal mktCap;

	@Column(name = "last_div", columnDefinition = "DECIMAL(10,6)")
	private BigDecimal lastDiv;

	@Column(name = "price_range", length = 50)
	private String range;

	@Column(name = "changes", columnDefinition = "DECIMAL(12,4)")
	private BigDecimal changes;

	@Column(name = "company_name", length = 500)
	private String companyName;

	@Column(name = "currency", length = 10)
	private String currency;

	@Column(name = "cik", length = 20)
	private String cik;

	@Column(name = "isin", length = 20)
	private String isin;

	@Column(name = "cusip", length = 20)
	private String cusip;

	@Column(name = "exchange", length = 100)
	private String exchange;

	@Column(name = "exchange_short_name", length = 20)
	private String exchangeShortName;

	@Column(name = "industry", length = 200)
	private String industry;

	@Column(name = "website", length = 500)
	private String website;

	@Column(name = "description", length = 10000)
	private String description;

	@Column(name = "ceo", length = 200)
	private String ceo;

	@Column(name = "sector", length = 100)
	private String sector;

	@Column(name = "country", length = 100)
	private String country;

	@Column(name = "full_time_employees", columnDefinition = "DECIMAL(15,0)")
	private BigDecimal fullTimeEmployees;

	@Column(name = "phone", length = 50)
	private String phone;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "zip", length = 20)
	private String zip;

	@Column(name = "dcf_diff", columnDefinition = "DECIMAL(12,4)")
	private BigDecimal dcfDiff;

	@Column(name = "dcf", columnDefinition = "DECIMAL(12,4)")
	private BigDecimal dcf;

	@Column(name = "image", length = 500)
	private String image;

	@Column(name = "ipo_date", length = 20)
	private String ipoDate;

	@Column(name = "default_image", nullable = false)
	private boolean defaultImage;

	@Column(name = "is_etf", nullable = false)
	private boolean isEtf;

	@Column(name = "is_actively_trading", nullable = false)
	private boolean isActivelyTrading;

	@Column(name = "is_fund", nullable = false)
	private boolean isFund;

	@Column(name = "is_adr", nullable = false)
	private boolean isAdr;
}