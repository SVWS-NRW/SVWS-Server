import { describe, expect, test, vi } from "vitest";

import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import type { GenericAuswahlGruppenprozessHost } from "~/states/GenericAuswahlState";
import { GenericGruppenprozessState } from "~/states/GenericGruppenprozessState";
import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "~/states/GruppenprozessBereich";

interface Eintrag {
	id: number;
}

interface Daten {
	id: number;
	name: string;
}

class Bereich extends GruppenprozessBereich<Eintrag, Daten> {

	public readonly patchMock = vi.fn(async (partials: List<Partial<Daten>>): Promise<void> => undefined);

	public erlaubt = true;

	/** Wenn gesetzt, wartet das Laden der Daten darauf */
	public ladeVerzoegerung: Promise<void> | null = null;

	/** Wenn gesetzt, scheitert das Laden der Daten */
	public ladeScheitert = false;

	public get bezeichnung(): string {
		return "Bereich " + this._schluessel.name;
	}

	public constructor(private readonly _schluessel: GruppenprozessBereichSchluessel<Bereich>) {
		super(d => d.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<Bereich> {
		return this._schluessel;
	}

	public override istBerechtigt(): boolean {
		return this.erlaubt;
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<Daten> | null> {
		await this.ladeVerzoegerung;
		return this.ladeScheitert ? null : [...ids].map(id => ({ id, name: "Name" + id }));
	}

	protected override async doPatch(partials: List<Partial<Daten>>): Promise<void> {
		await this.patchMock(partials);
	}

}

const STAMM = new GruppenprozessBereichSchluessel<Bereich>("Test: Stammdaten");
const PERSONAL = new GruppenprozessBereichSchluessel<Bereich>("Test: Personaldaten");

function createState(auswahl: number[] = [1, 2]) {
	const host: GenericAuswahlGruppenprozessHost<Eintrag> = {
		getEntry: id => ({ id }),
		getMultiSelection: () => ArrayList.of(...auswahl),
	};
	return { state: new GenericGruppenprozessState<Eintrag>(host) };
}

describe("GenericGruppenprozessState (Orchestrator)", () => {

	test("ohne angemeldeten Bereich ist der Orchestrator nutzbar: nichts ausstehend, Speichern, Verwerfen und Zurücksetzen tun nichts", async () => {
		const { state } = createState();
		expect(state.bereich(STAMM)).toBeNull();
		expect(state.hasPending).toBe(false);
		await expect(state.patchMultiple()).resolves.toBeUndefined();
		expect(() => state.clearPending()).not.toThrow();
		expect(() => state.reset()).not.toThrow();
	});

	test("register meldet den Bereich an, bereich liefert ihn, ohne Anmeldung null", async () => {
		const { state } = createState();
		expect(state.bereich(STAMM)).toBeNull();
		const bereich = await state.register(STAMM, () => new Bereich(STAMM));
		expect(state.bereich(STAMM)).toBe(bereich);
		expect(bereich.istAngemeldet).toBe(true);
		expect([...bereich.mapDaten.keySet()].sort()).toEqual([1, 2]);
	});

	test("die Identität eines Bereiches bestimmt das Schlüsselobjekt, nicht der Name", async () => {
		const { state } = createState();
		const gleicherName = new GruppenprozessBereichSchluessel<Bereich>(STAMM.name);
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		expect(state.bereich(gleicherName)).toBeNull();
		const anderer = await state.register(gleicherName, () => new Bereich(gleicherName));
		expect(anderer).not.toBe(stamm);
		expect(state.bereich(STAMM)).toBe(stamm);
		expect(state.bereich(gleicherName)).toBe(anderer);
	});

	test("die Fehlermeldungen nennen den Namen des Schlüssels", async () => {
		const { state } = createState();
		expect(() => state.bereichOrException(STAMM)).toThrow(STAMM.name);
		const ohneBerechtigung = new Bereich(STAMM);
		ohneBerechtigung.erlaubt = false;
		await expect(state.register(STAMM, () => ohneBerechtigung)).rejects.toThrow(STAMM.name);
	});

	test("bereichOrException liefert den angemeldeten Bereich und wirft ohne Anmeldung", async () => {
		const { state } = createState();
		expect(() => state.bereichOrException(STAMM)).toThrow();
		const bereich = await state.register(STAMM, () => new Bereich(STAMM));
		expect(state.bereichOrException(STAMM)).toBe(bereich);
		state.reset();
		expect(() => state.bereichOrException(STAMM)).toThrow();
	});

	test("ein zweites Anmelden liefert denselben Bereich und erzeugt keinen neuen", async () => {
		const { state } = createState();
		const createBereich = vi.fn(() => new Bereich(STAMM));
		const erster = await state.register(STAMM, createBereich);
		const zweiter = await state.register(STAMM, createBereich);
		expect(zweiter).toBe(erster);
		expect(createBereich).toHaveBeenCalledTimes(1);
	});

	test("ein gleichzeitiges Anmelden liefert ebenfalls denselben Bereich", async () => {
		const { state } = createState();
		const [a, b] = await Promise.all([state.register(STAMM, () => new Bereich(STAMM)), state.register(STAMM, () => new Bereich(STAMM))]);
		expect(a).toBe(b);
	});

	test("ein Bereich ohne Berechtigung wird nicht angemeldet, sondern führt zu einem Fehler", async () => {
		const { state } = createState();
		const bereich = new Bereich(STAMM);
		bereich.erlaubt = false;
		await expect(state.register(STAMM, () => bereich)).rejects.toThrow();
		expect(state.bereich(STAMM)).toBeNull();
		expect(bereich.istAngemeldet).toBe(false);
	});

	test("hasPending ist wahr, sobald irgendein Bereich ausstehende Änderungen hat", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		expect(state.hasPending).toBe(false);
		personal.model.setPending("name", "X");
		expect(state.hasPending).toBe(true);
		personal.model.clearPending();
		stamm.model.setPending("name", "Y");
		expect(state.hasPending).toBe(true);
	});

	test("patchMultiple patcht alle Bereiche mit ausstehenden Änderungen und leert diese", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		stamm.model.setPending("name", "Neu");
		await state.patchMultiple();
		expect(stamm.patchMock).toHaveBeenCalledTimes(1);
		expect(personal.patchMock).not.toHaveBeenCalled();
		expect(state.hasPending).toBe(false);
	});

	test("bei einem Fehler behält der fehlerhafte Bereich sein Pending, die übrigen werden trotzdem gepatcht und der Fehler wird weitergegeben", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		stamm.patchMock.mockRejectedValueOnce(new Error("Stammdaten fehlgeschlagen"));
		stamm.model.setPending("name", "A");
		personal.model.setPending("name", "B");
		await expect(state.patchMultiple()).rejects.toThrow("Stammdaten fehlgeschlagen");
		expect(personal.patchMock).toHaveBeenCalledTimes(1);
		expect(personal.model.hasPending).toBe(false);
		expect(stamm.model.hasPending).toBe(true);
		expect(state.hasPending).toBe(true);
	});

	test("patchMultiple startet das Patchen aller Bereiche parallel", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		const { promise, resolve: freigeben } = Promise.withResolvers<void>();
		stamm.patchMock.mockImplementationOnce(async () => await promise);
		stamm.model.setPending("name", "A");
		personal.model.setPending("name", "B");
		const ergebnis = state.patchMultiple();
		await vi.waitFor(() => expect(personal.patchMock).toHaveBeenCalledTimes(1));
		expect(stamm.patchMock).toHaveBeenCalledTimes(1);
		expect(stamm.model.hasPending).toBe(true);
		freigeben();
		await ergebnis;
		expect(state.hasPending).toBe(false);
	});

