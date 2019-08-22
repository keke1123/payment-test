package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.PaymentSummaryRepo;
import gh.shin.group.GroupPolicyFactory;
import gh.shin.util.NoDataFoundException;
import gh.shin.web.value.PaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
    public PaymentSummary findByGroupId(final String groupId) {
        return paymentSummaryRepo.findById(groupId).orElseThrow(() -> new NoDataFoundException("Group: '" + groupId + "' is not found."));
    }

    @Async
    @Override
    @Transactional
    public void create(PaymentSummary summary) {
        paymentSummaryRepo.save(summary);
    }

    @Override
    @Cacheable(value = "summary", key = "#groupId")
    public PaymentSummary calculateSummary(final PaymentEnt paymentEnt) {
        PaymentInfo paymentInfo = new PaymentInfo(paymentEnt);
        LocalDateTime now = LocalDateTime.now();
        String groupId = groupPolicyFactory.getGroupIdByPaymentInfo(paymentInfo);
        PaymentSummary paymentSummary = paymentSummaryRepo.findById(groupId).orElse(new PaymentSummary(groupId, now));
        paymentSummary.calculate(paymentInfo);
        create(paymentSummary);
        return paymentSummary;
    }

}
