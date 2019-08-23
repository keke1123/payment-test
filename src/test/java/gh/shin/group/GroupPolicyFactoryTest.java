package gh.shin.group;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.web.value.PaymentInfo;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static gh.shin.Constants.Category.FASHION;
import static gh.shin.Constants.Location.BUSAN;
import static gh.shin.Constants.PaymentMethod.CARD;
import static gh.shin.group.GroupPolicyTest.getPaymentEnt;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class GroupPolicyFactoryTest {

    private GroupPolicyFactory<PaymentInfo> policyFactory;

    @Before
    public void set() {
        policyFactory = new GroupPolicyFactory<>();
    }

    @Test
    public void _01_addPolicies_with_null() {
        GroupPolicy<PaymentInfo> p1 = new GroupPolicy<>("ab1", (f) -> true);
        GroupPolicy<PaymentInfo> p2 = new GroupPolicy<>("ab2", (f) -> true);
        GroupPolicy<PaymentInfo> p3 = null;
        policyFactory.addPolicies(p1, p2, p3);
        assertEquals(2, policyFactory.size());
    }

    @Test
    public void _02_getGroupIds_success() {
        GroupPolicy<PaymentInfo> p1 = new GroupPolicy<>("ab1", (f) -> true);
        GroupPolicy<PaymentInfo> p2 = new GroupPolicy<>("ab2", (f) -> true);
        policyFactory.addPolicies(p1, p2);
        assertEquals(2, policyFactory.getGroupIds().size());
    }

    @Test
    public void _03_getGroupIds_success_with_duplicate() {
        GroupPolicy<PaymentInfo> p1 = new GroupPolicy<>("ab1", (f) -> true);
        GroupPolicy<PaymentInfo> p2 = new GroupPolicy<>("ab1", (f) -> false);
        policyFactory.addPolicies(p1, p2);
        assertEquals(1, policyFactory.getGroupIds().size());

        PaymentEnt ent = getPaymentEnt();

        PaymentInfo info = new PaymentInfo(ent);
        String id = policyFactory.getGroupIdByPaymentInfo(info);
        assertEquals("ab1", id);
    }

    @Test
    public void _04_getGroupIds_success_with_wrong_input() {
        GroupPolicy<PaymentInfo> p1 = new GroupPolicy<>("ab1", (f) -> false);
        GroupPolicy<PaymentInfo> p2 = new GroupPolicy<>("ab1", (f) -> true);
        policyFactory.addPolicies(p1, p2);
        assertEquals(1, policyFactory.getGroupIds().size());

        PaymentEnt ent = getPaymentEnt();

        PaymentInfo info = new PaymentInfo(ent);
        String id = policyFactory.getGroupIdByPaymentInfo(info);
        assertEquals("", id);
    }
}