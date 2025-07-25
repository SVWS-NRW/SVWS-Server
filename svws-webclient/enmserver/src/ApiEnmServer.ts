import { BaseApi, type ApiFile } from "@core/api/BaseApi";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerConfig } from "@core/core/data/benutzer/BenutzerConfig";
import { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import { ENMLeistungBemerkungen } from "@core/core/data/enm/ENMLeistungBemerkungen";
import { ENMLernabschnitt } from "@core/core/data/enm/ENMLernabschnitt";
import { ENMSchuelerAnkreuzkompetenz } from "@core/core/data/enm/ENMSchuelerAnkreuzkompetenz";
import { ENMTeilleistung } from "@core/core/data/enm/ENMTeilleistung";
import { ServerMode } from "@core/core/types/ServerMode";

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
	 * Implementierung der GET-Methode getServerMode für den Zugriff auf die URL https://{hostname}/api/mode
	 *
	 * Liest den Modus aus, in dem der Server betrieben wird.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Ein String, mit dem Server-Mode. Dieser wird direkt in das zugehörige Objekt umgewandelt.
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: ServerMode
	 *   Code 401: Die Authentifzierung des Benutzers ist fehlgeschlagen
	 *   Code 500: Ein interner Fehler im ENM-Server ist aufgetreten.
	 *
	 * @returns der Server-Mode
	 */
	public async getServerMode() : Promise<ServerMode> {
		return ServerMode.getByText(await super.getTextBased("/api/mode", "*/*"));
	}

	/**
	 * Implementierung der GET-Methode getSchulform für den Zugriff auf die URL https://{hostname}/api/schulform
	 *
	 * Liest die Schulform aus, für welche die Daten auf dem Server vorliegen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Ein String, mit der Schulform. Diese wird direkt in das zugehörige Objekt umgewandelt.
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: Schulform
	 *   Code 401: Die Authentifzierung des Benutzers ist fehlgeschlagen
	 *   Code 500: Ein interner Fehler im ENM-Server ist aufgetreten.
	 *
	 * @returns die Schulform oder null, falls der Server ein ungültiges Schulform-Kürzel geliefert hat
	 */
	public async getSchulform() : Promise<Schulform | null> {
		return Schulform.data().getWertByKuerzel(await super.getTextBased("/api/schulform", "*/*"));
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
		return await super.getBinary("/api/daten", "*/*");
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
		await super.postTextBased("/api/alive", 'application/json', '*/*', "");
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
		await super.postTextBased("/api/leistung", 'application/json', '*/*', ENMLeistung.transpilerToJSONPatch(patch));
		return;
	}

	/**
	 * Implementierung der POST-Methode patchENMSchuelerLernabschnitt für den Zugriff auf die URL https://{hostname}/api/lernabschnitt
	 *
	 * Die Methode erlaubt das Patchen von ENM-Lernabschnittsdaten.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert
	 *   Code 403: Ein Patch der Lernabschnittsdaten ist durch den aktuelle angemeldeten Lehrer nicht erlaubt
	 *   Code 404: Die Lernabschnittsdaten zu der übergebenen ID wurden nicht gefunden.
	 *
	 * @param {Partial<ENMLernabschnitt>} patch   die zu patchenden Attribut der Lernabschnittsdaten
	 */
	public async patchENMSchuelerLernabschnitt(patch: Partial<ENMLernabschnitt>): Promise<void> {
		await super.postTextBased("/api/lernabschnitt", 'application/json', '*/*', ENMLernabschnitt.transpilerToJSONPatch(patch));
		return;
	}

	/**
	 * Implementierung der POST-Methode patchENMSchuelerBemerkungen für den Zugriff auf die URL https://{hostname}/api/bemerkungen
	 *
	 * Die Methode erlaubt das Patchen von ENM-Leistungsbemerkungen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert
	 *   Code 403: Ein Patch der Leistungsbemerkungen ist durch den aktuelle angemeldeten Lehrer nicht erlaubt
	 *   Code 404: Die Leistungsbemerkungen zu der übergebenen ID wurden nicht gefunden.
	 *
	 * @param {number} idSchueler                       die IDs des Schülers, dessen Leistungsbemerkungen angepasst werden sollen
	 * @param {Partial<ENMLeistungBemerkungen>} patch   die zu patchenden Attribut der Leistungsbemerkungen
	 */
	public async patchENMSchuelerBemerkungen(idSchueler: number, patch: Partial<ENMLeistungBemerkungen>): Promise<void> {
		const body = `{ "id": ${idSchueler}, "patch": ${ENMLeistungBemerkungen.transpilerToJSONPatch(patch)}}`;
		await super.postTextBased("/api/bemerkungen", 'application/json', '*/*', body);
		return;
	}

	/**
	 * Implementierung der POST-Methode patchENMTeilleistung für den Zugriff auf die URL https://{hostname}/api/teilleistung
	 *
	 * Die Methode erlaubt das Patchen von ENM-Teilleistungen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert
	 *   Code 403: Ein Patch der Teilleistung ist durch den aktuelle angemeldeten Lehrer nicht erlaubt
	 *   Code 404: Die Teilleistung zu der übergebenen ID wurden nicht gefunden.
	 *
	 * @param {Partial<ENMTeilleistung>} patch   die zu patchenden Attribut der Teilleistung
	 */
	public async patchENMTeilleistung(patch: Partial<ENMTeilleistung>): Promise<void> {
		await super.postTextBased("/api/teilleistung", 'application/json', '*/*', ENMTeilleistung.transpilerToJSONPatch(patch));
		return;
	}

	/**
	 * Implementierung der POST-Methode patchENMSchuelerAnkreuzkompetenzen für den Zugriff auf die URL https://{hostname}/api/ankreuzkompetenz
	 *
	 * Die Methode erlaubt das Patchen von ENM-Schüler-Ankreuzkompetenzen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert
	 *   Code 403: Ein Patch der Ankreuzkompetenz ist durch den aktuelle angemeldeten Lehrer nicht erlaubt
	 *   Code 404: Die Ankreuzkompetenz zu der übergebenen ID wurden nicht gefunden.
	 *
	 * @param {Partial<ENMSchuelerAnkreuzkompetenz>} patch   die zu patchenden Attribut der Ankreuzkompetenz
	 */
	public async patchENMSchuelerAnkreuzkompetenzen(patch: Partial<ENMSchuelerAnkreuzkompetenz>): Promise<void> {
		await super.postTextBased("/api/ankreuzkompetenz", 'application/json', '*/*', ENMSchuelerAnkreuzkompetenz.transpilerToJSONPatch(patch));
		return;
	}


	/**
	 * Implementierung der GET-Methode getClientConfig für den Zugriff auf die URL https://{hostname}/api/clientconfig
	 *
	 * Liest die Konfigurationseinträge aus.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Key-Value-Paare der Konfigurationseinträge als Liste
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerConfig
	 *
	 * @returns Die Key-Value-Paare der Konfigurationseinträge als Liste
	 */
	public async getClientConfig() : Promise<BenutzerConfig> {
		return BenutzerConfig.transpilerFromJSON(await super.getTextBased("/api/clientconfig", "*/*"));
	}

	/**
	 * Implementierung der PUT-Methode setClientConfigUserKey für den Zugriff auf die URL https://{hostname}/api/clientconfig
	 *
	 * Schreibt den Konfigurationseintrag für den angebenen Schlüsselwert in die benutzerspezifische Konfiguration.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} key - der Pfad-Parameter key
	 */
	public async setClientConfigUserKey(data : string | null, key : string) : Promise<void> {
		const body = `{ "key": ${JSON.stringify(key)}, "value": ${JSON.stringify(data)} }`;
		return super.putJSON("/api/clientconfig", body);
	}

}
