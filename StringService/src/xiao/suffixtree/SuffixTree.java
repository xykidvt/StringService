package xiao.suffixtree;


import java.util.*;
import xiao.lca.*;


public class SuffixTree extends AbstractTree{

	
	private final Set<ActiveNode> newActive = new TreeSet<ActiveNode>(new Comparator<ActiveNode>(){
    public int compare(ActiveNode n1, ActiveNode n2){
		    return n2.getKey()-n1.getKey();
	}
	});
	private final List<ActiveNode> active = new ArrayList<ActiveNode>();
	private final List<String> inputs = new ArrayList<String>();
	private Node root;
	private ActiveNode activeRoot;
	private int numInternalNodes;
	private final Set<Node> leafNodes = new HashSet<Node>();
	

	private final Set<Node> internalNodes = new HashSet<Node>();
	
	public SuffixTree(String s) {
		inputs.add(s+"$");
		root = new Node(-1,-1,-1,0);
    	activeRoot = new ActiveNode(-1,-1);
    	activeRoot.setHost(root);
    	active.add(activeRoot);
    	numInternalNodes=1;//root is an internal node
    	createSuffixTree();
    }
        
    public SuffixTree(List<String> list){
    	
    	for(String s : list){
    		inputs.add(s+"$");
    	}
    	root = new Node(-1,-1,-1,-1);
    	activeRoot = new ActiveNode(-1,-1);
    	activeRoot.setHost(root);
    	active.add(activeRoot);
    	numInternalNodes=1;//root is an internal node
    	createSuffixTree();
    }
    
    public Set<Node> getLeafNodes() {
		return leafNodes;
	}

	public Set<Node> getInternalNodes() {
		return internalNodes;
	}
	
