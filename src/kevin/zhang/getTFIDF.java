package kevin.zhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kevin.zhang.Content;

public class getTFIDF {

	public static LinkedList<Content> getContent(String filename) throws Exception{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		LinkedList<Content> contents = new LinkedList<Content>();
		String line = null;
		while ((line=buffer.readLine()) != null){
			//System.out.println(line);
			Content content = new Content();
			content.setContent(line);
			contents.add(content);
		}
		return contents;
	}
	
	public static LinkedList<String> getString(String filename) throws Exception{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		LinkedList<String> dicts = new LinkedList<String>();
		String line = null;
		while ((line=buffer.readLine()) != null){
			//System.out.println(line);
			dicts.add(line);
		}
		return dicts;
	}
	
	public static double getPMIs(String word, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<String, Double> PMIs = new HashMap<String, Double>();
		int youhaiFreq = 0;
		for (Content item : youhaiContents){
			if (item.getWords().contains(word))
				youhaiFreq ++;
		}
		int AllFreq = youhaiFreq;
		for (Content item : wuhaiContents){
			if (item.getWords().contains(word))
				AllFreq++;
		}
		double PMI1 = Math.log10(youhaiFreq * 1.0 / (AllFreq * youhaiContents.size()) + 0.0001);
		
		int wuhaiFreq = 0;
		for (Content item : wuhaiContents){
			if (item.getWords().contains(word))
				wuhaiFreq ++;
		}
		double PMI2 = Math.log10(wuhaiFreq * 1.0 / (AllFreq * wuhaiContents.size()) + 0.0001);
		
		return PMI1 + PMI2;
	}
	
	public static LinkedList<String> getGlobalWords(LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		LinkedList<String> globalwords = new LinkedList<String>();
		for (Content content : youhaiContents){
			for (String word : content.getWords()){
				if (!globalwords.contains(word))
					globalwords.add(word);
			}
		}
		for (Content content : wuhaiContents){
			for (String word : content.getWords())
				if (!globalwords.contains(word))
					globalwords.add(word);
		}
		return globalwords;
	}
	
	public static void WriteFile(String filename){
		
	}
	
	public static HashMap<Integer, Double> getTFIDF(Content content, LinkedList<String> PMIWords, LinkedList<Content> youhaiContents, LinkedList<Content> wuhaiContents){
		HashMap<Integer, Double> tfidf = new HashMap<Integer, Double>();
		LinkedList<Content> contents = new LinkedList<Content>();
		for (Content item : youhaiContents){
			contents.add(item);
		}
		for (Content item : wuhaiContents){
			contents.add(item);
		}
		for (String word : content.getWords()){
			if (PMIWords.contains(word))
				tfidf.put(PMIWords.indexOf(word), tf(content.getWords(), word) * idf(word, contents));
		}
		
		return tfidf;
	}
	
	public static double tf(LinkedList<String> words, String word){
		int num = 0;
		for (String item : words){
			if (item.equals(word))
				num ++;
		}
		return num * 1.0 / words.size();
	}
	
	public static double idf(String word, LinkedList<Content> contents){
		int num = 0;
		for (Content content : contents){
			if (content.getWords().contains(word))
				num ++;
		}
		return Math.log10(contents.size() * 1.0 / (num + 1));
	}
	
	public static LinkedList<Content> getContentWords(String filename) throws IOException{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		String line = null;
		LinkedList<Content> contents = new LinkedList<Content>();
		while((line=buffer.readLine()) != null){
			Content content = new Content();
			LinkedList<String> words = new LinkedList<String>();
			for (String word : line.split(" ")){
				if (!word.equals(" "))
					words.add(word);
			}
			content.setWords(words);
			contents.add(content);
		}
		
		buffer.close();
		fileReader.close();
		
		return contents;
	}
	
