import type { ApiFile } from '@core';
import { BaseApi } from '@core';

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
	 * Implementierung der GET-Methode isAlive für den Zugriff auf die URL https://{hostname}/status/alive
	 *
	 * Eine Test-Methode zum Prüfen, ob der ENM-Server erreichbar ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Server wurde gefunden
	 *
	 * @returns Der Server ist erreichbar!
	 */
	public async isAlive() : Promise<void> {
		await super.postJSON("/api/alive", "");
		return;
	}

}
