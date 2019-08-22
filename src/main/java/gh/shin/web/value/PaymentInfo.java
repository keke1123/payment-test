package gh.shin.web.value;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;

import java.io.Serializable;
import java.util.Objects;

public final class PaymentInfo implements Serializable {
    private static final long serialVersionUID = -7722974456683772876L;
    private final Long accountId;
    private final Integer age;
    private final String region;
    private final String residence;
    private final Integer amount;
    private final String itemCategory;
    private final String methodType;

    public PaymentInfo(PaymentEnt paymentEnt) {
        AccountEnt accountEnt = paymentEnt.getAccount();
        if (accountEnt == null)
            throw new RuntimeException("Account data is null");
        this.accountId = accountEnt.getAccountId();
        this.age = accountEnt.getAge();
        this.residence = accountEnt.getResidence();
        this.region = paymentEnt.getRegion();
        this.amount = paymentEnt.getAmount();
        this.itemCategory = paymentEnt.getItemCategory();
        this.methodType = paymentEnt.getMethodType();
    }

    public Long getAccountId() {
        return accountId;
    }

    public Integer getAge() {
        return age;
    }

    public String getRegion() {
        return region;
    }

    public String getResidence() {
        return residence;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getMethodType() {
        return methodType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentInfo)) return false;
        PaymentInfo that = (PaymentInfo) o;
        return Objects.equals(accountId, that.accountId) &&
            Objects.equals(age, that.age) &&
            Objects.equals(region, that.region) &&
            Objects.equals(residence, that.residence) &&
            Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, age, region, residence, amount);
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
            "accountId=" + accountId +
            ", age=" + age +
            ", region='" + region + '\'' +
            ", residence='" + residence + '\'' +
            ", amount=" + amount +
            ", itemCategory='" + itemCategory + '\'' +
            ", methodType='" + methodType + '\'' +
            '}';
    }
}
