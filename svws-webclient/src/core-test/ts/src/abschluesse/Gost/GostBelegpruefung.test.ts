import { readFileSync, readdirSync } from "fs";
import { resolve, basename } from "path";
import { describe, test, expect } from "vitest";
import {
	Abiturdaten,
	AbiturdatenManager,
	GostBelegpruefungErgebnis,
	GostBelegpruefungErgebnisFehler,
	GostBelegpruefungsArt,
	GostFach,
	Vector
} from "~/index";


const test_dir = resolve(
    __dirname,
    "../../../../../../../svws-core/src/test/resources/de/svws_nrw/abschluesse/gost/test"
);
const files = readdirSync(test_dir);

const jahrgaenge_faecher: { [jahrgang: string]: GostFach[] } = {};
const abiturdaten: { [jahrgang: string]: { [id: string]: Abiturdaten } } = {};
const belegpruefungsergebnisse_gesamt: { [jahrgang: string]: any } = {};
const belegpruefungsergebnisse_ef1: { [jahrgang: string]: any } = {};

files
    .filter((file) => file.includes("_GostFaecher"))
    .forEach((file) => {
        const json = JSON.parse(readFileSync(resolve(test_dir, file), "utf8"));
        const split = basename(file, ".json").split("_");
        if (!jahrgaenge_faecher[split[1]]) jahrgaenge_faecher[split[1]] = [];
        json.forEach((fach: { [string: string]: any }) => {
            jahrgaenge_faecher[split[1]].push(
                GostFach.transpilerFromJSON(JSON.stringify(fach))
            );
        });
    });
files
    .filter((file) => file.includes("_Abiturdaten"))
    .forEach((file) => {
        const split = basename(file, ".json").split("_");
        if (!abiturdaten[split[1]]) abiturdaten[split[1]] = {};
        abiturdaten[split[1]][split[2]] = Abiturdaten.transpilerFromJSON(
            readFileSync(resolve(test_dir, file), "utf8")
        );
    });
files
    .filter((file) => file.includes("_Belegpruefungsergebnis_Gesamt"))
    .forEach((file) => {
        const split = basename(file, ".json").split("_");
        if (!belegpruefungsergebnisse_gesamt[split[1]])
            belegpruefungsergebnisse_gesamt[split[1]] = {};
        if (!belegpruefungsergebnisse_gesamt[split[1]][split[2]])
            belegpruefungsergebnisse_gesamt[split[1]][split[2]] = {};
        belegpruefungsergebnisse_gesamt[split[1]][split[2]] =
            GostBelegpruefungErgebnis.transpilerFromJSON(
                readFileSync(resolve(test_dir, file), "utf8")
            );
    });
files
    .filter((file) => file.includes("_Belegpruefungsergebnis_EF1"))
    .forEach((file) => {
        const split = basename(file, ".json").split("_");
        if (!belegpruefungsergebnisse_ef1[split[1]])
            belegpruefungsergebnisse_ef1[split[1]] = {};
        if (!belegpruefungsergebnisse_ef1[split[1]][split[2]])
            belegpruefungsergebnisse_ef1[split[1]][split[2]] = {};
        belegpruefungsergebnisse_ef1[split[1]][split[2]] =
            GostBelegpruefungErgebnis.transpilerFromJSON(
                readFileSync(resolve(test_dir, file), "utf8")
            );
    });

describe.each(Object.entries(abiturdaten))(
    "Testfälle Jahrgang %s",
    (jahrgang, schueler) => {
        const gost_faecher = jahrgaenge_faecher[jahrgang];
        const list = new Vector<GostFach>();
        for (let index = 0; index < gost_faecher.length; index++) {
            list.add(gost_faecher[index]);
        }
        describe.each(Object.entries(schueler))("Testfall %s", (id, abitur) => {
            test("EF1", () => {
                const manager = new AbiturdatenManager(
                    abitur,
                    list,
                    GostBelegpruefungsArt.EF1
                );
                const ergebnis = manager.getBelegpruefungErgebnis();
                const expected: GostBelegpruefungErgebnis =
                    belegpruefungsergebnisse_ef1[jahrgang][id];
                expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
                const fehlercodes_expected = expected.fehlercodes
                    .toArray<GostBelegpruefungErgebnisFehler>(
                        new Array<GostBelegpruefungErgebnisFehler>()
                    )
                    .map((f) => f.code)
                    .sort();
                const fehlercodes_ergebnis = ergebnis.fehlercodes
                    .toArray<GostBelegpruefungErgebnisFehler>(
                        new Array<GostBelegpruefungErgebnisFehler>()
                    )
                    .map((f) => f.code)
                    .sort();
                expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
            });
            test("Gesamt", () => {
                const manager = new AbiturdatenManager(
                    abitur,
                    list,
                    GostBelegpruefungsArt.GESAMT
                );
                const ergebnis = manager.getBelegpruefungErgebnis();
                const expected: GostBelegpruefungErgebnis =
                    belegpruefungsergebnisse_gesamt[jahrgang][id];
                expect(expected.erfolgreich).toBe(ergebnis.erfolgreich);
                const fehlercodes_expected = expected.fehlercodes
                    .toArray<GostBelegpruefungErgebnisFehler>(
                        new Array<GostBelegpruefungErgebnisFehler>()
                    )
                    .map((f) => f.code)
                    .sort();
                const fehlercodes_ergebnis = ergebnis.fehlercodes
                    .toArray<GostBelegpruefungErgebnisFehler>(
                        new Array<GostBelegpruefungErgebnisFehler>()
                    )
                    .map((f) => f.code)
                    .sort();
                expect(fehlercodes_expected).toEqual(fehlercodes_ergebnis);
            });
        });
    }
);
