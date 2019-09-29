package spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Trie implements ITrie {
    private TrieNode root;
    private int wordCount;
    private int nodeCount;
    private int hashCode;
    private int longestWord;


    public Trie (){
        this.root = new TrieNode();
        wordCount = 0;
        nodeCount = 1;
        hashCode = 0;
        longestWord = 0;
    }

    void addDictionary(String fileName) {
        try(Scanner scan = new Scanner(new File(fileName))){
            while (scan.hasNext()){
                String word = scan.next();
                add(word);
            }
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }
    }

    @Override
    public void add(String wordIn) {
        String word = wordIn.toLowerCase();
        if(!"".equals(word) && valid(word)){
            if(find(word)== null) {
                wordCount++;
            }
            if(word.length() > longestWord){
                longestWord = word.length();
            }
            hashCode = word.hashCode() / 3 + hashCode;
            addHelp(root, word);
        }
    }

    private void addHelp(TrieNode root, String word){
        TrieNode[] nodes = root.getNodes();
        if(!"".equals(word)) {
            int a = word.charAt(0) - 'a';
            if (nodes[a] == null) {
                nodes[a] = new TrieNode();
                nodeCount++;
            }
            if(word.length() > 1){
                addHelp(nodes[a], word.substring(1,word.length()));
            } else {
                addHelp(nodes[a], "");
            }
        } else {
            root.incrementValue();
        }
    }

    @Override
    public INode find(String word) {
        return findHelp(root, word);
    }

    private TrieNode findHelp(TrieNode root, String word){
        TrieNode[] nodes = root.getNodes();
        if(!"".equals(word)) {
            int a = word.charAt(0) - 'a';
            if (nodes[a] == null) {
                return null;
            }
            if(word.length() > 1){
                return findHelp(nodes[a], word.substring(1,word.length()));
            } else {
                return findHelp(nodes[a], "");
            }
        } else if (root.getValue() > 0) {
            return root;
        } else {
            return null;
        }
    }

    @Override
    public int hashCode(){
        return hashCode;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    private boolean valid(String word){
        boolean valid = true;
        for(int a = 0; a < word.length(); a++){
            if(word.charAt(a) < 'a' || word.charAt(a) > 'z'){
                valid = false;
                break;
            }
        }
        return valid;
    }

    @Override
    public boolean equals(Object o){
        try {
            Trie t = (Trie) o;
            return equalsHelp(root, t.root);
        } catch (Exception ex){
            System.out.println("error");
            return false;
        }
    }

    private boolean equalsHelp(TrieNode root, TrieNode rootComp){
        TrieNode[] nodes = root.getNodes();
        TrieNode[] nodesComp = rootComp.getNodes();
        if(root.getValue() != rootComp.getValue()){
            return false;
        }
        for(int a = 0; a < 26 ; a++) {
            if (nodes[a] != null && nodesComp[a] != null) {
                if(!equalsHelp(nodes[a], nodesComp[a])){
                    return false;
                }
            } else if (nodes[a] != null && nodesComp[a] == null) {
                return false;
            } else if (nodes[a] == null && nodesComp[a] != null) {
                return false;
            } else if(root.getValue() != rootComp.getValue()){
                return false;
            }
        }
        return true;
    }
    @Override
    public String toString(){
        return toStringHelp(this.root, "").toString();
    }

    public StringBuilder toStringHelp(TrieNode root, String word){
        TrieNode[] nodes = root.getNodes();
        StringBuilder up = new StringBuilder();
        for (int a = 0; a < nodes.length; a++){
            if(nodes[a] != null){
                StringBuilder temp = new StringBuilder();
                temp.append(word);
                temp.append((char)(a + 'a'));
                if(nodes[a].getValue() > 0){
                    up.append(temp).append('\n');
                }
                up.append(toStringHelp(nodes[a], temp.toString()));
            }
        }
        return up;
    }

    public int getLongestWord(){
        return longestWord;
    }
}
