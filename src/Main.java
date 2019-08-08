import java.io.IOException;

public class Main {
    public static final int minLength = 3;
    public static final String[][] input = {{"s", "n", "a", "b"},
                                            {"d", "e", "o", "o"},
                                            {"a", "e", "n", "s"},
                                            {"r", "r", "r", "a"}};

    public static void main(String[] args) throws IOException {
        Engine engine = new Engine(minLength); // launches the program
    }
}
