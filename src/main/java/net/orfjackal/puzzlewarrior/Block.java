package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public interface Block {

    boolean hasPieceAt(int boardRow, int boardCol);

    char pieceAt(int boardRow, int boardCol);

    void copyTo(char[][] board);

    boolean canMoveDown(Board board);

    void moveDown();
}
