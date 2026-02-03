import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Kataloge Tests", () => {
	describe.each([{ schema: "GymAbi01" }])('gegen %s', ({ schema }) => {
		const api = privilegedApiServer;

		test("getBeschaeftigungsarten", async () => {
			const result = await api.getBeschaeftigungsarten(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKatalogBetriebsart", async () => {
			const result = await api.getBetriebsarten(schema);
			expect(result).toMatchSnapshot();
		});

		test("getHaltestellen", async () => {
			const result = await api.getHaltestellen(schema);
			expect(result).toMatchSnapshot();
		});

	});
});
