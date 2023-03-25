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
package de.svws_nrw.core.adt.collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testet die Klasse {@link LinkedCollection} 
 * 
 * @author Marina Bachran
 */
class TestLinkedCollection {

	static private LinkedCollection<Integer> coll;
	static private LinkedCollection<Integer> coll2;
	static private Vector<Integer> v2;
	static private Vector<Integer> v3;
	static private Vector<Integer> v4;
	
	static private LinkedCollection<Integer> empty;

	static private final Integer[] data = { 5, 5, 4, 3, 2, 7, 1 };
	static private final Integer[] data2 = { 3, 42, 0, 7, 8, 9 };
	static private final Integer[] data3 = { 8, 9, 22, 42 };
	static private final Integer[] data4 = { 3, 42, 0, 7, 8, 9, 21 };
	static private final Integer[] dataRemovedData2 = { 5, 5, 4, 2, 1 };
	static private final Integer[] dataRetainedData2 = { 3, 7 };
	
	
	/**
	 * Bereitet mehrere Datenstrukturen für die einzelnen Testmethoden vor.
	 */
	@BeforeEach
	void setUp() {
		coll = new LinkedCollection<>();
		for (Integer value : data)
			coll.add(value);
		coll2 = new LinkedCollection<>();
		for (Integer value : data2)
			coll2.add(value);
		v2 = new Vector<>();
		for (Integer value : data2)
			v2.add(value);
		v3 = new Vector<>();
		for (Integer value : data3)
			v3.add(value);
		v4 = new Vector<>();
		for (Integer value : data4)
			v4.add(value);
    	empty = new LinkedCollection<>();
	}

	/**
	 * Test method for {@link LinkedCollection#hashCode()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testHashCode() {
		assertEquals(1, empty.hashCode());
		assertEquals(2032292796, coll.hashCode());
	}

	/**
	 * Test method for {@link LinkedCollection#LinkedCollection()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testLinkedCollection() {
		LinkedCollection<Integer> result = new LinkedCollection<>();
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Test method for {@link LinkedCollection#LinkedCollection(LinkedCollection)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testLinkedCollectionLinkedCollectionOfQextendsE() {
		LinkedCollection<Integer> result = new LinkedCollection<>(coll);
		assertNotNull(result);
		assertEquals(coll.size(), result.size());
	}

	/**
	 * Test method for {@link LinkedCollection#size()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testSize() {
		assertEquals(0, empty.size());
		assertEquals(7, coll.size());
		coll.remove(5);
		assertEquals(6, coll.size());
		coll.add(11);
		assertEquals(7, coll.size());
	}

	/**
	 * Test method for {@link LinkedCollection#isEmpty()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testIsEmpty() {
		assertFalse(coll.isEmpty());
		assertTrue(empty.isEmpty());
	}

	/**
	 * Test method for {@link LinkedCollection#contains(Object)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testContains() {
		assertTrue(coll.contains(5));
		assertFalse(coll.contains(1234));
		assertFalse(empty.contains(1234));
	}

	/**
	 * Test method for {@link LinkedCollection#iterator()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testIterator() {
		Iterator<Integer> iter = coll.iterator();
		assertTrue(iter.hasNext());
		assertEquals(5, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(5, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(4, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(3, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(2, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(7, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
		assertThrows(NoSuchElementException.class, () -> { iter.next(); });
		Iterator<Integer> iter2 = coll.iterator();
		coll.add(11);
		assertThrows(ConcurrentModificationException.class, () -> { iter2.next(); });
	}

	/**
	 * Test method for {@link LinkedCollection#toArray()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testToArray() {
		Object[] a = coll.toArray();
		assertEquals(7, a.length);
		assertArrayEquals(data, a);
		a = empty.toArray();
		assertEquals(0, a.length);
		assertArrayEquals(new Object[0], a);
	}

	/**
	 * Test method for {@link LinkedCollection#toArray(Object[])}.
	 */
	@SuppressWarnings({ "static-method" })
	@Test
	final void testToArrayTArray() {
		Integer[] big = new Integer[10];
		Integer[] small = new Integer[4];
		Integer[] a = coll.toArray(big);
		assertEquals(10, a.length);
		Integer[] cmp = new Integer[10];
		System.arraycopy(data, 0, cmp, 0, data.length);
		assertArrayEquals(cmp, a);
		a = coll.toArray(small);
		assertEquals(7, a.length);
		assertArrayEquals(data, a);
	}

