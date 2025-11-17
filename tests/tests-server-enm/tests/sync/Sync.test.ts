import { describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js";

const targetUrlSVWSAppServer: string = process.env.VITE_APP_targetHost ?? "X";

const targetUrlENMServerFORSvwsApp: string = process.env.VITE_ENM_FOR_SVWS_targetHost ?? 'X';

const CLIENT_SECRET = process.env.VITE_CLIENT_SECRET ?? "clientsecret";

const targetDB = "ENM02A";
let idConnection = 1;

const svwsAppapiService = getApiService('Admin', '', targetUrlSVWSAppServer);

describe("Init and Sync Workflow", () => {

	test.sequential("Bestimme die Liste der Verbindungen zu ENM-Servern.", async () => {
		const responseGet = await svwsAppapiService.get(`/db/${targetDB}/enm/connections`);
		expect(responseGet.status).toBe(200);
	});

	test.sequential.skip("Lösche die aktuelle Einstellung falls eine vorhanden ist", async () => {
		// falls eine Verbindung besteht, wird diese entfernt
		const deleteReponse = await svwsAppapiService.delete(`/db/${targetDB}/enm/connection/${idConnection}`);
		expect(deleteReponse.status).toBeOneOf([200, 404]);
	});

	test.sequential("Die Secret Konfiguration kann created werden", async () => {
		const createBody = {
			url: targetUrlENMServerFORSvwsApp,
			clientID: "1",
			clientSecret: CLIENT_SECRET,
		};

		const responsePost = await svwsAppapiService.post(`/db/${targetDB}/enm/connection/create`, {
			body: JSON.stringify(createBody),
			headers: { "Content-Type": "application/json" },
		});

		const createdData = await responsePost.json();
		idConnection = createdData.id;
		expect(responsePost.status).toBe(201);
	});

	test.sequential("Die aktuelle Konfiguration kann gepatched werden", async () => {
		const patchBody = {
			url: targetUrlENMServerFORSvwsApp,
			clientSecret: CLIENT_SECRET,
		};

		const responsePatch = await svwsAppapiService.patch(`/db/${targetDB}/enm/connection/${idConnection}`, {
			body: JSON.stringify(patchBody),
			headers: { "Content-Type": "application/json" },
		});
		expect(responsePatch.status).toBe(204);
	});

	test.sequential("Get Setup > 200", async () => {
		let responseGet = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/setup`);
		if (responseGet.status === 409) {
			const responsePatch = await svwsAppapiService.patch(`/db/${targetDB}/enm/connection/${idConnection}`, {
				body: JSON.stringify({ serverTLSCertIsTrusted: true }),
				headers: { "Content-Type": "application/json" },
			});
			expect(responsePatch.status).toBe(204);
			responseGet = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/setup`);
		}
		expect(responseGet.status).toBe(200);
	});

	test.sequential("Get auf die aktuelle Konfiguration enthält erwartete Secret Informationen inklusive TLS", async () => {
		const responsePatch = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}`);
		const secretData = await responsePatch.json();
		expect(secretData.clientSecret).toBe(CLIENT_SECRET);
		expect(secretData.serverTLSCert.length).toBeGreaterThan(100);
		expect(responsePatch.status).toBe(200);
	});

	test.sequential("Check Anfrage > 200", async () => {
		const responseGetCheck = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/check`);
		expect(responseGetCheck.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Sync Anfrage > 200", async () => {
		const responseGetSync = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/synchronize`);
		expect(responseGetSync.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Upload Anfrage > 200", async () => {
		const responseGetUpload = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/upload`);
		expect(responseGetUpload.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Download Anfrage > 200", async () => {
		const responseGetDownload = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/download`);
		expect(responseGetDownload.status).toBe(200);
	});

});
