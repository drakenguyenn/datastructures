// CS310-01
// Drake Nguyen cssc1246
// 4/27/2020
// Program 4 BalancedTreeDictionary.java

package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

@SuppressWarnings("unchecked")

public class BalancedTreeDictionary<K, V> implements DictionaryADT<K, V>{

    private TreeMap tree;   //use Treemap implementation to make a tree
    private K key;
    private V value;

    public BalancedTreeDictionary(){
        tree = new TreeMap();
        key = null;
        value = null;
    }

    // Adds the given key/value pair to the dictionary. Returns
    // false if the dictionary is full, or if the key is a duplicate.
    // Returns true if addition succeeded.
    public boolean put(K key, V value){
        if(tree.containsKey(key))   //check for duplicate
            return false;   
        else{
            tree.put(key, value);   //insert into tree
            return true;
        }
    }

    // Deletes the key/value pair identified by the key parameter.
    // Returns true if the key/value pair was found and removed,
    // otherwise false.
    public boolean delete(K key){
        return tree.remove(key) != null;    //return the T/F depending on if remove correctly removes the key, or if it cannot be found
    }

    // Returns the value associated with the parameter key. Returns
    // null if the key is not found or the dictionary is empty.
    public V get(K key){
        if(isEmpty() || !tree.containsKey(key)) //Empty dictionary or key is not found
            return null;

        return (V)tree.get(key);    //use treemap implementatino to get the value at the key
    }

    // Returns the key associated with the parameter value. Returns
    // null if the value is not found in the dictionary. If more
    // than one key exists that matches the given value, returns the
    // first one found.
    public K getKey(V value){
        Iterator<V> valIter = values(); //Iterable for value and key to traverse through and check
        Iterator<K> keyIter = keys();

        while(valIter.hasNext() && keyIter.hasNext()){  //While they have elements
            if(((Comparable<V>)valIter.next()).compareTo(value) == 0){  //if the values are the same
                return keyIter.next();  //return the key at the index
            }
            keyIter.next();
        }
        return null;
    }

    // Returns the number of key/value pairs currently stored
    // in the dictionary
    public int size(){
        return tree.size();
    }

    // Returns true if the dictionary is full
    public boolean isFull(){
        return false;
    }

    // Returns true if the dictionary is empty
    public boolean isEmpty(){
        return tree.isEmpty();
    }

    // Makes the dictionary empty
    public void clear(){
        tree.clear();
    }

    // Returns an Iterator of the keys in the dictionary, in ascending
    // sorted order
    public Iterator<K> keys(){
        return tree.navigableKeySet().iterator();
    }

    // Returns an Iterator of the values in the dictionary. The
    // order of the values must match the order of the keys.
    public Iterator<V> values(){
        return tree.values().iterator();
    }

}