package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
public class Board {

    private static final char EMPTY = '\0';

    private char[][] board;
    private char[][] falling;
    private int fallingRow;
    private int fallingCol;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
    }

    public void addBlock(char piece1, char piece2) {
        if (falling != null) {
            throw new IllegalStateException("There is already a falling block");
        }
        falling = new char[][]{
                {0, piece2, 0},
                {0, piece1, 0},
                {0, 0, 0},
        };
        fallingRow = 0;
        fallingCol = board[0].length / 2;
    }

    public void tick(int count) {
        for (int i = 0; i < count; i++) {
            tick();
        }
    }

    public void tick() {
        if (fallingBlockIsOnLastRow() || fallingBlockWouldCollide()) {
            stopFalling();
        }
        fallingRow++;
    }

    private boolean fallingBlockIsOnLastRow() {
        return fallingRow == board.length - 1;
    }

    private boolean fallingBlockWouldCollide() {
        return board[fallingRow + 1][fallingCol] != EMPTY;
    }

    public boolean falling() {
        return falling != null;
    }

    private void stopFalling() {
        board[fallingRow - 1][fallingCol] = falling[0][1];
        board[fallingRow][fallingCol] = falling[1][1];
        falling = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                char cell = board[row][col];
                if (containsFallingBlock(row, col)) {
                    cell = fallingBlockFrom(row);
                } else if (cell == EMPTY) {
                    cell = '.';
                }
                sb.append(cell);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private boolean containsFallingBlock(int row, int col) {
        return falling != null
                && col == fallingCol
                && (row == fallingRow || row == fallingRow - 1);
    }

    private char fallingBlockFrom(int row) {
        int i = 1 - fallingRow + row;
        return falling[i][1];
    }

    public void rotateRight() {
    }
}
