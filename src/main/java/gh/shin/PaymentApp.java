package gh.shin;

import com.google.common.collect.Maps;
import gh.shin.Constants.Category;
import gh.shin.Constants.Group;
import gh.shin.Constants.Location;
import gh.shin.Constants.PaymentMethod;
import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.entity.PaymentSummary;
import gh.shin.entity.repo.AccountRepo;
import gh.shin.entity.repo.PaymentRepo;
import gh.shin.entity.repo.PaymentSummaryRepo;
import gh.shin.group.GroupPolicy;
import gh.shin.group.GroupPolicyFactory;
import gh.shin.util.PaymentSummaryUtil;
import gh.shin.web.value.PaymentInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gh.shin.Constants.Category.FASHION;
import static gh.shin.Constants.Group.GROUP_1;
import static gh.shin.Constants.Group.GROUP_2;
import static gh.shin.Constants.Group.GROUP_3;
import static gh.shin.Constants.Group.GROUP_4;
import static gh.shin.Constants.Location.GYEONGGI;
import static gh.shin.Constants.Location.SEOUL;
import static gh.shin.Constants.PaymentMethod.CARD;
import static gh.shin.Constants.PaymentMethod.CASH;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebMvc
@EnableAsync
public class PaymentApp {
    private static final Logger log = LoggerFactory.getLogger(PaymentApp.class);

    private final DispatcherServlet dispatcherServlet;
    private final AccountRepo accountRepo;
    private final PaymentRepo paymentRepo;
    private final PaymentSummaryRepo paymentSummaryRepo;
    private final EntityManagerFactory entityManagerFactory;

    @Value("classpath:csv/accounts.csv")
    private Resource accountResource;
    @Value("classpath:csv/payments.csv")
    private Resource paymentResource;

    @Autowired
    public PaymentApp(final DispatcherServlet dispatcherServlet, final AccountRepo accountRepo, final PaymentRepo paymentRepo
        , final PaymentSummaryRepo paymentSummaryRepo, final EntityManagerFactory entityManagerFactory) {
        this.dispatcherServlet = dispatcherServlet;
        this.accountRepo = accountRepo;
        this.paymentRepo = paymentRepo;
        this.paymentSummaryRepo = paymentSummaryRepo;
        this.entityManagerFactory = entityManagerFactory;
    }

    public static void main(String[] args) {
        if (System.getProperty("file.encoding") == null || !System.getProperty("file.encoding").equalsIgnoreCase("utf-8")) {
            throw new RuntimeException("please set '-Dfile.encoding=UTF-8' on java before run for multilingual service.");
        }
        SpringApplication.run(PaymentApp.class, args);
    }

    /**
     * Policy
     * GROUP_1 : 10,000원 미만 + 카드
     * GROUP_2 : 서울 || 경기 + 30대 (30~39)
     * GROUP_3 : 거주지역 == 결제지역 + 패션
     * GROUP_4 : 1,100,000원 이상 카드 || 1,000,00 이상 송금
     *
     * @return GroupPolicyFactory
     * @see Location
     * @see PaymentMethod
     * @see Category
     * @see Group
     */
    @Bean
    GroupPolicyFactory groupPolicyFactory() {
        GroupPolicy group1Policy = new GroupPolicy(GROUP_1, (f) -> f.getAmount() < 10000 && f.getMethodType().equals(CARD));
        GroupPolicy group2Policy = new GroupPolicy(GROUP_2, (f) -> (f.getAge() >= 30 && f.getAge() <= 39)
            && (f.getResidence().equals(SEOUL) || f.getResidence().equals(GYEONGGI)));
        GroupPolicy group3Policy = new GroupPolicy(GROUP_3, (f) -> f.getItemCategory().equals(FASHION) && f.getRegion().equals(f.getResidence()));
        GroupPolicy group4Policy = new GroupPolicy(GROUP_4, (f) ->
            (f.getAmount() >= 1100000 && f.getMethodType().equals(CARD))
                || (f.getAmount() >= 1000000 && f.getMethodType().equals(CASH)));

        GroupPolicyFactory groupPolicyFactory = new GroupPolicyFactory();
        groupPolicyFactory.addPolicies(group1Policy, group2Policy, group3Policy, group4Policy);
        return groupPolicyFactory;
    }

