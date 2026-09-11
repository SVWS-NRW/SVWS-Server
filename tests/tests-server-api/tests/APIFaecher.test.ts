import { privilegedApiServer } from "@testUtils/APIUtils";
import { describe, expect, test } from "vitest";

import { FachDaten } from "@core/core/data/fach/FachDaten";

describe("Feacher Tests", () => {
	describe.each([{ schema: "GymAbi01" }])('gegen %s', ({ schema }) => {
		const api = privilegedApiServer;

		test("getFaecher", async () => {
			const result = await api.getFaecher(schema);
			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(FachDaten);
		});

		test("getFach", async () => {
			const result = await api.getFach(schema, 16);
			expect(result).toMatchSnapshot();
		});
	});
});
