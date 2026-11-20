import { beforeAll, describe, expect, test, vi } from "vitest";

import { SchuelerLeistungsdaten } from "@core/asd/data/schueler/SchuelerLeistungsdaten";
import { ZulaessigeKursart } from "@core/asd/types/kurse/ZulaessigeKursart";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";

import { SchuelerLeistungsdatenModelProxy } from "~/components/schueler/lernabschnitte/leistungen/modelproxy/SchuelerLeistungsdatenModelProxy";
import type { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";

/** Wartet, bis die asynchronen Patch-Aufrufe des ModelProxys abgeschlossen sind. */
async function warteAufPatch(): Promise<void> {
	await new Promise(resolve => setTimeout(resolve));
}

function erstelleLeistung(kursart: string, abifach: number | null): SchuelerLeistungsdaten {
	const leistung = new SchuelerLeistungsdaten();
	leistung.id = 1;
	leistung.kursart = kursart;
	leistung.abifach = abifach;
	return leistung;
}

describe("Tests für den SchuelerLeistungsdatenModelProxy", () => {

	beforeAll(() => {
		new JsonCoreTypeReaderStatic().readAll();
	});

	test.each([
		[ZulaessigeKursart.AB5, "GKS", null, 5],
		[ZulaessigeKursart.AB4, "AB5", 5, 4],
		[ZulaessigeKursart.AB3, "GKS", null, 3],
		[ZulaessigeKursart.GKS, "AB5", 5, null],
	])("Kursart %s setzt das passende Abiturfach", async (kursart, kursartVorher, abifachVorher, abifachErwartet) => {
		const patch = vi.fn(async () => true);
		const manager = {} as SchuelerLernabschnittManager;
		const model = new SchuelerLeistungsdatenModelProxy(() => erstelleLeistung(kursartVorher, abifachVorher), () => manager, () => 2029, patch);

		model.kursart.value = kursart;
		await warteAufPatch();

		expect(patch).toHaveBeenCalledWith(expect.objectContaining({ kursart: kursart.daten(2029)?.kuerzel, abifach: abifachErwartet }));
	});

});
