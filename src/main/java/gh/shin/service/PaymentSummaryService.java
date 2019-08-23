package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;

import java.util.Optional;
import java.util.concurrent.Future;

public interface PaymentSummaryService {
    Optional<PaymentSummary> findByGroupId(String groupId);

    Future<PaymentSummary> save(PaymentSummary summary);

    PaymentSummary calculateSummary(PaymentEnt paymentEnt);
}
