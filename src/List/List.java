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

    public Node remove(String value){
        Node temp = this.head;
        if (this.head.getValue().equals(value)){
            this.head = temp.getNext();
        }else{
            while (temp != null){

            }
        }
        return temp;

    }

    @Override
    public String toString() {
        String result = "[";
        if (this.head == null){
            return result +']';
        }
        Node temp = this.head;
        while (temp.getNext() != null){
            result+=temp.getValue()+", ";
            temp = temp.getNext();
        }
        result += temp.getValue() + "]";
        return result;
    }
}
