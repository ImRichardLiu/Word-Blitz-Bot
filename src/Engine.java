import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Engine {
    public Board board;
    public HashMap<Integer, String> wordString; // stores words
    public HashMap<Integer, Word> words;
    public Queue<Word> combinations; // stores all possible combinations of char sequences
    private boolean gameGoing = false;
    private boolean start = true;
    private String input;
    private int minLength;
    private boolean found = false;
    private int key = 0;
    private Robot cursor;
    private final int limit = 70;


    /**
     * Engine constructor is the main loop of the bot.
     */
    public Engine(int minLength) throws IOException, AWTException, InterruptedException {
        cursor = new Robot();
        words = new HashMap<>();
        wordString = new HashMap<>();
        combinations = new LinkedList<>();
        this.minLength = minLength;
        WordChecker check = new WordChecker();
        gameLoop(check);
        /* System.out.println("Words that exist are:");
        for (String s: words) {
            System.out.println(s);
        } */
    }
    public void gameLoop(WordChecker check) throws InterruptedException{
        while (true) {
            if (!gameGoing) {
                if (start) {
                    initialize();
                }
                enterString();
                gameGoing = true;
            }
            while (gameGoing) {
                StdDraw.clear(Color.BLACK);
                if (!found) {
                    StdDraw.setPenColor(Color.WHITE);
                    Font font = new Font("Calibri Light", Font.PLAIN, 16);
                    StdDraw.setFont(font);
                    StdDraw.text(2.5, 2.5, "Finding words...");
                    StdDraw.show();
                    findWords(check);
                    displayWords();
                    found = true;
                }
                if (StdDraw.isKeyPressed(32)) {
                    gameGoing = false;
                    input = "";
                    found = false;
                    words = new HashMap<>();
                    combinations = new LinkedList<>();
                    wordString = new HashMap<>();
                    key = 0;
                    start = true;
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
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character letter = StdDraw.nextKeyTyped();
                if (start == true) {
                    this.input = String.valueOf(letter);
                    start = false;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(2.5, 3.25, "Enter letters: ");
                    StdDraw.text(2.5, 2, this.input);
                    StdDraw.show();
                } else if (StdDraw.isKeyPressed(8)) {
                    input = input.substring(0, input.length() - 1);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(2.5, 3.25, "Enter letters: ");
                    StdDraw.text(2.5, 2, this.input);
                    StdDraw.show();
                }
                else {
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

    private void findWords(WordChecker check) {
        Matrix matrix = new Matrix(this.input);
        board = new Board(matrix);
        addSingleLetters();
        int length = 1;
        while(length < 16) {
            calculate(length, check, length + 1 >= minLength);
            length++;
        }
        System.out.println("Max length of " + length + " has been reached.");
        System.out.println(words.size());
    }

    private void displayWords() throws InterruptedException{
        StdDraw.clear(Color.BLACK);
        int num = Math.min(words.size(), limit);
        if (num == 0) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text(2.5, 2.5, "no words found");
            StdDraw.show();
        }
        int cols = Math.floorDiv(num, 10);
        if (num % 10 > 0) {
            cols += 1;
        }
        int curr = 0;
        int count = 0;
        while (count < num) {
            if (StdDraw.hasNextKeyTyped()) {
                break;
            }
            Word word = null;
            for (int i = key; i >= 0; i--) {
                if (words.containsKey(i)) {
                    word = words.remove(i);
                    break;
                }
            }
            double x = 0.4 + (4.9 / cols) * curr;
            double y  = 4.85 - (count % 10) * 0.5;
            Font font = new Font("Calibri Light", Font.PLAIN, 16);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(x, y, word.getWord());
            font = new Font("Calibri Light", Font.PLAIN, 10);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.text(x + 0.05, y - 0.25, word.path());
            StdDraw.show();
            tracePath(word);
            count += 1;
            if (count != 0 && count % 10 == 0) {
                curr += 1;
            }
        }

        System.out.println(count);
    }

    private void tracePath(Word word) throws InterruptedException{
        int x;
        int y;
        for (int i = 0; i < word.getPath().size(); i++) {
            int tempY = Math.floorDiv(word.getPath().get(i), 4) ;
            int tempX = word.getPath().get(i) % 4;
            System.out.print("(" + tempX + "," + tempY + ") ");
            x = 465 + tempX * 110;
            y = 475 + tempY * 110;
            if (i == 0) {
                cursor.mouseMove(x, y);
                cursor.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            } else {
                cursor.mouseMove(x, y);
            }
            Thread.sleep(1000);
        }
        cursor.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
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
                LinkedList<Integer> path = new LinkedList<>();
                Word input = new Word(str, i, j, usedBoard, path);
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
        LinkedList<Integer> path = curr.getPath();
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
                    Word w = new Word(newBuilder, x, y, curr.getBoardCopy(), path);
                    // Word w = new Word(newBuilder, x, y);
                    String str = w.getWord();
                    if (str.length() != 16) {
                        combinations.add(w);
                    }
                    if (check.contains(w.getWord()) && minLen && !wordString.containsValue(str)) {
                        wordString.put(key, str);
                        words.put(key, w);
                        key++;
                    }
                }
            }
        }
    }

    public boolean verifyLoc(int x, int y) {
        return (x >= 0 && x < 4) && (y >= 0 && y < 4);
    }
}
