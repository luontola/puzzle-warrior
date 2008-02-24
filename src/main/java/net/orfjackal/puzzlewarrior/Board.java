package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
public class Board {

    public static final char EMPTY = '\0';

    private char[][] board;
    private Block falling;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
    }

    public boolean falling() {
        return falling != null;
    }

    public int rows() {
        return board.length;
    }

    public int columns() {
        return board[0].length;
    }

    public char pieceAt(int row, int col) {
        return board[row][col];
    }

    public void dropNewBlock(char piece1, char piece2) {
        if (falling()) {
            throw new IllegalStateException("There is already a falling block");
        }
        int row = 0;
        int col = columns() / 2;
        falling = new Block(piece1, piece2, row, col);
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
        Block[] columns = falling.breakToColumns();
        for (Block b : columns) {
            while (b.canMoveDown(this)) {
                b.moveDown();
            }
            b.copyTo(board);
        }
        falling = null;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                char cell = board[row][col];
                if (falling != null && falling.hasPieceAt(row, col)) {
                    cell = falling.pieceAt(row, col);
                } else if (cell == EMPTY) {
                    cell = '.';
                }
                sb.append(cell);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
