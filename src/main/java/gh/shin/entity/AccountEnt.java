package gh.shin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "ACCOUNT_DATA")
public class AccountEnt implements Serializable {
    private static final long serialVersionUID = -8879142076237230028L;
    private Long accountId;
    private String residence;
    private Integer age;

    public AccountEnt() {
    }

    public AccountEnt(Long accountId, String residence, Integer age) {
        this.accountId = accountId;
        this.residence = residence;
        this.age = age;
    }

    @Id
    @Column(name = "ACCOUNT_ID")
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(final Long accountId) {
        this.accountId = accountId;
    }

    @Column(name = "RESIDENCE")
    public String getResidence() {
        return residence;
    }

    public void setResidence(final String residence) {
        this.residence = residence;
    }

    @Column(name = "AGE")
    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }
}
