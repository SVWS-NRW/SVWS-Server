/*
 * Copyright 2022 Marina Bachran
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its 
 *    contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.nrw.schule.svws.core.adt.tree;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testet die Klassen {@link MinHeap} und {@link MinHeapIterator}.
 * 
 * @author Marina Bachran
 */
class TestMinHeap {

	static private MinHeap<Integer> emptyHeap;
	static private MinHeap<Integer> heap; // 1, 3, 2, 5, 4, 7, 5

	static private final List<Integer> content = Arrays.asList(1, 3, 2, 5, 4, 7, 5);
	static private final List<Integer> contained = Arrays.asList(1, 3, 2);
	static private final List<Integer> notContained = Arrays.asList(10, 30, 20);
	static private final List<Integer> partlyContained = Arrays.asList(1, 30, 2);
	static private final List<Integer> partlyContained2 = Arrays.asList(5, 17);
	static private final Integer[] heapArray = new Integer[] { 1, 3, 2, 5, 4, 7, 5 };

	/**
	 * Erstellt einen Heap mit Elementen und einen ohne für die Tests.
	 */
	@BeforeEach
	void setUp() {
		heap = new MinHeap<Integer>(Comparator.naturalOrder(), 1);
		heap.add(5);
		heap.add(5);
		heap.add(4);
		heap.add(3);
		heap.add(2);
		heap.add(7);
		heap.add(1);
		emptyHeap = new MinHeap<Integer>(Comparator.naturalOrder(), 1);
	}

	/**
	 * Test method for {@link MinHeap#MinHeap(Comparator, int)}.
	 */
	@SuppressWarnings("unused")
	@Test
	void testMinHeapComparatorOfTInt() {
		MinHeap<Integer> heap2 = new MinHeap<>((a, b) -> Integer.compare(a, b), 1);
		assertNotNull(heap2);
		assertEquals(1, heap2.capacity());
		assertThrows(IllegalArgumentException.class, () -> {
			new MinHeap<Integer>((a, b) -> Integer.compare(a, b), -10);
		});
	}

	/**
	 * Test method for {@link MinHeap#MinHeap(Comparator)}.
	 */
	@Test
	void testMinHeapComparatorOfT() {
		MinHeap<Integer> heap = new MinHeap<>((a, b) -> Integer.compare(a, b));
		assertNotNull(heap);
		assertEquals(63, heap.capacity());
	}

	/**
	 * Test method for {@link MinHeap#MinHeap(MinHeap)}.
	 */
	@Test
	void testMinHeapMinHeapOfT() {
		MinHeap<Integer> copy = new MinHeap<>(heap);
		assertEquals(heap.capacity(), copy.capacity());
		assertEquals(heap.size(), copy.size());
		assertEquals(heap.comparator(), copy.comparator());
		assertArrayEquals(heap.toArray(), copy.toArray());
	}

	/**
	 * Test method for {@link MinHeap#capacity()}.
	 */
	@Test
	void testCapacitiy() {
		assertEquals(7, heap.capacity());
	}

	/**
	 * Test method for {@link MinHeap#add(Object)}.
	 */
	@Test
	void testAdd() {
		assertFalse(emptyHeap.add(null));
		emptyHeap.add(5);
		assertArrayEquals(new Integer[] { 5 }, emptyHeap.toArray());
		emptyHeap.add(5);
		assertArrayEquals(new Integer[] { 5, 5 }, emptyHeap.toArray());
		emptyHeap.add(4);
		assertArrayEquals(new Integer[] { 4, 5, 5 }, emptyHeap.toArray());
		emptyHeap.add(3);
		assertArrayEquals(new Integer[] { 3, 4, 5, 5 }, emptyHeap.toArray());
		emptyHeap.add(2);
		assertArrayEquals(new Integer[] { 2, 3, 5, 5, 4 }, emptyHeap.toArray());
		emptyHeap.add(7);
		assertArrayEquals(new Integer[] { 2, 3, 5, 5, 4, 7 }, emptyHeap.toArray());
		emptyHeap.add(1);
		assertArrayEquals(new Integer[] { 1, 3, 2, 5, 4, 7, 5 }, emptyHeap.toArray());
		emptyHeap.add(6);
		assertArrayEquals(new Integer[] { 1, 3, 2, 5, 4, 7, 5, 6 }, emptyHeap.toArray());
	}

	/**
	 * Test method for {@link MinHeap#element()}.
	 */
	@Test
	void testElement() {
		assertEquals(1, heap.element());
		assertThrows(NoSuchElementException.class, () -> {
			emptyHeap.element();
		});
	}

