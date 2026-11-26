import { describe, expect, test, vi } from "vitest";
import { nextTick, ref } from "vue";

import type { List } from "@core/java/util/List";
import { MultiModelProxy } from "@ui/model/MultiModelProxy";

interface TestModel {
	id: number;
	name: string | null;
	aktiv: boolean;
	datum: string | null;
}

function model(id: number, name: string | null, aktiv = false, datum: string | null = null): TestModel {
	return { id, name, aktiv, datum };
}

/** Erzeugt einen Proxy auf einer reaktiven Datenmenge; der Patch ist ein Mock, der standardmäßig erfolgreich ist */
function createProxy(daten: TestModel[], patch: (partials: List<Partial<TestModel>>) => Promise<void> = vi.fn(async () => undefined)) {
	const data = ref<TestModel[]>(daten);
	const proxy = new MultiModelProxy<TestModel>({ data: () => data.value, getId: m => m.id, patchMultiple: patch });
	return { data, proxy, patch };
}

describe("MultiModelProxy Testsuite", () => {

	describe("Initialisierung", () => {
		test("ohne Änderungen gibt es kein Pending und keine Partials", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B")]);
			expect(proxy.hasPending).toBe(false);
			expect(proxy.isPending("name")).toBe(false);
			expect(proxy.pending).toEqual({});
			expect([...proxy.partials()]).toEqual([]);
		});
	});

	describe("Häufigkeitsverteilung der aktuellen Werte", () => {
		test("liefert je Wert die Anzahl der Datensätze in der Reihenfolge des ersten Auftretens", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B"), model(3, "A")]);
			expect([...proxy.valueCounts("name")]).toEqual([["A", 2], ["B", 1]]);
		});

		test("fehlende Werte (null) werden unter null gezählt", () => {
			const { proxy } = createProxy([model(1, null), model(2, "B"), model(3, null)]);
			expect(proxy.valueCounts("name").get(null)).toBe(2);
			expect(proxy.valueCounts("name").get("B")).toBe(1);
		});

		test("bei leerer Datenmenge ist die Verteilung leer", () => {
			const { proxy } = createProxy([]);
			expect(proxy.valueCounts("name").size).toBe(0);
		});

		test("die Gesamtzahl der Datensätze steht zur Verfügung", () => {
			expect(createProxy([model(1, "A"), model(2, "B")]).proxy.count).toBe(2);
			expect(createProxy([]).proxy.count).toBe(0);
		});

		test("das Pending verändert die Verteilung der aktuellen Werte nicht", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B")]);
			proxy.setPending("name", "X");
			expect([...proxy.valueCounts("name")]).toEqual([["A", 1], ["B", 1]]);
		});
	});

	describe("Pending setzen und entfernen", () => {
		test("setPending setzt hasPending, isPending und den Wert für das Feld", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B")]);
			proxy.setPending("name", "X");
			expect(proxy.hasPending).toBe(true);
			expect(proxy.isPending("name")).toBe(true);
			expect(proxy.isPending("datum")).toBe(false);
			expect(proxy.pending).toEqual({ name: "X" });
		});

		test("ein Pending-Wert null zählt als Pending (Wert leeren)", () => {
			const { proxy } = createProxy([model(1, "A")]);
			proxy.setPending("name", null);
			expect(proxy.hasPending).toBe(true);
			expect(proxy.isPending("name")).toBe(true);
			expect(proxy.pending.name).toBeNull();
		});

		test("removePending nimmt nur das eine Feld zurück", () => {
			const { proxy } = createProxy([model(1, "A")]);
			proxy.setPending("name", "X");
			proxy.setPending("aktiv", true);
			proxy.removePending("name");
			expect(proxy.isPending("name")).toBe(false);
			expect(proxy.isPending("aktiv")).toBe(true);
			expect(proxy.hasPending).toBe(true);
			proxy.removePending("aktiv");
			expect(proxy.hasPending).toBe(false);
		});

		test("removePending auf einem Feld ohne Pending ändert nichts", () => {
			const { proxy } = createProxy([model(1, "A")]);
			proxy.removePending("name");
			expect(proxy.hasPending).toBe(false);
		});

		test("clearPending verwirft alles Pending", () => {
			const { proxy } = createProxy([model(1, "A")]);
			proxy.setPending("name", "X");
			proxy.setPending("aktiv", true);
			proxy.clearPending();
			expect(proxy.hasPending).toBe(false);
			expect(proxy.pending).toEqual({});
			expect([...proxy.partials()]).toEqual([]);
		});
	});

	describe("Partials je Datensatz", () => {
		test("je Datensatz entsteht ein Teilobjekt mit ID und den Pending-Werten", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B")]);
			proxy.setPending("name", "X");
			proxy.setPending("aktiv", true);
			expect([...proxy.partials()]).toEqual([{ id: 1, name: "X", aktiv: true }, { id: 2, name: "X", aktiv: true }]);
		});

		test("ein Pending-Wert null erscheint als null im Teilobjekt", () => {
			const { proxy } = createProxy([model(1, "A")]);
			proxy.setPending("name", null);
			expect([...proxy.partials()]).toEqual([{ id: 1, name: null }]);
		});
	});

	describe("Patch", () => {
		test("patchMultiple übergibt die Partials, leert danach das Pending", async () => {
			const { proxy, patch } = createProxy([model(1, "A"), model(2, "B")]);
			proxy.setPending("name", "X");
			await expect(proxy.patchMultiple()).resolves.toBeUndefined();
			expect(patch).toHaveBeenCalledTimes(1);
			expect([...(patch as ReturnType<typeof vi.fn>).mock.calls[0][0]]).toEqual([{ id: 1, name: "X" }, { id: 2, name: "X" }]);
			expect(proxy.hasPending).toBe(false);
		});

		test("ohne Pending wird nichts gepatcht", async () => {
			const { proxy, patch } = createProxy([model(1, "A")]);
			await expect(proxy.patchMultiple()).resolves.toBeUndefined();
			expect(patch).not.toHaveBeenCalled();
		});

		test("wirft der Patch, bleibt das Pending erhalten und der Fehler wird weitergegeben", async () => {
			const fehler = new Error("Server nicht erreichbar");
			const { proxy } = createProxy([model(1, "A")], vi.fn(() => Promise.reject(fehler)));
			proxy.setPending("name", "X");
			await expect(proxy.patchMultiple()).rejects.toBe(fehler);
			expect(proxy.hasPending).toBe(true);
			expect(proxy.pending).toEqual({ name: "X" });
		});
	});

	describe("Anzeige-Mapper", () => {
		test("displayText nutzt den konfigurierten Mapper, sonst den Rohwert", () => {
			const data = ref([model(1, "A")]);
			const proxy = new MultiModelProxy<TestModel>({
				data: () => data.value, getId: m => m.id, patchMultiple: async () => undefined,
				displayMappers: new Map([["aktiv", (v: boolean) => v ? "Ja" : "Nein"]]),
			});
			expect(proxy.displayText("aktiv", true)).toBe("Ja");
			expect(proxy.displayText("aktiv", false)).toBe("Nein");
			expect(proxy.displayText("name", "A")).toBe("A");
		});
	});

	describe("vordefinierte Anzeige-Mapper", () => {

		test("booleanDisplayMapper liefert Ja und Nein", () => {
			expect(MultiModelProxy.booleanDisplayMapper(true)).toBe("Ja");
			expect(MultiModelProxy.booleanDisplayMapper(false)).toBe("Nein");
		});

		test("dateDisplayMapper formatiert ein ISO-Datum deutsch", () => {
			expect(MultiModelProxy.dateDisplayMapper("2024-03-05")).toBe("05.03.2024");
		});

	});

	describe("attribute (computed für die Bindung in der UI)", () => {
		test("ohne Pending liefert es den Default, danach den (gemappten) Pending-Wert", () => {
			const { proxy } = createProxy([model(1, "A")]);
			const roh = proxy.attribute({ attr: "datum", defaultValue: null });
			const gemappt = proxy.attribute({ attr: "name", defaultValue: "-", fromPending: (v: string | null) => `<${v}>`, toPending: v => v.toUpperCase() });
			expect(roh.value).toBeNull();
			expect(gemappt.value).toBe("-");
			roh.value = "2022-05-05";
			gemappt.value = "abc";
			expect(roh.value).toBe("2022-05-05");
			expect(proxy.pending).toEqual({ datum: "2022-05-05", name: "ABC" });
			expect(gemappt.value).toBe("<ABC>");
		});

		test("das Setzen ist ein Pending für die gesamte Auswahl", () => {
			const { proxy } = createProxy([model(1, "A"), model(2, "B")]);
			proxy.attribute({ attr: "aktiv", defaultValue: false }).value = true;
			expect([...proxy.partials()]).toEqual([{ id: 1, aktiv: true }, { id: 2, aktiv: true }]);
		});
	});

});
