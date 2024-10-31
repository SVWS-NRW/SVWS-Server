import { describe, test, expect, beforeEach } from "vitest";
import { l, n, s } from "../../shared/TestObjects";

import { HashSet } from "@transpiled";

let v: HashSet<unknown>;

describe.each([s, n, l])(
	"java.util.HashSet, getestet mit $name",
	({ a, b, c, d, e }) => {
		beforeEach(() => {
			v = new HashSet();
		});
		test("constructor: HashSet is a HashSet", () => {
			const vv = new HashSet();
			expect(vv).toBeInstanceOf(HashSet);
		});
		test("constructor: HashSet is empty when created", () => {
			const vv = new HashSet();
			expect(vv.size()).toEqual(0);
		});
		test("put: HashSet can put one value", () => {
			v.add(a);
			expect(v.size()).toEqual(1);
			expect(v.contains(a)).toBeTruthy();
		});
		test("remove: HashSet remove value", () => {
			v.add(a);
			expect(v.remove(a)).toEqual(true);
			expect(v.size()).toBe(0);
			v.add(a);
			expect(v.remove(b)).toEqual(false);
			expect(v.size()).toBe(1);
		});
		test("clear: empties the HashSet", () => {
			v.add(a);
			v.clear();
			expect(v.size()).toEqual(0);
		});
		test("isEmpty: returns bool of emptiness", () => {
			expect(v.isEmpty()).toBeTruthy();
			v.add(a);
			expect(v.isEmpty()).toBeFalsy();
		});
		test("put: HashSet can handle null values", () => {
			expect(v.contains(null)).toBeFalsy();
			expect(v.add(null)).toBeTruthy();
			expect(v.size()).toEqual(1);
			expect(v.contains(null)).toBeTruthy();
			expect(v.add(null)).toBeFalsy();
			expect(v.size()).toEqual(1);
			expect(v.contains(null)).toBeTruthy();
			expect(v.remove(null)).toBeTruthy();
			expect(v.size()).toEqual(0);
			expect(v.contains(null)).toBeFalsy();
			expect(v.remove(null)).toBeFalsy();
			expect(v.size()).toEqual(0);
			expect(v.contains(null)).toBeFalsy();
		});
	}
);
