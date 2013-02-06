package xiao.lca;

import java.util.*;

public abstract class GeneralNode extends AbstractNode{
      
	
	
	private int key;
	private final List<GeneralNode> children = new ArrayList<GeneralNode>();
	private GeneralNode parent;
	
	private GeneralNode I; //the node in subtree with maximum height(preorder)
	private int A;  //
	private int preorder;  //the preorder number of this node
	  
	public GeneralNode getI() {
		return I;
	}

	public void setI(GeneralNode i) {
		I = i;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public GeneralNode getParent() {
		return parent;
	}

	public void setParent(GeneralNode parent) {
		this.parent = parent;
	}
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getPreorder() {
		return preorder;
	}

	public void setPreorder(int preorder) {
		this.preorder = preorder;
	}

	public List<? extends GeneralNode> getAllChildren(){
		return children;
	}
	
	public void addChild(GeneralNode n){
		children.add(n);
	}
	
	@Override public String toString(){
		return key+" ";
	}
	@Override public boolean equals(Object n){
		if(!(n instanceof Node))
			return false;
		return this.key == ((GeneralNode)n).key;
	}
}
