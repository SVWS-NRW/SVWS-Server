import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Schule Tests ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer

		test("getSchullogo", async () => {
			const result = await api.getSchullogo(schema);
			expect(result).toMatchSnapshot();
		});

		test("getSchuleNummer", async () => {
			const result = await api.getSchuleNummer(schema);
			expect(result).toMatchSnapshot();
		});

		test("getSchuleStammdaten", async () => {
			const result = await api.getSchuleStammdaten(schema);
			expect(result).toMatchSnapshot();
		});

		test("getLernplattformen", async () => {
			const result = await api.getLernplattformen(schema);
			expect(result).toMatchSnapshot();
		});
	})
})
