package gh.shin.service;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.AccountRepo;
import gh.shin.entity.repo.PaymentRepo;
import gh.shin.util.CreationFailedException;
import gh.shin.util.PaymentRequestValidator;
import gh.shin.util.PaymentRequestValidator.ValidationResult;
import gh.shin.web.value.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final AccountRepo accountRepo;
    private final PaymentSummaryService paymentSummaryService;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, AccountRepo accountRepo, PaymentSummaryService paymentSummaryService) {
        this.paymentRepo = paymentRepo;
        this.accountRepo = accountRepo;
        this.paymentSummaryService = paymentSummaryService;
    }

    @Override
    @Transactional
    public boolean create(final PaymentRequest request) {
        ValidationResult validationResult = PaymentRequestValidator.validate(request);
        boolean result = false;
        if (validationResult.isValid()) {
            Long accId = request.getAccountId();
            AccountEnt account = accountRepo.findById(accId)
                .orElseThrow(() -> new CreationFailedException("Account " + accId + " is not found"));

            Long payId = request.getPaymentId();
            paymentRepo.findById(payId).ifPresent((o) -> {
                throw new CreationFailedException("Payment " + payId + " is already exists.");
            });

            PaymentEnt paymentEnt = new PaymentEnt();
            paymentEnt.setPaymentId(payId);
            paymentEnt.setAccount(account);
            paymentEnt.setAmount(request.getAmount());
            paymentEnt.setItemCategory(request.getItemCategory());
            paymentEnt.setMethodType(request.getMethodType());
            paymentEnt.setRegion(request.getRegion());
            paymentRepo.save(paymentEnt);
            PaymentSummary summaryResult = paymentSummaryService.calculateSummary(paymentEnt);
            if(summaryResult == null){
                throw new NullPointerException();
            }
            result = true;
        } else {
            throw new CreationFailedException(validationResult.getMsg());
        }
        return result;
    }
}
