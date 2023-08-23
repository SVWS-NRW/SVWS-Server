import { test, expect, describe } from "vitest";
import { LinkedCollection, NoSuchElementException, Random } from "@core";
import { TestRandomComparator } from "../shared/TestComparator";
import { random_numbers } from "./random_numbers1k";

const MAX_VALUE = Number.MAX_SAFE_INTEGER;
const RND = new Random();
const d1 = new LinkedCollection();
const d2 = new LinkedCollection();

describe("TestDequeRandom", () => {
	function getListWithSomeElementsInCommon(): LinkedCollection<any> {
		const temp = new LinkedCollection();
		for (const value in d1) if (RND.nextBoolean()) temp.addLast(value);
		const value = Object(RND.nextInt(MAX_VALUE));
		temp.addLast(value);
		return temp;
	}

	function testEquality(): boolean {
		const i1 = d1.iterator();
		const i2 = d2.iterator();

		while (i1.hasNext() || i2.hasNext()) {
			if (i1.hasNext() != i2.hasNext()) return true;
			const v1 = i1.next();
			const v2 = i2.next();
			if (v1 != v2) return false;
		}
		return true;
	}

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
	 * Object[] toArray() as any[]; <br>
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
	describe.each(random_numbers)(
		"Testet grundlegende Funktionen der {@link LinkedCollection}.",
		({ a, b }) => {
			if (a === 0) {
				// Adding
				switch (b) {
					case 0:
						test("testAddFirst", () => {
							const num = Object(RND.nextInt(MAX_VALUE));
							d1.addFirst(num);
							d2.addFirst(num);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 1:
						test("testAddFirst", () => {
							const num = Object(RND.nextInt(MAX_VALUE));
							d1.addFirst(num);
							d2.addFirst(num);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 2:
						test("testAddLast", () => {
							const num = Object(RND.nextInt(MAX_VALUE));
							d1.addLast(num);
							d2.addLast(num);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 3:
						test("testOfferFirst", () => {
							const num = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.offerFirst(num);
							const b2 = d2.offerFirst(num);
							expect(b1).toBe(b2);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 4:
						test("testOfferLast", () => {
							const num = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.offerLast(num);
							const b2 = d2.offerLast(num);
							expect(b1).toBe(b2);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 5:
						test("testAdd", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.add(obj);
							const b2 = d2.add(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 6:
						test("testOffer", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.offer(obj);
							const b2 = d2.offer(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 7:
						test("testAddAll", () => {
							let temp1 = new LinkedCollection();
							let temp2 = new LinkedCollection();
							if (RND.nextBoolean()) {
								for (let i = 0; i < 10; i++) {
									const value = Object(RND.nextInt(MAX_VALUE));
									temp1.addLast(value);
									temp2.addLast(value);
								}
							} else {
								temp1 = d1;
								temp2 = d2;
							}

							const b1 = d1.addAll(temp1);
							const b2 = d2.addAll(temp2);

							expect(b1).toBe(b2);
							expect(testEquality()).toBeTruthy();
						});
						break;
					case 8:
						test("testPush", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							d1.push(obj);
							d2.push(obj);
							expect(testEquality()).toBeTruthy();
						});
						break;
				}
			}
			if (a === 1) {
				// Removing
				switch (b) {
					case 0:
						test("testRemoveFirst", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								expect(d1.removeFirst()).toBe(d2.removeFirst());
							} else {
								expect(() => d1.removeFirst()).toThrow( NoSuchElementException)
								expect(() => d2.removeFirst()).toThrow( NoSuchElementException);
							}
						});
						break;
					case 1:
						test("testRemoveLast", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								expect(d1.removeLast()).toBe(d2.removeLast());
							} else {
								expect(() => d1.removeLast()).toThrow(NoSuchElementException);
								expect(() => d2.removeLast()).toThrow(NoSuchElementException);
							}
						});
						break;
					case 2:
						test("testPollFirst", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								expect(d1.pollFirst()).toBe(d2.pollFirst());
							} else {
								expect(d1.pollFirst()).toBe(null);
								expect(d2.pollFirst()).toBe(null);
							}
						});
						break;
					case 3:
						test("testPollLast", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								expect(d1.pollLast()).toBe(d2.pollLast());
							} else {
								expect(d1.pollLast()).toBe(null);
								expect(d2.pollLast()).toBe(null);
							}
						});
						break;
					case 4:
						test("testRemoveFirstOccurrence", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.removeFirstOccurrence(obj);
							const b2 = d2.removeFirstOccurrence(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 5:
						test("testRemoveLastOccurrence", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.removeLastOccurrence(obj);
							const b2 = d2.removeLastOccurrence(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 6:
						test("testRemoveToBoolean", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.remove(obj);
							const b2 = d2.remove(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 7:
						test("testPoll", () => {
							const i1 = d1.poll();
							const i2 = d2.poll();
							expect(i1).toBe(i2);
						});
						break;
					case 8:
						test("testPop", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								const i1 = d1.pop();
								const i2 = d2.pop();
								expect(i1).toBe(i2);
							} else {
								expect(() => d1.pop()).toThrow(NoSuchElementException);
								expect(() => d1.pop()).toThrow(NoSuchElementException);
							}
						});
						break;
					case 9:
						test("testRemoveObject", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.remove(obj);
							const b2 = d2.remove(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 10:
						test.skip("testRemoveAll", () => {
							const temp = getListWithSomeElementsInCommon();

							const b1 = d1.removeAll(temp);
							const b2 = d2.removeAll(temp);

							expect(b1).toBe(b2);

							expect(testEquality()).toBeTruthy();
						});
						break;
					case 11:
						test("testRetainAll", () => {
							const temp = new LinkedCollection();
							for (let i = 0; i < d1.size() / 4; i++)
								temp.addLast(Object(RND.nextInt(MAX_VALUE)));

							const b1 = d1.retainAll(temp);
							const b2 = d2.retainAll(temp);

							expect(b1).toBe(b2);

							expect(testEquality()).toBeTruthy();
						});
						break;
					case 12:
						test("testRemoveAllSelf", () => {
							if (RND.nextBoolean()) {
								d1.clear();
								d2.clear();
							}

							const b1 = d1.removeAll(d1);
							const b2 = d2.removeAll(d2);

							expect(b1).toBe(b2);

							expect(testEquality()).toBeTruthy();
						});
						break;
					case 13:
						test("testRetainAllSelfOrNull", () => {
							if (RND.nextBoolean()) {
								d1.clear();
								d2.clear();
							}

							if (RND.nextBoolean()) {
								const b1 = d1.retainAll(d1);
								const b2 = d2.retainAll(d2);
								expect(b1).toBe(b2);
							} else {
								const b1 = d1.retainAll(null);
								const b2 = d2.retainAll(new LinkedCollection());
								expect(b1).toBe(b2);
							}

							expect(testEquality()).toBeTruthy();
						});
						break;
					case 14:
						test("testRemoveToElement", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								const i1 = d1.remove();
								const i2 = d2.remove();
								expect(i1).toBe(i2);
							} else {
								expect(() => d1.remove()).toThrow(NoSuchElementException);
								expect(() => d2.remove()).toThrow(NoSuchElementException);
							}
						});
						break;
				}
			}
			if (a === 2) {
				// Misc
				switch (b) {
					case 0:
						test("testSize", () => {
							expect(d1.size()).toBe(d2.size());
						});
						break;
					case 1:
						test("testGetFirst", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() === 0) {
								expect(() => d1.getFirst()).toThrow(NoSuchElementException);
								expect(() => d2.getFirst()).toThrow(NoSuchElementException);
							} else {
								expect(d1.getFirst()).toBe(d2.getFirst());
							}
						});
						break;
					case 2:
						test("testGetLast", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() === 0) {
								expect(() => d1.getLast()).toThrow(NoSuchElementException);
								expect(() => d2.getLast()).toThrow(NoSuchElementException);
							} else {
								expect(d1.getLast()).toBe(d2.getLast());
							}
						});
						break;
					case 3:
						test("testPeekFirst", () => {
							expect(d1.peekFirst()).toBe(d2.peekFirst());
						});
						break;
					case 4:
						test("testPeekLast", () => {
							expect(d1.peekLast()).toBe(d2.peekLast());
						});
						break;
					case 5:
						test("testElement", () => {
							expect(d1.size()).toBe(d2.size());
							if (d1.size() != 0) {
								const i1 = d1.element();
								const i2 = d1.element();
								expect(i1).toBe(i2);
							} else {
								expect(() => d1.element()).toThrow(NoSuchElementException);
								expect(() => d2.element()).toThrow(NoSuchElementException);
							}
						});
						break;
					case 6:
						test("testPeek", () => {
							const i1 = d1.peek();
							const i2 = d2.peek();
							expect(i1).toBe(i2);
						});
						break;
					case 7:
						test("testContains", () => {
							const obj = Object(RND.nextInt(MAX_VALUE));
							const b1 = d1.contains(obj);
							const b2 = d2.contains(obj);
							expect(b1).toBe(b2);
						});
						break;
					case 8:
						test("testDescendingIterator", () => {
							const i1 = d1.descendingIterator();
							const i2 = d2.descendingIterator();

							while (i1.hasNext() || i2.hasNext()) {
								expect(i1.hasNext() === i2.hasNext()).toBeTruthy();
								const v1 = i1.next();
								const v2 = i2.next();
								expect(v1).toBe(v2);
							}
						});
						break;
					case 9:
						test("testClear", () => {
							d1.clear();
							d2.clear();

							expect(testEquality()).toBeTruthy();
						});
						break;
					case 10:
						test("testIsEmpty", () => {
							const b1 = d1.isEmpty();
							const b2 = d2.isEmpty();
							expect(b1).toBe(b2);
						});
						break;
					case 11:
						test("testToArray", () => {
							const o1 = d1.toArray() as any[];
							const o2 = d2.toArray() as any[];

							expect(o1.length).toBe(o2.length);

							for (let i = 0; i < o1.length; i++)
								expect(o1[i] === o2[i]).toBeTruthy();

							const i1 = d1.iterator();
							for (let i = 0; i < o1.length; i++) {
								const value = i1.next();
								expect(o1[i] === value).toBeTruthy();
							}

							const i2 = d2.iterator();
							for (let i = 0; i < o2.length; i++) {
								const value = i2.next();
								expect(o2[i] === value).toBeTruthy();
							}
						});
						break;
					case 12:
						test("testToArrayWithType", () => {
							const o1 = d1.toArray() as any[];
							const o2 = d2.toArray() as any[];

							expect(o1.length).toBe(o2.length);

							for (let i = 0; i < o1.length; i++)
								expect(o1[i] === o2[i]).toBeTruthy();

							const i1 = d1.iterator();
							for (let i = 0; i < o1.length; i++) {
								const value = i1.next();
								expect(o1[i] === value).toBeTruthy();
							}

							const i2 = d2.iterator();
							for (let i = 0; i < o2.length; i++) {
								const value = i2.next();
								expect(o2[i] === value).toBeTruthy();
							}
						});
						break;
					case 13:
						test("testContainsAll", () => {
							const temp = getListWithSomeElementsInCommon();

							const b1 = d1.containsAll(temp);
							const b2 = d2.containsAll(temp);

							expect(b1).toBe(b2);
						});
						break;
					case 14:
						test("testEquals", () => {
							expect(d1 === d2).toBeFalsy();
							// Das klappt nicht, weil d2=LinkedCollection testet, ob d1 vom Typ List ist.
							// if (d2.equals(d1) == false)
							// fail("d2.equals(d1) == false");
						});
						break;
					case 15:
						test("testSpecialIndex", () => {
							if (d1.isEmpty()) return;

							const temp1 = new LinkedCollection(d1);
							const index = RND.nextInt(d1.size());
							const value2 = Object(RND.nextInt(MAX_VALUE));
							temp1.set(index, value2);
							const value3 = temp1.get(index);

							expect(value2).toBe(value3);
						});
						break;
					case 16:
						test("testSpecialSort", () => {
							const temp1 = new LinkedCollection(
								d1
							) as LinkedCollection<number>;
							temp1.sort(new TestRandomComparator());

							let old: null | any = null;
							for (const value of temp1) {
								if (old != null) expect(old - value).toBeLessThanOrEqual(0);
								old = value;
							}
						});
						break;
					case 17:
						test("testToString", () => {
							expect(d1.toString() === d2.toString()).toBeTruthy();
						});
						break;
					case 18:
						test("hashcode", () => {
							expect(d1.hashCode()).toBe(d2.hashCode());
						});
						break;
				}
			}
		}
	);
});

