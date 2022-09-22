/* eslint-disable @typescript-eslint/ban-types */
import { describe, test, expect, beforeEach } from "vitest";
import {
	LinkedCollection,
	LinkedCollectionIterator,
	LehrerListeEintrag,
	ConcurrentModificationException,
	JavaIterator,
	NoSuchElementException,
	Vector
} from "~/index";
import { TestMaxComparator } from "../../../shared/TestComparator";

const maxComparator = new TestMaxComparator();

const l0 = new LehrerListeEintrag();
const l1 = new LehrerListeEintrag();
const l2 = new LehrerListeEintrag();
const l3 = new LehrerListeEintrag();
const l4 = new LehrerListeEintrag();
const l5 = new LehrerListeEintrag();
const l7 = new LehrerListeEintrag();
const l8 = new LehrerListeEintrag();
const l9 = new LehrerListeEintrag();
const l42 = new LehrerListeEintrag();
const l = {
    name: "LehrerListeEintrag",
    data: [l5, l5, l4, l3, l2, l7, l1],
    data2: [l3, l42, l0, l7, l8, l9],
};
const s = {
    name: "String",
    data: [
        new String("5"),
        new String("5"),
        new String("4"),
        new String("3"),
        new String("2"),
        new String("7"),
        new String("1"),
    ],
    data2: [
        new String("3"),
        new String("42"),
        new String("0"),
        new String("7"),
        new String("8"),
        new String("9"),
    ],
};
const n = {
    name: "Number",
    data: [
        new Number(5),
        new Number(5),
        new Number(4),
        new Number(3),
        new Number(2),
        new Number(7),
        new Number(1),
    ],
    data2: [
        new Number(3),
        new Number(42),
        new Number(0),
        new Number(7),
        new Number(8),
        new Number(9),
    ],
};

let coll: LinkedCollection<Number | String | LehrerListeEintrag>;
let coll2: LinkedCollection<Number | String | LehrerListeEintrag>;
let empty: LinkedCollection<Number | String | LehrerListeEintrag>;
let v2: Vector<Number | String | LehrerListeEintrag>;

