import { nextTick, ref, type Ref } from "vue";
import { describe, test, expect, beforeAll, vi } from "vitest";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";
import type { Comparator } from "../../../../../../core/src/java/util/Comparator";
import type { SelectFilter } from "../../../../../src/ui/controls/select/filter/SelectFilter";
import { CoreTypeSelectManager } from "../../../../../src/ui/controls/select/manager/CoreTypeSelectManager";
import { JsonCoreTypeReaderStatic } from "../../../../../../core/src/asd/utils/JsonCoreTypeReaderStatic";
import { Klassenart } from "../../../../../../core/src/asd/types/klassen/Klassenart";
import { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import type { Class } from "../../../../../../core/src/java/lang/Class";
import type { KlassenartKatalogEintrag } from "../../../../../../core/src/asd/data/klassen/KlassenartKatalogEintrag";
import { LehrerZugangsgrund } from "../../../../../../core/src/asd/types/lehrer/LehrerZugangsgrund";

const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	reader.readAll();
	vi.mock("../../../../../../../svws-asd/src/main/resources/de/svws_nrw/asd/types/klassen/Klassenart.json", async () => ({
		default: (await import("./Klassenart.mock.json")).default,
	}));
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
			expect(manager.sort).toBeNull();
		});

		test('Neuer Manager mit clazz setzt leere Optionenliste.', () => {
			const { clazz } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz });

			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('Neuer Manager mit clazz und schuljahr setzt filteredOptions und unfilteredOptions.', () => {
			const { clazz, schuljahr, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			initialOrder.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und schulformen(einzelne) setzt leere filteredOptions und unfilteredOptions mit zulässiger Schulform.', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, schulformen: Schulform.GY });

			const entries = [
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
			];

			expect(manager.unfilteredOptions.size()).toBe(6);
			entries.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(6);
			entries.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und schulformen(mehrere) setzt leere filteredOptions und unfilteredOptions mit zulässigen Schulformen.', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, schulformen: [Schulform.GY, Schulform.H] });
			const entries = [
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
				"B - Kurzer Eintrag",
				"KS - Kurzer Eintrag",
				"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"P - Ein etwas längerer Eintrag",
			];

			expect(manager.unfilteredOptions.size()).toBe(10);
			entries.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(10);
			entries.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und sort setzt eine sortierte gefilterte und unsortierte ungefilterter Liste.', () => {
			const { clazz, schuljahr, sort, initialOrder, sorted } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, sort: sort });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			sorted.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('Neuer Manager mit clazz, schuljahr und filter erzeugt eine gefilterte Optionenliste.', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0).text).toBe("OK - Ein sehr laaanger Eintrag mit 45 Zeichen");
			expect(manager.filteredOptions.get(1).text).toBe("OJ - Ein längerer Eintrag mit 40 Zeichen");
		});

		test('Neuer Manager mit clazz, schuljahr, filter und sort erzeugt eine gefilterte und sortierte Optionenliste.', () => {
			const { clazz, schuljahr, sort, longNameFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, sort: sort, filters: [longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test("Neuer Manager mit clazz, schuljahr und selectionDisplayText(Funktion) hat Custom Text für die Selektion", () => {
			const { clazz, schuljahr, klassenart, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: selectionDisplayText });
			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
		});

		test("Neuer Manager mit clazz, schuljahr und selectionDisplayText('kuerzel') hat Kürzel als Text für die Selektion", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'kuerzel' });
			expect(manager.getSelectionText(klassenart)).toBe("OK");
		});

		test("Neuer Manager mit clazz, schuljahr und selectionDisplayText('text') hat Text als Text für die Selektion", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'text' });
			expect(manager.getSelectionText(klassenart)).toBe("O - Kurzer Eintrag");
		});

		test("Neuer Manager mit clazz, schuljahr und selectionDisplayText('kuerzelText') hat 'Kürzel - Text' als Text für die Selektion", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'kuerzelText' });
			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("Neuer Manager mit clazz, schuljahr und optionDisplayText(Funktion) hat Custom Text für die Optionen", () => {
			const { clazz, schuljahr, klassenart, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: optionDisplayText });
			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");
		});

		test("Neuer Manager mit clazz, schuljahr und optionDisplayText('kuerzel') hat Kürzel als Text für die Optionen", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'kuerzel' });
			expect(manager.getOptionText(klassenart)).toBe("OK");
		});

		test("Neuer Manager mit clazz, schuljahr und optionDisplayText('text') hat Text als Text für die Optionen", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'text' });
			expect(manager.getOptionText(klassenart)).toBe("O - Kurzer Eintrag");
		});

		test("Neuer Manager mit clazz, schuljahr und optionDisplayText('kuerzelText') hat 'Kürzel - Text' als Text für die Optionen", () => {
			const { clazz, schuljahr, klassenart } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'kuerzelText' });
			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
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
			const { clazz, schuljahr, initialOrder } = createTestData();
			const clazzRef: Ref<Class<Klassenart> | null> = ref(null);
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazzRef, schuljahr: schuljahr });


			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);

			clazzRef.value = clazz;
			await nextTick();

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
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
			const { clazz, schuljahr, startsWithFilter } = createTestData();
			const filterRef = ref<SelectFilter<KlassenartKatalogEintrag>[]>([]);
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: filterRef });

			expect(manager.filteredOptions.size()).toBe(manager.unfilteredOptions.size());

			filterRef.value = [startsWithFilter];
			await nextTick();

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test("Neues sort bei Änderung des Refs", async () => {
			const { clazz, schuljahr, sort, initialOrder, sorted } = createTestData();
			const sortRef = ref<((a: KlassenartKatalogEintrag, b: KlassenartKatalogEintrag) => number) | Comparator<KlassenartKatalogEintrag> | null>(null);
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, sort: sortRef });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			sortRef.value = { compare: sort };
			await nextTick();

			sorted.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

	});

	describe.concurrent("Funktionen", () => {

		test('get unfilteredOptions() mit Optionen und Filtern -> Gibt die ungefilterten Optionen zurück', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});

		test('get filteredOptions() mit Optionen und Filtern -> Gibt die gefilterten Optionen zurück', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);

			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test('updateFilteredOptions() -> aktualisiert alle Optionen', () => {
			const { clazz, schuljahr, letter, letters, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			letter.value = "K";
			letters.value = 5;
			manager.updateFilteredOptions();

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"Kein Eintrag",
				"KS - Kurzer Eintrag",
				"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test('updateFilteredOptions(longNameFilter) -> aktualisiert nur die Optionen des Filters', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			letters.value = 41;
			letter.value = "K";
			manager.updateFilteredOptions(longNameFilter);

			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0).text).toBe("OK - Ein sehr laaanger Eintrag mit 45 Zeichen");
		});


		test('updateFilteredOptions(longNameFilter, false) -> aktualisiert nur die Optionen des Filters', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			letters.value = 41;
			letter.value = "K";
			manager.updateFilteredOptions(longNameFilter, false);

			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0).text).toBe("OK - Ein sehr laaanger Eintrag mit 45 Zeichen");
		});


		test('updateFilteredOptions(longNameFilter, true) -> löscht den Filter und aktualisiert keine anderen Filter', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			letters.value = 41;
			letter.value = "K";
			manager.updateFilteredOptions(longNameFilter, true);
			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test('toList(List) -> gibt List unverändert zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
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
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
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
			const { clazz, schuljahr, startsWithFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.unfilteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test('addFilter(Filter) mit bereits existierenden Filter mit diesem Schlüssel -> überschreibt den Filter und aktualisiert die gefilterten Optionen', () => {
			const { clazz, schuljahr, startsWithFilter, letter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			letter.value = "K";
			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"Kein Eintrag",
				"KS - Kurzer Eintrag",
				"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});


		test('removeFilter(Filter) mit existierendem Filter -> entfernt existierenden Filter und aktualisiert die gefilterten Optionen', () => {
			const { clazz, schuljahr, startsWithFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});


		test('removeFilter(key) mit existierendem Filter -> entfernt existierenden Filter per Schlüssel und aktualisiert die gefilterten Optionen', () => {
			const { clazz, schuljahr, startsWithFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(3);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"O - Kurzer Eintrag",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
			].forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			manager.removeFilter("startsWithLetter");

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});


		test('removeFilter(Filter) ohne existierenden Filter -> entfernt nichts', () => {
			const { clazz, schuljahr, startsWithFilter, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.removeFilter(startsWithFilter);
			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});


		test('removeFilter(key) ohne existierenden Filter -> entfernt nichts', () => {
			const { clazz, schuljahr, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.removeFilter("startsWithFilter");

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});


		test('getFilterByKey(filterKey) und Filter existiert -> gibt Filter zurück', () => {
			const { clazz, schuljahr, startsWithFilter, longNameFilter } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, filters: [startsWithFilter, longNameFilter] });

			const result = manager.getFilterByKey("startsWithLetter");

			expect(result).toBe(startsWithFilter);
		});


		test('getFilterByKey(filterKey) und Filter existiert nicht -> gibt null zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			const result = manager.getFilterByKey("nonExistingFilter");

			expect(result).toBeNull();
		});

		test("get selectionDisplayText() -> gibt den Wert von selectionDisplayText zurück", () => {
			const manager = new CoreTypeSelectManager();

			expect(manager.selectionDisplayText).toBe("kuerzelText");
		});

		test("set selectionDisplayText() -> setzt den Wert von selectionDisplayText", () => {
			const { clazz, schuljahr, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });
			manager.selectionDisplayText = selectionDisplayText;

			expect(manager.selectionDisplayText).toBe(selectionDisplayText);
		});

		test('getSelectionText(null) -> gibt leeren String zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.getSelectionText(null);
			expect(result).toBe("");
		});

		test('getSelectionText(selection) ohne selectionDisplayText -> gibt das Kürzel mit Text der Selection zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test('getSelectionText(selection) mit Funktion als selectionDisplayText-> gibt den Text der Selection entsprechend der Funktion zurück', () => {
			const { klassenart, clazz, schuljahr, selectionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: selectionDisplayText });

			expect(manager.getSelectionText(klassenart)).toBe("sel:O - Kurzer Eintrag");
		});

		test('getSelectionText(selection) mit selectionDisplayText = text -> gibt den Text der Selection zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'text' });

			expect(manager.getSelectionText(klassenart)).toBe("O - Kurzer Eintrag");
		});

		test('getSelectionText(selection) mit selectionDisplayText = kuerzel -> gibt das Kürzel der Selection zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'kuerzel' });

			expect(manager.getSelectionText(klassenart)).toBe("OK");
		});

		test('getSelectionText(selection) mit selectionDisplayText = kuerzelText -> gibt das Kürzel mit Text der Selection zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, selectionDisplayText: 'kuerzelText' });

			expect(manager.getSelectionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("get optionDisplayText() -> gibt den Wert von optionDisplayText zurück", () => {
			const manager = new CoreTypeSelectManager();

			expect(manager.optionDisplayText).toBe("kuerzelText");
		});

		test("set optionDisplayText() -> setzt den Wert von optionDisplayText", () => {
			const { clazz, schuljahr, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });
			manager.optionDisplayText = optionDisplayText;

			expect(manager.optionDisplayText).toBe(optionDisplayText);
		});

		test('getOptionText(null) -> gibt leeren String zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.getOptionText(null);
			expect(result).toBe("");
		});

		test('getOptionText(option) ohne optionDisplayText -> gibt das Kürzel mit Text der Option zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test('getOptionText(option) mit Funktion als optionDisplayText-> gibt den Text der Option entsprechend der Funktion zurück', () => {
			const { klassenart, clazz, schuljahr, optionDisplayText } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: optionDisplayText });

			expect(manager.getOptionText(klassenart)).toBe("opt:O - Kurzer Eintrag");
		});

		test('getOptionText(option) mit optionDisplayText = text -> gibt den Text der Option zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'text' });

			expect(manager.getOptionText(klassenart)).toBe("O - Kurzer Eintrag");
		});

		test('getOptionText(option) mit optionDisplayText = kuerzel -> gibt das Kürzel der Option zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'kuerzel' });

			expect(manager.getOptionText(klassenart)).toBe("OK");
		});

		test('getOptionText(option) mit optionDisplayText = kuerzelText -> gibt das Kürzel mit Text der Option zurück', () => {
			const { klassenart, clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr, optionDisplayText: 'kuerzelText' });

			expect(manager.getOptionText(klassenart)).toBe("OK - O - Kurzer Eintrag");
		});

		test("get sort() -> gibt die aktuelle Sortierfunktion zurück", () => {
			const { clazz, schuljahr, sort } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, sort: sort });

			expect(manager.sort).not.toBeNull();

			const list = new ArrayList<KlassenartKatalogEintrag>();
			list.add(manager.unfilteredOptions.get(0));
			list.add(manager.unfilteredOptions.get(1));
			list.add(manager.unfilteredOptions.get(2));

			list.sort(manager.sort!);
			[
				"B - Kurzer Eintrag",
				"Kein Eintrag",
				"KS - Kurzer Eintrag",
			].forEach((element, index) => {
				expect(list.get(index).text).toBe(element);
			});
		});



		test("set sort() -> setzt die neue Sortierfunktion", () => {
			const { clazz, schuljahr, sort, initialOrder, sorted } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.sort = sort;

			expect(manager.filteredOptions.size()).toBe(13);
			sorted.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('toComparator(null) -> gibt null zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.toComparator(null);

			expect(result).toBeNull();
		});


		test('toComparator(undefined) -> gibt null zurück', () => {
			const manager = new CoreTypeSelectManager();
			const result = manager.toComparator(undefined);

			expect(result).toBeNull();
		});


		test('toComparator(sort Funktion) -> gibt Comparator Objekt mit compare zurück', () => {
			const { sort } = createTestData();
			const manager = new CoreTypeSelectManager();
			const result = manager.toComparator(sort);

			expect(result).not.toBeNull();
			expect(typeof result!.compare).toBe("function");
		});


		test('toComparator(Comparator Objekt) -> gibt das gleiche Comparator Objekt zurück', () => {
			const { sort } = createTestData();
			const comp: Comparator<KlassenartKatalogEintrag> = { compare: sort };
			const manager = new CoreTypeSelectManager();
			const result = manager.toComparator(comp);

			expect(result).toBe(comp);
		});


		test('updateSort() sort ist null -> sortiert die gefilterten Optionen nicht', () => {
			const { clazz, schuljahr, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});
		});


		test('updateSort() sort ist vorhanden -> sortiert die gefilterten Optionen', () => {
			const { clazz, schuljahr, sort, initialOrder, sorted } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
			});

			manager.sort = sort;
			manager.updateSort();

			sorted.forEach((element, index) => {
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('updateSort() filteredOptions leer -> es wird nichts sortiert', () => {
			const { sort } = createTestData();
			const manager = new CoreTypeSelectManager<KlassenartKatalogEintrag, Klassenart>();
			expect(manager.filteredOptions.size()).toBe(0);

			manager.sort = sort;
			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('get clazz() -> gibt die aktuell gesetzte Klasse zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.clazz).toBe(clazz);
		});

		test('set clazz(neuer Wert) -> setzt neue Klasse, aktualisiert Manager und ruft updateOptions auf', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.clazz = LehrerZugangsgrund.class;
			expect(manager.clazz).toBe(LehrerZugangsgrund.class);
			expect(manager.unfilteredOptions.size()).toBe(5);
			expect(manager.filteredOptions.size()).toBe(5);
		});

		test('set clazz(selber Wert) -> setzt weder eine neue Klasse, noch neue Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.clazz = clazz;
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);
		});

		test('set clazz(null) -> setzt die Klasse auf null und löscht alle Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.clazz = null;
			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('set clazz(undefined) -> setzt die Klasse auf undefined und löscht alle Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.clazz).toBe(clazz);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.clazz = undefined;
			expect(manager.clazz).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('get schuljahr() -> gibt das aktuell gesetzte Schuljahr zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });

			expect(manager.schuljahr).toBe(schuljahr);
		});

		test('set schuljahr(neuer Wert) -> setzt neues Schuljahr und aktualisiert die Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schuljahr = 2025;
			expect(manager.schuljahr).toBe(2025);
			expect(manager.unfilteredOptions.size()).toBe(14);
			expect(manager.filteredOptions.size()).toBe(14);
		});

		test('set schuljahr(null) -> setzt das Schuljahr auf null und löscht alle Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schuljahr = null;
			expect(manager.schuljahr).toBeNull();
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('set schuljahr(selber Wert) -> ändert weder das Schuljahr, noch die Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schuljahr = schuljahr;
			expect(manager.schuljahr).toBe(schuljahr);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);
		});

		test('get schulformen() -> gibt die aktuell gesetzten Schulformen zurück', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, schulformen: [Schulform.BK, Schulform.GY] });

			expect(manager.schulformen).not.toBeNull();
			// Checkt, ob Schulformen eine Liste ist
			expect(Symbol.iterator in new Object(manager.schulformen!)).toBe(true);
			expect((manager.schulformen as Schulform[]).length).toBe(2);
			expect((manager.schulformen as Schulform[])).toEqual([Schulform.BK, Schulform.GY]);
		});

		test('set schulformen(neuer Wert) -> setzt neue Schulformen und aktualisiert die Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr });
			expect(manager.schulformen).toBe(null);
			expect(manager.unfilteredOptions.size()).toBe(13);
			expect(manager.filteredOptions.size()).toBe(13);

			manager.schulformen = Schulform.GY;
			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(6);
			expect(manager.filteredOptions.size()).toBe(6);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
			].forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('set schulformen(null) -> setzt Schulformen auf null und aktualisiert die Optionen', () => {
			const { clazz, schuljahr, initialOrder } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, schulformen: Schulform.GY });
			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(6);
			expect(manager.filteredOptions.size()).toBe(6);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
			].forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			manager.schulformen = null;
			expect(manager.schulformen).toBeNull();
			expect(manager.filteredOptions.size()).toBe(13);
			initialOrder.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

		test('set schulformen(selber Wert) -> setzt weder neue Schulformen, noch neue Optionen', () => {
			const { clazz, schuljahr } = createTestData();
			const manager = new CoreTypeSelectManager({ clazz: clazz, schuljahr: schuljahr, schulformen: Schulform.GY });

			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(6);
			expect(manager.filteredOptions.size()).toBe(6);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
			].forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});

			// denselben Wert nochmal setzen → sollte return treffen, keine Änderung
			manager.schulformen = Schulform.GY;
			expect(manager.schulformen).toBe(Schulform.GY);
			expect(manager.unfilteredOptions.size()).toBe(6);
			expect(manager.filteredOptions.size()).toBe(6);
			[
				"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
				"OJ - Ein längerer Eintrag mit 40 Zeichen",
				"H - Kurzer Eintrag",
				"V - Kurzer Eintrag",
				"T - Kurzer Eintrag",
				"I - Kurzer Eintrag",
			].forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index).text).toBe(element);
				expect(manager.filteredOptions.get(index).text).toBe(element);
			});
		});

	});

});

