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

    private BlockPair(BlockPair other) {
        this.shape = other.shape.clone();
        this.centerRow = other.centerRow;
        this.centerCol = other.centerCol;
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
        BlockPair test = new BlockPair(this);
        test.moveDown();
        return !test.collidesWith(board);
    }

    public boolean canMoveLeft(Board board) {
        BlockPair test = new BlockPair(this);
        test.moveLeft();
        return !test.collidesWith(board);
    }

    public boolean canMoveRight(Board board) {
        BlockPair test = new BlockPair(this);
        test.moveRight();
        return !test.collidesWith(board);
    }

    public boolean canRotateClockwise(Board board) {
        BlockPair test = new BlockPair(this);
        test.rotateClockwise();
        return !test.collidesWith(board);
    }

    public boolean canRotateCounterClockwise(Board board) {
        BlockPair test = new BlockPair(this);
        test.rotateCounterClockwise();
        return !test.collidesWith(board);
    }

    public void moveDown() {
        centerRow++;
    }

    public void moveLeft() {
        centerCol--;
    }

    public void moveRight() {
        centerCol++;
    }

    public void rotateClockwise() {
        shape = rotateClockwise(shape);
    }

    public void rotateCounterClockwise() {
        shape = rotateCounterClockwise(shape);
    }

    private static char[][] rotateClockwise(char[][] shape) {
        char[][] rotated = new char[3][3];
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                rotated[col][2 - row] = shape[row][col];
            }
        }
        return rotated;
    }

    private static char[][] rotateCounterClockwise(char[][] shape) {
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
                    if (outside(board, row, col) || collidesWith(board, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean outside(Board board, int shapeRow, int shapeCol) {
        int row = toBoardRow(shapeRow);
        int col = toBoardCol(shapeCol);
        return row >= board.rows() || col < 0 || col >= board.columns();
    }

    private boolean collidesWith(Board board, int shapeRow, int shapeCol) {
        int row = toBoardRow(shapeRow);
        int col = toBoardCol(shapeCol);
        return row >= 0 && board.blockAt(row, col) != Board.EMPTY;
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
