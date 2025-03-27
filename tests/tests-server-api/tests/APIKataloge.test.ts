import {describe, expect, test} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";

describe("Kataloge Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

		test("getKatalogBeschaeftigungsart", async () => {
			const result = await api.getKatalogBeschaeftigungsart(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKatalogBetriebsart", async () => {
			const result = await api.getKatalogBetriebsart(schema);
			expect(result).toMatchSnapshot();
		});

		test("getHaltestellen", async () => {
			const result = await api.getHaltestellen(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKatalogOrte", async () => {
			const result = await api.getKatalogOrte(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKatalogOrtsteile", async () => {
			const result = await api.getKatalogOrtsteile(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKatalogReligionen", async () => {
			const result = await api.getKatalogReligionen(schema);
			expect(result).toMatchSnapshot();
		});
	})
})
