// CS310-01
// Drake Nguyen cssc1246
// 2/11/2020
// Program 1 OrderedArrayPriorityQueue.java

//Disclaimer: the way I ordered priority is reverse to how the PQ1_Grader orders it.

package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class OrderedArrayPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    E[] queue;
    int currentSize, maxSize;

    public OrderedArrayPriorityQueue(){
        this(DEFAULT_MAX_CAPACITY);     //uses 1 arg constructor to initalize current and max size, and DFM for queue
    }

    public OrderedArrayPriorityQueue(int capacity){
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
        
        //find index to insert object
        int loc = findInsertPoint(object, 0, currentSize-1);

        //right shift
        for(int i = currentSize-1; i >= loc; i--){
            queue[i+1] = queue[i];
        }

        queue[loc] = object;    //insert object
        currentSize++;          //accomodate for inserted object by adding to array size

        return true;
    }

    // Removes the object of highest priority that has been in the
    // PQ the longest, and returns it. Returns null if the PQ is empty.
    public E remove(){
        if(isEmpty())
            return null;
        
        //since the array is ordered, the highest priority will always be at the end of the queue
        //--currentSize will find the last index (highest priority) and remove it
        return queue[--currentSize];
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

        return queue[currentSize-1];    //return the object without removing it
    }

    // Returns true if the priority queue contains the specified element
    // false otherwise.
    public boolean contains(E obj){
        //simple binary search to see if object is there
        //best case: O(1), ave case: O(logn), worst case: O(n)
        int lo = 0;
        int hi = currentSize - 1;

        while(lo <= hi) {
            int mid = (lo + hi) / 2;
            if(((Comparable<E>)queue[mid]).compareTo(obj) < 0)
                hi = mid - 1;
            else if(((Comparable<E>)queue[mid]).compareTo(obj) > 0)
                lo = mid + 1;
            else
                return true;
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

    // Returns an index if the element at mid is >= 0 for insertion
    // Uses recursion to keep searching
    public int findInsertPoint(E obj, int lo, int hi){
        if(hi < lo)
            return lo;

        int mid = (lo+hi) >> 1;     //right shift

        if(((Comparable<E>)obj).compareTo(queue[mid]) >= 0)
            return findInsertPoint(obj, lo, mid-1);    //recursively go left
        return findInsertPoint(obj, mid+1, hi);    //recursively go right
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
