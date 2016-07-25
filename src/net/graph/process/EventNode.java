package net.graph.process;

import java.util.*;


public class EventNode {
	private String topicsID;
	private String newsID;
	private String title; 
	private Date date;
	private String segmentation;
	//private HashSet<String> entity = new HashSet<String>();
	private LinkedList<String> entity = new LinkedList<String>();
	private LinkedList<String> titleEntity = new LinkedList<String>();

	private LinkedList<Edge> edge = new LinkedList<Edge>();  //存储和本节点相邻的结点，存储的每个结点又保存着是无向边还是有向边
	
	//keywords中存储的float是在计算关键词时存储的tf*idf的值
	private HashMap<String, Float> keywords = new HashMap<String, Float>();
	
	//words是分词以后的结果
	private List<String> words = new LinkedList<String>();
	//文章结点的向量表示
	float[] articleVector;
	//含有位置的文章向量表示，由于位置容易出错，改成字符串的
	HashMap<String, Float> tfidfVector = new HashMap<String, Float>();
	
	//LDA文档-主题矩阵
	List<Double> LDA_Matrix = new LinkedList<Double>();
	
	//层次聚类算法使用到的类
	private ArrayList<EventNode> cluster = null;
	
	public String getTopicsID(){
		return topicsID;
	}
	public void setTopicsID(String topicsID){
		this.topicsID = topicsID;
	}
	public String getNewsID(){
		return newsID;
	}
	public void setNewsID(String newsID){
		this.newsID = newsID;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public LinkedList<String> getTitleEntity(){
		return titleEntity;
	}
	public void setTitleEntity(LinkedList<String> titleEntity){
		this.titleEntity = titleEntity;
	}
	public Date getDate(){
		return date;
	}
	public void SetDate(Date date){
		this.date = date;
	}
	public LinkedList<String> getEntity(){
		return entity;
	}
	public void setEntity(LinkedList<String> entity){
		this.entity = entity;
	}
	public LinkedList<Edge> getEdge(){
		return edge;
	}
	public void setEdge(LinkedList<Edge> edge){
		this.edge  = edge;
	}
	public ArrayList<EventNode> getCluster(){
		return cluster;
	}
	public void setCluster(ArrayList<EventNode> cluster){
		this.cluster = cluster;
	}
	public HashMap<String, Float> getTFIDFKeywords(){
		return keywords;
	}
	public List<String> getWords(){
		return words;
	}
	public void setWords(List<String> words){
		this.words = words;
	}
	public List<String> getKeywords(){
		List<String> words = new LinkedList<String>();
		for (String str : keywords.keySet())
			words.add(str);
		return words;
	}
	public void setKeywords(HashMap<String, Float> keywords){
		this.keywords = keywords;
	}
	public String getSegmentation(){
		return segmentation;
	}
	public void setSegmentation(String segmentation){
		this.segmentation = segmentation;
	}
	public float[] getArticleVector(){
		return articleVector;
	}
	public void setArticleVector(float[] articleVector){
		this.articleVector = articleVector;
	}
	public HashMap<String, Float> getTfidfVector(){
		return tfidfVector;
	}
	public void setTfidfVector(HashMap<String, Float> tfidfVector){
		this.tfidfVector = tfidfVector;
	}
	public List<Double> getLDA_Matrix(){
		return LDA_Matrix;
	}
	public void setLDA_Matrix(List<Double> LDA_Matrix){
		this.LDA_Matrix = LDA_Matrix;
	}
	
}
