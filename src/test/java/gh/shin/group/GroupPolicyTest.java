package gh.shin.group;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.web.value.PaymentInfo;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static gh.shin.Constants.Category.FASHION;
import static gh.shin.Constants.Location.BUSAN;
import static gh.shin.Constants.PaymentMethod.CARD;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class GroupPolicyTest {

    private static GroupFilter<PaymentInfo> FILTER;
    private static GroupPolicy<PaymentInfo> POLICY;

    @BeforeClass
    public static void set() {
        FILTER = (info) -> info.getAmount() == 100;
        POLICY = new GroupPolicy<>("a", FILTER);
    }

    @Test(expected = RuntimeException.class)
    public void _01_filter_fail_invalid_object() {
        PaymentEnt ent = new PaymentEnt();
        ent.setAmount(0);
        PaymentInfo info = new PaymentInfo(ent);
        assertFalse(POLICY.filter(info));
    }

    @Test
    public void _02_filter_true() {
        PaymentEnt ent = getPaymentEnt();
        PaymentInfo info = new PaymentInfo(ent);
        assertTrue(POLICY.filter(info));
    }

    @Test(expected = NullPointerException.class)
    public void _03_invalid_filter() {
        GroupFilter<PaymentInfo> filter = null;
        new GroupPolicy<>("a", filter);
    }

    @Test(expected = NullPointerException.class)
    public void _04_invalid_groupId() {
        GroupFilter<PaymentInfo> filter = null;
        new GroupPolicy<>(null, filter);
    }

    public static PaymentEnt getPaymentEnt(){
        AccountEnt acc = new AccountEnt();
        acc.setAge(34);
        acc.setResidence(BUSAN);
        PaymentEnt ent = new PaymentEnt();
        ent.setAccount(acc);
        ent.setItemCategory(FASHION);
        ent.setMethodType(CARD);
        ent.setAmount(100);
        ent.setRegion(BUSAN);
        return ent;
    }

}
