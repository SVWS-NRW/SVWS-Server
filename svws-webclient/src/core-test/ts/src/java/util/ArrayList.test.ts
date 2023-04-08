import { describe, test, expect, beforeEach } from "vitest";
import { ArrayList, ArrayListEnumerator, ArrayIndexOutOfBoundsException, NullPointerException } from "~/index";
import { l,n,s } from "../../shared/TestObjects";
import { TestConsumer } from "../../shared/TestConsumer";
import { TestMaxComparator } from "../../shared/TestComparator";

const maxComparator = new TestMaxComparator();

let v: ArrayList<unknown>;

describe.each([s, n, l])("java.util.ArrayList, getestet mit $name", ({ a, b, c, d, e }) => {
	beforeEach(() => {
		v = new ArrayList();
		v.add(a);
	});
	test("constructor: ArrayList is a ArrayList", () => {
		const vv = new ArrayList();
		expect(vv).toBeInstanceOf(ArrayList);
	});
	test("constructor: ArrayList is empty when created", () => {
		const vv = new ArrayList();
		expect(vv.size()).toEqual(0);
	});
	test("constructor: ArrayList has one element", () => {
		expect(v.size()).toEqual(1);
	});
	test("add: ArrayList can add null", () => {
		expect(() => v.add(null)).toThrow(NullPointerException);
	});
	test("add: ArrayList can add one element", () => {
		v.add(b);
		expect(v.size()).toEqual(2);
		expect(v.contains(a)).toBeTruthy();
		expect(v.contains(b)).toBeTruthy();
	});
	test("add: ArrayList contains 'c' element in second position", () => {
		v.add(c);
		expect(v.indexOf(c)).toEqual(1);
	});
	test("add: ArrayList add at parcular position position", () => {
		v.add(b);
		v.add(c);
		v.add(1, "a+");
		expect(v.indexOf("a+")).toEqual(1);
	});
	test("addAll: add Collection to ArrayList", () => {
		//
	});
	test("addElement: ArrayList adds element", () => {
		v.addElement(b);
		expect(v.contains(b)).toBeTruthy();
	});
	test("capacity: returns size of ArrayList", () => {
		v.add(c);
		expect(v.capacity()).toEqual(2);
		v.add(c);
		expect(v.capacity()).toEqual(3);
	});
	test("size: returns size of ArrayList", () => {
		v.add(c);
		expect(v.size()).toEqual(2);
		v.add(c);
		expect(v.size()).toEqual(3);
	});
	test("clear: empes e ArrayList", () => {
		v.add(c);
		v.clear();
		expect(v.size()).toEqual(0);
	});
	test("isEmpty: returns bool of emptiness", () => {
		v.add(c);
		expect(v.isEmpty()).toBeFalsy();
		v.clear();
		expect(v.isEmpty()).toBeTruthy();
	});
	test("clone: returns copy of ArrayList", () => {
		v.add(c);
		const v2 = v.clone();
		expect(v.size()).toEqual(2);
		expect(v).not.toBe(v2);
		v.remove(c);
		expect(v.contains(c)).toBeFalsy();
		expect(v2.contains(c)).toBeTruthy();
	});
	test("removeElementAt: ArrayList throws on wrong index", () => {
		expect(() => v.removeElementAt(1)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("removeElementAt: ArrayList throws on negative index", () => {
		expect(() => v.removeElementAt(-2)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("removeElementAt: ArrayList removes 'c' element in second position", () => {
		v.add(c);
		v.removeElementAt(1);
		expect(v.contains(c)).toBeFalsy();
		expect(v.contains(a)).toBeTruthy();
		expect(v.size()).toEqual(1);
	});
	test("removeElement: ArrayList removes 'b' element", () => {
		v.add(b);
		expect(v.removeElement(b)).toBeTruthy();
		expect(v.contains(b)).toBeFalsy();
	});
	test("remove: ArrayList 'c' element is removed", () => {
		v.add(c);
		expect(v.remove(c)).toBeTruthy();
		expect(v.contains(c)).toBeFalsy();
		expect(v.size()).toEqual(1);
	});
	test("removeElementAt: ArrayList firstelement is removed", () => {
		v.add(c);
		v.removeElementAt(0);
		expect(v.contains(a)).toBeFalsy();
		expect(v.size()).toEqual(1);
	});
	test("copyInto: ArrayList copies elements into array", () => {
		const array = [1, 2, 3];
		v.copyInto(array);
		expect(array).toEqual([a, 2, 3]);
		v.add(b);
		v.add(c);
		v.add(d);
		v.add(e);
		v.copyInto(array);
		expect(array).toEqual([a, b, c, d, e]);
	});
	test("elementAt: ArrayList throws on wrong index", () => {
		expect(() => v.elementAt(-1)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("elementAt: returns element from index", () => {
		expect(v.elementAt(0)).toEqual(a);
	});
	test("firstElement: returns firselement", () => {
		expect(v.firstElement()).toEqual(a);
	});
	test("lastElement: returns laselement", () => {
		v.add(b);
		expect(v.lastElement()).toEqual(b);
	});
	test("setElementAt: ArrayList throws on wrong index", () => {
		expect(() => v.setElementAt(c, 3)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("setElementAt: ArrayList throws on negative index", () => {
		expect(() => v.setElementAt(c, -2)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("setElementAt: ArrayList sets element at index", () => {
		v.setElementAt(b, 0);
		expect(v.elementAt(0)).toEqual(b);
	});
	test("toArray: returns array of elements", () => {
		expect(v.toArray()).toEqual([a]);
		v.add(b);
		expect(v.toArray()).toEqual([a, b]);
	});
	test("toArray: returns given array with elements", () => {
		const array = [1, 2, 3];
		v.toArray(array);
		expect(array).toMatchSnapshot();
	});
	test.skip("toString: returns string of elements", () => {
		expect(v.toString()).toEqual(`[${a.toString()}]`);
		v.add(b);
		expect(v.toString()).toEqual(`[${a.toString()}, ${b.toString()}]`);
	});
	test("get: returns element at index", () => {
		expect(v.get(0)).toEqual(a);
	});
	test("get: throw at wrong index", () => {
		expect(() => v.get(2)).toThrow(ArrayIndexOutOfBoundsException);
		expect(() => v.get(-1)).toThrow(ArrayIndexOutOfBoundsException);
	});
	test("set: sets element at index", () => {
		v.set(0, b);
		expect(v.get(0)).toEqual(b);
	});
	test("set: throw at wrong index", () => {
		expect(() => v.set(2, c)).toThrow(ArrayIndexOutOfBoundsException);
		expect(() => v.set(-1, c)).toThrow(ArrayIndexOutOfBoundsException);
	});
	test("trimToSize: does nothing in TS", () => {
		v.trimToSize();
		expect(v.capacity()).toBe(1);
	});
	test("ensureCapacity: does nothing in TS", () => {
		v.ensureCapacity(2);
		expect(v.capacity()).toBe(1);
	});
	test("setSize: sets e size of e test", () => {
		v.setSize(2);
		expect(v.size()).toBe(2);
	});
	test("elements: returns an instance of ArrayListEnumerator", () => {
		expect(v.elements()).toBeInstanceOf(ArrayListEnumerator);
		expect(v.elements().nextElement()).toEqual(a);
	});
	test("equals: checks if object is equal to test", () => {
		const v2 = new ArrayList();
		v2.add(a);
		expect(v.equals(v2)).toBeTruthy();
		v2.add(b);
		expect(v.equals(v2)).toBeFalsy();
	});
	test("indexOf: returns e index of a given element", () => {
		expect(v.indexOf(a)).toBe(0);
		v.add(b);
		expect(v.indexOf(b)).toBe(1);
		expect(v.indexOf(c)).toBe(-1);
		expect(v.indexOf(a, 1)).toBe(-1);
		expect(v.indexOf(a, 0)).toBe(0);
		expect(v.indexOf(b, 0)).toBe(1);
	});
	test("lastIndexOf: returns e last index of a given element", () => {
		expect(v.lastIndexOf(a)).toBe(0);
		v.add(b);
		expect(v.lastIndexOf(b)).toBe(1);
		expect(v.lastIndexOf(c)).toBe(-1);
		expect(v.lastIndexOf(a, 1)).toBe(0);
		expect(v.lastIndexOf(a, 0)).toBe(0);
		v.add(a);
		expect(v.lastIndexOf(a, 0)).toBe(0);
		expect(v.lastIndexOf(a)).toBe(2);
	});
	test("hashCode: returns e hash code of e test", () => {
		expect(v.hashCode()).toBeDefined();
	});
	test("insertElementAt: inserts element at given position", () => {
		v.insertElementAt(b, 1);
		expect(v.elementAt(1)).toEqual(b);
		expect(v.size()).toEqual(2);
	});
	test("containsAll: whether incoming Collection is part of ArrayList", () => {
		const v2 = new ArrayList();
		v2.add(a);
		expect(v.containsAll(v2)).toBeTruthy();
		v2.add(b);
		expect(v.containsAll(v2)).toBeFalsy();
	});
	test("addAll: adds all elements of incoming Collection", () => {
		const v2 = new ArrayList();
		v2.add(a);
		v2.add(b);
		v.addAll(v2);
		expect(v.size()).toBe(3);
		expect(v.elementAt(0)).toEqual(a);
		expect(v.elementAt(1)).toEqual(a);
	});
	test("adAll: add all incoming elements from Collection beginning from index", () => {
		const v2 = new ArrayList();
		v2.add(a);
		v2.add(b);
		v.addAll(1, v2);
		expect(v.size()).toBe(3);
		expect(v.elementAt(0)).toEqual(a);
		expect(v.elementAt(1)).toEqual(a);
		expect(v.elementAt(2)).toEqual(b);
	});
	test("forEach: Consumer receives Elements", () => {
		v.add(b);
		const c = new TestConsumer();
		v.forEach(c);
		expect(c.value).toEqual([a, b]);
	});
	test("removeAll: removes all elements of incoming Collection", () => {
		const v2 = new ArrayList();
		v2.add(b);
		v.add(b);
		v.add(c);
		v.removeAll(v2);
		expect(v.size()).toBe(2);
		expect(v.elementAt(0)).toEqual(a);
		expect(v.elementAt(1)).toEqual(c);
	});
	test("iterator: returns an Iterator for the ArrayList", () => {
		v.add(b);
		v.add(c);
		const it = v.iterator();
		expect(it.next()).toEqual(a);
		expect(it.next()).toEqual(b);
		expect(it.next()).toEqual(c);
		expect(it.hasNext()).toBeFalsy();
	});
	test("symbol iterator:", () => {
		v.add(b);
		v.add(c);
		for (const e of v) {
			expect(e).toBeDefined();
			expect(e).toBeInstanceOf(Object);
		}
	});
	test("listIterator: returns an Iterator for the ArrayList", () => {
		v.add(b);
		v.add(c);
		const it = v.listIterator();
		expect(it.next()).toEqual(a);
		expect(it.next()).toEqual(b);
		expect(it.next()).toEqual(c);
		expect(it.hasNext()).toBeFalsy();
	});
	test("listIterator: returns an Iterator for the ArrayList starting at index", () => {
		v.add(b);
		v.add(c);
		const it = v.listIterator(1);
		expect(it.next()).toEqual(b);
		expect(it.next()).toEqual(c);
		expect(it.hasNext()).toBeFalsy();
	});
	test("retainAll: only keeps Elements given in Collection", () => {
		const v2 = new ArrayList();
		v2.add(b);
		v.add(b);
		v.add(c);
		v.retainAll(v2);
		expect(v.size()).toBe(1);
		expect(v.elementAt(0)).toEqual(b);
	});
	test.todo("subList: returns partial List from index to index", () => {
		//const v2 = v.subList(1, 2);
		//expect(v2.size()).toBe(1);
		//expect(v2.elementAt(0)).toEqual(b);
	});
	test.todo(
		"removeIf: remove elements from ArrayList if they meet condition",
		() => {
			//
		}
	);
	test.todo("spliterator: creates a Spliterator instance", () => {
		//expect(v.spliterator()).toBeInstanceOf(ArrayListSpliterator);
	});
	test("sort: Sortiere Elemente mit Comparator Funktion", () => {
		v.add(b);
		v.add(c);
		expect(v.elementAt(0)).toEqual(a);
		v.sort(maxComparator);
		expect(v.elementAt(0)).toEqual(c);
	});
});
