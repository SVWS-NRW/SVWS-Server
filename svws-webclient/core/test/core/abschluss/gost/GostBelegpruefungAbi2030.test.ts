import { fail } from "node:assert";
import { readdirSync, readFileSync } from "node:fs";
import { basename, resolve } from "node:path";
import { beforeAll, describe, expect, test } from "vitest";

import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { GostBelegpruefungErgebnisFehler } from "@core/core/abschluss/gost/GostBelegpruefungErgebnisFehler";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { GostLaufbahnplanungExportV1 } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1";
import { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import { GostLaufbahnplanungDataHandler } from "@core/core/utils/gost/GostLaufbahnplanungDataHandler";

describe("Laufbahnplanung Tests", () => {

	const test_dir = resolve(import.meta.dirname, "../../../../../../svws-core/src/test/resources/de/svws_nrw/core/abschluss/gost/belegpruefung/abi2030");

	const files = readdirSync(test_dir);
	const belegungFiles = files.filter((file) => file.includes("_Belegung"));

	async function loadLpDaten(file: string): Promise<GostLaufbahnplanungDataHandler> {
		try {
			const fullname = resolve(test_dir, file);
			const buffer = readFileSync(fullname);
			const gzData: File = new File([buffer], fullname, { type: 'application/gzip' });
			const ds = new DecompressionStream("gzip");
			const rawData = await (new Response(gzData.stream().pipeThrough(ds))).text();
			const revision: number = JSON.parse(rawData).lpRevision;
			switch (revision) {
				case 1:
					return GostLaufbahnplanungDataHandler.importV1(GostLaufbahnplanungExportV1.transpilerFromJSON(rawData));
				case 2:
					return GostLaufbahnplanungDataHandler.importV2(GostLaufbahnplanungExportV2.transpilerFromJSON(rawData));
				default:
					fail(`Die Revision der Laufbahnplanungsdatei (${revision}) entspricht nicht den unterstützen Revisionen 1 und 2`);
			}
		} catch (e) {
			fail("Datei: " + file);
		}
	}

	const abiturdaten: Record<string, GostLaufbahnplanungDataHandler> = {};
	const belegpruefungsergebnisse_gesamt: Record<string, any> = {};
	const belegpruefungsergebnisse_ef1: Record<string, any> = {};

	for (const file of belegungFiles) {
		const filename = basename(file, ".lp");
		const match = /\d{2}_Jg_[^_]+_\d+/.exec(filename);
		const name = match ? match[0] : `Ungültige_Datei_${filename}`;

		describe(`Testfall ${name}`, () => {
			test("Dateiname des Testfalls validieren", () => {
				if (match === null) {
					fail(`Der Dateiname ${filename} ist für einen Testfall fehlerhaft benannt.`);
				}
			});
			test("EF1", async (ctx) => {
				if (match === null) {
					ctx.skip();
				}

				const ergebnisFile = files.find(f => f.includes(`${name}_Belegpruefungsergebnis_EF1`));
				if (ergebnisFile === undefined) {
					fail(`Kein EF1 Ergebnis für ${name} gefunden.`);
				}
				const expected = GostBelegpruefungErgebnis.transpilerFromJSON(readFileSync(resolve(test_dir, ergebnisFile), "utf8"));

				const lpDaten = await loadLpDaten(file);
				const manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(), lpDaten.getFaecherManager(), GostBelegpruefungsArt.EF1);
				const ergebnis = manager.getBelegpruefungErgebnis();
				expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
				const fehlercodes_expected = expected.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort((a, b) => a.localeCompare(b));
				const fehlercodes_ergebnis = ergebnis.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort((a, b) => a.localeCompare(b));
				expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
			});

			test("Gesamt", async (ctx) => {
				if (match === null) {
					ctx.skip();
				}

				const ergebnisFile = files.find(f => f.includes(`${name}_Belegpruefungsergebnis_Gesamt`));
				if (ergebnisFile === undefined) {
					fail(`Kein EF1 Ergebnis für ${name} gefunden.`);
				}
				const expected = GostBelegpruefungErgebnis.transpilerFromJSON(readFileSync(resolve(test_dir, ergebnisFile), "utf8"));

				const lpDaten = await loadLpDaten(file);
				const manager = new AbiturdatenManager(lpDaten.getAbiturdaten(), lpDaten.getGostJahrgangsdaten(), lpDaten.getFaecherManager(), GostBelegpruefungsArt.GESAMT);
				const ergebnis = manager.getBelegpruefungErgebnis();
				expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
				const fehlercodes_expected = expected.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort((a, b) => a.localeCompare(b));
				const fehlercodes_ergebnis = ergebnis.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort((a, b) => a.localeCompare(b));
				expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
			});
		});
	}
});