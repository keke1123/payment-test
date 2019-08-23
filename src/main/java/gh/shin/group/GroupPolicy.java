package gh.shin.group;

import java.util.Objects;

public final class GroupPolicy<T extends FilteringTarget> {
    private final String groupId;
    private final GroupFilter<T> filter;

    public GroupPolicy(String groupId, GroupFilter<T> filter) {
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

    boolean filter(T target) {
        if (target.isValid()) {
            return this.filter.filter(target);
        } else {
            throw new RuntimeException("Filtering target is invalid. " + target.toString());
        }
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
