package BTree;
import List.List;
import List.Node;

import static BTree.BadWordsList.getBadWordsList;
import static BTree.BadWordsList.setBadWordsList;

/**
 * Created by andre on 25/11/2016.
 * Project: TEC
 * Using IntelliJ
 */

/**
 *  The {@code BTree} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>size</em>, and <em>is-empty</em> methods.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a B-tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>get</em>, <em>put</em>, and <em>contains</em> operations
 *  each make log<sub><em>numChildren</em></sub>(<em>key_value</em>) probes in the worst case,
 *  where <em>key_value</em> is the number of key-value pairs
 *  and <em>numChildren</em> is the branching factor.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://algs4.cs.princeton.edu/62btree">Section 6.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BTree<Key extends Comparable<Key>, Value>{

    // max children per B-tree node = Order-1
    // (must be even and greater than 2)
    private static final int Order = 5;

    private Entry<Entry[]> root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int key_value;           // number of key-value pairs in the B-tree

    //Entry
    private static class Entry <T> {

        private int numChildren = 0;
        private T children = null;

        private Comparable key;
        private final Object value;
        private String time;

        public Entry(){
            this.key = null;
            this.value = null;
        }

        public Entry (Comparable key, Object value, String time) {

            this.key = key;
            this.value = value;
            this.time = time;
        }


        public int getNumChildren() {
            return numChildren;
        }

        public void setNumChildren(int numChildren) {
            this.numChildren = numChildren;
        }
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree() {
        root  = new Entry<Entry[]>();
        root.children = new Entry[Order + 1];
    }

    //Methods of the BTree

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param key the key
     * @param value the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     * @throws IllegalArgumentException if {@code key} && {@code value} are already in the tree
     * @throws IllegalArgumentException if {@code value} contains bad words
     */
    public void put(Key key, Value value, String time) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        if (isMessageSaved(key, value, root.children, root.getNumChildren())) throw new IllegalArgumentException("Don't do spam!");
        if (banHammer((String) value)) throw new IllegalArgumentException("Don't say bad words");
        insert(root, root.children, key, value, time);
        key_value++;

        // rebalance root if necessary
        reBalanceRoot();
    }

    /**
     * Puts the new Entry in the deepest part of the tree according to the key and the value
     * @param rooty the array father / the first entry depending of the recursive level
     * @param children the array where to look if the new Entry can be there
     * @param key key
     * @param value value
     */
    private void insert(Entry<Entry[]> rooty, Entry<Entry[]>[] children, Key key, Value value, String time) {
        int j = 0;

        try {
            while (!less(key, children[j].key)) {
                j++;
            }
        } catch (NullPointerException e){}

        if (children[j] == null || children[j].children == null) {
            for (int i = 1; !(Order - j == i); i++) {
                children[Order - i] = children[Order - 1 - i];
            }
            children[j] = new Entry<Entry[]>(key, value, time);
            rooty.setNumChildren(rooty.getNumChildren() + 1);
        } else {
            insert(children[j], children[j].children, key, value, time);
        }

        if (children[j].getNumChildren() == Order) {
            split(children[j].children, children, j);
        }
    }

    /**
     * Split the array in a mini-BTree
     * @param children the array to split
     * @param rooty the array where the Entry that ascend will be
     * @param index the position of the array where to split
     */
    private void split(Entry<Entry[]>[] children, Entry<Entry[]>[] rooty, int index) {

        Entry<Entry[]> halfEntry = children[Order / 2];
        Entry<Entry[]>[] theChildren = theChildren(children);
        Entry<Entry[]>[] theOtherChildren = theOtherChildren(children);
        for (int i = 1; !(Order - index == i - 1); i++){
            rooty[Order + 1 - i] = rooty[Order - i];
        }
        rooty[index + 1].children = theOtherChildren;
        rooty[index + 1].setNumChildren(arrayCount(theOtherChildren));
        rooty[index] = halfEntry;
        rooty[index].children = theChildren;
        rooty[index].setNumChildren(arrayCount(theChildren));
    }

    /**
     * Puts the first items of the string in an array
     * @param children array of the items
     * @return Entry[] with the object that are before the half entry
     */
    private  Entry[] theChildren(Entry<Entry[]>[] children){
        Entry<Entry[]>[] theChildren = new Entry[Order + 1];
        for (int j = 0; j < Order / 2; j++){
            theChildren[j] = children[j];
        }
        if (children[Order / 2].children != null) {
            theChildren[Order / 2] = new Entry<>();
            theChildren[Order / 2].children = children[Order / 2].children;
        }

        return  theChildren;
    }

    /**
     * Puts the rest of the items in the string in an array
     * @param children array of the items
     * @return Entry[] with the object that are after the half entry
     */
    private Entry[] theOtherChildren(Entry<Entry[]>[] children){
        Entry<Entry[]>[] theOtherChildren = new Entry[Order + 1];
        int adjustment;
        adjustment = Order % 2;
        for (int j = 0; j < Order / 2 + adjustment; j++){
            theOtherChildren[j] = children[Order / 2 + 1 + j];
        }
        return theOtherChildren;
    }

    /**
     * Rebalance the root if the number of children is the same as the order of the tree
     */
    private void reBalanceRoot (){
        if (root.getNumChildren() == Order){
            Entry<Entry[]>[] newRoot = new Entry[Order + 1];

            newRoot[0] = root.children[Order / 2];
            newRoot[0].children = theChildren(root.children);
            newRoot[0].setNumChildren(arrayCount(newRoot[0].children));

            newRoot[1] = new Entry<>();
            newRoot[1].children = theOtherChildren(root.children);
            newRoot[1].setNumChildren(arrayCount(newRoot[1].children));

            root.children = newRoot;
            root.setNumChildren(arrayCount(root.children));
            height += 1;
        }
    }

    /**
     * Gets the number of children an array has
     * @param children array of children
     * @return int with the number of children
     */
    private int arrayCount (Entry<Entry[]>[] children){
        int j = 0;
        try{
            while (children[j].key != null){
                j++;
            }
        } catch (NullPointerException e){}
        return j;
    }

    /**
     * Compare two objects
     * @param k1 first object
     * @param k2 second object
     * @return true if the first item is less than the second
     */
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }





    //Search Methods

    /**
     * Returns the value associated with the given key.
     *
     * @param search the search item
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public String search(String search) {
        if (search == null) throw new IllegalArgumentException("argument to get() is null");
        String people = removeLastChar(search4Person((Key) search, root.children, root.getNumChildren()));
        String messages = removeLastChar(search4Message(search, root.children, root.getNumChildren()));
        return ("[{'Messages': [" + people + "]}, {'People': [" + messages + "]}]") ;
    }

    /***
     * Search for the messages of the person in the tree
     * @param search person to search for
     * @param children tree where to search
     * @param numChildren number of children the tree has
     * @return JSON format of all the messages the {@code search} person has send
     */
    private String search4Person(Key search, Entry<Entry[]>[] children, int numChildren) {

        String megaString = "";

        for (int j = 0; j <= numChildren; j++){
            try {
                if (children[j].key == search) {
                    megaString += ("{'Message': '" + children[j].value + "', 'Time': '" + children[j].time + "'},");
                }

                if (children[j].children != null) {
                    megaString += search4Person(search, children[j].children, children[j].getNumChildren());
                }
            } catch (NullPointerException e) {}
        }

        return megaString;
    }

    /**
     * Searches for a message in the tree
     * @param search string to search for
     * @param children tree where to search
     * @param numChildren number of children the tree has
     * @return JSON format of all the messages with the {@code search} in it
     */
    private String search4Message(String search, Entry<Entry[]>[] children, int numChildren) {

        String megaString = "";

        for (int j = 0; j <= numChildren; j++){

            try{
                if (children[j].children != null){
                    megaString += search4Message(search, children[j].children, children[j].getNumChildren());
                }

                String aux1 = (String) children[j].value;
                String aux2 = aux1.replaceFirst("(?i)" + search, "");

                if (aux1 != aux2){
                    megaString = ("{'Person': '" + children[j].key + "', 'Time': '" + children[j].time + "'},");
                }
            } catch (NullPointerException e){}
        }

        return megaString;
    }

    /**
     * Says if the message is already saved in the tree
     * @param value text to verify
     * @param key person to check
     * @param children tree where to look for
     * @param numChildren number of children the tree has
     * @return {@code false} if the message isn't in the tree, {@code true} otherwise
     */
    private boolean isMessageSaved (Key key, Value value, Entry<Entry[]>[] children, int numChildren){

        boolean ToF = false;

        for (int j = 0; j <= numChildren && !(ToF); j++){
            try {
                if (children[j].value == value && children[j].key == key) {
                    return true;
                } else if (children[j].children != null) {
                    ToF = isMessageSaved(key, value, children[j].children, children[j].getNumChildren());
                }
            } catch (NullPointerException e){}
        }
        return ToF;
    }

    /**
     * Says if the text has bad words in it
     * @param evaluate string to evaluate
     * @return {@code true} is it has bad words, {@code false} otherwise
     */
    public boolean banHammer (String evaluate){

        setBadWordsList();
        Node badWordsList = getBadWordsList().getHead();

        boolean ToF = false;
        String aux;

        while (badWordsList != null){

            aux = evaluate.replaceFirst(badWordsList.getValue(), "");

            if (evaluate != aux){
                ToF = true;
                break;
            }

            badWordsList = badWordsList.getNext();
        }
        return ToF;
    }

    /**
     * Removes the last coma for the JSON format
     * @param string string
     * @return {@code string} without ","
     */
    public String removeLastChar(String string) {
        if (string != null && string.length() > 0 && string.charAt(string.length()-1)==',') {
            string = string.substring(0, string.length()-1);
        }
        return string;
    }
}
