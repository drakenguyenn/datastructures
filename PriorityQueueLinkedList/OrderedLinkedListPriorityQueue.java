// CS310-01
// Drake Nguyen cssc1246
// 3/5/2020
// Program 2 OrderedLinkedListPriorityQueue.java

package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

@SuppressWarnings("unchecked")
public class OrderedLinkedListPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E>{

	class Node<E>{
		E data;
		Node<E> next;
		
		public Node(E obj){
			data = obj;
			next = null;
		}
	}

    private Node<E> head;
    private int size;
    private long modificationCounter;	//logs how many modifications were made by the methods

    public OrderedLinkedListPriorityQueue(){	//zero arg constructor, sets everything to null/0
		head = null;
		size = 0;
		modificationCounter = 0;
    }

	// Inserts a new object into the priority queue. Returns true if
	// the insertion is successful. If the PQ is full, the insertion
	// is aborted, and the method returns false.
	public boolean insert(E object){
		Node<E> newNode = new Node<E>(object), prev = null, curr = head;

		while(curr != null && ((Comparable<E>)object).compareTo(curr.data) >= 0){	//check ordered list for correct insertion index
			prev = curr;
			curr = curr.next;
		}

		if(prev == null){
			newNode.next = head;	//first case where prev was not changed
			head = newNode;
		}
		else{
			prev.next = newNode;	//insert
			newNode.next = curr;
		}

		size++;
		modificationCounter++;

		return true;	//hardcoded to true because it will never be full
	}

	// Removes the object of highest priority that has been in the
	// PQ the longest, and returns it. Returns null if the PQ is empty.
	public E remove(){
		if(isEmpty())
			return null;

		E tmp = head.data;

		if(head == null)
			return null;		//first case where head is null
		else
			head = head.next;	//remove at head bc ordered list

		size--;
		modificationCounter++;

		return tmp;
	}

	// Deletes all instances of the parameter obj from the PQ if found, and
	// returns true. Returns false if no match to the parameter obj is found.
	public boolean delete(E obj){
        Node<E> curr = head, prev = null;	

		if(isEmpty())
			return false;

		if(!contains(obj))
			return false;
		
		while(curr != null){
			if(((Comparable<E>)obj).compareTo(curr.data) == 0){	//check to see if obj is in list
				if(prev == null){
					//first case, remove if at front of list
					curr = curr.next;
					head = head.next;
					size--;
					modificationCounter++;
				}
				else{
					//delete every obj instance
					prev.next = curr.next;
					curr = curr.next;
					size--;
					modificationCounter++;
				}
			}
			else{
				//iterate through list
				prev = curr;
				curr = curr.next;
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
		
		return head.data;	//ordered list, first element is the highest priority
	}

	// Returns true if the priority queue contains the specified element
	// false otherwise.
	public boolean contains(E obj){
		Node<E> curr = head;

		while(curr != null){
			if(((Comparable<E>)obj).compareTo(curr.data) == 0)	//check to see if data equals object
				return true;
			curr = curr.next;					//iterate through list
		}

		return false;
	}

	// Returns the number of objects currently in the PQ.
	public int size(){
		return size;
	}

	// Returns the PQ to an empty state.
	public void clear(){
		head = null;
		size = 0;
		modificationCounter = 0;
	}

	// Returns true if the PQ is empty, otherwise false
	public boolean isEmpty(){
		return size == 0;
	}

	// Returns true if the PQ is full, otherwise false. List based
	// implementations should always return false.
	public boolean isFull(){
		return false;
	}

	// Returns an iterator of the objects in the PQ, in no particular
	// order.
	public Iterator<E> iterator(){
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E>{
		Node<E> nodePtr;
		long modCounter;

		public IteratorHelper(){
			nodePtr = head;
			modCounter = modificationCounter;
		}

		public boolean hasNext(){
			if(modCounter != modificationCounter)
				throw new ConcurrentModificationException();
			return nodePtr != null;
		}

		public E next(){
			if(!hasNext())
				throw new NoSuchElementException();

			E tmp = nodePtr.data;
			nodePtr = nodePtr.next;
			return tmp;
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}		
	}
}
