package com.hankcs.lda;

import net.graph.cluster.KMeans;
import net.graph.process.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class MainLDA {
	
	public static void Main_LDA(LinkedList<EventNode> eventNodes) throws IOException{
		//数据换成eventNodes中的数据
		Corpus corpus = Corpus.load(eventNodes);
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
		ldaGibbsSampler.gibbs(80);
		double[][] theta = ldaGibbsSampler.getTheta();
		
		//调用K-Means聚类
		
		for (Integer i = 0; i < theta.length; i++){
			HashMap<String, Float> LDA = new HashMap<String, Float>();
			for (Integer j = 0; j < theta[0].length; j++){
				LDA.put(j.toString(), (float)theta[i][j]);
			}
			eventNodes.get(i).setTfidfVector(LDA);
		}
		KMeans kmeans = new KMeans();
		kmeans.KMEANS(eventNodes, 21);

		boolean flag = true;
		if (flag)
			return;
		
		//选择最大的作为聚类集合LDA_Cluster
		for (int i = 0; i < theta.length; i++){
			LinkedList<Double> LDA_Matrix = new LinkedList<Double>();
			for (int j = 0; j < theta[0].length; j++){
				LDA_Matrix.add(theta[i][j]);
			}
			eventNodes.get(i).setLDA_Matrix(LDA_Matrix);
		}
		LDACluster.LDA_Cluster(eventNodes);
		
		
		File file = new File("document_topic.txt");
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		
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
			for (int i =0; i < theta.length; i++){
				System.out.println("document:" +  i);
				buffer.write("document:" + i);
				buffer.newLine();
				StringBuilder LDA_Matrix = new StringBuilder();
				for (int j = 0; j < theta[0].length; j++){
					//System.out.print(theta[i][j] + " ");
					buffer.write(theta[i][j] + " ");
					LDA_Matrix.append(theta[i][j] + " ");
					buffer.newLine();
				}
				System.out.println();
				buffer.newLine();
				buffer.newLine();
				
				String sql = "update news_final set LDA_Matrix=" + "\"" + LDA_Matrix + "\" where id=" + (i+1);;
				try{
					statement.execute(sql);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			statement.close();
			conn.close();
			
		}catch(Exception e){
		}
			
		
		
		
		buffer.close();
		fileWriter.close();
		//theta[m][k]  文档m,主题k
		//Map<String, Double>[] topicMap = LdaUtil.translate(theta, corpus.getVocabulary(), 100);
		//LdaUtil.explain(topicMap);
	}
	
	public static void main(String[] args) throws IOException{
		//
		Corpus corpus = Corpus.load("data-lda/mini");
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
		ldaGibbsSampler.gibbs(25);
		//double[][] phi = ldaGibbsSampler.getPhi();
		double[][] theta = ldaGibbsSampler.getTheta();
		Map<String, Double>[] topicMap = LdaUtil.translate(theta, corpus.getVocabulary(), 1000);
		LdaUtil.explain(topicMap);
	}

}
