/** @source code inspired by BullyWiiPlaza on Stack Overflow
 * stackoverflow.com/questions/11607270/how-to-check-whether-given-string-is-a-word
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;

public class WordChecker {
    private HashSet<String> wordSet;

    public WordChecker() throws IOException {
        wordSet = new HashSet<>();
        Path file = Paths.get("google-10000-english.txt");
        byte[] readFile = Files.readAllBytes(file);
        String wordString = new String(readFile);
        String[] words = wordString.split("\n");
        Collections.addAll(wordSet, words);
    }

    public boolean contains(String word)
    {
        return wordSet.contains(word);
    }
}