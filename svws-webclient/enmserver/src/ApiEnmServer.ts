import { BaseApi, type ApiFile } from "@core/api/BaseApi";
import { ENMLeistung } from "@core/core/data/enm/ENMLeistung";

export class ApiEnmServer extends BaseApi {

	/**
	 * Erstellt eine neue API mit der übergebenen Konfiguration.
	 *
	 * @param {string} url - die URL des Servers: Alle Pfadangaben sind relativ zu dieser URL
	 * @param {string} username - der Benutzername für den API-Zugriff
	 * @param {string} password - das Kennwort des Benutzers für den API-Zugriff
	 */
	public constructor(url : string, username : string, password : string) {
		super(url, username, password);
	}

	/**
	 * Implementierung der GET-Methode getLehrerENMDaten für den Zugriff auf die URL https://{hostname}/api/daten
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) für den aktuell angemeldeten Lehrer-Benutzer aus der Datenbank
	 * und liefert diese GZip-komprimiert zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierte ENM-JSON-Datei
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 401: Die Authentifzierung des Benutzers ist fehlgeschlagen
	 *   Code 404: Es wurden nicht alle benötigten ENM-Daten gefunden.
	 *   Code 500: Ein interner Fehler im ENM-Server ist aufgetreten.
	 *
	 * @returns die GZip-komprimierte ENM-JSON-Datei
	 */
	public async getLehrerENMDaten() : Promise<ApiFile> {
		return await super.getOctetStream("/api/daten");
	}

	/**
	 * Implementierung der POST-Methode isAlive für den Zugriff auf die URL https://{hostname}/api/alive
	 *
	 * Eine Test-Methode zum Prüfen, ob der ENM-Server erreichbar ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Server wurde gefunden
	 */
	public async isAlive() : Promise<void> {
		await super.postJSON("/api/alive", "");
		return;
	}

	/**
	 * Implementierung der POST-Methode patchENMLeistung für den Zugriff auf die URL https://{hostname}/api/leistung
	 *
	 * Die Methode erlaubt das Patchen von ENM-Leistungsdaten.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert
	 *   Code 403: Ein Patch der Leistungsdaten ist durch den aktuelle angemeldeten Lehrer nicht erlaubt
	 *   Code 404: Die Leistungsdaten zu der übergebenen ID wurden nicht gefunden.
	 *
	 * @param {Partial<ENMLeistung>} patch   die zu patchenden Attribut der Leistungsdaten
	 */
	public async patchENMLeistung(patch: Partial<ENMLeistung>): Promise<void> {
		await super.postJSON("/api/leistung", ENMLeistung.transpilerToJSONPatch(patch));
		return;
	}

}
