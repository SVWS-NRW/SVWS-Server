import { beforeAll, describe, expect, test, vi } from "vitest";
import { nextTick, type Ref, ref } from "vue";

import type { CoreTypeData } from "@core/asd/data/CoreTypeData";
import type { KlassenartKatalogEintrag } from "@core/asd/data/klassen/KlassenartKatalogEintrag";
import { Klassenart } from "@core/asd/types/klassen/Klassenart";
import { LehrerZugangsgrund } from "@core/asd/types/lehrer/LehrerZugangsgrund";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import type { Class } from "@core/java/lang/Class";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";
import type { SelectFilter } from "@ui/ui/controls/select/filter/SelectFilter";
import type { SelectSort } from "@ui/ui/controls/select/manager/BaseSelectManager";
import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

const reader = new JsonCoreTypeReaderStatic();
vi.mock("@json/klassen/Klassenart.json", async () => ({
	default: (await import("./Klassenart.mock.json")).default,
}));

beforeAll(async () => {
	reader.readAll();
});

describe("UiSelect CoreTypeSelectManager Tests", () => {

	describe("Konfigurationen", () => {
		test("Neuer Manager ohne config hat Default-Werte", () => {
			const { klassenart } = createTestData();
			const manager = new CoreTypeSelectManager();

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('Neuer Manager mit clazz setzt filteredOptions und unfilteredOptions mit aktuellsten Historieneinträgen', () => {
			const { clazz, sortedAlphabetical, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und schuljahr setzt filteredOptions und unfilteredOptions mit dem angegebenen Jahr.', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr });

			expect(manager.unfilteredOptions.size()).toBe(13);
			[
				"** - Kein Eintrag",
				"BK - B - Kurzer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"KK - K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"OK - O - Kurzer Eintrag",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"PK - P - Ein etwas längerer Eintrag",
				"ZK - Z - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(13);
			[
				"** - Kein Eintrag",
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"BK - B - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"KK - K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"OK - O - Kurzer Eintrag",
				"PK - P - Ein etwas längerer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"ZK - Z - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und schulformen(einzelne) setzt filteredOptions und unfilteredOptions mit zulässiger Schulform und Schuljahr.', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr, schulformen: Schulform.GY });

			expect(manager.unfilteredOptions.size()).toBe(6);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(6);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und schulformen(einzelne) setzt filteredOptions und unfilteredOptions mit zulässiger Schulform und aktuellsten Einträgen.', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schulformen: Schulform.GY });

			expect(manager.unfilteredOptions.size()).toBe(7);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"CK - C - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und schulformen(mehrere) setzt filteredOptions und unfilteredOptions mit zulässigen Schulformen und Schuljahr.', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr, schulformen: [Schulform.GY, Schulform.H] });

			expect(manager.unfilteredOptions.size()).toBe(10);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"BK - B - Kurzer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"KK - K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"PK - P - Ein etwas längerer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(10);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"BK - B - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"KK - K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"PK - P - Ein etwas längerer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und schulformen(mehrere) setzt filteredOptions und unfilteredOptions mit zulässigen Schulformen und aktuellsten Einträgen.', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schulformen: [Schulform.GY, Schulform.H] });

			expect(manager.unfilteredOptions.size()).toBe(12);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
				"BK - B - Kurzer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"KK - K - Aktuellster Eintrag",
				"AK - A - Kurzer Eintrag",
				"PK - P - Ein etwas längerer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(12);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"BK - B - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"KK - K - Aktuellster Eintrag",
				"PK - P - Ein etwas längerer Eintrag",
				"SK - KS - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = undefined -> sortiert alphabetisch, da der CoreType keine Prop "sortierung" besitzt, welches die Defaultsortierung wäre', () => {
			const { clazz, unsorted, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = "sortierungProp" -> sortiert alphabetisch, da der CoreType keine Prop "sortierung" besitzt', () => {
			const { clazz, unsorted, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort: "sortierungProp" });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = "idProp" -> sortiert die Optionen nach ID', () => {
			const { clazz, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort: "idProp" });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			[
				"0 - Kein Eintrag",
				"1003 - B - Kurzer Eintrag",
				"2004 - KS - Kurzer Eintrag",
				"3001 - K - Aktuellster Eintrag",
				"4000 - O - Kurzer Eintrag",
				"5000 - Z - Kurzer Eintrag",
				"6001 - V - Kurzer Eintrag",
				"7002 - T - Kurzer Eintrag",
				"8001 - I - Kurzer Eintrag",
				"9000 - AA - Ein längerer Eintrag mit 45 Zeichen",
				"10003 - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"11002 - A - Kurzer Eintrag",
				"12005 - H - Kurzer Eintrag",
				"13002 - P - Ein etwas längerer Eintrag",
				"14003 - C - Kurzer Eintrag",
			].forEach((element, index) => {
				const option = manager.filteredOptions.get(index);
				const displayText = `${option.id} - ${option.text}`;
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = "alphabetical" -> sortiert die Optionen alphabetisch', () => {
			const { clazz, unsorted, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort: "alphabetical" });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = "unsorted" -> behält die ursprüngliche Reihenfolge bei', () => {
			const { clazz, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort: "unsorted" });

			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			unsorted.forEach((element, index) => {
				const unfilteredDisplayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(unfilteredDisplayText).toBe(element);

				const filteredDisplayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(filteredDisplayText).toBe(element);
			});
		});

		test("Neuer Manager mit clazz und sort als Funktion -> sortiert nach dem eigenen Comparator", () => {
			const { clazz, sort, unsorted, sortedByText } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedByText.forEach((element, index) => {
				const displayText = manager.filteredOptions.get(index).text;
				expect(displayText).toBe(element);
			});
		});

		test("Neuer Manager mit clazz und sort als Comparator-Objekt -> sortiert nach dem eigenen Comparator", () => {
			const { clazz, sort, unsorted, sortedByText } = createTestData();
			const comparator = { compare: sort };
			const manager = new CoreTypeSelectManager({ clazz, sort: comparator });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(15);
			sortedByText.forEach((element, index) => {
				const displayText = manager.filteredOptions.get(index).text;
				expect(displayText).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und filter erzeugt eine gefilterte Optionenliste.', () => {
			const { clazz, startsWithFilter, longNameFilter, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");
		});

		test('Neuer Manager mit clazz, filter und sort erzeugt eine gefilterte und sortierte Optionenliste.', () => {
			const { clazz, sort, longNameFilter, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, sort: sort, filters: [longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - Ein längerer Eintrag mit 45 Zeichen",
				"BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test("Neuer Manager mit clazz und selectionDisplayText(Funktion) hat Custom Text für die Selektion", () => {
			const { clazz, klassenart, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, selectionDisplayText: selectionDisplayText });
			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
		});

		test.each([
			{ selectionDisplayText: 'kuerzel', expected: "OK" },
			{ selectionDisplayText: 'text', expected: "O - Kurzer Eintrag" },
			{ selectionDisplayText: 'kuerzelText', expected: "OK - O - Kurzer Eintrag" },
		] as const)("Neuer Manager mit clazz und selectionDisplayText($selectionDisplayText) hat '$expected' als Text für die Selektion", ({ selectionDisplayText, expected }) => {
			const { clazz, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, selectionDisplayText });
			expect(manager.getSelectionText(klassenart)).toBe(expected);
		});

		test("Neuer Manager mit clazz und optionDisplayText(Funktion) hat Custom Text für die Optionen", () => {
			const { clazz, klassenart, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, optionDisplayText: optionDisplayText });
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");
		});

		test.each([
			{ optionDisplayText: 'kuerzel', expected: "OK" },
			{ optionDisplayText: 'text', expected: "O - Kurzer Eintrag" },
			{ optionDisplayText: 'kuerzelText', expected: "OK - O - Kurzer Eintrag" },
		] as const)("Neuer Manager mit clazz und optionDisplayText($optionDisplayText) hat '$expected' als Text für die Optionen", ({ optionDisplayText, expected }) => {
			const { clazz, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, optionDisplayText });
			expect(manager.getOptionText(klassenart)).toBe(expected);
		});

		test("setConfig() -> es ändert sich nichts an den Konfigurationen", () => {
			const { klassenart } = createTestData();
			const manager = new CoreTypeSelectManager();

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");

			manager.setConfig();

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("setConfig({}, true) -> es werden die Defaultwerte für alle Displayfunktionen gesetzt", () => {
			const { klassenart, selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ selectionDisplayText, optionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");

			manager.setConfig({}, true);

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("setConfig({}, false) -> es ändert sich nichts an den Konfigurationen", () => {
			const { klassenart } = createTestData();
			const manager = new CoreTypeSelectManager();

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");

			manager.setConfig({}, false);

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("setConfig({selectionDisplayText}, false) -> selectionDisplayText erhält einen neuen Wert", () => {
			const { klassenart, selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ selectionDisplayText, optionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s.text}` }, false);

			expect(manager.getSelectionText(klassenart)).toBe("newSel:O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");
		});

		test("setConfig({selectionDisplayText}, true) -> selectionDisplayText erhält einen neuen Wert, optionDisplayText erhält Defaultwert", () => {
			const { klassenart, selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ selectionDisplayText, optionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s.text}` }, true);

			expect(manager.getSelectionText(klassenart)).toBe("newSel:O - Kurzer Eintrag");
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});
	});

	describe.concurrent("Reaktivität", () => {

		test("Neue clazz bei Änderung des Refs", async () => {
			const { clazz, unsorted } = createTestData();
			const clazzRef: Ref<Class<Klassenart> | null> = ref(null);
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazzRef });


			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);

			clazzRef.value = clazz;
			await nextTick();

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test("Neues schuljahr bei Änderung des Refs", async () => {
			const { schuljahr } = createTestData();
			const schuljahrRef = ref<number | null>(null);
			const manager = new CoreTypeSelectManager({ schuljahr: schuljahrRef });

			expect(manager.schuljahr).toBeNull();

			schuljahrRef.value = schuljahr;
			await nextTick();

			expect(manager.schuljahr).toBe(schuljahr);
		});

		test("Neue schulformen bei Änderung des Refs", async () => {
			const schulformenRef = ref<Schulform | null>(null);
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ schulformen: schulformenRef });

			schulformenRef.value = Schulform.GY;
			await nextTick();

			expect(manager.schulformen).toBeInstanceOf(Schulform);
			expect((manager.schulformen as Schulform).__name).toBe(Schulform.GY.__name);
		});


		test("Neues selectionDisplayText bei Änderung des Refs", async () => {
			const { klassenart, selectionDisplayText } = createTestData();
			const selRef = ref<(s: KlassenartKatalogEintrag) => string>(selectionDisplayText);
			const manager = new CoreTypeSelectManager({ selectionDisplayText: selRef });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");

			selRef.value = (s) => `newSel:${s.text}`;
			await nextTick();

			expect(manager.getSelectionText(klassenart)).toBe("newSel:O - Kurzer Eintrag");
		});
		test("Neues optionDisplayText bei Änderung des Refs", async () => {
			const { klassenart, optionDisplayText } = createTestData();
			const optRef = ref<(s: KlassenartKatalogEintrag) => string>(optionDisplayText);
			const manager = new CoreTypeSelectManager({ optionDisplayText: optRef });

			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");

			optRef.value = (s) => `newOpt:${s.text}`;
			await nextTick();

			expect(manager.getOptionText(klassenart)).toBe("newOpt:O - Kurzer Eintrag");
		});

		test("Neue filters bei Änderung des Refs", async () => {
			const { clazz, startsWithFilter } = createTestData();
			const filterRef = ref<SelectFilter<KlassenartKatalogEintrag>[]>([]);
			const manager = new CoreTypeSelectManager({ clazz, filters: filterRef });

			expect(manager.filteredOptions.size()).toBe(manager.unfilteredOptions.size());

			filterRef.value = [startsWithFilter];
			await nextTick();

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test("Neues sort bei Änderung des Refs", async () => {
			const { clazz, sort, unsorted, sortedByText } = createTestData();
			const sortRef = ref<SelectSort<KlassenartKatalogEintrag>>("sortierungProp");
			const manager = new CoreTypeSelectManager({ clazz, sort: sortRef });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortRef.value = { compare: sort };
			await nextTick();

			sortedByText.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

	});

	describe.concurrent("Funktionen", () => {

		test('get unfilteredOptions() mit Optionen und Filtern -> Gibt die ungefilterten Optionen zurück', () => {
			const { clazz, startsWithFilter, longNameFilter, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('get filteredOptions() mit Optionen und Filtern -> Gibt die gefilterten Optionen zurück', () => {
			const { clazz, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");

		});


		test('updateFilteredOptions() -> aktualisiert alle Optionen', () => {
			const { clazz, letter, lettersCount, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");

			letter.value = "B";
			lettersCount.value = 5;
			manager.updateFilteredOptions();
			expect(manager.filteredOptions.size()).toBe(2);
			[
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"BK - B - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('updateFilteredOptions(longNameFilter) -> aktualisiert nur die Optionen des Filters', () => {
			const { clazz, startsWithFilter, longNameFilter, letter, lettersCount } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");

			letter.value = "B";
			lettersCount.value = 41;
			manager.updateFilteredOptions(longNameFilter);

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText2 = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText2).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");
		});


		test('updateFilteredOptions(longNameFilter, false) -> aktualisiert nur die Optionen des Filters', () => {
			const { clazz, startsWithFilter, longNameFilter, letter, lettersCount } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");

			letter.value = "B";
			lettersCount.value = 41;
			manager.updateFilteredOptions(longNameFilter, false);
			expect(manager.filteredOptions.size()).toBe(1);
			const displayText2 = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText2).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");
		});


		test('updateFilteredOptions(longNameFilter, true) -> löscht den Filter und aktualisiert keine anderen Filter', () => {
			const { clazz, startsWithFilter, longNameFilter, letter, lettersCount } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen");

			letter.value = "B";
			lettersCount.value = 41;
			manager.updateFilteredOptions(longNameFilter, true);
			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('toList(List) -> gibt List unverändert zurück', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			const arrayList = new ArrayList<CoreTypeData>();
			for (const f of manager.unfilteredOptions) {
				arrayList.add(f);
			}

			const result = manager.toList(arrayList);

			expect(result).toBe(arrayList);
			expect(result.size()).toBe(manager.unfilteredOptions.size());

			for (let i = 0; i < manager.unfilteredOptions.size(); i++) {
				expect(result.get(i)).toBe(manager.unfilteredOptions.get(i));
			}
		});


		test('toList(Array) -> konvertiert das Array zu ArrayList', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			const arr = manager.unfilteredOptions.toArray();

			const result = manager.toList(arr);

			expect(result).toBeInstanceOf(ArrayList);
			expect(result.size()).toBe(manager.unfilteredOptions.size());

			for (let i = 0; i < manager.unfilteredOptions.size(); i++) {
				expect(result.get(i)).toBe(manager.unfilteredOptions.get(i));
			}
		});


		test('toList(null|undefined) -> gibt leere ArrayList zurück', () => {
			const manager = new CoreTypeSelectManager();

			const resultNull = manager.toList(null);
			expect(resultNull).toBeInstanceOf(ArrayList);
			expect(resultNull.size()).toBe(0);

			const resultUndefined = manager.toList(undefined);
			expect(resultUndefined).toBeInstanceOf(ArrayList);
			expect(resultUndefined.size()).toBe(0);
		});


		test('addFilter(Filter) ohne bereits existierenden Filter mit diesem Schlüssel -> fügt neuen Filter hinzu und aktualisiert die gefilterten Optionen', () => {
			const { clazz, startsWithFilter, unsorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.unfilteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('addFilter(Filter) mit bereits existierenden Filter mit diesem Schlüssel -> überschreibt den Filter und aktualisiert die gefilterten Optionen', () => {
			const { clazz, startsWithFilter, letter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			letter.value = "K";
			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(1);
			const displayText = manager.getOptionText(manager.filteredOptions.getFirst());
			expect(displayText).toBe("KK - K - Aktuellster Eintrag");
		});


		test('removeFilter(Filter) mit existierendem Filter -> entfernt existierenden Filter und aktualisiert die gefilterten Optionen', () => {
			const { clazz, startsWithFilter, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('removeFilter(key) mit existierendem Filter -> entfernt existierenden Filter per Schlüssel und aktualisiert die gefilterten Optionen', () => {
			const { clazz, startsWithFilter, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"AK - A - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.removeFilter("startsWithLetter");

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('removeFilter(Filter) ohne existierenden Filter -> entfernt nichts', () => {
			const { clazz, startsWithFilter, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.removeFilter(startsWithFilter);
			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('removeFilter(key) ohne existierenden Filter -> entfernt nichts', () => {
			const { clazz, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.removeFilter("startsWithFilter");

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('getFilterByKey(filterKey) und Filter existiert -> gibt Filter zurück', () => {
			const { clazz, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, filters: [startsWithFilter, longNameFilter] });

			const result = manager.getFilterByKey("startsWithLetter");

			expect(result).toBe(startsWithFilter);
		});


		test('getFilterByKey(filterKey) und Filter existiert nicht -> gibt null zurück', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			const result = manager.getFilterByKey("nonExistingFilter");

			expect(result).toBeNull();
		});

		test("get selectionDisplayText() -> gibt den Wert von selectionDisplayText zurück", () => {
			const manager = new CoreTypeSelectManager();

			expect(manager.selectionDisplayText).toBe("kuerzelText");
		});

		test("set selectionDisplayText() -> setzt den Wert von selectionDisplayText", () => {
			const { clazz, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz });
			manager.selectionDisplayText = selectionDisplayText;

			expect(manager.selectionDisplayText).toBe(selectionDisplayText);
		});

		test('getSelectionText(null) -> gibt leeren String zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.getSelectionText(null);
			expect(result).toBe("");
		});

		test('getSelectionText(selection) ohne selectionDisplayText -> gibt das Kürzel mit Text der Selection zurück', () => {
			const { klassenart, clazz } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz });

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test('getSelectionText(selection) mit Funktion als selectionDisplayText-> gibt den Text der Selection entsprechend der Funktion zurück', () => {
			const { klassenart, clazz, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz, selectionDisplayText: selectionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
		});

		test.each([
			{ selectionDisplayText: 'text', expected: "O - Kurzer Eintrag" },
			{ selectionDisplayText: 'kuerzel', expected: "OK" },
			{ selectionDisplayText: 'kuerzelText', expected: "OK - O - Kurzer Eintrag" },
		] as const)('getSelectionText(selection) mit selectionDisplayText = $selectionDisplayText -> gibt $expected zurück', ({ selectionDisplayText, expected }) => {
			const { klassenart, clazz } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz, selectionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe(expected);
		});

		test("get optionDisplayText() -> gibt den Wert von optionDisplayText zurück", () => {
			const manager = new CoreTypeSelectManager();

			expect(manager.optionDisplayText).toBe("kuerzelText");
		});

		test("set optionDisplayText() -> setzt den Wert von optionDisplayText", () => {
			const { clazz, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz });
			manager.optionDisplayText = optionDisplayText;

			expect(manager.optionDisplayText).toBe(optionDisplayText);
		});

		test('getOptionText(null) -> gibt leeren String zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.getOptionText(null);
			expect(result).toBe("");
		});

		test('getOptionText(option) ohne optionDisplayText -> gibt das Kürzel mit Text der Option zurück', () => {
			const { klassenart, clazz } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz });

			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test('getOptionText(option) mit Funktion als optionDisplayText-> gibt den Text der Option entsprechend der Funktion zurück', () => {
			const { klassenart, clazz, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz, optionDisplayText: optionDisplayText });

			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");
		});

		test.each([
			{ optionDisplayText: 'text', expected: "O - Kurzer Eintrag" },
			{ optionDisplayText: 'kuerzel', expected: "OK" },
			{ optionDisplayText: 'kuerzelText', expected: "OK - O - Kurzer Eintrag" },
		] as const)('getOptionText(option) mit optionDisplayText = $optionDisplayText -> gibt $expected zurück', ({ optionDisplayText, expected }) => {
			const { klassenart, clazz } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz, optionDisplayText });

			expect(manager.getOptionText(klassenart)).toBe(expected);
		});

		test('getComparator() mit sort = "unsorted" -> gibt null zurück', () => {
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ sort: "unsorted" });

			const result = manager.getComparator();

			expect(result).toBeNull();
		});

		test('getComparator() mit sort Funktion -> gibt Comparator Objekt mit compare zurück', () => {
			const { sort } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ sort });
			const result = manager.getComparator();

			expect(result).not.toBeNull();
			expect(typeof result!.compare).toBe("function");
		});

		test('getComparator() mit sort = Comparator Objekt -> gibt das gleiche Comparator Objekt zurück', () => {
			const { sort } = createTestData();
			const comp: Comparator<KlassenartKatalogEintrag> = { compare: sort };
			const manager = new CoreTypeSelectManager({ sort: comp });
			const result = manager.getComparator();

			expect(result).toBe(comp);
		});

		test.each(["sortierungProp", "idProp", "alphabetical", null, undefined] as const)(
			'getComparator() mit sort = "%s" -> gibt einen Comparator zurück',
			(sort) => {
				const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ sort });

				const result = manager.getComparator();

				expect(result).not.toBeNull();
				expect(typeof result?.compare).toBe("function");
			}
		);

		test('updateSort() comparator ist null -> bringt die Optionen in ihre ursprüngliche Reihenfolge', async () => {
			const { clazz, sortedAlphabetical, unsorted } = createTestData();
			const sortRef = ref<SelectSort<KlassenartKatalogEintrag>>(null);
			const manager = new CoreTypeSelectManager({ clazz, sort: sortRef });

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortRef.value = "unsorted"; // erzeugt comparator = null
			await nextTick();

			expect(manager.filteredOptions.size()).toBe(15);
			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});


		test('updateSort() comparator ist vorhanden -> sortiert die gefilterten Optionen', async () => {
			const { clazz, sort, sortedAlphabetical, sortedByText } = createTestData();
			const sortRef = ref<SelectSort<KlassenartKatalogEintrag>>(null);
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz, sort: sortRef });

			expect(manager.filteredOptions.size()).toBe(15);
			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortRef.value = sort;
			await nextTick();

			sortedByText.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('updateSort() filteredOptions leer -> es wird nichts sortiert', () => {
			const { sort } = createTestData();
			const sortRef = ref<SelectSort<KlassenartKatalogEintrag>>(null);
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ sort: sortRef });

			expect(manager.filteredOptions.size()).toBe(0);

			sortRef.value = sort;
			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('get clazz() -> gibt die aktuell gesetzte Klasse zurück', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.clazz).toBe(clazz);
		});

		test('set clazz(neuer Wert) -> setzt neue Klasse, aktualisiert Manager und ruft updateOptions auf', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.clazz = LehrerZugangsgrund.class;
			expect(manager.clazz).toBe(LehrerZugangsgrund.class);
			expect(manager.unfilteredOptions.size()).toBe(5);
			expect(manager.filteredOptions.size()).toBe(5);
		});

		test('set clazz(selber Wert) -> setzt weder eine neue Klasse, noch neue Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.clazz = clazz;
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);
		});

		test('set clazz(null) -> setzt die Klasse auf null und löscht alle Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.clazz = null;
			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('set clazz(undefined) -> setzt die Klasse auf undefined und löscht alle Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.clazz = undefined;
			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('get schuljahr() -> gibt das aktuell gesetzte Schuljahr zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr });

			expect(manager.schuljahr).toBe(schuljahr);
		});

		test('set schuljahr(neuer Wert) -> setzt neues Schuljahr und aktualisiert die Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });
			expect(manager.schuljahr).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.schuljahr = 2025;
			expect(manager.schuljahr).toBe(2025);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);
		});

		test('set schuljahr(null) -> setzt das Schuljahr auf null und aktualisiert die Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr });
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schuljahr = null;
			expect(manager.schuljahr).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);
		});

		test('set schuljahr(selber Wert) -> ändert weder das Schuljahr, noch die Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schuljahr: schuljahr });
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schuljahr = schuljahr;
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);
		});

		test('get schulformen() -> gibt die aktuell gesetzten Schulformen zurück', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schulformen: [Schulform.BK, Schulform.GY] });

			expect(manager.schulformen).not.toBeNull();
			// Checkt, ob Schulformen eine Liste ist
			expect(Symbol.iterator in new Object(manager.schulformen!)).toBe(true);
			expect(manager.schulformen as Schulform[]).toHaveLength(2);
			expect((manager.schulformen as Schulform[])).toEqual([Schulform.BK, Schulform.GY]);
		});

		test('set schulformen(neuer Wert) -> setzt neue Schulformen und aktualisiert die Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz });

			expect(manager.schulformen).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			manager.schulformen = Schulform.GY;
			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"CK - C - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('set schulformen(null) -> setzt Schulformen auf null und aktualisiert die Optionen', () => {
			const { clazz, unsorted, sortedAlphabetical } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schulformen: Schulform.GY });

			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			[
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"CK - C - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			].forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.schulformen = null;
			expect(manager.schulformen).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(15);
			expect(manager.filteredOptions.size()).toBe(15);

			unsorted.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortedAlphabetical.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

		test('set schulformen(selber Wert) -> setzt weder neue Schulformen, noch neue Optionen', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz, schulformen: Schulform.GY });

			const unsortedEntries = [
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"HK - H - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"CK - C - Kurzer Eintrag",
			];

			const sortedEntries = [
				"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
				"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
				"CK - C - Kurzer Eintrag",
				"HK - H - Kurzer Eintrag",
				"IK - I - Kurzer Eintrag",
				"TK - T - Kurzer Eintrag",
				"VK - V - Kurzer Eintrag",
			];

			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			unsortedEntries.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortedEntries.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			manager.schulformen = Schulform.GY;
			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			unsortedEntries.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.unfilteredOptions.get(index));
				expect(displayText).toBe(element);
			});

			sortedEntries.forEach((element, index) => {
				const displayText = manager.getOptionText(manager.filteredOptions.get(index));
				expect(displayText).toBe(element);
			});
		});

	});
});

