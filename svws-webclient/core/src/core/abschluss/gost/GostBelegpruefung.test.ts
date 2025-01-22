import { readFileSync, readdirSync } from "fs";
import { resolve, basename } from "path";
import { describe, test, expect } from "vitest";
import type { GostBelegpruefungErgebnisFehler } from "../../../index";
import { Abiturdaten, AbiturdatenManager, GostJahrgangFachkombination, GostFaecherManager, GostJahrgangsdaten, GostBelegpruefungErgebnis, GostBelegpruefungsArt, GostFach, ArrayList } from "../../../index";


const test_dir = resolve(__dirname, "../../../../../../svws-core/src/test/resources/de/svws_nrw/abschluesse/gost/test");
const files = readdirSync(test_dir);

const jahrgaenge: Record<string, GostJahrgangsdaten> = {};
const jahrgaenge_faecher: Record<string, GostFach[] | undefined> = {};
const jahrgaenge_fachkombis: Record<string, GostJahrgangFachkombination[] | undefined> = {};
const abiturdaten: Record<string, Record<string, Abiturdaten> | undefined> = {};
const belegpruefungsergebnisse_gesamt: Record<string, any> = {};
const belegpruefungsergebnisse_ef1: Record<string, any> = {};

files
	.filter((file) => file.includes("_GostJahrgangsdaten"))
	.forEach((file) => {
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		jahrgaenge[jahrgang] = GostJahrgangsdaten.transpilerFromJSON(readFileSync(resolve(test_dir, file), "utf8"));
	});
files
	.filter((file) => file.includes("_GostFaecher"))
	.forEach((file) => {
		const json = JSON.parse(readFileSync(resolve(test_dir, file), "utf8"));
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		jahrgaenge_faecher[jahrgang] ||= [];
		json.forEach((fach: Record<string, any>) => jahrgaenge_faecher[jahrgang]?.push(GostFach.transpilerFromJSON(JSON.stringify(fach))));
	});
files
	.filter((file) => file.includes("_GostJahrgangFachkombination"))
	.forEach((file) => {
		const json = JSON.parse(readFileSync(resolve(test_dir, file), "utf8"));
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		jahrgaenge_fachkombis[jahrgang] ||= [];
		json.forEach((fachkombi: Record<string, any>) => jahrgaenge_fachkombis[jahrgang]?.push(GostJahrgangFachkombination.transpilerFromJSON(JSON.stringify(fachkombi))));
	});
files
	.filter((file) => file.includes("_Abiturdaten"))
	.forEach((file) => {
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		abiturdaten[jahrgang] ||= {};
		abiturdaten[jahrgang][nr] = Abiturdaten.transpilerFromJSON(readFileSync(resolve(test_dir, file), "utf8"));
	});
files
	.filter((file) => file.includes("_Belegpruefungsergebnis_Gesamt"))
	.forEach((file) => {
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		belegpruefungsergebnisse_gesamt[jahrgang] ||= {};
		belegpruefungsergebnisse_gesamt[jahrgang][nr] = GostBelegpruefungErgebnis.transpilerFromJSON(readFileSync(resolve(test_dir, file), "utf8"));
	});
files
	.filter((file) => file.includes("_Belegpruefungsergebnis_EF1"))
	.forEach((file) => {
		const [_, jahrgang, nr] = basename(file, ".json").split("_");
		belegpruefungsergebnisse_ef1[jahrgang] ||= {};
		belegpruefungsergebnisse_ef1[jahrgang][nr] = GostBelegpruefungErgebnis.transpilerFromJSON(readFileSync(resolve(test_dir, file), "utf8"));
	});

describe.each(Object.entries(abiturdaten))(
	"Testfälle Jahrgang %s",
	(jahrgang, schueler) => {
		const jahrgangsdaten = jahrgaenge[jahrgang];
		const gost_faecher = jahrgaenge_faecher[jahrgang] || [];
		const list = new ArrayList<GostFach>();
		for (let index = 0; index < gost_faecher.length; index++)
			list.add(gost_faecher[index]);
		const gost_fachkombis = jahrgaenge_fachkombis[jahrgang] || [];
		const listKombis = new ArrayList<GostJahrgangFachkombination>();
		for (let index = 0; index < gost_fachkombis.length; index++)
			listKombis.add(gost_fachkombis[index]);
		describe.each(Object.entries(schueler!))("Testfall %s", (id, abitur) => {
			test("EF1", () => {
				const faecherManager = new GostFaecherManager(1, list, listKombis);
				const manager = new AbiturdatenManager(abitur, jahrgangsdaten, faecherManager, GostBelegpruefungsArt.EF1);
				const ergebnis = manager.getBelegpruefungErgebnis();
				const expected: GostBelegpruefungErgebnis = belegpruefungsergebnisse_ef1[jahrgang][id];
				expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
				const fehlercodes_expected = expected.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort();
				const fehlercodes_ergebnis = ergebnis.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort();
				expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
			});
			test("Gesamt", () => {
				const faecherManager = new GostFaecherManager(1, list, listKombis);
				const manager = new AbiturdatenManager(abitur, jahrgangsdaten, faecherManager, GostBelegpruefungsArt.GESAMT);
				const ergebnis = manager.getBelegpruefungErgebnis();
				const expected: GostBelegpruefungErgebnis = belegpruefungsergebnisse_gesamt[jahrgang][id];
				expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
				const fehlercodes_expected = expected.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort();
				const fehlercodes_ergebnis = ergebnis.fehlercodes
					.toArray<GostBelegpruefungErgebnisFehler>(new Array<GostBelegpruefungErgebnisFehler>())
					.map((f) => f.code)
					.sort();
				expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
			});
		});
	}
);
