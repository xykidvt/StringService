package xiao.suffixtree;

import java.util.*;
import xiao.lca.*;


public class Node extends GeneralNode {
    
	private final int start;
	private final int end;
	private int cut;
	private final List<Integer> stringIndex = new ArrayList<Integer>();
	private int key;
	private final List<Node> children = new ArrayList<Node>();
	private Node parent;
	private boolean visited;
	private static int id;
	private boolean isLeaf;
	private int preorder;
	
	

	public Node(int start, int end, int key, int i) {
		this(start, end, key, new ArrayList<Integer>());
		this.stringIndex.add(i);
	}
	
	public Node(int start, int end, int key, List<Integer> list) {
		super.setI(this);
		this.start = start;
		this.end = end;
		this.key = key;
		for(Integer i: list)
			this.stringIndex.add(i);
		this.cut = start;
		id++;
	}
	
	public boolean isLeaf(){
		return this.isLeaf;
	}
	
	public void setLeaf(boolean b){
		this.isLeaf = b;
	}
	
	public boolean isVisited(){
		return visited;
	}
	public void setVisited(boolean b){
		visited = b;
	}
	
	public int getPreorder() {
		return preorder;
	}

	public void setPreorder(int preorder) {
		this.preorder = preorder;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getCut() {
		return cut;
	}

	public List<Integer> getAllStringIndex() {
		return stringIndex;
	}
	
	public int getFirstStringIndex(){
		return stringIndex.get(0);
	}

	public int getKey() {
		return key;
	}

	public List<Node> getAllChildren() {
		return children;
	}

	public Node getParent() {
		return parent;
	}

	public void setCut(int cut) {
		this.cut = cut;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setStringIndex(int i){
		this.stringIndex.add(i);
	}
	
	public void removeChild(Node child){
		children.remove(child);
	}
	public void addChild(Node child){
		children.add(child);
	}
    //================================================
	@Override public String toString(){
		return "("+stringIndex+")"+key+"["+cut+", "+end+"]"+"{"+start+", "+end+"}";
	}
	@Override public int hashCode(){
		return id;
	}
	
	@Override public boolean equals(Object o){
		return this.key==((Node)o).key;
	}
}


