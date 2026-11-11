import { beforeAll, describe, expect, test, vi } from "vitest";

import { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashSet } from "@core/java/util/HashSet";

import { KlassenAuswahlFilter, klassenFilterSchulgliederungKey } from "~/states/klassen/KlassenAuswahlFilter";

vi.mock("~/states/SchuleStateImpl", () => ({
	schuleStateImpl: { schuljahr: 2020 },
}));

describe("Tests für KlassenAuswahlFilter", () => {

	beforeAll(() => {
		new JsonCoreTypeReaderStatic().readAll();
	});

	test("Filter | Klasse not in Jahrgänge throws Exception", () => {
		const klasse = new KlassenListeEintrag();
		klasse.id = -1;
		klasse.idJahrgang = 2; // existiert nicht in den Jahrgängen
		const schulgliederung = Schulgliederung.getBySchuljahrAndSchulform(2020, Schulform.GY).get(0);
		const filter = new KlassenAuswahlFilter(() => [klasse], () => null);
		filter.setOptions({ jahrgaenge: new ArrayList(), lehrer: new ArrayList(), schulgliederungen: ArrayList.of(schulgliederung), schuljahr: 2020 });

		// Schulgliederungsauswahl setzen → der Filter ruft die Jahrgangsdaten ab
		filter.setValue("schulgliederungen", new HashSet<string>(ArrayList.of(klassenFilterSchulgliederungKey(schulgliederung, 2020))));

		expect(() => filter.filtered).toThrow("Kein gültiger Schlüsselwert.");
	});

});
