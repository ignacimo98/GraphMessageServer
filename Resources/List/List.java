package Resources.List;

/**
 * Created by nachomora on 11/27/16.
 */
public class List {
	
	private Node head;
	
	public List(){
		this.head = null;
	}
	
	public void insertFirst(String value){
		Node newNode = new Node(value);
		newNode.setNext(head);
		head = newNode;
	}
	
	public Node getHead() {
		return head;
	}
	
	public void setHead(Node head) {
		this.head = head;
	}
}
