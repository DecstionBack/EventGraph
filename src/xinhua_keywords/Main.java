package xinhua_keywords;

/**
 * Created by lenovo on 2016/7/25.
 */

import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;

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

    public static void writeFiles(LinkedList<Content> contents) throws IOException{
        //输出每篇文章的关键词
        File file = new File("keywords.txt");
        FileWriter fileWriter  = new FileWriter(file);
        BufferedWriter buffer = new BufferedWriter(fileWriter);
        for (Content content : contents){
            ArrayList<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String, Double>>(content.getTfidf().entrySet());
            Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue()) * (-1);
                }
            });


            //输出前20个词语
            int number = 20, i = 0;
            for (Map.Entry<String, Double> entry : content.getTfidf().entrySet()){
                System.out.print(entry.getKey() + " ");
                buffer.write(entry.getKey() + " ");
                i++;
                if (i >= 20)
                    break;
            }
            System.out.println();
            buffer.newLine();
            buffer.newLine();
        }
        buffer.close();
        fileWriter.close();
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
        HashMap<String, Double> idfs = xinhua.calIdf(Contents);
        System.out.println(idfs);
        //计算所有词语的原始tf*idf值
        xinhua.calTfidf(Contents, idfs);
        System.out.println("cal TFIDF OK!");
        //计算每个词语的最大和第二大tf*idf值
        HashMap<String, LinkedList<Double>> maxTfidfs = xinhua.maxTfidf(Contents);
        System.out.println(maxTfidfs);
        System.out.println("max TFIDF OK!");
        //计算每个词语减其余文章的最大tf*idf值以后的tf*idf值
        xinhua.minusTfidf(Contents, maxTfidfs);
        for (Content content : Contents){
            System.out.println(content.getTfidf());
        }
        System.out.println("minus TFIDF OK!");
        writeFiles(Contents);


    }
}
