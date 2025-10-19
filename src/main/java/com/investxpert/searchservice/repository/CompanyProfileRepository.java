package com.investxpert.searchservice.repository;

import com.investxpert.searchservice.model.CompanyProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfileModel, String> {

    @Query("SELECT DISTINCT c.industry FROM CompanyProfileModel c WHERE c.industry IS NOT NULL AND c.isActivelyTrading = true ORDER BY c.industry")
    List<String> findDistinctIndustries();

    @Query("SELECT DISTINCT c.country FROM CompanyProfileModel c WHERE c.country IS NOT NULL AND c.isActivelyTrading = true ORDER BY c.country")
    List<String> findDistinctCountries();

    @Query("SELECT DISTINCT c.sector FROM CompanyProfileModel c WHERE c.sector IS NOT NULL AND c.isActivelyTrading = true ORDER BY c.sector")
    List<String> findDistinctSectors();

    @Query("SELECT DISTINCT c.exchange FROM CompanyProfileModel c WHERE c.exchange IS NOT NULL AND c.isActivelyTrading = true ORDER BY c.exchange")
    List<String> findDistinctExchanges();

    @Query("SELECT DISTINCT c.currency FROM CompanyProfileModel c WHERE c.currency IS NOT NULL AND c.isActivelyTrading = true ORDER BY c.currency")
    List<String> findDistinctCurrencies();

    @Query("SELECT MIN(c.mktCap), MAX(c.mktCap) FROM CompanyProfileModel c WHERE c.mktCap IS NOT NULL AND c.mktCap > 0 AND c.isActivelyTrading = true")
    Object[] findMarketCapRange();

    @Query("SELECT MIN(c.price), MAX(c.price) FROM CompanyProfileModel c WHERE c.price IS NOT NULL AND c.price > 0 AND c.isActivelyTrading = true")
    Object[] findPriceRange();

    @Query("SELECT MIN(c.beta), MAX(c.beta) FROM CompanyProfileModel c WHERE c.beta IS NOT NULL AND c.isActivelyTrading = true")
    Object[] findBetaRange();

    @Query("SELECT COUNT(c) FROM CompanyProfileModel c WHERE c.isActivelyTrading = true")
    long countActivelyTradingCompanies();
}