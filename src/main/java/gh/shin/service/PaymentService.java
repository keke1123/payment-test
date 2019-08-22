package gh.shin.service;

import gh.shin.web.value.PaymentRequest;

import javax.transaction.Transactional;

public interface PaymentService {

    boolean create(PaymentRequest request);
}
