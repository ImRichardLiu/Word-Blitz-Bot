import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Engine {
    public Board board;
    public Set<String> words; // stores words
    public Queue<Word> combinations; // stores all possible combinations of char sequences
    private boolean gameGoing = false;
    private boolean start = true;
    private String input;
    private int minLength;
    private boolean found = false;


    /**
     * Engine constructor is the main loop of the bot.
     */
    public Engine(int minLength) throws IOException {
        words = new HashSet<>();
        combinations = new LinkedList<>();
        this.minLength = minLength;
        gameLoop();
        /* System.out.println("Words that exist are:");
        for (String s: words) {
            System.out.println(s);
        } */
    }
    public void gameLoop() throws IOException {
        while (true) {
            if (!gameGoing) {
                initialize();
                enterString();
                gameGoing = true;
            }
            if (gameGoing) {
                StdDraw.clear(Color.BLACK);
                if (!found) {
                    StdDraw.text(2.5, 2.5, "Finding words...");
                    StdDraw.show();
                    findWords();
                    displayWords();
                    found = true;
                }
                if (StdDraw.hasNextKeyTyped()) {
                    gameGoing = false;
                }
            }
        }
    }

    private void initialize() {
        StdDraw.setCanvasSize(512, 512);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Calibri Light", Font.PLAIN, 16);
        StdDraw.setXscale(0, 5);
        StdDraw.setYscale(0, 5);
        StdDraw.setFont(font);
    }

    private void enterString() {
        StdDraw.text(2.5, 3.25, "Enter letters: ");
        StdDraw.enableDoubleBuffering();
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character letter = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (start == true) {
                    this.input = String.valueOf(letter);
                    start = false;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(2.5, 3.25, "Enter letters: ");
                    StdDraw.text(2.5, 2, this.input);
                    StdDraw.show();
                } else {
                    this.input += String.valueOf(letter);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(2.5, 3.25, "Enter letters: ");
                    StdDraw.text(2.5, 2, this.input);
                    StdDraw.show();
                    if (this.input.length() == 16) {
                        break;
                    }
                }
            }
        }
    }

    private void findWords() throws IOException{
        Matrix matrix = new Matrix(this.input);
        board = new Board(matrix);
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
    }

    private void displayWords() {
        StdDraw.clear(Color.BLACK);
        int i = 0;
        for (String word : words) {
            double x = 0.25;
            double y = 5 - (i % 10) * 0.5;
            if (i >= 10) {
                x = 2.75;
            }
            StdDraw.text(x, y, word);
            StdDraw.show();
            i++;
        }
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
        System.out.println(currWord);
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
