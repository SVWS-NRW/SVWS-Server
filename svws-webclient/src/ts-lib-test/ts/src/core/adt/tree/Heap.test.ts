/* eslint-disable @typescript-eslint/ban-types */
import { describe, test, expect, beforeEach } from "vitest";
import {
	MinHeap,
	LehrerListeEintrag,
	IllegalArgumentException,
	NoSuchElementException
} from "~/index";
import {
    TestMinComparator,
//    TestMaxComparator,
} from "../../../shared/TestComparator";

const minComparator = new TestMinComparator();
//const maxComparator = new TestMaxComparator();

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
        new Number(5), //0, 3
        new Number(5), //1, 4
        new Number(4), //2, 3
        new Number(3), //3, 2
        new Number(2), //4, 1
        new Number(7), //5, 6
        new Number(1), //6, 0
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

let coll: MinHeap<Number | String | LehrerListeEintrag>;
let empty: MinHeap<Number | String | LehrerListeEintrag>;

//TODO: test for MaxHeap mit maxComparator
describe.each([s, n, l])("MinHeap mit $name", ({ data }) => {
    beforeEach(() => {
        coll = new MinHeap<Number | String | LehrerListeEintrag>(
            minComparator,
            1
        );
        for (let index = 0; index < data.length; index++) {
            coll.add(data[index]);
        }
        empty = new MinHeap<Number | String | LehrerListeEintrag>(
            minComparator,
            1
        );
    });
    test("constructor: create Heap with capacity", () => {
        const temp_coll = new MinHeap(minComparator, 1);
        expect(temp_coll).toBeInstanceOf(MinHeap);
        expect(temp_coll.capacity()).toBe(1);
    });
    test("constructor: throw when used with negative or zero capacity", () => {
        expect(() => new MinHeap(minComparator, -1)).toThrow(
            IllegalArgumentException
        );
        expect(() => new MinHeap(minComparator, 0)).toThrow(
            IllegalArgumentException
        );
    });
    test("constructor: create Heap without capacity", () => {
        const temp_coll = new MinHeap(minComparator);
        expect(temp_coll).toBeInstanceOf(MinHeap);
        expect(temp_coll.capacity()).toBe(63);
    });
    test("capacity: shows the capacity for Heap", () => {
        expect(coll.capacity()).toBe(7);
        expect(empty.capacity()).toBe(1);
    });
    test("add: adds Elements to Heap", () => {
        expect(empty.add(data[0])).toBe(true);
        // expect(empty.add(null)).toBe(false);
        expect(empty.capacity()).toBe(1);
        empty.add(data[5]);
        empty.add(data[4]);
        expect(empty.capacity()).toBe(3);
        expect(empty.poll()).toEqual(data[4]);
        expect(empty.poll()).toEqual(data[1]);
        expect(empty.poll()).toEqual(data[5]);
    });
    test("element: returns an element if avaialble", () => {
        expect(coll.element()).toEqual(data[6]);
    });
    test("element: throws if no element is available", () => {
        expect(() => empty.element()).toThrow(NoSuchElementException);
    });
    test("offer: adds Element to Heap", () => {
        expect(empty.offer(data[0])).toBeTruthy();
        expect(empty.capacity()).toBe(1);
    });
    test("poll: pops the first Element from the Heap", () => {
        expect(coll.poll()).toEqual(data[6]);
        expect(coll.poll()).toEqual(data[4]);
        expect(coll.poll()).toEqual(data[3]);
        expect(coll.poll()).toEqual(data[2]);
        expect(coll.poll()).toEqual(data[1]);
        expect(coll.poll()).toEqual(data[0]);
        expect(coll.poll()).toEqual(data[5]);
    });
    test("peek: returns root of Heap", () => {
        expect(coll.peek()).toEqual(data[6]);
    });
    test("peek: returns null if no element is available", () => {
        expect(empty.peek()).toBeNull();
    });
});
