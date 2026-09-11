import { privilegedApiServer } from "@testUtils/APIUtils";
import { describe, expect, test } from "vitest";

import type { ENMv1Fach } from "@core/core/data/enm/v1/ENMv1Fach";
import type { ENMv1Floskelgruppe } from "@core/core/data/enm/v1/ENMv1Floskelgruppe";
import type { ENMv1Foerderschwerpunkt } from "@core/core/data/enm/v1/ENMv1Foerderschwerpunkt";
import type { ENMv1Jahrgang } from "@core/core/data/enm/v1/ENMv1Jahrgang";
import type { ENMv1Klasse } from "@core/core/data/enm/v1/ENMv1Klasse";
import type { ENMv1Lehrer } from "@core/core/data/enm/v1/ENMv1Lehrer";
import type { ENMv1Lerngruppe } from "@core/core/data/enm/v1/ENMv1Lerngruppe";
import type { ENMv1Note } from "@core/core/data/enm/v1/ENMv1Note";
import type { ENMv1Schueler } from "@core/core/data/enm/v1/ENMv1Schueler";
import type { ENMv1Teilleistungsart } from "@core/core/data/enm/v1/ENMv1Teilleistungsart";
import { ArrayList } from "@core/java/util/ArrayList";

describe("APIENM Tests", () => {
	describe.each([{ schema: "GymAbi01" }])('gegen %s', ({ schema }) => {
		const api = privilegedApiServer;

		test("getLehrerENMDaten", async () => {
			const result = await api.getLehrerENMDaten(schema, 76);
			expect(result).toMatchSnapshot({
				noten: expect.any(ArrayList<ENMv1Note>),
				foerderschwerpunkte: expect.any(ArrayList<ENMv1Foerderschwerpunkt>),
				jahrgaenge: expect.any(ArrayList<ENMv1Jahrgang>),
				klassen: expect.any(ArrayList<ENMv1Klasse>),
				floskelgruppen: expect.any(ArrayList<ENMv1Floskelgruppe>),
				lehrer: expect.any(ArrayList<ENMv1Lehrer>),
				faecher: expect.any(ArrayList<ENMv1Fach>),
				teilleistungsarten: expect.any(ArrayList<ENMv1Teilleistungsart>),
				lerngruppen: expect.any(ArrayList<ENMv1Lerngruppe>),
				schueler: expect.any(ArrayList<ENMv1Schueler>),
			});
			expect(result.noten.size()).toMatchSnapshot();
			expect(result.foerderschwerpunkte.size()).toMatchSnapshot();
			expect(result.jahrgaenge.size()).toMatchSnapshot();
			expect(result.klassen.size()).toMatchSnapshot();
			expect(result.floskelgruppen.size()).toMatchSnapshot();
			expect(result.lehrer.size()).toMatchSnapshot();
			expect(result.faecher.size()).toMatchSnapshot();
			expect(result.teilleistungsarten.size()).toMatchSnapshot();
			expect(result.lerngruppen.size()).toMatchSnapshot();
			expect(result.schueler.size()).toMatchSnapshot();
		});
	});
});
