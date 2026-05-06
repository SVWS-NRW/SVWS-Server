import { describe, expect, test } from "vitest";
import { ApiEnmServerTest } from "../../utils/ApiEnmServerTest";

describe("Init Setup Methode", () => {

	const apiService = new ApiEnmServerTest('', '');

	test("POST setup > 405", async () => {
		const response = await apiService.testEmptyPost(`/api/setup`);
		expect(response.status).toBe(405);
	});

	test("PUT setup > 405", async () => {
		const response = await apiService.testEmptyPut(`/api/setup`);
		expect(response.status).toBe(405);
	});

	test("First (and only) setup successfully, a further request fails > 204", async () => {
		const response = await apiService.testGet(`/api/setup`);
		expect(response.status).toBe(204);

		const responseSecond = await apiService.testGet(`/api/setup`);
		expect(responseSecond.status).toBe(409);
	});

});
