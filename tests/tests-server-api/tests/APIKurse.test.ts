import {describe, expect, test} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";

describe("APIKurse Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

		test("getKurse", async () => {
			const result = await api.getKurse(schema);
			expect(result).toMatchSnapshot();
		});

		test("getKurs", async () => {
			const result = await api.getKurs(schema, 6178);
			expect(result).toMatchSnapshot();
		});

		test("getKurseFuerAbschnitt", async () => {
			const result = await api.getKurseFuerAbschnitt(schema, 13);
			expect(result).toMatchSnapshot();
		});
	})
})