	/**
	 * Test method for {@link LinkedCollection#add(Object)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testAdd() {
		empty.add(11);
		assertEquals(1, empty.size());
		empty.add(5);
		assertEquals(2, empty.size());
		empty.add(7);
		assertEquals(3, empty.size());
		empty.add(5);
		assertEquals(4, empty.size());
		assertFalse(empty.add(null));
	}

	/**
	 * Test method for {@link LinkedCollection#remove(Object)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testRemove() {
		assertFalse(coll.remove(42));
		coll.remove(3);
		assertEquals(6, coll.size());
		coll.remove(1);
		assertEquals(5, coll.size());
		coll.remove(5);
		assertEquals(4, coll.size());
		coll.remove(5);
		assertEquals(3, coll.size());
		coll.remove(2);
		assertEquals(2, coll.size());
		coll.remove(4);
		assertEquals(1, coll.size());
		coll.remove(7);
		assertEquals(0, coll.size());
		assertFalse(coll.remove(42));
	}

	/**
	 * Test method for {@link LinkedCollection#containsAll(Collection)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testContainsAll() {
		assertTrue(coll.containsAll(coll));
		assertTrue(coll.containsAll(empty));
		assertFalse(coll.containsAll(coll2));
	}

	/**
	 * Test method for {@link LinkedCollection#addAll(Collection)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testAddAll() {
		LinkedCollection<Integer> cloneColl = new LinkedCollection<>(coll);  
		Integer[] both = coll.toArray(new Integer[coll.size() + coll2.size()]);
		Integer[] second = coll2.toArray(new Integer[0]);
		System.arraycopy(second, 0, both, coll.size(), coll2.size());
		coll.addAll(coll2);
		assertEquals(both.length, coll.size());
		assertArrayEquals(both, coll.toArray());
		
		assertFalse(coll.addAll(empty));

		empty.addAll(coll2);
		assertEquals(coll2.size(), empty.size());
		assertArrayEquals(coll2.toArray(), empty.toArray());

		v2.add(null);
		cloneColl.addAll(v2);
		assertEquals(both.length, cloneColl.size());
		assertArrayEquals(both, cloneColl.toArray());
	}

	/**
	 * Test method for {@link LinkedCollection#removeAll(Collection)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testRemoveAll() {
		assertTrue(coll.removeAll(v2));
		assertEquals(dataRemovedData2.length, coll.size());
		assertArrayEquals(dataRemovedData2, coll.toArray());
		
		assertTrue(coll2.removeAll(coll2));
		assertEquals(0, coll2.size());
		
		assertFalse(empty.removeAll(empty));
		assertEquals(0, empty.size());
	}

	/**
	 * Test method for {@link LinkedCollection#retainAll(Collection)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testRetainAll() {
		assertTrue(coll.retainAll(v2));
		assertEquals(dataRetainedData2.length, coll.size());
		assertArrayEquals(dataRetainedData2, coll.toArray());
		assertFalse(coll.retainAll(coll));
		assertFalse(empty.retainAll(coll));
		assertTrue(coll.retainAll(v3));
		assertFalse(coll2.retainAll(v2));
	}

	/**
	 * Test method for {@link LinkedCollection#clear()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testClear() {
		coll.clear();
		assertTrue(coll.isEmpty());
	}

	/**
	 * Test method for {@link LinkedCollection#equals(Object)}.
	 */
	@SuppressWarnings({ "unlikely-arg-type", "static-method" })
	@Test
	final void testEqualsObject() {
		assertFalse(coll.equals("no collection"));
		assertFalse(coll.equals(v2));
		assertFalse(coll.equals(coll2));
		assertFalse(coll.equals(v4));
		assertTrue(coll2.equals(v2));
		assertTrue(coll2.equals(coll2));
	}

	/**
	 * Test method for {@link LinkedCollection#clone()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testClone() {
		assertDoesNotThrow(() -> { assertEquals(coll, coll.clone()); });
	}

	/**
	 * Test method for {@link LinkedCollection#toString()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testToString() {
		assertEquals("[5, 5, 4, 3, 2, 7, 1]", coll.toString());
	}

	/**
	 * Test method for {@link LinkedCollection#sort(Comparator)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	final void testSort() {
		Integer[] sorted = { 1, 2, 3, 4, 5, 5, 7 };
		assertTrue(coll.sort((a, b) -> Integer.compare(a, b)));
		assertArrayEquals(sorted, coll.toArray());
		assertTrue(empty.sort((a, b) -> Integer.compare(a, b)));
		assertEquals(0, empty.size());
		assertTrue(empty.add(11));
		assertTrue(empty.sort((a, b) -> Integer.compare(a, b)));
		assertEquals(1, empty.size());
		assertEquals(11, empty.iterator().next());
		assertFalse(coll.sort(null));
	}

}
