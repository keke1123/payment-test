package gh.shin.web.value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class PaymentRequest implements Serializable {
    private static final long serialVersionUID = -5339060201939844631L;
    @NotNull
    private Long paymentId;
    @NotNull
    private Long accountId;
    @NotNull
    private Integer amount;
    @NotNull
    private String methodType;
    @NotNull
    private String itemCategory;
    @NotNull
    private String region;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(final Long accountId) {
        this.accountId = accountId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(final Integer amount) {
        this.amount = amount;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(final String methodType) {
        this.methodType = methodType;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(final String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentRequest)) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(accountId, that.accountId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(methodType, that.methodType) &&
            Objects.equals(itemCategory, that.itemCategory) &&
            Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, accountId, amount, methodType, itemCategory, region);
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
            "paymentId=" + paymentId +
            ", accountId=" + accountId +
            ", amount=" + amount +
            ", methodType='" + methodType + '\'' +
            ", itemCategory='" + itemCategory + '\'' +
            ", region='" + region + '\'' +
            '}';
    }
}
