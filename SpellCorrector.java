package spell;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    private Trie dictionary;
    private SortedSet<Edits> edits;
    private SortedSet<Edits> suggestions;

    public SpellCorrector(){
        this.dictionary = new Trie();
        this.edits = new TreeSet<>();
        this.suggestions = new TreeSet<>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        dictionary.addDictionary(dictionaryFileName);
        //System.out.println(dictionary.toString());
    }

    @Override
    public String suggestSimilarWord(String inputWordIn) {
        edits = new TreeSet<>();
        suggestions = new TreeSet<>();
        if(!"".equals(inputWordIn) && !(inputWordIn.length() - dictionary.getLongestWord() > 2)){
            String inputWord = inputWordIn.toLowerCase();
            if(dictionary.find(inputWord) == null) {
                runEdits(inputWord, 1);
                //printEdits();
                if(suggestions.size() == 0){
                    runMoreEdits(inputWord);
                }
                //printSuggestions();
                if(suggestions.size() > 0){
                    return suggestions.iterator().next().getWord();
                } else {
                    return null;
                }
            } else {
                return inputWord;
            }
        } else {
            return null;
        }
    }

    private void runEdits(String inputWord, int distance){
        deletion(inputWord, distance);
        alteration(inputWord, distance);
        insertion(inputWord, distance);
        transposition(inputWord, distance);
        updateEdits();
    }
    private void runMoreEdits(String inputWord){
        SortedSet<String> set = new TreeSet<>();
        for(Edits e : edits){
            set.add(e.getWord());
        }
        for(String s : set){
            runEdits(s, 2);
        }
    }

    private void updateEdits(){
        for(Edits e : edits){
            INode found = dictionary.find(e.getWord());
            if(found != null){
                int num = found.getValue();
                e.setCount(num);
                if(num > 0){
                    suggestions.add(e);
                }
            }
        }
    }

    private void printEdits(){
        for (Edits e: edits){
            System.out.println(e.toString());
        }
    }

    private void printSuggestions(){
        for (Edits e: suggestions){
            System.out.println(e.toString());
        }
    }


    public void deletion(String inputWord, int distance){
        if(inputWord.length() > 1) {
            for (int a = 0; a < inputWord.length(); a++) {
                StringBuilder sb = new StringBuilder();
                if(a == 0){
                    sb.append(inputWord.substring(1));
                } else if(a == inputWord.length() - 1){
                    sb.append(inputWord.substring(0, inputWord.length() - 1));
                } else {
                    sb.append(inputWord.substring(0, a));
                    sb.append(inputWord.substring(a + 1));
                }
                edits.add(new Edits(sb.toString(), distance));
            }
        }
    }
    public void alteration(String inputWord, int distance){
        for(int a = 0; a < inputWord.length(); a++){
            for(int b = 0;  b < 26; b++){
                StringBuilder sb = new StringBuilder();
                char insert = (char)(b + 'a');
                if(a == 0){
                    sb.append(insert);
                    sb.append(inputWord.substring(1));
                } else if(a == inputWord.length() - 1){
                    sb.append(inputWord.substring(0, inputWord.length() - 1));
                    sb.append(insert);
                } else {
                    sb.append(inputWord.substring(0, a));
                    sb.append(insert);
                    sb.append(inputWord.substring(a + 1));
                }
                edits.add(new Edits(sb.toString(), distance));
            }
        }
    }
    public void insertion(String inputWord, int distance) {
        for (int b = 0; b < 26; b++) {
            StringBuilder sb = new StringBuilder();
            char insert = (char) (b + 'a');
            sb.append(insert);
            sb.append(inputWord);
            edits.add(new Edits(sb.toString(), distance));
            for (int a = 0; a < inputWord.length(); a++) {
                sb = new StringBuilder();
                sb.append(inputWord.substring(0, a + 1));
                sb.append(insert);
                sb.append(inputWord.substring(a + 1));
                edits.add(new Edits(sb.toString(), distance));
            }
        }
    }
    public void transposition(String inputWord, int distance) {
        if (inputWord.length() > 1) {
            for (int a = 0; a < inputWord.length() - 1; a++) {
                StringBuilder sb = new StringBuilder();
                char first = inputWord.charAt(a);
                char second = inputWord.charAt(a + 1);
                sb.append(inputWord.substring(0,a)).append(second).append(first).append(inputWord.substring(a + 2));
                edits.add(new Edits(sb.toString(), distance));
            }
        }
    }
}
