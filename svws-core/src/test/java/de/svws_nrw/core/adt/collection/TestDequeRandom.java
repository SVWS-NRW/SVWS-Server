package de.svws_nrw.core.adt.collection;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testet die Klasse {@link LinkedCollection}.
 *
 * @author Benjamin A. Bartsch
 */
public class TestDequeRandom {

	private final int _MAX_VALUE = 100;
	private final int _ROUNDS = 100000;
	private final Random _random = new Random(1L);
	private final Deque<Integer> d1 = new LinkedCollection<>();
	private final Deque<Integer> d2 = new LinkedList<>();

	/**
	 * Folgende Methoden werden zufällig getestet. <br>
	 *
	 *
	 * ----- interface Deque<E> ----- void <br>
	 * addFirst(E e); <br>
	 * void addLast(E e); <br>
	 * boolean offerFirst(E e); <br>
	 * boolean offerLast(E e); <br>
	 * E removeFirst(); <br>
	 * E removeLast(); <br>
	 * E pollFirst(); <br>
	 * E pollLast(); <br>
	 * E getFirst(); <br>
	 * E getLast(); <br>
	 * E peekFirst(); <br>
	 * E peekLast(); <br>
	 * boolean removeFirstOccurrence(Object o); <br>
	 * boolean removeLastOccurrence(Object o); <br>
	 * void push(E e); <br>
	 * E pop(); <br>
	 * TODO iterator.remove() fails on {@link LinkedCollection} <br>
	 *
	 *
	 * ----- interface Queue<E> ----- <br>
	 * E remove(); <br>
	 * E poll(); <br>
	 * E element(); <br>
	 * E peek(); <br>
	 * Iterator<E> descendingIterator(); <br>
	 * boolean offer(E e); <br>
	 *
	 *
	 * ----- interface Collection<E> ----- <br>
	 * int size(); <br>
	 * boolean isEmpty(); <br>
	 * boolean contains(Object o); <br>
	 * Iterator<E> iterator(); --> testEquality <br>
	 * Object[] toArray(); <br>
	 * <T> T[] toArray(T[] a); <br>
	 * boolean add(E e); <br>
	 * boolean remove(Object o); <br>
	 * boolean containsAll(Collection<?> c); boolean addAll(Collection<? extends E> c); <br>
	 * boolean removeAll(Collection<?> c); <br>
	 * boolean retainAll(Collection<?> c); <br>
	 * clear(); <br>
	 * boolean equals(Object o); <br>
	 * public String toString(); <br>
	 *
	 */
	@Test
	@DisplayName("Testet grundlegende Funktionen der {@link LinkedCollection}.")
	void testeAlles() {
		for (int i = 0; i < _ROUNDS; i++) {
			final int type = _random.nextInt(3);
			if (type == 0) { // Adding
				switch (_random.nextInt(9)) {
					case 0 -> testAddFirst();
					case 1 -> testAddFirst();
					case 2 -> testAddLast();
					case 3 -> testOfferFirst();
					case 4 -> testOfferLast();
					case 5 -> testAdd();
					case 6 -> testOffer();
					case 7 -> testAddAll();
					case 8 -> testPush();
					default -> throw new IllegalArgumentException("Unexpected value");
				}
			}
			if (type == 1) { // Removing
				switch (_random.nextInt(15)) {
					case 0 -> testRemoveFirst();
					case 1 -> testRemoveLast();
					case 2 -> testPollFirst();
					case 3 -> testPollLast();
					case 4 -> testRemoveFirstOccurrence();
					case 5 -> testRemoveLastOccurrence();
					case 6 -> testRemoveToBoolean();
					case 7 -> testPoll();
					case 8 -> testPop();
					case 9 -> testRemoveObject();
					case 10 -> testRemoveAll();
					case 11 -> testRetainAll();
					case 12 -> testRemoveAllSelf();
					case 13 -> testRetainAllSelfOrNull();
					case 14 -> testRemoveToElement();
					default -> throw new IllegalArgumentException("Unexpected value");
				}
			}
			if (type == 2) { // Misc
				switch (_random.nextInt(19)) {
					case 0 -> testSize();
					case 1 -> testGetFirst();
					case 2 -> testGetLast();
					case 3 -> testPeekFirst();
					case 4 -> testPeekLast();
					case 5 -> testElement();
					case 6 -> testPeek();
					case 7 -> testContains();
					case 8 -> testDescendingIterator();
					case 9 -> testClear();
					case 10 -> testIsEmpty();
					case 11 -> testToArray();
					case 12 -> testToArrayWithType();
					case 13 -> testContainsAll();
					case 14 -> testEquals();
					case 15 -> testSpecialIndex();
					case 16 -> testSpecialSort();
					case 17 -> testToString();
					case 18 -> testHashCode();
					default -> throw new IllegalArgumentException("Unexpected value");
				}
			}
		}
	}

