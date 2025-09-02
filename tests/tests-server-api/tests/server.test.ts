import { describe, test, expect } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Server Tests2 ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({}) => {
		const api = privilegedApiServer;

		test("Ein Server Objekt konnte erzeugt werden", async () => {
			expect(api).toBeDefined();
		});
		test("einfacher isAlive Check", async () => {
			try {
				await api.isAlive();
			} catch (e) {
				console.log(e);
			}
			expect(await api.isAlive()).toBeDefined();
		});
	});
});
