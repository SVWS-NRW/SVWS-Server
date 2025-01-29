import {describe, expect, test} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";

describe("Gesamtschule Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

		// TODO: Fix Me
		test.skip("getGesamtschuleSchuelerPrognoseLeistungsdaten", async () => {
			const result = await api.getGesamtschuleSchuelerPrognoseLeistungsdaten(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		// TODO: Fix Me
		test.skip("getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt", async () => {
			const result = await api.getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt(schema, 1199, 13);
			expect(result).toMatchSnapshot();
		});
	})
})
