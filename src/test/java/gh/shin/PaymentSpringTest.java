package gh.shin;

import gh.shin.Constants.Category;
import gh.shin.Constants.Group;
import gh.shin.Constants.Location;
import gh.shin.Constants.PaymentMethod;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.PaymentRepo;
import gh.shin.entity.repo.PaymentSummaryRepo;
import gh.shin.service.PaymentService;
import gh.shin.service.PaymentSummaryService;
import gh.shin.util.CreationFailedException;
import gh.shin.web.value.PaymentRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
public class PaymentSpringTest {
    private Logger log = LoggerFactory.getLogger(PaymentSpringTest.class);
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private PaymentSummaryService paymentSummaryService;
    @Autowired
    private PaymentSummaryRepo paymentSummaryRepo;

    @Test(expected = CreationFailedException.class)
    public void _1_payment_01_PaymentService_fail_validation_empty() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_02_PaymentService_fail_validation_no_exist_accId() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(100L);
        paymentRequest.setAccountId(654L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType("method");
        paymentRequest.setItemCategory("item");
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_03_PaymentService_fail_validation_exist_payId() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1L);
        paymentRequest.setAccountId(654L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType("method");
        paymentRequest.setItemCategory("item");
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_04_PaymentService_fail_validation_methodType() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1000L);
        paymentRequest.setAccountId(1L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType("method");
        paymentRequest.setItemCategory("item");
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_05_PaymentService_fail_validation_itemCategory() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1000L);
        paymentRequest.setAccountId(1L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType(PaymentMethod.CARD);
        paymentRequest.setItemCategory("item");
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_06_PaymentService_fail_validation_location() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1000L);
        paymentRequest.setAccountId(1L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType(PaymentMethod.CARD);
        paymentRequest.setItemCategory(Category.BEAUTY);
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test(expected = CreationFailedException.class)
    public void _1_payment_07_PaymentService_fail_validation_location() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1000L);
        paymentRequest.setAccountId(1L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType(PaymentMethod.CARD);
        paymentRequest.setItemCategory(Category.BEAUTY);
        paymentRequest.setRegion("asdfasdf");
        paymentService.create(paymentRequest);
    }

    @Test
    public void _1_payment_08_PaymentService_success() {
        long beforeSize = paymentRepo.count();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentId(1000L);
        paymentRequest.setAccountId(1L);
        paymentRequest.setAmount(111);
        paymentRequest.setMethodType(PaymentMethod.CARD);
        paymentRequest.setItemCategory(Category.BEAUTY);
        paymentRequest.setRegion(Location.BUSAN);
        paymentService.create(paymentRequest);
        assertTrue(beforeSize < paymentRepo.count());
    }


    @Test(expected = RuntimeException.class)
    public void _2_PaymentSummary_01_findByGroupId_null() {
        assertNull(paymentSummaryService.findByGroupId("asdf123").orElseThrow(RuntimeException::new));
    }

    @Test
    public void _2_PaymentSummary_02_findByGroupId() {
        assertNotNull(paymentSummaryService.findByGroupId(Group.GROUP_1));
    }

    @Test(expected = ExecutionException.class)
    public void _2_PaymentSummary_03_save_id_null() throws InterruptedException, ExecutionException, TimeoutException {
        PaymentSummary summary = new PaymentSummary();
        assertNotNull(paymentSummaryService.save(summary).get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void _2_PaymentSummary_04_save() throws InterruptedException {
        LocalDateTime createTime = LocalDateTime.now();
        PaymentSummary summary = new PaymentSummary();
        summary.setGroupId(Group.GROUP_1);
        summary.setAvgAmount(5555555);
        summary.setCount(333L);
        summary.setCreatedTime(createTime);
        summary.setMaxAmount(555);
        summary.setMinAmount(11);
        summary.setTotalAmount(6666666666L);
        Future<PaymentSummary> newSum = paymentSummaryService.save(summary);
        while (!newSum.isDone()) {
            log.info("not saved yet.");
            Thread.sleep(1000);
        }
        PaymentSummary saved = paymentSummaryRepo.findById(Group.GROUP_1).orElse(null);
        assertEquals(5555555, saved.getAvgAmount().intValue());
    }

    //TODO Controller teet

    //TODO REST test

}
