package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;

public interface PaymentSummaryService {
    PaymentSummary findByGroupId(String groupId);

    void create(PaymentSummary summary);

    PaymentSummary calculateSummary(PaymentEnt paymentEnt);
}
