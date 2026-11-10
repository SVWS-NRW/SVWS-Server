import { readdirSync, readFileSync } from "node:fs";
import { resolve } from "node:path";
import { gunzipSync } from "node:zlib";
import { describe, expect, test, vi } from "vitest";

import { Note } from "@core/asd/types/Note";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostAbiturMarkierungsalgorithmusErgebnis } from "@core/core/abschluss/gost/GostAbiturMarkierungsalgorithmusErgebnis";
import type { GostAbiturMarkierungsalgorithmusMarkierung } from "@core/core/abschluss/gost/GostAbiturMarkierungsalgorithmusMarkierung";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
import { GostFach } from "@core/core/data/gost/GostFach";
import { GostJahrgangFachkombination } from "@core/core/data/gost/GostJahrgangFachkombination";
import { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostZulassungSimulation } from "@core/core/data/gost/GostZulassungSimulation";
import { GostZulassungSimulationAbiturfach } from "@core/core/data/gost/GostZulassungSimulationAbiturfach";
import { GostZulassungSimulationNote } from "@core/core/data/gost/GostZulassungSimulationNote";
import { GostLaufbahnplanungExportV1 } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1";
import { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { GostLaufbahnplanungDataHandler } from "@core/core/utils/gost/GostLaufbahnplanungDataHandler";
import { GostZulassungSimulationUtils } from "@core/core/utils/gost/GostZulassungSimulationUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

const pfadTestdaten = resolve(import.meta.dirname, "../../../../../../svws-core/src/test/resources/de/svws_nrw");
const pfadVor2030 = resolve(pfadTestdaten, "abschluesse/gost/test");
const pfad2030 = resolve(pfadTestdaten, "core/abschluss/gost/belegpruefung/abi2030");

type Testfall = { daten: Abiturdaten; jahrgangsdaten: GostJahrgangsdaten; faecher: GostFaecherManager };

function ladeVor2030(): Testfall {
	const lese = (name: string) => readFileSync(resolve(pfadVor2030, name), "utf8");
	const daten = Abiturdaten.transpilerFromJSON(lese("Jahrgang_01_1001_Abiturdaten.json"));
	const faecher = new ArrayList<GostFach>();
	for (const fach of JSON.parse(lese("Jahrgang_01_GostFaecher.json"))) {
		faecher.add(GostFach.transpilerFromJSON(JSON.stringify(fach)));
	}
	const kombinationen: List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();
	for (const kombination of JSON.parse(lese("Jahrgang_01_GostJahrgangFachkombination.json"))) {
		kombinationen.add(GostJahrgangFachkombination.transpilerFromJSON(JSON.stringify(kombination)));
	}
	const manager = new GostFaecherManager(daten.schuljahrAbitur, faecher);
	manager.addFachkombinationenAll(kombinationen);
	return { daten, jahrgangsdaten: GostJahrgangsdaten.transpilerFromJSON(lese("Jahrgang_01_GostJahrgangsdaten.json")), faecher: manager };
}

function lade2030(): Testfall {
	const datei = readdirSync(pfad2030).find(name => name.includes("_Belegung"));
	const json = gunzipSync(readFileSync(resolve(pfad2030, datei ?? ""))).toString("utf8");
	const handler = (JSON.parse(json).lpRevision === 1)
		? GostLaufbahnplanungDataHandler.importV1(GostLaufbahnplanungExportV1.transpilerFromJSON(json))
		: GostLaufbahnplanungDataHandler.importV2(GostLaufbahnplanungExportV2.transpilerFromJSON(json));
	return { daten: handler.getAbiturdaten(), jahrgangsdaten: handler.getGostJahrgangsdaten(), faecher: handler.getFaecherManager() };
}

function belegungen(daten: Abiturdaten): AbiturFachbelegung[] {
	return daten.fachbelegungen.toArray(new Array<AbiturFachbelegung>());
}

function ersteBelegung(daten: Abiturdaten, halbjahr: GostHalbjahr = GostHalbjahr.Q21): { fach: AbiturFachbelegung; belegung: AbiturFachbelegungHalbjahr } {
	for (const fach of belegungen(daten)) {
		const belegung = fach.belegungen[halbjahr.id];
		if (belegung !== null) {
			return { fach, belegung };
		}
	}
	throw new Error("Der Testfall hat keine Belegung im Halbjahr " + halbjahr.kuerzel);
}

function erstesAbiturfach(daten: Abiturdaten): AbiturFachbelegung {
	const fach = belegungen(daten).find(f => f.abiturFach !== null);
	if (fach === undefined) {
		throw new Error("Der Testfall hat kein Abiturfach");
	}
	return fach;
}

function erzeugeNote(idFach: number, halbjahr: GostHalbjahr, notenpunkte: number | null): GostZulassungSimulationNote {
	const note = new GostZulassungSimulationNote();
	note.idFach = idFach;
	note.halbjahr = halbjahr.id;
	note.notenpunkte = notenpunkte;
	return note;
}

function simulationMitNote(idFach: number, halbjahr: GostHalbjahr, notenpunkte: number | null): GostZulassungSimulation {
	const simulation = new GostZulassungSimulation();
	simulation.noten.add(erzeugeNote(idFach, halbjahr, notenpunkte));
	return simulation;
}

function simulationAlleNoten(daten: Abiturdaten, notenpunkte: number): GostZulassungSimulation {
	const simulation = new GostZulassungSimulation();
	for (const fach of belegungen(daten)) {
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if (fach.belegungen[halbjahr.id] !== null) {
				simulation.noten.add(erzeugeNote(fach.fachID, halbjahr, notenpunkte));
			}
		}
	}
	return simulation;
}

