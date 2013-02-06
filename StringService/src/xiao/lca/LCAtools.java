package xiao.lca;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LCAtools {
    
	/**
	 * 
	 * @param i input integer
	 * @return  the position of the leftmost 1 bit
	 */
	 public static int height(int i){
		 int position = 0;
		 while(i>0){
			 if((i&1)==1)
				 break;
			 else{
				 i>>=1;
				 position++;
			 }
		 }
		 return position;
	 }
	 
	 /**
	 * 
     * @param i input integer
	 * @return  the position of the rightmost 1 bit
	 */
	 public static int depth(int i){
		 if(i<=0)
			 return 0;
		 else
		 return (int)(Math.log10(i)/Math.log10(2));
	 }
	 
	 /**
	  * 
	  * @param binary Input binary
	  * @param k Position k on the input binary from left to right 
	  * @return The position of first 1-bit to the left of the input binary's position k
	  */
	 public static int minRight(int binary, int k){
		 int result = k;
		 int range = (int)(Math.log10(binary)/Math.log10(2)+1);
		 binary = binary>>k;
		 for(int i=1;i<=range-k;i++){
			 result++;
			 if(binary%2==1)
				 break;
			 else
				 binary = binary>>1;
		 }
		 return result;
	 }
	 
	 /**
	  * Find and return The lowest common ancestor of x and y in a complete binary tree (in-order numbered).
	  * @param x Input Node in-order numbered x
	  * @param y Input Node in-order numbered y
	  * @return The lowest common ancestor of x and y in a complete binary tree (in-order numbered).
	  */
	 public static int LCAinB(int x, int y){
		 int higherNode = height(x)>height(y)?x:y;
		 int temp = x^y;
		 int shift = Math.max(depth(temp), height(higherNode));
		 return shift(higherNode,shift);
	 }
	 
	 /**
	  * The new binary equals input binary shift right s, set the last position 1, then shift left s. This is the lca
	  * of node x and y in the complete binary tree.
	  * @param x  The input binary
	  * @param s  The shift
	  * @return   The new binary equals input binary shift right s, set the last position 1, then shift left s
	  */
	 public static int shift(int x, int s){
		 x = x>>s;
		 x=x|1;
		 return x<<s;
	 }
	 //======================================================================================
	 public static void preProcessTree(RootedTree t){
		 if(t.getRoot()==null){
			 System.out.println("Empty tree");
			 return;
		 }
		 System.out.println("Preprocess Tree : ");
		 
		 System.out.print("Setting up the preorder numbers ..... ");
		 preorderNum(t.getRoot(),1);
		 System.out.print("Done");
	     System.out.println();
		 
	     System.out.print("Generating I value ..... ");
		 generateI(t.getRoot());
		 System.out.print("Done");
		 System.out.println();
		 
		 System.out.print("Generating A value ..... ");
		 generateA(t.getRoot());
		 System.out.print("Done");
		 System.out.println();
		 
		 System.out.println("Preprocess finishsed.");
	 }
	 /**
		 * Number each node in a depth-first manner.
		 * @param n Root of the tree
		 * @param num Preorder number
		 * @return  
		 */
		private static int preorderNum(GeneralNode n, int num){
			
			  int i =num;
			  n.setPreorder(i);
		      for(GeneralNode child:n.getAllChildren()){
		    	  
		    	  i = preorderNum(child,i+1);
		      }
		      return i;
		}
	    
		/**
		 * I is the reference to the node in its subtree that has the maximum height for preordered number
		 * @param root Root of the tree
		 */
		public static void generateI(GeneralNode root){
			
			if(root==null)
				return;
			for(GeneralNode leaf : getLeafNodes(root)){
				GeneralNode current = leaf;
				//int currentH = height(current.getPreorder());
				while(current!=null){
					GeneralNode parent = current.getParent();
					if(parent==null)
						break;
					if(height(current.getI().getPreorder())>height(parent.getI().getPreorder()))
						parent.setI(current.getI());
					current = parent;
				}
			}
		}
		
		/**
		 * A is the number of distinct run encountered on the path from root to current (inclusive).
		 * @param current Current node
		 */
		public static void generateA(GeneralNode current){
			int maxH = height(current.getI().getPreorder());
			if(current.getParent()==null)
				current.setA(1<<(maxH-1));
			else{
				int parentA = current.getParent().getA();
				current.setA(parentA|1<<(maxH-1));	
			}
			for(GeneralNode child: current.getAllChildren()){
                generateA(child);
			}
		}
		//==========================================================================
		/**
		 * Find and return all the leaf nodes in the tree with BSF.
		 * @return A List containing all the leaf nodes in the tree
		 */
		public static List<GeneralNode> getLeafNodes(GeneralNode n){
			List<GeneralNode> leafNodes = new ArrayList<GeneralNode>();
			LinkedList<GeneralNode> queue = new LinkedList<GeneralNode>();
			queue.add(n);
			while(queue.size()>0){
				GeneralNode current = queue.poll();
				if(current.getAllChildren().size()==0)
					leafNodes.add(current);
				else
					for(GeneralNode child:current.getAllChildren())
						queue.add(child);
			}
			return leafNodes;
		}
	 
	 
	 public static void main(String[] args){
		 //System.out.println(depth(2));
		 //System.out.println(minRight(9,1));
		 //System.out.println(LCAinB(7,7));
		 //for(int i=0;i<10;i++)
			 //System.out.println(i+": "+depth(i));
		 for(int i=1;i<9;i++)
			 for(int j=1;j<9;j++){
				 System.out.println(i+"+"+j+": "+LCAinB(i,j));
			 }
	 }
}
