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
            board.dropNewBlock('b', 'g');
            return board;
        }

        public void startsFromTheTopCenter() {
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

        public void stopsOnReachingTheBottom() {
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

        public void stopsOnReachingTheBottomUpsideDown() {
            board.rotateClockwise();
            board.rotateClockwise();
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...b..\n" +
                                                 "...g..\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...b..\n" +
                                                 "...g..\n"));
        }

        public void stopsOnHittingAnotherBlock() {
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.dropNewBlock('r', 'y');
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

        public void breaksOnHittingAnotherBlockSideways() {
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.dropNewBlock('r', 'y');
            board.rotateClockwise();
            specify(board.toString(), does.equal("...ry.\n" +
                                                 "......\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.tick();
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...ry.\n" +
                                                 "...g..\n" +
                                                 "...b..\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...r..\n" +
                                                 "...g..\n" +
                                                 "...by.\n"));
        }

        public void atMostOneBlockMayBeFallingAtATime() {
            specify(board.toString(), does.equal("...b..\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
            specify(new Block() {
                public void run() throws Throwable {
                    board.dropNewBlock('r', 'y');
                }
            }, does.raise(IllegalStateException.class));
            specify(board.toString(), does.equal("...b..\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeMovedLeft() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.moveLeft();
            specify(board.toString(), does.equal("..g...\n" +
                                                 "..b...\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeMovedRight() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.moveRight();
            specify(board.toString(), does.equal("....g.\n" +
                                                 "....b.\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeRotatedClockwise() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...bg.\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...b..\n" +
                                                 "...g..\n" +
                                                 "......\n"));
            board.rotateClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "..gb..\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeRotatedCounterClockwise() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateCounterClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "..gb..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateCounterClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...b..\n" +
                                                 "...g..\n" +
                                                 "......\n"));
            board.rotateCounterClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...bg.\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeFlipped() {
            board.tick();
            specify(board.toString(), does.equal("...g..\n" +
                                                 "...b..\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.flip();
            specify(board.toString(), does.equal("...b..\n" +
                                                 "...g..\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void mayBeFlippedSideways() {
            board.tick();
            board.rotateClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...bg.\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.flip();
            specify(board.toString(), does.equal("......\n" +
                                                 "...gb.\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void willNotMoveLeftWhenHittingAWall() {
            board.moveLeft();
            board.moveLeft();
            board.moveLeft();
            specify(board.toString(), does.equal("b.....\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.moveLeft();
            specify(board.toString(), does.equal("b.....\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void willNotMoveRightWhenHittingAWall() {
            board.moveRight();
            board.moveRight();
            specify(board.toString(), does.equal(".....b\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.moveRight();
            specify(board.toString(), does.equal(".....b\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void willNotRotateClockwiseWhenHittingAWall() {
            board.tick();
            board.moveRight();
            board.moveRight();
            specify(board.toString(), does.equal(".....g\n" +
                                                 ".....b\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateClockwise();
            specify(board.toString(), does.equal(".....g\n" +
                                                 ".....b\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void willNotRotateCounterClockwiseWhenHittingAWall() {
            board.tick();
            board.moveLeft();
            board.moveLeft();
            board.moveLeft();
            specify(board.toString(), does.equal("g.....\n" +
                                                 "b.....\n" +
                                                 "......\n" +
                                                 "......\n"));
            board.rotateCounterClockwise();
            specify(board.toString(), does.equal("g.....\n" +
                                                 "b.....\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void willNotMoveNorRotateWhenHittingAnotherBlock() {
            board.moveLeft();
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "..g...\n" +
                                                 "..b...\n"));
            board.dropNewBlock('r', 'y');
            board.tick(2);
            specify(board.toString(), does.equal("......\n" +
                                                 "...y..\n" +
                                                 "..gr..\n" +
                                                 "..b...\n"));
            board.moveLeft();
            specify(board.toString(), does.equal("......\n" +
                                                 "...y..\n" +
                                                 "..gr..\n" +
                                                 "..b...\n"));
            board.rotateCounterClockwise();
            specify(board.toString(), does.equal("......\n" +
                                                 "...y..\n" +
                                                 "..gr..\n" +
                                                 "..b...\n"));
        }
    }

    public class ExplosiveBlocks {

        public Board create() {
            board = new Board(4, 6);
            board.dropNewBlock('b', 'g');
            board.rotateClockwise();
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "...bg.\n"));
            return board;
        }

        public void explodeBlocksOfTheSameColorWhenTouchingThem() {
            board.dropNewBlock('B', 'g');
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...g..\n" +
                                                 "...B..\n" +
                                                 "...bg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "...gg.\n"));
        }

        public void doNothingWhenTouchingBlocksOfADifferentColor() {
            board.dropNewBlock('G', 'b');
            board.tick(3);
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...b..\n" +
                                                 "...G..\n" +
                                                 "...bg.\n"));
        }

    }

    // TODO: explosive blocks blow same color
    // TODO: explosive diamonds blow touched color
}
