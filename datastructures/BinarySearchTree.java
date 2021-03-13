// CS310-01
// Drake Nguyen cssc1246
// 4/27/2020
// Program 4 BinarySearchTree.java

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

@SuppressWarnings("unchecked")

public class BinarySearchTree<K, V> implements DictionaryADT<K, V> {


    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> leftChild, rightChild;    //the node may only have two children, left or right

        public Node(K key, V value){
            this.key = key;
            this.value = value;
            leftChild = rightChild = null;
        }
    }

    private Node<K,V> root;     //call it root because it must have a root to be a tree
    private int currSize;
    private long modCounter;

    public BinarySearchTree(){
        root = null;
        currSize = 0;
        modCounter = 0;
    }

    // Adds the given key/value pair to the dictionary. Returns
    // false if the dictionary is full, or if the key is a duplicate.
    // Returns true if addition succeeded.
    public boolean put(K key, V value){
        if(find(key,root) != null)      //if the tree contains the key, return false
            return false;

        if(root == null)
            root = new Node<K,V>(key, value);   //if the tree is empty, insert at the root
        else   
            insert(key, value, root, null, false);  //recursively insert at the end of the tree

        currSize++;
        modCounter++;
        return true;
    }

    // Deletes the key/value pair identified by the key parameter.
    // Returns true if the key/value pair was found and removed,
    // otherwise false.
    public boolean delete(K key){
        if(isEmpty())
            return false;

        if(remove(root, key) == null)   //if its null that means that the key/value was not found and removed
            return false;

        currSize--;
        modCounter++;
        return true;    //if it was removed in the above if statement, then return true
    }

    // Returns the value associated with the parameter key. Returns
    // null if the key is not found or the dictionary is empty.
    public V get(K key){
        return find(key, root);     //recursively find to make things less complicated in BST
    }

    // Returns the key associated with the parameter value. Returns
    // null if the value is not found in the dictionary. If more
    // than one key exists that matches the given value, returns the
    // first one found.
    public K getKey(V value){
        return findKey(value, root);    //recursively find to make things less complicated in BST
    }

    // Returns the number of key/value pairs currently stored
    // in the dictionary
    public int size(){
        return currSize;
    }

    // Returns true if the dictionary is full
    public boolean isFull(){
        return false;   //Never at full capacity
    }

    // Returns true if the dictionary is empty
    public boolean isEmpty(){
        return currSize == 0;
    }

    // Makes the dictionary empty
    public void clear(){
        root = null;
        currSize = 0;
        modCounter++;
    }

    // Returns an Iterator of the keys in the dictionary, in ascending
    // sorted order
    public Iterator<K> keys(){
        return new KeyIteratorHelper();
    }

    // Returns an Iterator of the values in the dictionary. The
    // order of the values must match the order of the keys.
    public Iterator<V> values(){
        return new ValueIteratorHelper();
    }


    //Taken from Riggins coursereader
    private V find(K key, Node<K,V> node){
        if(node == null)
            return null;

        if(((Comparable<K>)key).compareTo(node.key) < 0)
            return find(key, node.leftChild);   //go left
        if(((Comparable<K>)key).compareTo(node.key) > 0)
            return find(key, node.rightChild);  //go right

        return (V)node.value;   //return the value if it equals the key
    }

    private K findKey(V value, Node<K,V> node){
        if(node == null)
            return null;
            
        if(((Comparable<V>)value).compareTo(node.value) < 0)
            return findKey(value, node.leftChild);  //go left
        if(((Comparable<V>)value).compareTo(node.value) > 0)
            return findKey(value, node.rightChild); //go right

        return (K)node.key; //return the key if it equals the value
    }

    //Taken from Riggins coursereader
    private void insert(K key, V value, Node<K,V> node, Node<K,V> parent, boolean wasLeft){
        if(node == null){   //node is null, so we can insert left or right
            if(wasLeft)
                parent.leftChild = new Node<K,V>(key,value);    //if we went left, make the parent's leftChild the insertion point
            else
                parent.rightChild = new Node<K,V>(key,value);   //if we went right, make the parent's rightChild the insertion point
        }
        else if(((Comparable<K>)key).compareTo((K)node.key) < 0)
            insert(key, value, node.leftChild, node, true);     //go left
        else
            insert(key, value, node.rightChild, node, false);   //go right
    }

    private Node<K,V> remove(Node<K,V> node, K key){
        if(node == null)    //tree is empty
            return node;   
        
        //recursively go down the tree, either by going left or right
        if(((Comparable<K>)node.key).compareTo((K)key) < 0)
            node.leftChild = remove(node.leftChild, key);   //go left
        else if(((Comparable<K>)node.key).compareTo((K)key) > 0)
            node.rightChild = remove(node.rightChild, key); //go right
        else{  
            //else, the key == node.key, so we want to delete it
            //one child or no children 
            if(node.leftChild == null)  //find the other child
                return node.rightChild;
            else if(node.rightChild == null)
                return node.leftChild;

            //if not above^, node has two children so we have to find the inorder successor
            node.key = inorderSuccessor(node.rightChild);

            //when you find the inorder successor, recursively call remove again to delete it
            node.rightChild = remove(node.rightChild, node.key);
        }

        return node;
    }

    private K inorderSuccessor(Node<K,V> node){
        K min = node.key;
        while(node.leftChild != null){  //while there are still elements in the tree
            min = node.leftChild.key;   //make the min the leftChild's key (smallest value in the right)
            node = node.leftChild;      //traverse down
        }

        return min;
    }

    // Fail fast iterator taken from Riggins Course Reader
    abstract class IteratorHelper<E> implements Iterator<E>{
        protected Node<K,V>[] nodes;
        protected int index, iteratorIndex;
        protected long modCheck;

        public IteratorHelper(){
            nodes = new Node[currSize];
            index = 0;
            iteratorIndex = 0;
            modCheck = modCounter;
            inorderArr(root);    
        }

        public boolean hasNext(){
            if(modCheck != modCounter)
                throw new ConcurrentModificationException();
            return index < currSize;
        }

        public abstract E next();

        public void remove(){
            throw new UnsupportedOperationException();
        }

        private void inorderArr(Node<K,V> node){
            if(node != null)
                return;
            inorderArr(node.leftChild);
            nodes[iteratorIndex++] = node;
            inorderArr(node.rightChild);
        }
    }

    class KeyIteratorHelper<K> extends IteratorHelper<K> {
        public KeyIteratorHelper(){
            super();
        }

        public K next(){
            return (K) nodes[index++].key;
        }
    }

    class ValueIteratorHelper<V> extends IteratorHelper<V>{
        public ValueIteratorHelper(){
            super();
        }

        public V next(){
            return (V) nodes[index++].value;
        }
    }

    private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>{
        K key;
        V value;

        public DictionaryNode(K key, V value){
            this.key = key;
            this.value = value;
        }

        public int compareTo(DictionaryNode<K,V> node){
            return ((Comparable<K>)key).compareTo((K)node.key);
        }
    }

}