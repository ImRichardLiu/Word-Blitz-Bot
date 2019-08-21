import java.util.LinkedList;

public class Word {
    public StringBuilder word;
    public int locX;
    public int locY;
    public Boolean[][] boolBoard;
    public LinkedList<Integer> path;

    public Word(StringBuilder input, int x, int y, Boolean[][] usedBoard, LinkedList<Integer> prevPath) {
        word = input;
        locX = x;
        locY = y;
        boolBoard = usedBoard;
        boolBoard[x][y] = true;
        this.path = new LinkedList<>();
        for (int i = 0; i < prevPath.size(); i++) {
            this.path.add(prevPath.get(i));
        }
        this.path.add(4*x + y);
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

    public LinkedList<Integer> getPath() {
        return this.path;
    }

    public String path() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            ret.append(path.get(i));
            if (i != path.size() - 1) {
                ret.append(",");
            }
        }
        return ret.toString();
    }
}
