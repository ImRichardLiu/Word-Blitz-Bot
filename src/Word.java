public class Word {
    public StringBuilder word;
    public int locX;
    public int locY;
    public Boolean[][] boolBoard;

    public Word(StringBuilder input, int x, int y, Boolean[][] usedBoard) {
        word = input;
        locX = x;
        locY = y;
        boolBoard = usedBoard;
        boolBoard[x][y] = true;
    }
    /*
    public Word(StringBuilder input, int x, int y) {
        word = input;
        locX = x;
        locY = y;
    } */

    public int getX() {
        return locX;
    }

    public int getY() {
        return locY;
    }

    public String getWord() {
        return word.toString();
    }

    public StringBuilder getBuilder() {
        return word;
    }

    public Boolean[][] getBoardCopy() {
        Boolean[][] boardClone = new Boolean[4][];
        for (int i = 0; i < 4; i++) {
            boardClone[i] = boolBoard[i].clone();
        }
        return boardClone;
    }

    public boolean unused(int currX, int currY) {
        return boolBoard[currX][currY] == false;
    }
}
