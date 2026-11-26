import { describe, expect, test, vi } from "vitest";

import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import type { GenericAuswahlGruppenprozessHost } from "~/states/GenericAuswahlState";
import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "~/states/GruppenprozessBereich";

interface Eintrag {
	id: number;
	name: string;
}

interface Daten {
	id: number;
	name: string;
	ort?: string;
}

class TestBereich extends GruppenprozessBereich<Eintrag, Daten> {

	public static readonly SCHLUESSEL = new GruppenprozessBereichSchluessel<TestBereich>("test.bereich");

	public readonly ladeMock = vi.fn(async (ids: List<number>): Promise<Iterable<Daten> | null> => [...ids].map(id => daten(id)));

	public readonly patchMock = vi.fn(async (partials: List<Partial<Daten>>): Promise<void> => undefined);

	public erlaubt = true;

	public readonly bezeichnung = "Testbereich";

	public constructor() {
		super(d => d.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<TestBereich> {
		return TestBereich.SCHLUESSEL;
	}

	public override istBerechtigt(): boolean {
		return this.erlaubt;
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<Daten> | null> {
		return await this.ladeMock(ids);
	}

	protected override async doPatch(partials: List<Partial<Daten>>): Promise<void> {
		await this.patchMock(partials);
	}

}

/** Ein Bereich, der das Patchen nicht überschreibt */
class BereichOhnePatch extends GruppenprozessBereich<Eintrag, Daten> {

	public readonly bezeichnung = "Bereich ohne Patch";

	public constructor() {
		super(d => d.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<BereichOhnePatch> {
		return new GruppenprozessBereichSchluessel<BereichOhnePatch>("test.ohnepatch");
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<Daten> | null> {
		return [...ids].map(id => daten(id));
	}

}

function daten(id: number): Daten {
	return { id, name: "Name" + id };
}

function ids(...werte: number[]): List<number> {
	return ArrayList.of(...werte);
}

function createHost(auswahl: number[] = []) {
	const host: GenericAuswahlGruppenprozessHost<Eintrag> = {
		getEntry: id => ({ id, name: "E" + id }),
		getMultiSelection: () => ids(...auswahl),
	};
	return { host };
}

describe("GruppenprozessBereich", () => {

	test("ein Bereich hat eine Bezeichnung für die Anzeige", () => {
		expect(new TestBereich().bezeichnung).toBe("Testbereich");
	});

	test("ohne Anmeldung ist der Bereich leer und nicht angemeldet", () => {
		const bereich = new TestBereich();
		expect(bereich.istAngemeldet).toBe(false);
		expect(bereich.mapDaten.isEmpty()).toBe(true);
	});

	test("beim Anmelden werden die Daten der aktuellen Mehrfachauswahl geladen und der Bereich ist angemeldet", async () => {
		const { host } = createHost([1, 2]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		expect(bereich.istAngemeldet).toBe(true);
		expect([...bereich.mapDaten.keySet()].sort()).toEqual([1, 2]);
	});

	test("lädt nur das Delta nach und entfernt abgewählte Einträge erst bei der Übernahme", async () => {
		const bereich = new TestBereich();
		(await bereich.loadForMultiSelection(ids(1, 2)))();
		expect(bereich.ladeMock).toHaveBeenCalledTimes(1);
		expect([...bereich.mapDaten.keySet()].sort()).toEqual([1, 2]);

		const apply = await bereich.loadForMultiSelection(ids(2, 3));
		expect(bereich.ladeMock).toHaveBeenCalledTimes(2);
		expect([...bereich.ladeMock.mock.calls[1][0]]).toEqual([3]);
		expect([...bereich.mapDaten.keySet()].sort()).toEqual([1, 2]);
		apply();
		expect([...bereich.mapDaten.keySet()].sort()).toEqual([2, 3]);

		(await bereich.loadForMultiSelection(ids(2)))();
		expect(bereich.ladeMock).toHaveBeenCalledTimes(2);
		expect([...bereich.mapDaten.keySet()]).toEqual([2]);
	});

	test("eine leere Mehrfachauswahl verwirft alle Daten, ohne zu laden", async () => {
		const bereich = new TestBereich();
		(await bereich.loadForMultiSelection(ids(1)))();
		(await bereich.loadForMultiSelection(ids()))();
		expect(bereich.mapDaten.isEmpty()).toBe(true);
		expect(bereich.ladeMock).toHaveBeenCalledTimes(1);
	});

	test("ohne Ergebnis für das Delta führt zu einer Exception und ändert nichts", async () => {
		const bereich = new TestBereich();
		(await bereich.loadForMultiSelection(ids(1)))();
		bereich.ladeMock.mockResolvedValueOnce(null);
		await expect(bereich.loadForMultiSelection(ids(1, 2))).rejects.toThrow();
		expect([...bereich.mapDaten.keySet()]).toEqual([1]);
	});

	test("abmelden verwirft Daten, ausstehende Änderungen und Sperren", async () => {
		const { host } = createHost([1, 2]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		bereich.model.setPending("name", "X");
		bereich.denyPatch("a", "A");
		bereich.abmelden();
		expect(bereich.istAngemeldet).toBe(false);
		expect(bereich.isPatchAllowed).toBe(true);
		expect(bereich.mapDaten.isEmpty()).toBe(true);
		expect(bereich.model.hasPending).toBe(false);
	});

	test("der Proxy zählt die Datensätze und die Werte der Mehrfachauswahl", async () => {
		const { host } = createHost([1, 2]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		expect(bereich.model.count).toBe(2);
		expect([...bereich.model.valueCounts("name")]).toEqual([["Name1", 1], ["Name2", 1]]);
	});

	test("Patchen übergibt die Teilobjekte und übernimmt die Werte danach in die Daten", async () => {
		const { host } = createHost([1, 2]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		bereich.model.setPending("name", "Neu");
		await bereich.model.patchMultiple();
		expect([...bereich.patchMock.mock.calls[0][0]]).toEqual([{ id: 1, name: "Neu" }, { id: 2, name: "Neu" }]);
		expect(bereich.mapDaten.get(1)?.name).toBe("Neu");
		expect(bereich.mapDaten.get(2)?.name).toBe("Neu");
		expect(bereich.model.hasPending).toBe(false);
	});

	test("beim Übernehmen des Patches bleiben die nicht gepatchten Felder je Datensatz erhalten", async () => {
		const { host } = createHost([1, 2]);
		const bereich = new TestBereich();
		bereich.ladeMock.mockImplementation(async ids => [...ids].map(id => ({ id, name: "Name" + id, ort: "Ort" + id })));
		await bereich.anmelden(host, host.getMultiSelection());
		bereich.model.setPending("name", "Neu");
		await bereich.model.patchMultiple();
		expect(bereich.mapDaten.get(1)).toEqual({ id: 1, name: "Neu", ort: "Ort1" });
		expect(bereich.mapDaten.get(2)).toEqual({ id: 2, name: "Neu", ort: "Ort2" });
	});

	test("beim Übernehmen des Patches wird das ursprüngliche Datenobjekt nicht verändert", async () => {
		const { host } = createHost([1]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		const vorher = bereich.mapDaten.get(1);
		bereich.model.setPending("name", "Neu");
		await bereich.model.patchMultiple();
		expect(vorher?.name).toBe("Name1");
		expect(bereich.mapDaten.get(1)).not.toBe(vorher);
	});

	test("schlägt das Patchen fehl, bleiben die Daten und das Pending erhalten", async () => {
		const { host } = createHost([1]);
		const bereich = new TestBereich();
		await bereich.anmelden(host, host.getMultiSelection());
		bereich.patchMock.mockRejectedValueOnce(new Error("Fehler"));
		bereich.model.setPending("name", "Neu");
		await expect(bereich.model.patchMultiple()).rejects.toThrow("Fehler");
		expect(bereich.mapDaten.get(1)?.name).toBe("Name1");
		expect(bereich.model.hasPending).toBe(true);
	});

	test("das Patchen wird standardmäßig nicht unterstützt", async () => {
		const { host } = createHost([1]);
		const bereich = new BereichOhnePatch();
		await bereich.anmelden(host, host.getMultiSelection());
		bereich.model.setPending("name", "Neu");
		await expect(bereich.model.patchMultiple()).rejects.toThrow();
		expect(bereich.mapDaten.get(1)?.name).toBe("Name1");
	});

	test("ohne Anmeldung ist ein Zugriff auf den Auswahl-State ein Fehler, z.B. beim Nachladen der Daten", async () => {
		const bereich = new BereichOhnePatch();
		expect(bereich.istBerechtigt()).toBe(true);
		expect(() => (bereich as any).getEintragOrException(1)).toThrow();
	});

	describe("Fassade für den Orchestrator", () => {

		test("hasPending und clearPending beziehen sich auf den Proxy, clearPending hebt auch die Sperren auf", async () => {
			const { host } = createHost([1]);
			const bereich = new TestBereich();
			await bereich.anmelden(host, host.getMultiSelection());
			expect(bereich.hasPending).toBe(false);
			bereich.model.setPending("name", "X");
			bereich.denyPatch("a", "Hinweis");
			expect(bereich.hasPending).toBe(true);
			expect(bereich.isPatchAllowed).toBe(false);
			expect(bereich.patchHinweise).toEqual(["Hinweis"]);
			bereich.clearPending();
			expect(bereich.hasPending).toBe(false);
			expect(bereich.isPatchAllowed).toBe(true);
			expect(bereich.patchHinweise).toEqual([]);
		});

		test("patchMultiple patcht die ausstehenden Änderungen und leert sie", async () => {
			const { host } = createHost([1, 2]);
			const bereich = new TestBereich();
			await bereich.anmelden(host, host.getMultiSelection());
			bereich.model.setPending("name", "Neu");
			await bereich.patchMultiple();
			expect(bereich.patchMock).toHaveBeenCalledTimes(1);
			expect(bereich.hasPending).toBe(false);
		});

	});

	describe("Änderung der Mehrfachauswahl", () => {

		test("ändert sich die Menge der ausgewählten Einträge, werden Pending und Sperren bei der Übernahme verworfen", async () => {
			const bereich = new TestBereich();
			(await bereich.loadForMultiSelection(ids(1, 2)))();
			bereich.model.setPending("name", "X");
			bereich.denyPatch("a", "Hinweis");
			(await bereich.loadForMultiSelection(ids(1, 3)))();
			expect(bereich.hasPending).toBe(false);
			expect(bereich.isPatchAllowed).toBe(true);
		});

		test("eine leere Mehrfachauswahl (z.B. Wechsel des Schuljahresabschnitts) verwirft Pending und Sperren", async () => {
			const bereich = new TestBereich();
			(await bereich.loadForMultiSelection(ids(1)))();
			bereich.model.setPending("name", "X");
			(await bereich.loadForMultiSelection(ids()))();
			expect(bereich.hasPending).toBe(false);
		});

		test("bleibt die Menge gleich (auch in anderer Reihenfolge), bleiben Pending und Sperren erhalten", async () => {
			const bereich = new TestBereich();
			(await bereich.loadForMultiSelection(ids(1, 2)))();
			bereich.model.setPending("name", "X");
			bereich.denyPatch("a", "Hinweis");
			(await bereich.loadForMultiSelection(ids(2, 1)))();
			expect(bereich.hasPending).toBe(true);
			expect(bereich.isPatchAllowed).toBe(false);
		});

		test("das Pending wird erst bei der Übernahme verworfen, nicht beim Laden", async () => {
			const bereich = new TestBereich();
			(await bereich.loadForMultiSelection(ids(1)))();
			bereich.model.setPending("name", "X");
			const apply = await bereich.loadForMultiSelection(ids(2));
			expect(bereich.hasPending).toBe(true);
			apply();
			expect(bereich.hasPending).toBe(false);
		});

	});

});
