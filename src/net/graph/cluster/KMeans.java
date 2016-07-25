package net.graph.cluster;
/*
 *	Copyright 2013 Jonathan Zong
 *	www.jonathanzong.com
 *  --
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.*;

import net.graph.process.EventNode;
import net.graph.process.process;
import net.graph.test.Main;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class KMeans
{
	/*public static void main(String[] args) throws IOException{
		KMeans(null);
	}*/
	public static List<String> setTFIDF(LinkedList<EventNode> eventNodes){
		HashMap<String, Integer> global = new HashMap<String, Integer>();
    	//global不仅记录所有的单词，而且包含其出现的个数
	    //global是文档中出现的单词总量，只是表示是否含有，并不含有频率等信息，docs中存放着所有文档分词后的words
        //node中存放的keywords是HashMap<String word, Integer tf-idf>格式
        for (EventNode node : eventNodes){
        	//System.out.println(node.getWords());
        	for (String word : node.getWords()){
        		if (global.keySet().contains(word))
        			global.put(word, global.get(word) + 1);
        		else
        			global.put(word, 1);
        	}
        }
        System.out.println("global set...");
        System.out.println(global.size());
        //把要删除的字符串保存在一个链表里面
        List<String> deleteWord = new LinkedList<String>();
        for (String str : global.keySet()){
        	if (global.get(str) < 5)
        		deleteWord.add(str);
        }
        System.out.println("deleteword set...");
       
       //for (String item : global.keySet()){
       // 	System.out.println(item + ":" + global.get(item)); 
       // }
       
        //globalwords表示取出停用词以后的单词列表
        List<String> globalwords = new ArrayList<String>();
        for (String item : global.keySet())
        	if (!deleteWord.contains(item))
        		globalwords.add(item);
        System.out.println("globalword set...");
        boolean flag = true;
        if (flag)
        	return globalwords;
        //删除文档中包含要删除的词
        for (EventNode node : eventNodes){
        	List<String> nodewords = node.getWords();
        	nodewords.removeAll(deleteWord);
        	node.setWords(nodewords);
        	//System.out.println(node.getWords());
        }
        System.out.println("remove deleteword set...");
        //这儿使用分词，而不是用计算出来的关键词
	    for (EventNode node : eventNodes){
	    	HashMap<String, Float> tfidfvector = new HashMap<String, Float>();
	    	int pos = -1;
	    	float tfidf = 0;
	    	System.out.println("node:" + eventNodes.indexOf(node) + " set tfidf vector...");
	    	//System.out.println(node.getWords());
	    	for (String word : node.getWords()){
//	    		pos = globalwords.indexOf(word);
	    		tfidf = tf(node.getWords(), word) * idf(eventNodes, word);
	    		tfidfvector.put(word, tfidf);
	    	}
	    	tfidfvector.remove(0);
	    	//System.out.println(tfidfvector);
	    	node.setTfidfVector(tfidfvector);
	    	
	    }
	    System.out.println("tfidf vector set...");
	    return null;
	}
	
    public void KMEANS(LinkedList<EventNode> eventNodes, int clusternum) throws IOException
    {
    	//设置HashMap<Integer,　Float>类型的TFIDF值，最好将其存入到数据库中
//    	setTFIDF(eventNodes);
//    	boolean flag = true;
//    	if (flag) return;
    	//获取setTFIDF的globalword来求后面的位置
    	
    	
    	
//    	List<String> globalwords = setTFIDF(eventNodes);
    	
//    	System.out.println("文本+标题"+Main.titleWeight+"实体"+Main.entityWeight);
    	//根据标题和实体处理tfidf值
//    	for (EventNode node : eventNodes){
//    		HashMap<String, Float> vector = node.getTfidfVector();
//    		for (String word : node.getTitleEntity()){
////    			Integer pos = globalwords.indexOf(word);
//    			if (vector.containsKey(word)){
//    				vector.put(word, (float) (vector.get(word) * Main.titleWeight));
//    			}
//    		}
//    		for (String word : node.getEntity()){
////    			Integer pos = globalwords.indexOf(word);
//    			if (vector.containsKey(word)){
//    				vector.put(word, (float) (vector.get(word) * Main.entityWeight));
//    			}
//    		}
//    		node.setTfidfVector(vector);
//    	}
    	
	    //boolean flag = true;
	    //if (flag) return;
    	
	    HashMap<Integer, HashSet<EventNode>> clusters = new HashMap<Integer, HashSet<EventNode>>();
	    HashMap<Integer, HashMap<String, Float>> rand = new HashMap<Integer, HashMap<String, Float>>();
	    int k = clusternum;
	    int maxiter = 1000; //这里设置迭代的次数最多为100次
	    //选取初始的随机点
	  //选取距离尽可能远的k个初始值点,第一轮是如此选取，以后每次直接把上次的点拿过来就好了
	    rand.put(0, eventNodes.get(0).getTfidfVector());
    	for (int i = 1; i < k; i++){
    		float maxdistance = 0, distance = 0, mindistance = 1; //每次都找和前面点离的最近的点中的最大值,maxdistance是最终结果
    		Integer pos = -1; //记录下一个要选取的点的位置
    		for (EventNode node : eventNodes){
    			//记录每一个点离已选取的点的最近距离，然后选择最大的最小距离
    			for (Map.Entry<Integer, HashMap<String, Float>> random : rand.entrySet()){
    				//System.out.println("node:" + eventNodes.indexOf(node));
    				mindistance = 1;
    				if (eventNodes.indexOf(node) == random.getKey()) {mindistance = -1; break;}
    				distance = cosSim(node.getTfidfVector(), random.getValue());
    				//这儿选取的是一个点到选好的随机点之间的最小值
    				if (distance < mindistance){
    					mindistance = distance;
    				}
    			}
    			//选取最近距离中的最大值
    			if (mindistance > maxdistance){
    				maxdistance = mindistance;
					pos = eventNodes.indexOf(node);
				}
    		}
    		rand.put(pos, eventNodes.get(pos).getTfidfVector());
    	}
    	System.out.println("round:" + 0 + " rand set...");
	    for(int init=0;init<maxiter;init++){
	    	System.out.println("round:" + init);
	    	clusters.clear();
	    	//由于每次选择的r可能不相同，所以clusters需要重新配置
	    	for (Integer r : rand.keySet()){
	    		//System.out.println(r);
	    		clusters.put(r, new HashSet<EventNode>());
	    		//System.out.println(clusters.get(r));
	    	}
		    boolean go = false;
		    	for (int i = 0; i < eventNodes.size(); i++){
		    		int center = -1;
		    		float sim = 0;
		    		for (Integer r : rand.keySet()){
		    			//csim会出现等于0的情况
		    			float csim = cosSim(eventNodes.get(i).getTfidfVector(), rand.get(r));
		    			//System.out.println("csim:" + csim);
		    			if (csim >= sim){
		    				sim = csim;
		    				center = r;
		    			}
		    		}
		    		//System.out.println("center:" + center);
		    		clusters.get(center).add(eventNodes.get(i));
		    	}
		    	System.out.println("round:" + init +" cluster set...");
		    	//更新的时候得到一个keyset的并集，然后计算平均值
		    	for (Integer r : rand.keySet()){
		    		HashMap<String, Float> updatecenter = new HashMap<String, Float>();
		    		//先全部加起来，然后再计算
		    		for (EventNode node : clusters.get(r)){
		    			for (String key : node.getTfidfVector().keySet()){
		    				if (!updatecenter.containsKey(key))
		    					updatecenter.put(key, node.getTfidfVector().get(key));
		    				else
		    					updatecenter.put(key, updatecenter.get(key) + node.getTfidfVector().get(key));
		    			}
		    		}
		    		//对updatecenter中的数据进行平均取值
		    		for (String r1 : updatecenter.keySet())
		    			updatecenter.put(r1, updatecenter.get(r1) / clusters.get(r).size());
		    		System.out.println("round:" + init +" updatecenter set...");
		    		//判断是否更新以后的中心和原来的中心一样,rand的中心是rand.get(r)，而更新以后的就是updatecenter
		    		float sim = cosSim(rand.get(r), updatecenter);
		    		if (sim < 0.999999)
		    			go =true;
		    		rand.put(r, updatecenter);
		    		System.out.println("sim:" + sim);
		    	}
		    if (!go)
		    {
		    	System.out.println("total iterator:" + init);
		    	break;
		    }
	    }
	    //对结果进行分析，计算prec、call以及F1值
	   // resultAnalyze(eventNodes, clusters, Main.titleWeight, Main.entityWeight);
	    resultAnalyze2(eventNodes, clusters, Main.titleWeight, Main.entityWeight);
    	}
    
    public static void resultAnalyze2(LinkedList<EventNode> eventNodes, 
    		HashMap<Integer, HashSet<EventNode>> clusters, Integer titleWeight, Integer entityWeight) throws IOException{
    	System.out.println("Write into file result.txt");
    	//输出结果到文件，方便查看
	    File file = new File("result_LDA.txt");
	    FileWriter fileWriter = new FileWriter(file, true);
	    BufferedWriter buffer = new BufferedWriter(fileWriter);
	    int numberOfCluster = 0;
	    
	  //先要获得每类新闻的数目
	    HashMap<Integer, Integer> classnumbers = new HashMap<Integer, Integer>();
	    for (EventNode node : eventNodes){
	    	int topicsID = Integer.parseInt(node.getTitle().split(":")[0]);
	    	if (!classnumbers.containsKey(topicsID))
	    		classnumbers.put(topicsID, 1);
	    	else classnumbers.put(topicsID, classnumbers.get(topicsID) + 1);
	    }
	    float[] precs = new float[clusters.size() * 2];
	    for (int i = 0; i < precs.length; i++)
	    	precs[i] = 0;
	    float[] recalls = new float[classnumbers.size() * 2];
	    for (int i = 0; i < recalls.length; i++)
	    	recalls[i] = 0;
	    
	    
	    
	    HashMap<Integer, Integer> recallnum  = new HashMap<Integer, Integer>();
	    for (HashSet<EventNode> cluster : clusters.values()){
//	    	topicsID:number对，方便找最大值
	    	HashMap<Integer, Integer> precnums = new HashMap<Integer, Integer>();
	    	for (EventNode node : cluster){
	    		//buffer.write(node.getTitle());
	    		//buffer.newLine();
	    		//统计每个类簇的precnum数目
	    		int topicsID = Integer.parseInt(node.getTitle().split(":")[0]);
	    		if (!precnums.containsKey(topicsID))
	    			precnums.put(topicsID, 1);
	    		else precnums.put(topicsID, precnums.get(topicsID) + 1);
	    	}
	    	int maxprecnum = 0, maxclassnum = -1;
	    	for (Integer topicsID : precnums.keySet()){
	    		if (precnums.get(topicsID) > maxprecnum){
	    			maxprecnum = precnums.get(topicsID);
	    			maxclassnum = topicsID;
	    		}
	    	}
	    	precs[numberOfCluster] = (float) (maxprecnum * 1.0 / cluster.size() * cluster.size() / 707);
	    	recalls[numberOfCluster++] = (float) (maxprecnum * 1.0 / classnumbers.get(maxclassnum) * cluster.size() / 707);
//	    	buffer.newLine();
//	    	buffer.newLine();
//	    	buffer.newLine();
//	    	buffer.newLine();
	    }
	    
	    //计算prec值
	    float sum = 0;
	    for (int i = 0; i < precs.length; i++)
	    	sum += precs[i];
	    //最终的prec值
	    //float prec = sum / clusters.size();
	    float prec = sum;
	    
	    
	    sum = 0;
	    for (int i = 0; i < recalls.length; i++)
	    	sum += recalls[i];
	   // float recall = sum / recalls.length;
	    float recall = sum;
	    
	    //计算F-measure值
	    float F1 = 2 * (prec * recall) / (prec + recall);
	    
	    //输出到result文件
	    buffer.write("文本+标题" + titleWeight + "+实体" + entityWeight + " ");
	    System.out.println("prec:" + prec + " ");
	    buffer.write("prec:" + prec + " ");
	    System.out.println("recall:" + recall + " ");
	    buffer.write("recall:" + recall + " ");
	    System.out.println("F1:" + F1 + " ");
	    buffer.write("F1:" + F1 + " ");
	    buffer.newLine();
	    buffer.newLine();
	    buffer.close();
	    fileWriter.close();
    }
    
    static void resultAnalyze(LinkedList<EventNode> eventNodes, 
    		HashMap<Integer, HashSet<EventNode>> clusters, Integer titleWeight, Integer entityWeight) throws IOException{
    	System.out.println("Write into file result.txt");
    	//输出结果到文件，方便查看
	    File file = new File("result.txt");
	    FileWriter fileWriter = new FileWriter(file, true);
	    BufferedWriter buffer = new BufferedWriter(fileWriter);
	    //prec: precnum / precnumall   recall: recallnum/ recallnumall
	    Integer precnum = 0, recallnum = 0, precnumall = 0, recallnumall = 0;
	    
	    HashMap<Integer, Integer> articleclassnum = new HashMap<Integer, Integer>();
	    for (EventNode node : eventNodes){
	    	Integer topicsID = Integer.parseInt(node.getTitle().split(":")[0]);
	    	if (!articleclassnum.containsKey(topicsID))
	    		articleclassnum.put(topicsID, 1);
	    	else articleclassnum.put(topicsID, articleclassnum.get(topicsID) + 1); 
	    }
	    for (Integer key : articleclassnum.keySet())
	    	recallnumall += articleclassnum.get(key) * (articleclassnum.get(key) - 1) / 2;
	    
	   // System.out.println(articleclassnum);
	    //计算实验结果每一类中含有的各个子类的文章篇数
	    int i = 0;
	    HashMap<Integer, Integer> articleclass = new HashMap<Integer, Integer>();
	    for (HashSet<EventNode> cluster : clusters.values()){
	    	articleclass.clear();
	    	for (EventNode node : cluster){
	    		Integer topicsID = Integer.parseInt(node.getTitle().split(":")[0]);
	    		if (!articleclass.containsKey(topicsID))
	    			articleclass.put(topicsID, 1);
	    		else articleclass.put(topicsID, articleclass.get(topicsID) + 1);
	    		System.out.println(node.getTitle() + "\n");
	    		//buffer.write(node.getTitle() + "\r\n");
	    		//buffer.newLine();
	    		//buffer.newLine();
	    	}
	    	for (Integer topicsID : articleclass.keySet()){
	    		precnum += articleclass.get(topicsID) * (articleclass.get(topicsID) - 1) / 2;
	    		recallnum += articleclass.get(topicsID) * (articleclass.get(topicsID) - 1) / 2;
	    		precnumall += cluster.size() * (cluster.size() -1) / 2;
	    	}
	    	i++;
	    	//buffer.write("\r\n");
	    	//buffer.write("\r\n");
	    	//buffer.newLine();
	    	//buffer.newLine();
	    	System.out.println("\n\n\n\n");
	    	//buffer.newLine();
	    }
	    //计算最终的prec和recall以及F1的值
	    buffer.write("文本+标题" + titleWeight + "+实体" + entityWeight + " ");
	    float prec = (float) (precnum * 1.0 / precnumall);
	    float recall = (float) (recallnum * 1.0 / recallnumall);
	    float F1 = 2 * (prec * recall) / (prec + recall);
	    System.out.println("prec:" + prec + " ");
	    buffer.write("prec:" + prec + " ");
	    System.out.println("recall:" + recallnum * 1.0 / recallnumall + " ");
	    buffer.write("recall:" + recallnum * 1.0 / recallnumall + " ");
	    System.out.println("F1:" + F1 + " ");
	    buffer.write("F1:" + F1 + " ");
	    buffer.newLine();
	    buffer.newLine();
	    buffer.close();
	    fileWriter.close();
    }
    
    static float cosSim(HashMap<String, Float> a, HashMap<String, Float> b){
    	float dotp=0, maga=0, magb=0;
    	for (String word : a.keySet()){
    		//a和b中都有的词语才相互计算乘积
    		if (b.get(word) != null)
    			dotp += a.get(word)*b.get(word);
    		maga += Math.pow(a.get(word),2);
    		
    	}
    	for (String word : b.keySet())
    		magb += Math.pow(b.get(word),2);
    	if(maga - 0 < 0.0000001)
    		return 0;
    	if(maga - 0 < 0.0000001)
    		return 0;
    	float d = (float) (dotp / (Math.sqrt(maga) * Math.sqrt(magb)));
    	//System.out.println("dotp:" + dotp + " maga:" + maga + " magb:" + magb);
    	//System.out.println("d:" + d);
    	return d;
    }

    static float tf(List<String> words, String term){
    	float n = 0;
    	for(String word : words)
    		if(word.equals(term))
    			n++;
    	return n/words.size();
    }

    
    static float idf(List<EventNode> eventNodes, String term){
    	float n = 0;
    	for (EventNode node : eventNodes){
    		for (String word : node.getWords()){
    			if (word.equals(term)){
    				n++;
    				break;
    			}
    		}
    	}
    	return (float)Math.log10(eventNodes.size() / n);
    }
}


