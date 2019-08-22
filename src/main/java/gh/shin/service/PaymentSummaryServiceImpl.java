package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.PaymentSummaryRepo;
import gh.shin.group.GroupPolicyFactory;
import gh.shin.web.value.PaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentSummaryServiceImpl implements PaymentSummaryService {
    private final PaymentSummaryRepo paymentSummaryRepo;
    private final GroupPolicyFactory groupPolicyFactory;

    @Autowired
    public PaymentSummaryServiceImpl(PaymentSummaryRepo paymentSummaryRepo, GroupPolicyFactory groupPolicyFactory) {
        this.paymentSummaryRepo = paymentSummaryRepo;
        this.groupPolicyFactory = groupPolicyFactory;
    }

    @Override
    @CachePut(value = "summary", key = "#groupId")
    public Optional<PaymentSummary> findByGroupId(final String groupId) {
        return paymentSummaryRepo.findById(groupId);
    }

    @Async
    @Override
    @CacheEvict(value = "summary", key = "#paymentSummary.groupId")
    @Transactional
    public Optional<PaymentSummary> save(final PaymentSummary paymentSummary) {
        return Optional.of(paymentSummaryRepo.save(paymentSummary));
    }


    @Override
    public PaymentSummary calculateSummary(final PaymentEnt paymentEnt) {
        PaymentInfo paymentInfo = new PaymentInfo(paymentEnt);
        LocalDateTime now = LocalDateTime.now();
        String groupId = groupPolicyFactory.getGroupIdByPaymentInfo(paymentInfo);
        PaymentSummary paymentSummary = findByGroupId(groupId).orElse(new PaymentSummary(groupId, now));
        paymentSummary.calculate(paymentInfo);
        save(paymentSummary);
        return paymentSummary;
    }

}
