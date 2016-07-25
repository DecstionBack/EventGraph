package net.graph.process;

//Edge类表示边的属性，包括无向边或者有向边，而且只需要记录指向的点就可以了，因为起点是已知的
public class Edge {  
	private EventNode toNode;
	public enum Type{
		DIRECTED_EDGE,
		UNDIRECTED_EDGE
	}
	private Type edgeType;
	
	public EventNode getToNode(){
		return toNode;
	}
	public void setToNode(EventNode toNode){
		this.toNode = toNode;
	}
	public Type getEdgeType(){
		return edgeType;
	}
	public void setEdgeType(Type edgeType){
		this.edgeType = edgeType;
	}
}
