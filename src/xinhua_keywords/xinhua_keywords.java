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
    public HashMap<String, Double> getIdf(LinkedList<Content> contents){
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
}
