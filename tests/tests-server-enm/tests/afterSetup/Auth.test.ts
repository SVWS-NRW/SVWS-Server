import { beforeAll, describe, expect, test } from "vitest";
import { ApiEnmServerTest } from "../../utils/ApiEnmServerTest";
import { enmURL } from "../../../utils/APIUtils";

const targetUrlENMServer: string = enmURL;

const apiServiceAuth = new ApiEnmServerTest(targetUrlENMServer, 'D.Berthold@lmail.de', 'uXkpaRLY');
const apiServiceNoAuth = new ApiEnmServerTest(targetUrlENMServer, '', '');

beforeAll(async () => {
	await apiServiceAuth.login();
});

describe("POST Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/alive`);
		expect(response.status).toBe(405);
	});

	test("ankreuzkompetenz > 401", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(401);
	});

	test("bemerkungen > 401", async () => {
		try {

			const response = await apiServiceNoAuth.testEmptyPost(`/api/bemerkungen`);
			expect(response.status).toBe(401);
		} catch (e) {
			console.log(e);
		}
	});

	test("clientconfig > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/clientconfig`);
		expect(response.status).toBe(405);
	});

	test("daten > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/daten`);
		expect(response.status).toBe(405);
	});

	test("leistung > 401", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/leistung`);
		expect(response.status).toBe(401);
	});

	test("lernabschnitt > 401", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/lernabschnitt`);
		expect(response.status).toBe(401);
	});

	test("mode > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/mode`);
		expect(response.status).toBe(405);
	});

	test("teilleistung > 401", async () => {
		const response = await apiServiceNoAuth.testEmptyPost(`/api/teilleistung`);
		expect(response.status).toBe(401);
	});
});

describe("GET Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 204", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/alive`);
		expect(response.status).toBe(204);
	});

	test("ankreuzkompetenz > 405", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(405);
	});

	test("bemerkungen > 405", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/bemerkungen`);
		expect(response.status).toBe(405);
	});

	test("clientconfig > 401", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/clientconfig`);
		expect(response.status).toBe(401);
	});

	test("daten > 401", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/daten`);
		expect(response.status).toBe(401);
	});

	test("leistung > 405", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/leistung`);
		expect(response.status).toBe(405);
	});

	test("lernabschnitt > 405", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/lernabschnitt`);
		expect(response.status).toBe(405);
	});

	test("mode > 200", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/mode`);
		expect(response.status).toBe(200);
		expect(await response.text()).toBe("stable");
	});

	test("teilleistung > 405", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/teilleistung`);
		expect(response.status).toBe(405);
	});

	test("check_smtp > 200", async () => {
		const response = await apiServiceNoAuth.testGet(`/api/check_smtp`);
		expect(response.status).toBe(200);
	});
});

describe("PUT Requests ohne Auth gegen den ENM Server", () => {
	test("alive > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/alive`);
		expect(response.status).toBe(405);
	});

	test("ankreuzkompetenz > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(405);
	});

	test("bemerkungen > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/bemerkungen`);
		expect(response.status).toBe(405);
	});

	test("clientconfig > 401", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/clientconfig`);
		expect(response.status).toBe(401);
	});

	test("daten > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/daten`);
		expect(response.status).toBe(405);
	});

	test("leistung > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/leistung`);
		expect(response.status).toBe(405);
	});

	test("lernabschnitt > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/lernabschnitt`);
		expect(response.status).toBe(405);
	});

	test("mode > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/mode`);
		expect(response.status).toBe(405);
	});

	test("teilleistung > 405", async () => {
		const response = await apiServiceNoAuth.testEmptyPut(`/api/teilleistung`);
		expect(response.status).toBe(405);
	});
});

describe("GET Requests mit Auth gegen den ENM Server", () => {
	test("clientconfig > 200", async () => {
		const response = await apiServiceAuth.testGet(`/api/clientconfig`);
		expect(response.status).toBe(200);
	});

	test("daten > 200", async () => {
		const response = await apiServiceAuth.testGet(`/api/daten`);
		expect(response.status).toBe(200);
	});
});

describe("POST Requests mit Auth gegen den ENM Server", () => {
	test("ankreuzkompetenz > 400", async () => {
		const response = await apiServiceAuth.testEmptyPost(`/api/ankreuzkompetenz`);
		expect(response.status).toBe(400);
	});

	test("bemerkungen > 400", async () => {
		const response = await apiServiceAuth.testEmptyPost(`/api/bemerkungen`);
		expect(response.status).toBe(400);
	});

	test("leistung > 400", async () => {
		const response = await apiServiceAuth.testEmptyPost(`/api/leistung`);
		expect(response.status).toBe(400);
	});

	test("lernabschnitt > 400", async () => {
		const response = await apiServiceAuth.testEmptyPost(`/api/lernabschnitt`);
		expect(response.status).toBe(400);
	});

	test("teilleistung > 400", async () => {
		const response = await apiServiceAuth.testEmptyPost(`/api/teilleistung`);
		expect(response.status).toBe(400);
	});
});

describe("PUT Requests mit Auth gegen den ENM Server", () => {
	test("clientconfig > 400", async () => {
		const response = await apiServiceAuth.testEmptyPut(`/api/clientconfig`);
		expect(response.status).toBe(400);
	});
});
