import { describe, test, expect, beforeEach } from "vitest";
import { StringBuilder, StringIndexOutOfBoundsException } from "~/index";

let s: StringBuilder;

describe("java.util.StringBuilder", () => {
	beforeEach(() => {
		s = new StringBuilder();
	});
	test("constructor: StringBuilder is a StringBuilder", () => {
		const ss = new StringBuilder();
		expect(ss).toBeInstanceOf(StringBuilder);
	});
	test("constructor/isEmpty: StringBuilder is empty when created", () => {
		const ss = new StringBuilder();
		expect(ss.isEmpty()).toBeTruthy();
	});
	test("length: shows size", () => {
		expect(s.length()).toBe(0);
	});
	test("capacity: shows 0", () => {
		expect(s.capacity()).toBe(0);
	});
	test("ensureCapacity: void", () => {
		expect(s.ensureCapacity(111)).toBeUndefined();
	});
	test("trimToSize: void", () => {
		expect(s.trimToSize()).toBeUndefined();
	});
	test("setLength: setzt korrekte Länge der Strings", ()=>{
		s = new StringBuilder("Bielefeld")
		expect(s.length()).toBe(9);
		s.setLength(4)
		expect(s.length()).toBe(4)
	})
	test("setLength: throws, wenn Länge unter 0", ()=>{
		expect(()=>s.setLength(-1)).toThrow(StringIndexOutOfBoundsException);
	})
});
