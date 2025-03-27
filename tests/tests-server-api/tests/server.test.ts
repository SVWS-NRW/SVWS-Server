import {describe, test, expect} from "vitest";
import {getApiServer} from "./utils/TestUtils.js";

describe("Server Tests2 ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer(schema);

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
