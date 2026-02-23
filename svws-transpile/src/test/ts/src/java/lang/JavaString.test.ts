import { describe, expect, test } from "vitest";
import { JavaString } from "../../../../../main/resources/typescript/java/lang/JavaString";

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
		expect(JavaString.replaceFirst("Bielefeld", "e", "a")).toBe("Bialefeld");
	});
	test("replaceAll: replaces all characters identified by regex", () => {
		expect(JavaString.replaceAll("Bielefeld", "e", "a")).toBe("Bialafald");
	});
	test("compareTo: compares two strings", () => {
		expect(JavaString.compareTo("a", "b")).toBeLessThan(0);
		expect(JavaString.compareTo("b", "a")).toBeGreaterThan(0);
		expect(JavaString.compareTo("a", "a")).toBe(0);
		expect(JavaString.compareTo("a", "A")).toBeLessThan(0);
		expect(JavaString.compareTo("Bielefeld", "Herford")).toBeLessThan(0);
		expect(JavaString.compareTo("Bielefeld", null)).toBeLessThan(0);
	});
	test("compareToIgnoreCase: compares two strings", () => {
		expect(JavaString.compareToIgnoreCase("a", "b")).toBeLessThan(0);
		expect(JavaString.compareToIgnoreCase("A", "b")).toBeLessThan(0);
		expect(JavaString.compareToIgnoreCase("b", "A")).toBeGreaterThan(0);
		expect(JavaString.compareToIgnoreCase("a", "A")).toBe(0);
		expect(JavaString.compareToIgnoreCase("Bielefeld", "Herford")).toBeLessThan(0);
		expect(JavaString.compareToIgnoreCase("Bielefeld", null)).toBeLessThan(0);
	});
	test("equalsIgnoreCase: compares two strings", () => {
		expect(JavaString.equalsIgnoreCase("A", "b")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("b", "A")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("a", "A")).toBeTruthy();
		expect(JavaString.equalsIgnoreCase("Bielefeld", "Herford")).toBeFalsy();
		expect(JavaString.equalsIgnoreCase("Bielefeld", null)).toBeFalsy();
	});
	test("format: returns a formatted string", () => {
		expect(JavaString.format("Hey, ich komme aus %s.", "Bielefeld")).toBe("Hey, ich komme aus Bielefeld.");
		expect(JavaString.format("Hey, hast Du mal %d Euro für mich?", 5)).toBe("Hey, hast Du mal 5 Euro für mich?");
		expect(JavaString.format("Hey, hast Du mal %s Euro für mich?", 5)).toBe("Hey, hast Du mal 5 Euro für mich?");
	});
	test("format: further tests", () => {
		expect(JavaString.format("Ein großes %s.", "Haus")).toBe("Ein großes Haus.");
		expect(JavaString.format("Hallo %s!", "Welt")).toBe("Hallo Welt!");
		expect(JavaString.format("Farbe: %s", "Blau")).toBe("Farbe: Blau");
		expect(JavaString.format("%s und %s", "Start", "Ende")).toBe("Start und Ende");
		expect(JavaString.format("%s", "abc")).toBe("abc");
		expect(JavaString.format("Zahl: %d", 42)).toBe("Zahl: 42");
		expect(JavaString.format("1 + 1 = %d", 2)).toBe("1 + 1 = 2");
		expect(JavaString.format("Jahr: %d", 2024)).toBe("Jahr: 2024");
		expect(JavaString.format("Von %d bis %d", 3, 7)).toBe("Von 3 bis 7");
		expect(JavaString.format("%d", -5)).toBe("-5");
		expect(JavaString.format("Wahr: %b", true)).toBe("Wahr: true");
		expect(JavaString.format("Falsch: %b", false)).toBe("Falsch: false");
		expect(JavaString.format("Ist aktiv: %b", true)).toBe("Ist aktiv: true");
		expect(JavaString.format("%b und %b", true, false)).toBe("true und false");
		expect(JavaString.format("%b", false)).toBe("false");
		expect(JavaString.format("Name: %s, Alter: %d", "Alice", 30)).toBe("Name: Alice, Alter: 30");
		expect(JavaString.format("Kurs %s hat %d Schüler", "GE", 25)).toBe("Kurs GE hat 25 Schüler");
		expect(JavaString.format("ID: %d, Bezeichnung: %s", 7, "Mathe")).toBe("ID: 7, Bezeichnung: Mathe");
		expect(JavaString.format("%s%d", "a", 42)).toBe("a42");
		expect(JavaString.format("%d Punkte für %s", 100, "Alice")).toBe("100 Punkte für Alice");
		expect(JavaString.format("Name: %s, Aktiv: %b", "Bob", true)).toBe("Name: Bob, Aktiv: true");
		expect(JavaString.format("Fach %s, Schriftlich: %b", "Deutsch", false)).toBe("Fach Deutsch, Schriftlich: false");
		expect(JavaString.format("%b ist %s", true, "Wahrheit")).toBe("true ist Wahrheit");
		expect(JavaString.format("Status: %b, Grund: %s", false, "unbekannt")).toBe("Status: false, Grund: unbekannt");
		expect(JavaString.format("%b%s", false, "test")).toBe("falsetest");
		expect(JavaString.format("%d: %b", 42, true)).toBe("42: true");
		expect(JavaString.format("%d: %b", 0, false)).toBe("0: false");
		expect(JavaString.format("Anzahl: %d, Vorhanden: %b", 3, true)).toBe("Anzahl: 3, Vorhanden: true");
		expect(JavaString.format("%b, Wert: %d", true, 99)).toBe("true, Wert: 99");
		expect(JavaString.format("%b%d", false, 7)).toBe("false7");
		expect(JavaString.format("Aktiv: %b, Name: %s, Alter: %d", true, "Alice", 30)).toBe("Aktiv: true, Name: Alice, Alter: 30");
		expect(JavaString.format("Kurs %s, %d Schüler, Schriftlich: %b", "Mathe", 25, false)).toBe("Kurs Mathe, 25 Schüler, Schriftlich: false");
		expect(JavaString.format("%d Punkte, Bestanden: %b, Schüler: %s", 42, true, "Bob")).toBe("42 Punkte, Bestanden: true, Schüler: Bob");
		expect(JavaString.format("%b, %d, %s", false, 7, "Hallo")).toBe("false, 7, Hallo");
		expect(JavaString.format("ID: %d, Bezeichnung: %s, Aktiv: %b", 3, "Deutsch", true)).toBe("ID: 3, Bezeichnung: Deutsch, Aktiv: true");
	});

});
