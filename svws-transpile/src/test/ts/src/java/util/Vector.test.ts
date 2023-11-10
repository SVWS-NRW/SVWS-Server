import { describe, test, expect, beforeEach } from "vitest";
import { Vector, VectorEnumerator, ArrayIndexOutOfBoundsException, NullPointerException } from "@transpiled";
import { l,n,s } from "../../shared/TestObjects";
import { TestConsumer } from "../../shared/TestConsumer";
import { TestMaxComparator } from "../../shared/TestComparator";

const maxComparator = new TestMaxComparator();

let v: Vector<unknown>;

describe.each([s, n, l])("java.util.Vector, getestet mit $name", ({ a, b, c, d, e }) => {
	beforeEach(() => {
		v = new Vector();
		v.add(a);
	});
	test("constructor: Vector is a Vector", () => {
		const vv = new Vector();
		expect(vv).toBeInstanceOf(Vector);
	});
	test("constructor: Vector is empty when created", () => {
		const vv = new Vector();
		expect(vv.size()).toEqual(0);
	});
	test("constructor: Vector has one element", () => {
		expect(v.size()).toEqual(1);
	});
	test("add: Vector can add null", () => {
		expect(() => v.add(null)).toThrow(NullPointerException);
	});
	test("add: Vector can add one element", () => {
		v.add(b);
		expect(v.size()).toEqual(2);
		expect(v.contains(a)).toBeTruthy();
		expect(v.contains(b)).toBeTruthy();
	});
	test("add: Vector contains 'c' element in second position", () => {
		v.add(c);
		expect(v.indexOf(c)).toEqual(1);
	});
	test("add: Vector add at parcular position position", () => {
		v.add(b);
		v.add(c);
		v.add(1, "a+");
		expect(v.indexOf("a+")).toEqual(1);
	});
	test("addAll: add Collection to Vector", () => {
		//
	});
	test("addElement: Vector adds element", () => {
		v.addElement(b);
		expect(v.contains(b)).toBeTruthy();
	});
	test("capacity: returns size of Vector", () => {
		v.add(c);
		expect(v.capacity()).toEqual(2);
		v.add(c);
		expect(v.capacity()).toEqual(3);
	});
	test("size: returns size of Vector", () => {
		v.add(c);
		expect(v.size()).toEqual(2);
		v.add(c);
		expect(v.size()).toEqual(3);
	});
	test("clear: empes e Vector", () => {
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
	test("clone: returns copy of Vector", () => {
		v.add(c);
		const v2 = v.clone();
		expect(v.size()).toEqual(2);
		expect(v).not.toBe(v2);
		v.remove(c);
		expect(v.contains(c)).toBeFalsy();
		expect(v2.contains(c)).toBeTruthy();
	});
	test("removeElementAt: Vector throws on wrong index", () => {
		expect(() => v.removeElementAt(1)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("removeElementAt: Vector throws on negative index", () => {
		expect(() => v.removeElementAt(-2)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("removeElementAt: Vector removes 'c' element in second position", () => {
		v.add(c);
		v.removeElementAt(1);
		expect(v.contains(c)).toBeFalsy();
		expect(v.contains(a)).toBeTruthy();
		expect(v.size()).toEqual(1);
	});
	test("removeElement: Vector removes 'b' element", () => {
		v.add(b);
		expect(v.removeElement(b)).toBeTruthy();
		expect(v.contains(b)).toBeFalsy();
	});
	test("remove: Vector 'c' element is removed", () => {
		v.add(c);
		expect(v.remove(c)).toBeTruthy();
		expect(v.contains(c)).toBeFalsy();
		expect(v.size()).toEqual(1);
	});
	test("removeElementAt: Vector firstelement is removed", () => {
		v.add(c);
		v.removeElementAt(0);
		expect(v.contains(a)).toBeFalsy();
		expect(v.size()).toEqual(1);
	});
	test("copyInto: Vector copies elements into array", () => {
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
	test("elementAt: Vector throws on wrong index", () => {
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
	test("setElementAt: Vector throws on wrong index", () => {
		expect(() => v.setElementAt(c, 3)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("setElementAt: Vector throws on negative index", () => {
		expect(() => v.setElementAt(c, -2)).toThrow(
			ArrayIndexOutOfBoundsException
		);
	});
	test("setElementAt: Vector sets element at index", () => {
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
	test("toString: returns string of elements", () => {
		const string = v.toString();
		const [obj] = JSON.parse(string);
		expect(obj).toEqual(a)
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
	test("elements: returns an instance of VectorEnumerator", () => {
		expect(v.elements()).toBeInstanceOf(VectorEnumerator);
		expect(v.elements().nextElement()).toEqual(a);
	});
	test("equals: checks if object is equal to test", () => {
		const v2 = new Vector();
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
	test("containsAll: whether incoming Collection is part of Vector", () => {
		const v2 = new Vector();
		v2.add(a);
		expect(v.containsAll(v2)).toBeTruthy();
		v2.add(b);
		expect(v.containsAll(v2)).toBeFalsy();
	});
	test("addAll: adds all elements of incoming Collection", () => {
		const v2 = new Vector();
		v2.add(a);
		v2.add(b);
		v.addAll(v2);
		expect(v.size()).toBe(3);
		expect(v.elementAt(0)).toEqual(a);
		expect(v.elementAt(1)).toEqual(a);
	});
	test("adAll: add all incoming elements from Collection beginning from index", () => {
		const v2 = new Vector();
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
		const v2 = new Vector();
		v2.add(b);
		v.add(b);
		v.add(c);
		v.removeAll(v2);
		expect(v.size()).toBe(2);
		expect(v.elementAt(0)).toEqual(a);
		expect(v.elementAt(1)).toEqual(c);
	});
	test("iterator: returns an Iterator for the Vector", () => {
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
		}
	});
	test("listIterator: returns an Iterator for the Vector", () => {
		v.add(b);
		v.add(c);
		const it = v.listIterator();
		expect(it.next()).toEqual(a);
		expect(it.next()).toEqual(b);
		expect(it.next()).toEqual(c);
		expect(it.hasNext()).toBeFalsy();
	});
	test("listIterator: returns an Iterator for the Vector starting at index", () => {
		v.add(b);
		v.add(c);
		const it = v.listIterator(1);
		expect(it.next()).toEqual(b);
		expect(it.next()).toEqual(c);
		expect(it.hasNext()).toBeFalsy();
	});
	test("retainAll: only keeps Elements given in Collection", () => {
		const v2 = new Vector();
		v2.add(b);
		v.add(b);
		v.add(c);
		v.retainAll(v2);
		expect(v.size()).toBe(1);
		expect(v.elementAt(0)).toEqual(b);
	});
	test("sort: Sortiere Elemente mit Comparator Funktion", () => {
		v.add(b);
		v.add(c);
		expect(v.elementAt(0)).toEqual(a);
		v.sort(maxComparator);
		expect(v.elementAt(0)).toEqual(c);
	});
});
