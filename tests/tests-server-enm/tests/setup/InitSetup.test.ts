import { describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js"

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";

describe("Init Setup Methode", () => {

	const apiService = getApiService('', '', targetUrlENMServer)

	test("POST setup > 403", async () => {
		const response = await apiService.post(`/api/setup`)
		expect(response.status).toBe(403);
	});

	test("PUT setup > 403", async () => {
		const response = await apiService.put(`/api/setup`)
		expect(response.status).toBe(403);
	});

	test("First (and only) setup successfully, a further request fails > 204", async () => {
		const response = await apiService.get(`/api/setup`)
		expect(response.status).toBe(204);

		const responseSecond = await apiService.get(`/api/setup`)
		expect(responseSecond.status).toBe(409);
	});
})
