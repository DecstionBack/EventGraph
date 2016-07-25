package net.graph.process;

import net.graph.cluster.*;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class LDACluster {

	public static void LDA_Cluster(List<EventNode> eventNodes) throws IOException{
		HashMap<Integer, HashSet<EventNode>> clusters = new HashMap<Integer, HashSet<EventNode>>();
		for (EventNode node : eventNodes){
			List<Double> vector = node.getLDA_Matrix();
			double max = -1;
			int pos = -1;
			for (double item : vector){
				if (item > max){
					max = item;
					pos = vector.indexOf(item);
				}
			}
			if (!clusters.containsKey(pos)){
				HashSet<EventNode> cluster = new HashSet<EventNode>();
				cluster.add(node);
				clusters.put(pos, cluster);
			}
			else {
				HashSet<EventNode> cluster = clusters.get(pos);
				cluster.add(node);
				clusters.put(pos, cluster);
			}
		}
		
		for (HashSet<EventNode> cluster : clusters.values()){
			for (EventNode item : cluster){
				System.out.println(item.getTitle());
			}
			System.out.println("\n\n\n");
		} 
		
		KMeans.resultAnalyze2((LinkedList<EventNode>)eventNodes, clusters, 1, 1);
	}
	
}
