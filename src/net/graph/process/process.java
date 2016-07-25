package net.graph.process;

import com.hankcs.lda.*;
import java.util.*;
import net.graph.cluster.*;

import net.graph.process.Edge.Type;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

import net.graph.NLPIR.EntityNLPIR;
import kevin.zhang.*;

import com.ansj.vec.util.*;

public class process {
	private LinkedList<EventNode> eventNodes = new LinkedList<EventNode>();
	public static double sameNewsThreshold = 0.8, similarNewsThreshold = 0.4;
	
	public void LoadData(){
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
	
			//读取news
			String sql = "select * from news_final";
			ResultSet rs = statement.executeQuery(sql);
			Integer count = 0;  //number of nodes
			//Integer topicID, newsID;
			//String newsTitle, newsLink, newsTime, newsText, newsSource;
			while(rs.next()){
				count++;
				EventNode node = new EventNode();
				node.setTopicsID(rs.getString("topicsID"));
				node.setNewsID(rs.getString("newsID"));
				node.setTitle(rs.getString("topicsID") + ":" + rs.getString("newsTitle"));
				//node.setText(rs.getString("newsText"));
				//node.SetDate((processDate(rs.getString("newsTime"))));
				//只是使用标题抽取
				//node.setWords(processWords(rs.getString("newsTitleWords")));
				//只是使用纯实体
				//node.setWords(processWords(rs.getString("newsKeywordsAndEntities")));
				//只是使用文本
				node.setWords(processWords(rs.getString("newsWordsUserdict")));
				//使用文本加标题
				//node.setWords(processWords(rs.getString("newsTitleWords") + " " + rs.getString("newsWordsUserdict")));
				//使用文本加实体
				//node.setWords(processWords(rs.getString("newsWords") + " " + rs.getString("newsKeywordsAndEntities")));
				//使用文本加标题加实体
				//node.setWords(processWords(rs.getString("newsTitleWords") + " " + rs.getString("newsWordsUserdict") + " " + rs.getString("newsKeywordsAndEntities")));
				//使用文本加评论
				//node.setWords(processWords(rs.getString("newsWords") + " " + rs.getString("newsCommentsKeywordsAndEntites")));
				//node.setTitleEntity(processEntity(rs.getString("newsTitleWords")));
				//node.setEntity(processEntity(rs.getString("newsKeywordsAndEntities")));
				//node.setTfidfVector(processTFIDFVector(rs.getString("newsContentTFIDF")));
				//node.setLDA_Matrix(processLDA_Matrix(rs.getString("LDA_Matrix")));
				//System.out.println(node.getWords());
				eventNodes.add(node);
			}
			System.out.println("NumberOfNews: " + count);
			rs.close();
			conn.close();
			//LDACluster.LDA_Cluster(eventNodes);
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//设置关键词，后面将其放到数据库中直接读取，节约时间
		//processKeywords(eventNodes);
		
	}
	
	public Date processDate(String time){
		if (time == null || time.equals("null"))
			return null;
		Integer year = Integer.parseInt(time.substring(0, 4));
		Integer month = Integer.parseInt(time.substring(5, 7));
		Integer day = Integer.parseInt(time.substring(8, 10));
		Integer hour, minute;
		try{
		hour = Integer.parseInt(time.substring(11,13));
		minute = Integer.parseInt(time.substring(14, 16));
		}catch(Exception e){
			hour = 0;
			minute = 0;
		}
		//System.out.println(year + " " + month + " " + day + " " + hour + " " + minute);
		@SuppressWarnings("deprecation")
		Date date = new Date(year, month, day, hour, minute);
		return date;
		
	}
	//加载数据库中存放的实体和关键词
	public HashSet<String> processKeywordsAndEntities(String keywordsandentities){
		String[] KeyWordsAndEntities = keywordsandentities.split("|");
		HashSet<String> wordsandentities = new HashSet<String>();
		for (String item : KeyWordsAndEntities)
			wordsandentities.add(item);
		return wordsandentities;
	}
	public static void processKeywords(LinkedList<EventNode> eventNodes ){
		ExtractKeywordsAndEntities extract = new ExtractKeywordsAndEntities();
		extract.TFIDFExtractKeywords(eventNodes);
		//extract.TFIDFExtractKeywordsDB(eventNodes);
	}
	//处理读取的实体和关键词
	public static LinkedList<String> processEntity(String str){
		LinkedList<String> entity = new LinkedList<String>();
		for (String s : str.split(" "))
			entity.add(s);
		return entity;
	}
	public static LinkedList<String> processTitleEntity(String str){
		LinkedList<String> titleEntity = new LinkedList<String>();
		for (String s : str.split(" "))
			titleEntity.add(s);
		return titleEntity;
	}
	
