//package net.graph.process;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.*;
//
//import net.graph.process.Edge.Type;
//
//public class MVAG {
//	public static float similarityThreshold = (float) 0.2;
//	public static Integer recallsum = 0;
//	public static float maxprec = 0;
//	public static float maxrecall = 0;
//	public static float maxF1 = 0;
//	public static float precsimilarity = 0;
//	public static float recallsimilarity = 0;
//	public static float F1similarity = 0;
//	private static Integer getRecallsum(ArrayList<EventNode> eventNodes){
//		HashMap<Integer, Integer> articleclassnum = new HashMap<Integer, Integer>();
//		Integer recallsum = 0;
//	    for (EventNode node : eventNodes){
//	    	Integer topicsID = Integer.parseInt(node.getTitle().split(":")[0]);
//	    	if (!articleclassnum.containsKey(topicsID))
//	    		articleclassnum.put(topicsID, 1);
//	    	else articleclassnum.put(topicsID, articleclassnum.get(topicsID) + 1); 
//	    }
//	    for (Integer key : articleclassnum.keySet())
//	    	recallsum += articleclassnum.get(key) * (articleclassnum.get(key) - 1) / 2;
//	    return recallsum;
//	}
//	private static void build(ArrayList<EventNode> eventNodes) throws IOException{
//		//根据相似性建立无向边
//				for (EventNode node1 : eventNodes){
//					for (EventNode node2 : eventNodes){
//						if (node1 != node2 && !node1.getEdge().contains(node2)){
//							if (cosSim(node1.getTfidfVector(), node2.getTfidfVector()) > similarityThreshold){
//							//	System.out.println(cosSim(node1.getTfidfVector(), node2.getTfidfVector()));
//								Edge edge = new Edge();
//								edge.setEdgeType(Type.UNDIRECTED_EDGE);
//								edge.setToNode(node2);
//								node1.getEdge().add(edge);
//								Edge edge2 = new Edge();
//								edge2.setEdgeType(Type.UNDIRECTED_EDGE);
//								edge2.setToNode(node1);
//								node2.getEdge().add(edge2);
//							}
//						}
//					}
//				}
//				
//				File file = new File("graph-" + similarityThreshold + ".txt");
//				FileWriter fileWriter = new FileWriter(file, true);
//				BufferedWriter buffer = new BufferedWriter(fileWriter);
//				
//				Integer precnum = 0, queuesize = 0, precsum = 0;
//				HashMap<Integer, Integer> precmap = new HashMap<Integer, Integer>();
//				
//				//直接输出图的聚类结果,类似于广度遍历
//				boolean[] visited = new boolean[eventNodes.size()];
//				for (int i = 0; i < visited.length; i++)
//					visited[i] = true;
//				Queue<EventNode> queue = new LinkedList<EventNode>();
//				for (EventNode node : eventNodes){
//					if (!visited[eventNodes.indexOf(node)])
//						continue;
//					queue.add(node);
//					visited[eventNodes.indexOf(node)] = false;
//					while(!queue.isEmpty()){
//						EventNode top = queue.poll();
//						System.out.println(top.getTitle());
//						queuesize++;
//						Integer key = Integer.parseInt(top.getTitle().split(":")[0]);
//						if (!precmap.containsKey(key))
//							precmap.put(key, 1);
//						else precmap.put(key, precmap.get(key) + 1);
//						
//						buffer.write(top.getTitle() + "\r\n");
//						buffer.newLine();
//						for (Edge edge : top.getEdge()){
//							if (!queue.contains(edge.getToNode()) && visited[eventNodes.indexOf(edge.getToNode())]){
//								queue.add(edge.getToNode());
//								visited[eventNodes.indexOf(edge.getToNode())] = false;
//							    }
//							}
//						}
//					for (Integer key : precmap.keySet())
//						precnum += precmap.get(key) * (precmap.get(key) - 1) / 2;
//					precsum += queuesize * (queuesize - 1) / 2;
//					
//					System.out.println("\n\n\n\n");
//					buffer.write("\r\n");
//					buffer.write("\r\n");
//					buffer.newLine();
//				}
//				float prec = (float) (precnum * 1.0 / precsum);
//				if (prec > maxprec){
//					maxprec = prec;
//					precsimilarity = similarityThreshold;
//				}
//				float recall = (float) (precnum * 1.0 / recallsum);
//				if (recall > maxrecall){
//					maxrecall = recall;
//					recallsimilarity = similarityThreshold;
//				}
//				float F1 = 2 * prec * recall / (prec + recall);
//				if (F1 > maxF1){
//					maxF1 =  F1;
//					F1similarity = similarityThreshold;
//				}
//				buffer.write("prec:" + prec + " recall:" + recall + " F1:" + F1 + "\r\n");
//				buffer.close();
//				fileWriter.close();
//	}
//	public void buildGraph(LinkedList<EventNode> nodes) throws IOException{
//		//把eventNodes转换为ArrayList便于取位置
//		ArrayList<EventNode> eventNodes = new ArrayList<EventNode>();
//		for (EventNode node : nodes){
//			eventNodes.add(node);
//		}
//		recallsum = getRecallsum(eventNodes);
//		for (similarityThreshold = (float) 0.2; similarityThreshold < 0.81; similarityThreshold += 0.05){
//			build(eventNodes);
//		}
//		File file = new File("result.txt");
//		FileWriter fileWriter = new FileWriter(file, true);
//		BufferedWriter buffer = new BufferedWriter(fileWriter);
//		buffer.write("maxprec:" + maxprec + " similarityThreshold=" + precsimilarity + "\r\n");
//		buffer.write("maxrecall:" + maxrecall + " similarityThreshold=" + recallsimilarity + "\r\n");
//		buffer.write("maxF1:" + maxF1 + " similarityThreshold=" + F1similarity + "\r\n");
//		buffer.close();
//	}
//	
//	//计算两个HashMap<Integer, Float>之间的距离
//	private static float cosSim(HashMap<Integer, Float> a, HashMap<Integer, Float> b){
//    	float dotp=0, maga=0, magb=0;
//    	for (Integer pos : a.keySet()){
//    		//a和b中都有的词语才相互计算乘积
//    		if (b.get(pos) != null)
//    			dotp += a.get(pos)*b.get(pos);
//    		maga += Math.pow(a.get(pos),2);
//    	}
//    	for (Integer pos : b.keySet())
//    		magb += Math.pow(b.get(pos),2);
//    	float d = (float) (dotp / (Math.sqrt(maga) * Math.sqrt(magb)));
//    	return d==Float.NaN?0:d;
//    }
//
//	public static void main(String[] args){
//		
//	}
//}
