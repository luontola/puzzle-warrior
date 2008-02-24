package net.orfjackal.puzzlewarrior;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public class BlockPair {

    // row and col are relative to the center of shape
    private char[][] shape;
    private int centerRow;
    private int centerCol;

    public BlockPair(char piece1, char piece2, int row, int col) {
        this.shape = new char[][]{
                {0, piece2, 0},
                {0, piece1, 0},
                {0, 0, 0},
        };
        this.centerRow = row;
        this.centerCol = col;
    }

    private BlockPair(char[][] shape, int centerRow, int centerCol) {
        this.shape = shape.clone();
        this.centerRow = centerRow;
        this.centerCol = centerCol;
    }

    public boolean hasBlockAt(int boardRow, int boardCol) {
        return Math.abs(this.centerRow - boardRow) <= 1
                && Math.abs(this.centerCol - boardCol) <= 1
                && blockAt(boardRow, boardCol) != Board.EMPTY;
    }

    public char blockAt(int boardRow, int boardCol) {
        int shapeRow = toShapeRow(boardRow);
        int shapeCol = toShapeCol(boardCol);
        return shape[shapeRow][shapeCol];
    }

    public void copyTo(char[][] board) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != Board.EMPTY) {
                    board[toBoardRow(row)][toBoardCol(col)] = shape[row][col];
                }
            }
        }
    }

    public boolean canMoveDown(Board board) {
        BlockPair test = new BlockPair(shape, centerRow + 1, centerCol);
        return !test.collidesWith(board);
    }

    public void moveDown() {
        centerRow++;
    }

    public void rotateRight() {
        shape = rotateRight(shape);
    }

    public void rotateLeft() {
        shape = rotateLeft(shape);
    }

    private static char[][] rotateRight(char[][] shape) {
        char[][] rotated = new char[3][3];
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                rotated[col][2 - row] = shape[row][col];
            }
        }
        return rotated;
    }

    private char[][] rotateLeft(char[][] shape) {
        char[][] rotated = new char[3][3];
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                rotated[2 - col][row] = shape[row][col];
            }
        }
        return rotated;
    }

    private boolean collidesWith(Board board) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != Board.EMPTY) {
                    if (outsideBoard(board, row) || collidesWith(board, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean collidesWith(Board board, int shapeRow, int chapeCol) {
        int row = toBoardRow(shapeRow);
        int col = toBoardCol(chapeCol);
        return board.blockAt(row, col) != Board.EMPTY;
    }

    private boolean outsideBoard(Board board, int row) {
        return toBoardRow(row) >= board.rows();
    }

    private int toShapeCol(int boardCol) {
        return 1 - this.centerCol + boardCol;
    }

    private int toShapeRow(int boardRow) {
        return 1 - this.centerRow + boardRow;
    }

    private int toBoardCol(int shapeCol) {
        return this.centerCol + shapeCol - 1;
    }

    private int toBoardRow(int shapeRow) {
        return this.centerRow + shapeRow - 1;
    }
}
