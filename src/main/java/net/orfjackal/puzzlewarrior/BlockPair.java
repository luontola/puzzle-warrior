package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public class BlockPair {

    // row and col are relative to the center of shape
    private final char[][] shape;
    private int row;
    private int col;

    public BlockPair(char piece1, char piece2, int row, int col) {
        this.shape = new char[][]{
                {0, piece2, 0},
                {0, piece1, 0},
                {0, 0, 0},
        };
        this.row = row;
        this.col = col;
    }

    public void copyTo(char[][] board) {
        board[row - 1][col] = shape[0][1];
        board[row][col] = shape[1][1];
    }

    public char blockAt(int row, int col) {
        int shapeRow = 1 - this.row + row;
        int shapeCol = 1 - this.col + col;
        return shape[shapeRow][shapeCol];
    }

    public boolean containsBlockAt(int row, int col) {
        return Math.abs(this.row - row) <= 1
                && Math.abs(this.col - col) <= 1
                && blockAt(row, col) != Board.EMPTY;
    }

    public boolean canMoveDown(Board board) {
        return !isOnLastRow(board) && !wouldCollide(board);
    }

    private boolean isOnLastRow(Board board) {
        return row == board.rows() - 1;
    }

    private boolean wouldCollide(Board board) {
        return board.blockAt(row + 1, col) != Board.EMPTY;
    }

    public void moveDown() {
        row++;
    }
}
