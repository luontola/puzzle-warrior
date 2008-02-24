package net.orfjackal.puzzlewarrior;

import java.util.List;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public interface FallingBlock extends Block {

    boolean collidesWith(Board board);

    boolean canMoveLeft(Board board);

    boolean canMoveRight(Board board);

    boolean canRotateClockwise(Board board);

    boolean canRotateCounterClockwise(Board board);

    void moveLeft();

    void moveRight();

    void rotateClockwise();

    void rotateCounterClockwise();

    void flip();

    List<Block> breakToPieces();
}
