import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";
import { ArrayList } from "../../../svws-webclient/core/src/java/util/ArrayList";
import type { ENMNote } from "../../../svws-webclient/core/src/core/data/enm/ENMNote";
import type { ENMFoerderschwerpunkt } from "../../../svws-webclient/core/src/core/data/enm/ENMFoerderschwerpunkt";
import type { ENMJahrgang } from "../../../svws-webclient/core/src/core/data/enm/ENMJahrgang";
import type { ENMKlasse } from "../../../svws-webclient/core/src/core/data/enm/ENMKlasse";
import type { ENMFloskelgruppe } from "../../../svws-webclient/core/src/core/data/enm/ENMFloskelgruppe";
import type { ENMLehrer } from "../../../svws-webclient/core/src/core/data/enm/ENMLehrer";
import type { ENMFach } from "../../../svws-webclient/core/src/core/data/enm/ENMFach";
import type { ENMTeilleistungsart } from "../../../svws-webclient/core/src/core/data/enm/ENMTeilleistungsart";
import type { ENMLerngruppe } from "../../../svws-webclient/core/src/core/data/enm/ENMLerngruppe";
import type { ENMSchueler } from "../../../svws-webclient/core/src/core/data/enm/ENMSchueler";

describe("APIENM Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test("getLehrerENMDaten", async () => {
			const result = await api.getLehrerENMDaten(schema, 76);
			expect(result).toMatchSnapshot({
				noten: expect.any(ArrayList<ENMNote>),
				foerderschwerpunkte: expect.any(ArrayList<ENMFoerderschwerpunkt>),
				jahrgaenge: expect.any(ArrayList<ENMJahrgang>),
				klassen: expect.any(ArrayList<ENMKlasse>),
				floskelgruppen: expect.any(ArrayList<ENMFloskelgruppe>),
				lehrer: expect.any(ArrayList<ENMLehrer>),
				faecher: expect.any(ArrayList<ENMFach>),
				teilleistungsarten: expect.any(ArrayList<ENMTeilleistungsart>),
				lerngruppen: expect.any(ArrayList<ENMLerngruppe>),
				schueler: expect.any(ArrayList<ENMSchueler>),
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
	})
})
