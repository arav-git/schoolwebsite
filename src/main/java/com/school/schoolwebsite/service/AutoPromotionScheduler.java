package com.school.schoolwebsite.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoPromotionScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoPromotionScheduler.class);

    private final PromotionService promotionService;

    @Value("${app.promotion.auto.enabled:true}")
    private boolean autoPromotionEnabled;

    @Value("${app.promotion.auto.day-month:04-01}")
    private String autoPromotionDayMonth;

    public AutoPromotionScheduler(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Scheduled(cron = "${app.promotion.auto.cron:0 30 2 * * *}")
    public void runAutoPromotion() {
        if (!autoPromotionEnabled) {
            return;
        }

        String todayDayMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        if (!todayDayMonth.equals(autoPromotionDayMonth)) {
            return;
        }

        String academicYear = promotionService.defaultAcademicYear();
        int promoted = promotionService.autoPromoteEligible(academicYear);
        log.info("Auto promotion run completed for {}. Promoted {} students.", academicYear, promoted);
    }
}