	//处理读入的TFIDF值
	public static HashMap<String, Float> processTFIDFVector(String tfidfvector){
		HashMap<String, Float> TFIDF = new HashMap<String, Float>();
		String[] vector = tfidfvector.split(" ");
		for (int i = 0; i < vector.length; i++){
			String key = vector[i].split(":")[0];
			Float value = Float.parseFloat(vector[i].split(":")[1]);
			TFIDF.put(key, value);
		}
		return TFIDF;
	}
	
	public static List<Double> processLDA_Matrix(String vector){
		List<Double> LDA_Matrix = new LinkedList<Double>();
		for (String item : vector.split(" ")){
			LDA_Matrix.add(Double.parseDouble(item));
		}
		return LDA_Matrix;
	}
	
	public static LinkedList<String> processWords(String str){
		LinkedList<String> words = new LinkedList<String>();
		for (String word : str.split(" "))
			if(word.length() > 0)
				words.add(word);
		//System.out.println(words);
		return words;
	}
	public void coarsenGraph() throws IOException{
		//CosineEntity(eventNodes);  //对实体和关键词的并集进行余弦相似性进行聚类
		//K-means算法结合TF-IDF进行文本聚类
	//	System.out.println(eventNodes.size());
		//KMeans means = new KMeans();
		//means.KMEANS(eventNodes, 24);  //第二个参数代表聚类数目
		//将得到的tf-idf值插入到数据库中
		//ExtractKeywordsAndEntities.insertTFIDF(eventNodes);
		//使用MVAG建立图谱
		//MVAG mvag = new MVAG();
		//mvag.buildGraph(eventNodes);
		
		//LDA算法
		MainLDA.Main_LDA(eventNodes);
		
	}
	
//	public void CosineEntity(LinkedList<EventNode> eventNodes){
//		LinkedList<HashSet<EventNode>> cluster = new LinkedList<HashSet<EventNode>>();
//		Integer numberOfIndex = 0;
//		LinkedList<LinkedList<EventNode>> sameNews = new LinkedList<LinkedList<EventNode>>();
//		
//		for (EventNode node : eventNodes){
//			ListIterator<EventNode> it = eventNodes.listIterator(numberOfIndex + 1);
//			numberOfIndex++;
//			EventNode temp = new EventNode();
//			while(it.hasNext()){
//				temp = it.next();
//				HashSet<String> set1 = null; //node.getEntity();
//				HashSet<String> set2= null; //temp.getEntity();
//				HashSet<String> unionOfEntities = new HashSet<String>(set1);
//				unionOfEntities.addAll(set2); //set1 set2取并集
//				ArrayList<Integer> vector1 = new ArrayList<Integer>();
//				ArrayList<Integer> vector2 = new ArrayList<Integer>();
//				for (String entity : unionOfEntities){
//					if (set1.contains(entity))
//						vector1.add(1);
//					else vector1.add(0);
//					if (set2.contains(entity))
//						vector2.add(1);
//					else vector2.add(0);
//				}
//				Integer sum1 = 0, sum2 = 0, multi = 0;
//				for (int i = 0; i < vector1.size(); i++){
//					multi += vector1.get(i) * vector2.get(i);
//					sum1 += vector1.get(i) * vector1.get(i);
//					sum2 += vector2.get(i) * vector2.get(i);
//				}
//				Double res = Math.abs(multi * 1.0) / (Math.sqrt(sum1) * Math.sqrt(sum2));
//				//同一篇新闻先放到一起，后面把重复的删除
//				/*if (res >= sameNewsThreshold)
//				{
//					 LinkedList<EventNode> isNodeInSameNews = getSameNewsLinkedList(sameNews, node);
//					 LinkedList<EventNode> isTempInSameNews = getSameNewsLinkedList(sameNews, temp);
//					 if ((isNodeInSameNews == null) && (isTempInSameNews == null))
//					 {
//						 LinkedList<EventNode> samenews = new LinkedList<EventNode>();
//						 samenews.add(node);
//						 samenews.add(temp);
//						 sameNews.add(samenews);
//					 }
//					 else if (isNodeInSameNews != null )
//					 {
//						 isNodeInSameNews.add(temp);
//					 }
//					 else if (isTempInSameNews != null){
//						 isTempInSameNews.add(node);
//					 }
//				}*/
//				//同一事件的新闻
//				if (res >= similarNewsThreshold){
//					if ((node.getSetOfCluster() == null) && (temp.getSetOfCluster() == null)){
//						HashSet<EventNode> setOfCluster = new HashSet<EventNode>();
//						setOfCluster.add(node);
//						setOfCluster.add(temp);
//						node.setSetOfCluster(setOfCluster);
//						temp.setSetOfCluster(setOfCluster);
//						cluster.add(setOfCluster);	
//					}
//					else if (node.getSetOfCluster() != null){
//						node.getSetOfCluster().add(temp);
//						temp.setSetOfCluster(node.getSetOfCluster());
//					}
//					else if (temp.getSetOfCluster() != null)
//						temp.getSetOfCluster().add(node);
//						node.setSetOfCluster(temp.getSetOfCluster());
//				}
//			}
//			//可能单独成为一类
//			if (node.getSetOfCluster() == null){
//				HashSet<EventNode> setOfCluster = new HashSet<EventNode>();
//				setOfCluster.add(node);
//				node.setSetOfCluster(setOfCluster);
//				cluster.add(setOfCluster);
//			}
//		}
//		//从分类中去除相同的新闻
//		/*for (LinkedList<EventNode> samenews : sameNews){
//			boolean isFirst = true;
//			for (EventNode node : samenews){
//				if (isFirst){
//					isFirst = false;
//					continue;
//				}
//				for (HashSet<EventNode> setOfCluster : cluster)
//					if (setOfCluster.contains(node))
//						setOfCluster.remove(node);
//				eventNodes.remove(node);
//			}
//		}*/
//		//输出聚类之后的集合
//		for (HashSet<EventNode> setOfCluster : cluster){
//			for (EventNode node : setOfCluster)
//				System.out.println(node.getTopicsID() + "  :  " + node.getTitle());
//			System.out.println("\n\n");
//		}
//		//for (HashSet<EventNode> setOfCluster : cluster){
//			//processEdge(setOfCluster);
//			//DisplayClusterNodes(setOfCluster);
//		//}
//	}
	
