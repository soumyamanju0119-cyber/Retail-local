package com.retail.rewards.controller;

import com.retail.rewards.model.CustomerRewardSummary;
import com.retail.rewards.model.RewardResponse;
import com.retail.rewards.service.RewardService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes REST endpoints for viewing customer reward summaries.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    /**
     * Creates a reward controller.
     *
     * @param rewardService reward summary service
     */
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Returns rewards for every customer in the selected period.
     *
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return reward response with monthly and total points
     */
    @GetMapping
    public RewardResponse getRewards(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return rewardService.getRewards(startDate, endDate);
    }

    /**
     * Returns rewards for a single customer in the selected period.
     *
     * @param customerId customer identifier
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return customer reward summary
     */
    @GetMapping("/{customerId}")
    public CustomerRewardSummary getRewardsByCustomer(
            @PathVariable String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return rewardService.getRewardsByCustomer(customerId, startDate, endDate);
    }
}
