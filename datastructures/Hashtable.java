// CS310-01
// Drake Nguyen cssc1246
// 4/27/2020
// Program 4 Hashtable.java

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

@SuppressWarnings("unchecked")

public class Hashtable<K, V> implements DictionaryADT<K, V> {

    private ListADT<DictionaryNode<K,V>>[] list;
    private int currSize, tableSize;
    private long modCounter;
    
    public Hashtable(int size){
        currSize = 0;
        tableSize = (int) (size * 1.3f);  //hashtable sizes should always be bigger than the actual size, account for this by having a tablesize bigger than maxsize  
        modCounter = 0;

        //taken from Riggins Coursereader
        list = new LinkedListDS[tableSize];     //use tableSize so the hashtable can be larger than the amount of elements
        for(int i = 0; i < tableSize; i++){
            list[i] = new LinkedListDS<DictionaryNode<K,V>>();  //use the wrapper class we implemented below to initialize list at each index
        }
    }

    // Adds the given key/value pair to the dictionary. Returns
    // false if the dictionary is full, or if the key is a duplicate.
    // Returns true if addition succeeded.
    public boolean put(K key, V value){
        if(isFull())
            return false;

        //taken from Riggins Coursereader
        //find array using the hash code and modding by tableSize to account for negative numbers
        int index = ( key.hashCode() & 0x7FFFFFFF ) % tableSize;

        //use LinkedListDS method provided contains to check if there are duplicates in the table
        if(list[index].contains(new DictionaryNode<K,V>(key,null)))
            return false;   //check for duplicates

        //use LinkedListDS method to insert into the hashtable at the index
        list[index].addLast(new DictionaryNode<K,V>(key,value));   

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

        int index = ( key.hashCode() & 0x7FFFFFFF ) % tableSize;

        //use LinkedListDS method to remove at the index the dictionary node that matches the key
        list[index].remove(new DictionaryNode<K,V>(key,null)); 
         
        currSize--;
        modCounter++;

        return true;
    }

    // Returns the value associated with the parameter key. Returns
    // null if the key is not found or the dictionary is empty.
    public V get(K key){
        if(isEmpty())
            return null;

        int index = ( key.hashCode() & 0x7FFFFFFF ) % tableSize;

        // Use the LinkedListDS method provided to search for a DictionaryNode at the list's index
        DictionaryNode<K,V> node = list[index].search(new DictionaryNode<K,V>(key,null));
        
        if(node == null)
            return null;
        
        return node.value;
    }
    // Returns the key associated with the parameter value. Returns
    // null if the value is not found in the dictionary. If more
    // than one key exists that matches the given value, returns the
    // first one found.
    public K getKey(V value){
        if(isEmpty())
            return null;

        //Iterate through the list to find the element with the value we want
        for(int i = 0; i < list.length; i++){
            Iterator<DictionaryNode<K,V>> iter = list[i].iterator();    //make an iterator element to search through

            while(iter.hasNext()){  //while there are still elements
                DictionaryNode<K,V> node = iter.next();
                if(((Comparable<V>)node.value).compareTo((V)value)==0)  //check to see if the values are the same
                    return (K)node.key;     //return inside to make sure the first one is returned
            }
        }
        return null;
    }

    // Returns the number of key/value pairs currently stored
    // in the dictionary
    public int size(){
        return currSize;
    }

    // Returns true if the dictionary is full
    public boolean isFull(){
        return false;   //hardcoded to false because the table can never be full with chaining
    }

    // Returns true if the dictionary is empty
    public boolean isEmpty(){
        return currSize == 0;
    }

    // Makes the dictionary empty
    public void clear(){
        //use the LinkedListDS method makeEmpty to clear the dictionary
        for(ListADT n: list)
            n.makeEmpty(); 
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

    // Fail fast iterator taken from Riggins Course Reader
    abstract class IteratorHelper<E> implements Iterator<E>{
        protected DictionaryNode<K,V>[] nodes;
        protected int index;
        protected long modCheck;

        public IteratorHelper(){
            nodes = new DictionaryNode[currSize];
            index = 0;
            int j = 0;
            modCheck = modCounter;
            for(int i = 0; i < tableSize; i++)
                for(DictionaryNode n : list[i])
                    nodes[j++] = n;
            nodes = (DictionaryNode<K,V>[]) shellSort(nodes);        
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

    // Wrapper class to implement using chaining
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

    //shellSort method taken from Riggins coursereader
    public DictionaryNode<K,V>[] shellSort(DictionaryNode<K,V> array[]){
        DictionaryNode<K,V>[] node = array;
        int in, out, h = 1;
        DictionaryNode<K,V> tmp;
        int size = node.length;

        while(h <= size/3)
            h = h*3+1;
        while(h  > 0){
                for(out = h; out < size; out++){
                    tmp = node[out];
                    in = out;
                    while(in > h-1 && node[in-h].compareTo(tmp) >= 0){
                        node[in] = node[in-h];
                        in -= h;
                    }
                    node[in] = tmp;
                }
            h = (h-1)/3;
        }
        return node;
    }

}