	private void testHashCode() {
		if (d1.hashCode() != d2.hashCode())
			fail("d1.hashCode() != d2.hashCode() --> " + d1.hashCode() + " != " + d2.hashCode());
	}

	private void testToString() {
		if (!d1.toString().equals(d2.toString()))
			fail("!d1.toString().equals(d2.toString())");
	}

	private void testRemoveToElement() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			final Integer i1 = d1.remove();
			final Integer i2 = d2.remove();
			if (i1 != i2)
				fail("d1.remove() != d2.remove() --> " + i1 + "!=" + i2);
		} else {
			try {
				d1.remove();
				fail("d1.remove() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.remove();
				fail("d2.remove() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testSpecialSort() {
		final LinkedCollection<Integer> temp1 = new LinkedCollection<>((LinkedCollection<Integer>) d1);

		temp1.sort(new Comparator<Integer>() {
			@Override
			public int compare(final Integer o1, final Integer o2) {
				return o1.compareTo(o2);
			}
		});

		Integer old = null;
		for (final Integer value : temp1) {
			if (old != null)
				if (old.compareTo(value) > 0)
					fail("old.compareTo(value) > 0 --> LinkedCollection IST NICHT SORTIERT");
			old = value;
		}

	}

	private void testSpecialIndex() {
		if (d1.isEmpty())
			return;

		final LinkedCollection<Integer> temp1 = new LinkedCollection<>((LinkedCollection<Integer>) d1);
		final int index = _random.nextInt(d1.size());
		final int value2 = _random.nextInt(_MAX_VALUE);
		temp1.set(index, value2);
		final Integer value3 = temp1.get(index);

		if (value2 != value3)
			fail("value2 != value3 --> " + value2 + " != " + value3);
	}

	private void testRetainAllSelfOrNull() {
		if (_random.nextBoolean()) {
			d1.clear();
			d2.clear();
		}

		if (_random.nextBoolean()) {
			final boolean b1 = d1.retainAll(d1);
			final boolean b2 = d2.retainAll(d2);
			if (b1 != b2)
				fail("d1.retainAll(d1) != d2.retainAll(d2) --> " + b1 + " != " + b2);
		} else {
			final boolean b1 = d1.retainAll(null);
			final boolean b2 = d2.retainAll(new LinkedList<>());
			if (b1 != b2)
				fail("d1.retainAll(null) != d2.retainAll(null) --> " + b1 + " != " + b2);
		}

		testEquality();
	}

	private void testRemoveAllSelf() {
		if (_random.nextBoolean()) {
			d1.clear();
			d2.clear();
		}

		final boolean b1 = d1.removeAll(d1);
		final boolean b2 = d2.removeAll(d2);

		if (b1 != b2)
			fail("d1.removeAll(d1) != d2.removeAll(d2) --> " + b1 + " != " + b2);

		testEquality();
	}

	private void testEquals() {
		if (!d1.equals(d2))
			fail("!d1.equals(d2)");
		// Das klappt nicht, weil d2=LinkedList testet, ob d1 vom Typ List ist.
		// if (d2.equals(d1) == false)
		// fail("d2.equals(d1) == false");
	}

	private void testRetainAll() {
		final LinkedList<Integer> temp = new LinkedList<>();
		for (int i = 0; i < d1.size() / 4; i++)
			temp.addLast(_random.nextInt(_MAX_VALUE));

		final boolean b1 = d1.retainAll(temp);
		final boolean b2 = d2.retainAll(temp);

		if (b1 != b2)
			fail("d1.retainAll(temp) != d2.retainAll(temp) --> " + b1 + " != " + b2);

		testEquality();
	}

	private void testRemoveAll() {
		final LinkedList<Integer> temp = getListWithSomeElementsInCommon();

		final boolean b1 = d1.removeAll(temp);
		final boolean b2 = d2.removeAll(temp);

		if (b1 != b2)
			fail("d1.removeAll(temp) != d2.removeAll(temp) --> " + b1 + " != " + b2);

		testEquality();
	}

	private void testContainsAll() {
		final LinkedList<Integer> temp = getListWithSomeElementsInCommon();

		final boolean b1 = d1.containsAll(temp);
		final boolean b2 = d2.containsAll(temp);

		if (b1 != b2)
			fail("d1.containsAll(temp) != d2.containsAll(temp) --> " + b1 + " != " + b2);
	}

	private LinkedList<Integer> getListWithSomeElementsInCommon() {
		final LinkedList<Integer> temp = new LinkedList<>();
		for (final Integer value : d1)
			if (_random.nextBoolean())
				temp.addLast(value);
		final Integer value = _random.nextInt(_MAX_VALUE);
		temp.addLast(value);
		return temp;
	}

	private void testToArrayWithType() {
		final Integer[] o1 = d1.toArray(new Integer[0]);
		final Integer[] o2 = d2.toArray(new Integer[0]);

		if (o1.length != o2.length)
			fail("d1.toArray().length != d2.toArray().length --> " + o1.length + " != " + o2.length);

		for (int i = 0; i < o1.length; i++)
			if (!o1[i].equals(o2[i]))
				fail("!o1[i].equals(o2[i]) --> " + o1[i] + " != " + o2[i]);

		final Iterator<Integer> i1 = d1.iterator();
		for (int i = 0; i < o1.length; i++) {
			final Integer value = i1.next();
			if (!o1[i].equals(value))
				fail("!o1[i].equals(value) --> " + o1[i] + " != " + value);
		}

		final Iterator<Integer> i2 = d2.iterator();
		for (int i = 0; i < o2.length; i++) {
			final Integer value = i2.next();
			if (!o2[i].equals(value))
				fail("!o2[i].equals(value) --> " + o2[i] + " != " + value);
		}

	}

	private void testToArray() {
		final Object[] o1 = d1.toArray();
		final Object[] o2 = d2.toArray();

		if (o1.length != o2.length)
			fail("d1.toArray().length != d2.toArray().length --> " + o1.length + " != " + o2.length);

		for (int i = 0; i < o1.length; i++)
			if (!o1[i].equals(o2[i]))
				fail("!o1[i].equals(o2[i]) --> " + o1[i] + " != " + o2[i]);

		final Iterator<Integer> i1 = d1.iterator();
		for (int i = 0; i < o1.length; i++) {
			final Integer value = i1.next();
			if (!o1[i].equals(value))
				fail("!o1[i].equals(value) --> " + o1[i] + " != " + value);
		}

		final Iterator<Integer> i2 = d2.iterator();
		for (int i = 0; i < o2.length; i++) {
			final Integer value = i2.next();
			if (!o2[i].equals(value))
				fail("!o2[i].equals(value) --> " + o2[i] + " != " + value);
		}
	}

	private void testIsEmpty() {
		final boolean b1 = d1.isEmpty();
		final boolean b2 = d2.isEmpty();
		if (b1 != b2)
			fail("d1.isEmpty() != d2.isEmpty() --> " + b1 + "!=" + b2);
	}

	private void testClear() {
		d1.clear();
		d2.clear();

		testEquality();
	}

	private void testDescendingIterator() {
		final Iterator<Integer> i1 = d1.descendingIterator();
		final Iterator<Integer> i2 = d2.descendingIterator();

		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				testPrintLists(true);
			final int v1 = i1.next();
			final int v2 = i2.next();
			if (v1 != v2)
				testPrintLists(true);
		}

	}

	private void testContains() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.contains(obj);
		final boolean b2 = d2.contains(obj);
		if (b1 != b2)
			fail("d1.contains(temp1) != d2.contains(temp2) --> " + b1 + "!=" + b2);
	}

	private void testRemoveObject() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.remove(obj);
		final boolean b2 = d2.remove(obj);
		if (b1 != b2)
			fail("d1.remove(temp1) != d2.remove(temp2) --> " + b1 + "!=" + b2);
	}

	private void testPop() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			final Integer i1 = d1.pop();
			final Integer i2 = d2.pop();
			if (i1 != i2)
				fail("d1.pop() != d2.pop() --> " + i1 + "!=" + i2);
		} else {
			try {
				d1.pop();
				fail("d1.pop() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.pop();
				fail("d2.pop() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testPush() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		d1.push(obj);
		d2.push(obj);
		testEquality();
	}

	private void testAddAll() {
		Deque<Integer> temp1 = new LinkedList<>();
		Deque<Integer> temp2 = new LinkedList<>();
		if (_random.nextBoolean()) {
			for (int i = 0; i < 10; i++) {
				final int value = _random.nextInt(_MAX_VALUE);
				temp1.addLast(value);
				temp2.addLast(value);
			}
		} else {
			temp1 = d1;
			temp2 = d2;
		}

		final boolean b1 = d1.addAll(temp1);
		final boolean b2 = d2.addAll(temp2);

		if (b1 != b2)
			fail("d1.addAll(temp1) != d2.addAll(temp2) --> " + b1 + "!=" + b2);

		testEquality();
	}

	private void testPeek() {
		final Integer i1 = d1.peek();
		final Integer i2 = d2.peek();
		if (i1 != i2)
			fail("d1.peek() != d2.peek() --> " + i1 + "!=" + i2);
	}

	private void testElement() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			final Integer i1 = d1.element();
			final Integer i2 = d1.element();
			if (i1 != i2)
				fail("d1.element() != d2.element() --> " + i1 + "!=" + i2);
		} else {
			try {
				d1.element();
				fail("d1.element() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.element();
				fail("d2.element() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testPoll() {
		final Integer i1 = d1.poll();
		final Integer i2 = d2.poll();
		if (i1 != i2)
			fail("d1.poll() != d2.poll() --> " + i1 + "!=" + i2);
	}

	private void testRemoveToBoolean() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.remove(obj);
		final boolean b2 = d2.remove(obj);
		if (b1 != b2)
			fail("d1.remove(" + obj + ") != d2.remove(" + obj + ") --> " + b1 + "!=" + b2);
	}

	private void testOffer() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.offer(obj);
		final boolean b2 = d2.offer(obj);
		if (b1 != b2)
			fail("d1.offer(" + obj + ") != d2.offer(" + obj + ") --> " + b1 + "!=" + b2);

	}

	private void testAdd() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.add(obj);
		final boolean b2 = d2.add(obj);
		if (b1 != b2)
			fail("d1.add(" + obj + ") != d2.add(" + obj + ") --> " + b1 + "!=" + b2);

	}

	private void testRemoveFirstOccurrence() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.removeFirstOccurrence(obj);
		final boolean b2 = d2.removeFirstOccurrence(obj);
		if (b1 != b2)
			fail("d1.removeFirstOccurrence(" + obj + ") != d2.removeFirstOccurrence(" + obj + ") --> " + b1 + "!="
					+ b2);
	}

	private void testRemoveLastOccurrence() {
		final Integer obj = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.removeLastOccurrence(obj);
		final boolean b2 = d2.removeLastOccurrence(obj);
		if (b1 != b2)
			fail("d1.removeLastOccurrence(" + obj + ") != d2.removeLastOccurrence(" + obj + ") --> " + b1 + "!=" + b2);
	}

	private void testPeekFirst() {
		if (d1.peekFirst() != d2.peekFirst())
			fail("d1.peekFirst() != d2.peekFirst() --> " + d1.peekFirst() + "!=" + d2.peekFirst());
	}

	private void testPeekLast() {
		if (d1.peekLast() != d2.peekLast())
			fail("d1.peekLast() != d2.peekLast() --> " + d1.peekLast() + "!=" + d2.peekLast());
	}

	private void testGetFirst() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.getFirst() != d2.getFirst())
				fail("d1.getFirst() != d2.getFirst() --> " + d1.getFirst() + "!=" + d2.getFirst());
		} else {
			try {
				d1.getFirst();
				fail("d1.getFirst() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.getFirst();
				fail("d2.getFirst() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testGetLast() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.getLast() != d2.getLast())
				fail("d1.getLast() != d2.getLast() --> " + d1.getLast() + "!=" + d2.getLast());
		} else {
			try {
				d1.getLast();
				fail("d1.getLast() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.getLast();
				fail("d2.getLast() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testPollFirst() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.pollFirst() != d2.pollFirst())
				fail("d1.pollFirst() != d2.pollFirst()");
		} else {
			if (d1.pollFirst() != null)
				fail("d1.pollFirst() != null");
			if (d2.pollFirst() != null)
				fail("d2.pollFirst() != null");
		}
	}

	private void testPollLast() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.pollLast() != d2.pollLast())
				fail("d1.pollLast() != d2.pollLast()");
		} else {
			if (d1.pollFirst() != null)
				fail("d1.pollLast() != null");
			if (d2.pollFirst() != null)
				fail("d2.pollLast() != null");
		}
	}

	private void testRemoveFirst() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.removeFirst() != d2.removeFirst())
				fail("d1.removeFirst() != d2.removeFirst()");
		} else {
			try {
				d1.removeFirst();
				fail("d1.removeFirst() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.removeFirst();
				fail("d2.removeFirst() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testRemoveLast() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
		if (d1.size() != 0) {
			if (d1.removeLast() != d2.removeLast())
				fail("d1.removeLast() != d2.removeLast()");
		} else {
			try {
				d1.removeLast();
				fail("d1.removeLast() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
			try {
				d2.removeLast();
				fail("d2.removeLast() != NoSuchElementException");
			} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
				// success
			}
		}
	}

	private void testSize() {
		if (d1.size() != d2.size())
			fail("d1.size() != d2.size() --> " + d1.size() + " != " + d2.size());
	}

	private void testOfferFirst() {
		final int num = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.offerFirst(num);
		final boolean b2 = d2.offerFirst(num);
		if (b1 != b2)
			fail("d1.offerFirst(num) != d2.offerFirst(num) --> " + b1 + "!=" + b2);
		testEquality();
	}

	private void testOfferLast() {
		final int num = _random.nextInt(_MAX_VALUE);
		final boolean b1 = d1.offerLast(num);
		final boolean b2 = d2.offerLast(num);
		if (b1 != b2)
			fail("d1.offerLast(num) != d2.offerLast(num) --> " + b1 + "!=" + b2);
		testEquality();
	}

	private void testAddFirst() {
		final int num = _random.nextInt(_MAX_VALUE);
		d1.addFirst(num);
		d2.addFirst(num);
		testEquality();
	}

	private void testAddLast() {
		final int num = _random.nextInt(_MAX_VALUE);
		d1.addLast(num);
		d2.addLast(num);
		testEquality();
	}

	private void testEquality() {
		final Iterator<Integer> i1 = d1.iterator();
		final Iterator<Integer> i2 = d2.iterator();

		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				testPrintLists(true);
			final int v1 = i1.next();
			final int v2 = i2.next();
			if (v1 != v2)
				testPrintLists(true);
		}
	}

	private void testPrintLists(final boolean fail) {
		System.out.println("d1 = " + d1);
		System.out.println("d2 = " + d2);
		if (fail)
			fail("Beide Collections sind nicht identisch!");
	}

}
