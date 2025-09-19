import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";
import { FaecherListeEintrag } from "../../../svws-webclient/core/src/core/data/fach/FaecherListeEintrag";

describe("Feacher Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

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
