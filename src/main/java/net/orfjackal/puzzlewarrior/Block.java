package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public interface Block {

    char EMPTY = '\0';

    int centerRow();

    int centerCol();

    boolean hasPieceAt(int boardRow, int boardCol);

    char pieceAt(int boardRow, int boardCol);

    boolean canMoveDown(Board board);

    void moveDown();
}
