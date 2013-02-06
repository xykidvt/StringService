package xiao.services;

import java.util.*;
import xiao.suffixtree.*;

public class StringServices {
     
	//====================================================
	//find longest palindrome of a string
	public static List<String> LP(String s){
		String reverse = reversed(s);
		return LCS(s,reverse);
	}
	public static String reversed(String s){
		char[] c = s.toCharArray();
		int left =0;
		int right = s.length()-1;
		while(left<right){
			c=swap(left++,right--,c);
		}
		return new String(c);
	}
	public static char[] swap(int i, int j, char[] c){
		char temp = c[i];
		c[i]=c[j];
		c[j]=temp;
		return c;
	}
	//======================================================
	/**
	 * Find the longest common substring of the input strings
	 * @param args Input strings
	 * @return  Longest common substring of the input strings
	 */
	public static List<String> LCS(String... args){
		List<String> input = new ArrayList<String>();
		for(String s: args)
			input.add(s);
        return LCS(input);
	}
	
	/**
	 * Find the longest common substring of the input strings. The principle is to find
	 * the shared path of all input strings with maximum length.
	 * @param input Input strings
	 * @return  Longest common substring of the input strings
	 */
	public static List<String> LCS(List<String> input){
		if(input.size()==1)
        	return null;
		SuffixTree st = new SuffixTree(input);
		//find the longest path of internal nodes
		List<Node> commonNodes = new ArrayList<Node>();
		List<String> result = new ArrayList<String>();
	
		for(Node n: st.getInternalNodes()){
			//System.out.println("checking internal node: "+n);
			if(st.isCommonAncestor(n)){
				commonNodes.add(n);	
				//System.out.println("common ancestor added: "+n);
			}		
		}
		for(Node n: st.getLeafNodes()){
			if(st.isCommonAncestor(n)){
				commonNodes.add(n);	
				//System.out.println("common ancestor added: "+n);
			}	
		}		
		//find the longest common ancestor
		List<Node> lcs = maxLength(commonNodes);
		
	    for(Node n: lcs){
	    	//leaf nodes need to get rid of "$"
	    	if(n.isLeaf()==true)
			   result.add(st.getInputs().get(0).substring(n.getStart(),n.getEnd()));
	    	else
	    		result.add(st.getInputs().get(0).substring(n.getStart(),n.getEnd()+1));
	    }
			return result;
		}
		    
	
	/**
	 * Find and return the Nodes with the maximum path length in the given collection of nodes
	 * @param it The collection of Nodes
	 * @return The Nodes with the maximum path length
	 */
	public static List<Node> maxLength(Iterable<Node> it){
		if(it == null)
			return null;
		List<Node> result = new ArrayList<Node>();
	
		int max=0;
		for(Node n: it){
			int length = n.getEnd()-n.getStart();
			if(length>max){
				max = length; 
				
			}	
		}
		for(Node n: it){
			int length = n.getEnd()-n.getStart();
			if(length == max)
				result.add(n);
		}
		return result;
	}
	//========================================================================
    /**
     * Find and return the longest repeat sequence in a string. 
     * @param s The input string
     * @return  The longest repeat sequence in a string
     */
	public static List<String> longestRepeat(String s){
		
		SuffixTree st = new SuffixTree("$"+s);
		List<Node> list = new ArrayList<Node>();
		
		for(Node n: st.getInternalNodes()){
			if(diverseNode(st, n)){
				list.add(n);
			}
		}
		List<Node> longestRepeats = maxLength(list);
		List<String> result = new ArrayList<String>();
		
		if(longestRepeats == null)
			return null;
		else{
			for(Node repeat: longestRepeats){
				result.add(st.getInputs().get(0).substring(repeat.getStart(),repeat.getEnd()+1));
			}
		}
		   return result;
	}
	/**
	 * Return True if all children of the given node are leaf nodes and have different left character.
	 * Left character is the character left to the start of the given node.
	 * @param st The suffix tree
	 * @param n The input node
	 * @return True if all children of the given node are leaf nodes and have different left character
	 */
	private static boolean diverseNode(SuffixTree st, Node n){
		Set<Character> set = new HashSet<Character>();
		for(Node node: n.getAllChildren()){
			    //System.out.println("checking child "+node+" of node "+n);
				if(n.isLeaf()==false)
					return false;
				else{
					if(set.contains(st.getInputs().get(0).charAt(node.getKey()-1))) return false;
					else set.add(st.getInputs().get(0).charAt(node.getKey()-1));			
				}
		}
		return true;
	}
	//=========================================================
	public static void main(String[] args){
		//List<String> result = LCS("ryutabab","ryuabab","abstabab");
		
		//List<String> result = longestRepeat("xxtsxtffxtftfxtsds");
		List<String> result = StringServices.LP("xiaotoaix");
		
	    System.out.println("Result: "+result);
	
		
		//System.out.println(reversed(input));
		
	}
}

