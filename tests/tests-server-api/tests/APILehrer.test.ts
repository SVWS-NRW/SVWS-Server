import {getApiServer} from "./utils/TestUtils.js";
import {describe, expect, test} from "vitest";
import {LehrerListeEintrag, List} from "@svws-nrw/svws-core";

describe("Lehrer Tests ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer("gymabi");

		test("getLehrerListe", async () => {
			const liste: List<LehrerListeEintrag> = await api.getLehrer(schema);
			expect(liste).toMatchSnapshot();
			expect(liste.get(0)).toMatchSnapshot();
		});

		test("getLehrerPersonaldaten", async () => {
			const result = await api.getLehrerPersonaldaten(schema, 76);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerStammdaten", async () => {
			const result = await api.getLehrerStammdaten(schema, 76);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerAbgangsgruende", async () => {
			const result = await api.getLehrerAbgangsgruende(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerBeschaeftigungsarten", async () => {
			const result = await api.getLehrerBeschaeftigungsarten(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerEinsatzstatus", async () => {
			const result = await api.getLehrerEinsatzstatus(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerLeitungsfunktionen", async () => {
			const result = await api.getLehrerLeitungsfunktionen(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerRechtsverhaeltnisse", async () => {
			const result = await api.getLehrerRechtsverhaeltnisse(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerZugangsgruende", async () => {
			const result = await api.getLehrerZugangsgruende(schema);
			expect(result).toMatchSnapshot();
		});
	})

})
