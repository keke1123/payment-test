package gh.shin.group;

@FunctionalInterface
public interface GroupFilter<T extends FilteringTarget> {
    boolean filter(T filter);
}
