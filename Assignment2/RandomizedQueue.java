import java.util.Iterator;
import edu.princeton.cs.algs4.*;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] q;
	private int N;

	public RandomizedQueue() {              // construct an empty randomized queue
		q = (Item[]) new Object[1];
		N = 0;
	}
	public boolean isEmpty() {              // is the queue empty?
		return 0 == N;
	}
	public int size() {                     // return the number of items on the queue
		return N;
	}
	public void enqueue(Item item) {        // add the item
		if (null == item)
			throw new java.lang.IllegalArgumentException();
		else {
			if (N == q.length) 
				resize(2 * q.length);
			q[N++] = item;
		}
	}
	public Item dequeue() {                 // remove and return a random item
		if(isEmpty())
			throw new java.util.NoSuchElementException();
		else {
			int index = StdRandom.uniform(N);
			Item item = q[index];
			if ((N - 1) != index)
				q[index] = q[N - 1];
			q[N - 1] = null;
			N--;
			if (N > 0 && N == q.length / 4)
				resize(q.length / 2);

			return item;
		}
	}
	public Item sample() {                  // return (but do not remove) a random item
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		else {
			int index = StdRandom.uniform(N);

			return q[index];
		}
	}
	public Iterator<Item> iterator() {      // return an independent iterator over items in random order
		return new RandomizedQueueIterator();
	}
	private class RandomizedQueueIterator implements Iterator<Item> {
		private int i = 0;
		private int[] shuffleArray = new int[N];

		private void shuffleArray() {
			for (int j = 0; j < N; j++)
				shuffleArray[j] = j;

			StdRandom.shuffle(shuffleArray);
		}
		public boolean hasNext() {
			return i < N;
		}
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
		public Item next() {
			if (!hasNext())
				throw new java.util.NoSuchElementException();
			else {
				return q[shuffleArray[i++]];
			}
		}
	}
	private void resize(int capacity) {
		assert capacity >= N;

		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++)
			copy[i] = q[i];
		q = copy;
	}
	public static void main(String[] args) {// unit testing (optional)

	}
}