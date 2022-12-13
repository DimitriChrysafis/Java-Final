

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;


public class Wordle {


    private WordleGWindow gw;

    public String chooseWord() {

        int size = WordleDictionary.FIVE_LETTER_WORDS.length;
        int randomIndex = new Random().nextInt(size);
        String returnValue = WordleDictionary.FIVE_LETTER_WORDS[randomIndex];
        char x = returnValue.charAt(4);
        if (x == 'S' || x == 's') {
            double randomNum = Math.random();
            if (randomNum > 0.33) {
                return chooseWord();
            }
        }
        System.out.println(returnValue);
        return returnValue;
    }

    private String word;
    WordleCanvas myCanvas;
    static boolean flushColorsToCelebrate;
    static int lastUsed;

    public void celebrate() {
        flushColorsToCelebrate = true;
        lastUsed = 50;
    }


    public void run() {
        myCanvas = new WordleCanvas() {
            public void paintComponent(Graphics g) {
                if (flushColorsToCelebrate) {
                    Color newColor = new Color(new Random().nextInt(256 * 256 * 256));
                    // System.out.println(lastUsed);
                    if (lastUsed-- == 0) {
                        newColor = WordleGWindow.CORRECT_COLOR;
                        flushColorsToCelebrate = false;
                    }
                    for (int i = 0; i < 5; i++) {
                        this.setSquareColor(getCurrentRow() - 1, i, newColor);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                super.paintComponent(g);
            }
        };
        word = chooseWord();
        gw = new WordleGWindow(myCanvas);

        gw.addEnterListener((s) -> enterAction(s));
    }

    /**
     * @param guessToClue a Map of guesses (so far) with their associated clues
     * @param dictionary the dictionary used for this Wordle
     * @return a List of all the possible words (given the guesses and clues so far)
     *
     * example:
     * guessToClue = {OMENS=**e**, HELMS=*el**, GRASP=G****, LYART=l****}
     * returns the following list of possible words: [glide, glued, guile]
     *
     */
    public static List<String> listOfPossibleWords(Map<String, String> guessToClue, String[] dictionary) {
        List<String> returnValue = new ArrayList<>();
            for(int i = 0; i < dictionary.length; i++){
                String current = dictionary[i];
                boolean allMatch = true;
                for(Map.Entry<String, String> element : guessToClue.entrySet()) {
                    String key = element.getKey();
                    String value = element.getValue();
                   String hint = getHint(key, current);
                   if (!hint.equals(value)){
                       allMatch = false;
                       break;
                   }
                }
                if(allMatch){
                   returnValue.add(current);
                }
                // System.out.println(current);
            }
        return returnValue;
    }

    public void enterAction(String guess) {
        if (guess.length() < 5) {
            String betterGuess = guess.toLowerCase();

            int size = WordleDictionary.FIVE_LETTER_WORDS.length;
            for (int i = 0; i < size; i++) {
                if (WordleDictionary.FIVE_LETTER_WORDS[i].startsWith(betterGuess)) {
                    System.out.println(WordleDictionary.FIVE_LETTER_WORDS[i]);
                }

            }
            return;
        }
        String result = getHint(guess, word);
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            char temp = result.charAt(i);
            char lowerTemp = Character.toLowerCase(temp);
            char key = Character.toUpperCase(temp);
            if (temp == '*') {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.MISSING_COLOR);
                if (gw.getKeyColor("" + key) != WordleGWindow.CORRECT_COLOR) {
                    if (gw.getKeyColor("" + key) != WordleGWindow.PRESENT_COLOR) {
                        gw.setKeyColor(new String("" + temp), WordleGWindow.MISSING_COLOR);
                    }
                }
            } else if (temp == lowerTemp) {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.PRESENT_COLOR);
                if (gw.getKeyColor("" + key) != WordleGWindow.CORRECT_COLOR) {
                    gw.setKeyColor(new String("" + key), WordleGWindow.PRESENT_COLOR);
                }
            } else {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.CORRECT_COLOR);
                counter++;
                gw.setKeyColor(new String("" + key), WordleGWindow.CORRECT_COLOR);
            }
        }
        if (counter == 5) {
            celebrate();
        }
        gw.setCurrentRow(gw.getCurrentRow() + 1);
        for (String a : WordleDictionary.FIVE_LETTER_WORDS) {
            if (a.equals(guess.toLowerCase())) {
                return;
            }
        }
        gw.showMessage("Word Doesn't Exist ");
    }

    static public String getHint(String guess1, String word1) {
        char[] word = word1.toUpperCase().toCharArray();
        char[] guess = guess1.toUpperCase().toCharArray();
        char[] x = new char[]{
                '*', '*', '*', '*', '*'
        };
        for (int i = 0; i < 5; i++) {
            char input = guess[i];
            if (input == word[i]) {
                x[i] = word[i];
                guess[i] = '*';
                word[i] = '*';

            }
        }
        for (int i = 0; i < 5; i++) {
            if (guess[i] == '*') {
                continue;
            }
            for (int j = 0; j < 5; j++) {
                if (guess[i] == word[j]) {
                    x[i] = Character.toLowerCase(guess[i]);
                    guess[i] = '*';
                    word[j] = '*';
                    break;
                }
            }
        }
        return new String(x);

    }

    public static void main(String[] args) {
        new Wordle().run();
    }


}
