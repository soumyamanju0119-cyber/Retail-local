package com.retail.rewards.repository;

import com.retail.rewards.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Supplies an in-memory dataset for demonstrating reward calculations.
 */
@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = List.of(
            new Transaction("T-1001", "C001", "Alice Johnson", new BigDecimal("120.00"), LocalDate.of(2026, 1, 12)),
            new Transaction("T-1002", "C001", "Alice Johnson", new BigDecimal("54.25"), LocalDate.of(2026, 1, 28)),
            new Transaction("T-1003", "C001", "Alice Johnson", new BigDecimal("130.10"), LocalDate.of(2026, 2, 10)),
            new Transaction("T-1004", "C001", "Alice Johnson", new BigDecimal("89.99"), LocalDate.of(2026, 3, 5)),
            new Transaction("T-1005", "C002", "Brian Smith", new BigDecimal("45.00"), LocalDate.of(2026, 1, 8)),
            new Transaction("T-1006", "C002", "Brian Smith", new BigDecimal("75.00"), LocalDate.of(2026, 2, 2)),
            new Transaction("T-1007", "C002", "Brian Smith", new BigDecimal("220.40"), LocalDate.of(2026, 2, 24)),
            new Transaction("T-1008", "C002", "Brian Smith", new BigDecimal("102.00"), LocalDate.of(2026, 3, 17)),
            new Transaction("T-1009", "C003", "Carla Gomez", new BigDecimal("199.99"), LocalDate.of(2026, 1, 14)),
            new Transaction("T-1010", "C003", "Carla Gomez", new BigDecimal("51.00"), LocalDate.of(2026, 2, 11)),
            new Transaction("T-1011", "C003", "Carla Gomez", new BigDecimal("49.99"), LocalDate.of(2026, 2, 20)),
            new Transaction("T-1012", "C003", "Carla Gomez", new BigDecimal("300.00"), LocalDate.of(2026, 3, 9))
    );

    /**
     * Returns all demo transactions.
     *
     * @return immutable list of transactions
     */
    @Override
    public List<Transaction> findAll() {
        return transactions;
    }
}
