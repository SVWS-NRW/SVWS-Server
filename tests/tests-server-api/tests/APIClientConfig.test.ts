import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe.skip("ClientConfig Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {

		const api = privilegedApiServer;

		test("getClientConfigUserKey", async () => {
			// throw bei fehlendem Parameter, bzw Fehler abfangen
			const result = await api.getClientConfigUserKey(schema, "test", "test");
			expect(result).toMatchSnapshot();
		});
	})
})
