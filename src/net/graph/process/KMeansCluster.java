package net.graph.process;

import com.ansj.vec.Word2VEC;
import com.ansj.vec.util.*;
import com.ansj.vec.util.WordKmeans.Classes;

import java.io.IOException;
import java.util.*;

public class KMeansCluster {
	public static float[] getArticleVector(List<String> words) throws IOException{
		Word2VEC w2v = new Word2VEC();
		//需要提前训练好模型
		w2v.loadJavaModel("vectors.mod");
		
		float[] articlevector = new float[w2v.getWordVector(words.get(0)).length];
		for (int i = 0; i < articlevector.length; i++)
			articlevector[i] = 0;
		for (String word : words){
			float[] tempvector = w2v.getWordVector(word);
			for (int i = 0; i < articlevector.length; i++)
				articlevector[i] += tempvector[i];
		}
		return articlevector;
	}
	public void KMeans(List<EventNode> eventNodes) throws IOException{
		for (EventNode node : eventNodes){
			node.setArticleVector(getArticleVector(node.getKeywords()));
			float[] temp = getArticleVector(node.getKeywords());
			for (int i = 0; i < temp.length; i++)
				System.out.print(temp[i] + " ");
			System.out.println();
		}
		
		HashMap<String, float[]> wordMap = new HashMap<String, float[]>();
		for (EventNode node : eventNodes){
			wordMap.put(node.getTitle(), node.getArticleVector());
		}
		WordKmeans kmeans = new WordKmeans(wordMap, 3, 50);
		Classes[] explain = kmeans.explain();
		for (int i = 0; i < explain.length; i++) {
            System.out.println("--------" + i + "---------");
            //System.out.println(explain[i].getTop(10));
            System.out.println(explain[i].getAll().toString());
        }
		//得到词向量
		
		//根据词向量得到文章向量
		
		//进行K-Means聚类
	}
}