	public static LinkedList<String> getPMIWords(int number) throws Exception{
		String driver = "com.mysql.jdbc.Driver";
		String url  ="jdbc:mysql://192.168.140.200:3306/mingan?useSSL=false";
		String user = "root";
		String password = "ekgdebs";
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		LinkedList<String> PMIWords = new LinkedList<String>();
		try{
			Class.forName(driver);
			//Connection conn = DriverManager.getConnection(url, user, null);
			conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			statement = conn.createStatement();
	
			//读取news
			String sql = "select * from test limit " + number;
			rs = statement.executeQuery(sql);
			while(rs.next()){
				String keyword = rs.getString("keyword");
				PMIWords.add(keyword);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			rs.close();
			statement.close();
			conn.close();
		}
		return PMIWords;
	}
	
	
	public static void getFilteredWords() throws Exception{
		LinkedList<String> filtered = new LinkedList<String>();
		File file = new File(".\\sample\\wuhai.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		
		File file2 = new File(".\\sample\\wuhai_word.txt");
		FileWriter fileWriter = new FileWriter(file2);
		BufferedWriter buffer2 = new BufferedWriter(fileWriter);
		
		String line = null;
		while((line=buffer.readLine())!=null){
			buffer2.write(line.split(";")[0] + ":");
			int length = line.split(";")[0].length();
			//第二个参数没有意义
			LinkedList<String> words = SegmentationNLPIR2.Segmentation(line.substring(length, line.length()),new LinkedList<String>());
			System.out.println(words);
			for (String word : words){
//				if (!word.contains(" ") && !word.contains(" "))
//					buffer2.write(word + " ");
				if (word.equals(""))
					continue;
				if (words.indexOf(word) == words.size() - 1)
					continue;
				buffer2.write(word + " ");
			}
			buffer2.newLine();
		}
		buffer2.close();
		buffer.close();
		fileReader.close();
		fileWriter.close();
	}
	
	public static void main(String[] args) throws Exception{
		//LinkedList<String> stopwords = getString("停用词表.txt");
		//getFilteredWords(/*stopwords*/);
		
//		boolean flag = true;
//		if (flag)
//			return;
		LinkedList<Content> youhaiContents = getContentWords(".\\sample\\youhai_words.txt");
		LinkedList<Content> wuhaiContents = getContentWords(".\\sample\\wuhai_words.txt");
		LinkedList<Content> trainyouhaiContents = getContentWords(".\\sample\\train_youhai_words.txt");
		LinkedList<Content> trainwuhaiContents = getContentWords(".\\sample\\train_wuhai_words.txt");
		LinkedList<Content> testyouhaiContents = getContentWords(".\\sample\\test_youhai_words.txt");
		LinkedList<Content> testwuhaiContents = getContentWords(".\\sample\\test_wuhai_words.txt");
		
		//LinkedList<String> userDict = getString(".\\sample\\userdic.txt");
		
		
		
		//读取特征词，使用数据库读取
		int number = Integer.parseInt(args[0]);
		LinkedList<String> PMIWords = getPMIWords(number);
		System.out.println("Selected Words:" + PMIWords);
		
		System.out.println("read files complete...");
		
		File file = new File(".\\sample\\result_train_TFIDF_" + number +".txt");
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		for(Content content : trainyouhaiContents){
			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
			
			List<Integer> keys = new ArrayList<Integer>();
			for (Integer key : content.getTFIDF().keySet())
				keys.add(key);
			if (keys.size() > 0){
			Collections.sort(keys);
			buffer.write("1 ");
			for (Integer key : keys)
				buffer.write(key + ":" + content.getTFIDF().get(key) + " ");
			}
			buffer.newLine();
		}
		
		for(Content content : trainwuhaiContents){
			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
			List<Integer> keys = new ArrayList<Integer>();
			for (Integer key : content.getTFIDF().keySet())
				keys.add(key);
			if (keys.size() > 0){
			Collections.sort(keys);
			buffer.write("0 ");
			for (Integer key : keys)
				buffer.write(key + ":" + content.getTFIDF().get(key) + " ");
			}
			buffer.newLine();
		}
		buffer.close();
		fileWriter.close();
		
		File file2 = new File(".\\sample\\result_test_TFIDF_" + number + ".txt");
		FileWriter fileWriter2 = new FileWriter(file2);
		BufferedWriter buffer2 = new BufferedWriter(fileWriter2);
		for(Content content : testyouhaiContents){
			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
			
			List<Integer> keys = new ArrayList<Integer>();
			for (Integer key : content.getTFIDF().keySet())
				keys.add(key);
			if (keys.size() > 0){
			Collections.sort(keys);
			buffer2.write("1 ");
			for (Integer key : keys)
				buffer2.write(key + ":" + content.getTFIDF().get(key) + " ");
			}
			buffer2.newLine();
		}
		
		for(Content content : testwuhaiContents){
			content.setTFIDF(getTFIDF(content, PMIWords, youhaiContents, wuhaiContents));
			List<Integer> keys = new ArrayList<Integer>();
			for (Integer key : content.getTFIDF().keySet())
				keys.add(key);
			if (keys.size() > 0){
			Collections.sort(keys);
			buffer.write("0 ");
			for (Integer key : keys)
				buffer2.write(key + ":" + content.getTFIDF().get(key) + " ");
			}
			buffer2.newLine();
		}
		buffer2.close();
		fileWriter2.close();
	}
}
