import { describe, expect, test, vi } from "vitest";

import { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import { KlassenStammdatenState } from "~/states/klassen/KlassenStammdatenState";

vi.mock("~/router/Api", () => ({
	api: { server: {}, schema: "test" },
}));

// Der KlassenListeManager liest intern schuleStateImpl.schuljahr; daher wird nur `schuljahr` gestubbt.
vi.mock("~/states/SchuleStateImpl", () => ({
	schuleStateImpl: { schuljahr: 2020 },
}));

describe("Tests für KlassenStammdatenState", () => {

	test("Update | Updating class data should reflect changes in the list entry", () => {
		const eintrag = new KlassenListeEintrag();
		eintrag.id = 1;
		eintrag.kuerzel = "A";
		const liste = new ListeMitAuswahl<number, KlassenListeEintrag>(ArrayList.of(eintrag), e => e.id, { compare: (a, b) => a.id - b.id }, null);
		const state = new KlassenStammdatenState({
			onSelect: () => () => { /* wird nicht benötigt */ },
			getEntry: id => liste.get(id),
			updateEntry: (id, update) => update(liste.getOrException(id)),
		});

		const neueDaten = new KlassenDaten();
		neueDaten.id = 1;
		neueDaten.kuerzel = "B";
		state.setDaten(neueDaten);

		expect(state.daten).toBe(neueDaten);
		expect(eintrag.kuerzel).toBe("B");
	});

	test("Reihenfolge | Klassenleitung Reihenfolge should be updated correctly", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const movedUp = KlassenStammdatenState.updateReihenfolge(
			klassenleitungen,
			2,
			true
		);
		expect(movedUp).toBe(true);
		expect(klassenleitungen.toArray()).toEqual([2, 1, 3]);

		const movedDown = KlassenStammdatenState.updateReihenfolge(
			klassenleitungen,
			2,
			false
		);
		expect(movedDown).toBe(true);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | Class management sequence should be false if list has single element", () => {
		const klassenleitungen: List<number> = ArrayList.of(1);
		const result = KlassenStammdatenState.updateReihenfolge(
			klassenleitungen,
			2,
			true
		);
		expect(result).toBe(false);
	});

	test("Sequence | Upper edge condition", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const result = KlassenStammdatenState.updateReihenfolge(
			klassenleitungen,
			1,
			true
		);
		expect(result).toBe(false);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | Bottom edge condition", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2, 3);
		const result = KlassenStammdatenState.updateReihenfolge(
			klassenleitungen,
			3,
			false
		);
		expect(result).toBe(false);
		expect(klassenleitungen.toArray()).toEqual([1, 2, 3]);
	});

	test("Sequence | An error is thrown if the class teacher is not in the list", () => {
		const klassenleitungen: List<number> = ArrayList.of(1, 2);

		expect(() => {
			KlassenStammdatenState.updateReihenfolge(
				klassenleitungen,
				3,
				true
			);
		}).toThrow(
			"Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden."
		);
	});

});
