import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Kataloge Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test("getBeschaeftigungsarten", async () => {
			const result = await api.getBeschaeftigungsarten(schema);
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
