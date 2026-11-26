import { beforeAll, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import type { GenericAuswahlGruppenprozessHost } from "~/states/GenericAuswahlState";
import { SchuelerStammdatenBereich } from "~/states/schueler/SchuelerStammdatenBereich";

vi.mock("~/router/Api", () => ({
	api: {
		server: {
			getSchuelerStammdatenMultiple: vi.fn(async (ids: List<number>) => ArrayList.of(...[...ids].map(id => Object.assign(new SchuelerStammdaten(), { id })))),
		},
		schema: "test",
		status: { start: vi.fn(), stop: vi.fn() },
	},
}));

vi.mock("~/states/SchuleStateImpl", () => ({
	schuleStateImpl: { schuljahr: 2020 },
}));

const jahr = new Date().getFullYear();
const MIN = jahr + 1 - 100;
const MAX = jahr + 1;

describe("SchuelerStammdatenBereich", () => {

	test("das gültige Zuzugsjahr liegt zwischen 100 Jahren vor und einem Jahr nach dem aktuellen Jahr", () => {
		const bereich = new SchuelerStammdatenBereich();
		expect(bereich.minZuzugsjahr).toBe(MIN);
		expect(bereich.maxZuzugsjahr).toBe(MAX);
	});

	describe("setZuzugsjahr", () => {

		test("ein gültiges Zuzugsjahr wird als ausstehende Änderung gesetzt, das Speichern bleibt möglich", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(2015);
			expect(bereich.model.pending.zuzugsjahr).toBe(2015);
			expect(bereich.isPatchAllowed).toBe(true);
		});

		test.each([MIN, MAX])("die Grenze %i ist gültig", grenze => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(grenze);
			expect(bereich.model.pending.zuzugsjahr).toBe(grenze);
			expect(bereich.isPatchAllowed).toBe(true);
		});

		test.each([MIN - 1, MAX + 1])("das ungültige Zuzugsjahr %i wird nicht übernommen und sperrt das Speichern mit Hinweis", ungueltig => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(ungueltig);
			expect(bereich.model.isPending("zuzugsjahr")).toBe(false);
			expect(bereich.isPatchAllowed).toBe(false);
			expect(bereich.patchHinweise).toEqual([`Zuzugsjahr: Der Wert muss zwischen ${MIN} und ${MAX} liegen.`]);
		});

		test("ein ungültiges Zuzugsjahr entfernt eine bereits gesetzte ausstehende Änderung", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(2015);
			bereich.setZuzugsjahr(MAX + 1);
			expect(bereich.model.isPending("zuzugsjahr")).toBe(false);
		});

		test("ein gültiges Zuzugsjahr hebt die Sperre wieder auf", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(MAX + 1);
			bereich.setZuzugsjahr(2015);
			expect(bereich.isPatchAllowed).toBe(true);
			expect(bereich.model.pending.zuzugsjahr).toBe(2015);
		});

		test("ohne Wert wird die ausstehende Änderung entfernt und die Sperre aufgehoben", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(2015);
			bereich.setZuzugsjahr(MAX + 1);
			bereich.setZuzugsjahr(null);
			expect(bereich.model.isPending("zuzugsjahr")).toBe(false);
			expect(bereich.isPatchAllowed).toBe(true);
		});

	});

	describe("resetMigrationshintergrund", () => {

		test("verwirft die ausstehenden Änderungen aller Felder zum Migrationshintergrund und hebt die Sperre des Zuzugsjahres auf", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.setZuzugsjahr(2015);
			bereich.model.setPending("idGeburtsland", 5);
			bereich.model.setPending("idVerkehrspracheFamilie", 6);
			bereich.model.setPending("idGeburtslandMutter", 7);
			bereich.model.setPending("idGeburtslandVater", 8);
			bereich.setZuzugsjahr(MAX + 1);
			bereich.resetMigrationshintergrund();
			expect(bereich.model.pending).toEqual({});
			expect(bereich.isPatchAllowed).toBe(true);
		});

		test("lässt ausstehende Änderungen anderer Felder unberührt", () => {
			const bereich = new SchuelerStammdatenBereich();
			bereich.model.setPending("istVolljaehrig", true);
			bereich.model.setPending("idGeburtsland", 5);
			bereich.resetMigrationshintergrund();
			expect(bereich.model.pending).toEqual({ istVolljaehrig: true });
		});

	});

	describe("Bindungen nach dem Abmelden", () => {

		beforeAll(() => {
			new JsonCoreTypeReaderStatic().readAll();
		});

		/** Die Mehrfachauswahl ist reaktiv wie im Auswahl-State, damit die Bindungen des Proxys bei einer Änderung neu berechnet werden */
		const auswahl = ref([1, 2]);
		const host: GenericAuswahlGruppenprozessHost<any> = {
			getEntry: id => ({ id, status: 2 }),
			getMultiSelection: () => ArrayList.of(...auswahl.value),
		};

		test("alleExtern eines nicht angemeldeten Bereiches wirft keinen Fehler", () => {
			const bereich = new SchuelerStammdatenBereich();
			expect(() => bereich.model.alleExtern.value).not.toThrow();
		});

		test("alleExtern wirft nach dem Abmelden keinen Fehler, z.B. beim Verlassen der Gruppenprozesse (Auswahl aufheben)", async () => {
			const bereich = new SchuelerStammdatenBereich();
			await bereich.anmelden(host, host.getMultiSelection());
			expect(() => bereich.model.alleExtern.value).not.toThrow();
			bereich.abmelden();
			auswahl.value = []; // Der Auswahl-State hebt die Mehrfachauswahl nach dem Verlassen der Gruppenprozesse auf
			expect(() => bereich.model.alleExtern.value).not.toThrow();
		});

	});

});
