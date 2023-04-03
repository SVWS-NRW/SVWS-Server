package de.svws_nrw.core.adt.set;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.adt.collection.LinkedCollection;

/**
 * Testet die Klasse {@link AVLSet} mit randomisierten Daten, indem alle Operationen auf der Dummy-Klasse
 * {@link DummySet} simultan durchgeführt werden.
 *
 * @author Benjamin A. Bartsch
 */
public class TestAVLSetRandom {
	private final int MAX_VALUE = 1000;
	private final int ROUNDS = MAX_VALUE * MAX_VALUE;
	private final Random RANDOM = new Random(1);
	private final AVLSet<Integer> set1 = new AVLSet<>();
	private final DummySet set2 = new DummySet(MAX_VALUE);

	/**
	 * Testet die Klasse {@link AVLSet} mit randomisierten Daten, indem alle Operationen auf der Dummy-Klasse
	 * {@link DummySet} simultan durchgeführt werden.
	 */
	@Test
	@DisplayName("Testet die Klasse mit randomisierten Daten.")
	void testeSet() {
		set1.clear();
		set2.clear();
		for (int i = 0; i < ROUNDS; i++) {
			testeEineRunde();
		}
	}

	private void testeEineRunde() {
		switch (RANDOM.nextInt(21)) {
			case 0 -> { // first
				if (set1.size() > 0)
					if (unequal(set1.first(), set2.first()))
						fail("set1.first() != set2.first() --> " + set1.first() + " != " + set2.first());
			}
			case 1 -> { // last
				if (set1.size() > 0)
					if (unequal(set1.last(), set2.last()))
						fail("set1.last() != set2.last() --> " + set1.last() + " != " + set2.last() + " BUT "
								+ set1.last().equals(set2.last()));
			}
			case 2 -> { // size
				if (set1.size() != set2.size())
					fail("set1.size() != set2.size() --> " + set1.size() + " != " + set2.size());
			}
			case 3 -> { // isEmpty
				final boolean b1 = set1.isEmpty();
				final boolean b2 = set2.isEmpty();
				if (b1 != b2)
					fail("set1.isEmpty() != set2.isEmpty() --> " + b1 + " != " + b2);
			}
			case 4 -> { // contains
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = set1.contains(v);
				final boolean b2 = set2.contains(v);
				if (b1 != b2)
					fail("set1.contains(" + v + ") != set2.contains(" + v + ") --> " + b1 + " != " + b2);
			}
			case 5 -> { // Object[] toArray()
				final Object[] oArr1 = set1.toArray();
				final Object[] oArr2 = set2.toArray();
				for (int i = 0; i < Math.max(oArr1.length, oArr2.length); i++) {
					final Integer i1 = (Integer) oArr1[i];
					final Integer i2 = (Integer) oArr2[i];
					if (unequal(i1, i2))
						fail("oArr1[" + i + "] != oArr2[" + i + "] --> " + i1 + " != " + i2);
				}
			}
			case 6 -> { // T[] toArray()
				final Integer[] tArr1 = set1.toArray(new Integer[0]);
				final Integer[] tArr2 = set2.toArray(new Integer[0]);
				for (int i = 0; i < Math.max(tArr1.length, tArr2.length); i++) {
					final Integer i1 = tArr1[i];
					final Integer i2 = tArr2[i];
					if (unequal(i1, i2))
						fail("oArr1[" + i + "] != oArr2[" + i + "] --> " + i1 + " != " + i2);
				}
			}
			case 7 -> { // add
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = set1.add(v);
				final boolean b2 = set2.add(v);
				if (b1 != b2)
					fail("set1.add(" + v + ") != set2.add(" + v + ") --> " + b1 + " != " + b2);
			}
			case 8 -> { // remove
				final int v = RANDOM.nextInt(MAX_VALUE);
				final boolean b1 = set1.remove(v);
				final boolean b2 = set2.remove(v);
				if (b1 != b2)
					fail("set1.remove(" + v + ") != set2.remove(" + v + ") --> " + b1 + " != " + b2);
			}
			case 9 -> { // containsAll
				final LinkedCollection<Integer> c = new LinkedCollection<>();
				for (int i = 0; i < MAX_VALUE / 5; i++)
					c.addLast(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.containsAll(c);
				final boolean b2 = set2.containsAll(c);
				if (b1 != b2)
					fail("set1.containsAll(" + c + ") != set2.containsAll(" + c + ") --> " + b1 + " != " + b2);
			}
			case 10 -> { // addAll
				final LinkedCollection<Integer> c = new LinkedCollection<>();
				for (int i = 0; i < 10; i++)
					c.addLast(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.addAll(c);
				final boolean b2 = set2.addAll(c);
				if (b1 != b2)
					fail("set1.addAll(" + c + ") != set2.addAll(" + c + ") --> " + b1 + " != " + b2);
			}
			case 11 -> { // retainAll
				final LinkedCollection<Integer> c = new LinkedCollection<>();
				for (int i = 0; i < 10; i++)
					c.addLast(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.retainAll(c);
				final boolean b2 = set2.retainAll(c);
				if (b1 != b2)
					fail("set1.retainAll(" + c + ") != set2.retainAll(" + c + ") --> " + b1 + " != " + b2);
				checkEquality();
			}
			case 12 -> { // removeAll
				final LinkedCollection<Integer> c = new LinkedCollection<>();
				for (int i = 0; i < 10; i++)
					c.addLast(RANDOM.nextInt(MAX_VALUE));
				final boolean b1 = set1.removeAll(c);
				final boolean b2 = set2.removeAll(c);
				if (b1 != b2)
					fail("set1.removeAll(" + c + ") != set2.removeAll(" + c + ") --> " + b1 + " != " + b2);
			}
			case 13 -> { // clear
				set1.clear();
				set2.clear();
				final boolean b1 = set1.isEmpty();
				final boolean b2 = set2.isEmpty();
				if (b1 != b2)
					fail("set1.clear()/isEmpty?, set2.clear/isEmpty? --> " + b1 + " != " + b2);
			}
			case 14 -> { // lower
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.lower(v);
				final Integer i2 = set2.lower(v);
				if (unequal(i1, i2))
					fail("set1.lower(" + v + ") != set2.lower(" + v + ") --> " + i1 + " != " + i2);
			}
			case 15 -> { // floor
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.floor(v);
				final Integer i2 = set2.floor(v);
				if (unequal(i1, i2))
					fail("set1.floor(" + v + ") != set2.floor(" + v + ") --> " + i1 + " != " + i2);
			}
			case 16 -> { // ceiling
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.ceiling(v);
				final Integer i2 = set2.ceiling(v);
				if (unequal(i1, i2))
					fail("set1.ceiling(" + v + ") != set2.ceiling(" + v + ") --> " + i1 + " != " + i2);
			}
			case 17 -> { // higher
				final int v = RANDOM.nextInt(MAX_VALUE);
				final Integer i1 = set1.higher(v);
				final Integer i2 = set2.higher(v);
				if (unequal(i1, i2))
					fail("set1.higher(" + v + ") != set2.higher(" + v + ") --> " + i1 + " != " + i2);
			}
			case 18 -> { // pollFirst
				final Integer i1 = set1.pollFirst();
				final Integer i2 = set2.pollFirst();
				if (unequal(i1, i2))
					fail("set1.pollFirst() != set2.pollFirst() --> " + i1 + " != " + i2);
			}
			case 19 -> { // pollLast
				final Integer i1 = set1.pollLast();
				final Integer i2 = set2.pollLast();
				if (unequal(i1, i2))
					fail("set1.pollLast() != set2.pollLast() --> " + i1 + " != " + i2);
			}
			case 20 -> { // iterator
				final Iterator<Integer> iter1 = set1.iterator();
				final Iterator<Integer> iter2 = set2.iterator();
				while (iter1.hasNext() || iter2.hasNext()) {
					final Integer i1 = iter1.next();
					final Integer i2 = iter2.next();
					if (unequal(i1, i2))
						fail("iter1.next() != iter2.next() --> " + i1 + " != " + i2);
				}
			}
			default -> throw new IllegalArgumentException("Unexpected value");
		}
	}

	private void checkEquality() {
		final Integer[] t1 = set1.toArray(new Integer[0]);
		final Integer[] t2 = set2.toArray(new Integer[0]);
		if (t1.length != t2.length) {
			System.out.println(Arrays.toString(t1));
			System.out.println(Arrays.toString(t2));
			fail("checkEquality failed!");
		}
	}

	private static boolean unequal(final Integer a, final Integer b) {
		return (a == null) ? (b != null) : !a.equals(b);
	}

}
