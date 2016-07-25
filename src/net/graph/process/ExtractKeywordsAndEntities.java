package net.graph.process;

import java.util.*;

import net.graph.process.Edge.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

import kevin.zhang.SegmentationNLPIR;
import net.graph.NLPIR.EntityNLPIR;

public class ExtractKeywordsAndEntities {
	public static Integer NumberOfKeywords = 20;
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url  ="jdbc:mysql://127.0.0.1:3306/sinanews?useSSL=false";
	public static String user = "root";
	public static String password = "ekgdebs";
	public static void NewsExtractKeywordsAndEntities(){
	try{
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		if (!conn.isClosed())
			System.out.println("Connecting to the Database successfully!");
		Statement statement = conn.createStatement();
		Statement statement2 = conn.createStatement();

		String sql = "select * from keywordurl where id>=29581";
		//String newsID = null;
		ResultSet rs = statement.executeQuery(sql);
		StringBuffer keywordsandentities = new StringBuffer("");
		while(rs.next()){
			keywordsandentities.delete(0, keywordsandentities.length());
			//keywordsandentities.delete(0, keywordsandentities.length());
			//newsID = rs.getString("newsID");
			String id = rs.getString("id");
			System.out.println(id + ":");
			HashSet<String> KeywordsAndEntities = EntityNLPIR.KeyWordsAndEntities(rs.getString("abstract"));
			for (String str : KeywordsAndEntities)
				keywordsandentities.append(str + " ");
			if (keywordsandentities.length() >1)
				keywordsandentities.deleteCharAt(keywordsandentities.length() - 1);
			System.out.println(keywordsandentities);
			sql = "update keywordurl set WordsAndEntities=\"" + keywordsandentities + "\" where id=\"" + id + "\"";
			statement2.execute(sql);
		}
		statement.close();
		statement2.close();
		conn.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}
	public static void CommentsExtractKeywordsAndEntities(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			//读取news
			String sql = "select commentsID, commentsContent from comments_final where commentsKeyWordsAndEntities is null";
			StringBuffer keywordsandentities = new StringBuffer("");
			StringBuffer commentsID = new StringBuffer("");
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				commentsID.delete(0, commentsID.length());
				keywordsandentities.delete(0, keywordsandentities.length());
				commentsID.append(rs.getString("commentsID"));
				System.out.println(commentsID);
				HashSet<String> KeywordsAndEntities = new HashSet<String>();
				try{
				KeywordsAndEntities = EntityNLPIR.KeyWordsAndEntities(rs.getString("commentsContent"));
				}catch(Error e2){
					e2.printStackTrace();
					Thread.sleep(5000);
					continue;
				}
				for (String str : KeywordsAndEntities)
					keywordsandentities.append(str + " ");
				try{
				keywordsandentities.deleteCharAt(keywordsandentities.length() - 1);
				}catch(Exception e){
					e.printStackTrace();
				}
				System.out.println(keywordsandentities);
				sql = "update comments_final set commentsKeyWordsAndEntities=\"" + keywordsandentities + "\" where commentsID=" + commentsID;
				statement2.execute(sql);
				
			}
			statement.close();
			statement2.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
	public static void KeywordsIntoTxt(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			File file = new File("news.txt");
			FileWriter fileWriter =  new FileWriter(file ,true);
			BufferedWriter buffer = new BufferedWriter(fileWriter);
			//读取news
			String sql = "select newsKeywordsTFIDF from news";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String keywords = rs.getString("newsKeywordsTFIDF");
				System.out.println(keywords);
				buffer.write(keywords);
			}
			statement.close();
			conn.close();
			buffer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//处理分词以后的结果
	public static void NewsWordsProcess(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			Statement statement3 = conn.createStatement();
			//取出报社
			String sql = "select newsSource from news2 where topicsID in (13,26,27)";
			ResultSet newsSource = statement.executeQuery(sql);
			List<String> deleteWords = new LinkedList<String>();
			////deleteWords.add("原");
			//deleteWords.add("标题");
			///deleteWords.add("年");
			//deleteWords.add("月");
			//deleteWords.add("日");
			//deleteWords.add("本文");
			while (newsSource.next()){
				if (!deleteWords.contains(newsSource.getString("newsSource")))
					deleteWords.add(newsSource.getString("newsSource"));
			}
			deleteWords.add("中新网");
			deleteWords.add("中新社");
			deleteWords.add("快讯");
			StringBuffer buffer = new StringBuffer();
			for (String str : deleteWords){
				buffer.append(str + "|");
			}
			String delete = buffer.toString();
			statement.close();
			
			//读取news
			sql = "select newsID, newsWords from news2 where topicsID in (13,26,27)";
			ResultSet rs = statement2.executeQuery(sql);
			while(rs.next()){
				String newsID = null, newsWords = null;
				newsID = rs.getString("newsID");
				newsWords = rs.getString("newsWords");
				newsWords = newsWords.replaceAll(delete, "");
				System.out.println(newsWords);
				sql = "update news2 set newsWords=\"" + newsWords + "\" where newsID=\"" + newsID + "\"";
				statement3.execute(sql);
			}
			statement.close();
			statement2.close();
			statement3.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//处理文本保存在newsContentProcess栏中
	public static void NewsContentProcess(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			//读取news
			String sql = "select newsID, newsContent from news2 where topicsID in (13,26,27)";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String newsID = null, newsContent = null;
				newsID = rs.getString("newsID");
				newsContent = rs.getString("newsContent");
				Integer pos = -1;
				if ((pos = newsContent.indexOf("编辑：")) != -1){
					newsContent = newsContent.substring(0, pos);
				}
				else if ((pos = newsContent.indexOf("编辑:")) != -1){
					newsContent = newsContent.substring(0, pos);
				}
				if ((pos = newsContent.indexOf("（完）责任"))!=-1)
					newsContent = newsContent.substring(0, pos);
				
				
				sql = "update news2 set newsContentProcess=\"" + newsContent + "\" where newsID=\"" + newsID + "\"";
				statement2.execute(sql);
				
			}
			statement.close();
			statement2.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//使用NLPIR进行分词并且将其保存到数据库中
	public static void NewsSegmentationWords(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			//读取news
			String sql = "select * from news_final_copy";
			String newsID = null;
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				StringBuffer SegmentationWords = new StringBuffer("");
				//keywordsandentities.delete(0, keywordsandentities.length());
				newsID = rs.getString("newsID");
				LinkedList<String> words =  SegmentationNLPIR.Segmentation(rs.getString("newsContentProcess"));
				for (String str : words)
					SegmentationWords.append(str + " ");
				SegmentationWords.deleteCharAt(SegmentationWords.length() - 1);
				System.out.println(SegmentationWords);
				sql = "update news_final_copy set newsWordsUserdict=\"" + SegmentationWords + "\" where newsID=\"" + newsID + "\"";
				statement2.execute(sql);
				
			}
			statement.close();
			statement2.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
	//把数据库中的分词结果保存到文件中作为语料进行训练
	public static void SegmentationIntoTxt() throws IOException{
		File file = new File("newsContent.txt");
		FileWriter fileWriter = new FileWriter(file, true);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			//读取news
			String sql = "select newsContent from news2";
			//String newsID = null;
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				buffer.write(rs.getString("newsContent") + "\n\n\n\n");
			}
			statement.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		buffer.close();
		fileWriter.close();
	}
	//使用tf-idf计算关键词并将其保存到数据库中
	public void TFIDFExtractKeywordsDB(LinkedList<EventNode> eventNodes){
		//Extract key words according to tf-idf
		//map--key:string value:tf-idf
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			Statement statement2 = conn.createStatement();
			//读取news
			String sql = "select newsID, newsWords from news";
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()){
				String newsID = rs.getString("newsID");
				String newsWords = rs.getString("newsWords");
				HashMap<String, Float> map = new HashMap<String, Float>();
				List<String> segmentation = Arrays.asList(newsWords.split(" "));
				for (String word : segmentation)
					map.put(word, tf(segmentation, word)*idf(eventNodes, word));
				List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(map.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<String, Float>>(){
					@Override
					public int compare(Map.Entry<String,Float> o1, Map.Entry<String,Float> o2){
						return o1.getValue().compareTo(o2.getValue()) * (-1);
					}
					});
				//HashMap<String, Float> keywords = new HashMap<String, Float>();
				//if (list.size() > NumberOfKeywords)
				//	for (int i = 0; i < NumberOfKeywords; i++)
				//		keywords.put(list.get(i).getKey(), list.get(i).getValue());
				StringBuffer buffer = new StringBuffer("");
				if (list.size() > NumberOfKeywords)
					for (int i = 0; i < NumberOfKeywords; i++)
						buffer.append(list.get(i).getKey() + " ");
				System.out.println(buffer.toString());
				sql = "update news set newsKeywordsTFIDF=\"" + buffer.toString() + "\" where newsID=\"" + newsID + "\"";
				statement2.execute(sql);
				//System.out.println(keywords.toString());
				//return keywords;
			}
			statement.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void TFIDFExtractKeywords(LinkedList<EventNode> eventNodes){
		//Extract key words according to tf-idf
		//map--key:string value:tf-idf
		HashMap<String, Float> map = new HashMap<String, Float>();
		for (EventNode node : eventNodes){
			List<String> segmentation = Arrays.asList(node.getSegmentation().split(" "));
			//if (segmentation.contains("①"))
			//	System.out.println("yes");
			for (String word : segmentation){
				map.put(word, tf(segmentation, word)*idf(eventNodes, word));
			}
			//将map转换为list进行排序
			List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Float>>(){
				@Override
				public int compare(Map.Entry<String,Float> o1, Map.Entry<String,Float> o2){
					return o1.getValue().compareTo(o2.getValue()) * (-1);
				}
			});
		//	for (Map.Entry<String, Double> item : list){
		//		System.out.println(item.getKey() + ":" + item.getValue());
		//	}
			HashMap<String, Float> keywords = new HashMap<String, Float>();
			if (list.size() > NumberOfKeywords)
				for (int i = 0; i < NumberOfKeywords; i++)
					keywords.put(list.get(i).getKey(), list.get(i).getValue());
			//System.out.println(keywords.toString());
			node.setKeywords(keywords);
		}
			
	}
		
