package com.retail.rewards.repository;

import com.retail.rewards.model.Transaction;
import java.util.List;

/**
 * Provides access to recorded transactions.
 */
public interface TransactionRepository {

    /**
     * Returns all known transactions.
     *
     * @return immutable list of transactions
     */
    List<Transaction> findAll();
}
