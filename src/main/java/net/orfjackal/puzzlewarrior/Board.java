package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 23.2.2008
 */
public class Board {

    private char[][] board;
    private char[] falling;
    private int fallingRow;
    private int fallingCol;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                char cell = board[row][col];
                if (isFallingBlock(row, col)) {
                    cell = getFallingBlock(row);
                } else if (cell == '\0') {
                    cell = '.';
                }
                sb.append(cell);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private boolean isFallingBlock(int row, int col) {
        return falling != null
                && col == fallingCol
                && (row == fallingRow || row == fallingRow - 1);
    }

    private char getFallingBlock(int row) {
        return falling[fallingRow - row];
    }

    public void addBlock(char type1, char type2) {
        falling = new char[]{type1, type2};
        fallingCol = board[0].length / 2;
    }

    public void tick() {
        fallingRow++;
        if (fallingRow == board.length - 1) {
            stopFalling();
        }
    }

    public void tick(int count) {
        for (int i = 0; i < count; i++) {
            tick();
        }
    }

    private void stopFalling() {
        board[fallingRow][fallingCol] = falling[0];
        board[fallingRow - 1][fallingCol] = falling[1];
        falling = null;
    }
}
