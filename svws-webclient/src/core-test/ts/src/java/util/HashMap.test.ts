import { describe, test, expect, beforeEach } from "vitest";
import {l,n,s} from "../../shared/TestObjects";

import { HashMap } from "~/index";

let v: HashMap<unknown, unknown>;

describe.each([s, n, l])(
    "java.util.HashMap, getestet mit $name",
    ({ a, b, c, d, e }) => {
        beforeEach(() => {
            v = new HashMap();
        });
        test("constructor: HashMap is a HashMap", () => {
            const vv = new HashMap();
            expect(vv).toBeInstanceOf(HashMap);
        });
        test("constructor: HashMap is empty when created", () => {
            const vv = new HashMap();
            expect(vv.size()).toEqual(0);
        });
        test("put: HashMap can put null", () => {
            expect(v.put(null, null)).toBeNull();
        });
        test("put: HashMap can put one KV pair", () => {
            v.put(a,b);
            expect(v.size()).toEqual(1);
            expect(v.containsKey(a)).toBeTruthy();
            expect(v.containsValue(b)).toBeTruthy();
        });
        test("get: HashMap get KV pair", () => {
            v.put(a,b);
            expect(v.get(a)).toEqual(b);
        });
        test("remove: HashMap remove KV pair", () => {
            v.put(a,b);
            expect(v.remove(a)).toEqual(b);
            expect(v.size()).toBe(0);
        });
        test.todo("putAll: HashMap puts all KV pairs from other Map", () => {
            const vv = new HashMap();
            vv.put(a,b);
            vv.put(c,d);
            v.putAll(vv);
        expect(v.size()).toEqual(2);
            expect(v.containsKey(a)).toBeTruthy();
            expect(v.containsValue(b)).toBeTruthy();
            expect(v.containsKey(c)).toBeTruthy();
            expect(v.containsValue(d)).toBeTruthy();
        });
        test("clear: empties the HashMap", () => {
            v.put(a,b);
            v.clear();
            expect(v.size()).toEqual(0);
        });
        test("isEmpty: returns bool of emptiness", () => {
            expect(v.isEmpty()).toBeTruthy();
            v.put(a,b);
            expect(v.isEmpty()).toBeFalsy();
        });
    }
);
