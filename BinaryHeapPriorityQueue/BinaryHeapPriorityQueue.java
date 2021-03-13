// CS310-01
// Drake Nguyen cssc1246
// 4/5/2020
// Program 3 BinaryHeapPriorityQueue.java

package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

@SuppressWarnings("unchecked")
public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {

//required for stable behavior, taken from Riggin's coursereader
protected class Wrapper<E> implements Comparable<Wrapper<E>>{
    long number;
    E data;

    public Wrapper(E d){
        number = entryNumber++;
        data = d;
    }

    public int compareTo(Wrapper<E> o){
        if(((Comparable<E>)data).compareTo(o.data) == 0)
            return (int)(number - o.number);
        return ((Comparable<E>)data).compareTo(o.data);
    }
}

private Wrapper<E>[] arr; //Binary heap
private int currentSize, maxSize; //heaps have a max size, used to check if full
private long entryNumber, modificationCounter; //entry number is used for the wrapper class, modification counter for fail-fast iterator

public BinaryHeapPriorityQueue(){
    this(DEFAULT_MAX_CAPACITY); //will use 1 arg constructor to set no arg constructor
}

public BinaryHeapPriorityQueue(int capacity){
    arr = new Wrapper[capacity];
    currentSize = 0;
    maxSize = capacity;
    modificationCounter = 0;
    entryNumber = 0;
}

 // Inserts a new object into the priority queue. Returns true if
 // the insertion is successful. If the PQ is full, the insertion
 // is aborted, and the method returns false.
 public boolean insert(E object){
    if(isFull())
        return false;

    Wrapper<E> tmp = new Wrapper<E>(object); //cast object to a wrapper so it can be inserted into the heap
    arr[currentSize] = tmp;
    currentSize++;
    trickleUp(currentSize-1); //trickle to account for out of order insertion and put in order
                              //trickleUp makes the method O(logn)

    modificationCounter++;

    return true;
 }

 // Removes the object of highest priority that has been in the
 // PQ the longest, and returns it. Returns null if the PQ is empty.
 public E remove(){
    if(isEmpty())
        return null;

    Wrapper<E> tmp = arr[0]; //highest priority is at index 0
    trickleDown(0); //trickle down to account for object at 0 being removed
                    //trickleDown makes the method O(logn)
    currentSize--;
    modificationCounter++;

    return tmp.data;
 }

 // Deletes all instances of the parameter obj from the PQ if found, and
 // returns true. Returns false if no match to the parameter obj is found.
 public boolean delete(E obj){
     if(isEmpty())
        return false;

    for(int i = 0; i <= currentSize; i++){  //linear search
        if(arr[i].data.compareTo(obj) == 0){    //check to see if the element at i is the object to be removed
            
            Wrapper<E> parent = arr[(i-1) >> 1];    //arr(i) is the element to be removed, so shift by 1 is the parent
            Wrapper<E> child = arr[currentSize-1];  //the child is the last element in the arr
            if( parent.compareTo(child) < 0)        
                trickleDown(i);
            else{
                arr[i] = child;
                trickleUp(i);
            }
            i = i-1; //accounts for elements that are right next to each other and once you remove i = i-1
            modificationCounter++;
            currentSize--;
        }
    }
    return true;

 }

 // Returns the object of highest priority that has been in the
 // PQ the longest, but does NOT remove it.
 // Returns null if the PQ is empty.
 public E peek(){
    if(isEmpty())
        return null;

    return arr[0].data; //highest priority element is at the root (index 0)
 }
    
 // Returns true if the priority queue contains the specified element
 // false otherwise.
 public boolean contains(E obj){
     if(isEmpty())
        return false;
     
    boolean foundObj = false;

     for(int i = 0; i < currentSize; i++){  //linear search through the heap
         if(arr[i].data.compareTo(obj) == 0){
            foundObj = true;
         }
     }   
     return foundObj;
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

 // Taken from course reader, when there is a gap or hole in the heap trickle the list upwards to fill
 // Modified with parameter index to account for locations inside the heap I want to trickle up at
 public void trickleUp(int index){
     int newIndex = index;
     int parentIndex = (newIndex-1) >> 1;
     Wrapper<E> newValue = arr[newIndex];
     while(parentIndex >= 0 && newValue.compareTo(arr[parentIndex]) < 0){
         arr[newIndex] = arr[parentIndex];
         newIndex = parentIndex;
         parentIndex = (parentIndex-1) >> 1;
     }
     arr[newIndex] = newValue;
 }

 // Taken from course reader, when there is a gap or hole in the heap trickle the list downwards to fill
 // Modified with paramter index to account for locations inside the heap I want to trickle down at
 public void trickleDown(int index){
    int curr = index;
    int child = getNextChild(curr);
    while((child != -1 &&
            arr[curr].compareTo(arr[child]) < 0) &&
            (arr[child].compareTo(arr[currentSize-1]) < 0)){
                arr[curr] = arr[child];
                curr = child;
                child = getNextChild(curr);
            }
            arr[curr] = arr[currentSize-1];
 }
// Taken from course reader, finds the next child in the heap
 public int getNextChild(int curr){
     int left = (curr << 1) + 1;
     int right = left+1;
     if(right < currentSize) {
         if(arr[left].compareTo(arr[right]) < 0)
            return left;
        return right;
     }
     if(left < currentSize)
        return left;
    return -1;
 }

 // Returns an iterator of the objects in the PQ, in no particular
 // order.
 public Iterator<E> iterator(){
    return new IteratorHelper();
}

class IteratorHelper implements Iterator<E>{
    int counter;
    long modCounter;

    public IteratorHelper(){
        counter = 0;
        modCounter = modificationCounter;
    }

    public boolean hasNext(){
        if(modCounter != modificationCounter)
            throw new ConcurrentModificationException();
        return counter < currentSize;
    }

    public E next(){
        if(!hasNext())
            throw new NoSuchElementException();

        return arr[counter++].data;
    }

    public void remove(){
        throw new UnsupportedOperationException();
    }		
}

} 