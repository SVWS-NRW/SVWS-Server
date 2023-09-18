import { beforeEach, describe, expect, test } from "vitest";
import type { JavaMap } from "@core";
import { ArrayMap, ArrayMapCollection, ArrayMapEntrySet, ArrayMapKeySet, GostKursart, IllegalArgumentException } from "@core";

let m: ArrayMap<unknown, unknown>;

describe("ArrayMap", () => {
	beforeEach(()=>{
		m = new ArrayMap(GostKursart.values());
	})
	test("constructor: ArrayMap ist ein ArrayMap", () => {
		expect(m).toBeInstanceOf(ArrayMap);
	});
	test("constructor: funktioniert nicht mit gewöhnlichen Arrays", ()=>{
		expect(()=>new ArrayMap([1,2,3])).toThrow(IllegalArgumentException);
	})
	test("keySet: gibt die Keys zurück als Set", ()=>{
		expect(m.keySet()).toBeInstanceOf(ArrayMapKeySet);
	})
	test("value: gibt die Values als Collection zurück", ()=>{
		expect(m.values()).toBeInstanceOf(ArrayMapCollection);
	})
	test("entrySet: gibt das EntrySet als Set zurück", ()=>{
		expect(m.entrySet()).toBeInstanceOf(ArrayMapEntrySet);
	})
	test("getNumberOfKeys: gibt die Anzahl der möglichen Schlüsselwerte zurück", ()=>{
		expect(m.getNumberOfKeys()).toEqual(5);
	})
	test("size: gibt die Anzahl der hinterlegten Entries zurück", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.size()).toEqual(1);
	})
	test("size: gibt die Anzahl der hinterlegten Entries zurück", ()=>{
		expect(m.size()).toEqual(0);
	})
	test("isEmpty: zeigt an, ob das ArrayMap leer ist, nein", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.isEmpty()).toBeFalsy();
	})
	test("isEmpty: zeigt an, ob das ArrayMap leer ist, ja", ()=>{
		m.clear();
		expect(m.isEmpty()).toBeTruthy();
	})
	test("getKey: holt den Schlüssel vom Index", ()=>{
		expect(m.getKeyAt(0)).toEqual(GostKursart.LK);
	})
	test("getKey: holt den Schlüssel vom Index nicht, wenn der Index nicht passt", ()=>{
		expect(m.getKeyAt(9)).toEqual(null);
	})
	test("getEntryByIndex: ermittelt den Entry vom index", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.getEntryByIndex(0)?.getValue()).toEqual(1);
	})
	test("getEntryByIndex: ermittelt den Entry vom index", ()=>{
		expect(m.getEntryByIndex(0)).toEqual(null);
	})
	test("getEntry: ermittelt den Entry nach Key", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.getEntry(GostKursart.LK)?.getValue()).toEqual(1);
	})
	test("containsKey: Prüft, ob der Schlüssel gültig ist", ()=> {
		m.put(GostKursart.LK, 1);
		expect(m.containsKey(GostKursart.LK)).toBeTruthy();
	})
	test("containsKey: Prüft, ob der Schlüssel gültig ist", ()=> {
		expect(m.containsKey(GostKursart.GK)).toBeFalsy();
	})
	test("containsValue: Prüft, ob Entry vorhanden ist", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.containsValue(1)).toBeTruthy();
	})
	test("get: hole Entry", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.get(GostKursart.LK)).toEqual(1);
	})
	test("getValueAt: holt ein Value mit Index", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.getValueAt(0)).toEqual(1);
	})
	test("put: Ergänze ein Value zu Key", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.getValueAt(0)).toEqual(1);
	})
	test("remove: entfernt ein Value mit Key", ()=>{
		m.put(GostKursart.LK, 1);
		expect(m.getValueAt(0)).toEqual(1);
		expect(m.isEmpty()).toBeFalsy();
		expect(m.remove(GostKursart.LK)).toEqual(1);
		expect(m.isEmpty()).toBeTruthy();
	})
	test("putAll: Einfügen mehrer Values", ()=>{
		m.put(GostKursart.LK, 1);
		const mm: JavaMap<GostKursart, unknown> = new ArrayMap(GostKursart.values());
		mm.put(GostKursart.GK, 2);
		mm.put(GostKursart.ZK, 3);
		m.putAll(mm);
		expect(m.size()).toEqual(3);
	})
	test("clear: Leere alle Values", ()=>{
		m.put(GostKursart.LK, 1);
		m.clear();
		expect(m.size()).toEqual(0);
	})
});
