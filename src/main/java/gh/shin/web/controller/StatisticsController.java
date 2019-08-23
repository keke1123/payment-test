package gh.shin.web.controller;

import gh.shin.entity.PaymentSummary;
import gh.shin.service.PaymentSummaryServiceImpl;
import gh.shin.util.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {
    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    private final PaymentSummaryServiceImpl paymentSummaryService;

    @Autowired
    public StatisticsController(PaymentSummaryServiceImpl paymentSummaryService) {
        this.paymentSummaryService = paymentSummaryService;
    }

    @GetMapping("/statistics")
    public PaymentSummary readByGroupId(@RequestParam(name = "groupId", required = true) String groupId) {
        log.info("request received: {}", groupId);
        return paymentSummaryService.findByGroupId(groupId).orElseThrow(() -> new NoDataFoundException("Group: '" + groupId + "' is not found."));
    }
}
