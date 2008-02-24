package net.orfjackal.puzzlewarrior;

import static net.orfjackal.puzzlewarrior.Board.EMPTY;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public class Block {

    // row and col are relative to the center of shape
    private char[][] shape;
    private int centerRow;
    private int centerCol;

    public Block(char piece1, char piece2, int row, int col) {
        this(new char[][]{
                {0, piece2, 0},
                {0, piece1, 0},
                {0, 0, 0},
        }, row, col);
    }

    private Block(Block other) {
        this(other.shape, other.centerRow, other.centerCol);
    }

    private Block(char[][] shape, int centerRow, int centerCol) {
        this.shape = deepCopy(shape);
        this.centerRow = centerRow;
        this.centerCol = centerCol;
    }

    public boolean hasPieceAt(int boardRow, int boardCol) {
        return Math.abs(this.centerRow - boardRow) <= 1
                && Math.abs(this.centerCol - boardCol) <= 1
                && pieceAt(boardRow, boardCol) != EMPTY;
    }

    public char pieceAt(int boardRow, int boardCol) {
        int shapeRow = toShapeRow(boardRow);
        int shapeCol = toShapeCol(boardCol);
        return shape[shapeRow][shapeCol];
    }

    public void copyTo(char[][] board) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != EMPTY) {
                    board[toBoardRow(row)][toBoardCol(col)] = shape[row][col];
                }
            }
        }
    }

    public boolean canMoveDown(Board board) {
        Block test = new Block(this);
        test.moveDown();
        return !test.collidesWith(board);
    }

    public boolean canMoveLeft(Board board) {
        Block test = new Block(this);
        test.moveLeft();
        return !test.collidesWith(board);
    }

    public boolean canMoveRight(Board board) {
        Block test = new Block(this);
        test.moveRight();
        return !test.collidesWith(board);
    }

    public boolean canRotateClockwise(Board board) {
        Block test = new Block(this);
        test.rotateClockwise();
        return !test.collidesWith(board);
    }

    public boolean canRotateCounterClockwise(Board board) {
        Block test = new Block(this);
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
                if (shape[row][col] != EMPTY) {
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
        // it is allowed for the block to be partly above the board, so the board row may be negative
        return row >= 0 && board.pieceAt(row, col) != EMPTY;
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

    public Block[] breakToColumns() {
        List<Block> pieces = new ArrayList<Block>();
        for (int col = 0; col < shape[0].length; col++) {
            char[][] broken = clearColumnsOtherThan(col, shape);
            if (notEmpty(broken)) {
                pieces.add(new Block(broken, centerRow, centerCol));
            }
        }
        return pieces.toArray(new Block[pieces.size()]);
    }

    private static char[][] clearColumnsOtherThan(int colToKeep, char[][] shape) {
        char[][] cleared = deepCopy(shape);
        for (int row = 0; row < cleared.length; row++) {
            for (int col = 0; col < cleared[row].length; col++) {
                if (col != colToKeep) {
                    cleared[row][col] = EMPTY;
                }
            }
        }
        return cleared;
    }

    private static boolean notEmpty(char[][] shape) {
        for (char[] rows : shape) {
            for (char cell : rows) {
                if (cell != EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    private static char[][] deepCopy(char[][] array) {
        char[][] copy = array.clone();
        for (int i = 0; i < copy.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }
}
