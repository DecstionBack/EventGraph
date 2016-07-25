package xinhua_keywords;

/**
 * Created by lenovo on 2016/7/25.
 */

import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.domain.Term;

import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;



public class Xinhua_keywords {

    public LinkedList<Content> getContents() throws SQLException{
        String driver = "com.mysql.jdbc.Driver";
        String url  ="jdbc:mysql://192.168.140.200:3306/gov_info?useSSL=false&character_encoding=utf8";
        String user = "root";
        String password = "ekgdebs";
        LinkedList<Content> contents = new LinkedList<Content>();
        Connection conn = null;
        Statement statement= null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed())
                System.out.println("Connecting to the Database successfully!");
            statement = conn.createStatement();
            String sql = "select id, title, content from xinhua_copy where person='xijinping'";
            rs =  statement.executeQuery(sql);

            while (rs.next()){
                Content content = new Content();
                content.setTitle(rs.getString("title"));
                content.setContent(rs.getString("content").replaceAll("[\n ]",""));
                contents.add(content);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            rs.close();
            statement.close();
            conn.close();
            return contents;
        }

    }

    public LinkedList<Content> segmentWords(LinkedList<Content> contents, LinkedList<String> stopwords){
        for (Content content : contents){
            LinkedList<String> words = new LinkedList<String>();
            for (Term term : ToAnalysis.parse(content.getContent())){
                words.add(term.getName());
            }
            words.removeAll(stopwords);
            //System.out.println(words);
            content.setWords(words);
        }

        return contents;
    }

    private double idf(String word, LinkedList<Content> contents){
        int number = 0;
        for (Content content : contents){
            if (content.getWords().contains(word)){
                number++;
                continue;
            }
        }
        return Math.log(contents.size() * 1.0 / (number + 1));
    }
    public HashMap<String, Double> calIdf(LinkedList<Content> contents){
        HashMap<String, Double> idfs = new HashMap<String, Double>();
        for (Content content : contents){
            for (String word : content.getWords()){
                if (!idfs.containsKey(word)){
                    idfs.put(word, idf(word, contents));
                }
            }
        }
        return idfs;
    }

    public double tf(String word, LinkedList<String> words){
        int number = 0;
        for(String item : words)
            if (item.equals(word))
                number++;
        return number * 1.0 / words.size();
    }
    public void calTfidf(LinkedList<Content> contents, HashMap<String, Double> idfs){
        for (Content content : contents){
            HashMap<String, Double> tfidf = new HashMap<String, Double>();
            for (String word : content.getWords()){
                if (!tfidf.containsKey(word)){
                    tfidf.put(word, tf(word,content.getWords()) * idfs.get(word));
                }
            }
            content.setTfidf(tfidf);
            System.out.println(tfidf);
        }
    }

    //由下面的maxTfidf调用，计算某一个词语的最大和第二大tf*idf值
    public LinkedList<Double> maxTfidf(String word, LinkedList<Content> contents){
        //如果词语只出现过一次，则不会有secondTfidf此时默认的为Double的最小值，和减去0基本是一样的，没有影响
        double firstTfidf = Double.MIN_VALUE, secondTfidf = Double.MIN_VALUE;
        for (Content content : contents){
            if (content.getWords().contains(word)){
                double temp = content.getTfidf().get(word);
                if (temp > firstTfidf)
                    firstTfidf = temp;
                else if (temp > secondTfidf)
                    secondTfidf = temp;
            }
        }
        LinkedList<Double> maxtfidf = new LinkedList<Double>();
        maxtfidf.add(firstTfidf);
        maxtfidf.add(secondTfidf);

        return maxtfidf;
    }

    //计算每个词语的最大和第二大tf*idf值
    public HashMap<String, LinkedList<Double>> maxTfidf(LinkedList<Content> contents){
        HashMap<String, LinkedList<Double>> maxTfidfs = new HashMap<String, LinkedList<Double>>();
        for (Content content : contents){
            for (String word : content.getWords()){
                if (!maxTfidfs.containsKey(word)){
                    maxTfidfs.put(word, maxTfidf(word, contents));
                }
            }
        }
        return maxTfidfs;
    }
    public void minusTfidf(LinkedList<Content> contents, HashMap<String, LinkedList<Double>> maxTfidfs){
        double firstTfidf = Double.MIN_VALUE;
        double secondTfidf = Double.MIN_VALUE;
        for (Content content : contents){
            for (String word : content.getWords()) {
                firstTfidf = maxTfidfs.get(word).get(0);
                if (Math.abs(content.getTfidf().get(word) - firstTfidf) < 0.000000001){
                    secondTfidf = maxTfidfs.get(word).get(1);
                    content.getTfidf().put(word, firstTfidf - secondTfidf);
                }
                else{
                    content.getTfidf().put(word, content.getTfidf().get(word) - firstTfidf);
                }
            }
        }
    }
}
