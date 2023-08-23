import { describe, expect, test } from "vitest";
import { JavaString } from "@transpiled";

describe("java.util.StringBuilder", () => {
	test("contains: detects a substring", () => {
		expect(JavaString.contains("Bielefeld", "feld")).toBeTruthy();
	});
	test("contains: return false when substring null", () => {
		expect(JavaString.contains("Bielefeld", null)).toBeFalsy();
	});
	test("indexOf: returns index of Substring", () => {
		expect(JavaString.indexOf("Bielefeld", "feld")).toBe(5);
	});
	test("indexOf: returns index of Substring or -1 if null", () => {
		expect(JavaString.indexOf("Bielefeld", null)).toBe(-1);
	});
	test("indexOf: can also use fromIndex", () => {
		expect(JavaString.indexOf("Bielefeld", "e", 4)).toBe(4);
	});
	test("replaceFirst: replaces all characters identified by regex", () => {
		expect(JavaString.replaceFirst("Bielefeld", "e", "a")).toBe("Bialefeld")
	});
	test("replaceAll: replaces all characters identified by regex", ()=>{
		expect(JavaString.replaceAll("Bielefeld", "e", "a")).toBe("Bialafald")
	})
	test("compareTo: compares two strings", ()=>{
		expect(JavaString.compareTo("a", "b")).toBe(-1);
		expect(JavaString.compareTo("b", "a")).toBe(1);
		expect(JavaString.compareTo("a", "a")).toBe(0);
		expect(JavaString.compareTo("A", "a")).toBe(-32);
		expect(JavaString.compareTo("Bielefeld", "Herford")).toBe(-6);
		expect(JavaString.compareTo("Bielefeld", null)).toBe(-1);
	})
	test.skip("compareToIgnoreCase: compares two strings", ()=>{
		expect(JavaString.compareToIgnoreCase("a", "b")).toBe(-1);
		expect(JavaString.compareToIgnoreCase("A", "b")).toBe(-1);
		expect(JavaString.compareToIgnoreCase("b", "A")).toBe(1);
		expect(JavaString.compareToIgnoreCase("a", "A")).toBe(0);
		expect(JavaString.compareToIgnoreCase("Bielefeld", "Herford")).toBe(-6);
		expect(JavaString.compareToIgnoreCase("Bielefeld", null)).toBe(-1);
	})
	test("equalsIgnoreCase: compares two strings", ()=>{
		expect(JavaString.equalsIgnoreCase("A", "b")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("b", "A")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("a", "A")).toBeTruthy();
		expect(JavaString.equalsIgnoreCase("Bielefeld", "Herford")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("Bielefeld", null)).toBeFalsy();
	})
	test("format: returns a formatted string", ()=>{
		expect(JavaString.format("Hey, ich komme aus %s.", "Bielefeld")).toBe("Hey, ich komme aus Bielefeld.")
		expect(JavaString.format("Hey, hast Du mal %d Euro für mich?", 5)).toBe("Hey, hast Du mal 5 Euro für mich?")
		expect(JavaString.format("Hey, hast Du mal %s Euro für mich?", 5)).toBe("Hey, hast Du mal 5 Euro für mich?")
	})
});
