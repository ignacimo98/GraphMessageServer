package Graph;

/**
 * Created by nachomora on 11/28/16.
 */
public class Edge{
    public Device one;
    public Device two;
    public int weight;

    public Edge(Device one, Device two, int weight) {
        this.one = one;
        this.two = two;
        this.weight = weight;
    }

    public Edge(Device one, Device two){
        this(one, two, 1);
    }


    public boolean compareTo(Edge edge) {
        if (this.one == edge.one && this.two == edge.two && this.weight == edge.weight ) {
            return true;
        }
        else{
            return false;
        }
    }
}
