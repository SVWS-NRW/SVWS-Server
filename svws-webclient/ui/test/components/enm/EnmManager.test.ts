import { describe, expect, test } from "vitest";

import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { ENMv2Daten } from "@core/core/data/enm/v2/ENMv2Daten";

import { EnmManager } from "../../../src/components/enm/EnmManager";

/** Erzeugt leere ENM-Daten für den Lehrer mit der übergebenen ID, wobei die Schulleitung bei anderen Lehrern liegt. */
function createDaten(idLehrer: number): ENMv2Daten {
	const daten = new ENMv2Daten();
	daten.lehrerID = idLehrer;
	daten.idSchulleitung = 1;
	daten.idSchulleitungStv = 2;
	return daten;
}

describe("EnmManager", () => {
	describe("istSchulleitung", () => {
		test("Schulleitung und stellvertretende Schulleitung aus den ENM-Daten gehören zur Schulleitung", () => {
			expect(new EnmManager(createDaten(1), ENMLehrerFunktion.SCHULLEITUNG).istSchulleitung()).toBe(true);
			expect(new EnmManager(createDaten(2), ENMLehrerFunktion.SCHULLEITUNG).istSchulleitung()).toBe(true);
		});

		test("Ein weiteres Mitglied der Schulleitung wird anerkannt, wenn die Daten mit der Funktion Schulleitung geladen wurden", () => {
			expect(new EnmManager(createDaten(3), ENMLehrerFunktion.SCHULLEITUNG).istSchulleitung()).toBe(true);
		});

		test("Ein Fachlehrer gehört nicht zur Schulleitung", () => {
			expect(new EnmManager(createDaten(3), ENMLehrerFunktion.FACHLEHRER).istSchulleitung()).toBe(false);
		});
	});
});
