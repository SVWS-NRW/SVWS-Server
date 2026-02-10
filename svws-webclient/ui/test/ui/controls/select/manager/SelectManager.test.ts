import { nextTick, ref } from "vue";
import { describe, test, expect } from "vitest";
import { SelectManager, type SelectManagerConfig } from "../../../../../src/ui/controls/select/manager/SelectManager";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";
import type { Comparator } from "../../../../../../core/src/java/util/Comparator";
import type { SelectFilter } from "../../../../../src/ui/controls/select/filter/SelectFilter";

describe("UiSelect SelectManager Tests", () => {

	describe("Konfigurationen", () => {
		test("Neuer Manager ohne config hat Default-Werte", () => {
			const manager = new SelectManager();

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
			expect(manager.sort).toBeNull();
		});

		test('Neuer Manager mit Optionen setzt gefilterte und ungefilterter Liste.', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);
		});

		test('Neuer Manager mit Optionen und Sort setzt eine sortierte gefilterte und unsortierte ungefilterter Liste.', () => {
			const { fruits, sort } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, sort: sort });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Apple");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
			expect(manager.filteredOptions.get(2)).toBe("Blueberry");
			expect(manager.filteredOptions.get(3)).toBe("Cherry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Mango");
			expect(manager.filteredOptions.get(6)).toBe("Melon");
		});

		test('Neuer Manager mit Optionen und Filtern erzeugt gefilterte Optionen.', () => {
			const { fruits, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
		});

		test('Neuer Manager mit Optionen, Filtern und Sortierung erzeugt gefilterte, sortierte Optionen.', () => {
			const { fruits, startsWithFilter, longNameFilter, sort } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter], sort: sort });
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Banana");
			expect(manager.filteredOptions.get(1)).toBe("Blueberry");
		});

		test("Neuer Manager mit selectionDisplayText hat Custom Text für die Selektion", () => {
			const { selectionDisplayText } = createTestData();
			const config: SelectManagerConfig<string> = { selectionDisplayText: selectionDisplayText };
			const manager = new SelectManager<string>(config);
			expect(manager.selectionDisplayText("y")).toBe("sel:y");
		});

		test("Neuer Manager mit optionDisplayText hat Custom Text für Optionen", () => {
			const { optionDisplayText } = createTestData();
			const config: SelectManagerConfig<string> = { optionDisplayText: optionDisplayText };
			const manager = new SelectManager<string>(config);
			expect(manager.optionDisplayText("y")).toBe("opt:y");
		});

		test("setConfig() -> es ändert sich nichts an den Konfigurationen", () => {
			const manager = new SelectManager<string>();

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");

			manager.setConfig();

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
		});

		test("setConfig({}, true) -> es werden die Defaultwerte für alle Displayfunktionen gesetzt", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const config: SelectManagerConfig<string> = { selectionDisplayText: selectionDisplayText, optionDisplayText: optionDisplayText };
			const manager = new SelectManager<string>(config);

			expect(manager.selectionDisplayText("foo")).toBe("sel:foo");
			expect(manager.optionDisplayText("bar")).toBe("opt:bar");

			manager.setConfig({}, true);

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
		});

		test("setConfig({}, false) -> es ändert sich nichts an den Konfigurationen", () => {
			const manager = new SelectManager<string>();

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");

			manager.setConfig({}, false);

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
		});

		test("setConfig({selectionDisplayText}, false) -> selectionDisplayText erhält einen neuen Wert", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const config: SelectManagerConfig<string> = { selectionDisplayText: selectionDisplayText, optionDisplayText: optionDisplayText };
			const manager = new SelectManager<string>(config);

			expect(manager.selectionDisplayText("foo")).toBe("sel:foo");
			expect(manager.optionDisplayText("bar")).toBe("opt:bar");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s}` }, false);

			expect(manager.selectionDisplayText("foo")).toBe("newSel:foo");
			expect(manager.optionDisplayText("bar")).toBe("opt:bar");
		});

		test("setConfig({selectionDisplayText}, true) -> selectionDisplayText erhält einen neuen Wert, optionDisplayText erhält Defaultwert", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const config: SelectManagerConfig<string> = { selectionDisplayText: selectionDisplayText, optionDisplayText: optionDisplayText };
			const manager = new SelectManager<string>(config);

			expect(manager.selectionDisplayText("foo")).toBe("sel:foo");
			expect(manager.optionDisplayText("bar")).toBe("opt:bar");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s}` }, true);

			expect(manager.selectionDisplayText("foo")).toBe("newSel:foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
		});

	});

	describe.concurrent("Reaktivität", () => {

		test("Neue options bei Änderung des Refs", async () => {
			const { fruits } = createTestData();
			const optRef = ref(fruits);
			const config: SelectManagerConfig<string> = { options: optRef };
			const manager = new SelectManager<string>(config);

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			optRef.value.push("Papaya");

			await nextTick();

			expect(manager.unfilteredOptions.size()).toBe(8);
			expect(manager.filteredOptions.size()).toBe(8);

			expect(manager.unfilteredOptions.getLast()).toBe("Papaya");
			expect(manager.filteredOptions.getLast()).toBe("Papaya");

		});

		test("Neue filters bei Änderung des Refs", async () => {
			const { fruits, startsWithFilter } = createTestData();
			const filterRef = ref<SelectFilter<string>[]>([]);
			const config: SelectManagerConfig<string> = { options: fruits, filters: filterRef };
			const manager = new SelectManager<string>(config);

			expect(manager.filteredOptions.size()).toBe(7);

			filterRef.value = [startsWithFilter];

			await nextTick();

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
		});


		test("Neues sort bei Änderung des Refs", async () => {
			const { fruits, sort } = createTestData();
			const sortRef = ref<((a: string, b: string) => number) | null>(null);
			const config: SelectManagerConfig<string> = { options: fruits, sort: sortRef };
			const manager = new SelectManager<string>(config);

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");

			sortRef.value = sort;

			await nextTick();

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Apple");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
			expect(manager.filteredOptions.get(2)).toBe("Blueberry");
			expect(manager.filteredOptions.get(3)).toBe("Cherry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Mango");
			expect(manager.filteredOptions.get(6)).toBe("Melon");
		});

		test("Neues selectionDisplayText bei Änderung des Refs", async () => {
			const { selectionDisplayText } = createTestData();
			const selRef = ref<(s: string) => string>(selectionDisplayText);
			const config: SelectManagerConfig<string> = { selectionDisplayText: selRef };
			const manager = new SelectManager<string>(config);

			expect(manager.selectionDisplayText("b")).toBe("sel:b");

			selRef.value = (s) => `newSel:${s}`;

			await nextTick();

			expect(manager.selectionDisplayText("b")).toBe("newSel:b");
		});

		test("Neues optionDisplayText bei Änderung des Refs", async () => {
			const { optionDisplayText } = createTestData();
			const optRef = ref<(s: string) => string>(optionDisplayText);
			const config: SelectManagerConfig<string> = { optionDisplayText: optRef };
			const manager = new SelectManager<string>(config);

			expect(manager.optionDisplayText("b")).toBe("opt:b");

			optRef.value = (s) => `newOpt:${s}`;

			await nextTick();

			expect(manager.optionDisplayText("b")).toBe("newOpt:b");
		});
	});

	describe.concurrent("Funktionen", () => {

		test('get unfilteredOptions() mit Optionen und Filtern -> Gibt die ungefilterten Optionen zurück', () => {
			const { fruits, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");
		});

		test('set unfilteredOptions() mit Optionen -> Setzt die gefilterten Optionen', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");

			manager.unfilteredOptions = ["A", "B", "C"];

			expect(manager.filteredOptions.size()).toBe(3);
			expect(manager.filteredOptions.get(0)).toBe("A");
			expect(manager.filteredOptions.get(1)).toBe("B");
			expect(manager.filteredOptions.get(2)).toBe("C");
		});

		test('get filteredOptions() mit Optionen und Filtern -> Gibt die gefilterten Optionen zurück', () => {
			const { fruits, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
		});

		test('updateFilteredOptions() -> aktualisiert alle Optionen', () => {
			const { fruits, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			letters.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions();
			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0)).toBe("Clementine");
		});

		test('updateFilteredOptions(startsWithFilter) -> aktualisiert nur die Optionen des Filters', () => {
			const { fruits, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			letters.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter);
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Clementine");
			expect(manager.filteredOptions.get(1)).toBe("Cherry");
		});

		test('updateFilteredOptions(startsWithFilter, false) -> aktualisiert nur die Optionen des Filters', () => {
			const { fruits, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			letters.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter, false);
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Clementine");
			expect(manager.filteredOptions.get(1)).toBe("Cherry");
		});

		test('updateFilteredOptions(startsWithFilter, true) -> löscht den Filter und aktualisiert keine anderen Filter', () => {
			const { fruits, startsWithFilter, longNameFilter, letter, letters } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			letters.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter, true);
			expect(manager.filteredOptions.size()).toBe(4);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Clementine");
			expect(manager.filteredOptions.get(2)).toBe("Cherry");
			expect(manager.filteredOptions.get(3)).toBe("Banana");
		});

		test('toList(List) -> gibt List unverändert zurück', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>();
			const arrayList = new ArrayList<string>();
			for (const f of fruits) {
				arrayList.add(f);
			}

			const result = manager.toList(arrayList);

			expect(result).toBe(arrayList);
			expect(result.size()).toBe(7);

			for (let i = 0; i < fruits.length; i++) {
				expect(result.get(i)).toBe(fruits[i]);
			}
		});

		test('toList(Array) -> konvertiert das Array zu ArrayList', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>();

			const result = manager.toList(fruits);

			expect(result).toBeInstanceOf(ArrayList);
			expect(result.size()).toBe(7);

			for (let i = 0; i < fruits.length; i++) {
				expect(result.get(i)).toBe(fruits[i]);
			}
		});

		test('toList(null|undefined) ->  gibt leere ArrayList zurück', () => {
			const manager = new SelectManager<string>();

			const resultNull = manager.toList(null);

			expect(resultNull).toBeInstanceOf(ArrayList);
			expect(resultNull.size()).toBe(0);

			const resultUndefined = manager.toList(undefined);

			expect(resultUndefined).toBeInstanceOf(ArrayList);
			expect(resultUndefined.size()).toBe(0);
		});

		test('addFilter(Filter) ohne bereits existierenden Filter mit diesem Schlüssel -> fügt neuen Filter hinzu und aktualisiert die gefilterten Optionen,', () => {
			const { fruits, startsWithFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.filteredOptions.size()).toBe(7);

			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
		});

		test('addFilter(Filter) mit bereits existierenden Filter mit diesem Schlüssel -> überschreibt den Filter und aktualisiert die gefilterten Optionen,', () => {
			const { fruits, startsWithFilter, letter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			letter.value = "A";
			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0)).toBe("Apple");
		});

		test('removeFilter(Filter) mit existierendem Filter -> entfernt existierenden Filter und aktualisiert die gefilterten Optionen', () => {
			const { fruits, startsWithFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(7);
			for (let i = 0; i < fruits.length; i++) {
				expect(manager.filteredOptions.get(i)).toBe(fruits[i]);
			}
		});

		test('removeFilter(key) mit existierendem Filter -> entfernt existierenden Filter per Schlüssel und aktualisiert die gefilterten Optionen', () => {
			const { fruits, startsWithFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe("Blueberry");
			expect(manager.filteredOptions.get(1)).toBe("Banana");

			manager.removeFilter("startsWithLetter");

			expect(manager.filteredOptions.size()).toBe(7);
			for (let i = 0; i < fruits.length; i++) {
				expect(manager.filteredOptions.get(i)).toBe(fruits[i]);
			}
		});

		test('removeFilter(Filter) ohne existierenden Filter -> entfernt nichts', () => {
			const { fruits, startsWithFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.filteredOptions.size()).toBe(7);

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(7);
			for (let i = 0; i < fruits.length; i++) {
				expect(manager.filteredOptions.get(i)).toBe(fruits[i]);
			}
		});

		test('removeFilter(key) ohne existierenden Filter -> entfernt nichts', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.filteredOptions.size()).toBe(7);

			manager.removeFilter("startsWithFilter");

			expect(manager.filteredOptions.size()).toBe(7);
			for (let i = 0; i < fruits.length; i++) {
				expect(manager.filteredOptions.get(i)).toBe(fruits[i]);
			}
		});

		test('getFilterByKey(filterKey) und Filter existiert -> gibt Filter zurück', () => {
			const { fruits, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, filters: [startsWithFilter, longNameFilter] });

			const result = manager.getFilterByKey("startsWithLetter");

			expect(result).toBe(startsWithFilter);
		});

		test('getFilterByKey(filterKey) und Filter existiert nicht -> gibt null zurück', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			const result = manager.getFilterByKey("nonExistingFilter");

			expect(result).toBeNull();
		});

		test("get selectionDisplayText() -> gibt den Wert von selectionDisplayText", () => {
			const manager = new SelectManager<string>();
			expect(manager.selectionDisplayText("foo")).toBe("foo");
		});

		test("set selectionDisplayText() -> setzt den Wert von selectionDisplayText", () => {
			const { selectionDisplayText } = createTestData();
			const manager = new SelectManager<string>();
			manager.selectionDisplayText = selectionDisplayText;

			expect(manager.selectionDisplayText("foo")).toBe("sel:foo");
		});

		test('getSelectionText(null) -> gibt leeren String zurück', () => {
			const manager = new SelectManager<string>();

			const result = manager.getSelectionText(null);

			expect(result).toBe("");
		});

		test('getSelectionText(option) -> gibt den Text der Option zurück', () => {
			const { fruits, selectionDisplayText } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, selectionDisplayText: selectionDisplayText });

			const result = manager.getSelectionText("Apple");

			expect(result).toBe("sel:Apple");
		});

		test("get optionDisplayText() -> gibt den Wert von optionDisplayText", () => {
			const manager = new SelectManager<string>();

			expect(manager.optionDisplayText("foo")).toBe("foo");
		});

		test("set optionDisplayText() -> setzt den Wert von optionDisplayText", () => {
			const { optionDisplayText } = createTestData();
			const manager = new SelectManager<string>();
			manager.selectionDisplayText = optionDisplayText;

			expect(manager.selectionDisplayText("foo")).toBe("opt:foo");
		});

		test('getOptionText(null) -> gibt leeren String zurück', () => {
			const manager = new SelectManager<string>();
			const result = manager.getOptionText(null);

			expect(result).toBe("");
		});

		test('getOptionText(option) -> gibt den Text der Option zurück', () => {
			const { fruits, optionDisplayText } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, optionDisplayText: optionDisplayText });

			const result = manager.getOptionText("Apple");

			expect(result).toBe("opt:Apple");
		});

		test("get sort() -> gibt die aktuelle Sortierfunktion zurück", () => {
			const { fruits, sort } = createTestData();
			const manager = new SelectManager<string>({ options: fruits, sort: sort });

			expect(manager.sort).not.toBeNull();

			const letters = new ArrayList();
			letters.add("C");
			letters.add("B");
			letters.add("A");
			letters.sort(manager.sort!);

			expect(letters.get(0)).toBe("A");
			expect(letters.get(1)).toBe("B");
			expect(letters.get(2)).toBe("C");
		});

		test("set sort() -> setzt die neue Sortierfunktion", () => {
			const { fruits, sort } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Mango");
			expect(manager.filteredOptions.get(1)).toBe("Apple");
			expect(manager.filteredOptions.get(2)).toBe("Melon");
			expect(manager.filteredOptions.get(3)).toBe("Blueberry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Cherry");
			expect(manager.filteredOptions.get(6)).toBe("Banana");

			manager.sort = sort;

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Apple");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
			expect(manager.filteredOptions.get(2)).toBe("Blueberry");
			expect(manager.filteredOptions.get(3)).toBe("Cherry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Mango");
			expect(manager.filteredOptions.get(6)).toBe("Melon");

			// unfilteredOptions bleibt unverändert
			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");
		});

		test('toComparator(null) -> gibt null zurück', () => {
			const manager = new SelectManager<string>();
			const result = manager.toComparator(null);

			expect(result).toBeNull();
		});

		test('toComparator(undefined) -> gibt null zurück', () => {
			const manager = new SelectManager<string>();
			const result = manager.toComparator(undefined);

			expect(result).toBeNull();
		});

		test('toComparator(sort Funktion) -> gibt Comparator Objekt mit compare zurück', () => {
			const { sort } = createTestData();
			const manager = new SelectManager<string>();
			const result = manager.toComparator(sort);

			expect(result).not.toBeNull();
			expect(typeof result!.compare).toBe("function");
		});

		test('toComparator(Comparator Objekt) -> gibt das gleiche Comparator Objekt zurück', () => {
			const { sort } = createTestData();
			const comp: Comparator<string> = { compare: sort };
			const manager = new SelectManager<string>();
			const result = manager.toComparator(comp);

			expect(result).toBe(comp);
		});

		test('updateSort() sort ist null -> sortiert die gefilterten Optionen nicht', () => {
			const { fruits } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(7);

			for (let i = 0; i < fruits.length; i++) {
				expect(manager.filteredOptions.get(i)).toBe(fruits[i]);
			}
		});

		test('updateSort() sort ist vorhanden -> sortiert die gefilterten Optionen', () => {
			const { fruits, sort } = createTestData();
			const manager = new SelectManager<string>({ options: fruits });

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Mango");
			expect(manager.filteredOptions.get(1)).toBe("Apple");
			expect(manager.filteredOptions.get(2)).toBe("Melon");
			expect(manager.filteredOptions.get(3)).toBe("Blueberry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Cherry");
			expect(manager.filteredOptions.get(6)).toBe("Banana");

			manager.sort = sort;

			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.get(0)).toBe("Apple");
			expect(manager.filteredOptions.get(1)).toBe("Banana");
			expect(manager.filteredOptions.get(2)).toBe("Blueberry");
			expect(manager.filteredOptions.get(3)).toBe("Cherry");
			expect(manager.filteredOptions.get(4)).toBe("Clementine");
			expect(manager.filteredOptions.get(5)).toBe("Mango");
			expect(manager.filteredOptions.get(6)).toBe("Melon");


			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.unfilteredOptions.get(0)).toBe("Mango");
			expect(manager.unfilteredOptions.get(1)).toBe("Apple");
			expect(manager.unfilteredOptions.get(2)).toBe("Melon");
			expect(manager.unfilteredOptions.get(3)).toBe("Blueberry");
			expect(manager.unfilteredOptions.get(4)).toBe("Clementine");
			expect(manager.unfilteredOptions.get(5)).toBe("Cherry");
			expect(manager.unfilteredOptions.get(6)).toBe("Banana");
		});

		test('updateSort() filteredOptions leer -> es wird nichts sortiert', () => {
			const { sort } = createTestData();
			const manager = new SelectManager<string>();
			manager.sort = sort;
			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(0);
		});

	});

});

/**
 * Erstellt Testdaten für die Tests des SelectManagers.
 *
 * @returns die testdaten: fruits, letters, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort
 */
function createTestData() {
	const fruits = ["Mango", "Apple", "Melon", "Blueberry", "Clementine", "Cherry", "Banana"];
	const letters = { value: 5 };
	const letter = { value: "B" };

	const startsWithFilter = {
		key: "startsWithLetter",
		apply: (options: List<string>) => {
			const filtered = new ArrayList<string>();
			for (const option of options) {
				if (option.startsWith(letter.value)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const longNameFilter = {
		key: "longName",
		apply: (options: List<string>) => {
			const filtered = new ArrayList<string>();
			for (const option of options) {
				if (option.length > letters.value) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const selectionDisplayText = (s: string) => `sel:${s}`;
	const optionDisplayText = (s: string) => `opt:${s}`;
	const sort = (a: string, b: string) => a.localeCompare(b);


	return { fruits, letters, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort };
}
