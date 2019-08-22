package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface PaymentSummaryService {
    Optional<PaymentSummary> findByGroupId(String groupId);

    Optional<PaymentSummary> save(PaymentSummary summary);

    PaymentSummary calculateSummary(PaymentEnt paymentEnt);
}
