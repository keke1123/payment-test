package gh.shin.group;

import gh.shin.web.value.PaymentInfo;

import java.util.Objects;

public final class GroupPolicy {
    private final String groupId;
    private final GroupFilter filter;

    public GroupPolicy(String groupId, GroupFilter filter) {
        if (groupId == null || groupId.length() < 1) {
            throw new RuntimeException("Group Id must not be empty.");
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

    public boolean filter(PaymentInfo paymentInfo) {
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
}
