import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Engine {
    public Board board;
    public Set<String> words; // stores words
    public Queue<Word> combinations; // stores all possible combinations of char sequences

    /**
     * Engine constructor is the main loop of the bot.
     */
    public Engine(String[][] input, int minLength) throws IOException {
        words = new HashSet<>();
        combinations = new LinkedList<>();
        board = new Board(input);
        addSingleLetters();
        int length = 1;
        WordChecker check = new WordChecker();
        while(length < 16) {
            calculate(length, check, length + 1 >= minLength);
            length++;
        }
        System.out.println("Max length of " + length + " has been reached.");
        while (!combinations.isEmpty()) {
            System.out.println(combinations.remove().getWord());
        }
        /* System.out.println("Words that exist are:");
        for (String s: words) {
            System.out.println(s);
        } */
    }

    /**
     * Make Word objects from the initial letters and puts it into combinations queue.
     */
    public void addSingleLetters() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Boolean[][] usedBoard = {{false, false, false, false},
                                        {false, false, false, false},
                                        {false, false, false, false},
                                        {false, false, false, false}};
                StringBuilder str = new StringBuilder();
                str.append(board.get(i, j));
                Word input = new Word(str, i, j, usedBoard);
                // Word input = new Word(str, i, j);
                combinations.add(input);
            }
        }
    }

    /**
     * Iterates through combinations until all specified-length combinations
     * have been looked through.
     */
    public void calculate(int length, WordChecker check, boolean minLen) {
        Word curr = combinations.peek();
        String currWord = curr.getWord();
        while (currWord.length() == length) { // runs as long as specified length is correct
            curr = combinations.remove();
            addLetterToWords(curr, check, minLen);
            if (combinations.isEmpty()) {
                return;
            }
            curr = combinations.peek();
            currWord = curr.getWord();
        }
    }

    /**
     * Adds new character from every possible direction of current word.
     */
    public void addLetterToWords(Word curr, WordChecker check, boolean minLen) {
        for (int dir = 1; dir <= 8; dir++) {
            StringBuilder builder = curr.getBuilder();
            int x = curr.getX(), y = curr.getY();
            switch (dir) {
                case 1: // top
                    x -= 1;
                    break;
                case 2: // top right
                    x -= 1;
                    y += 1;
                    break;
                case 3: // right
                    y += 1;
                    break;
                case 4: // bot right
                    x += 1;
                    y += 1;
                    break;
                case 5: // bot
                    x += 1;
                    break;
                case 6: // bot left
                    x += 1;
                    y -= 1;
                    break;
                case 7: // left
                    y -= 1;
                    break;
                case 8: // top left
                    x -= 1;
                    y -= 1;
                    break;
                default:
                    break;
            }
            if (verifyLoc(x, y)) {
                if (curr.unused(x, y)) {
                    StringBuilder newBuilder = new StringBuilder(builder);
                    newBuilder.append(board.get(x, y));
                    Word w = new Word(newBuilder, x, y, curr.getBoardCopy());
                    // Word w = new Word(newBuilder, x, y);
                    String str = w.getWord();
                    if (str.length() != 16) {
                        combinations.add(w);
                    }
                    if (check.contains(str) && minLen && !words.contains(str)) {
                        words.add(str);
                        System.out.println(str);
                    }
                }
            }
        }
    }

    public boolean verifyLoc(int x, int y) {
        return (x >= 0 && x < 4) && (y >= 0 && y < 4);
    }

}
