package com.assignment.cryptoanalyzer.service.impl;

import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import com.assignment.cryptoanalyzer.repository.CryptoRepository;
import com.assignment.cryptoanalyzer.service.interfaces.CryptoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service implementation for managing cryptocurrency data, including
 * caching, database interactions, and initialization logic.
 *
 * <p>Key Features:
 * <ul>
 *     <li>Initializes data from a database or file source at startup.</li>
 *     <li>Maintains a thread-safe in-memory cache for fast access to cryptocurrency data.</li>
 *     <li>Supports fetching, caching, and saving cryptocurrency data.</li>
 *     <li>Automatically loads data from files to database if the database is empty.</li>
 * </ul>
 *
 * <p>Thread-Safe Components:
 * <ul>
 *     <li>Uses {@link ConcurrentHashMap} for currency cache to ensure safe concurrent access.</li>
 *     <li>Employs {@link CopyOnWriteArrayList} for storing data by currency in the cache.</li>
 * </ul>
 *
 * <p>Workflow:
 * <ol>
 *     <li>On startup, the {@link #initialize()} method checks the database state:
 *         <ul>
 *             <li>If the database is empty, data is read from files, saved to the database, and cached.</li>
 *             <li>If the database contains data, it is loaded directly into the cache.</li>
 *         </ul>
 *     </li>
 *     <li>Currency data can be fetched from the cache using {@link #getCryptoData(String)}.</li>
 *     <li>New data can be added using {@link #addNewData(List)}, which updates both the database and cache.</li>
 * </ol>
 *
 * <p>Dependencies:
 * <ul>
 *     <li>{@link CryptoRepository} - Handles database operations.</li>
 *     <li>{@link FileServiceImpl} - Reads cryptocurrency data from external files.</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Service} - Marks the class as a Spring-managed service.</li>
 *     <li>{@link RequiredArgsConstructor} - Generates a constructor for required final fields.</li>
 *     <li>{@link PostConstruct} - Initializes data after bean creation.</li>
 * </ul>
 *
 * <p>Usage:
 * This class is used for applications requiring efficient and scalable cryptocurrency data handling,
 * with support for data persistence, caching, and real-time updates.
 */

@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private final Map<String, List<CryptoCurrency>> currencyCache = new ConcurrentHashMap<>();

    private final CryptoRepository cryptoRepository;
    private final FileServiceImpl fileService;

    @PostConstruct
    public void initialize() {
        if (cryptoRepository.count() == 0) {
            List<CryptoCurrency> dataFromFiles = fileService.readData();
            saveToDatabase(dataFromFiles);
            updateCache(dataFromFiles);
        } else {
            loadFromDatabaseToCache();
        }
    }

    private void updateCache(List<CryptoCurrency> filesData) {
        filesData.forEach(e ->
                currencyCache.computeIfAbsent(e.getCurrencyCode(),
                                key -> new CopyOnWriteArrayList<>())
                        .add(e));
    }

    private void loadFromDatabaseToCache() {
        List<CryptoCurrency> allData = cryptoRepository.findAll();
        updateCache(allData);
    }

    private List<CryptoCurrency> loadDataByCurrencyCode(String currencyCode ) {
        return cryptoRepository.findByCurrencyCode(currencyCode);
    }

    public List<CryptoCurrency> getCryptoData(String currencyCode) {
        if(!currencyCache.containsKey(currencyCode)) {
            List<CryptoCurrency> data = loadDataByCurrencyCode(currencyCode);
            currencyCache.put(currencyCode, data);
        }
        return currencyCache.get(currencyCode);
    }

    public void addNewData(List<CryptoCurrency> newData) {
        saveToDatabase(newData);
        updateCache(newData);
    }

    public void saveToDatabase(List<CryptoCurrency> cryptoCurrencies) {
        if (cryptoCurrencies != null && !cryptoCurrencies.isEmpty()) {
            cryptoRepository.saveAllAndFlush(cryptoCurrencies);
        }
    }

}