	test("bei Fehlern in mehreren Bereichen werden alle Fehler als AggregateError gemeldet", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		const fehlerStamm = new Error("Stammdaten fehlgeschlagen");
		const fehlerPersonal = new Error("Personaldaten fehlgeschlagen");
		stamm.patchMock.mockRejectedValueOnce(fehlerStamm);
		personal.patchMock.mockRejectedValueOnce(fehlerPersonal);
		stamm.model.setPending("name", "A");
		personal.model.setPending("name", "B");
		const fehler = await state.patchMultiple().then(() => null, (e: unknown) => e);
		expect(fehler).toBeInstanceOf(AggregateError);
		expect((fehler as AggregateError).errors).toEqual([fehlerStamm, fehlerPersonal]);
		expect((fehler as AggregateError).message).toBe("Stammdaten fehlgeschlagen; Personaldaten fehlgeschlagen");
		expect(state.hasPending).toBe(true);
	});

	describe("Mehrfachauswahl", () => {

		test("loadForMultiSelection lädt die Daten aller Bereiche und übernimmt sie erst mit der zurückgegebenen Funktion", async () => {
			const { state } = createState([1, 2]);
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
			const apply = await state.loadForMultiSelection(ArrayList.of(2, 3));
			expect([...stamm.mapDaten.keySet()].sort()).toEqual([1, 2]);
			expect([...personal.mapDaten.keySet()].sort()).toEqual([1, 2]);
			apply();
			expect([...stamm.mapDaten.keySet()].sort()).toEqual([2, 3]);
			expect([...personal.mapDaten.keySet()].sort()).toEqual([2, 3]);
		});

		test("ein Fehler beim Laden eines Bereiches übernimmt in keinem Bereich Daten", async () => {
			const { state } = createState([1]);
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
			vi.spyOn(personal as unknown as { ladeDatenMultiple: () => Promise<null> }, "ladeDatenMultiple").mockResolvedValueOnce(null);
			await expect(state.loadForMultiSelection(ArrayList.of(1, 2))).rejects.toThrow();
			expect([...stamm.mapDaten.keySet()]).toEqual([1]);
			expect([...personal.mapDaten.keySet()]).toEqual([1]);
		});

		test("eine Änderung der Mehrfachauswahl während der Anmeldung eines Bereiches wird nach dem Anmelden berücksichtigt", async () => {
			const { state } = createState([1]);
			const bereich = new Bereich(STAMM);
			const { promise, resolve: laden } = Promise.withResolvers<void>();
			bereich.ladeVerzoegerung = promise;
			const anmeldung = state.register(STAMM, () => bereich);
			const apply = state.loadForMultiSelection(ArrayList.of(2, 3));
			laden();
			await anmeldung;
			(await apply)();
			expect([...bereich.mapDaten.keySet()].sort()).toEqual([2, 3]);
		});

		test("ein fehlgeschlagenes Anmelden lässt loadForMultiSelection nicht scheitern", async () => {
			const { state } = createState([1]);
			const bereich = new Bereich(STAMM);
			bereich.ladeScheitert = true;
			const anmeldung = state.register(STAMM, () => bereich);
			const apply = state.loadForMultiSelection(ArrayList.of(2));
			await expect(anmeldung).rejects.toThrow();
			await expect(apply).resolves.toBeTypeOf("function");
			expect(state.bereich(STAMM)).toBeNull();
		});

		test("überholt eine neuere Mehrfachauswahl eine ältere, übernimmt nur die neuere ihre Daten", async () => {
			const { state } = createState([1]);
			const bereich = await state.register(STAMM, () => new Bereich(STAMM));
			const { promise, resolve: laden } = Promise.withResolvers<void>();
			bereich.ladeVerzoegerung = promise;
			const aelter = state.loadForMultiSelection(ArrayList.of(2));
			bereich.ladeVerzoegerung = null;
			const neuer = await state.loadForMultiSelection(ArrayList.of(3));
			neuer();
			laden();
			(await aelter)();
			expect([...bereich.mapDaten.keySet()]).toEqual([3]);
		});

		test("ohne Überholen übernimmt jede Mehrfachauswahl ihre Daten", async () => {
			const { state } = createState([1]);
			const bereich = await state.register(STAMM, () => new Bereich(STAMM));
			(await state.loadForMultiSelection(ArrayList.of(2)))();
			(await state.loadForMultiSelection(ArrayList.of(3)))();
			expect([...bereich.mapDaten.keySet()]).toEqual([3]);
		});

		test("ohne angemeldete Bereiche tut loadForMultiSelection nichts", async () => {
			const { state } = createState();
			const apply = await state.loadForMultiSelection(ArrayList.of(1));
			expect(() => apply()).not.toThrow();
		});

		test("eine geänderte Mehrfachauswahl verwirft das Pending der Bereiche", async () => {
			const { state } = createState([1, 2]);
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			stamm.model.setPending("name", "X");
			(await state.loadForMultiSelection(ArrayList.of()))();
			expect(state.hasPending).toBe(false);
		});

		test("nach reset werden die Bereiche nicht mehr informiert", async () => {
			const { state } = createState([1]);
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			state.reset();
			(await state.loadForMultiSelection(ArrayList.of(1, 2)))();
			expect(stamm.mapDaten.isEmpty()).toBe(true);
		});

	});

	test("clearPending verwirft das Pending aller Bereiche, sie bleiben angemeldet", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
		stamm.model.setPending("name", "A");
		personal.model.setPending("name", "B");
		state.clearPending();
		expect(state.hasPending).toBe(false);
		expect(state.bereich(STAMM)).toBe(stamm);
		expect(stamm.istAngemeldet).toBe(true);
	});

	test("reset meldet alle Bereiche ab und verwirft deren Daten und Pending", async () => {
		const { state } = createState();
		const stamm = await state.register(STAMM, () => new Bereich(STAMM));
		stamm.model.setPending("name", "A");
		state.reset();
		expect(state.bereich(STAMM)).toBeNull();
		expect(stamm.istAngemeldet).toBe(false);
		expect(stamm.mapDaten.isEmpty()).toBe(true);
		expect(state.hasPending).toBe(false);
	});

	test("nach reset kann ein Bereich erneut angemeldet werden", async () => {
		const { state } = createState();
		await state.register(STAMM, () => new Bereich(STAMM));
		state.reset();
		const neu = await state.register(STAMM, () => new Bereich(STAMM));
		expect(neu.istAngemeldet).toBe(true);
	});

	describe("Bereiche mit ausstehenden Änderungen", () => {

		test("ohne Pending ist die Liste leer", async () => {
			const { state } = createState();
			await state.register(STAMM, () => new Bereich(STAMM));
			expect(state.bereicheMitPending).toEqual([]);
		});

		test("nennt die Bezeichnungen der Bereiche, die ausstehende Änderungen haben", async () => {
			const { state } = createState();
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
			personal.model.setPending("name", "X");
			expect(state.bereicheMitPending).toEqual(["Bereich Test: Personaldaten"]);
			stamm.model.setPending("name", "Y");
			expect(state.bereicheMitPending).toEqual(["Bereich Test: Stammdaten", "Bereich Test: Personaldaten"]);
		});

	});

	describe("Sperren des Speicherns", () => {

		test("ohne angemeldeten Bereich ist das Speichern möglich", () => {
			const { state } = createState();
			expect(state.isPatchAllowed).toBe(true);
			expect(state.patchHinweise).toEqual([]);
		});

		test("eine Sperre in einem Bereich sperrt das Speichern des Orchestrators und liefert die Hinweise aller Bereiche", async () => {
			const { state } = createState();
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			const personal = await state.register(PERSONAL, () => new Bereich(PERSONAL));
			expect(state.isPatchAllowed).toBe(true);
			stamm.denyPatch("a", "Hinweis Stamm");
			personal.denyPatch("b", "Hinweis Personal");
			expect(state.isPatchAllowed).toBe(false);
			expect(state.patchHinweise).toEqual(["Hinweis Stamm", "Hinweis Personal"]);
			stamm.allowPatch("a");
			expect(state.patchHinweise).toEqual(["Hinweis Personal"]);
			personal.allowPatch("b");
			expect(state.isPatchAllowed).toBe(true);
		});

		test("clearPending hebt die Sperren aller Bereiche auf", async () => {
			const { state } = createState();
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			stamm.denyPatch("a", "Hinweis");
			state.clearPending();
			expect(state.isPatchAllowed).toBe(true);
		});

		test("reset hebt die Sperren auf", async () => {
			const { state } = createState();
			const stamm = await state.register(STAMM, () => new Bereich(STAMM));
			stamm.denyPatch("a", "Hinweis");
			state.reset();
			expect(state.isPatchAllowed).toBe(true);
		});

	});

});