	/**
	 * Test method for {@link MinHeap#offer(Object)}.
	 */
	@Test
	void testOffer() {
		heap.offer(6);
		assertArrayEquals(new Integer[] { 1, 3, 2, 5, 4, 7, 5, 6 }, heap.toArray());
	}

	/**
	 * Test method for {@link MinHeap#peek()}.
	 */
	@Test
	void testPeek() {
		assertEquals(1, heap.peek());
		assertNull(emptyHeap.peek());
	}

	/**
	 * Test method for {@link MinHeap#poll()}.
	 */
	@Test
	void testPoll() {
		assertNull(emptyHeap.poll());
		heap.poll();
		assertArrayEquals(new Integer[] { 2, 3, 5, 5, 4, 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] { 3, 4, 5, 5, 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] { 4, 5, 5, 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] { 5, 5, 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] { 5, 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] { 7 }, heap.toArray());
		heap.poll();
		assertArrayEquals(new Integer[] {}, heap.toArray());
	}

	/**
	 * Test method for {@link MinHeap#remove()}. remove() ruft poll() auf. Deshalb wird hier nur auf die Exception
	 * geprüft.
	 */
	@Test
	void testRemove() {
		assertThrows(NoSuchElementException.class, () -> {
			emptyHeap.remove();
		});
		assertEquals(1, heap.remove());
	}

	/**
	 * Test method for {@link MinHeap#size()}.
	 */
	@Test
	void testSize() {
		assertEquals(0, emptyHeap.size());
		assertEquals(7, heap.size());
		heap.poll();
		assertEquals(6, heap.size());
		heap.add(11);
		assertEquals(7, heap.size());
	}

	/**
	 * Test method for {@link MinHeap#isEmpty()}.
	 */
	@Test
	void testIsEmpty() {
		assertFalse(heap.isEmpty());
		assertTrue(emptyHeap.isEmpty());
	}

	/**
	 * Test method for {@link MinHeap#contains(Object)}.
	 */
	@Test
	void testContains() {
		assertTrue(heap.contains(5));
		assertFalse(heap.contains(10));
		assertFalse(emptyHeap.contains(1));
		assertTrue(heap.contains(2));
	}

	/**
	 * Test method for {@link MinHeap#containsAll(Collection)}.
	 */
	@Test
	void testContainsAll() {
		assertTrue(heap.containsAll(heap));
		assertTrue(heap.containsAll(contained));
		assertFalse(heap.containsAll(notContained));
		assertFalse(heap.containsAll(partlyContained));
	}

	/**
	 * Test method for {@link MinHeap#addAll(Collection)}.
	 */
	@Test
	void testAddAll() {
		assertFalse(emptyHeap.addAll(emptyHeap));
		assertTrue(heap.addAll(heap));
		heap.clear();
		heap.addAll(content);
		assertTrue(heap.addAll(partlyContained));
		assertFalse(heap.addAll(Arrays.asList(null, null)));
	}

	/**
	 * Test method for {@link MinHeap#remove(Object)}.
	 */
	@Test
	void testRemoveObject() {
		assertFalse(heap.remove(10));
		assertFalse(emptyHeap.remove(10));
		assertTrue(heap.remove(4));
		assertFalse(heap.remove(4));
		assertFalse(heap.contains(4));
		assertTrue(heap.remove(3));
		assertFalse(heap.remove(3));
		assertTrue(heap.remove(2));
		assertFalse(heap.remove(2));
		assertTrue(heap.remove(5)); // Heap enthält zwei 5en
		assertTrue(heap.remove(5)); // Heap enthält immer noch eine 5
		assertFalse(heap.remove(5));
		assertTrue(heap.remove(7));
		assertFalse(heap.remove(7));
		assertTrue(heap.remove(1));
		assertFalse(heap.remove(1));
	}

	/**
	 * Test method for {@link MinHeap#removeAll(Collection)}.
	 */
	@Test
	void testRemoveAll() {
		MinHeap<Integer> heap2 = new MinHeap<>(heap);
		assertTrue(heap2.removeAll(heap2));
		assertEquals(0, heap2.size());
		assertFalse(heap.removeAll(notContained));
		assertTrue(heap.removeAll(contained));
		heap.addAll(Arrays.asList(1, 3, 2));
		assertTrue(heap.removeAll(partlyContained));
		assertTrue(heap.removeAll(partlyContained2));
		assertFalse(emptyHeap.removeAll(emptyHeap));
	}

