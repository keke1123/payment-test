package gh.shin.service;

import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.PaymentSummaryRepo;
import gh.shin.group.GroupPolicyFactory;
import gh.shin.util.PaymentSummaryUtil;
import gh.shin.web.value.PaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
public class PaymentSummaryServiceImpl implements PaymentSummaryService {
    private final PaymentSummaryRepo paymentSummaryRepo;
    private final GroupPolicyFactory<PaymentInfo> groupPolicyFactory;

    @Autowired
    public PaymentSummaryServiceImpl(PaymentSummaryRepo paymentSummaryRepo, GroupPolicyFactory<PaymentInfo> groupPolicyFactory) {
        this.paymentSummaryRepo = paymentSummaryRepo;
        this.groupPolicyFactory = groupPolicyFactory;
    }

    @Override
    public Optional<PaymentSummary> findByGroupId(final String groupId) {
        return paymentSummaryRepo.findById(groupId);
    }

    @Async
    @Override
    @Transactional
    public Future<PaymentSummary> save(final PaymentSummary paymentSummary) {
        return new AsyncResult<>(paymentSummaryRepo.save(paymentSummary));
    }


    @Override
    public PaymentSummary calculateSummary(final PaymentEnt paymentEnt) {
        PaymentInfo paymentInfo = new PaymentInfo(paymentEnt);
        LocalDateTime now = LocalDateTime.now();
        String groupId = groupPolicyFactory.getGroupIdByPaymentInfo(paymentInfo);
        if(groupId.length() > 0) {
            PaymentSummary paymentSummary = PaymentSummaryUtil.calculate(paymentInfo, findByGroupId(groupId).orElse(new PaymentSummary(groupId, now)));
            save(paymentSummary);
            return paymentSummary;
        }else{
            return null;
        }
    }

}
