import {beforeEach, describe, expect, test} from "vitest";
import {getApiService} from "../../utils/RequestBuilder.js"
import {parse} from "../../utils/ENMApiDataParser.js";
import {resolve} from "node:path";

const targetUrlENMServer: string = process.env.VITE_ENM_targetHost ?? "https://localhost";

const targetUrlSVWSAppServer: string = process.env.VITE_APP_targetHost ?? "https://localhost:5050";

const targetUrlENMServerFORSvwsApp: string = process.env.VITE_ENM_FOR_SVWS_targetHost ?? 'https://env-server--dd0794';

const CLIENT_SECRET = process.env.VITE_CLIENT_SECRET ?? "clientsecret";

const targetDB = "EnmA"

const svwsAppapiService = getApiService('Admin', '', targetUrlSVWSAppServer)

function delay(ms: number) {
	return new Promise( resolve => setTimeout(resolve, ms) );
}

describe("Init and Sync Workflow", () => {
	// Verarbeitungszeit zwischen den Anfragen erhöhen
	beforeEach(async () => {
		await delay(1000);
	})

	test("Lösche die aktuelle Einstellung falls eine vorhanden ist", async () => {
		// falls eine Verbindung besteht, wird diese entfernt
		const deleteReponse = await svwsAppapiService.delete(`/db/${targetDB}/oauth/secrets/1`)
		expect(deleteReponse.status).toBeOneOf([200, 404]);
	})

	test("Die Secret Konfiguration kann created werden", async () => {
		const createBody = {
			id: 1,
			authServer: targetUrlENMServerFORSvwsApp,
			clientID: "1",
			clientSecret: CLIENT_SECRET,
		}

		const responsePost = await svwsAppapiService.post(`/db/${targetDB}/oauth/secrets/create`, {
			body: JSON.stringify(createBody),
			headers: {"Content-Type": "application/json"},
		})

		expect(responsePost.status).toBe(201);
	})

	test("Die aktuelel Konfiguration kann gepatched werden", async () => {
		const patchBody = {
			authServer: targetUrlENMServerFORSvwsApp,
			clientSecret: CLIENT_SECRET,
		}

		const responsePatch = await svwsAppapiService.patch(`/db/${targetDB}/oauth/secrets/1`, {
			body: JSON.stringify(patchBody),
			headers: {"Content-Type": "application/json"},
		})
		expect(responsePatch.status).toBe(204);
	})

	test("Get Setup > 200", async () => {
		const responseGet = await svwsAppapiService.get(`/db/${targetDB}/enm/setup`)
		expect(responseGet.status).toBe(200);
	})

	test("Get auf die aktuelle Konfiguration enthält erwartete Secret Informationen inklusive TLS", async () => {
		const responsePatch = await svwsAppapiService.get(`/db/${targetDB}/oauth/secrets/1`)
		const secretData = await responsePatch.json()
		expect(secretData.clientSecret).toBe(CLIENT_SECRET);
		expect(secretData.tlsCert.length).toBeGreaterThan(100);
		expect(responsePatch.status).toBe(200);
	})

	test("Check Anfrage > 200", async () => {
		const responseGetCheck = await svwsAppapiService.get(`/db/${targetDB}/enm/check`)
		expect(responseGetCheck.status).toBe(200);
	})

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test("Sync Anfrage > 200", async () => {
		const responseGetSync = await svwsAppapiService.get(`/db/${targetDB}/enm/synchronize`)
		expect(responseGetSync.status).toBe(200);
	})

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test("Upload Anfrage > 200", async () => {
		const responseGetUpload = await svwsAppapiService.get(`/db/${targetDB}/enm/upload`)
		expect(responseGetUpload.status).toBe(200);
	})

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test("Download Anfrage > 200", async () => {
		const responseGetDownload = await svwsAppapiService.get(`/db/${targetDB}/enm/download`)
		expect(responseGetDownload.status).toBe(200);
	})
})
