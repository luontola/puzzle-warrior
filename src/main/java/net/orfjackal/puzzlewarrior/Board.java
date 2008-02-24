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
        if (stopped.removeAll(explodingBlocks())) {
            pack();
        }
    }

    private List<Block> explodingBlocks() {
        List<Block> exploding = new ArrayList<Block>();
        for (Block block : stopped) {
            if (block.isDiamond()) {
                exploding.addAll(explodableByDiamond(block));
            }
            if (block.isExplosive()) {
                exploding.addAll(explodableNextTo(block));
            }
        }
        addChainedBlocksTo(exploding);
        return exploding;
    }

    private List<Block> explodableByDiamond(Block diamond) {
        assert diamond.isDiamond();
        List<Block> explode = new ArrayList<Block>();
        explode.add(diamond);
        explode.addAll(targettedByDiamond(diamond));
        return explode;
    }

    private List<Block> targettedByDiamond(Block diamond) {
        List<Block> targets = new ArrayList<Block>();
        Block targetType = blockBelow(diamond);
        if (targetType != null) {
            targets.addAll(blocksOfType(targetType));
        }
        return targets;
    }

    private Block blockBelow(Block other) {
        Block below = null;
        for (Block b : stopped) {
            if (other.touches(b) && other.centerRow() < b.centerRow()) {
                assert below == null;
                below = b;
            }
        }
        return below;
    }

    private List<Block> blocksOfType(Block type) {
        List<Block> list = new ArrayList<Block>();
        for (Block b : stopped) {
            if (b.sameTypeAs(type)) {
                list.add(b);
            }
        }
        return list;
    }

    private List<Block> explodableNextTo(Block explosive) {
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