	public LinkedList<EventNode> getSameNewsLinkedList(LinkedList<LinkedList<EventNode>> sameNews, EventNode node){
		for (LinkedList<EventNode> samenews : sameNews)
			if (samenews.contains(node))
				return samenews;
		return null;
	}
	
	
	public void processEdge(HashSet<EventNode> setOfCluster){
		//建立边，先按照时间排序，然后再输出结果
		ArrayList<EventNode> tempList = new ArrayList<EventNode>();
		tempList.addAll(setOfCluster);
		Comparator<EventNode> comparator = new Comparator<EventNode>(){
			public int compare(EventNode a, EventNode b){
				return a.getDate().compareTo(b.getDate());
			}
		};
		Collections.sort(tempList,comparator);
		ArrayList<HashSet<EventNode>> timeList = new ArrayList<HashSet<EventNode>>();
		for (int i = 0; i < tempList.size() - 1; i++){
			HashSet<EventNode> timeSet = new HashSet<EventNode>();
			while (tempList.get(i).getDate().compareTo(tempList.get(i + 1).getDate()) == 0){
				timeSet.add(tempList.get(i));
				i++;
			}
			timeSet.add(tempList.get(i));
			timeList.add(timeSet);
		} 
		for (int i = 0; i < timeList.size() - 1; i++){
			HashSet<EventNode> set1 = timeList.get(i);
			HashSet<EventNode> set2 = timeList.get(i + 1);
			for (EventNode node1 : set1){
				LinkedList<Edge> edges = new LinkedList<Edge>();
				for (EventNode node2 : set2){
					Edge edge = new Edge();
					edge.setEdgeType(Type.DIRECTED_EDGE);
					edge.setToNode(node2);
					edges.add(edge);
				}
				node1.setEdge(edges);
			}
		}
		
		
		//输出DAG
		for (int i = 0; i < timeList.size(); i++){
			for (EventNode node : timeList.get(i)){
				System.out.println("Node" + node.getNewsID() + " : " + node.getTitle());
				System.out.println("Edge:");
				for (Edge toNode : node.getEdge())
					System.out.println(toNode.getToNode().getNewsID() + " " + toNode.getToNode().getTitle());
			}
		}
		
		/*
		for (EventNode node : setOfCluster){
			for (EventNode temp : setOfCluster){
				if (!(node == temp)){
					if (node.getDate().compareTo(temp.getDate()) < 0){
						Edge edge = new Edge();
						edge.setToNode(temp);
						edge.setEdgeType(Type.DIRECTED_EDGE);
						node.getEdge().add(edge);
					}
					else if (node.getDate().compareTo(temp.getDate()) == 0){
						Edge edge = new Edge();
						edge.setToNode(temp);
						edge.setEdgeType(Type.UNDIRECTED_EDGE);
						node.getEdge().add(edge);
						}
				}
			}
		}*/
	}
	
	public void DisplayClusterNodes(HashSet<EventNode> setOfCluster){
		for (EventNode node : setOfCluster){
			System.out.println("Node:" + node.getNewsID() + " " + node.getTitle() + " " /*+ node.getDate()*/);
			System.out.println("Edge:");
			for (Edge edge : node.getEdge()){
				System.out.print(edge.getToNode().getNewsID() + " " + edge.getToNode().getTitle() + " " /*+ edge.getToNode().getDate() +  " "*/);
				switch(edge.getEdgeType()){
				case DIRECTED_EDGE : { System.out.println("Directed edge"); break;}
				case UNDIRECTED_EDGE : { System.out.println("Undirected edge"); break;}
				default : break;
				}
			}
		}
	}
	
}
