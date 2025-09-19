package de.svws_nrw.core.adt.map;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.adt.collection.LinkedCollection;

/**
 * Testet die Klasse {@link AVLMap}, deren Methoden und ihre abhängige Klassen.
 *
 * @author Benjamin A. Bartsch
 */
class TestAVLMapRandom {
	private final int MAX_VALUE = 500;
	private final int ROUNDS = MAX_VALUE * MAX_VALUE;
	private final Random RANDOM = new Random(1);
	private final AVLMap<Integer, Integer> map1 = new AVLMap<>();
	private final DummyMap map2 = new DummyMap(MAX_VALUE);

	private NavigableMap<Integer, Integer> nav1 = map1;
	private NavigableMap<Integer, Integer> nav2 = map2;
	private Collection<Integer> col1 = map1.values();
	private Collection<Integer> col2 = map2.values();
	private NavigableSet<Integer> set1 = map1.navigableKeySet();
	private NavigableSet<Integer> set2 = map2.navigableKeySet();
	private Set<Entry<Integer, Integer>> ent1 = map1.entrySet();
	private Set<Entry<Integer, Integer>> ent2 = map2.entrySet();

	/**
	 * Testet die Klasse {@link AVLMap}, deren Methoden und ihre abhängige Klassen.
	 */
	@Test
	@DisplayName("Testet die AVLMap.")
	void testeAVLMap() {

		for (int i = 0; i < ROUNDS; i++) {
			testeEineRunde();
		}
	}

