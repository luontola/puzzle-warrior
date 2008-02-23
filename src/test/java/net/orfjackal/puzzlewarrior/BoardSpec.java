package net.orfjackal.puzzlewarrior;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
@RunWith(JDaveRunner.class)
public class BoardSpec extends Specification<Board> {

    private Board board;

    public class AnEmptyBoard {

        public Board create() {
            board = new Board(1, 1);
            return board;
        }

        public void isEmpty() {
            specify(board.toString(), does.equal(".\n"));
        }
    }

    public class AFallingBlock {

        public Board create() {
            board = new Board(4, 6);
            board.addBlock('b', 'g');
            return board;
        }

        public void startsFromTheTopMiddle() {
            specify(board.toString(), does.equal("...b..\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void fallsOnTick() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void stopsWhenItReachesTheBottom() {
            board.tick(3);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
        }

        public void stopsWhenItHitsAnotherBlock() {
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.addBlock('r', 'y');
            specify(board.toString(), does.equal("...r..\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.tick();
            specify(should.be.falling());
            specify(board.toString(), does.equal("...y..\n" +
                                                 "...r..\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("...y..\n" +
                                                 "...r..\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
        }

        public void atMostOneBlockMayBeFallingAtATime() {
            specify(board.toString(), does.equal("...b..\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
            specify(new Block() {
                public void run() throws Throwable {
                    board.addBlock('r', 'y');
                }
            }, does.raise(IllegalStateException.class));
            specify(board.toString(), does.equal("...b..\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

//        public void mayBeRotatedClockwise() {
//            board.tick();
//            specify(board.toString(), does.equal("...g..\n" +
//                                                 "...b..\n" +
//                                                 "......\n" +
//                                                 "......\n"));
//            board.rotateRight();
//            specify(board.toString(), does.equal("......\n" +
//                                                 "...bg.\n" +
//                                                 "......\n" +
//                                                 "......\n"));
//        }

        // TODO: rotations
        // TODO: breaks when hits blocks sideways
        // TODO: move left and right
        // TODO: move down
        // TODO: explosive blocks blow same color
        // TODO: explosive diamonds blow touched color
    }
}
