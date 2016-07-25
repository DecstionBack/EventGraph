package xinhua_keywords;

/**
 * Created by lenovo on 2016/7/25.
 */

import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class Main {
    public static LinkedList<String> getStopwords() throws IOException{
        File file = new File("停用词表.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(fileReader);

        LinkedList<String> stopwords = new LinkedList<String>();
        String line = null;
        while((line = buffer.readLine())!=null){
            stopwords.add(line);
        }
        buffer.close();
        fileReader.close();
        return stopwords;
    }

    public static void main(String[] args) throws Exception{
        Xinhua_keywords xinhua = new Xinhua_keywords();
        //从数据库读取新闻内容
        LinkedList<Content> Contents = xinhua.getContents();
        System.out.println(Contents.size()); 

        //读取停用词，分词
        LinkedList<String> stopwords = getStopwords();
        Contents = xinhua.segmentWords(Contents, stopwords);
        System.out.println("segmentation OK!");
        //计算词语的idf值保存起来
        HashMap<String, Double> idfs = xinhua.getIdf(Contents);
        System.out.println(idfs);

    }
}
