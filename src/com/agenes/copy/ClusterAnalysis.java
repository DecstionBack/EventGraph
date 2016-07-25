package com.agenes.copy;  
  
import net.graph.process.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
  
  
public class ClusterAnalysis {  
   
	public static float[][] distances;
	public static List<ArrayList<EventNode>> clusters;
	public static int round = 0;
	
  public static void startAnalysis(List<EventNode> eventNodes){
	  //初始化
	  initialCluster(eventNodes);
	  
	  List<ArrayList<EventNode>> finalClusters = clusters;
	  
	  //95%的都参与了聚类
	  while(finalClusters.size() > eventNodes.size() * 0.05){
		  System.out.println("round:" + round);
		  round++;
		  int mergeIndexA = 0;
		  int mergeIndexB = 0;
		  for (int i = 0; i < finalClusters.size(); i++){
			  for (int j = i + 1; j < finalClusters.size(); j++){
				  ArrayList<EventNode> clusterA = finalClusters.get(i);
				  ArrayList<EventNode> clusterB = finalClusters.get(j);
				  float Dist = Float.MAX_VALUE;
				  for (EventNode nodeA : clusterA){
					  for (EventNode nodeB : clusterB){
						  //float tempDist = getDistance(nodeA, nodeB);
						  float tempDist = distances[eventNodes.indexOf(nodeA)][eventNodes.indexOf(nodeB)];
						  if (tempDist < Dist){
							  Dist = tempDist;
							  mergeIndexA = i;
							  mergeIndexB = j;
						  }
					  }
				  }
			  }
		  }
		  finalClusters = mergeCluster(finalClusters, mergeIndexA, mergeIndexB);
	  }
   }  
  
   private static List<ArrayList<EventNode>> mergeCluster(List<ArrayList<EventNode>> clusters,int mergeIndexA,int mergeIndexB){  
	   ArrayList<EventNode> clusterA = clusters.get(mergeIndexA);
	   ArrayList<EventNode> clusterB = clusters.get(mergeIndexB);
	   for (EventNode node : clusterB){
		   clusterA.add(node);
		   node.setCluster(clusterA);
	   }
	   clusters.remove(mergeIndexB);

	   return clusters;  
   }  
  
   private static void initialCluster(List<EventNode> eventNodes){  
	   clusters = new ArrayList<ArrayList<EventNode>>();
	   for (EventNode node : eventNodes){
		   ArrayList<EventNode> cluster = new ArrayList<EventNode>();
		   cluster.add(node);
		   clusters.add(cluster);
		   node.setCluster(cluster);
	   }
   }  
  
   //计算两篇文章的相似度
   private static float getDistance(EventNode node1, EventNode node2){
	   float distance = 0;
	   float maga = 0;
	   float magb = 0;
	   HashMap<String, Float> vector1 = node1.getTfidfVector();
	   HashMap<String, Float> vector2 = node2.getTfidfVector();
	   for (String key : vector1.keySet()){
		   maga = vector1.get(key) * vector1.get(key);
		   if (vector2.containsKey(key))
			   distance += vector1.get(key) * vector2.get(key);
	   }
	   
	   for (String key : vector2.keySet())
		   magb += vector2.get(key) * vector2.get(key);
	   
	   return (float) (distance / Math.sqrt(maga * maga + magb * magb)); 
   }
   
  
   public static void main(String[] args){  
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
			List<EventNode> eventNodes = new ArrayList<EventNode>();
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
				//node.setWords(processWords(rs.getString("newsWordsUserdict")));
				//使用文本加标题
				//node.setWords(processWords(rs.getString("newsTitleWords") + " " + rs.getString("newsWordsUserdict")));
				//使用文本加实体
				//node.setWords(processWords(rs.getString("newsWords") + " " + rs.getString("newsKeywordsAndEntities")));
				//使用文本加标题加实体
				//node.setWords(processWords(rs.getString("newsTitleWords") + " " + rs.getString("newsWordsUserdict") + " " + rs.getString("newsKeywordsAndEntities")));
				//使用文本加评论
				//node.setWords(process.processWords(rs.getString("newsWords")));
				//node.setTitleEntity(processEntity(rs.getString("newsTitleWords")));
				//node.setEntity(processEntity(rs.getString("newsKeywordsAndEntities")));
				node.setTfidfVector(process.processTFIDFVector(rs.getString("newsContentAndTitleTFIDF")));
				//System.out.println(node.getWords());
				eventNodes.add(node);
			} //while
			System.out.println("NumberOfNews: " + count);
			rs.close();
			conn.close();
			calcDist(eventNodes);
			startAnalysis(eventNodes);
			for (ArrayList<EventNode> cluster : clusters){
				for (EventNode node : cluster){
					System.out.println(node.getTitle());
				}
				System.out.println("\n\n\n\n");
			}
   }catch(Exception e){
	   e.printStackTrace();
   }
   } //main
   
   public static void calcDist(List<EventNode> eventNodes){
	   distances = new float[eventNodes.size()][eventNodes.size()];
	   for (int i= 0; i < eventNodes.size(); i++){
		   for (int j = i + 1; j < eventNodes.size(); j++){
			   distances[i][j] = getDistance(eventNodes.get(i), eventNodes.get(j));
			   distances[j][i] = distances[i][j];
		   }
	   }
	   System.out.println("distances calculating complete！");
   }
}  
