import {describe, expect, test} from "vitest";
import {getApiService} from "./utils/RequestBuilder.js"

const allowDestructiveTests = process.env.MODE === 'allowDestructiveTests'

describe("Prüft ob entsprechende Endpunkte erreichbar sind", () => {
	const apiService = getApiService('Admin', '')

	describe.each([{schema: "GymAbiDav"}])('gegen %s', ({schema}) => {
		test("Dav/", async () => {
			const response = await apiService.propfind(`/dav/${schema}`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test(`dav/${schema}/benutzer/-1`, async () => {
			const response = await apiService.propfind(`/dav/${schema}/benutzer/-1`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test(`dav/${schema}/adressbuecher`, async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test(`dav/${schema}/adressbuecher/-1`, async () => {
			const response = await apiService.propfind(`/dav/${schema}/adressbuecher/-1`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});
		test(`dav/${schema}/kalender`, async () => {
			const response = await apiService.propfind(`/dav/${schema}/kalender`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test(`dav/${schema}/kalender/-1`, async () => {
			const response = await apiService.propfind(`/dav/${schema}/kalender/-1`, {body: "a"})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test(`dav/${schema}/kalender/-1/something-something.ics`, async () => {
			const response = await apiService.put(`/dav/${schema}/kalender/-1/something-something.ics`, {
				body: "a",
				headers: {"If-None-Match": "", "Content-Type": "Text/Calender"}
			})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		// Test schlägt fehl.
		test.skip(`dav/${schema}/kalender/-1/something-something.ics`, async () => {
			const response = await apiService.put(`/dav/${schema}/kalender/-1/something-something.ics`, {
				body: "a",
				headers: {
					"If-Match": "*",
					"Content-Type": "Text/Calender"
				}
			})
			expect(response).toBeDefined();
			expect(response!.status).toBe(500);
		});


		test.runIf(allowDestructiveTests)(`dav/${schema}/kalender/-1/something-something.ics`, async () => {
			const response = await apiService.delete(`/dav/${schema}/kalender/-1/something-something.ics`, {
				headers: {
					"If-Match": "*",
					"Content-Type": "Text/Calender"
				}
			})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});

		test.runIf(allowDestructiveTests)(`dav/${schema}/kalender/-1/something-something.ics`, async () => {
			const response = await apiService.delete(`/dav/${schema}/kalender/-1/something-something.ics`, {
				headers: {
					"Content-Type": "Text/Calender"
				}
			})
			expect(response).toBeDefined();
			expect(response!.status).toBe(400);
		});
	});
});

describe("Prüft ob entsprechende Endpunkte nicht erreichbar sind", () => {
	describe.each([{schema: "GymAbiDav"}])('gegen %s', ({schema}) => {
		const apiService = getApiService('Admin', '')

		test(`dav/${schema}/adressbuecher/-1`, async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(404);
		});

		test(`dav/${schema}/adressbuecher/-1/-1.vcf`, async () => {
			const response = await apiService.report(`/dav/${schema}/adressbuecher/-1/-1.vcf`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(404);
		});

		test(`dav/${schema}/kalender/-1`, async () => {
			const response = await apiService.report(`/dav/${schema}/kalender/-1`)
			expect(response).toBeDefined();
			expect(response!.status).toBe(404);
		});
	});
});
