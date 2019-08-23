package gh.shin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gh.shin.web.value.PaymentInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "PAYMENT_SUMMARY")
public class PaymentSummary implements Serializable {
    private static final long serialVersionUID = -3758260558794171828L;
    private String groupId;
    private Long count = 0L;
    private Long totalAmount = 0L;
    private Integer avgAmount = 0;
    private Integer minAmount = Integer.MAX_VALUE;
    private Integer maxAmount = 0;
    @JsonIgnore
    private LocalDateTime createdTime;

    public PaymentSummary() {
    }

    public PaymentSummary(String groupId, LocalDateTime createdTime) {
        this.groupId = groupId;
        this.createdTime = createdTime;
    }

    @Id
    @Column(name = "GROUP_ID")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "COUNT")
    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    @Column(name = "TOTAL_AMOUNT")
    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "AVG_AMOUNT")
    public Integer getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(final Integer avgAmount) {
        this.avgAmount = avgAmount;
    }

    @Column(name = "MIN_AMOUNT")
    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(final Integer minAmount) {
        this.minAmount = minAmount;
    }

    @Column(name = "MAX_AMOUNT")
    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(final Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Column(name = "CREATED_TIME")
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(final LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }


}
