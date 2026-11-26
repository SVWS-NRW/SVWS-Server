import { describe, expect, test, vi } from "vitest";

import { NavigationGuardQuery } from "@ui/ui/modal/NavigationGuardQuery";
import { ViewType } from "@ui/ui/nav/ViewType";

import { GruppenprozessNavigationGuard } from "~/router/GruppenprozessNavigationGuard";
import type { RouteNode } from "~/router/RouteNode";

/** Erzeugt einen einfachen Ersatz für einen Routenknoten */
function node(name: string, parent: unknown, types: ViewType[] = []): RouteNode<any, any> {
	return { name, parent, hasType: (t: ViewType) => types.includes(t) } as unknown as RouteNode<any, any>;
}

function createGruppenprozesse(hasPending: boolean) {
	return {
		hasPending,
		query: new NavigationGuardQuery(),
		clearPending: vi.fn(),
		patchMultiple: vi.fn(async () => undefined),
		isPatchAllowed: true,
		bereicheMitPending: ["Individualdaten"],
		patchHinweise: [] as string[],
	};
}

const auswahl = node("schueler", undefined);
const andereAuswahl = node("lehrer", undefined);
const tabDaten = node("schueler.gruppenprozesse.daten", auswahl, [ViewType.GRUPPENPROZESSE]);
const tabAllgemeines = node("schueler.gruppenprozesse.allgemeines", auswahl, [ViewType.GRUPPENPROZESSE]);
const einzelansicht = node("schueler.daten", auswahl, [ViewType.DEFAULT]);
const lehrerTab = node("lehrer.gruppenprozesse.daten", andereAuswahl, [ViewType.GRUPPENPROZESSE]);

describe("GruppenprozessNavigationGuard", () => {

	describe("isBlocked", () => {

		test("ohne ausstehende Änderungen blockiert er nie", () => {
			const guard = new GruppenprozessNavigationGuard(auswahl, createGruppenprozesse(false) as any);
			expect(guard.isBlocked(einzelansicht, tabDaten)).toBe(false);
			expect(guard.isBlocked(tabDaten, tabDaten)).toBe(false);
		});

		test("mit ausstehenden Änderungen blockiert er das Verlassen der Gruppenprozesse", () => {
			const guard = new GruppenprozessNavigationGuard(auswahl, createGruppenprozesse(true) as any);
			expect(guard.isBlocked(einzelansicht, tabDaten)).toBe(true);
			expect(guard.isBlocked(lehrerTab, tabDaten)).toBe(true);
		});

		test("ein Wechsel zu einem anderen Tab der Gruppenprozesse der gleichen Auswahl wird nicht blockiert", () => {
			const guard = new GruppenprozessNavigationGuard(auswahl, createGruppenprozesse(true) as any);
			expect(guard.isBlocked(tabAllgemeines, tabDaten)).toBe(false);
		});

		test("ein Wechsel auf den gleichen Knoten (z.B. Schuljahresabschnitt) wird blockiert", () => {
			const guard = new GruppenprozessNavigationGuard(auswahl, createGruppenprozesse(true) as any);
			expect(guard.isBlocked(tabDaten, tabDaten)).toBe(true);
		});

	});

	describe("queryConfirmation", () => {

		async function bestaetige(reply: "cancel" | "discard" | "save", gruppenprozesse = createGruppenprozesse(true)) {
			const guard = new GruppenprozessNavigationGuard(auswahl, gruppenprozesse as any);
			const ergebnis = guard.queryConfirmation(einzelansicht);
			expect(gruppenprozesse.query.isOpen).toBe(true);
			gruppenprozesse.query.reply(reply);
			return { ergebnis: await ergebnis, gruppenprozesse };
		}

		test("die Abfrage enthält die Bereiche mit Änderungen und die Angaben zum gesperrten Speichern", async () => {
			const gruppenprozesse = createGruppenprozesse(true);
			gruppenprozesse.isPatchAllowed = false;
			gruppenprozesse.patchHinweise = ["Zuzugsjahr ungültig"];
			const guard = new GruppenprozessNavigationGuard(auswahl, gruppenprozesse as any);
			const ergebnis = guard.queryConfirmation(einzelansicht);
			expect(gruppenprozesse.query.info).toEqual({ bereiche: ["Individualdaten"], isPatchAllowed: false, patchHinweise: ["Zuzugsjahr ungültig"] });
			gruppenprozesse.query.reply("cancel");
			await ergebnis;
		});

		test("Abbrechen: die Navigation wird abgebrochen, nichts wird verworfen oder gespeichert", async () => {
			const { ergebnis, gruppenprozesse } = await bestaetige("cancel");
			expect(ergebnis).toBe(false);
			expect(gruppenprozesse.clearPending).not.toHaveBeenCalled();
			expect(gruppenprozesse.patchMultiple).not.toHaveBeenCalled();
		});

		test("Verwerfen: die Änderungen werden verworfen und die Navigation fortgesetzt", async () => {
			const { ergebnis, gruppenprozesse } = await bestaetige("discard");
			expect(ergebnis).toBe(true);
			expect(gruppenprozesse.clearPending).toHaveBeenCalledTimes(1);
			expect(gruppenprozesse.patchMultiple).not.toHaveBeenCalled();
		});

		test("Speichern: die Änderungen werden gespeichert und die Navigation fortgesetzt", async () => {
			const { ergebnis, gruppenprozesse } = await bestaetige("save");
			expect(ergebnis).toBe(true);
			expect(gruppenprozesse.patchMultiple).toHaveBeenCalledTimes(1);
			expect(gruppenprozesse.clearPending).not.toHaveBeenCalled();
		});

		test("Speichern bei gesperrtem Speichern: es wird nicht gespeichert und die Navigation abgebrochen", async () => {
			const gruppenprozesse = createGruppenprozesse(true);
			gruppenprozesse.isPatchAllowed = false;
			const { ergebnis } = await bestaetige("save", gruppenprozesse);
			expect(ergebnis).toBe(false);
			expect(gruppenprozesse.patchMultiple).not.toHaveBeenCalled();
		});

		test("Speichern mit Fehler: der Fehler wird weitergegeben, die Navigation wird nicht bestätigt", async () => {
			const gruppenprozesse = createGruppenprozesse(true);
			gruppenprozesse.patchMultiple.mockRejectedValueOnce(new Error("Fehler"));
			await expect(bestaetige("save", gruppenprozesse)).rejects.toThrow("Fehler");
		});

	});

});
