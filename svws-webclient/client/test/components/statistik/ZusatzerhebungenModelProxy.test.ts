import { beforeAll, describe, expect, test, vi } from "vitest";

import { ZusatzerhebungenStatistikGesamt } from "@core/asd/data/statistik/ZusatzerhebungenStatistikGesamt";
import { InternatArt } from "@core/asd/types/schule/InternatArt";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";

import { ZusatzerhebungenModelProxy } from "~/components/statistik/modelproxy/ZusatzerhebungenModelProxy";

/** Wartet, bis die asynchronen Patch-Aufrufe des ModelProxys abgeschlossen sind. */
async function warteAufPatch(): Promise<void> {
	await new Promise(resolve => setTimeout(resolve));
}

function erstelleDaten(idInternatArt: number | null): ZusatzerhebungenStatistikGesamt {
	const daten = new ZusatzerhebungenStatistikGesamt();
	daten.idSchuljahresabschnitt = 11;
	daten.idInternatArt = idInternatArt;
	daten.anzahlInternatplaetzeJungenMoeglich = 32;
	daten.anzahlInternatplaetzeJungenBelegt = 30;
	daten.anzahlInternatplaetzeMaedchenMoeglich = 24;
	daten.anzahlInternatplaetzeMaedchenBelegt = 23;
	daten.anzahlInternatplaetzeNeutralMoeglich = 84;
	daten.anzahlInternatplaetzeNeutralBelegt = 79;
	return daten;
}

function idVon(art: InternatArt): number {
	const eintrag = art.daten(2026);
	if (eintrag === null) {
		throw new Error("Die Internatsart " + art.name() + " ist im Schuljahr 2026 nicht gültig.");
	}
	return eintrag.id;
}

