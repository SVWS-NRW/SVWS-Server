import { describe, expect, test } from "vitest";
import { getApiService } from "../../utils/RequestBuilder.js";
import { ENMServerConfigElement } from "../../../../svws-webclient/core/src/core/data/enm/ENMServerConfigElement.js";

const targetUrlSVWSAppServer: string = process.env.VITE_APP_targetHost ?? "X";

const targetUrlENMServerFORSvwsApp: string = process.env.VITE_ENM_FOR_SVWS_targetHost ?? 'X';

const CLIENT_SECRET = process.env.VITE_CLIENT_SECRET ?? "clientsecret";

const targetDB = "ENM02A";
let idConnection = 1;

const svwsAppapiService = getApiService('Admin', '', targetUrlSVWSAppServer);

describe("Teste die WeNoM-Verbindung zwischen dem ENM-Server und dem WeNoM-Server zur Initialisierung, Konfiguration und Synchronisation", () => {

	test.sequential("Bestimme die Liste der WeNoM-Verbindungen auf dem SVWS-Server.", async () => {
		const responseGet = await svwsAppapiService.get(`/db/${targetDB}/enm/connections`);
		expect(responseGet.status).toBe(200);
	});

	test.sequential.skip("Lösche die WeNoM-Verbindung falls eine vorhanden ist", async () => {
		// falls eine Verbindung besteht, wird diese entfernt
		const deleteReponse = await svwsAppapiService.delete(`/db/${targetDB}/enm/connection/${idConnection}`);
		expect(deleteReponse.status).toBeOneOf([200, 404]);
	});

	test.sequential("Die WeNoM-Verbindung kann erstellt werden", async () => {
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

	test.sequential("Die aktuelle WeNoM-Verbindung kann gepatched werden", async () => {
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

	test.sequential("Es kann ein Setup für eine WeNoM-Verbindug ausgeführt werden > 200", async () => {
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

	test.sequential("Ein Get auf die WeNoM-Verbindung enthält erwartete Secret Informationen inklusive TLS", async () => {
		const responsePatch = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}`);
		const secretData = await responsePatch.json();
		expect(secretData.clientSecret).toBe(CLIENT_SECRET);
		expect(secretData.serverTLSCert.length).toBeGreaterThan(100);
		expect(responsePatch.status).toBe(200);
	});

	test.sequential("Eine Check über die WeNoM-Verbindindung ist erfolgreich > 200", async () => {
		const responseGetCheck = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/check`);
		expect(responseGetCheck.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Eine Synchronisation der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const responseGetSync = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/synchronize`);
		expect(responseGetSync.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Ein Upload der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const responseGetUpload = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/upload`);
		expect(responseGetUpload.status).toBe(200);
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Ein Download der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const responseGetDownload = await svwsAppapiService.get(`/db/${targetDB}/enm/connection/${idConnection}/download`);
		expect(responseGetDownload.status).toBe(200);
	});

	// Diese Test dient der Initialisierung des ENM-Servers mit einer Konfiguration für die Sperrungen bei Klassen
	test.sequential("Das Setzen der Konfiguration für die Sperrung der Noteneingabe über die WeNoM-Verbindung ist erfolgreich > 204", async () => {
		const configKlasse = {
			"istFehlstundenEingabeKlassenweise": false,
			"spalten": [
				{ "gesperrt": false, "idTeilleistung": 1, "name": "Klassenarbeit 1" },
				{ "gesperrt": false, "idTeilleistung": 4, "name": "Klassenarbeit 2" },
				{ "gesperrt": false, "idTeilleistung": 5, "name": "Mitarbeit 1" },
				{ "gesperrt": false, "idTeilleistung": 6, "name": "Mitarbeit 2" },
				{ "gesperrt": false, "idTeilleistung": 7, "name": "Mitarbeit 3" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Teilnoten" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Quartalsnoten" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Note" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Mahnung" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Fehlstunden" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "FB" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "ASV" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "AUE" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "ZB" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "LELS" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Förderbemerkungen" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "Versetzungsbemerkungen" },
				{ "gesperrt": false, "idTeilleistung": null, "name": "SchulformEmpfehlung" },
			],
			"tsEingabeAb": null,
			"tsEingabeBis": null,
		};
		const configKlassen = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 23, 27].map(id => ({ id, ...configKlasse }));
		const element = new ENMServerConfigElement();
		element.key = "noteneingabe.gesperrt";
		element.value = JSON.stringify(configKlassen);
		element.type = "global";
		const body = ENMServerConfigElement.transpilerToJSON(element);

		const options: RequestInit = { };
		options.headers = { };
		options.headers["Content-Type"] = 'application/json';
		options.body = body;

		const responseGetDownload = await svwsAppapiService.post(`/db/${targetDB}/enm/connection/${idConnection}/config`, options);
		expect(responseGetDownload.status).toBe(200);
	});

});