/**
 * Erstellt Testdaten für die Tests des CoreTypeSelectManagers.
 *
 * @returns die testdaten: fruits, letters, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort
 */
function createTestData() {
	const clazz = Klassenart.class;
	const schuljahr = 2019;
	const klassenart = Klassenart.AM.daten(schuljahr);
	const letters = { value: 39 };
	const letter = { value: "O" };

	const startsWithFilter = {
		key: "startsWithLetter",
		apply: (options: List<KlassenartKatalogEintrag>) => {
			const filtered = new ArrayList<KlassenartKatalogEintrag>();
			for (const option of options) {
				if (option.text.startsWith(letter.value)) {
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
				if (option.text.length > letters.value) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const selectionDisplayText = (s: KlassenartKatalogEintrag) => `sel:${s.text}`;
	const optionDisplayText = (s: KlassenartKatalogEintrag) => `opt:${s.text}`;
	const sort = (a: KlassenartKatalogEintrag, b: KlassenartKatalogEintrag) => a.text.localeCompare(b.text);
	const initialOrder = [
		"Kein Eintrag",
		"B - Kurzer Eintrag",
		"KS - Kurzer Eintrag",
		"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
		"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
		"O - Kurzer Eintrag",
		"OJ - Ein längerer Eintrag mit 40 Zeichen",
		"H - Kurzer Eintrag",
		"P - Ein etwas längerer Eintrag",
		"Z - Kurzer Eintrag",
		"V - Kurzer Eintrag",
		"T - Kurzer Eintrag",
		"I - Kurzer Eintrag",
	];

	const sorted = [
		"B - Kurzer Eintrag",
		"H - Kurzer Eintrag",
		"I - Kurzer Eintrag",
		"K - Ein sehr langer Eintrag mit insgesamt 52 Zeichen",
		"Kein Eintrag",
		"KS - Kurzer Eintrag",
		"O - Kurzer Eintrag",
		"OJ - Ein längerer Eintrag mit 40 Zeichen",
		"OK - Ein sehr laaanger Eintrag mit 45 Zeichen",
		"P - Ein etwas längerer Eintrag",
		"T - Kurzer Eintrag",
		"V - Kurzer Eintrag",
		"Z - Kurzer Eintrag",
	];


	return { clazz, schuljahr, klassenart, letters, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort, initialOrder, sorted };
}