/**
 * Erstellt Testdaten für die Tests des CoreTypeSelectManagers.
 *
 * @returns die testdaten: clazz, schuljahr, klassenart, lettersCount, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort,
 * 			unsorted, sortedAlphabetical, sortedByText,
 */
function createTestData() {
	const clazz = Klassenart.class;
	const schuljahr = 2010;
	const klassenart = Klassenart.AM.daten(schuljahr);
	const lettersCount = { value: 39 };
	const letter = { value: "A" };

	const startsWithFilter = {
		key: "startsWithLetter",
		apply: (options: List<KlassenartKatalogEintrag>) => {
			const filtered = new ArrayList<KlassenartKatalogEintrag>();
			for (const option of options) {
				const text = option.kuerzel + " - " + option.text;
				if (text.startsWith(letter.value)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const longNameFilter = {
		key: "longName",
		apply: (options: List<KlassenartKatalogEintrag>) => {
			const filtered = new ArrayList<KlassenartKatalogEintrag>();
			for (const option of options) {
				const text = option.kuerzel + " - " + option.text;
				if (text.length > lettersCount.value) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const selectionDisplayText = (s: KlassenartKatalogEintrag) => `sel:${s.text}`;
	const optionDisplayText = (s: KlassenartKatalogEintrag) => `opt:${s.text}`;
	const sort = (a: KlassenartKatalogEintrag, b: KlassenartKatalogEintrag) => a.text.localeCompare(b.text);

	/**
	 * Unsortierte aktuelle Historieneinträge mit dem Anzeigetext "Kürzel - Text".
	 */
	const unsorted = [
		"** - Kein Eintrag",
		"BK - B - Kurzer Eintrag",
		"SK - KS - Kurzer Eintrag",
		"KK - K - Aktuellster Eintrag",
		"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
		"OK - O - Kurzer Eintrag",
		"AK - A - Kurzer Eintrag",
		"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
		"HK - H - Kurzer Eintrag",
		"PK - P - Ein etwas längerer Eintrag",
		"ZK - Z - Kurzer Eintrag",
		"VK - V - Kurzer Eintrag",
		"TK - T - Kurzer Eintrag",
		"IK - I - Kurzer Eintrag",
		"CK - C - Kurzer Eintrag",
	];

	/**
	 * Aktuelle Historieneinträge sortiert nach der Prop text.
	 * Hier stehen bewusst nur die Textwerte, da der Comparator option.text vergleicht.
	 */
	const sortedByText = [
		"A - Kurzer Eintrag",
		"AA - Ein längerer Eintrag mit 45 Zeichen",
		"B - Kurzer Eintrag",
		"BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
		"C - Kurzer Eintrag",
		"H - Kurzer Eintrag",
		"I - Kurzer Eintrag",
		"K - Aktuellster Eintrag",
		"Kein Eintrag",
		"KS - Kurzer Eintrag",
		"O - Kurzer Eintrag",
		"P - Ein etwas längerer Eintrag",
		"T - Kurzer Eintrag",
		"V - Kurzer Eintrag",
		"Z - Kurzer Eintrag",
	];

	/**
	 * Aktuelle Historieneinträge nach dem Default-Comparator sortiert.
	 * Die Werte entsprechen dem optionDisplayText "Kürzel - Text".
	 */
	const sortedAlphabetical = [
		"** - Kein Eintrag",
		"AA - BB - Ein sehr laaanger Eintrag mit 50 Zeichen",
		"AK - A - Kurzer Eintrag",
		"BB - AA - Ein längerer Eintrag mit 45 Zeichen",
		"BK - B - Kurzer Eintrag",
		"CK - C - Kurzer Eintrag",
		"HK - H - Kurzer Eintrag",
		"IK - I - Kurzer Eintrag",
		"KK - K - Aktuellster Eintrag",
		"OK - O - Kurzer Eintrag",
		"PK - P - Ein etwas längerer Eintrag",
		"SK - KS - Kurzer Eintrag",
		"TK - T - Kurzer Eintrag",
		"VK - V - Kurzer Eintrag",
		"ZK - Z - Kurzer Eintrag",
	];

	return {
		clazz,
		schuljahr,
		klassenart,
		lettersCount,
		letter,
		startsWithFilter,
		longNameFilter,
		selectionDisplayText,
		optionDisplayText,
		sort,
		unsorted,
		sortedAlphabetical,
		sortedByText,
	};
}