    /**
     * SpringApplication 기동 후 수행
     * payments.csv, accounts.csv 파일 파싱 --> DB 저장
     *
     * @param groupPolicyFactory <- Injected by groupPolicyFactory()
     * @return ApplicationListener
     */
    @Bean
    ApplicationListener<ContextRefreshedEvent> applicationListener(GroupPolicyFactory groupPolicyFactory) {
        return contextRefreshedEvent -> {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                int accCount = _persistAccounts(new FileReader(accountResource.getFile()), entityManager);
                List<PaymentEnt> payments = _persistPayments(new FileReader(paymentResource.getFile()), entityManager);

                log.info("{} Accounts, {} Payment rows insert complete.", accCount, payments.size());
                if (payments.size() > 0) {
                    int summaryCount = _summaryPayments(entityManager, _mapToPaymentInfo(payments), groupPolicyFactory);
                    log.info("{} PaymentSummary has been created.", summaryCount);
                }
                transaction.commit();
            } catch (IOException | HibernateException e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        };
    }

    private int _summaryPayments(EntityManager entityManager, List<PaymentInfo> paymentInfos, GroupPolicyFactory groupPolicyFactory) {
        List<String> groupIds = groupPolicyFactory.getGroupIds();
        Map<String, PaymentSummary> groupIdWithSummary = Maps.newHashMapWithExpectedSize(groupIds.size());
        LocalDateTime now = LocalDateTime.now();
        groupIds.forEach((id) -> groupIdWithSummary.put(id, new PaymentSummary(id, now)));

        for (PaymentInfo paymentInfo : paymentInfos) {
            String groupId = groupPolicyFactory.getGroupIdByPaymentInfo(paymentInfo);
            //group id에 해당이 없는 사람들은 저장하지 않아도 되므로 pass
            if (groupId.length() > 0) {
                PaymentSummary paymentSummary = groupIdWithSummary.get(groupId);
                PaymentSummaryUtil.calculate(paymentInfo, paymentSummary);
            }
        }
        groupIdWithSummary.values().forEach(entityManager::persist);
        return groupIdWithSummary.size();
    }

    private List<PaymentInfo> _mapToPaymentInfo(List<PaymentEnt> payments) {
        return payments.stream().map(PaymentInfo::new).collect(Collectors.toList());
    }

    /**
     * CSV 파일 파싱 후 AccountEnt로 변환 후 persist
     *
     * @param fileReader
     * @param entityManager
     * @return persist 완료된 row count
     * @throws IOException - 일반적인 File I/O 관련
     */
    private int _persistAccounts(FileReader fileReader, EntityManager entityManager) throws IOException {
        int result = 0;
        Iterable<CSVRecord> accountRecords = _getCsvRecords(fileReader);
        for (CSVRecord record : accountRecords) {
            Long id = Long.parseLong(record.get("accountId"));
            String residence = record.get("residence");
            Integer age = Integer.parseInt(record.get("age"));
            AccountEnt accountEnt = new AccountEnt(id, residence, age);
            entityManager.persist(accountEnt);
            result++;
        }
        return result;
    }

    /**
     * CSV 파일 파싱 후 PaymentEnt로 변환 후 persist
     *
     * @param fileReader
     * @param entityManager
     * @return persist 완료된 PaymentEnt list
     * @throws IOException - 일반적인 File I/O 관련
     */
    private List<PaymentEnt> _persistPayments(FileReader fileReader, EntityManager entityManager) throws IOException {
        Iterable<CSVRecord> paymentRecords = _getCsvRecords(fileReader);
        List<PaymentEnt> result = new ArrayList<>();
        for (CSVRecord record : paymentRecords) {
            Long id = Long.parseLong(record.get("paymentId"));
            Long accountId = Long.parseLong(record.get("accountId"));
            AccountEnt accountEnt = entityManager.find(AccountEnt.class, accountId);
            Integer amount = Integer.parseInt(record.get("amount"));
            String method = record.get("methodType");
            String item = record.get("itemCategory");
            String region = record.get("region");
            if (accountEnt != null) {
                PaymentEnt paymentEnt = PaymentEnt.builder()
                    .paymentId(id).account(accountEnt).amount(amount).methodType(method).itemCategory(item).region(region)
                    .build();
                entityManager.persist(paymentEnt);
                result.add(paymentEnt);
            } else {
                log.error("Account id {} didn't found.", accountId);
                log.debug("Unsaved Payment record : paymentId={},accountId={},amount={},methodType={},itemCategory={},region={}",
                    id, accountId, amount, method, item, region);
            }
        }
        return result;
    }

    /**
     * Resource를 기반으로 생성된 file path를 바탕으로 해당 CSV 데이터를 읽어들어 파싱
     * 1. 첫 번째 행을 헤더로 사용
     * 2. 빈 라인 무시
     * 3. 빈 문자열 트리밍
     *
     * @param fileReader
     * @return 파싱된 CSVRecord lines
     * @throws IOException - 일반적인 File I/O 관련
     */
    private Iterable<CSVRecord> _getCsvRecords(FileReader fileReader) throws IOException {
        return CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreEmptyLines()
            .withTrim().parse(fileReader);
    }

}
