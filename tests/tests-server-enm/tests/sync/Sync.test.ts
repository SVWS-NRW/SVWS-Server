import { describe, expect, test } from "vitest";
import { ENMServerConfigElement } from "../../../../svws-webclient/core/src/core/data/enm/ENMServerConfigElement";
import { ArrayList } from "@core/java/util/ArrayList";
import { ApiServer } from "@core/api/ApiServer";

const targetUrlSVWSAppServer: string = process.env.VITE_APP_targetHost ?? "X";
const targetUrlENMServerFORSvwsApp: string = process.env.VITE_ENM_FOR_SVWS_targetHost ?? 'X';
const CLIENT_SECRET = process.env.VITE_CLIENT_SECRET ?? "clientsecret";
const targetDB = "ENM02A";
const idConnection = 1;
const api = new ApiServer(targetUrlSVWSAppServer, 'Admin', '');

describe("Teste die WeNoM-Verbindung zwischen dem ENM-Server und dem WeNoM-Server zur Initialisierung, Konfiguration und Synchronisation", () => {

	test.sequential("Bestimme die Liste der WeNoM-Verbindungen auf dem SVWS-Server.", async () => {
		const responseGet = await api.getENMServerConnections(targetDB);
		expect(responseGet).toBeInstanceOf(ArrayList);
	});

	test.sequential("Die WeNoM-Verbindung kann erstellt werden", async () => {
		const createBody = {
			url: targetUrlENMServerFORSvwsApp,
			bezeichnung: "Neue Verbindung",
			clientID: "1",
			clientSecret: CLIENT_SECRET,
		};
		const conn = await api.addENMServerConnection(createBody, targetDB);
		expect(conn.clientID).toBe("1");
	});

	test.sequential("Die aktuelle WeNoM-Verbindung kann gepatcht werden", async () => {
		const patchBody = {
			url: targetUrlENMServerFORSvwsApp,
			clientSecret: CLIENT_SECRET,
		};
		await api.patchENMServerConnection(patchBody, targetDB, idConnection);
	});

	test.sequential("Es kann ein Setup für eine WeNoM-Verbindug ausgeführt werden > 200", async () => {
		try {
			await api.setupENMServer(targetDB, idConnection);
		} catch {
			await api.patchENMServerConnection({ serverTLSCertIsTrusted: true }, targetDB, idConnection);
		} finally {
			await api.setupENMServer(targetDB, idConnection);
		}
	});

	test.sequential("Ein Get auf die WeNoM-Verbindung enthält erwartete Secret Informationen inklusive TLS", async () => {
		const secretData = await api.getENMServerConnection(targetDB, idConnection);
		expect(secretData.clientSecret).toBe(CLIENT_SECRET);
		expect(secretData.serverTLSCert).toBeTruthy();
	});

	test.sequential("Eine Check über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const res = await api.checkENMServer(targetDB, idConnection);
		expect(res.success).toBeTruthy();
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Eine Synchronisation der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const res = api.synchronizeENMDaten(targetDB, idConnection);
		expect((await res).success).toBeTruthy();
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Ein Upload der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const responseGetUpload = await api.uploadENMDaten(targetDB, idConnection);
		expect(responseGetUpload.success).toBeTruthy();
	});

	// Dieser Test kann fehlschlagen, wenn das Client Secret falsch ist
	test.sequential("Ein Download der ENM-Daten über die WeNoM-Verbindung ist erfolgreich > 200", async () => {
		const responseGetDownload = await api.downloadENMDaten(targetDB, idConnection);
		expect(responseGetDownload.success).toBeTruthy();
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
				{ "gesperrt": false, "idTeilleistung": null, "name": "Quartal" },
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

		const responseGetDownload = await api.setENMServerConfigElement(element, targetDB, idConnection);
		expect(responseGetDownload.success).toBeTruthy();
	});

});
