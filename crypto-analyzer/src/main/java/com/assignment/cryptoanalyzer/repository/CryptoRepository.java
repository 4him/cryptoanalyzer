package com.assignment.cryptoanalyzer.repository;

import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import com.assignment.cryptoanalyzer.model.CryptoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoRepository extends JpaRepository<CryptoCurrency, Long> {
    @Query("SELECT c FROM CryptoCurrency c WHERE c.currencyCode = :currencyCode")
    List<CryptoCurrency> findByCurrencyCode(@Param("currencyCode") String currencyCode);


    // This is sample of how could functionality could be realized by SQL query
//    @Query("SELECT c.currencyCode, MIN(c.price), MAX(c.price), MIN(c.timestamp), MAX(c.timestamp) " +
//            "FROM CryptoCurrency c WHERE c.currencyCode = :currencyCode GROUP BY c.currencyCode")
//    List<CryptoModel> findStatsBycurrencyCode(@Param("currencyCode") String currencyCode);


    // This is sample of how could functionality could be realized by SQL query
//    @Query("SELECT c.currencyCode, (MAX(c.price) - MIN(c.price)) / MIN(c.price) AS normalized_range " +
//            "FROM CryptoCurrency c GROUP BY c.currencyCode ORDER BY normalized_range DESC")
//    List<CryptoModel> findAllByNormalizedRange();
}
