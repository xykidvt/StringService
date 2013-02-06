package xiao.suffixtree;


class ActiveNode {
	
     private int key;
     private Node host;
     private int position;
     
     
    protected  ActiveNode(int position, int key){
    	this.position = position;
    	this.key = key;
    }
    
	protected int getKey() {
		return key;
	}
	protected Node getHost() {
		return host;
	}
	protected int getPosition() {
		return position;
	}
	protected void setKey(int key) {
		this.key = key;
	}
	protected void setHost(Node host) {
		this.host = host;
	}
	protected void setPosition(int position) {
		this.position = position;
	}
	//=========================================================
	protected boolean isImplicit() {
		return this.position != host.getEnd();
	} 
    @Override public String toString(){
    	return "("+key+")"+"["+position+"]";
    }
}

