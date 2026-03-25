package com.retail.rewards.service;

import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.exception.InvalidDateRangeException;
import com.retail.rewards.model.CustomerRewardSummary;
import com.retail.rewards.model.MonthlyRewardSummary;
import com.retail.rewards.model.RewardResponse;
import com.retail.rewards.model.Transaction;
import com.retail.rewards.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Coordinates transaction retrieval and reward summary aggregation.
 */
@Service
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final RewardCalculator rewardCalculator;

    /**
     * Creates a reward service.
     *
     * @param transactionRepository transaction source
     * @param rewardCalculator transaction reward calculator
     */
    public RewardService(TransactionRepository transactionRepository, RewardCalculator rewardCalculator) {
        this.transactionRepository = transactionRepository;
        this.rewardCalculator = rewardCalculator;
    }

    /**
     * Returns reward summaries for all customers in the selected date range.
     *
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return reward summaries grouped by customer and month
     */
    public RewardResponse getRewards(LocalDate startDate, LocalDate endDate) {
        DateRange dateRange = resolveDateRange(startDate, endDate);
        List<Transaction> filteredTransactions = filterTransactions(dateRange.startDate(), dateRange.endDate());
        return new RewardResponse(dateRange.startDate(), dateRange.endDate(), buildCustomerSummaries(filteredTransactions));
    }

    /**
     * Returns reward summary for a single customer in the selected date range.
     *
     * @param customerId customer identifier
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return reward summary for the requested customer
     */
    public CustomerRewardSummary getRewardsByCustomer(String customerId, LocalDate startDate, LocalDate endDate) {
        DateRange dateRange = resolveDateRange(startDate, endDate);
        return buildCustomerSummaries(filterTransactions(dateRange.startDate(), dateRange.endDate())).stream()
                .filter(summary -> summary.customerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private DateRange resolveDateRange(LocalDate requestedStartDate, LocalDate requestedEndDate) {
        List<Transaction> transactions = transactionRepository.findAll();
        LocalDate defaultStartDate = transactions.stream()
                .map(Transaction::transactionDate)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new InvalidDateRangeException("No transactions are available."));
        LocalDate defaultEndDate = transactions.stream()
                .map(Transaction::transactionDate)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new InvalidDateRangeException("No transactions are available."));

        LocalDate effectiveStartDate = requestedStartDate != null ? requestedStartDate : defaultStartDate;
        LocalDate effectiveEndDate = requestedEndDate != null ? requestedEndDate : defaultEndDate;

        if (effectiveStartDate.isAfter(effectiveEndDate)) {
            throw new InvalidDateRangeException("Start date must be on or before end date.");
        }
        return new DateRange(effectiveStartDate, effectiveEndDate);
    }

    private List<Transaction> filterTransactions(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> !transaction.transactionDate().isBefore(startDate))
                .filter(transaction -> !transaction.transactionDate().isAfter(endDate))
                .toList();
    }

    private List<CustomerRewardSummary> buildCustomerSummaries(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::customerId))
                .values()
                .stream()
                .map(this::buildCustomerSummary)
                .sorted(Comparator.comparing(CustomerRewardSummary::customerId))
                .toList();
    }

    private CustomerRewardSummary buildCustomerSummary(List<Transaction> customerTransactions) {
        Transaction sampleTransaction = customerTransactions.get(0);
        Map<YearMonth, Long> monthlyPoints = customerTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> YearMonth.from(transaction.transactionDate()),
                        TreeMap::new,
                        Collectors.summingLong(rewardCalculator::calculatePoints)));

        List<MonthlyRewardSummary> monthlyRewards = monthlyPoints.entrySet().stream()
                .map(entry -> new MonthlyRewardSummary(entry.getKey().toString(), entry.getValue()))
                .toList();
        long totalPoints = monthlyRewards.stream().mapToLong(MonthlyRewardSummary::points).sum();

        return new CustomerRewardSummary(
                sampleTransaction.customerId(),
                sampleTransaction.customerName(),
                monthlyRewards,
                totalPoints);
    }

    private record DateRange(LocalDate startDate, LocalDate endDate) {
    }
}
