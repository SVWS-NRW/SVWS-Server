import {describe, expect, test} from "vitest";
import {getApiService} from "../../utils/RequestBuilder.js"

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";

const apiServiceAuth = getApiService('D.Berthold@lmail.de', 'uXkpaRLY', targetUrlENMServer)
const apiServiceNoAuth = getApiService('', '', targetUrlENMServer)

describe("POST Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 204", async () => {
		const response = await apiServiceNoAuth.post(`/api/alive`)
		expect(response.status).toBe(204);
		expect(await response.text()).toBe("")
	});

	test("ankreuzkompetenz > 401", async () => {
		const response = await apiServiceNoAuth.post(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(401);
	});

	test("bemerkungen > 401", async () => {
		const response = await apiServiceNoAuth.post(`/api/bemerkungen`)
		expect(response.status).toBe(401);
	});

	test("clientconfig > 403", async () => {
		const response = await apiServiceNoAuth.post(`/api/clientconfig`)
		expect(response.status).toBe(403);
	});

	test("daten > 403", async () => {
		const response = await apiServiceNoAuth.post(`/api/daten`)
		expect(response.status).toBe(403);
	});

	test("leistung > 401", async () => {
		const response = await apiServiceNoAuth.post(`/api/leistung`)
		expect(response.status).toBe(401);
	});

	test("lernabschnitt > 401", async () => {
		const response = await apiServiceNoAuth.post(`/api/lernabschnitt`)
		expect(response.status).toBe(401);
	});

	test("mode > 403", async () => {
		const response = await apiServiceNoAuth.post(`/api/mode`)
		expect(response.status).toBe(403);
	});

	test("teilleistung > 401", async () => {
		const response = await apiServiceNoAuth.post(`/api/teilleistung`)
		expect(response.status).toBe(401);
	});
})

describe("GET Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 204", async () => {
		const response = await apiServiceNoAuth.get(`/api/alive`)
		expect(response.status).toBe(204);
		expect(await response.text()).toBe("")
	});

	test("ankreuzkompetenz > 403", async () => {
		const response = await apiServiceNoAuth.get(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(403);
	});

	test("bemerkungen > 403", async () => {
		const response = await apiServiceNoAuth.get(`/api/bemerkungen`)
		expect(response.status).toBe(403);
	});

	test("clientconfig > 401", async () => {
		const response = await apiServiceNoAuth.get(`/api/clientconfig`)
		expect(response.status).toBe(401);
	});

	test("daten > 401", async () => {
		const response = await apiServiceNoAuth.get(`/api/daten`)
		expect(response.status).toBe(401);
	});

	test("leistung > 403", async () => {
		const response = await apiServiceNoAuth.get(`/api/leistung`)
		expect(response.status).toBe(403);
	});

	test("lernabschnitt > 403", async () => {
		const response = await apiServiceNoAuth.get(`/api/lernabschnitt`)
		expect(response.status).toBe(403);
	});

	test("mode > 200", async () => {
		const response = await apiServiceNoAuth.get(`/api/mode`)
		expect(response.status).toBe(200);
		expect(await response.text()).toBe("stable")
	});

	test("teilleistung > 403", async () => {
		const response = await apiServiceNoAuth.get(`/api/teilleistung`)
		expect(response.status).toBe(403);
	});

	test("check_smtp > 200", async () => {
		const response = await apiServiceNoAuth.get(`/api/check_smtp`)
		expect(response.status).toBe(200);
	});
})

describe("PUT Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 204", async () => {
		const response = await apiServiceNoAuth.put(`/api/alive`)
		expect(response.status).toBe(204);
		expect(await response.text()).toBe("")
	});

	test("ankreuzkompetenz > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(403);
	});

	test("bemerkungen > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/bemerkungen`)
		expect(response.status).toBe(403);
	});

	test("clientconfig > 401", async () => {
		const response = await apiServiceNoAuth.put(`/api/clientconfig`)
		expect(response.status).toBe(401);
	});

	test("daten > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/daten`)
		expect(response.status).toBe(403);
	});

	test("leistung > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/leistung`)
		expect(response.status).toBe(403);
	});

	test("lernabschnitt > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/lernabschnitt`)
		expect(response.status).toBe(403);
	});

	test("mode > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/mode`)
		expect(response.status).toBe(403);
	});

	test("teilleistung > 403", async () => {
		const response = await apiServiceNoAuth.put(`/api/teilleistung`)
		expect(response.status).toBe(403);
	});
})

describe("GET Requests mit Auth gegen den ENM Server", () => {
	test("clientconfig > 200", async () => {
		const response = await apiServiceAuth.get(`/api/clientconfig`)
		expect(response.status).toBe(200);
	});

	test("daten > 200", async () => {
		const response = await apiServiceAuth.get(`/api/daten`)
		expect(response.status).toBe(200);
	});
})

describe("POST Requests mit Auth gegen den ENM Server", () => {
	test("ankreuzkompetenz > 400", async () => {
		const response = await apiServiceAuth.post(`/api/ankreuzkompetenz`)
		expect(response.status).toBe(400);
	});

	test("bemerkungen > 400", async () => {
		const response = await apiServiceAuth.post(`/api/bemerkungen`)
		expect(response.status).toBe(400);
	});

	test("leistung > 400", async () => {
		const response = await apiServiceAuth.post(`/api/leistung`)
		expect(response.status).toBe(400);
	});

	test("lernabschnitt > 400", async () => {
		const response = await apiServiceAuth.post(`/api/lernabschnitt`)
		expect(response.status).toBe(400);
	});

	test("teilleistung > 400", async () => {
		const response = await apiServiceAuth.post(`/api/teilleistung`)
		expect(response.status).toBe(400);
	});
})

describe("PUT Requests mit Auth gegen den ENM Server", () => {
	test("clientconfig > 400", async () => {
		const response = await apiServiceAuth.put(`/api/clientconfig`)
		expect(response.status).toBe(400);
	});
})
