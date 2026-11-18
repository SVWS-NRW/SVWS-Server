import { describe, expect, test } from "vitest";

import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { EnmSperrManager } from "@ui/components/enm/EnmSperrManager";

/**
 * Erzeugt das JSON der Sperrkonfiguration für die Klasse mit der ID 1.
 *
 * @param spalten   die Spalten als Tupel aus ID der Teilleistungsart, Name und Sperrung
 *
 * @returns das JSON
 */
function erzeugeJson(spalten: Array<[number | null, string, boolean]>): string {
	return JSON.stringify([{
		id: 1, tsEingabeAb: null, tsEingabeBis: null, tsEingabeBisKlassenleitung: null,
		istNoteneingabeDurchKlassenleitung: false, istFehlstundenEingabeKlassenweise: false,
		spalten: spalten.map(([idTeilleistung, name, gesperrt]) => ({ idTeilleistung, name, gesperrt })),
	}]);
}

const fachlehrer = new Set([ENMLehrerFunktion.FACHLEHRER]);

describe("EnmSperrManager", () => {

	test("Eine freigegebene Teilleistungsart mit der Bezeichnung einer festen Spalte gibt diese nicht frei", () => {
		const json = erzeugeJson([[null, "Note", true], [100, "Note", false], [null, "Fehlstunden", true], [101, "Fehlstunden", false]]);
		const manager = new EnmSperrManager(json, ENMLehrerFunktion.FACHLEHRER);
		expect(manager.istSpalteneingabeErlaubt(1, "Note", fachlehrer)).toBe(false);
		expect(manager.istFehlstundeneingabeErlaubt(1, false, fachlehrer)).toBe(false);
	});

	test("Eine gesperrte Teilleistungsart mit der Bezeichnung einer festen Spalte sperrt diese nicht", () => {
		const json = erzeugeJson([[null, "Note", false], [100, "Note", true]]);
		const manager = new EnmSperrManager(json, ENMLehrerFunktion.FACHLEHRER);
		expect(manager.istSpalteneingabeErlaubt(1, "Note", fachlehrer)).toBe(true);
	});

	test("Die Teilnoten werden unabhängig von einer gleichnamigen Teilleistungsart geprüft", () => {
		const json = erzeugeJson([[null, "Teilnoten", true], [100, "Teilnoten", false]]);
		const manager = new EnmSperrManager(json, ENMLehrerFunktion.FACHLEHRER);
		expect(manager.istTeilleistungseingabeErlaubt(1, 100, fachlehrer)).toBe(false);
	});

	test("Bei gleicher Bezeichnung entscheidet die ID der Teilleistungsart", () => {
		const json = erzeugeJson([[null, "Teilnoten", false], [100, "KA", false], [101, "KA", true]]);
		const manager = new EnmSperrManager(json, ENMLehrerFunktion.FACHLEHRER);
		expect(manager.istTeilleistungseingabeErlaubt(1, 100, fachlehrer)).toBe(true);
		expect(manager.istTeilleistungseingabeErlaubt(1, 101, fachlehrer)).toBe(false);
	});

});
