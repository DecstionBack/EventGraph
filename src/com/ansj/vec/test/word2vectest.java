package com.ansj.vec.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


import com.ansj.vec.Learn;
import com.ansj.vec.Word2VEC;



import com.ansj.vec.Learn;
import com.ansj.vec.Word2VEC;


public class word2vectest {
	private static final File sportCorpusFile = new File("news.txt");

    public static void main(String[] args) throws IOException {
     /*   File[] files = new File("corpus/sport/").listFiles();
        
        //构建语料
        try (FileOutputStream fos = new FileOutputStream(sportCorpusFile)) {
            for (File file : files) {
                if (file.canRead() && file.getName().endsWith(".txt")) {
                    parserFile(fos, file);
                }
            }
        }
        */
        //进行分词训练
        if (args[0].equals("1")){
        Learn lean = new Learn() ;
        
        lean.learnFile(sportCorpusFile) ;
        
        lean.saveModel(new File("vectors.mod")) ;
        }
        
        else 
        //加载测试
        {
        Word2VEC w2v = new Word2VEC() ;
        
        w2v.loadJavaModel("vectors.mod") ;
        List<String> list1 = new LinkedList<String>();
        List<String> list2 = new LinkedList<String>();
        list1.add("东方");
        list2.add("中国");
        //System.out.println(w2v.distance(list));
        System.out.println(w2v.fileDistance(list1, list2));
        }
    }
    
   

    /*private static void parserFile(FileOutputStream fos, File file) throws FileNotFoundException,
                                                                   IOException {
        // TODO Auto-generated method stub
        try (BufferedReader br = IOUtil.getReader(file.getAbsolutePath(), IOUtil.UTF8)) {
            String temp = null;
            JSONObject parse = null;
            while ((temp = br.readLine()) != null) {
                parse = JSONObject.parseObject(temp);
                paserStr(fos, parse.getString("title"));
                paserStr(fos, StringUtil.rmHtmlTag(parse.getString("content")));
            }
        }
    }

    private static void paserStr(FileOutputStream fos, String title) throws IOException {
        List<Term> parse2 = ToAnalysis.parse(title) ;
        StringBuilder sb = new StringBuilder() ;
        for (Term term : parse2) {
            sb.append(term.getName()) ;
            sb.append(" ");
        }
        fos.write(sb.toString().getBytes()) ;
        fos.write("\n".getBytes()) ;
    }*/

}
