package gh.shin.group;


import com.google.common.collect.Lists;
import gh.shin.web.value.PaymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupPolicyFactory {
    private static final Logger log = LoggerFactory.getLogger(GroupPolicyFactory.class);
    private final Set<GroupPolicy> policies = new HashSet<>();

    public void addPolicies(GroupPolicy... policies) {
        List<GroupPolicy> policyList = Lists.newArrayList(policies);

        Iterator<GroupPolicy> argPolicyItr = policyList.iterator();
        while (argPolicyItr.hasNext()) {
            GroupPolicy argPolicy = argPolicyItr.next();
            if (argPolicy != null) {
                boolean duplicated = false;
                for (GroupPolicy policy : this.policies) {
                    if (argPolicy.getGroupId().equals(policy.getGroupId())) {
                        log.warn("{} groupId already registered. {} will be ignored.", argPolicy.getGroupId(), argPolicy);
                        argPolicyItr.remove();
                        duplicated = true;
                        break;
                    }
                }
                if (!duplicated) {
                    this.policies.add(argPolicy);
                }
            }
        }
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

    public int size() {
        return this.policies.size();
    }
}
