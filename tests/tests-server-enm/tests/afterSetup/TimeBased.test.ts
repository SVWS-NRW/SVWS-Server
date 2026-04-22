import { describe, expect, test } from "vitest";
import { ApiEnmServerTest } from "../../utils/ApiEnmServerTest";
import { enmURL } from "../../../utils/APIUtils";

const targetUrlENMServer: string = enmURL;

const apiServiceNoAuth = new ApiEnmServerTest(targetUrlENMServer, '', '');

async function multipleRequest(requestFunction: () => Promise<any>, interval: number, amount: number): Promise<void> {
	for (let i = 0; i < amount; i++) {
		await requestFunction();
		if (i < amount - 1) {
			await new Promise(resolve => setTimeout(resolve, interval));
		}
	}
}

// Dieser Fehlercode wird erwartet, wenn zu viele Anfragen gesendet wurden
const ErrorCodeTimingProtected = 403;

describe.skip(`Zeit basierte Angriffe auf verschiedene POST Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/ankreuzkompetenz`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/bemerkungen`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/bemerkungen`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/clientconfig`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/clientconfig`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/daten`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/daten`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/leistung`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/leistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/lernabschnitt`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/lernabschnitt`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/mode`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/mode`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPost(`/api/teilleistung`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPost(`/api/teilleistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
});

describe.skip(`Zeit basierte Angriffe auf verschiedene GET Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/ankreuzkompetenz`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/bemerkungen`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/bemerkungen`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/clientconfig`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/clientconfig`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/daten`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/daten`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/leistung`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/leistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/lernabschnitt`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/lernabschnitt`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/mode`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/mode`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testGet(`/api/teilleistung`), 100, 10);
		const response = await apiServiceNoAuth.testGet(`/api/teilleistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
});

describe.skip(`Zeit basierte Angriffe auf verschiedene PUT Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/ankreuzkompetenz`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/bemerkungen`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/bemerkungen`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/clientconfig`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/clientconfig`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/daten`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/daten`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/leistung`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/leistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/lernabschnitt`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/lernabschnitt`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/mode`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/mode`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.testEmptyPut(`/api/teilleistung`), 100, 10);
		const response = await apiServiceNoAuth.testEmptyPut(`/api/teilleistung`);
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
});
