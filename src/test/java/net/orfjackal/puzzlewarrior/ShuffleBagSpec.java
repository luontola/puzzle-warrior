package net.orfjackal.puzzlewarrior;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

/**
 * @author Esko Luontola
 * @since 25.2.2008
 */
@RunWith(JDaveRunner.class)
public class ShuffleBagSpec extends Specification<ShuffleBag<?>> {

    public class AShuffleBagWithOneOfEachValue {

        private ShuffleBag<Integer> bag;
        private Integer[] expectedValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        public ShuffleBag<Integer> create() {
            bag = new ShuffleBag<Integer>(new Random(123L));
            for (Integer value : expectedValues) {
                bag.put(value);
            }
            return bag;
        }

        public void allValuesAreReturnedOnce() {
            List<Integer> values = bag.getMany(10);
            specify(values, does.containExactly(expectedValues));
        }

        public void theReturnedValuesAreInRandomOrder() {
            List<Integer> values = bag.getMany(10);
            specify(values, does.not().containInOrder(expectedValues));
            specify(values, does.not().containInPartialOrder(1, 2, 3));
            specify(values, does.not().containInPartialOrder(4, 5, 6));
            specify(values, does.not().containInPartialOrder(7, 8, 9));
            specify(values, does.not().containInPartialOrder(3, 2, 1));
            specify(values, does.not().containInPartialOrder(6, 5, 4));
            specify(values, does.not().containInPartialOrder(9, 8, 7));
        }

        public void onFollowingRunsAllValuesAreReturnedOnce() {
            List<Integer> run1 = bag.getMany(10);
            List<Integer> run2 = bag.getMany(10);
            List<Integer> run3 = bag.getMany(10);
            specify(run1, does.containExactly(expectedValues));
            specify(run2, does.containExactly(expectedValues));
            specify(run3, does.containExactly(expectedValues));
        }

        public void onFollowingRunsTheValuesAreInDifferentOrder() {
            List<Integer> run1 = bag.getMany(10);
            List<Integer> run2 = bag.getMany(10);
            List<Integer> run3 = bag.getMany(10);
            specify(run1, does.not().containInOrder(run2));
            specify(run1, does.not().containInOrder(run3));
            specify(run2, does.not().containInOrder(run3));
        }

        public void moreValuesCanBeAddedWhileTheBagIsInUse() {
            Integer[] additionalValues = {10, 11, 12, 13, 14, 15};
            List<Integer> values = bag.getMany(6);
            for (Integer i : additionalValues) {
                bag.put(i);
            }
            values.addAll(bag.getMany(10));
            specify(values.size(), does.equal(expectedValues.length + additionalValues.length));
            specify(values, does.containAll(expectedValues));
            specify(values, does.containAll(additionalValues));
            specify(values, does.not().containInPartialOrder(additionalValues));
        }
    }

    public class AShuffleBagWithManyOfTheSameValue {

        private ShuffleBag<Character> bag;
        private Character[] expectedValues = {'a', 'b', 'b', 'c', 'c', 'c'};

        public ShuffleBag<Character> create() {
            bag = new ShuffleBag<Character>(new Random(123L));
            bag.putMany('a', 1);
            bag.putMany('b', 2);
            bag.putMany('c', 3);
            return bag;
        }

        public void allValuesAreReturnedTheSpecifiedNumberOfTimes() {
            List<Character> values = bag.getMany(6);
            specify(values, does.containExactly(expectedValues));
        }
    }

    public class AnEmptyShuffleBag {

        private ShuffleBag<Object> bag;

        public ShuffleBag<Object> create() {
            bag = new ShuffleBag<Object>();
            return bag;
        }

        public void canNotBeUsed() {
            specify(new jdave.Block() {
                public void run() throws Throwable {
                    bag.get();
                }
            }, should.raise(IllegalStateException.class));
        }
    }
}
