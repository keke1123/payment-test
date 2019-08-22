package gh.shin.group;


import gh.shin.web.value.PaymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupPolicyFactory {
    private static final Logger log = LoggerFactory.getLogger(GroupPolicyFactory.class);
    private final Set<GroupPolicy> policies = new HashSet<>();

    public void addPolicies(GroupPolicy... policies) {
        this.policies.addAll(Arrays.asList(policies));
    }

    public String getGroupIdByPaymentInfo(PaymentInfo paymentInfo) {
        String groupId = "";
        for (GroupPolicy policy : policies) {
            if (policy.filter(paymentInfo)) {
                if (groupId.equals("")) {
                    groupId = policy.getGroupId();
                } else {
                    log.error("PaymentInfo match duplicate. illegal data. {}", paymentInfo);
                    throw new RuntimeException("PaymentInfo match duplicate. illegal data.");
                }
            }
        }
        return groupId;
    }

    public List<String> getGroupIds() {
        return policies.stream().map(GroupPolicy::getGroupId).collect(Collectors.toList());
    }
}
