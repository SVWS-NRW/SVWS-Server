import { privilegedApiServer } from "@testUtils/APIUtils";
import { describe, expect, test } from "vitest";

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
