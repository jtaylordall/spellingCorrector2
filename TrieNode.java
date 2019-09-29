package spell;

public class TrieNode implements INode {
    private TrieNode[] nodes;
    private int value;

    public TrieNode (){
        nodes = new TrieNode[26];
        value = 0;
    }

    public void incrementValue(){
        value++;
    }

    @Override
    public int getValue() {
        return value;
    }

    public TrieNode[] getNodes() {
        return nodes;
    }
}
