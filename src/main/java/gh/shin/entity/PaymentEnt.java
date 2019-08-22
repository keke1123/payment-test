package gh.shin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity(name = "PAYMENT_DATA")
public class PaymentEnt implements Serializable {
    private static final long serialVersionUID = -5643781569083881863L;
    private Long paymentId;
    private AccountEnt account;
    private Integer amount;
    private String methodType;
    private String itemCategory;
    private String region;

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @Column(name = "PAYMENT_ID")
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final Long paymentId) {
        this.paymentId = paymentId;
    }

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    public AccountEnt getAccount() {
        return account;
    }

    public void setAccount(final AccountEnt account) {
        this.account = account;
    }

    @Column(name = "AMOUNT")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(final Integer amount) {
        this.amount = amount;
    }

    @Column(name = "METHOD_TYPE")
    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(final String methodType) {
        this.methodType = methodType;
    }

    @Column(name = "ITEM_CATEGORY")
    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(final String itemCategory) {
        this.itemCategory = itemCategory;
    }

    @Column(name = "REGION")
    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public static class Builder {
        private Long paymentId;
        private AccountEnt account;
        private Integer amount;
        private String methodType;
        private String itemCategory;
        private String region;

        public Builder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder account(AccountEnt account) {
            this.account = account;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder methodType(String methodType) {
            this.methodType = methodType;
            return this;
        }

        public Builder itemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public PaymentEnt build() {
            PaymentEnt ent = new PaymentEnt();
            ent.setPaymentId(paymentId);
            ent.setAccount(account);
            ent.setAmount(amount);
            ent.setMethodType(methodType);
            ent.setItemCategory(itemCategory);
            ent.setRegion(region);
            return ent;
        }
    }
}
