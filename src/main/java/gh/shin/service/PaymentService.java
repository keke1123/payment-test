package gh.shin.service;

import gh.shin.web.value.PaymentRequest;

public interface PaymentService {

    boolean create(PaymentRequest request);
}