	public List<String> getInputs(){
		return inputs;
	}
	
	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}


	//==================================================================
	/**
	 * Create the generalized suffix tree with the input string(s)
	 */
	private void createSuffixTree(){
				
		for(int i=0;i<inputs.size();i++){
			
			System.out.println("Now processing: "+inputs.get(i)); //
			
			for(int j=0;j<inputs.get(i).length();j++){
				
				System.out.println("Now adding: "+inputs.get(i).charAt(j));//
				
				showActive();
				
				for(ActiveNode n: active){
					
					System.out.println("now checking "+n);//
				    updateActive(n,j,i);
				}
				
				System.out.println("==============================================");
				 
				
				resetActive();
			}
			finishingTree(i);
		}
	}
	/**
	 * Pass all the new active nodes to active nodes for next run. activeRoot is always an active node.
	 */
	private void resetActive(){
		active.clear();
    	for(ActiveNode n:newActive)
    		active.add(n);
    	active.add(activeRoot);
    	newActive.clear();
	}
	
	
	private void showActive(){
    	System.out.print("Active nodes: ");
    	for(ActiveNode n: active)
    		System.out.print(n+ " ");
    	System.out.println();
    }
    
	/**
	 * Upon finishing the generalized suffix tree, the $ will still be active for string input
	 * other then the first one. This method will find all active nodes on $ position, add the 
	 * string index to the host
	 * @param stringIndex Current input string index
	 */
	private void finishingTree(int stringIndex){
		
		for(ActiveNode n: active){
    		if(n==activeRoot)
    			continue;
    		Node host = n.getHost();
    		host.setStringIndex(stringIndex);
    	}
		
		//for(Node n : leafNodes)
		//	finishingInternalNodes(n);
	}
	
	/**
	 * This method is no longer used. It add the string index of a leaf node to all its ancestors
	 * if they don't contain the string index(s) of this leaf node.
	 * @param leaf
	 
	private void finishingInternalNodes(Node leaf){
		Node current = leaf;
		System.out.println("Finishing leaf node: "+leaf);
		while(current!=root){
			Node parent = current.getParent();
			System.out.println("Found parent: "+parent);
			for(Integer i: current.getAllStringIndex()){
				if(!parent.getAllStringIndex().contains(i))
			         parent.setStringIndex(i);
			}
			current = parent;
		}
		
	}
	*/
	
	/**
	 * Updates the current active node
	 * @param current Current active node being updated
	 * @param incoming The index of the current char being updated
	 * @param stringIndex  The index of the current string in the inputs array
	 */
	private void updateActive(ActiveNode current, int incoming, int stringIndex){
		
		Node host = current.getHost();
		String currentString = inputs.get(stringIndex);
        char incomingChar = currentString.charAt(incoming);
        System.out.println("Updating: "+current);//
		
		if(current.isImplicit()){
			System.out.print(current+" is implicit"); //
			
			char nextChar = inputs.get(host.getFirstStringIndex()).charAt(current.getPosition()+1);
			if(nextChar == incomingChar){
				System.out.println(" has endChild");
				current.setPosition(current.getPosition()+1);
				newActive.add(current);
			}
			else{
				System.out.println(" has no endChild, split");
				split(current, incoming, stringIndex);
			}
		}
		else{
			System.out.print(current+" is explicit");
			//add the stringIndex of current active node to its host
			if(!host.getAllStringIndex().contains(stringIndex))
			         host.setStringIndex(stringIndex);
			Node endChild = searchEndChild(host, incomingChar);
			//System.out.println("End child: "+endChild);
			if(endChild == null){
				System.out.println(" has no endChild");
				//the start of a leaf node should be it's key
				//the cut of a leaf node should be incoming
				int startAndKey = (current==activeRoot)?incoming:current.getKey();
				Node newLeaf = new Node(startAndKey,currentString.length()-1,startAndKey,stringIndex);
				newLeaf.setCut(incoming);
				newLeaf.setLeaf(true);
				System.out.println("new leaf node created: "+newLeaf);
				
				newLeaf.setParent(host);
    			host.addChild(newLeaf);
    			leafNodes.add(newLeaf);
			}
			else{
				System.out.println(" has endChild "+endChild);
				int newKey = (current==activeRoot)?incoming:current.getKey();
			    ActiveNode newActiveNode = new ActiveNode(endChild.getCut(),newKey);
			    newActiveNode.setHost(endChild);
				newActive.add(newActiveNode);
			}
				
		}
			
	}
	
	/**
	 * Split the current active node's host node, create an explicit node and a leaf node
	 * @param current Current active node being updated
	 * @param incoming The index of the current char being updated
	 * @param stringIndex  The index of the current string in the inputs array
	 */
	public void split(ActiveNode current, int incoming, int stringIndex){
		
		Node host = current.getHost();
		Node parent = host.getParent();
		String currentString = inputs.get(stringIndex);
		
		//new explicit node inherit host's start and cut and string index
		Node newExplicit = new Node(host.getStart(),current.getPosition(),-1-numInternalNodes++,host.getAllStringIndex());
		newExplicit.setCut(host.getCut());
		internalNodes.add(newExplicit);
		
		Node newLeaf = new Node(current.getKey(),currentString.length()-1,current.getKey(),stringIndex);
		newLeaf.setCut(incoming);
		leafNodes.add(newLeaf);
		newLeaf.setLeaf(true);
		
		parent.addChild(newExplicit);
		parent.removeChild(host);
		newExplicit.setParent(parent);
			
	    newExplicit.addChild(host);
		host.setParent(newExplicit);
			
		newExplicit.addChild(newLeaf);
		newLeaf.setParent(newExplicit);
			
		host.setCut(current.getPosition()+1);
		
		
		//update the stringIndex in the new explicit node
		//it should inherit the stringindex of the host, and add the current string index
		if(!newExplicit.getAllStringIndex().contains(stringIndex))
	         newExplicit.setStringIndex(stringIndex);
	}
	
	/**
	 * Find and return the end child of an explicit node
	 * @param host The host node of the current active node
	 * @param incomingChar The char being updated
	 * @return The end child (the node that has the incoming char as its char at "cut" position)
	 */
	public Node searchEndChild(Node host, char incomingChar){
		
		for(Node child: host.getAllChildren()){
			char nextChar = inputs.get(child.getFirstStringIndex()).charAt(child.getCut());
			if(nextChar == incomingChar){
				//System.out.println("Found endChild: "+child);
				return child;
			}
				
		}
		return null;
	}
	//=======================================================================
	//display methods
	public void displayTree(Node current){
		for(Node n: current.getAllChildren()){
			System.out.println("Node Found: "+n);
			if(leafNodes.contains(n)){
				System.out.print("Suffix found: ");
				displaySuffix(n.getKey(),n.getFirstStringIndex());
				System.out.println();
			}
				
			else
				displayTree(n);
		}
	}
	
	public void displaySuffix(int i, int stringIndex){
		System.out.println(inputs.get(stringIndex).substring(i));
	}
	
	public void displayInternalNodes(){
		System.out.println("Ïnternal nodes:" );
		for(Node n: internalNodes)
			System.out.println(n);
	}
    //=======================================================================
	//application methods
	/*
	No longer used methods
	
	//Given a node, returns a list of all leaf nodes in the subtree rooted at this node.
	 
	public List<Node> subtreeLeafs(Node node){
		List<Node> result = new ArrayList<Node>();
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(node);
		while(queue.size()>0){
			Node current = queue.poll();
			for(Node child: current.getAllChildren()){
				 if(child.getAllChildren().size()==0)
					result.add(child);
				 else
					queue.add(child);
			}
		}
		return result;
	}
    
	
	//Given a node, tells if its children covers all inputs
    public boolean isCommonAncestor(Node node){
		boolean[] result = new boolean[inputs.size()];
	    for(Node leaf: subtreeLeafs(node)){
	    	for(Integer i : leaf.getAllStringIndex())
	    		result[i] = true;
	    }
	    for(boolean b : result)
	    	if(b==false)
	    		return false;
	    return true;
	}
	*/

	/**
	 * Determine if the given node is a path of all input strings in the suffix tree
	 * @param n Current node
	 * @return  True if it has string indexes of all input strings
	 */
	public boolean isCommonAncestor(Node n){
		for(int i=0;i<inputs.size();i++)
			if(!n.getAllStringIndex().contains(i))
				return false;
		return true;
	}
	
	
	public static void main(String[] args) {
		List<String> input = new ArrayList<String>();
		input.add("abab");
		input.add("bcbc");
		SuffixTree st2 = new SuffixTree(input);
		st2.displayTree(st2.root);
        st2.displayInternalNodes();
	}

}