describe.each([s, n, l])("LinkedCollection mit $name", ({ data, data2 }) => {
    beforeEach(() => {
        coll = new LinkedCollection();
        for (let index = 0; index < data.length; index++) {
            coll.add(data[index]);
        }
        coll2 = new LinkedCollection();
        for (let index = 0; index < data2.length; index++) {
            coll2.add(data2[index]);
        }
        v2 = new Vector();
        for (let index = 0; index < data2.length; index++) {
            v2.add(data2[index]);
        }
        empty = new LinkedCollection();
    });
    test.skip("hashCode: zero for empty Collection", () => {
        expect(empty.hashCode()).toBe(0);
    });
    test.skip("hashCode: hash with elements in Collection", () => {
        // hier gehen die Hashes auseinander. Richtig?
        expect(coll.hashCode()).toBe(2032292796);
    });
    test("constructor: empty Collection", () => {
        const result = new LinkedCollection();
        expect(result).toBeInstanceOf(LinkedCollection);
        expect(result.size()).toBe(0);
    });
    test("constructor: Collection with elements from other Collection", () => {
        const result = new LinkedCollection(coll);
        expect(result).toBeInstanceOf(LinkedCollection);
        expect(result.size()).toBe(data.length);
    });
    test("size: empty Collection", () => {
        expect(empty.size()).toBe(0);
    });
    test("size: Collection with elements", () => {
        // scheitert bei remove(Number) an:	if (((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.lang.Object'))) || (__param0 === null)) {
        expect(coll.size()).toBe(data.length);
        coll.remove(data[1]);
        expect(coll.size()).toBe(data.length - 1);
        coll.add(data[0]);
        expect(coll.size()).toBe(data.length);
    });
    test("isEmpty: empty Collection", () => {
        expect(empty.isEmpty()).toBe(true);
    });
    test("isEmpty: Collection with elements", () => {
        expect(coll.isEmpty()).toBe(false);
    });
    test("contains: empty Collection", () => {
        expect(empty.contains(data[0])).toBe(false);
    });
    test("contains: Collection with elements", () => {
        expect(coll.contains(data[0])).toBe(true);
        expect(coll.contains(data2[1])).toBe(false);
    });
    test("containsAll: empty Collection", () => {
        expect(coll.containsAll(coll)).toBe(true);
        expect(empty.containsAll(coll)).toBe(false);
    });
    test("containsAll: Collection with elements", () => {
        expect(coll.containsAll(empty)).toBe(true);
        expect(coll.containsAll(coll2)).toBe(false);
    });
    test("add: add to empty Collection", () => {
        expect(empty.add(data[0])).toBeTruthy();
        expect(empty.size()).toBe(1);
        expect(empty.contains(data[0])).toBe(true);
    });
    test("add: add null returns false", () => {
        expect(coll.add(null)).toBe(false);
    });
    test("addAll: returns a collection with all elemtens added", () => {
        const size = coll.size() + coll2.size();
        expect(coll.addAll(coll2)).toBeTruthy();
        expect(coll.size()).toBe(size);
    });
    test("addAll: adding null returns false", () => {
        expect(coll.addAll(null)).toBeFalsy();
    });
    test("remove: removes Elements from Collection", () => {
        expect(coll.remove(data[0])).toBeTruthy();
        expect(coll.size()).toBe(data.length - 1);
        expect(coll.contains(data[0])).toBe(true);
    });
    test("remove: removing null returns false", () => {
        expect(coll.remove(null)).toBeFalsy();
    });
    test("removeAll: returns false when used with null", () => {
        expect(coll.removeAll(null)).toBeFalsy();
    });
    test("removeAll: removes all Elements from Collection that are in other Collection", () => {
        expect(coll.removeAll(coll2)).toBeTruthy();
        expect(coll.size()).toBe(data.length - 2);
    });
    test("iterator: returns an Iterator", () => {
        const iter: JavaIterator<unknown> = coll.iterator();
        expect(iter).toBeInstanceOf(LinkedCollectionIterator);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[0]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[1]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[2]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[3]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[4]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[5]);
        expect(iter.hasNext()).toBe(true);
        expect(iter.next()).toBe(data[6]);
        expect(iter.hasNext()).toBe(false);
    });
    test("iterator: throws if #next doesn't exist", () => {
        const iter: JavaIterator<unknown> = coll.iterator();
        iter.next();
        iter.next();
        iter.next();
        iter.next();
        iter.next();
        iter.next();
        iter.next();
        expect(() => iter.next()).toThrow(NoSuchElementException);
    });
    test("iterator: throws if element is added during iteration", () => {
        const iter: JavaIterator<unknown> = coll.iterator();
        coll.add(data[0]);
        expect(() => iter.next()).toThrow(ConcurrentModificationException);
    });
    test("toArray: returns an array", () => {
        const result: Array<unknown> = coll.toArray();
        expect(result).toBeInstanceOf(Array);
        expect(result.length).toBe(data.length);
        expect(result).toEqual(data);
    });
    test("toArray: returns an array with the correct length when used with Array<T>", () => {
        const result: Array<unknown> = coll2.toArray(coll.toArray());
        expect(result).toBeInstanceOf(Array);
        expect(result.length).toBe(coll.size());
    });
    test("retainAll: returns false when used with empty collection", () => {
        expect(empty.retainAll(coll)).toBeFalsy();
    });
    test("retainAll: returns true when used with null", () => {
        expect(coll.retainAll(null)).toBeTruthy();
        expect(coll.size()).toBe(0);
    });
    test("retainAll: returns true when used with Collection with elements", () => {
        expect(coll.retainAll(coll2)).toBeTruthy();
        expect(coll.size()).toBe(2);
    });
    test("clear: empties the Collection", () => {
        coll.clear();
        expect(coll.size()).toBe(0);
    });
    test("equals: asserts equality of Collections", () => {
        expect(coll.equals(coll)).toBeTruthy();
        expect(coll.equals(v2)).toBeFalsy();
        expect(coll.equals(coll2)).toBeFalsy();
        expect(coll2.equals(v2)).toBeTruthy();
    });
    test("clone: returns a clone of Collection", () => {
        const clone: unknown = coll.clone();
        expect(clone).toBeInstanceOf(LinkedCollection);
        const cloneColl: LinkedCollection<unknown> =
            coll.clone() as LinkedCollection<unknown>;
        expect(cloneColl.size()).toBe(cloneColl.size());
        expect(cloneColl.containsAll(cloneColl)).toBeTruthy();
        expect(cloneColl.equals(cloneColl)).toBeTruthy();
    });
    test("toString: returns a String wiht the Elements", () => {
        expect(coll.toString()).toBeDefined();
    });
    test("sort: sorts the Collection according to Comparator", () => {
        expect(coll.sort(maxComparator)).toBeTruthy();
        expect(coll.getFirst()).toEqual(data[5]);
    });
    test("sort: sorts the Collection according to Comparator with null", () => {
        expect(coll.sort(null)).toBeFalsy();
    });
    test("poll: returns the first value", () => {
        expect(coll.poll()).toBe(data[0]);
    });
    test("poll: returns null if Collection is empty", () => {
        expect(empty.poll()).toBeNull();
    });
    test("peek: returns the first value", () => {
        expect(coll.peek()).toBe(data[0]);
    });
    test("peek: returns null if Collection is empty", () => {
        expect(empty.peek()).toBeNull();
    });
    test("element: returns the first value", () => {
        expect(coll.element()).toBe(data[0]);
    });
    test("element: throws if Collection is empty", () => {
        expect(() => empty.element()).toThrow(NoSuchElementException);
    });
    test("addFirst: adds Element to the head of the Colleciton", () => {
        coll.addFirst(data[7]);
        expect(coll.getFirst()).toBe(data[7]);
    });
    test("addLast: adds Element to the tail of the Colleciton", () => {
        coll.addLast(data[0]);
        expect(coll.getLast()).toBe(data[0]);
    });
    test("offerFirst: adds Element to the head of the Colleciton", () => {
        expect(coll.offerFirst(data[7])).toBeTruthy();
        expect(coll.getFirst()).toBe(data[7]);
    });
    test("offerLast: adds Element to the tail of the Colleciton", () => {
        expect(coll.offerLast(data[0])).toBeTruthy();
        expect(coll.getLast()).toBe(data[0]);
    });
    test("removeFirst: removes Element from the head of the Colleciton", () => {
        expect(coll.removeFirst()).toBe(data[0]);
        expect(coll.getFirst()).toBe(data[1]);
    });
    test("removeLast: removes Element from the tail of the Colleciton", () => {
        expect(coll.removeLast()).toBe(data[6]);
        expect(coll.getLast()).toBe(data[5]);
    });
    test("removeFirstOccurrence: removes first occurence of Element from the head of the Colleciton", () => {
        coll.removeFirstOccurrence(data[0]);
        expect(coll.getFirst()).toEqual(data[0]);
    });
    test("removeLastOccurrence: removes last occurence of Element from the tail of the Colleciton", () => {
        coll.removeLastOccurrence(data[2]);
        expect(coll.get(2)).toBe(data[3]);
    });
    test("push: adds Element to head of Collection", () => {
        coll.push(data[7]);
        expect(coll.getFirst()).toBe(data[7]);
    });
    test("pop: removes Element from head of Collection", () => {
        coll.pop();
        coll.pop();
        expect(coll.getFirst()).toBe(data[2]);
    });
    test("peekFirst: returns first Element of Collection", () => {
        expect(coll.peekFirst()).toBe(data[0]);
    });
    test("peekLast: returns last Element of Collection", () => {
        expect(coll.peekLast()).toBe(data[6]);
    });
    test("pollFirst: returns first Element of Collection and removes it", () => {
        expect(coll.pollFirst()).toBe(data[0]);
    });
    test("pollLast: returns last Element of Collection and removes it", () => {
        expect(coll.pollLast()).toBe(data[6]);
    });
    test("pollLast: returns null on empty Collection", () => {
        expect(empty.pollLast()).toBeNull();
    });
    test("offerFirst: returns true if Element was added to head of Collection", () => {
        expect(coll.offerFirst(data[7])).toBeTruthy();
    });
    test("offerLast: returns true if Element was added to tail of Collection", () => {
        expect(coll.offerLast(data[0])).toBeTruthy();
    });
    test("set: add Element to Collection at position of index", () => {
        expect(coll.set(1, data[6])).toBe(data[1]);
        expect(coll.get(1)).toBe(data[6]);
    });
    test("get: get Element from index in Collection", () => {
        expect(coll.get(1)).toBe(data[1]);
    });
    test("offer: returns true when Element was added successfully", () => {
        expect(coll.offer(data[7])).toBeTruthy();
    });
    test("offer: returns false when no element was added", () => {
        expect(coll.offer(null)).toBeFalsy();
    });
    test("getFirst: returns first Element", () => {
        expect(coll.getFirst()).toBe(data[0]);
    });
    test("getLast: returns last Element from Collection", () => {
        expect(coll.getLast()).toBe(data[6]);
    });
    test("getFirst: throws on empty Collection", () => {
        expect(() => empty.getFirst()).toThrow(NoSuchElementException);
    });
    test("getLast: throws on empty Collection", () => {
        expect(() => empty.getLast()).toThrow(NoSuchElementException);
    });
});
