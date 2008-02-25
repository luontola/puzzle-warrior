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
    private final List<T> values = new ArrayList<T>();
    private int nextIndex = 0;

    public ShuffleBag(Random random) {
        this.random = random;
    }

    public void put(T value) {
        values.add(value);
    }

    public void putMany(T value, int count) {
        for (int i = 0; i < count; i++) {
            put(value);
        }
    }

    public T get() {
        if (values.size() == 0) {
            throw new IllegalStateException("bag is empty");
        }
        int randomUnusedIndex = nextIndex + random.nextInt(values.size() - nextIndex);
        swap(values, randomUnusedIndex, nextIndex);
        T selected = values.get(nextIndex);
        nextIndex = (nextIndex + 1) % values.size();
        return selected;
    }

    public List<T> getMany(int count) {
        List<T> values = new ArrayList<T>();
        for (int i = 0; i < count; i++) {
            values.add(get());
        }
        return values;
    }

    private static <T> void swap(List<T> list, int x, int y) {
        T tmp = list.get(y);
        list.set(y, list.get(x));
        list.set(x, tmp);
    }
}
