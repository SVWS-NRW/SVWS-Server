import { describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js";
import { enmURL } from "../../../utils/APIUtils";

const targetUrlENMServer: string = enmURL;

describe("Init Setup Methode", () => {

	const apiService = getApiService('', '', targetUrlENMServer);

	test("POST setup > 405", async () => {
		const response = await apiService.post(`/api/setup`);
		expect(response.status).toBe(405);
	});

	test("PUT setup > 405", async () => {
		const response = await apiService.put(`/api/setup`);
		expect(response.status).toBe(405);
	});

	test("First (and only) setup successfully, a further request fails > 204", async () => {
		const response = await apiService.get(`/api/setup`);
		expect(response.status).toBe(204);

		const responseSecond = await apiService.get(`/api/setup`);
		expect(responseSecond.status).toBe(409);
	});
});
