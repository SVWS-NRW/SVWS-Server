import { describe, expect, test } from "vitest";
import {nextTick, ref, toValue} from "vue";

import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";
import type { SelectFilter } from "@ui/ui/controls/select/filter/SelectFilter";
import type { SelectSort } from "@ui/ui/controls/select/manager/BaseSelectManager";
import type { SelectManagerConfig } from "@ui/ui/controls/select/manager/SelectManager";
import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

describe("UiSelect SelectManager Tests", () => {

	describe("Konfigurationen", () => {
		test("Neuer Manager ohne config hat Default-Werte", () => {
			const manager = new SelectManager();

			expect(manager.selectionDisplayText("foo")).toBe("foo");
			expect(manager.optionDisplayText("bar")).toBe("bar");
			expect(manager.unfilteredOptions.size()).toBe(0);
			expect(manager.filteredOptions.size()).toBe(0);
		});

		test('Neuer Manager mit Optionen setzt gefilterte und ungefilterter Liste.', () => {
			const { fruitsByName, unsorted, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});
			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});


		test('Neuer Manager mit Optionen und sort = undefined -> sortiert nach der Default Prop "sortierung"', () => {
			const { fruitsByName, unsorted, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('Neuer Manager mit Optionen und sort = "sortierungProp" -> sortiert nach der Prop "sortierung"', () => {
			const { fruitsByName, unsorted, sortedBySortierung } = createTestData();
			const manager = new SelectManager({ options: Object.values(fruitsByName), sort: "sortierungProp" });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('Neuer Manager mit Optionen und sort = "idProp" -> sortiert die Optionen nach ID', () => {
			const { fruitsByName, unsorted, sortedById } = createTestData();
			const manager = new SelectManager({ options: Object.values(fruitsByName), sort: "idProp" });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			sortedById.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('Neuer Manager mit mit Optionen, optionText und sort = "alphabetical" -> sortiert die Optionen alphabetisch nach optionText', () => {
			const { fruitsByName, unsorted, sortedAlphabetical, optionDisplayText } = createTestData();
			const manager = new SelectManager({ options: Object.values(fruitsByName), sort: "alphabetical", optionDisplayText: optionDisplayText });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			sortedAlphabetical.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('Neuer Manager mit clazz und sort = "unsorted" -> behält die ursprüngliche Reihenfolge bei', () => {
			const { fruitsByName, unsorted } = createTestData();
			const manager = new SelectManager({ options: Object.values(fruitsByName), sort: "unsorted" });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test("Neuer Manager mit sort als eigene Funktion -> sortiert nach dem eigenen Comparator", () => {
			const { fruitsByName, sort, unsorted, sortedByCustom } = createTestData();
			const manager = new SelectManager({ options: Object.values(fruitsByName), sort });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(7);
			sortedByCustom.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});


		test('Neuer Manager mit Optionen und Filtern erzeugt gefilterte Optionen.', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, unsorted } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);
		});

		test('Neuer Manager mit Optionen, Filtern und Sortierung erzeugt gefilterte, sortierte Optionen.', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, sort, unsorted } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter], sort: sort });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Blueberry);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Banana);
		});

		test("Neuer Manager mit selectionDisplayText hat Custom Text für die Selektion", () => {
			const { selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ selectionDisplayText });
			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
		});

		test("Neuer Manager mit optionDisplayText hat Custom Text für Optionen", () => {
			const { optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test("setConfig() -> es ändert sich nichts an den Konfigurationen", () => {
			const { optionDisplayText, selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ optionDisplayText, selectionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			manager.setConfig();

			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test("setConfig({}, true) -> es werden die Defaultwerte für alle Displayfunktionen gesetzt", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ selectionDisplayText, optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			manager.setConfig({}, true);

			expect(manager.selectionDisplayText(fruit)).toBe("[object Object]");
			expect(manager.optionDisplayText(fruit)).toBe("[object Object]");
		});

		test("setConfig({}, false) -> es ändert sich nichts an den Konfigurationen", () => {
			const { optionDisplayText, selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ optionDisplayText, selectionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			manager.setConfig({}, false);

			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test("setConfig({selectionDisplayText}, false) -> selectionDisplayText erhält einen neuen Wert", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ selectionDisplayText, optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s.name}` }, false);

			expect(manager.selectionDisplayText(fruit)).toBe("newSel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test("setConfig({selectionDisplayText}, true) -> selectionDisplayText erhält einen neuen Wert, optionDisplayText erhält Defaultwert", () => {
			const { selectionDisplayText, optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ selectionDisplayText, optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			manager.setConfig({ selectionDisplayText: (s) => `newSel:${s.name}` }, true);

			expect(manager.selectionDisplayText(fruit)).toBe("newSel:Fruit");
			expect(manager.optionDisplayText(fruit)).toBe("[object Object]");
		});

	});

	describe.concurrent("Reaktivität", () => {

		test("Neue options bei Änderung des Refs", async () => {
			const { fruitsByName } = createTestData();
			const optRef = ref(Object.values(fruitsByName));
			const manager = new SelectManager<Fruit>({ options: optRef });

			expect(manager.unfilteredOptions.size()).toBe(7);
			expect(manager.filteredOptions.size()).toBe(7);

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			optRef.value.push(fruit);

			await nextTick();

			expect(manager.unfilteredOptions.size()).toBe(8);
			expect(manager.filteredOptions.size()).toBe(8);

			expect(manager.unfilteredOptions.getLast()).toBe(fruit);
			expect(manager.filteredOptions.getFirst()).toBe(fruit);

		});

		test("Neue filters bei Änderung des Refs", async () => {
			const { fruitsByName, startsWithFilter } = createTestData();
			const filterRef = ref<SelectFilter<Fruit>[]>([]);
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: filterRef });

			expect(manager.filteredOptions.size()).toBe(7);

			filterRef.value = [startsWithFilter];

			await nextTick();

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);
		});


		test("Neues sort bei Änderung des Refs", async () => {
			const { fruitsByName, sort, unsorted, sortedByCustom } = createTestData();
			const sortRef = ref<SelectSort<Fruit>>();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), sort: sortRef });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			sortRef.value = sort;

			await nextTick();

			expect(manager.filteredOptions.size()).toBe(7);
			sortedByCustom.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test("Neues selectionDisplayText bei Änderung des Refs", async () => {
			const { selectionDisplayText } = createTestData();
			const selRef = ref<(f: Fruit) => string>(selectionDisplayText);
			const manager = new SelectManager<Fruit>({ selectionDisplayText: selRef });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");

			selRef.value = (s) => `newSel:${s.name}`;

			await nextTick();

			expect(manager.selectionDisplayText(fruit)).toBe("newSel:Fruit");
		});

		test("Neues optionDisplayText bei Änderung des Refs", async () => {
			const { optionDisplayText } = createTestData();
			const optRef = ref<(f: Fruit) => string>(optionDisplayText);
			const config: SelectManagerConfig<Fruit> = { optionDisplayText: optRef };
			const manager = new SelectManager<Fruit>(config);

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");

			optRef.value = (s) => `newOpt:${s.name}`;

			await nextTick();

			expect(manager.optionDisplayText(fruit)).toBe("newOpt:Fruit");
		});
	});

	describe.concurrent("Funktionen", () => {

		test('get unfilteredOptions() mit Optionen und Filtern -> Gibt die ungefilterten Optionen zurück', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, unsorted } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});
		});

		test('set unfilteredOptions() mit Optionen -> Setzt die gefilterten Optionen', () => {
			const { fruitsByName, unsorted } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});

			const fruit1 = { id: 0, sortierung: 0, name: "Fruit1" };
			const fruit2 = { id: 1, sortierung: 1, name: "Fruit2" };
			manager.unfilteredOptions = [fruit1, fruit2];

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruit1);
			expect(manager.filteredOptions.get(1)).toBe(fruit2);
		});

		test('get filteredOptions() mit Optionen und Filtern -> Gibt die gefilterten Optionen zurück', () => {
			const { fruitsByName, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);
		});

		test('updateFilteredOptions() -> aktualisiert alle Optionen', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, letter, letterCount } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			letterCount.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions();
			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Clementine);
		});

		test('updateFilteredOptions(startsWithFilter) -> aktualisiert nur die Optionen des Filters', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, letter, letterCount } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			letterCount.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter);
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Clementine);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Cherry);
		});

		test('updateFilteredOptions(startsWithFilter, false) -> aktualisiert nur die Optionen des Filters', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, letter, letterCount } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			letterCount.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter, false);
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Clementine);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Cherry);
		});

		test('updateFilteredOptions(startsWithFilter, true) -> löscht den Filter und aktualisiert keine anderen Filter', () => {
			const { fruitsByName, startsWithFilter, longNameFilter, letter, letterCount } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });
			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			letterCount.value = 7;
			letter.value = "C";
			manager.updateFilteredOptions(startsWithFilter, true);
			expect(manager.filteredOptions.size()).toBe(4);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Clementine);
			expect(manager.filteredOptions.get(2)).toBe(fruitsByName.Blueberry);
			expect(manager.filteredOptions.get(3)).toBe(fruitsByName.Cherry);
		});

		test('toList(List) -> gibt List unverändert zurück', () => {
			const { fruitsByName } = createTestData();
			const manager = new SelectManager<Fruit>();
			const fruits = Object.values(fruitsByName);
			const arrayList = new ArrayList<Fruit>();

			for (const fruit of fruits) {
				arrayList.add(fruit);
			}

			const result = manager.toList(arrayList);

			expect(result).toBe(arrayList);
			expect(result.size()).toBe(7);

			for (let i = 0; i < fruits.length; i++) {
				expect(result.get(i)).toBe(fruits[i]);
			}
		});

		test('toList(Array) -> konvertiert das Array zu ArrayList', () => {
			const { fruitsByName } = createTestData();
			const manager = new SelectManager<Fruit>();
			const fruits = Object.values(fruitsByName);

			const result = manager.toList(fruits);

			expect(result).toBeInstanceOf(ArrayList);
			expect(result.size()).toBe(7);

			for (let i = 0; i < fruits.length; i++) {
				expect(result.get(i)).toBe(fruits[i]);
			}
		});

		test('toList(null|undefined) ->  gibt leere ArrayList zurück', () => {
			const manager = new SelectManager<Fruit>();

			const resultNull = manager.toList(null);

			expect(resultNull).toBeInstanceOf(ArrayList);
			expect(resultNull.size()).toBe(0);

			const resultUndefined = manager.toList(undefined);

			expect(resultUndefined).toBeInstanceOf(ArrayList);
			expect(resultUndefined.size()).toBe(0);
		});

		test('addFilter(Filter) ohne bereits existierenden Filter mit diesem Schlüssel -> fügt neuen Filter hinzu und aktualisiert die gefilterten Optionen,', () => {
			const { fruitsByName, startsWithFilter, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);
		});

		test('addFilter(Filter) mit bereits existierenden Filter mit diesem Schlüssel -> überschreibt den Filter und aktualisiert die gefilterten Optionen,', () => {
			const { fruitsByName, startsWithFilter, letter } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			letter.value = "A";
			manager.addFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(1);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Apple);
		});

		test('removeFilter(Filter) mit existierendem Filter -> entfernt existierenden Filter und aktualisiert die gefilterten Optionen', () => {
			const { fruitsByName, startsWithFilter, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('removeFilter(key) mit existierendem Filter -> entfernt existierenden Filter per Schlüssel und aktualisiert die gefilterten Optionen', () => {
			const { fruitsByName, startsWithFilter, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter] });

			expect(manager.filteredOptions.size()).toBe(2);
			expect(manager.filteredOptions.get(0)).toBe(fruitsByName.Banana);
			expect(manager.filteredOptions.get(1)).toBe(fruitsByName.Blueberry);

			manager.removeFilter("startsWithLetter");

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('removeFilter(Filter) ohne existierenden Filter -> entfernt nichts', () => {
			const { fruitsByName, startsWithFilter, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			manager.removeFilter(startsWithFilter);

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('removeFilter(key) ohne existierenden Filter -> entfernt nichts', () => {
			const { fruitsByName, sortedBySortierung } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			manager.removeFilter("startsWithFilter");

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('getFilterByKey(filterKey) und Filter existiert -> gibt Filter zurück', () => {
			const { fruitsByName, startsWithFilter, longNameFilter } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), filters: [startsWithFilter, longNameFilter] });

			const result = manager.getFilterByKey("startsWithLetter");

			expect(result).toBe(startsWithFilter);
		});

		test('getFilterByKey(filterKey) und Filter existiert nicht -> gibt null zurück', () => {
			const { fruitsByName } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName) });

			const result = manager.getFilterByKey("nonExistingFilter");

			expect(result).toBeNull();
		});

		test("get selectionDisplayText() -> gibt den Wert von selectionDisplayText", () => {
			const { selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ selectionDisplayText });
			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
		});

		test("set selectionDisplayText() -> setzt den Wert von selectionDisplayText", () => {
			const { selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>();
			manager.selectionDisplayText = selectionDisplayText;

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.selectionDisplayText(fruit)).toBe("sel:Fruit");
		});

		test('getSelectionText(null) -> gibt leeren String zurück', () => {
			const manager = new SelectManager<Fruit>();

			const result = manager.getSelectionText(null);

			expect(result).toBe("");
		});

		test('getSelectionText(option) -> gibt den Text der Option zurück', () => {
			const { fruitsByName, selectionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), selectionDisplayText: selectionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			const result = manager.getSelectionText(fruit);

			expect(result).toBe("sel:Fruit");
		});

		test("get optionDisplayText() -> gibt den Wert von optionDisplayText", () => {
			const { optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test("set optionDisplayText() -> setzt den Wert von optionDisplayText", () => {
			const { optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>();
			manager.optionDisplayText = optionDisplayText;

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			expect(manager.optionDisplayText(fruit)).toBe("opt:Fruit");
		});

		test('getOptionText(null) -> gibt leeren String zurück', () => {
			const manager = new SelectManager<Fruit>();
			const result = manager.getOptionText(null);

			expect(result).toBe("");
		});

		test('getOptionText(option) -> gibt den Text der Option zurück', () => {
			const { fruitsByName, optionDisplayText } = createTestData();
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), optionDisplayText: optionDisplayText });

			const fruit = { id: 0, sortierung: 0, name: "Fruit" };
			const result = manager.getOptionText(fruit);

			expect(result).toBe("opt:Fruit");
		});

		test('getComparator() mit sort = "unsorted" -> gibt null zurück', () => {
			const manager = new SelectManager<Fruit>({ sort: "unsorted" });
			const result = manager.getComparator();

			expect(result).toBeNull();
		});

		test('getComparator() mit sort = funktion -> gibt Comparator Objekt mit compare zurück', () => {
			const { sort } = createTestData();
			const manager = new SelectManager<Fruit>({ sort });
			const result = manager.getComparator();

			expect(result).not.toBeNull();
			expect(typeof result!.compare).toBe("function");
		});


		test('getComparator() mit Comparator Objekt -> gibt das gleiche Comparator Objekt zurück', () => {
			const { sort } = createTestData();
			const comp: Comparator<Fruit> = { compare: sort };
			const manager = new SelectManager<Fruit>({ sort: comp });
			const result = manager.getComparator();

			expect(result).toBe(comp);
		});

		test.each(["sortierungProp", "idProp", "alphabetical", null, undefined] as const)(
			'getComparator() mit sort = "%s" -> gibt einen Comparator zurück',
			(sort) => {
				const manager = new SelectManager<Fruit>({ sort });

				const result = manager.getComparator();

				expect(result).not.toBeNull();
				expect(typeof result?.compare).toBe("function");
			}
		);

		// Die Tests laufen einmal für die Prop "id" (sort = "idProp") und einmal für die Prop "sortierung" (sort = "sortierungProp")
		describe.each([["id", "idProp"], ["sortierung", "sortierungProp"]] as const)("Sortierung nach der Prop %s (sort = %s)", (prop, sort) => {
			type Eintrag = { id?: unknown; sortierung?: unknown; name: string };

			// Erzeugt eine Option, bei der die zu testende Prop (id bzw. sortierung) den übergebenen Wert hat.
			const withProp = (name: string, value: unknown): Eintrag => ({ name, [prop]: value });
			// Erzeugt eine Option, die die zu testende Prop (id bzw. sortierung) gar nicht besitzt.
			const withoutProp = (name: string): Eintrag => ({ name });

			const optionDisplayText = (e: Eintrag) => e.name;
			const erzeugeManager = (options: Eintrag[]) => new SelectManager<Eintrag>({ options, sort, optionDisplayText });
			// Gibt die Namen der gefilterten und sortierten Optionen des Managers in ihrer Reihenfolge zurück.
			const namen = (manager: SelectManager<Eintrag>) => [...manager.filteredOptions].map(e => e.name);

			test('number-Werte -> werden numerisch sortiert', () => {
				const manager = erzeugeManager([withProp("Anton", 10), withProp("Berta", 2), withProp("Cäsar", 100)]);

				expect(namen(manager)).toEqual(["Berta", "Anton", "Cäsar"]);
			});

			test('negative number-Werte -> werden numerisch sortiert', () => {
				const manager = erzeugeManager([withProp("Anton", -1), withProp("Berta", -10), withProp("Cäsar", 3), withProp("Dora", -2)]);

				expect(namen(manager)).toEqual(["Berta", "Dora", "Anton", "Cäsar"]);
			});

			test('Dezimalzahlen -> werden numerisch sortiert', () => {
				const manager = erzeugeManager([withProp("Anton", 1.5), withProp("Berta", 1.25), withProp("Cäsar", 1.1)]);

				expect(namen(manager)).toEqual(["Cäsar", "Berta", "Anton"]);
			});

			test('string-Werte -> werden alphabetisch nach dem Wert und nicht nach dem Anzeigetext sortiert', () => {
				const manager = erzeugeManager([withProp("Anton", "c"), withProp("Berta", "a"), withProp("Cäsar", "b")]);

				expect(namen(manager)).toEqual(["Berta", "Cäsar", "Anton"]);
			});

			test('Option ohne die Prop -> steht vorn, danach folgen die Optionen mit Wert', () => {
				const manager = erzeugeManager([withProp("Cäsar", 1), withoutProp("Zeta"), withProp("Berta", 2)]);

				expect(namen(manager)).toEqual(["Zeta", "Cäsar", "Berta"]);
			});

			test.each([null, undefined])('Option mit %s als Wert -> steht vorn, danach folgen die Optionen mit Wert', (wert) => {
				const manager = erzeugeManager([withProp("Cäsar", 1), withProp("Zeta", wert), withProp("Berta", 2)]);

				expect(namen(manager)).toEqual(["Zeta", "Cäsar", "Berta"]);
			});

			test('mehrere Optionen ohne Wert -> stehen vorn und sind untereinander alphabetisch sortiert', () => {
				const manager = erzeugeManager([withoutProp("Zeta"), withProp("Cäsar", 1), withProp("Alpha", null), withProp("Berta", 2)]);

				expect(namen(manager)).toEqual(["Alpha", "Zeta", "Cäsar", "Berta"]);
			});

			test('keine Option hat einen Wert -> alle Optionen werden alphabetisch sortiert', () => {
				const manager = erzeugeManager([withProp("Cäsar", null), withoutProp("Anton"), withProp("Berta", undefined)]);

				expect(namen(manager)).toEqual(["Anton", "Berta", "Cäsar"]);
			});

			test.each([{}, true])('erster Wert hat den Datentyp %o -> alle Optionen werden alphabetisch sortiert', (wert) => {
				const manager = erzeugeManager([withProp("Cäsar", wert), withProp("Anton", wert), withProp("Berta", wert)]);

				expect(namen(manager)).toEqual(["Anton", "Berta", "Cäsar"]);
			});

			test('Optionen werden nachträglich geändert -> der Datentyp wird neu bestimmt', () => {
				const manager = new SelectManager<Eintrag>({ sort, optionDisplayText });

				manager.unfilteredOptions = [withProp("Anton", 10), withProp("Berta", 2)];
				expect(namen(manager)).toEqual(["Berta", "Anton"]);

				manager.unfilteredOptions = [withProp("Anton", "b"), withProp("Berta", "a")];
				expect(namen(manager)).toEqual(["Berta", "Anton"]);

				manager.unfilteredOptions = [withProp("Anton", 1), withProp("Berta", 2)];
				expect(namen(manager)).toEqual(["Anton", "Berta"]);
			});

			test('Filter entfernt die Option ohne Wert -> die übrigen Optionen bleiben nach Wert sortiert', () => {
				const filter: SelectFilter<Eintrag> = {
					key: "nurMitWert",
					apply: (options: List<Eintrag>) => {
						const result = new ArrayList<Eintrag>();
						for (const option of options) {
							if (option.name !== "Zeta") {
								result.add(option);
							}
						}
						return result;
					},
				};
				const manager = new SelectManager<Eintrag>({ options: [withProp("Anton", 3), withProp("Berta", 2), withoutProp("Zeta")], sort, optionDisplayText, filters: [filter] });

				expect(namen(manager)).toEqual(["Berta", "Anton"]);
			});

			test('getComparator() ohne Optionen -> sortiert alphabetisch nach dem Anzeigetext', () => {
				const manager = erzeugeManager([]);

				const comparator = manager.getComparator();

				expect(comparator).not.toBeNull();
				expect(comparator!.compare(withProp("B", 1), withProp("A", 2))).toBeGreaterThan(0);
			});
		});

		test('updateSort() comparator ist null -> bringt die Optionen in ihre ursprüngliche Reihenfolge', async () => {
			const { fruitsByName, unsorted, sortedBySortierung } = createTestData();
			const sortRef = ref<SelectSort<Fruit>>(null);
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), sort: sortRef });

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			sortRef.value = "unsorted"; // erzeugt comparator = null
			await nextTick();

			expect(manager.filteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});
		});

		test('updateSort() comparator ist vorhanden -> sortiert die gefilterten Optionen', async () => {
			const { fruitsByName, sort, sortedBySortierung, sortedByCustom, unsorted } = createTestData();
			const sortRef = ref<SelectSort<Fruit>>(null);
			const manager = new SelectManager<Fruit>({ options: Object.values(fruitsByName), sort: sortRef });

			expect(manager.filteredOptions.size()).toBe(7);
			sortedBySortierung.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			sortRef.value = sort;
			await nextTick();

			expect(manager.filteredOptions.size()).toBe(7);
			sortedByCustom.forEach((element, index) => {
				expect(manager.filteredOptions.get(index)).toBe(element);
			});

			expect(manager.unfilteredOptions.size()).toBe(7);
			unsorted.forEach((element, index) => {
				expect(manager.unfilteredOptions.get(index)).toBe(element);
			});
		});

		test('updateSort() filteredOptions leer -> es wird nichts sortiert', () => {
			const { sort } = createTestData();
			const manager = new SelectManager<Fruit>({ sort });
			manager.updateSort();

			expect(manager.filteredOptions.size()).toBe(0);
		});

	});

});

/**
 * Erstellt Testdaten für die Tests des SelectManagers.
 *
 * @returns die testdaten: fruitsByName, letterCount, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort
 */
interface Fruit {
	id: number;
	sortierung: number;
	name: string;
}

/**
 * Erstellt Testdaten für die Tests des SelectManagers.
 *
 * @returns die Testdaten: fruitsByName, letterCount, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort, unsorted,
 * sortedAlphabetical, sortedById, sortedBySortierung, sortedByCustom
 */
function createTestData() {
	const fruitsByName = {
		Mango: { id: 10, sortierung: 30, name: "Mango" },
		Apple: { id: 150, sortierung: 45, name: "Apple" },
		Melon: { id: 2, sortierung: 15, name: "Melon" },
		Blueberry: { id: 1000, sortierung: 50, name: "Blueberry" },
		Clementine: { id: 100, sortierung: 20, name: "Clementine" },
		Cherry: { id: 7, sortierung: 60, name: "Cherry" },
		Banana: { id: 25, sortierung: 5, name: "Banana" },
	};

	const letterCount = { value: 5 };
	const letter = { value: "B" };

	const startsWithFilter = {
		key: "startsWithLetter",
		apply: (options: List<Fruit>) => {
			const filtered = new ArrayList<Fruit>();
			for (const option of options) {
				if (option.name.startsWith(letter.value)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const longNameFilter = {
		key: "longName",
		apply: (options: List<Fruit>) => {
			const filtered = new ArrayList<Fruit>();
			for (const option of options) {
				if (option.name.length > letterCount.value) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const selectionDisplayText = (s: Fruit) => `sel:${s.name}`;
	const optionDisplayText = (s: Fruit) => `opt:${s.name}`;
	const sort = (a: Fruit, b: Fruit) => b.name.localeCompare(a.name);

	/**
	 * Ursprüngliche Reihenfolge der Optionen.
	 */
	const unsorted: Fruit[] = [fruitsByName.Mango, fruitsByName.Apple, fruitsByName.Melon, fruitsByName.Blueberry, fruitsByName.Clementine, fruitsByName.Cherry, fruitsByName.Banana];

	/**
	 * Optionen alphabetisch nach dem Namen sortiert.
	 */
	const sortedAlphabetical: Fruit[] = [fruitsByName.Apple, fruitsByName.Banana, fruitsByName.Blueberry, fruitsByName.Cherry, fruitsByName.Clementine, fruitsByName.Mango, fruitsByName.Melon];

	/**
	 * Optionen numerisch aufsteigend nach id sortiert (2, 7, 10, 25, 100, 150, 1000).
	 * Ein lexikographischer Vergleich würde eine andere Reihenfolge liefern (10, 100, 1000, 150, 2, 25, 7).
	 */
	const sortedById: Fruit[] = [fruitsByName.Melon, fruitsByName.Cherry, fruitsByName.Mango, fruitsByName.Banana, fruitsByName.Clementine, fruitsByName.Apple, fruitsByName.Blueberry];

	/**
	 * Optionen aufsteigend nach der Props 'sortierung' sortiert (5, 15, 20, 30, 45, 50, 60).
	 */
	const sortedBySortierung: Fruit[] = [fruitsByName.Banana, fruitsByName.Melon, fruitsByName.Clementine, fruitsByName.Mango, fruitsByName.Apple, fruitsByName.Blueberry, fruitsByName.Cherry];

	/**
	 * Optionen mit dem eigenen Comparator (`sort`) alphabetisch absteigend nach dem Namen sortiert.
	 */
	const sortedByCustom: Fruit[] = [fruitsByName.Melon, fruitsByName.Mango, fruitsByName.Clementine, fruitsByName.Cherry, fruitsByName.Blueberry, fruitsByName.Banana, fruitsByName.Apple];

	return {
		fruitsByName, letterCount, letter, startsWithFilter, longNameFilter, selectionDisplayText, optionDisplayText, sort,
		unsorted, sortedAlphabetical, sortedById, sortedBySortierung, sortedByCustom,
	};
}
