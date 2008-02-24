package net.orfjackal.puzzlewarrior;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import static net.orfjackal.puzzlewarrior.Block.EMPTY;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
@RunWith(JDaveRunner.class)
public class BoardAndBlocksSpec extends Specification<Board> {

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

    public class AFullBoard {

        public Board create() {
            board = new Board(2, 2);
            board.dropNewBlock('b', 'g');
            board.tick(2);
            specify(board.toString(), does.equal(".g\n" +
                                                 ".b\n"));
            return board;
        }

        public void gameEndsWhenNoMoreBlocksWillFit() {
            specify(new Block() {
                public void run() throws Throwable {
                    board.dropNewBlock('r', 'y');
                }
            }, does.raise(BoardIsFullException.class));
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

        public void explodeBlocksOfTheSameColorWhenAboveThem() {
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

        public void explodeBlocksOfTheSameColorWhenBelowThem() {
            board.dropNewBlock('G', 'g');
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...g..\n" +
                                                 "...G..\n" +
                                                 "...bg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "...bg.\n"));
        }

        public void explodeBlocksOfTheSameColorWhenOnTheirLeft() {
            board.dropNewBlock('B', 'g');
            board.moveLeft();
            board.tick(3);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "..g...\n" +
                                                 "..Bbg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "..g.g.\n"));
        }

        public void explodeBlocksOfTheSameColorWhenOnTheirRight() {
            board.dropNewBlock('G', 'b');
            board.moveRight();
            board.moveRight();
            board.tick(3);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 ".....b\n" +
                                                 "...bgG\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "...b.b\n"));
        }

        public void explodeChainedBlocksOfTheSameColor() {
            board.dropNewBlock('B', 'b');
            board.rotateClockwise();
            board.moveLeft();
            board.moveLeft();
            board.tick(3);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 ".Bbbg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "....g.\n"));
        }

        public void explodeExplosiveBlocksOfTheSameColor() {
            BlockImpl b1 = new BlockImpl('G', EMPTY, 0, 1);
            BlockImpl b2 = new BlockImpl('G', EMPTY, 0, 2);
            specify(b1.canExplode(b2));
            specify(b2.canExplode(b1));
        }

        public void doNotExplodeBlocksOfADifferentColor() {
            board.dropNewBlock('G', 'b');
            board.tick(3);
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...b..\n" +
                                                 "...G..\n" +
                                                 "...bg.\n"));
        }

        public void normalBlocksDoNotExplodeByThemselves() {
            BlockImpl b1 = new BlockImpl('g', 'g', 0, 1);
            BlockImpl b2 = new BlockImpl('g', 'g', 0, 2);
            specify(!b1.canExplode(b2));
            specify(!b2.canExplode(b1));
        }

        public void sequentialComboExplosionsArePossible() {
            board.dropNewBlock('B', 'G');
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...G..\n" +
                                                 "...B..\n" +
                                                 "...bg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }

        public void simultaneousComboExplosionsArePossible() {
            board.dropNewBlock('B', 'G');
            board.rotateClockwise();
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "...BG.\n" +
                                                 "...bg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "......\n"));
        }
    }

    public class DiamondBlocks {

        public Board create() {
            board = new Board(4, 6);
            board.dropNewBlock('b', 'g');
            board.rotateClockwise();
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "...bg.\n"));
            board.dropNewBlock('b', 'g');
            board.rotateClockwise();
            board.moveLeft();
            board.moveLeft();
            board.tick(4);
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 ".bgbg.\n"));
            return board;
        }

        public void explodeAllBlocksOfAColorWhenAboveOneOfThem() {
            board.dropNewBlock('*', 'r');
            board.tick(2);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "...r..\n" +
                                                 "...*..\n" +
                                                 ".bgbg.\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 "..grg.\n"));
        }
        
        public void explodeOnlyThemselvesWhenAboveNothing() {
            board.dropNewBlock('*', 'r');
            board.moveRight();
            board.moveRight();
            board.tick(3);
            specify(should.be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 ".....r\n" +
                                                 ".bgbg*\n"));
            board.tick();
            specify(should.not().be.falling());
            specify(board.toString(), does.equal("......\n" +
                                                 "......\n" +
                                                 "......\n" +
                                                 ".bgbgr\n"));
        }
    }
}

// TODO: game ending (event?)
// TODO: ticker (reset tick timer on manual move down)
// TODO: GUI
// TODO: counting points (event?)
// TODO: counter gems (start from 5, need to drop 5 gems before turns into a normal gem)
// TODO: counter gems explode if near to a crash gem
// TODO: sending counter gem groups to the opponent
// TODO: grouping gems to rectangles
// TODO: multiplayer mode

// normal gem
// crash gem = explosive block
// rainbow gem = diamond block
// counter gem

// Videos:
// + http://www.youtube.com/watch?v=1-t5MYDIlwI
// http://www.youtube.com/watch?v=YsuaonMxbD0
// http://www.youtube.com/watch?v=8cS6e8Ar-78
// http://www.youtube.com/watch?v=0u9cPbF5TDk
