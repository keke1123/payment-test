package gh.shin.util;

import gh.shin.entity.PaymentSummary;
import gh.shin.web.value.PaymentInfo;

public class PaymentSummaryUtil {
    private PaymentSummaryUtil() {
    }

    public static PaymentSummary calculate(PaymentInfo info, PaymentSummary summary) {
        summary.setCount(summary.getCount() + 1);
        summary.setTotalAmount(summary.getTotalAmount() + info.getAmount());

        if (summary.getMinAmount() > info.getAmount()) {
            summary.setMinAmount(info.getAmount());
        }
        if (summary.getMaxAmount() < info.getAmount()) {
            summary.setMaxAmount(info.getAmount());
        }
        summary.setAvgAmount(Math.toIntExact(summary.getTotalAmount() / summary.getCount()));
        return summary;
    }
}
