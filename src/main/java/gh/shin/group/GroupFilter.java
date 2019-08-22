package gh.shin.group;

import gh.shin.web.value.PaymentInfo;

@FunctionalInterface
public interface GroupFilter {
    boolean filter(PaymentInfo filter);
}
