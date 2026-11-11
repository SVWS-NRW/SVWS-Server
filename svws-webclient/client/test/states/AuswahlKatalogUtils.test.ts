import { beforeAll, describe, expect, test } from "vitest";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";

import { getSchulgliederungen } from "~/states/AuswahlKatalogUtils";

describe("Tests für AuswahlKatalogUtils", () => {

	beforeAll(() => {
		new JsonCoreTypeReaderStatic().readAll();
	});

	test("getSchulgliederungen | liefert die Schulgliederungen der Schulform", () => {
		expect(getSchulgliederungen(Schulform.GY, 2020).toArray()).toEqual(
			Schulgliederung.getBySchuljahrAndSchulform(2020, Schulform.GY).toArray()
		);
	});

	test("getSchulgliederungen | liefert ohne Schulform alle Schulgliederungen in der Reihenfolge ihrer Definition", () => {
		expect(getSchulgliederungen(null, 2020).toArray()).toEqual(Schulgliederung.values());
	});

});