describe("Tests für den ZusatzerhebungenModelProxy", () => {

	beforeAll(() => {
		new JsonCoreTypeReaderStatic().readAll();
	});

	describe("Internatsart und Internatsplätze", () => {

		test("Ohne Auswahl und bei 'kein Internat' werden keine Internatsplätze angeboten", () => {
			expect(new ZusatzerhebungenModelProxy(() => erstelleDaten(null)).hatInternatsplaetze.value).toBe(false);
			expect(new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(InternatArt.KEIN_INTERNAT))).hatInternatsplaetze.value).toBe(false);
		});

		test.each([
			[InternatArt.INTERNAT_JUNGEN, true, false, false],
			[InternatArt.INTERNAT_MAEDCHEN, false, true, false],
			[InternatArt.INTERNAT_NEUTRAL, true, true, true],
		])("Bei %s sind die passenden Platzfelder eingabebereit", (art, jungen, maedchen, neutral) => {
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(art)));
			expect(model.hatInternatsplaetze.value).toBe(true);
			expect(model.erlaubtJungenplaetze.value).toBe(jungen);
			expect(model.erlaubtMaedchenplaetze.value).toBe(maedchen);
			expect(model.erlaubtNeutralplaetze.value).toBe(neutral);
		});

		test("Die Internatsart wird als Katalog-Eintrag geliefert", () => {
			const id = idVon(InternatArt.INTERNAT_MAEDCHEN);
			expect(new ZusatzerhebungenModelProxy(() => erstelleDaten(id)).internatArt.value?.id).toBe(id);
			expect(new ZusatzerhebungenModelProxy(() => erstelleDaten(null)).internatArt.value).toBeNull();
		});

		test("Beim Wechsel auf 'nur für Jungen' werden die Mädchen- und neutralen Plätze auf null zurückgesetzt", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(InternatArt.INTERNAT_NEUTRAL)), patch);

			model.internatArt.value = InternatArt.data().getEintragByID(idVon(InternatArt.INTERNAT_JUNGEN));
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({
				idInternatArt: idVon(InternatArt.INTERNAT_JUNGEN),
				anzahlInternatplaetzeMaedchenMoeglich: null,
				anzahlInternatplaetzeMaedchenBelegt: null,
				anzahlInternatplaetzeNeutralMoeglich: null,
				anzahlInternatplaetzeNeutralBelegt: null,
			});
		});

		test("Beim Wechsel auf 'nur für Mädchen' werden die Jungen- und neutralen Plätze auf null zurückgesetzt", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(InternatArt.INTERNAT_NEUTRAL)), patch);

			model.internatArt.value = InternatArt.data().getEintragByID(idVon(InternatArt.INTERNAT_MAEDCHEN));
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({
				idInternatArt: idVon(InternatArt.INTERNAT_MAEDCHEN),
				anzahlInternatplaetzeJungenMoeglich: null,
				anzahlInternatplaetzeJungenBelegt: null,
				anzahlInternatplaetzeNeutralMoeglich: null,
				anzahlInternatplaetzeNeutralBelegt: null,
			});
		});

		test.each([
			["kein Internat", () => InternatArt.data().getEintragByID(idVon(InternatArt.KEIN_INTERNAT))],
			["keine Auswahl", () => null],
		])("Bei '%s' werden alle Plätze auf null zurückgesetzt", async (_, eintrag) => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(InternatArt.INTERNAT_NEUTRAL)), patch);

			const neu = eintrag();
			model.internatArt.value = neu;
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({
				idInternatArt: neu?.id ?? null,
				anzahlInternatplaetzeJungenMoeglich: null,
				anzahlInternatplaetzeJungenBelegt: null,
				anzahlInternatplaetzeMaedchenMoeglich: null,
				anzahlInternatplaetzeMaedchenBelegt: null,
				anzahlInternatplaetzeNeutralMoeglich: null,
				anzahlInternatplaetzeNeutralBelegt: null,
			});
		});

		test("Beim Wechsel auf 'Jungen und Mädchen' bleiben alle Plätze erhalten", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(idVon(InternatArt.INTERNAT_JUNGEN)), patch);

			model.internatArt.value = InternatArt.data().getEintragByID(idVon(InternatArt.INTERNAT_NEUTRAL));
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({ idInternatArt: idVon(InternatArt.INTERNAT_NEUTRAL) });
		});

		test("Bereits leere Platzfelder werden nicht erneut zurückgesetzt", async () => {
			const patch = vi.fn(async () => true);
			const daten = erstelleDaten(idVon(InternatArt.INTERNAT_NEUTRAL));
			daten.anzahlInternatplaetzeMaedchenMoeglich = null;
			daten.anzahlInternatplaetzeMaedchenBelegt = null;
			daten.anzahlInternatplaetzeNeutralMoeglich = null;
			daten.anzahlInternatplaetzeNeutralBelegt = null;
			const model = new ZusatzerhebungenModelProxy(() => daten, patch);

			model.internatArt.value = InternatArt.data().getEintragByID(idVon(InternatArt.INTERNAT_JUNGEN));
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({ idInternatArt: idVon(InternatArt.INTERNAT_JUNGEN) });
		});
	});

	describe("Validierung", () => {

		test("Negative Anzahlen und Durchschnittswerte sind ungültig, 0 und null sind gültig", () => {
			const daten = erstelleDaten(null);
			daten.anzahlSchulanmeldungen = -1;
			daten.anzahlZurueckstellungenInsgesamtQuartal2 = 0;
			daten.durchschnittKlinikschuleAllgemeinbildendInsgesamt = -0.1;
			daten.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = 0;
			const model = new ZusatzerhebungenModelProxy(() => daten);

			expect(model.getFehler("anzahlSchulanmeldungen").isEmpty()).toBe(false);
			expect(model.getFehler("anzahlZurueckstellungenInsgesamtQuartal2").isEmpty()).toBe(true);
			expect(model.getFehler("anzahlZurueckstellungsverfahren").isEmpty()).toBe(true);
			expect(model.getFehler("durchschnittKlinikschuleAllgemeinbildendInsgesamt").isEmpty()).toBe(false);
			expect(model.getFehler("durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt").isEmpty()).toBe(true);
		});

		test("Eine negative Anzahl wird nicht gepatcht", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(null), patch);

			model.proxy.anzahlSchulanmeldungen = -5;
			await model.patch();

			expect(patch).not.toHaveBeenCalled();
		});

		test("Die Angaben zum Ansprechpartner werden auf Format und Länge geprüft", () => {
			const daten = erstelleDaten(null);
			daten.statistikAnsprechpartnerName = "x".repeat(256);
			daten.statistikAnsprechpartnerTelefon = "keine Nummer";
			daten.statistikAnsprechpartnerEmail = "max@";
			const model = new ZusatzerhebungenModelProxy(() => daten);

			expect(model.getFehler("statistikAnsprechpartnerName").isEmpty()).toBe(false);
			expect(model.getFehler("statistikAnsprechpartnerTelefon").isEmpty()).toBe(false);
			expect(model.getFehler("statistikAnsprechpartnerEmail").isEmpty()).toBe(false);
		});

		test("Gültige Angaben zum Ansprechpartner erzeugen keine Fehler", () => {
			const daten = erstelleDaten(null);
			daten.statistikAnsprechpartnerName = "Max Mustermann";
			daten.statistikAnsprechpartnerTelefon = "0211-980500";
			daten.statistikAnsprechpartnerEmail = "max.mustermann@musterschule.de";
			const model = new ZusatzerhebungenModelProxy(() => daten);

			expect(model.getFehler("statistikAnsprechpartnerName").isEmpty()).toBe(true);
			expect(model.getFehler("statistikAnsprechpartnerTelefon").isEmpty()).toBe(true);
			expect(model.getFehler("statistikAnsprechpartnerEmail").isEmpty()).toBe(true);
		});
	});

	describe("Automatischer Patch", () => {

		test("Die Angabe zu internationalen Kontakten wird direkt gepatcht", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(null), patch);

			model.proxy.hatInternationaleKontakte = true;
			await warteAufPatch();

			expect(patch).toHaveBeenCalledWith({ hatInternationaleKontakte: true });
		});

		test("Zahlwerte werden erst beim Verlassen des Feldes gepatcht", async () => {
			const patch = vi.fn(async () => true);
			const model = new ZusatzerhebungenModelProxy(() => erstelleDaten(null), patch);

			model.proxy.anzahlSchulanmeldungen = 113;
			await warteAufPatch();
			expect(patch).not.toHaveBeenCalled();

			await model.patch();
			expect(patch).toHaveBeenCalledWith({ anzahlSchulanmeldungen: 113 });
		});
	});

});
