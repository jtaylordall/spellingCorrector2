package spell;

public class Edits implements Comparable<Edits>{
    String word;
    int distance;
    int count;

    public Edits(String word, int distance){
        this.word = word;
        this.distance = distance;
        this.count = 0;
    }

    public String getWord() {
        return word;
    }

    public int getDistance() {
        return distance;
    }
    @Override
    public String toString(){
        return word + " d: " + distance + " c: " + count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return count;
    }

    @Override
    public int compareTo(Edits e){
        if(distance != e.distance){
            return Integer.compare(distance, e.getDistance());
        } else if(count != e.getCount()){
            return Integer.compare(e.getCount(), count);
        } else {
            return word.compareTo(e.getWord());
        }
    }

}
