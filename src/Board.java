public class Board {
    public String[][] charBoard;

    public Board(String[][] input) {
        charBoard = input;
    }


    public String get(int i, int j) {
        return charBoard[i][j];
    }
}
