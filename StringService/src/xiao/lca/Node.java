package xiao.lca;


public class Node extends GeneralNode {

	//private int A;   //the A(node)
	//private int preorder;
	//private int key;
	//private Node parent;
	//private final List<Node> children = new ArrayList<Node>();
	private static int count = 0;
	//private Node I;    //the I()
	
	Node(int key){
		super.setKey(key);
		super.setI(this);
	}
	
	@Override public int hashCode(){
		return count;
	}

}
