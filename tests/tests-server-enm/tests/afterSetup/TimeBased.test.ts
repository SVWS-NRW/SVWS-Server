import { describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js"
import { enmURL } from "../../../utils/APIUtils";

const targetUrlENMServer: string = enmURL;

const apiServiceNoAuth = getApiService('', '', targetUrlENMServer)

const multipleRequest = async (
	requestFunction: () => Promise<any>,
	interval: number,
	amount: number
): Promise<void> => {
	for (let i = 0; i < amount; i++) {
		await requestFunction();
		if (i < amount - 1) {
			await new Promise(resolve => setTimeout(resolve, interval));
		}
	}
};

// Dieser Fehlercode wird erwartet, wenn zu viele Anfragen gesendet wurden
const ErrorCodeTimingProtected = 403

describe.skip(`Zeit basierte Angriffe auf verschiedene POST Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/ankreuzkompetenz`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/bemerkungen`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/bemerkungen`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/clientconfig`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/clientconfig`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/daten`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/daten`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/leistung`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/leistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/lernabschnitt`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/lernabschnitt`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/mode`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/mode`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.post(`/api/teilleistung`), 100, 10)
		const response = await apiServiceNoAuth.post(`/api/teilleistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
})

describe.skip(`Zeit basierte Angriffe auf verschiedene GET Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/ankreuzkompetenz`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/bemerkungen`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/bemerkungen`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/clientconfig`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/clientconfig`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/daten`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/daten`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/leistung`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/leistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/lernabschnitt`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/lernabschnitt`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/mode`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/mode`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.get(`/api/teilleistung`), 100, 10)
		const response = await apiServiceNoAuth.get(`/api/teilleistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
})

describe.skip(`Zeit basierte Angriffe auf verschiedene PUT Endpunkte führen zu ${ErrorCodeTimingProtected}`, () => {
	test(`ankreuzkompetenz > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/ankreuzkompetenz`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`bemerkungen > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/bemerkungen`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/bemerkungen`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`clientconfig > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/clientconfig`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/clientconfig`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`daten > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/daten`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/daten`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`leistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/leistung`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/leistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`lernabschnitt > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/lernabschnitt`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/lernabschnitt`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`mode > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/mode`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/mode`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});

	test(`teilleistung > ${ErrorCodeTimingProtected}`, async () => {
		await multipleRequest(() => apiServiceNoAuth.put(`/api/teilleistung`), 100, 10)
		const response = await apiServiceNoAuth.put(`/api/teilleistung`)
		expect(response.status).toBe(ErrorCodeTimingProtected);
	});
})
