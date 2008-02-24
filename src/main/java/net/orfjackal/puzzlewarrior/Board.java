package net.orfjackal.puzzlewarrior;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
public class Board {

    private final int rows;
    private final int columns;
    private final SortedSet<Block> stopped = new TreeSet<Block>();
    private FallingBlock falling;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public boolean falling() {
        return falling != null;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public char pieceAt(int row, int col) {
        for (Block b : stopped) {
            if (b.hasPieceAt(row, col)) {
                return b.pieceAt(row, col);
            }
        }
        return Block.EMPTY;
    }

    public void dropNewBlock(char piece1, char piece2) {
        if (falling()) {
            throw new IllegalStateException("There is already a falling block");
        }
        int row = 0;
        int col = columns() / 2;
        falling = new BlockImpl(piece1, piece2, row, col);
    }

    public void tick(int count) {
        for (int i = 0; i < count; i++) {
            tick();
        }
    }

    public void tick() {
        if (falling()) {
            if (falling.canMoveDown(this)) {
                falling.moveDown();
            } else {
                stopFalling();
            }
        }
    }

    private void stopFalling() {
        stopped.addAll(falling.breakToPieces());
        falling = null;
        pack();
    }

    private void pack() {
        for (Block b : stopped) {
            while (b.canMoveDown(this)) {
                b.moveDown();
            }
        }
        if (stopped.removeAll(findExploding())) {
            pack();
        }
    }

    private List<Block> findExploding() {
        List<Block> exploding = new ArrayList<Block>();
        for (Block block : stopped) {
            if (block.isExplosive()) {
                exploding.addAll(findExplodableNextTo(block));
            }
        }
        addChainedBlocksTo(exploding);
        return exploding;
    }

    private List<Block> findExplodableNextTo(Block explosive) {
        List<Block> chained = new ArrayList<Block>();
        for (Block b : stopped) {
            if (explosive.canExplode(b)) {
                chained.add(b);
            }
        }
        if (chained.size() > 0) {
            chained.add(explosive);
        }
        return chained;
    }

    private void addChainedBlocksTo(List<Block> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            List<Block> chained = findSameColoredNextTo(blocks.get(i));
            chained.removeAll(blocks);
            blocks.addAll(chained);
        }
    }

    private List<Block> findSameColoredNextTo(Block block) {
        List<Block> chained = new ArrayList<Block>();
        for (Block b : stopped) {
            if (block.touches(b) && block.sameTypeAs(b)) {
                chained.add(b);
            }
        }
        return chained;
    }

    public void moveLeft() {
        if (falling.canMoveLeft(this)) {
            falling.moveLeft();
        }
    }

    public void moveRight() {
        if (falling.canMoveRight(this)) {
            falling.moveRight();
        }
    }

    public void rotateClockwise() {
        if (falling.canRotateClockwise(this)) {
            falling.rotateClockwise();
        }
    }

    public void rotateCounterClockwise() {
        if (falling.canRotateCounterClockwise(this)) {
            falling.rotateCounterClockwise();
        }
    }

    public void flip() {
        falling.flip();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                char piece = pieceAt(row, col);
                if (falling != null && falling.hasPieceAt(row, col)) {
                    piece = falling.pieceAt(row, col);
                } else if (piece == Block.EMPTY) {
                    piece = '.';
                }
                sb.append(piece);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
