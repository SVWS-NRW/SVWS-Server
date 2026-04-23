import { describe, expect, test } from "vitest";
import { ApiEnmServerTest } from "../../utils/ApiEnmServerTest";
import { BenutzerConfigElement } from "@core/core/data/benutzer/BenutzerConfigElement";
import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { ENMv2Teilleistung } from "@core/core/data/enm/v2/ENMv2Teilleistung";
import type { ENMv2LeistungBemerkungen } from "@core/core/data/enm/v2/ENMv2LeistungBemerkungen";
import type { ENMv2SchuelerAnkreuzkompetenz } from "@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz";
import type { ENMv2Lernabschnitt } from "@core/core/data/enm/v2/ENMv2Lernabschnitt";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { apiGiesen, apiBerthold } from "./Login";

const apiServiceAuth = apiGiesen;
const apiServiceAuthWrongTeacher = apiBerthold;

export function registerEndpunkteTests() {

	describe("Die Daten aus dem ENM-Server entsprechen den Erwartungen", async () => {
		test("getServerMode", async () => {
			const res = await apiServiceAuth.getServerMode();
			expect(res).toBe(ServerMode.STABLE);
		});
		test("getSchulform", async () => {
			const res = await apiServiceAuth.getSchulform();
			expect(res).toBe(Schulform.G);
		});
		test("getLehrerENMDaten", async () => {
			const daten = await apiServiceAuth.getLehrerENMDaten();
			expect(daten.lehrer.size()).toBe(23);
			expect(daten.schueler.size()).toBe(77);
		});
		test("isAlive", async () => {
			await apiServiceAuth.isAlive();
		});
		test("getClientConfig", async () => {
			const config = await apiServiceAuth.getClientConfig();
			expect(config.global).toBeDefined();
			expect(config.user).toBeDefined();
		});
	});



	describe("Patches verändern die Daten", async () => {

		test("patchENMLeistung", async () => {
			// Bestimme den Test-Schüler
			const idSchueler = 2889;
			const schueler = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schueler.nachname).toBe("Winter");
			expect(schueler.vorname).toBe("Jessika");

			// Führe einen Patch auf die Leistungsdaten aus
			const datum = schueler.leistungsdaten.getFirst();
			expect(datum.note).toBe(null);
			const patch: Partial<ENMv2Leistung> = {
				id: datum.id,
				noteQuartal: "NB",
				note: "5",
				istGemahnt: true,
				fehlstundenFach: 3,
				fehlstundenUnentschuldigtFach: 2,
				fachbezogeneBemerkungen: "Eine Testbemerkung",
			};
			await apiServiceAuth.patchENMLeistung(patch);

			// Lade den Schüler erneut und prüfe, ob die Daten korrekt gepatcht wurden
			const schuelerNeu = await apiServiceAuth.testLadeSchueler(idSchueler);
			const datumNeu = schuelerNeu.leistungsdaten.getFirst();
			expect(datumNeu.noteQuartal).toBe(patch.noteQuartal);
			expect(datumNeu.note).toBe(patch.note);
			expect(datumNeu.istGemahnt).toBe(patch.istGemahnt);
			expect(datumNeu.fehlstundenFach).toBe(patch.fehlstundenFach);
			expect(datumNeu.fehlstundenUnentschuldigtFach).toBe(patch.fehlstundenUnentschuldigtFach);
			expect(datumNeu.fachbezogeneBemerkungen).toBe(patch.fachbezogeneBemerkungen);

			// Prüfe, ob ein Patch durch einen Lehrer, der diese Daten nicht bearbeiten darf fehlschlägt
			await ApiEnmServerTest.testErrorStatus(() => apiServiceAuthWrongTeacher.patchENMLeistung(patch), 403);
		});

		test("patchENMSchuelerLernabschnitt", async () => {
			// Lade den Test-Schüler
			const idSchueler = 3029;
			const schueler = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schueler.nachname).toBe("Lindemann");
			expect(schueler.vorname).toBe("Stefanie");

			// Führe einen Patch auf die Bemerkungen aus
			const datum = schueler.lernabschnitt;
			const patch: Partial<ENMv2Lernabschnitt> = {
				id: datum.id,
				fehlstundenGesamt: 100,
				fehlstundenGesamtUnentschuldigt: 42,
			};
			await apiServiceAuth.patchENMSchuelerLernabschnitt(patch);

			// Lade den Schüler erneut und prüfe, ob die Daten korrekt gepatcht wurden
			const schuelerNeu = await apiServiceAuth.testLadeSchueler(idSchueler);
			const datumNeu = schuelerNeu.lernabschnitt;
			expect(datumNeu.id).toBe(patch.id);
			expect(datumNeu.fehlstundenGesamt).toBe(patch.fehlstundenGesamt);
			expect(datumNeu.fehlstundenGesamtUnentschuldigt).toBe(patch.fehlstundenGesamtUnentschuldigt);

			// Prüfe, ob ein Patch durch einen Lehrer, der diese Daten nicht bearbeiten darf fehlschlägt
			await ApiEnmServerTest.testErrorStatus(() => apiServiceAuthWrongTeacher.patchENMSchuelerLernabschnitt(patch), 403);
		});

		test("patchENMSchuelerBemerkungen", async () => {
			// Lade den Test-Schüler
			const idSchueler = 3074;
			const schueler = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schueler.nachname).toBe("Fusenig");
			expect(schueler.vorname).toBe("Kristin");

			// Führe einen Patch auf die Bemerkungen aus
			const patch: Partial<ENMv2LeistungBemerkungen> = {
				ASV: "Test",
				AUE: "Test",
				ZB: "Test",
			};
			await apiServiceAuth.patchENMSchuelerBemerkungen(idSchueler, patch);

			// Lade den Schüler erneut und prüfe, ob die Daten korrekt gepatcht wurden
			const schuelerNeu = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schuelerNeu.bemerkungen.ASV).toBe(patch.ASV);
			expect(schuelerNeu.bemerkungen.AUE).toBe(patch.AUE);
			expect(schuelerNeu.bemerkungen.ZB).toBe(patch.ZB);

			// Prüfe, ob ein Patch durch einen Lehrer, der diese Daten nicht bearbeiten darf fehlschlägt
			await ApiEnmServerTest.testErrorStatus(() => apiServiceAuthWrongTeacher.patchENMSchuelerBemerkungen(idSchueler, patch), 403);
		});

		test("patchENMTeilleistung", async () => {
			// Lade den Test-Schüler
			const idSchueler = 3029;
			const schueler = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schueler.nachname).toBe("Lindemann");
			expect(schueler.vorname).toBe("Stefanie");

			// Führe Patches auf die Teilleistungen aus
			const leistungsdaten = schueler.leistungsdaten.get(8);
			const patches = new Array<Partial<ENMv2Teilleistung>>;
			for (const datum of leistungsdaten.teilleistungen) {
				const patch: Partial<ENMv2Teilleistung> = {
					id: datum.id,
					note: "6",
				};
				await apiServiceAuth.patchENMTeilleistung(patch);
				patches.push(patch);
			}

			// Lade den Schüler erneut und prüfe, ob die Daten korrekt gepatcht wurden
			const schuelerNeu = await apiServiceAuth.testLadeSchueler(idSchueler);
			const leistungsdatenNeu = schuelerNeu.leistungsdaten.get(8);
			for (let i = 0; i < leistungsdatenNeu.teilleistungen.size(); i++) {
				const datumNeu = leistungsdatenNeu.teilleistungen.get(i);
				const patch = patches[i];
				expect(datumNeu.id).toBe(patch.id);
				expect(datumNeu.note).toBe(patch.note);
			}

			// Prüfe, ob ein Patch durch einen Lehrer, der diese Daten nicht bearbeiten darf fehlschlägt
			await ApiEnmServerTest.testErrorStatus(() => apiServiceAuthWrongTeacher.patchENMTeilleistung(patches[0]), 403);
		});

		test("patchENMSchuelerAnkreuzkompetenzen", async () => {
			// Lade den Test-Schüler
			const idSchueler = 3074;
			const schueler = await apiServiceAuth.testLadeSchueler(idSchueler);
			expect(schueler.nachname).toBe("Fusenig");
			expect(schueler.vorname).toBe("Kristin");

			// Führe einen Patch auf den ersten Eintrag zu Ankreuzkompetenzen aus
			const datum = schueler.ankreuzkompetenzen.getFirst();
			const patch: Partial<ENMv2SchuelerAnkreuzkompetenz> = {
				id: datum.id,
				stufen: [false, false, true, true, false],
			};
			await apiServiceAuth.patchENMSchuelerAnkreuzkompetenzen(patch);

			// Lade den Schüler erneut und prüfe, ob die Daten korrekt gepatcht wurden
			const schuelerNeu = await apiServiceAuth.testLadeSchueler(idSchueler);
			const datumNeu = schuelerNeu.ankreuzkompetenzen.getFirst();
			expect(datumNeu.id).toBe(patch.id);
			expect(datumNeu.stufen).toStrictEqual(patch.stufen);

			// Prüfe, ob ein Patch durch einen Lehrer, der diese Daten nicht bearbeiten darf fehlschlägt
			await ApiEnmServerTest.testErrorStatus(() => apiServiceAuthWrongTeacher.patchENMSchuelerAnkreuzkompetenzen(patch), 403);
		});

	});



	describe("Das Bearbeiten der Config", async () => {

		test.sequential("Ein Wert in der Config kann neu angelegt werden", async () => {
			// Setze den Wert
			await apiServiceAuth.setClientConfigUserKey("Zitroneneis", "Lieblingseis");

			// Lese die Config aus und suche den neu gesetzten Wert
			const configNeu = await apiServiceAuth.getClientConfig();
			let item = new BenutzerConfigElement();
			for (const datum of configNeu.user) {
				if (datum.key === "Lieblingseis") {
					item = datum;
				}
			}
			expect(item.value).toBe("Zitroneneis");
		});

		test.sequential("Ein Wert in der Config kann geändert werden", async () => {
			// Lade die Konfiguration und bestimme das erste Element
			const config = await apiServiceAuth.getClientConfig();
			const datum = config.user.getFirst();
			expect(datum.key).toBe('Lieblingseis');
			expect(datum.value).toBe('Zitroneneis');

			// Setze den Wert neu
			const neuerWert = "Schokoladeneis";
			await apiServiceAuth.setClientConfigUserKey(neuerWert, datum.key);

			// Lade die Konfiguration nue und prüfe, ob jetzt der neue Wert gesetzt ist
			const configNeu = await apiServiceAuth.getClientConfig();
			const datumNeu = configNeu.user.getFirst();
			expect(datumNeu.value).toBe(neuerWert);
		});

		test.sequential("Ein Wert in der Config kann gelöscht werden", async () => {
			// Lade die Konfiguration und bestimme das erste Element
			const config = await apiServiceAuth.getClientConfig();
			const datum = config.user.getFirst();
			expect(datum.key).toBe('Lieblingseis');
			expect(datum.value).toBe('Schokoladeneis');

			// Setze den Wert neu
			const neuerWert = null;
			await apiServiceAuth.setClientConfigUserKey(neuerWert, datum.key);

			// Lade die Konfiguration nue und prüfe, ob jetzt der neue vorhanden ist
			const configNeu = await apiServiceAuth.getClientConfig();
			expect(configNeu.user.isEmpty()).toBeTruthy();
		});

	});

}
