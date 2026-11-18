import { describe, expect, test } from "vitest";

import { ENMv2Teilleistungsart } from "@core/core/data/enm/v2/ENMv2Teilleistungsart";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";

import { NotenmodulConfigManagerSichtbareSpalten } from "~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten";

describe("NotenmodulConfigManagerSichtbareSpalten", () => {

	test("Die Teilleistungsarten werden in der Reihenfolge ihrer Sortierung aufgeführt", () => {
		const mapArten = new HashMap<number, ENMv2Teilleistungsart>();
		for (const [id, bezeichnung, sortierung] of [[100, "SoMi", 3], [101, "KA", 1], [102, "Test", 2]] as Array<[number, string, number]>) {
			const art = new ENMv2Teilleistungsart();
			art.id = id;
			art.bezeichnung = bezeichnung;
			art.sortierung = sortierung;
			mapArten.put(id, art);
		}
		const manager = new NotenmodulConfigManagerSichtbareSpalten(new ArrayList(), mapArten, async () => {});
		expect([...manager.spaltenTeilleistungen.value]).toEqual(["KA", "Test", "SoMi"]);
	});

});