	private static Float tf(List<String> words, String word){
		Float num = (float) 0;
		for (String str : words){
			if (str.equals(word))
				num++;
		}
		return num / words.size();
	}
	private static Float idf(List<EventNode> eventNodes, String word){
		double num = 0;
		for (EventNode node : eventNodes){
			if (node.getSegmentation().contains(word)){
				num++;
				continue;
			}
		}
		return (float) Math.log(eventNodes.size() / num);
	}
	public static void insertTFIDF(LinkedList<EventNode> eventNodes){
		String driver = "com.mysql.jdbc.Driver";
		String url  ="jdbc:mysql://127.0.0.1:3306/sinanews?useSSL=false";
		String user = "root";
		String password = "ekgdebs";
		try{
			Class.forName(driver);
			//Connection conn = DriverManager.getConnection(url, user, null);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Connecting to the Database successfully!");
			Statement statement = conn.createStatement();
			StringBuffer TFIDF = new StringBuffer("");
			for (EventNode node : eventNodes){
				TFIDF.delete(0, TFIDF.length());
				HashMap<String, Float> vector = node.getTfidfVector();
				for (String key : vector.keySet())
					TFIDF.append(key + ":" + vector.get(key) + " ");
				String sql = "update newsCommentsKeywordsAndEntites set newsCommentsKeywordsAndEntites=\"" + TFIDF + "\" where newsID=\"" + node.getNewsID() + "\"";
				statement.execute(sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//把评论的关键词和实体放到对应的新闻后面
	public static void newsCommentsKeywordsAndEntities(){
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/sinanews?useSSL=false&characterEncoding=UTF-8";
		String user = "root";
		String password = "ekgdebs";
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed()){
				System.out.println("Connecting to the Database successfully");
			}
			Statement statement = conn.createStatement();
			Statement updatestatement = conn.createStatement();
			String sql = "select * from comments_final";
			ResultSet rs = statement.executeQuery(sql);
			HashMap<String, StringBuffer> comments = new HashMap<String, StringBuffer>();
			while(rs.next()){
				String newsID = rs.getString("newsID");
				StringBuffer commentsEntities = new StringBuffer(rs.getString("CommentsKeywordsAndEntities"));
				if (!comments.containsKey(newsID))
				{
					comments.put(newsID, commentsEntities);
				}
				else{
					comments.put(newsID, comments.get(newsID).append(" ").append(commentsEntities));
				}
			}
			for (String newsID : comments.keySet()){
				System.out.println(newsID + ":" + comments.get(newsID));
				sql = "update newsCommentsKeywordsAndEntities set newsCommentsKeywordsAndEntities=\"" + comments.get(newsID) + "\" where newsID=\"" + newsID + "\"";
				try{
					updatestatement.execute(sql);
				}catch(Exception e){
					e.printStackTrace();
					continue;
				}
			}
			
			rs.close();
			statement.close();
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		//CommentsExtractKeywordsAndEntities();
		//newsCommentsKeywordsAndEntities();
		//ExtractKeywordsAndEntities.NewsExtractKeywordsAndEntities();
		//NewsSegmentationWords();
		//ExtractKeywordsAndEntities.CommentsExtractKeywordsAndEntities();
		//System.out.println("Processing...");
		//NewsContentProcess();
		//NewsWordsProcess();
		//NewsSegmentationWords();
		//System.out.println("Process Done!");
		//SegmentationIntoTxt();
		//ExtractKeywordsAndEntities extract = new ExtractKeywordsAndEntities();
		//extract.TFIDFExtractKeywords(null);
		//KeywordsIntoTxt();
	}
}
