// CS310-01
// Drake Nguyen cssc1246
// 2/11/2020
// Program 1 UnorderedArrayPriorityQueue.java

package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class UnorderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    E[] queue;
    int currentSize, maxSize;

    public UnorderedArrayPriorityQueue(){
        this(DEFAULT_MAX_CAPACITY);     //uses 1 arg constructor to initalize current and max size, and DFM for queue
    }

    public UnorderedArrayPriorityQueue(int capacity){
        currentSize = 0;
        maxSize = capacity;
        queue = (E[]) new Comparable[maxSize];
    }

    // Inserts a new object into the priority queue. Returns true if
    // the insertion is successful. If the PQ is full, the insertion
    // is aborted, and the method returns false.
    public boolean insert(E object){
        if(isFull())
            return false;
        //if we insert, the array's size will be ++, and if its unordered where the obj is placed does not matter
        queue[currentSize++] = object;

        return true;
    }

    // Removes the object of highest priority that has been in the
    // PQ the longest, and returns it. Returns null if the PQ is empty.
    public E remove(){
        if(isEmpty())
            return null;
    
        E max = queue[0];   //compared variable
        int loc = 0;        //placeholder index

        for(int i = 0; i < currentSize; i++){
            if(((Comparable<E>)queue[i]).compareTo(max) < 0){   //check priority
                max = queue[i];     //if queue[i] is higher priority, set max to queue[i]
                loc = i;            //update index
            }
        }

        for(int j = loc; j < currentSize-1; j++){
            queue[j] = queue[j+1];  //shift array to the left starting from removed index
        }

        currentSize--;  //accomodate for removed obj by decrementing currentSize once
        return max;
    }

    // Deletes all instances of the parameter obj from the PQ if found, and
    // returns true. Returns false if no match to the parameter obj is found.
    public boolean delete(E obj){
        int count = 0;      //variable to count how many instances show up
        int tmp = 0;        //tmp is the new index in the array, only incremented if obj is not found
        boolean foundObj = false;

        for(int i = 0; i < currentSize; i++){
            if(((Comparable<E>)queue[i]).compareTo(obj) == 0){  //check to see if instance shows up
                    count++;    //found object to delete
                    foundObj = true;    //if instance is there, set flag to true
            }
            else
                    queue[tmp++] = queue[i];    
                    //shift over the array at tmp++
                    //tmp is the new index in the array, only incremented if obj is not found
                    // old array: 0 4 6 8 2 3 4 6
                    //delete(6);
                    // new array: 0 4 8 2 3 4
                    
        }
    
        currentSize -= count;   //decrement size based on how many objects were removed

        return foundObj;
    }

    // Returns the object of highest priority that has been in the
    // PQ the longest, but does NOT remove it.
    // Returns null if the PQ is empty.
    public E peek(){
        if(isEmpty())
            return null;

        E max = queue[0];   //compared object
        for(int i = 0; i < currentSize; i++){
            if(((Comparable<E>)queue[i]).compareTo(max) < 0){   //check for highest priority
                max = queue[i];     //if queue[i] has a higher priority, set max to it
            }
        }

        return max;
    }

    // Returns true if the priority queue contains the specified element
    // false otherwise.
    public boolean contains(E obj){
        for(int i = 0; i < currentSize; i++){
            if(((Comparable<E>)queue[i]).compareTo(obj) == 0){  //check to see if queue has the obj
                return true;
            }
        }
        return false;
    }

    // Returns the number of objects currently in the PQ.
    public int size(){
        return currentSize;
    }

    // Returns the PQ to an empty state.
    public void clear(){
        currentSize = 0;
    }

    // Returns true if the PQ is empty, otherwise false
    public boolean isEmpty(){
        return currentSize == 0;
    }

    // Returns true if the PQ is full, otherwise false. List based
    // implementations should always return false.
    public boolean isFull(){
        return currentSize == maxSize;
    }

    // Returns an iterator of the objects in the PQ, in no particular
    // order.
    public Iterator<E> iterator(){
        return new IteratorHelper();
    }

    class IteratorHelper implements Iterator<E>{
        int counter;

        public IteratorHelper(){
            counter = 0;
        }

        public boolean hasNext(){
            return counter < currentSize;
        }

        public E next(){
            if(!hasNext())
                throw new NoSuchElementException();
            return queue[counter++];
        }
    };
}
