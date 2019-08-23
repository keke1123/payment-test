package gh.shin.group;

import gh.shin.web.value.PaymentInfo;

import java.util.Objects;

public final class GroupPolicy {
    private final String groupId;
    private final GroupFilter filter;

    public GroupPolicy(String groupId, GroupFilter filter) {
        if (groupId == null || groupId.length() < 1) {
            throw new NullPointerException("Group Id must not be empty.");
        }
        if (filter == null) {
            throw new NullPointerException("Group Filter must not be null.");
        }
        this.groupId = groupId;
        this.filter = filter;
    }

    public String getGroupId() {
        return groupId;
    }

    public GroupFilter getFilter() {
        return filter;
    }

    boolean filter(PaymentInfo paymentInfo) {
        return this.filter.filter(paymentInfo);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupPolicy)) return false;
        GroupPolicy that = (GroupPolicy) o;
        return Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return "GroupPolicy{" +
            "groupId='" + groupId + '\'' +
            ", filter=" + filter +
            '}';
    }
}
