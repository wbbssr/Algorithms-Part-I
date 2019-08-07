import java.util.Iterator;
import edu.princeton.cs.algs4.*;

public class Deque<Item> implements Iterable<Item> {
	private int size;
	private Node first, last;

	private class Node {
		private Item item;
		private Node next;
		private Node prev;
	}

	public Deque() {                        // construct an empty deque
		size = 0;
		first = null;
		last = null;
	} 
	public boolean isEmpty() {              // is the deque empty?
		return first == null;
	}
	public int size() {                     // return the number of items on the deque
		return size;
	}
	public void addFirst(Item item) {       // add the item to the front
		if (item == null)
			throw new java.lang.IllegalArgumentException();
		else {
			Node oldfirst = first;
			first = new Node();
			first.item = item;
			first.next = oldfirst;
			first.prev = null;
			if (0 == size)
				last = first;
			else
				oldfirst.prev = first;
			size++;
		}
	}
	public void addLast(Item item) {        // add the item to the end
		if (item == null)
			throw new java.lang.IllegalArgumentException();
		else {
			Node oldlast = last;
			last = new Node();
			last.item = item;
			last.next = null;
			last.prev = oldlast;
			if (isEmpty())
				first = last;
			else
				oldlast.next = last;
			size++;
		}
	}
	public Item removeFirst() {             // remove and return the item from the front
		if (isEmpty())
			throw new java.lang.UnsupportedOperationException();
		else {
			Item item = first.item;
			first = first.next;
			if (first != null)
				first.prev = null;
			if (isEmpty())
				last = null;
			size--;
			return item;
		}
	}
	public Item removeLast() {              // remove and return the item from the end
		if (isEmpty())
			throw new java.lang.UnsupportedOperationException();
		else {
			Item item = last.item;
			last = last.prev;
			if (last != null)
				last.next = null;
			if (1 == size)
				first = null;
			size--;
			return item;
		}
	}
	public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new java.lang.UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
	public static void main(String[] args) {// unit test (optional)
	}
}