package gh.shin.web.controller;

import gh.shin.service.PaymentServiceImpl;
import gh.shin.web.value.PaymentRequest;
import gh.shin.web.value.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public WebResponse createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("request received: {}", request);
        paymentService.create(request);
        return WebResponse.success();
    }
}