function setzeLeer(daten: Abiturdaten): void {
	for (const fach of belegungen(daten)) {
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			const belegung = fach.belegungen[halbjahr.id];
			if (belegung !== null) {
				belegung.notenkuerzel = null;
			}
		}
	}
}

function klonen(daten: Abiturdaten): Abiturdaten {
	return Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(daten));
}

function faecherMitVertiefungskurs(idFach: number): GostFaecherManager {
	const fach = new GostFach();
	fach.id = idFach;
	fach.kuerzel = "VX";
	const faecher = new ArrayList<GostFach>();
	faecher.add(fach);
	return new GostFaecherManager(2029, faecher);
}

function combine(original: Abiturdaten, simulation: GostZulassungSimulation, fixiereNoten = true, faecher = new GostFaecherManager(original.schuljahrAbitur)): Abiturdaten {
	const daten = klonen(original);
	GostZulassungSimulationUtils.combine(daten, simulation, faecher, fixiereNoten);
	return daten;
}

function berechne(original: Abiturdaten, simulation: GostZulassungSimulation, jahrgangsdaten: GostJahrgangsdaten, faecher: GostFaecherManager,
	refreshPflichtpruefungen = false, fixiereNoten = true) {
	const eingabe = combine(original, simulation, fixiereNoten);
	return { eingabe, manager: GostZulassungSimulationUtils.berechne(klonen(eingabe), jahrgangsdaten, faecher, refreshPflichtpruefungen) };
}