	/**
	 * Test method for {@link MinHeap#retainAll(Collection)}.
	 */
	@Test
	void testRetainAll() {
		assertFalse(heap.retainAll(heap));
		assertTrue(heap.retainAll(notContained));
		assertTrue(heap.isEmpty());
		heap.addAll(content);
		assertTrue(heap.retainAll(contained));
		heap.clear();
		heap.addAll(content);
		assertFalse(heap.retainAll(content));
		assertTrue(heap.retainAll(partlyContained));
		assertFalse(emptyHeap.retainAll(contained));
	}

	/**
	 * Test method for {@link MinHeap#clear()}.
	 */
	@Test
	void testClear() {
		heap.clear();
		assertEquals(0, heap.size());
	}

	/**
	 * Test method for {@link MinHeap#toArray()}.
	 */
	@Test
	void testToArray() {
		Integer[] heapContent = (Integer[]) heap.toArray();
		assertEquals(7, heapContent.length);
		assertArrayEquals(heapArray, heapContent);
	}

	/**
	 * Test method for {@link MinHeap#toArray(Object[])}.
	 */
	@Test
	void testToArrayUArray() {
		Integer[] test = new Integer[9];
		Integer[] heapContent = heap.toArray(test);
		assertEquals(9, heapContent.length);
		assertEquals(null, test[8]);
		assertArrayEquals(heapArray, Arrays.copyOf(heapContent, heapArray.length));
		test = new Integer[5];
		heapContent = heap.toArray(test);
		assertEquals(7, heapContent.length);
	}

	/**
	 * Test method for {@link MinHeap#iterator()}.
	 */
	@Test
	void testIterator() {
		final Iterator<Integer> iter = heap.iterator();
		assertNotNull(iter);
		Integer[] heapContent = heap.toArray(new Integer[heap.size()]);
		for (int i = 0; i < heapContent.length; i++) {
			assertTrue(iter.hasNext());
			Integer value = iter.next();
			assertNotNull(value);
			assertEquals(heapContent[i], value);
		}
		assertFalse(iter.hasNext());
		assertThrows(NoSuchElementException.class, () -> {
			iter.next();
		});
		final Iterator<Integer> iter2 = heap.iterator();
		iter2.next();
		heap.add(17);
		assertThrows(ConcurrentModificationException.class, () -> {
			iter2.hasNext();
		});
		assertThrows(ConcurrentModificationException.class, () -> {
			iter2.next();
		});
		emptyHeap.add(1);
		final Iterator<Integer> iter3 = emptyHeap.iterator();
		iter3.next();
		assertThrows(NoSuchElementException.class, () -> {
			iter3.next();
		});
	}

	/**
	 * Test method for {@link MinHeap#clone()}.
	 */
	@Test
	void testClone() {
		try {
			@SuppressWarnings("unchecked")
			MinHeap<Integer> h = (MinHeap<Integer>) heap.clone();
			assertEquals(heap.size(), h.size());
			assertEquals(heap, h);
		} catch (@SuppressWarnings("unused") CloneNotSupportedException e) {
			// Clone is supported...
		}
	}

	/**
	 * Test method for {@link MinHeap#toSortedArray()}.
	 */
	@Test
	void testToSortedArray() {
		Integer[] sorted = heap.toSortedArray();
		assertNotNull(sorted);
		assertEquals(heap.size(), sorted.length);
		assertTrue(heap.containsAll(Arrays.asList(sorted)));
		for (int i = 0; i < sorted.length - 1; i++)
			assertTrue(heap.comparator().compare(sorted[i], sorted[i + 1]) <= 0);
	}

	/**
	 * Test method for {@link MinHeap#toString()}.
	 */
	@Test
	void testToString() {
		assertEquals("1, 3, 2, 5, 4, 7, 5", heap.toString());
	}

	/**
	 * Test method for {@link MinHeap#hashCode()}.
	 */
	@Test
	void testHashCode() {
		int hashcode = -1604500284;
		assertEquals(hashcode, heap.hashCode());
		assertNotEquals(heap.hashCode(), emptyHeap.hashCode());
		assertEquals(heap.hashCode(), heap.hashCode());
		assertEquals(heap.hashCode(), (new MinHeap<>(heap)).hashCode());
	}

	/**
	 * Test method for {@link MinHeap#equals(Object)}.
	 */
	@Test
	void testEquals() {
		assertNotEquals(heap, emptyHeap);
		assertEquals(heap, heap);
		assertEquals(heap, new MinHeap<>(heap));
		emptyHeap.add(42);
		assertNotEquals(heap, emptyHeap);
		assertNotEquals(heap, "Irgend etwas");
		assertNotEquals(heap, null);
	}

}
