import {describe, expect, test} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";
import {
	ArrayList, ENMFach, ENMFloskelgruppe,
	ENMFoerderschwerpunkt,
	ENMJahrgang,
	ENMKlasse, ENMLehrer,
	ENMLerngruppe,
	ENMNote,
	ENMSchueler, ENMTeilleistungsart
} from "@core";

describe("APIENM Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

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
				schueler: expect.any(ArrayList<ENMSchueler>)
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
