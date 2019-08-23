package gh.shin.group;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.web.value.PaymentInfo;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class GroupPolicyTest {

    private static GroupFilter FILTER;
    private static GroupPolicy POLICY;

    @BeforeClass
    public static void set() {
        FILTER = (info) -> info.getAmount() == 100;
        POLICY = new GroupPolicy("a", FILTER);
    }

    @Test(expected = RuntimeException.class)
    public void _01_filter_fail_invalid_object() {
        PaymentEnt ent = new PaymentEnt();
        ent.setAmount(0);
        PaymentInfo info = new PaymentInfo(ent);
        assertFalse(POLICY.filter(info));
    }

    @Test
    public void _02_filter_false() {
        PaymentEnt ent = new PaymentEnt();
        ent.setAccount(new AccountEnt());
        ent.setAmount(0);
        PaymentInfo info = new PaymentInfo(ent);
        assertFalse(POLICY.filter(info));
    }

    @Test
    public void _02_filter_true() {
        PaymentEnt ent = new PaymentEnt();
        ent.setAccount(new AccountEnt());
        ent.setAmount(100);
        PaymentInfo info = new PaymentInfo(ent);
        assertTrue(POLICY.filter(info));
    }

    @Test(expected = NullPointerException.class)
    public void _03_invalid_filter() {
        GroupFilter filter = null;
        new GroupPolicy("a", filter);
    }

    @Test(expected = NullPointerException.class)
    public void _043_invalid_groupId() {
        GroupFilter filter = null;
        new GroupPolicy(null, filter);
    }

}
