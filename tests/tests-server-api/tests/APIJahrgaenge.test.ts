import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Jahrgaenge Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test("getJahrgaenge", async () => {
			const result = await api.getJahrgaenge(schema);
			expect(result).toMatchSnapshot();
		});

		test("getJahrgang", async () => {
			const result = await api.getJahrgang(schema, 10);
			expect(result).toMatchSnapshot();
		});
	})
})
