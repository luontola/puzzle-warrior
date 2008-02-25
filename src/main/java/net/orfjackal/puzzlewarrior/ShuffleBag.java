package net.orfjackal.puzzlewarrior;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Esko Luontola
 * @since 25.2.2008
 */
public class ShuffleBag<T> {

    private final Random random;

    /**
     * Used values are in the range {@code 0 <= index < cursor}.
     * Unused values are in the range {@code cursor <= index < values.size()}.
     */
    private final List<T> values = new ArrayList<T>();
    private int cursor = 0;

    public ShuffleBag() {
        this(new Random());
    }

    public ShuffleBag(Random random) {
        this.random = random;
    }

    public void add(T value) {
        values.add(value);
    }

    public T get() {
        if (values.size() == 0) {
            throw new IllegalStateException("bag is empty");
        }
        int grab = randomUnused();
        T value = values.get(grab);
        markAsUsed(grab);
        return value;
    }

    private int randomUnused() {
        return cursor + random.nextInt(values.size() - cursor);
    }

    private void markAsUsed(int indexOfUsed) {
        swap(values, indexOfUsed, cursor);
        cursor = (cursor + 1) % values.size();
    }

    private static <T> void swap(List<T> list, int x, int y) {
        T tmp = list.get(x);
        list.set(x, list.get(y));
        list.set(y, tmp);
    }

    public void addMany(T value, int quantity) {
        for (int i = 0; i < quantity; i++) {
            add(value);
        }
    }

    public List<T> getMany(int quantity) {
        List<T> results = new ArrayList<T>(quantity);
        for (int i = 0; i < quantity; i++) {
            results.add(get());
        }
        return results;
    }
}
