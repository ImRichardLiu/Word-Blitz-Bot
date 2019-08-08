public class Board {
    public Matrix charBoard;

    public Board(Matrix input) {
        charBoard = input;
    }


    public String get(int i, int j) {
        return charBoard.getChar(i, j);
    }
}
