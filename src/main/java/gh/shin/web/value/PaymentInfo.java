package gh.shin.web.value;

import gh.shin.entity.AccountEnt;
import gh.shin.entity.PaymentEnt;
import gh.shin.group.FilteringTarget;

import java.io.Serializable;
import java.util.Objects;

public final class PaymentInfo implements Serializable, FilteringTarget {
    private static final long serialVersionUID = -7722974456683772876L;
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
        this.age = accountEnt.getAge();
        this.residence = accountEnt.getResidence();
        this.region = paymentEnt.getRegion();
        this.amount = paymentEnt.getAmount();
        this.itemCategory = paymentEnt.getItemCategory();
        this.methodType = paymentEnt.getMethodType();
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
        return Objects.equals(age, that.age) &&
            Objects.equals(region, that.region) &&
            Objects.equals(residence, that.residence) &&
            Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, region, residence, amount);
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
            "age=" + age +
            ", region='" + region + '\'' +
            ", residence='" + residence + '\'' +
            ", amount=" + amount +
            ", itemCategory='" + itemCategory + '\'' +
            ", methodType='" + methodType + '\'' +
            '}';
    }

    @Override
    public boolean isValid() {
        return Objects.nonNull(age) && Objects.nonNull(region) && Objects.nonNull(residence) &&
            Objects.nonNull(amount) && Objects.nonNull(itemCategory) && Objects.nonNull(methodType);
    }
}
