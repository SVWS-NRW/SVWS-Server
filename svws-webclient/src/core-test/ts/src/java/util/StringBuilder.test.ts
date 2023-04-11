import { describe, test, expect, beforeEach } from "vitest";
import { IndexOutOfBoundsException, StringBuilder, StringIndexOutOfBoundsException } from "~/index";

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
	test("charAt: gibt den passenden String zurück", ()=>{
		s = new StringBuilder("Test")
		expect(s.charAt(1)).toBe("e");
	})
	test("charAt: gibt einen Fehler bei ungültigem Index zurück", ()=>{
		s = new StringBuilder("Test")
		expect(()=>s.charAt(9)).toThrow(StringIndexOutOfBoundsException);
	})
	test("codePointAt: Gibt den passenden codePoint zurück", ()=>{
		s = new StringBuilder("Karneval")
		expect(s.codePointAt(1)).toBe(97);
	})
	test("codePointAt: Gibt einen Fehler bei ungültigem Index zurück", ()=>{
		s = new StringBuilder("Karneval")
		expect(()=>s.codePointAt(9)).toThrow(StringIndexOutOfBoundsException);
	})
	test.todo("codePointBefore: Gibt den passenden codePoint zurück", ()=>{
		s = new StringBuilder("Karneval")
		expect(s.codePointAt(2)).toBe(97);
	})
	test("codePointCount: Gibt die passende Zahl von codePoints zurück", ()=>{
		s = new StringBuilder("Karneval")
		expect(s.codePointCount(1,3)).toBe(2);
	})
	test("codePointCount: Gibt einen Fehler bei ungültigem Index zurück", ()=>{
		s = new StringBuilder("Karneval")
		expect(()=>s.codePointCount(1,9)).toThrow(IndexOutOfBoundsException);
	})
});
