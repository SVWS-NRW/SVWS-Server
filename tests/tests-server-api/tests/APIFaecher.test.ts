import {describe, expect, test} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";
import {FaecherListeEintrag} from "@core";

describe("Feacher Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

		test("getFaecher", async () => {
			const result = await api.getFaecher(schema);
			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(FaecherListeEintrag);
		});

		test("getFach", async () => {
			const result = await api.getFach(schema, 16);
			expect(result).toMatchSnapshot();
		});
	})
})
