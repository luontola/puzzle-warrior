package net.orfjackal.puzzlewarrior;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
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
        private List<Integer> expectedValues = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        public ShuffleBag<Integer> create() {
            bag = new ShuffleBag<Integer>(new Random(123L));
            for (Integer value : expectedValues) {
                bag.add(value);
            }
            return bag;
        }

        public void onFirstRunAllValuesAreReturnedOnce() {
            List<Integer> values = bag.getMany(10);
            specify(values, does.containExactly(expectedValues));
        }

        public void onFirstRunTheValuesAreInRandomOrder() {
            List<Integer> values = bag.getMany(10);
            specify(values.get(0), does.not().equal(0));
            specify(values.get(0), does.not().equal(1));
            specify(values.get(0), does.not().equal(9));
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

        public void onFollowingRunsTheValuesAreInADifferentRandomOrderThanBefore() {
            List<Integer> run1 = bag.getMany(10);
            List<Integer> run2 = bag.getMany(10);
            List<Integer> run3 = bag.getMany(10);
            specify(run1, does.not().containInOrder(run2));
            specify(run1, does.not().containInOrder(run3));
            specify(run2, does.not().containInOrder(run3));
        }

        public void valuesAddedDuringARunWillBeIncludedInTheFollowingRun() {
            List<Integer> additionalValues = Arrays.asList(10, 11, 12, 13, 14, 15);
            List<Integer> expectedValues2 = new ArrayList<Integer>();
            expectedValues2.addAll(expectedValues);
            expectedValues2.addAll(additionalValues);

            List<Integer> run1 = bag.getMany(5);
            for (Integer i : additionalValues) {
                bag.add(i);
            }
            run1.addAll(bag.getMany(5));
            List<Integer> run2 = bag.getMany(16);

            specify(run1, does.containExactly(expectedValues));
            specify(run2, does.containExactly(expectedValues2));
        }
    }

    public class AShuffleBagWithManyOfTheSameValue {

        private ShuffleBag<Character> bag;
        private List<Character> expectedValues = Arrays.asList('a', 'b', 'b', 'c', 'c', 'c');

        public ShuffleBag<Character> create() {
            bag = new ShuffleBag<Character>(new Random(123L));
            bag.addMany('a', 1);
            bag.addMany('b', 2);
            bag.addMany('c', 3);
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
