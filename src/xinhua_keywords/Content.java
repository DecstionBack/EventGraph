package xinhua_keywords;

/**
 * Created by lenovo on 2016/7/25.
 */
import java.util.LinkedList;
import java.util.HashMap;

public class Content {
    private String title;
    private String content;
    private LinkedList<String> words;// = new LinkedList<String>();
    private HashMap<String, Double> tfidf = new HashMap<String, Double>();

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public void setWords(LinkedList<String> words){
        this.words = words;
    }

    public LinkedList<String> getWords(){
        return words;
    }

    public void setTfidf(HashMap<String, Double> tfidf){
        this.tfidf = tfidf;
    }

    public HashMap<String, Double> getTfidf(){
        return tfidf;
    }

}
