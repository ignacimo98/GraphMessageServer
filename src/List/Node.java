package List;

/**
 * Created by nachomora on 11/27/16.
 */
public class Node {
    private String value;
    private Node next;

    public Node(String value){
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public String getValue() {
        return value;
    }
}
