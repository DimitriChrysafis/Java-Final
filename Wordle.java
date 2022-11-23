

import java.util.Random;

public class Wordle {


    private WordleGWindow gw;

    public String chooseWord() {
        int size = WordleDictionary.FIVE_LETTER_WORDS.length;
        int randomIndex = new Random().nextInt(size);
        System.out.println(WordleDictionary.FIVE_LETTER_WORDS[randomIndex]);
        return WordleDictionary.FIVE_LETTER_WORDS[randomIndex];
    }

    private String word;


    public void run() {
        // word = "PENIS";
        word = chooseWord(); // choose random word
        gw = new WordleGWindow();
        /*        for (int i = 0; i < 5; i++) {
            gw.setSquareLetter(0, i, word.substring(i, i + 1));
        }

        gw.setCurrentRow(gw.getCurrentRow()+1);
    */
        gw.addEnterListener((s) -> enterAction(s));
    }

    /*
     * Called when the user hits the RETURN key or clicks the ENTER button,
     * passing in the string of characters on the current row.
     */

    public void enterAction(String guess) {
        String result = getHint(guess, word);
        for(int i = 0; i < 5; i++){
            char temp = result.charAt(i);
            char lowerTemp = Character.toLowerCase(temp);
            char key = Character.toUpperCase(temp);
            if(temp == '*'){
                gw.setSquareColor(gw.getCurrentRow(),i, WordleGWindow.MISSING_COLOR);
                if(gw.getKeyColor("" + key) != WordleGWindow.CORRECT_COLOR) {
                    if(gw.getKeyColor("" + key) != WordleGWindow.PRESENT_COLOR) {
                        gw.setKeyColor(new String("" + temp), WordleGWindow.MISSING_COLOR);
                    }
                }
            }else if(temp == lowerTemp){
                gw.setSquareColor(gw.getCurrentRow(),i, WordleGWindow.PRESENT_COLOR);
                if(gw.getKeyColor("" + key) != WordleGWindow.CORRECT_COLOR) {
                    gw.setKeyColor(new String("" +key),WordleGWindow.PRESENT_COLOR);
                }
            }else{
                gw.setSquareColor(gw.getCurrentRow(),i, WordleGWindow.CORRECT_COLOR);

                gw.setKeyColor(new String("" +key),WordleGWindow.CORRECT_COLOR);
            }
        }
        gw.setCurrentRow(gw.getCurrentRow()+1);
        for (String a : WordleDictionary.FIVE_LETTER_WORDS) {
            if (a.equals(guess.toLowerCase())) {

                return;
            }
        }
        gw.showMessage("Word Doesn't Exist ");
    }

    public String getHint(String guess1, String word1) {
        char[] word = word1.toUpperCase().toCharArray();
        char[] guess = guess1.toUpperCase().toCharArray();
        char[] x = new char[] {
            '*', '*', '*', '*', '*'
        } ;
        for (int i = 0; i < 5; i++) {
            char input = guess[i];
            if(input == word[i]) {
                x[i] = word[i];
                guess[i] = '*';
                word[i] = '*';

            }
        }
        for(int i = 0; i < 5; i++){
            if(guess[i] == '*'){
                continue;
            }
            for(int j = 0; j < 5; j++){
                if(guess[i] == word[j]){
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
