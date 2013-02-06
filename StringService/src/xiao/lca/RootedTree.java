package xiao.lca;

import java.util.*;

public class RootedTree extends AbstractTree {

	private int size;
	
	public GeneralNode getRoot(){
		return super.getRoot();
	}
	
	public void setRoot(GeneralNode n){
		super.setRoot(n);
	}
	/**
	 * Return the number of nodes in the tree.
	 * @return Size of the tree
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Add a node to the parent specified with the unique key
	 * @param key Key of the node to be added
	 * @param parentKey Key of the parent
	 * @return True if parent is found and new node is added
	 */
	public boolean add(int key, int parentKey){
		GeneralNode newNode = new Node(key);
		if(super.getRoot() == null)
			super.setRoot(newNode);
		else{
			GeneralNode parent = find(super.getRoot(),parentKey);
			if(parent!=null){
				parent.addChild(newNode);
			    newNode.setParent(parent);
			    size++;
			}
			else{
				return false;
			}	
		}
		return true;
	}
	
	/**
	 *  Find and return the node with the given key.
	 * @param n Input node
	 * @param key The key to find
	 * @return The node with the input key
	 */
	private GeneralNode find(GeneralNode n, int key){
		if(n.getKey()==key)
			return n;
		GeneralNode found = null;
		for(GeneralNode child : n.getAllChildren()){
			found = find(child,key);
		}
		return found;
	}

	/**
	 * Display the Rooted tree, nodes in each level is group in a row.
	 */
	public void displayTree(){
		if(getRoot()==null)
			return;
		LinkedList<GeneralNode> queue = new LinkedList<GeneralNode>();
		queue.add(getRoot());
		boolean isDone = false;
		while(!isDone){
			isDone = true;
			LinkedList<GeneralNode> children = new LinkedList<GeneralNode>();
			while(queue.size()>0){
			   GeneralNode current = queue.poll();
			   displayNode(current);
			   if(current.getAllChildren().size()>0){
				   isDone = false;
				   for(GeneralNode child: current.getAllChildren())
					   children.add(child);
			   }    
			} 
			System.out.println();
			while(children.size()>0)
				queue.add(children.poll());
		}
	}
	
	private void displayNode(GeneralNode current){
		   System.out.print(current+"[P:");
		   System.out.print((current.getParent()==null?"* ":current.getParent())+"pN:");
		   System.out.print(current.getPreorder()+" I:");
		   System.out.print(current.getI()+" A:");
		   System.out.print(current.getA()+"]   ");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RootedTree t = new RootedTree();
		t.add(1,-1);
		t.add(2,1);
		t.add(3,1);
		t.add(4,3);
		t.add(5,3);
		t.add(6,3);
		t.add(7,6);
		t.add(8,6);
		//System.out.println("leaf nodes: "+LCAtools.getLeafNodes(t.getRoot()));
		LCAtools.preProcessTree(t);
		t.displayTree();
		//System.out.println(t.getLeafNodes());
		
	}

}
