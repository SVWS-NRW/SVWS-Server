import { describe, expect, test } from "vitest";

import { GruppenprozessBereichPatchSperren } from "~/states/GruppenprozessBereichPatchSperren";

describe("GruppenprozessBereichPatchSperren", () => {

	test("ohne Sperre ist das Speichern möglich", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		expect(sperren.isPatchAllowed).toBe(true);
		expect(sperren.hinweise).toEqual([]);
	});

	test("eine Sperre verhindert das Speichern und liefert ihren Hinweis", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		sperren.deny("zuzugsjahr", "Zuzugsjahr ungültig");
		expect(sperren.isPatchAllowed).toBe(false);
		expect(sperren.hinweise).toEqual(["Zuzugsjahr ungültig"]);
	});

	test("dieselbe Sperre wird nur einmal geführt, der Hinweis wird aktualisiert", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		sperren.deny("zuzugsjahr", "alt");
		sperren.deny("zuzugsjahr", "neu");
		expect(sperren.hinweise).toEqual(["neu"]);
	});

	test("mehrere Sperren bleiben bestehen, bis jede einzeln freigegeben ist", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		sperren.deny("a", "Hinweis A");
		sperren.deny("b", "Hinweis B");
		expect(sperren.hinweise).toEqual(["Hinweis A", "Hinweis B"]);
		sperren.allow("a");
		expect(sperren.isPatchAllowed).toBe(false);
		sperren.allow("b");
		expect(sperren.isPatchAllowed).toBe(true);
	});

	test("das Freigeben einer nicht vorhandenen Sperre tut nichts", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		expect(() => sperren.allow("unbekannt")).not.toThrow();
		expect(sperren.isPatchAllowed).toBe(true);
	});

	test("allowAll hebt alle Sperren auf", () => {
		const sperren = new GruppenprozessBereichPatchSperren();
		sperren.deny("a", "A");
		sperren.deny("b", "B");
		sperren.allowAll();
		expect(sperren.isPatchAllowed).toBe(true);
		expect(sperren.hinweise).toEqual([]);
	});

});