describe("GostZulassungSimulationUtils", () => {

	describe("combine", () => {

		test("Eine leere Simulation ändert keine Noten und keine Prüfungsdaten, alle Q-Halbjahre gelten aber als bewertet", () => {
			const { daten } = lade2030();
			const vorher = Abiturdaten.transpilerToJSON(daten);

			const ergebnis = combine(daten, new GostZulassungSimulation());

			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				expect(ergebnis.bewertetesHalbjahr[halbjahr.id]).toBe(true);
			}
			const erwartet = Abiturdaten.transpilerFromJSON(vorher);
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				erwartet.bewertetesHalbjahr[halbjahr.id] = true;
			}
			expect(JSON.parse(Abiturdaten.transpilerToJSON(ergebnis))).toEqual(JSON.parse(Abiturdaten.transpilerToJSON(erwartet)));
		});

		test.each([[0, "6"], [15, "1+"], [7, "3-"]])("Die Notenpunkte %i werden auf einer Belegung ohne Note als Notenkürzel %s gesetzt", (notenpunkte, kuerzel) => {
			const { daten } = lade2030();
			setzeLeer(daten);
			const { fach } = ersteBelegung(daten);

			const ergebnis = combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, notenpunkte));

			expect(ersteBelegung(ergebnis).belegung.notenkuerzel).toBe(kuerzel);
		});

		test("Eine gültige echte Note bleibt bei aktivem Schalter erhalten", () => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten);
			const echteNote = belegung.notenkuerzel;

			const ergebnis = combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 15));

			expect(echteNote).not.toBeNull();
			expect(ersteBelegung(ergebnis).belegung.notenkuerzel).toBe(echteNote);
		});

		test("Bei ausgeschaltetem Schalter gilt der Simulationswert, beim Wiedereinschalten wieder die echte Note", () => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten);
			const echteNote = belegung.notenkuerzel;
			const simulation = simulationMitNote(fach.fachID, GostHalbjahr.Q21, 15);

			expect(ersteBelegung(combine(daten, simulation, false)).belegung.notenkuerzel).toBe("1+");

			expect(ersteBelegung(combine(daten, simulation)).belegung.notenkuerzel).toBe(echteNote);
			expect(simulation.noten.size()).toBe(1);
		});

		test.each([[""], [null]])("Eine leere echte Note (%j) in einem als bewertet geltenden Halbjahr bleibt editierbar", (leer) => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten);
			belegung.notenkuerzel = leer;
			daten.bewertetesHalbjahr[GostHalbjahr.Q21.id] = true;

			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11))).belegung.notenkuerzel).toBe("2");
		});

		test("Ein Eintrag mit einem unbekannten Halbjahr bleibt unberücksichtigt", () => {
			const { daten } = lade2030();
			const simulation = simulationMitNote(ersteBelegung(daten).fach.fachID, GostHalbjahr.Q21, 11);
			simulation.noten.get(0).halbjahr = 99;

			expect(() => combine(daten, simulation)).not.toThrow();
		});

		test("Pseudonoten wie AT bleiben bei aktivem Schalter gesperrt", () => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten);
			belegung.notenkuerzel = "AT";

			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11))).belegung.notenkuerzel).toBe("AT");
		});

		test.each(["E1", "E2", "E3", "NT", "NB", "NE", "LM", "AM"])("Die Pseudonote %s bleibt auch ohne Fixierung erhalten", (kuerzel) => {
			const { daten } = lade2030();
			const { fach, belegung } = ersteBelegung(daten);
			belegung.notenkuerzel = kuerzel;

			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), false)).belegung.notenkuerzel).toBe(kuerzel);
			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, null), false)).belegung.notenkuerzel).toBe(kuerzel);
		});

		test("Ein Attest bleibt vor Abitur 2030 auch ohne Fixierung erhalten", () => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten);
			belegung.notenkuerzel = "AT";

			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), false)).belegung.notenkuerzel).toBe("AT");
		});

		test.each([[true], [false]])("Auf einem Vertiefungskurs können keine Notenpunkte eingegeben werden (Fixierung: %s)", (fixiereNoten) => {
			const { daten } = lade2030();
			setzeLeer(daten);
			const { fach } = ersteBelegung(daten);

			expect(ersteBelegung(combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, 11), fixiereNoten, faecherMitVertiefungskurs(fach.fachID))).belegung.notenkuerzel).toBeNull();
		});

		test("Einträge für Fächer und Halbjahre ohne Belegung werden ohne Fehler ignoriert", () => {
			const { daten } = lade2030();
			const simulation = simulationMitNote(-4711, GostHalbjahr.Q21, 11);
			const ohneBelegung = belegungen(daten).find(f => f.belegungen[GostHalbjahr.Q22.id] === null);
			if (ohneBelegung !== undefined) {
				simulation.noten.add(erzeugeNote(ohneBelegung.fachID, GostHalbjahr.Q22, 11));
			}

			expect(() => combine(daten, simulation)).not.toThrow();
			if (ohneBelegung !== undefined) {
				expect(belegungen(combine(daten, simulation)).find(f => f.fachID === ohneBelegung.fachID)?.belegungen[GostHalbjahr.Q22.id]).toBeNull();
			}
		});

		test("Einträge für Halbjahre außerhalb der Qualifikationsphase werden ignoriert", () => {
			const { daten } = ladeVor2030();
			const { fach, belegung } = ersteBelegung(daten, GostHalbjahr.EF1);
			const echteNote = belegung.notenkuerzel;

			const ergebnis = combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.EF1, 15), false);

			expect(ersteBelegung(ergebnis, GostHalbjahr.EF1).belegung.notenkuerzel).toBe(echteNote);
		});

		test("Prüfungswerte werden nur bei Belegungen mit Abiturfach übernommen", () => {
			const { daten } = lade2030();
			const abiturfach = erstesAbiturfach(daten);
			const anderesFach = belegungen(daten).find(f => f.abiturFach === null);
			const simulation = new GostZulassungSimulation();
			for (const fach of [abiturfach, anderesFach]) {
				if (fach === undefined) {
					continue;
				}
				const eintrag = new GostZulassungSimulationAbiturfach();
				eintrag.idFach = fach.fachID;
				eintrag.pruefungNotenpunkte = 11;
				eintrag.muendlichBestehen = true;
				eintrag.muendlichFreiwillig = false;
				eintrag.muendlichReihenfolge = 2;
				eintrag.muendlichNotenpunkte = 3;
				simulation.abiturfaecher.add(eintrag);
			}

			const ergebnis = belegungen(combine(daten, simulation));

			const abi = ergebnis.find(f => f.fachID === abiturfach.fachID);
			expect(abi?.block2NotenKuerzelPruefung).toBe("2");
			expect(abi?.block2MuendlichePruefungBestehen).toBe(true);
			expect(abi?.block2MuendlichePruefungFreiwillig).toBe(false);
			expect(abi?.block2MuendlichePruefungReihenfolge).toBe(2);
			expect(abi?.block2MuendlichePruefungNotenKuerzel).toBe("5+");
			if (anderesFach !== undefined) {
				expect(ergebnis.find(f => f.fachID === anderesFach.fachID)?.block2NotenKuerzelPruefung).toBeNull();
			}
		});

		test("Die Art der besonderen Lernleistung und deren Note werden übernommen", () => {
			const { daten } = lade2030();
			const simulation = new GostZulassungSimulation();
			simulation.besondereLernleistung = "E";
			simulation.besondereLernleistungNotenpunkte = 11;

			const ergebnis = combine(daten, simulation);

			expect(ergebnis.besondereLernleistung).toBe("E");
			expect(ergebnis.besondereLernleistungNotenKuerzel).toBe("2");
		});

		test("Eine leere Simulation bedeutet keine besondere Lernleistung", () => {
			const { daten } = lade2030();
			daten.besondereLernleistung = null;

			expect(combine(daten, new GostZulassungSimulation()).besondereLernleistung).toBe("K");
		});

		test("Eine ungültige Art der besonderen Lernleistung gilt als keine besondere Lernleistung", () => {
			const { daten } = lade2030();
			const simulation = new GostZulassungSimulation();
			simulation.besondereLernleistung = "X";

			expect(combine(daten, simulation).besondereLernleistung).toBe("K");
		});

		test.each([[16], [-1]])("Der Wert %i außerhalb des Katalogs wird nicht übernommen", (notenpunkte) => {
			const { daten } = lade2030();
			setzeLeer(daten);
			const { fach } = ersteBelegung(daten);

			const ergebnis = combine(daten, simulationMitNote(fach.fachID, GostHalbjahr.Q21, notenpunkte));

			expect(ersteBelegung(ergebnis).belegung.notenkuerzel).toBeNull();
			expect(Note.fromKuerzel(ersteBelegung(ergebnis).belegung.notenkuerzel)).toBe(Note.KEINE);
		});

	});

	describe.each([["vor 2030", ladeVor2030], ["ab 2030", lade2030]])("berechne, Abitur %s", (_name, lade) => {

		test("Markierungen aus der Planung zählen nicht mit", () => {
			const { daten, jahrgangsdaten, faecher } = lade();
			setzeLeer(daten);
			ersteBelegung(daten).belegung.block1gewertet = true;

			const { manager } = berechne(daten, new GostZulassungSimulation(), jahrgangsdaten, faecher);

			for (const fach of belegungen(manager.daten())) {
				for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
					expect(fach.belegungen[halbjahr.id]?.block1gewertet ?? false).toBe(false);
				}
			}
		});

		test("Fehlen Noten, so gibt es keine Exception, keine Zulassung und einen Log-Eintrag", () => {
			const { daten, jahrgangsdaten, faecher } = lade();
			setzeLeer(daten);

			const { manager } = berechne(daten, new GostZulassungSimulation(), jahrgangsdaten, faecher);

			expect(manager.daten().block1Zulassung).toBeNull();
			expect(manager.getErgebnisMarkierungsalgorithmus().erfolgreich).toBe(false);
			expect(manager.getErgebnisMarkierungsalgorithmus().log.isEmpty()).toBe(false);
		});

		test("Mit vollständigen Noten läuft die Berechnung durch und die Zulassung ist bestimmt", () => {
			const { daten, jahrgangsdaten, faecher } = lade();

			const { manager } = berechne(daten, simulationAlleNoten(daten, 11), jahrgangsdaten, faecher, false, false);

			expect(manager.getErgebnisMarkierungsalgorithmus().erfolgreich).toBe(true);
			expect(manager.daten().block1Zulassung).not.toBeNull();
		});

		test("Fehlende Prüfungsnoten lassen das Prüfungsergebnis offen", () => {
			const { daten, jahrgangsdaten, faecher } = lade();

			const { manager } = berechne(daten, simulationAlleNoten(daten, 11), jahrgangsdaten, faecher, false, false);

			expect(manager.daten().pruefungBestanden).toBeNull();
		});

		test("Mündliche Eingaben bleiben beim Berechnen erhalten", () => {
			const { daten, jahrgangsdaten, faecher } = lade();
			const abiturfach = erstesAbiturfach(daten);
			const simulation = simulationAlleNoten(daten, 11);
			const eintrag = new GostZulassungSimulationAbiturfach();
			eintrag.idFach = abiturfach.fachID;
			eintrag.pruefungNotenpunkte = 11;
			eintrag.muendlichFreiwillig = true;
			eintrag.muendlichBestehen = false;
			eintrag.muendlichReihenfolge = 1;
			eintrag.muendlichNotenpunkte = 8;
			simulation.abiturfaecher.add(eintrag);

			const { manager } = berechne(daten, simulation, jahrgangsdaten, faecher, false, false);

			const fach = belegungen(manager.daten()).find(f => f.fachID === abiturfach.fachID);
			expect(fach?.block2MuendlichePruefungFreiwillig).toBe(true);
			expect(fach?.block2MuendlichePruefungReihenfolge).toBe(1);
			expect(fach?.block2MuendlichePruefungNotenKuerzel).toBe("3");
		});

		test("Bilinguale Belegungen werden für die Berechnung zusammengefasst, die Eingabedaten behalten alle Fächer", () => {
			const spy = vi.spyOn(AbiturdatenManager.prototype, "kombiniereFachbelegungenEinesFaches");
			const { daten, jahrgangsdaten, faecher } = lade();

			const { eingabe } = berechne(daten, simulationAlleNoten(daten, 11), jahrgangsdaten, faecher, false, false);

			expect(spy).toHaveBeenCalledOnce();
			expect(eingabe.fachbelegungen.size()).toBe(daten.fachbelegungen.size());
			spy.mockRestore();
		});

	});

	describe("Hilfsfunktionen für die Eingabe", () => {

		test("Eine gelöschte Note bleibt als Eintrag ohne Notenpunkte erhalten und leert damit auch eine echte Note", () => {
			const { daten } = ladeVor2030();
			const { fach } = ersteBelegung(daten);
			const simulation = new GostZulassungSimulation();

			simulation.noten.add(erzeugeNote(fach.fachID, GostHalbjahr.Q21, null));

			expect(ersteBelegung(combine(daten, simulation, false)).belegung.notenkuerzel).toBeNull();
		});

	});

	describe.each([["vor 2030", ladeVor2030], ["ab 2030", lade2030]])("Halbjahre ohne Belegung, Abitur %s", (_name, lade) => {

		/** Der Manager ergänzt beim Lesen der Fachwahlen leere Belegungen, so wie es die Laufbahnplanung vor dem Öffnen des Modals auslöst. */
		function nachLesenDerFachwahlen({ daten, jahrgangsdaten, faecher }: Testfall): Abiturdaten {
			const kopie = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(daten));
			new AbiturdatenManager(kopie, jahrgangsdaten, faecher, GostBelegpruefungsArt.GESAMT).getSchuelerFachwahlen();
			return kopie;
		}

		function halbjahrOhneBelegung(daten: Abiturdaten): { fach: AbiturFachbelegung; halbjahr: GostHalbjahr } {
			for (const fach of belegungen(daten)) {
				for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
					if (fach.belegungen[halbjahr.id] === null) {
						return { fach, halbjahr };
					}
				}
			}
			throw new Error("Der Testfall hat kein Fach mit einem Halbjahr ohne Belegung");
		}

		test("Das Lesen der Fachwahlen legt leere Belegungen an, die Simulation übernimmt sie nicht", () => {
			const testfall = lade();
			const { fach, halbjahr } = halbjahrOhneBelegung(testfall.daten);
			const gelesen = nachLesenDerFachwahlen(testfall);
			expect(belegungen(gelesen).find(f => f.fachID === fach.fachID)?.belegungen[halbjahr.id]?.kursartKuerzel).toBe("");

			const ergebnis = combine(gelesen, new GostZulassungSimulation());

			expect(belegungen(ergebnis).find(f => f.fachID === fach.fachID)?.belegungen[halbjahr.id]).toBeNull();
		});

		test("Eine eingegebene Note ohne Belegung bleibt unberücksichtigt", () => {
			const testfall = lade();
			const { fach, halbjahr } = halbjahrOhneBelegung(testfall.daten);

			const ergebnis = combine(nachLesenDerFachwahlen(testfall), simulationMitNote(fach.fachID, halbjahr, 10), false);

			expect(belegungen(ergebnis).find(f => f.fachID === fach.fachID)?.belegungen[halbjahr.id]).toBeNull();
		});

		test("Die Berechnung entspricht der Berechnung ohne vorheriges Lesen der Fachwahlen", () => {
			const testfall = lade();
			const simulation = simulationAlleNoten(testfall.daten, 11);

			const erwartet = berechne(testfall.daten, simulation, testfall.jahrgangsdaten, testfall.faecher, false, false).manager;
			const ergebnis = berechne(nachLesenDerFachwahlen(testfall), simulation, testfall.jahrgangsdaten, testfall.faecher, false, false).manager;

			expect(Abiturdaten.transpilerToJSON(ergebnis.daten())).toBe(Abiturdaten.transpilerToJSON(erwartet.daten()));
			expect(ergebnis.getErgebnisMarkierungsalgorithmus().erfolgreich).toBe(erwartet.getErgebnisMarkierungsalgorithmus().erfolgreich);
		});

	});

	describe("berechne, Abitur vor 2030 mit echten Noten", () => {

		test("Die Markierungen entsprechen dem erwarteten Ergebnis des Testfalls", () => {
			const { daten, jahrgangsdaten, faecher } = ladeVor2030();
			const erwartet = GostAbiturMarkierungsalgorithmusErgebnis.transpilerFromJSON(readFileSync(resolve(pfadVor2030, "Jahrgang_01_1001_Markierungsalgorithmus.json"), "utf8"));

			const { manager } = berechne(daten, new GostZulassungSimulation(), jahrgangsdaten, faecher);

			expect(erwartet.erfolgreich).toBe(true);
			expect(manager.getErgebnisMarkierungsalgorithmus().erfolgreich).toBe(true);
			for (const markierung of erwartet.markierungen) {
				const fach = belegungen(manager.daten()).find(f => f.fachID === markierung.idFach);
				const halbjahr = GostHalbjahr.fromIDorException(markierung.idHalbjahr);
				expect(fach?.belegungen[halbjahr.id]?.block1gewertet).toBe(markierung.markiert);
			}
			expect(manager.daten().block1Zulassung).toBe(true);
			expect(manager.daten().block1AnzahlKurse).toBe(erwartet.markierungen.toArray(new Array<GostAbiturMarkierungsalgorithmusMarkierung>()).filter(m => m.markiert).length);
		});

	});

});
