package List;

/**
 * Created by nachomora on 11/27/16.
 */
public class List {
    private Node head = null;

    public List(){

    }

    public void insertFirst(String value){
        Node newNode = new Node(value);
        newNode.setNext(head);
        head = newNode;
    }

    public Node getHead() {
        return head;
    }

    public boolean isIn(String value){
        boolean result = false;
        Node temp = this.head;
        while (temp != null){
            if (temp.getValue().equals(value)){
                result = true;
                break;
            } else{
                temp = temp.getNext();
            }
        }
        return result;
    }
}