	private void testeEineRunde() {
		final int rnd = RANDOM.nextInt(100);
		switch (rnd) {

			// #####################################
			// ############## TEST MAP #############
			// #####################################

			case 0 -> { // firstKey
				if (map1.isEmpty() || map2.isEmpty()) {
					try {
						map1.firstKey();
						fail("map1.firstKey() --> Sollte eine NoSuchElementException werfen!");
					} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
						// success
					}
					try {
						map2.firstKey();
						fail("map2.firstKey() --> Sollte eine NoSuchElementException werfen!");
					} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
						// success
					}
				} else {
					if (unequalObjects(map1.firstKey(), map2.firstKey()))
						fail("map1.firstKey() != map2.firstKey() --> " + map1.firstKey() + " != " + map2.firstKey());
				}
			}
			case 1 -> { // lastKey
				if (map1.isEmpty() || map2.isEmpty()) {
					try {
						map1.lastKey();
						fail("map1.lastKey() --> Sollte eine NoSuchElementException werfen!");
					} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
						// success
					}
					try {
						map2.lastKey();
						fail("map2.lastKey() --> Sollte eine NoSuchElementException werfen!");
					} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
						// success
					}
				} else {
					if (unequalObjects(map1.lastKey(), map2.lastKey()))
						fail("map1.lastKey() != map2.lastKey() --> " + map1.lastKey() + " != " + map2.lastKey());
				}
			}
			case 2 -> { // keySet (übernehme navigableKeySet von nav)
				set1 = nav1.navigableKeySet();
				set2 = nav2.navigableKeySet();
			}
			case 3 -> { // values (übernehme values von nav)
				col1 = nav1.values();
				col2 = nav2.values();
				if (unequalCollections(col1, col2))
					fail("values --> +unequalCollections(col1, col2)");
			}
			case 4 -> { // entrySet (übernehme entrySet von nav)
				ent1 = nav1.entrySet();
				ent2 = nav2.entrySet();
				if (unequalSets(set1, set2))
					fail("entrySet --> unequalSets(set1, set2)");

			}
			case 5 -> { // size
				final String s1 = map1.toString();
				final String s2 = map2.toString();
				if (map1.size() != map2.size())
					fail("map1.size() != map2.size() --> " + map1.size() + " != " + map2.size());
				final String t1 = map1.toString();
				final String t2 = map2.toString();
				if (!s1.equals(t1))
					fail("map1.toString() before != map1.toString() after");
				if (!s2.equals(t2))
					fail("map2.toString() before != map2.toString() after");
			}
			case 6 -> { // isEmpty
				if (map1.isEmpty() != map2.isEmpty())
					fail("map1.isEmpty() != map2.isEmpty() --> " + map1.isEmpty() + " != " + map2.isEmpty());
			}
			case 7 -> { // containsKey
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = map1.containsKey(v);
				final boolean b2 = map2.containsKey(v);
				if (b1 != b2)
					fail("map1.containsKey(" + v + ") != map2.containsKey(" + v + ") --> " + b1 + " != " + b2);
			}
			case 8 -> { // containsValue
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = map1.containsValue(v);
				final boolean b2 = map2.containsValue(v);
				if (b1 != b2)
					fail("map1.containsValue(" + v + ") != map2.containsValue(" + v + ") --> " + b1 + " != " + b2);
			}
			case 9 -> { // get
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = map1.get(v);
				final Integer i2 = map2.get(v);
				if (unequalObjects(i1, i2))
					fail("map1.get(" + v + ") != map2.get(" + v + ") --> " + i1 + " != " + i2);
			}
			case 10 -> { // put
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = map1.put(v, v);
				final Integer i2 = map2.put(v, v);
				if (unequalObjects(i1, i2))
					fail("map1.put(" + v + ") != map2.put(" + v + ") --> " + i1 + " != " + i2);
			}
			case 11 -> { // remove
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = map1.remove(v);
				final Integer i2 = map2.remove(v);
				if (unequalObjects(i1, i2))
					fail("map1.remove(" + v + ") != map2.remove(" + v + ") --> " + i1 + " != " + i2);
			}
			case 12 -> {
				// putAll (mit temporärer AVLMap)
				final AVLMap<Integer, Integer> map = new AVLMap<>();
				for (int i = 0; i < 10; i++) {
					final int v = RANDOM.nextInt(MAX_VALUE);
					map.put(v, v);
				}
				map1.putAll(map);
				map2.putAll(map);
				if (unequalMaps(map1, map2))
					fail("putAll --> unequalMaps");

				// putAll (mit temporärer DummyMap)
				final DummyMap mapDummy = new DummyMap(MAX_VALUE);
				for (int i = 0; i < 10; i++) {
					final int v = RANDOM.nextInt(MAX_VALUE);
					mapDummy.put(v, v);
				}
				map1.putAll(mapDummy);
				map2.putAll(mapDummy);
				if (unequalMaps(map1, map2))
					fail("putAll --> unequalMaps");
			}
			case 13 -> { // clear
				if (RANDOM.nextInt(MAX_VALUE) == 0) { // etwas seltener ausführen
					map1.clear();
					map2.clear();
					if (map1.size() != 0)
						fail("clear --> map1.size() = " + map1.size());
					if (map2.size() != 0)
						fail("clear --> map2.size() = " + map2.size());
					if (unequalMaps(map1, map2))
						fail("clear --> unequalMaps");
				}
			}
			case 14 -> { // lowerEntry
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Entry<Integer, Integer> e1 = map1.lowerEntry(v);
				final Entry<Integer, Integer> e2 = map2.lowerEntry(v);
				if (unequalObjects(e1, e2))
					fail("map1.lowerEntry(" + v + ") != map2.lowerEntry(" + v + ") --> " + e1 + " != " + e2);
			}
			case 15 -> { // lowerKey
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer e1 = map1.lowerKey(v);
				final Integer e2 = map2.lowerKey(v);
				if (unequalObjects(e1, e2))
					fail("map1.lowerKey(" + v + ") != map2.lowerKey(" + v + ") --> " + e1 + " != " + e2);
			}
			case 16 -> { // floorEntry
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Entry<Integer, Integer> e1 = map1.floorEntry(v);
				final Entry<Integer, Integer> e2 = map2.floorEntry(v);
				if (unequalObjects(e1, e2))
					fail("map1.floorEntry(" + v + ") != map2.floorEntry(" + v + ") --> " + e1 + " != " + e2);
			}
			case 17 -> { // floorKey
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer e1 = map1.floorKey(v);
				final Integer e2 = map2.floorKey(v);
				if (unequalObjects(e1, e2))
					fail("map1.floorKey(" + v + ") != map2.floorKey(" + v + ") --> " + e1 + " != " + e2);
			}
			case 18 -> { // ceilingEntry
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Entry<Integer, Integer> e1 = map1.ceilingEntry(v);
				final Entry<Integer, Integer> e2 = map2.ceilingEntry(v);
				if (unequalObjects(e1, e2))
					fail("map1.ceilingEntry(" + v + ") != map2.ceilingEntry(" + v + ") --> " + e1 + " != " + e2);
			}
			case 19 -> { // ceilingKey
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer e1 = map1.ceilingKey(v);
				final Integer e2 = map2.ceilingKey(v);
				if (unequalObjects(e1, e2))
					fail("map1.ceilingKey(" + v + ") != map2.ceilingKey(" + v + ") --> " + e1 + " != " + e2);
			}
			case 20 -> { // higherEntry
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Entry<Integer, Integer> e1 = map1.higherEntry(v);
				final Entry<Integer, Integer> e2 = map2.higherEntry(v);
				if (unequalObjects(e1, e2))
					fail("map1.higherEntry(" + v + ") != map2.higherEntry(" + v + ") --> " + e1 + " != " + e2);
			}
			case 21 -> { // higherKey
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer e1 = map1.higherKey(v);
				final Integer e2 = map2.higherKey(v);
				if (unequalObjects(e1, e2))
					fail("map1.higherKey(" + v + ") != map2.higherKey(" + v + ") --> " + e1 + " != " + e2);
			}
			case 22 -> { // firstEntry
				final Entry<Integer, Integer> e1 = map1.firstEntry();
				final Entry<Integer, Integer> e2 = map2.firstEntry();
				if (unequalObjects(e1, e2))
					fail("map1.firstEntry() != map2.firstEntry() --> " + e1 + " != " + e2);
			}
			case 23 -> { // lastEntry
				final Entry<Integer, Integer> e1 = map1.lastEntry();
				final Entry<Integer, Integer> e2 = map2.lastEntry();
				if (unequalObjects(e1, e2))
					fail("map1.lastEntry() != map2.lastEntry() --> " + e1 + " != " + e2);
			}
			case 24 -> { // pollFirstEntry
				final Entry<Integer, Integer> e1 = map1.pollFirstEntry();
				final Entry<Integer, Integer> e2 = map2.pollFirstEntry();
				if (unequalObjects(e1, e2))
					fail("map1.pollFirstEntry() != map2.pollFirstEntry() --> " + e1 + " != " + e2);
			}
			case 25 -> { // pollLastEntry
				final Entry<Integer, Integer> e1 = map1.pollLastEntry();
				final Entry<Integer, Integer> e2 = map2.pollLastEntry();
				if (unequalObjects(e1, e2))
					fail("map1.pollLastEntry() != map2.pollLastEntry() --> " + e1 + " != " + e2);
			}
			case 26 -> { // descendingMap
				nav1 = map1.descendingMap();
				nav2 = map2.descendingMap();
				if (unequalMaps(nav1, nav2))
					fail("descendingMap --> unequalMaps(nav1, nav2)");
			}
			case 27 -> { // navigableKeySet
				final boolean orig = RANDOM.nextBoolean();
				set1 = orig ? map1.navigableKeySet() : nav1.navigableKeySet();
				set2 = orig ? map2.navigableKeySet() : nav2.navigableKeySet();
				if (unequalSets(set1, set2))
					fail("navigableKeySet --> unequalSets(set1, set2)");
			}
			case 28 -> { // descendingKeySet
				final boolean orig = RANDOM.nextBoolean();
				set1 = orig ? map1.descendingKeySet() : nav1.descendingKeySet();
				set2 = orig ? map2.descendingKeySet() : nav2.descendingKeySet();
				if (unequalSets(set1, set2))
					fail("descendingKeySet --> unequalSets(set1, set2)");
			}
			case 29 -> { // subMap4
				final int value1 = RANDOM.nextInt(MAX_VALUE);
				final int value2 = RANDOM.nextInt(MAX_VALUE);
				final int from = Math.min(value1, value2);
				final int to = Math.max(value1, value2);
				final boolean fromInc = RANDOM.nextBoolean();
				final boolean toInc = RANDOM.nextBoolean();
				nav1 = map1.subMap(from, fromInc, to, toInc);
				nav2 = map2.subMap(from, fromInc, to, toInc);
				if (unequalMaps(nav1, nav2))
					fail("map1.subMap(" + from + "," + fromInc + ", " + to + "," + toInc + ") != map2.subMap(...) --> "
							+ nav1 + " != " + nav2);
			}
			case 30 -> { // headMap2
				final int to = RANDOM.nextInt(MAX_VALUE);
				final boolean toInc = RANDOM.nextBoolean();
				nav1 = map1.headMap(to, toInc);
				nav2 = map2.headMap(to, toInc);
				if (unequalMaps(nav1, nav2))
					fail("map1.headMap(" + to + ", " + toInc + ") != map2.headMap(" + to + ", " + toInc + ") --> "
							+ nav1 + " != " + nav2);
			}
			case 31 -> { // tailMap2
				final int from = RANDOM.nextInt(MAX_VALUE);
				final boolean fromInc = RANDOM.nextBoolean();
				nav1 = map1.tailMap(from, fromInc);
				nav2 = map2.tailMap(from, fromInc);
				if (unequalMaps(nav1, nav2))
					fail("map1.tailMap(" + from + ", " + fromInc + ") != map2.tailMap(" + from + ", " + fromInc
							+ ") --> " + nav1 + " != " + nav2);
			}
			case 32 -> { // subMap2
				final int to = RANDOM.nextInt(MAX_VALUE);
				final int from = RANDOM.nextInt(MAX_VALUE);
				nav1 = (NavigableMap<Integer, Integer>) map1.subMap(from, to);
				nav2 = (NavigableMap<Integer, Integer>) map2.subMap(from, to);
				if (unequalMaps(nav1, nav2))
					fail("map1.subMap(" + from + ", " + to + ") != map2.subMap(" + from + ", " + to + ") --> " + nav1
							+ " != " + nav2);
			}
			case 33 -> { // headMap1
				final int to = RANDOM.nextInt(MAX_VALUE);
				nav1 = (NavigableMap<Integer, Integer>) map1.headMap(to);
				nav2 = (NavigableMap<Integer, Integer>) map2.headMap(to);
				if (unequalMaps(nav1, nav2))
					fail("map1.headMap(" + to + ") != map2.headMap(" + to + ") --> " + nav1 + " != " + nav2);
			}
			case 34 -> { // tailMap1
				final int from = RANDOM.nextInt(MAX_VALUE);
				nav1 = (NavigableMap<Integer, Integer>) map1.tailMap(from);
				nav2 = (NavigableMap<Integer, Integer>) map2.tailMap(from);
				if (unequalMaps(nav1, nav2))
					fail("map1.tailMap(" + from + ") != map2.tailMap(" + from + ") --> " + nav1 + " != " + nav2);
			}
			case 35 -> { // hashCode
				final int hashCode1 = map1.hashCode();
				final int hashCode2 = map2.hashCode();
				if (hashCode1 != hashCode2)
					fail("hashCode1 != hashCode2 --> " + hashCode1 + " != " + hashCode2);

			}
			case 36 -> { // equals TODO BAR case 36
			}

			// ########################################
			// ############## TEST SUBMAP #############
			// ########################################

			case 37 -> { // get
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = nav1.get(v);
				final Integer i2 = nav2.get(v);
				if (unequalObjects(i1, i2))
					fail("nav1.get(" + v + ") != nav2.get(" + v + ") --> " + i1 + " != " + i2);
			}

			// #####################################
			// ########## TEST COLLECTION ##########
			// #####################################

			case 40 -> { // size
				final String s1 = col1.toString();
				final String s2 = col2.toString();
				if (col1.size() != col2.size())
					fail("col1.size() != col2.size() --> " + col1.size() + " != " + col2.size());
				final String t1 = col1.toString();
				final String t2 = col2.toString();
				if (!s1.equals(t1))
					fail("col1.toString() before != col1.toString() after");
				if (!s2.equals(t2))
					fail("col2.toString() before != col2.toString() after");
			}
			case 41 -> { // isEmpty
				if (col1.isEmpty() != col2.isEmpty())
					fail("col1.isEmpty() != col2.isEmpty() --> " + col1.isEmpty() + " != " + col2.isEmpty());
			}
			case 42 -> { // contains
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = col1.contains(v);
				final boolean b2 = col2.contains(v);
				if (b1 != b2)
					fail("col1.contains(" + v + ") != col2.contains(" + v + ") --> " + b1 + " != " + b2);
			}
			case 43 -> { // iterator
				// check
				Iterator<Integer> i1 = col1.iterator();
				Iterator<Integer> i2 = col2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					final int val1 = i1.next();
					final int val2 = i2.next();
					if (unequalObjects(val1, val2))
						fail("V-iterator1 != V-iterator --> " + val1 + " != " + val2);
				}
				// ... % löschen
				i1 = col1.iterator();
				i2 = col2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					i1.next();
					i2.next();
					final boolean delete = RANDOM.nextDouble() < 0.05;
					if (delete) {
						i1.remove();
						i2.remove();
					}
				}
				try {
					i1.next();
					fail("col1.iterator.next() --> sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}
				try {
					i2.next();
					fail("col2.iterator.next() --> sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}

				if (isRemoveThrowingException(i1) != isRemoveThrowingException(i2)) {
					fail("col1.iterator.remove() != col2.iterator.remove()");
				}

			}
			case 44 -> { // toArray
				final Object[] arr1 = col1.toArray();
				final Object[] arr2 = col2.toArray();
				if (unequalArrays(arr1, arr2))
					fail("col1.toArray() != col2.toArray()");
			}
			case 45 -> { // toArray with Type
				final Integer[] arr1 = col1.toArray(new Integer[0]);
				final Integer[] arr2 = col2.toArray(new Integer[0]);
				if (unequalArrays(arr1, arr2))
					fail("col1.toArray(new Integer[0]) != col2.toArray(new Integer[0])");
			}
			case 46 -> { // add
				final int v = RANDOM.nextInt(MAX_VALUE);
				try {
					col1.add(v);
					fail("col1.add(v) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					col2.add(v);
					fail("col2.add(v) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
			}
			case 47 -> { // remove
				final int v = RANDOM.nextInt(MAX_VALUE);
				try {
					col1.remove(v);
					fail("col1.remove(v) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					col2.remove(v);
					fail("col2.remove(v) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
			}
			case 48 -> { // containsAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (int i = 0; i < 2; i++)
					col.add(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = col1.containsAll(col);
				final boolean b2 = col2.containsAll(col);
				if (b1 != b2)
					fail("col1.containsAll(col) != col2.containsAll(col) --> " + b1 + " != " + b2);
			}
			case 49 -> { // addAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (int i = 0; i < 2; i++)
					col.add(RANDOM.nextInt(MAX_VALUE));
				try {
					col1.addAll(col);
					fail("col1.addAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					col2.addAll(col);
					fail("col2.addAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
			}
			case 50 -> { // removeAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (int i = 0; i < 2; i++)
					col.add(RANDOM.nextInt(MAX_VALUE));
				try {
					col1.removeAll(col);
					fail("col1.removeAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					col2.removeAll(col);
					fail("col2.removeAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
			}
			case 51 -> { // retainAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (int i = 0; i < 2; i++)
					col.add(RANDOM.nextInt(MAX_VALUE));
				try {
					col1.retainAll(col);
					fail("col1.retainAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					col2.retainAll(col);
					fail("col2.retainAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
			}
			case 52 -> { // clear
				if (RANDOM.nextInt(MAX_VALUE) == 0) { // seltener
					col1.clear();
					col2.clear();
					if ((col1.size() != 0) || (col2.size() != 0))
						fail("col1.size() UND col2.size() muss 0 sein --> statt " + col1.size() + " UND "
								+ col2.size());
				}
			}

			// #####################################
			// ############ TEST KEYSET ############
			// #####################################
			case 53 -> { // first (or Exception)
				if (set1.isEmpty() || set2.isEmpty()) {
					if (isFirstThrowingException(set1) != isFirstThrowingException(set2))
						fail("set1.first() != set2.first()");
				} else {
					final int i1 = set1.first();
					final int i2 = set2.first();
					if (unequalObjects(i1, i2))
						fail("set1.first() != set2.first() --> " + i1 + " != " + i2);
				}
			}
			case 54 -> { // last (or Exception)
				if (set1.isEmpty() || set2.isEmpty()) {
					if (isLastThrowingException(set1) != isFirstThrowingException(set2))
						fail("set1.last() != set2.last()");
				} else {
					final int i1 = set1.last();
					final int i2 = set2.last();
					if (unequalObjects(i1, i2))
						fail("set1.last() != set2.last() --> " + i1 + " != " + i2);
				}
			}
			case 55 -> { // size
				final String s1 = set1.toString();
				final String s2 = set2.toString();
				if (set1.size() != set2.size())
					fail("set1.size() != set2.size() --> " + set1.size() + " != " + set2.size());
				final String t1 = set1.toString();
				final String t2 = set2.toString();
				if (!s1.equals(t1))
					fail("set1.toString() before != set1.toString() after");
				if (!s2.equals(t2))
					fail("set2.toString() before != set2.toString() after");
			}
			case 56 -> { // isEmpty
				if (set1.isEmpty() != set2.isEmpty())
					fail("set1.isEmpty() != set2.isEmpty() --> " + set1.isEmpty() + " != " + set2.isEmpty());
			}
			case 57 -> { // contains
				final int key = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = set1.contains(key);
				final boolean b2 = set2.contains(key);
				if (b1 != b2)
					fail("set1.contains(" + key + ") != set2.contains(" + key + ") --> " + b1 + " != " + b2);
			}
			case 58 -> { // toArray
				final Object[] arr1 = set1.toArray();
				final Object[] arr2 = set2.toArray();
				if (unequalArrays(arr1, arr2))
					fail("set1.toArray() != set2.toArray()");
			}
			case 59 -> { // toArray with Type
				final Integer[] arr1 = set1.toArray(new Integer[0]);
				final Integer[] arr2 = set2.toArray(new Integer[0]);
				if (unequalArrays(arr1, arr2))
					fail("set1.toArray(new Integer[0]) != set2.toArray(new Integer[0])");
			}
			case 60 -> { // add
				final int v = RANDOM.nextInt(MAX_VALUE);
				map1.allowKeyAlone(false);
				map2.allowKeyAlone(false);
				try {
					set1.add(v);
					fail("set1.add() --> solle eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					set2.add(v);
					fail("set2.add() --> solle eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				map1.allowKeyAlone(true);
				map2.allowKeyAlone(true);

				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = set1.add(v);
					set1.remove(v); // Sofort entfernen, da verschiedene Dummy-Values sonst zu ungleicher col führt!
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = set2.add(v);
					set2.remove(v); // Sofort entfernen, da verschiedene Dummy-Values sonst zu ungleicher col führt!
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}
				if (f1 != f2)
					fail("set1.add(" + v + ") != set2.add(" + v + ") --> failed " + f1 + " != " + f2);
				if (b1 != b2)
					fail("set1.add(" + v + ") != set2.add(" + v + ") --> " + b1 + " != " + b2);
			}
			case 61 -> { // remove
				final int v = RANDOM.nextInt(MAX_VALUE);
				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = set1.remove(v);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = set2.remove(v);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}
				if (f1 != f2)
					fail("set1.remove(" + v + ") != set2.remove(" + v + ") --> failed " + f1 + " != " + f2);
				if (b1 != b2)
					fail("set1.remove(" + v + ") != set2.remove(" + v + ") --> " + b1 + " != " + b2);
			}
			case 62 -> { // containsAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (int i = 0; i < 2; i++)
					col.add(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.containsAll(col);
				final boolean b2 = set2.containsAll(col);
				if (b1 != b2)
					fail("set1.containsAll(col) != set2.containsAll(col) --> " + b1 + " != " + b2);
			}
			case 63 -> { // addAll
				final int v = RANDOM.nextInt(MAX_VALUE);
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				col.add(v);
				// case allowKeyAlone(false)
				map1.allowKeyAlone(false);
				map2.allowKeyAlone(false);
				try {
					set1.addAll(col);
					fail("set1.addAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				try {
					set2.addAll(col);
					fail("set2.addAll(col) --> Sollte eine UnsupportedOperationException werfen!");
				} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
					// success
				}
				// case allowKeyAlone(true)
				map1.allowKeyAlone(true);
				map2.allowKeyAlone(true);
				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = set1.addAll(col);
					set1.remove(v); // Sofort entfernen, da verschiedene Dummy-Values sonst zu ungleicher col führt!
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = set2.addAll(col);
					set2.remove(v); // Sofort entfernen, da verschiedene Dummy-Values sonst zu ungleicher col führt!
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.addAll(" + v + ") != set2.addAll(" + v + ") --> failed " + f1 + " != " + f2);
				if (b1 != b2)
					fail("set1.addAll(" + v + ") != set2.addAll(" + v + ") --> " + b1 + " != " + b2);

			}
			case 64 -> { // retainAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				for (final Integer i : set1) {
					if (RANDOM.nextDouble() < 0.5) // was bleiben soll
						col.addLast(i);
				}
				if (RANDOM.nextBoolean()) { // 50% Chance auf Zufalls-KEY
					final int key = RANDOM.nextInt(MAX_VALUE);
					col.addLast(key);
				}

				final boolean b1 = set1.retainAll(col);
				final boolean b2 = set2.retainAll(col);
				if (b1 != b2)
					fail("set1.retainAll(col) != set2.retainAll(col) --> " + b1 + " != " + b2);
				if (unequalSets(set1, set2))
					fail("retainAll --> unequalSets(set1, set2)");
			}
			case 65 -> { // removeAll
				final LinkedCollection<Integer> col = new LinkedCollection<>();
				col.addLast(RANDOM.nextInt(MAX_VALUE));
				col.addLast(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.removeAll(col);
				final boolean b2 = set2.removeAll(col);
				if (b1 != b2)
					fail("set1.removeAll(col) != set2.removeAll(col) --> " + b1 + " != " + b2);
			}
			case 66 -> { // clear
				if (RANDOM.nextInt(MAX_VALUE) == 0) { // etwas seltener ausführen
					set1.clear();
					set2.clear();
					if (set1.size() != 0)
						fail("set1.clear() failed --> size = " + set1.size());
					if (set2.size() != 0)
						fail("set2.clear() failed --> size = " + set2.size());
				}
			}
			case 67 -> { // lower
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.lower(v);
				final Integer i2 = set2.lower(v);
				if (unequalObjects(i1, i2))
					fail("set1.lower(" + v + ") != set2.lower(" + v + ") --> " + i1 + " != " + i2);
			}
			case 68 -> { // floor
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.floor(v);
				final Integer i2 = set2.floor(v);
				if (unequalObjects(i1, i2))
					fail("set1.floor(" + v + ") != set2.floor(" + v + ") --> " + i1 + " != " + i2);
			}
			case 69 -> { // ceiling
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.ceiling(v);
				final Integer i2 = set2.ceiling(v);
				if (unequalObjects(i1, i2))
					fail("set1.ceiling(" + v + ") != set2.ceiling(" + v + ") --> " + i1 + " != " + i2);
			}
			case 70 -> { // higher
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.higher(v);
				final Integer i2 = set2.higher(v);
				if (unequalObjects(i1, i2))
					fail("set1.higher(" + v + ") != set2.higher(" + v + ") --> " + i1 + " != " + i2);
			}
			case 71 -> { // pollFirst
				final Integer i1 = set1.pollFirst();
				final Integer i2 = set2.pollFirst();
				if (unequalObjects(i1, i2))
					fail("set1.pollFirst() != set2.pollFirst() --> " + i1 + " != " + i2);
			}
			case 72 -> { // pollLast
				final Integer i1 = set1.pollLast();
				final Integer i2 = set2.pollLast();
				if (unequalObjects(i1, i2))
					fail("set1.pollLast() != set2.pollLast() --> " + i1 + " != " + i2);
			}
			case 73 -> { // iterator
				// check
				Iterator<Integer> i1 = set1.iterator();
				Iterator<Integer> i2 = set2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					final int val1 = i1.next();
					final int val2 = i2.next();
					if (unequalObjects(val1, val2))
						fail("set1.iterator() != set2.iterator() --> " + val1 + " != " + val2);
				}
				// ... % löschen
				i1 = set1.iterator();
				i2 = set2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					i1.next();
					i2.next();
					final boolean delete = RANDOM.nextDouble() < 0.05;
					if (delete) {
						i1.remove();
						i2.remove();
					}
				}
				try {
					i1.next();
					fail("set1.iterator.next() --> Sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}
				try {
					i2.next();
					fail("set2.iterator.next() --> Sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}

				if (isRemoveThrowingException(i1) != isRemoveThrowingException(i2))
					fail("set1.iterator.remove() != set2.iterator.remove()");

				if (isRemoveThrowingException(i1) != isRemoveThrowingException(i2))
					fail("set1.iterator.remove() != set2.iterator.remove()");

			}
			case 74 -> { // descendingSet
				final NavigableSet<Integer> s1 = set1.descendingSet();
				final NavigableSet<Integer> s2 = set2.descendingSet();
				if (unequalSets(s1, s2))
					fail("set1.descendingSet() != set2.descendingSet() --> " + s1 + " != " + s2);
			}
			case 75 -> { // descendingIterator
				// check
				Iterator<Integer> i1 = set1.descendingIterator();
				Iterator<Integer> i2 = set2.descendingIterator();
				while (i1.hasNext() || i2.hasNext()) {
					final int val1 = i1.next();
					final int val2 = i2.next();
					if (unequalObjects(val1, val2))
						fail("set1.descendingIterator() != set2.descendingIterator() --> " + val1 + " != " + val2);
				}
				// ... % löschen
				i1 = set1.iterator();
				i2 = set2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					i1.next();
					i2.next();
					final boolean delete = RANDOM.nextDouble() < 0.05;
					if (delete) {
						i1.remove();
						i2.remove();
					}
				}
				try {
					i1.next();
					fail("set1.descendingIterator.next() --> Sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}
				try {
					i2.next();
					fail("set2.descendingIterator.next() --> Sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}

				if (isRemoveThrowingException(i1) != isRemoveThrowingException(i2))
					fail("set1.descendingIterator.remove() != set2.descendingIterator.remove()");

				if (isRemoveThrowingException(i1) != isRemoveThrowingException(i2))
					fail("set1.descendingIterator.remove() != set2.descendingIterator.remove()");

			}
			case 76 -> { // subSet(4 params)
				final int value1 = RANDOM.nextInt(MAX_VALUE);
				final int value2 = RANDOM.nextInt(MAX_VALUE);
				final int from = Math.min(value1, value2);
				final int to = Math.max(value1, value2);
				final boolean fromInc = RANDOM.nextBoolean();
				final boolean toInc = RANDOM.nextBoolean();

				boolean f1 = false;
				boolean f2 = false;
				NavigableSet<Integer> s1 = null;
				NavigableSet<Integer> s2 = null;
				try {
					s1 = set1.subSet(from, fromInc, to, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.subSet(from, fromInc, to, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.subSet(4 params) != set2.subSet(4 params) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.subSet(4 params) != set2.subSet(4 params) --> " + s1 + " != " + s2);
			}
			case 77 -> { // headSet(2 params)
				final int to = RANDOM.nextInt(MAX_VALUE);
				final boolean toInc = RANDOM.nextBoolean();

				boolean f1 = false;
				boolean f2 = false;
				NavigableSet<Integer> s1 = null;
				NavigableSet<Integer> s2 = null;
				try {
					s1 = set1.headSet(to, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.headSet(to, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.headSet(2 params) != set2.headSet(2 params) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.headSet(2 params) != set2.headSet(2 params) --> " + s1 + " != " + s2);
			}
			case 78 -> { // tailSet (2 params)
				final int from = RANDOM.nextInt(MAX_VALUE);
				final boolean toInc = RANDOM.nextBoolean();

				boolean f1 = false;
				boolean f2 = false;
				NavigableSet<Integer> s1 = null;
				NavigableSet<Integer> s2 = null;
				try {
					s1 = set1.tailSet(from, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.tailSet(from, toInc);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.tailSet(2 params) != set2.tailSet(2 params) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.tailSet(2 params) != set2.tailSet(2 params) --> " + s1 + " != " + s2);
			}
			case 79 -> { // subSet (2 params)
				final int from = RANDOM.nextInt(MAX_VALUE);
				final int to = RANDOM.nextInt(MAX_VALUE);

				boolean f1 = false;
				boolean f2 = false;
				SortedSet<Integer> s1 = null;
				SortedSet<Integer> s2 = null;
				try {
					s1 = set1.subSet(from, to);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.subSet(from, to);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.subSet(2 params) != set2.subSet(2 params) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.subSet(2 params) != set2.subSet(2 params) --> " + s1 + " != " + s2);
			}
			case 80 -> { // headSet(1 param)
				final int to = RANDOM.nextInt(MAX_VALUE);

				boolean f1 = false;
				boolean f2 = false;
				SortedSet<Integer> s1 = null;
				SortedSet<Integer> s2 = null;
				try {
					s1 = set1.headSet(to);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.headSet(to);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.headSet(1 param) != set2.headSet(1 param) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.headSet(1 param) != set2.headSet(1 param) --> " + s1 + " != " + s2);
			}
			case 81 -> { // tailSet (1 param)
				final int from = RANDOM.nextInt(MAX_VALUE);

				boolean f1 = false;
				boolean f2 = false;
				SortedSet<Integer> s1 = null;
				SortedSet<Integer> s2 = null;
				try {
					s1 = set1.tailSet(from);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f1 = true;
				}
				try {
					s2 = set2.tailSet(from);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
					f2 = true;
				}

				if (f1 != f2)
					fail("set1.tailSet(1 param) != set2.tailSet(1 param) --> failed --> " + f1 + " != " + f2);
				if (!f1 && !f2 && unequalSets(s1, s2))
					fail("set1.tailSet(1 param) != set2.tailSet(1 param) --> " + s1 + " != " + s2);
			}
			// #####################################
			// ########## TEST ENTRY-SET ###########
			// #####################################

			case 85 -> { // size
				final String s1 = ent1.toString();
				final String s2 = ent2.toString();
				if (ent1.size() != ent2.size())
					fail("ent1.size() != ent2.size() --> " + ent1.size() + " != " + ent2.size());
				final String t1 = ent1.toString();
				final String t2 = ent2.toString();
				if (!s1.equals(t1))
					fail("ent1.toString() before != ent1.toString() after");
				if (!s2.equals(t2))
					fail("ent2.toString() before != ent2.toString() after");
			}
			case 86 -> { // isEmpty
				if (ent1.isEmpty() != ent2.isEmpty())
					fail("ent1.isEmpty() != ent2.isEmpty() --> " + ent1.isEmpty() + " != " + ent2.isEmpty());
			}
			case 87 -> { // contains
				final int key = RANDOM.nextInt(MAX_VALUE);
				final int val = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key;
				final Entry<Integer, Integer> e = new DummyMapEntry(key, val);
				final boolean b1 = ent1.contains(e);
				final boolean b2 = ent2.contains(e);
				if (b1 != b2)
					fail("ent1.contains(" + e + ") != ent2.contains(" + e + ") --> " + b1 + " != " + b2);
			}
			case 88 -> { // toArray
				final Object[] arr1 = ent1.toArray();
				final Object[] arr2 = ent2.toArray();
				if (unequalArrays(arr1, arr2))
					fail("ent1.toArray() != ent2.toArray()");
			}
			case 89 -> { // toArrayWithType
				final Object[] arr1 = ent1.toArray(new Entry[0]);
				final Object[] arr2 = ent2.toArray(new Entry[0]);
				if (unequalArrays(arr1, arr2))
					fail("ent1.toArray() != ent2.toArray()");
			}
			case 90 -> { // add
				final int key = RANDOM.nextInt(MAX_VALUE);
				final int val = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key;
				final Entry<Integer, Integer> e = new DummyMapEntry(key, val);

				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = ent1.add(e);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = ent2.add(e);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}

				if (f1 != f2)
					fail("ent1.add(" + e + ") != ent2.add(" + e + ") intervall failed --> " + f1 + " != " + f2);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("ent1.add(" + e + ") != ent2.add(" + e + ") --> " + b1 + " != " + b2);
			}
			case 91 -> { // remove
				final int key = RANDOM.nextInt(MAX_VALUE);
				final int val = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key;
				final Entry<Integer, Integer> e = new DummyMapEntry(key, val);

				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = ent1.remove(e);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = ent2.remove(e);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}

				if (f1 != f2)
					fail("ent1.remove(" + e + ") != ent2.remove(" + e + ") intervall failed --> " + f1 + " != " + f2);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("ent1.remove(" + e + ") != ent2.remove(" + e + ") --> " + b1 + " != " + b2);
			}
			case 92 -> { // containsAll
				final int key1 = RANDOM.nextInt(MAX_VALUE);
				final int val1 = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key1;
				final int key2 = RANDOM.nextInt(MAX_VALUE);
				final int val2 = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key2;
				final LinkedCollection<Entry<Integer, Integer>> c = new LinkedCollection<>();
				c.add(new DummyMapEntry(key1, val1));
				c.add(new DummyMapEntry(key2, val2));

				final boolean b1 = ent1.containsAll(c);
				final boolean b2 = ent2.containsAll(c);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("ent1.containsAll(" + c + ") != ent2.containsAll(" + c + ") --> " + b1 + " != " + b2);
			}
			case 93 -> { // addAll
				final int key1 = RANDOM.nextInt(MAX_VALUE);
				final int val1 = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key1;
				final int key2 = RANDOM.nextInt(MAX_VALUE);
				final int val2 = RANDOM.nextBoolean() ? RANDOM.nextInt(MAX_VALUE) : key2;
				final LinkedCollection<Entry<Integer, Integer>> c = new LinkedCollection<>();
				c.add(new DummyMapEntry(key1, val1));
				c.add(new DummyMapEntry(key2, val2));

				boolean b1 = false;
				boolean b2 = false;
				boolean f1 = false;
				boolean f2 = false;
				try {
					b1 = ent1.addAll(c);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f1 = true;
				}
				try {
					b2 = ent2.addAll(c);
				} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
					f2 = true;
				}

				if (f1 != f2)
					fail("ent1.addAll(" + c + ") != ent2.addAll(" + c + ") intervall failed --> " + f1 + " != " + f2);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("ent1.addAll(" + c + ") != ent2.addAll(" + c + ") --> " + b1 + " != " + b2);
			}
			case 94 -> { // retainAll
				final LinkedCollection<Entry<Integer, Integer>> col = new LinkedCollection<>();
				for (final Entry<Integer, Integer> e : ent1) {
					if (RANDOM.nextDouble() < 0.5) // was bleiben soll
						col.addLast(e);
				}
				if (RANDOM.nextBoolean()) { // 50% Chance auf Zufalls-Entry
					final int key = RANDOM.nextInt(MAX_VALUE);
					final int val = RANDOM.nextInt(MAX_VALUE);
					col.addLast(new DummyMapEntry(key, val));
				}
				final boolean b1 = ent1.retainAll(col);
				final boolean b2 = ent2.retainAll(col);

				if (b1 != b2)
					fail("ent1.retainAll(col) != ent1.retainAll(col) --> " + b1 + " != " + b2);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("retainAll --> unequalSets(ent1, ent2)");
			}
			case 95 -> { // removeAll
				final LinkedCollection<Entry<Integer, Integer>> col = new LinkedCollection<>();
				final Iterator<Entry<Integer, Integer>> iter1 = ent1.iterator();
				while (iter1.hasNext())
					if (RANDOM.nextBoolean()) // 50% löschen
						col.addLast(iter1.next());
				final boolean b1 = ent1.removeAll(col);
				final boolean b2 = ent2.removeAll(col);

				if (b1 != b2)
					fail("ent1.removeAll(col) != ent1.removeAll(col) --> " + b1 + " != " + b2);
				if ((b1 != b2) || unequalSets(ent1, ent2))
					fail("removeAll --> unequalSets(ent1, ent2)");
			}
			case 96 -> { // clear
				if (RANDOM.nextInt(MAX_VALUE) == 0) { // etwas seltener ausführen
					ent1.clear();
					ent2.clear();
					if (ent1.size() != 0)
						fail("ent1.clear() failed --> size = " + ent1.size());
					if (ent2.size() != 0)
						fail("ent2.clear() failed --> size = " + ent2.size());
				}
			}
			case 97 -> { // iterator
				// check
				Iterator<Entry<Integer, Integer>> i1 = ent1.iterator();
				Iterator<Entry<Integer, Integer>> i2 = ent2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					final Entry<Integer, Integer> e1 = i1.next();
					final Entry<Integer, Integer> e2 = i2.next();
					if (unequalObjects(e1, e2))
						fail("E-iterator1 != E-iterator --> " + e1 + " != " + e2);
				}
				// ... % löschen
				i1 = ent1.iterator();
				i2 = ent2.iterator();
				while (i1.hasNext() || i2.hasNext()) {
					i1.next();
					i2.next();
					final boolean delete = RANDOM.nextDouble() < 0.05;
					if (delete) {
						i1.remove();
						i2.remove();
					}
				}
				try {
					i1.next();
					fail("ent1.iterator.next() --> sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}
				try {
					i2.next();
					fail("ent2.iterator.next() --> sollte eine NoSuchElementException werfen!");
				} catch (@SuppressWarnings("unused") final NoSuchElementException ex) {
					// success
				}

				if (isRemoveEntryThrowingException(i1) != isRemoveEntryThrowingException(i2))
					fail("set1.iterator.remove() != set2.iterator.remove()");

				if (isRemoveEntryThrowingException(i1) != isRemoveEntryThrowingException(i2))
					fail("set1.iterator.remove() != set2.iterator.remove()");
			}
			case 98 -> { // misc
				if (!nav1.isEmpty()) {
					final Integer value = RANDOM.nextInt(MAX_VALUE);
					final Entry<Integer, Integer> e1 = nav1.firstEntry();
					final Entry<Integer, Integer> e2 = nav2.firstEntry();
					try {
						e1.setValue(value);
						fail("nav1.firstEntry().setValue(value) --> Sollte eine UnsupportedOperationException werfen!");
					} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
						// success
					}
					try {
						e2.setValue(value);
						fail("nav2.firstEntry().setValue(value) --> Sollte eine UnsupportedOperationException werfen!");
					} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
						// success
					}
					if (e1.equals(new Object()))
						fail("e1.equals(new Object()) --> Sollte FALSE sein!");
					if (e2.equals(new Object()))
						fail("e2.equals(new Object()) --> Sollte FALSE sein!");

					final int key3 = e1.getKey() + RANDOM.nextInt(2);
					final int val3 = e2.getValue() + RANDOM.nextInt(2);
					final DummyMapEntry e3 = new DummyMapEntry(key3, val3);
					if (e1.equals(e3) != e2.equals(e3))
						fail("e1.equals(e3) != e2.equals(e3) --> " + e1.equals(e3) + " != " + e2.equals(e3));

					map1.comparator();
					try {
						map2.comparator();
						fail("map2.comparator() --> Sollte eine UnsupportedOperationException werfen!");
					} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
						// success
					}

					set1.comparator();
					try {
						set2.comparator();
						fail("set2.comparator() --> Sollte eine UnsupportedOperationException werfen!");
					} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
						// success
					}

					nav1.comparator();
					try {
						nav2.comparator();
						fail("nav2.comparator() --> Sollte eine UnsupportedOperationException werfen!");
					} catch (@SuppressWarnings("unused") final UnsupportedOperationException ex) {
						// success
					}
				}

				if (!map1.equals(map2))
					fail("map1.equals(map2) == false");
				if (!map2.equals(map1))
					fail("map2.equals(map1) == false");
				if (!map1.equals(map1))
					fail("map1.equals(map1) == false");
				if (!map2.equals(map2))
					fail("map2.equals(map2) == false");

			}
			default -> {
				/**/ }
		} // ... end of switch
	}

	private static boolean isLastThrowingException(final NavigableSet<Integer> set) {
		try {
			set.last();
			return false;
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return true;
		}
	}

	private static boolean isFirstThrowingException(final NavigableSet<Integer> set) {
		try {
			set.first();
			return false;
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return true;
		}
	}

	private static boolean isRemoveThrowingException(final Iterator<Integer> i) {
		try {
			i.remove();
			return false;
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return true;
		}
	}

	private static boolean isRemoveEntryThrowingException(final Iterator<Entry<Integer, Integer>> i) {
		try {
			i.remove();
			return false;
		} catch (@SuppressWarnings("unused") final Exception ex) {
			return true;
		}
	}

	private static boolean unequalSets(final Set<Entry<Integer, Integer>> e1, final Set<Entry<Integer, Integer>> e2) {
		if (e1.size() != e2.size())
			return true;

		final Iterator<Entry<Integer, Integer>> i1 = e1.iterator();
		final Iterator<Entry<Integer, Integer>> i2 = e2.iterator();
		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				return true;
			final Entry<Integer, Integer> v1 = i1.next();
			final Entry<Integer, Integer> v2 = i2.next();
			if (unequalObjects(v1, v2))
				return true;
		}

		return false;
	}

	private static boolean unequalSets(final SortedSet<Integer> s1, final SortedSet<Integer> s2) {
		if (s1.size() != s2.size())
			return true;

		final Iterator<Integer> i1 = s1.iterator();
		final Iterator<Integer> i2 = s2.iterator();
		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				return true;
			final int v1 = i1.next();
			final int v2 = i2.next();
			if (unequalObjects(v1, v2))
				return true;
		}

		return false;
	}

	private static boolean unequalArrays(final Object[] arr1, final Object[] arr2) {
		if (arr1.length != arr2.length)
			return true;
		for (int i = 0; i < arr1.length; i++)
			if (unequalObjects(arr1[i], arr2[i]))
				return true;
		return false;
	}

	private static boolean unequalMaps(final SortedMap<Integer, Integer> m1, final SortedMap<Integer, Integer> m2) {
		if (m1.size() != m2.size())
			return true;

		final Iterator<Integer> i1 = m1.keySet().iterator();
		final Iterator<Integer> i2 = m2.keySet().iterator();
		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				return true;
			final int v1 = i1.next();
			final int v2 = i2.next();
			if (unequalObjects(v1, v2))
				return true;
		}

		return false;
	}

	private static boolean unequalCollections(final Collection<Integer> m1, final Collection<Integer> m2) {
		if (m1.size() != m2.size())
			return true;

		final Iterator<Integer> i1 = m1.iterator();
		final Iterator<Integer> i2 = m2.iterator();
		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext())
				return true;
			final int v1 = i1.next();
			final int v2 = i2.next();
			if (unequalObjects(v1, v2))
				return true;
		}

		return false;
	}

	private static boolean unequalObjects(final Object a, final Object b) {
		return (a == null) ? (b != null) : !a.equals(b);
	}

}
