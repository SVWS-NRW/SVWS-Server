import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerConfig } from "@core/core/data/benutzer/BenutzerConfig";
import { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import { ENMv2LeistungBemerkungen } from "@core/core/data/enm/v2/ENMv2LeistungBemerkungen";
import { ENMv2Lernabschnitt } from "@core/core/data/enm/v2/ENMv2Lernabschnitt";
import { ENMv2SchuelerAnkreuzkompetenz } from "@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz";
import { ENMv2Teilleistung } from "@core/core/data/enm/v2/ENMv2Teilleistung";
import { ServerMode } from "@core/core/types/ServerMode";
import { BaseApi } from "./BaseApi";
import { ENMv2Daten } from "@core/core/data/enm/v2/ENMv2Daten";

export interface ApiLoginData {
	idLehrer: number;
	isTotp: boolean;
	setup: {
		secret: string, issuer: string, account: string
	} | null;
}

export class ApiEnmServer extends BaseApi {

	/**
	 * Erstellt eine neue API mit der übergebenen Konfiguration.
	 *
	 * @param {string} url - die URL des Servers: Alle Pfadangaben sind relativ zu dieser URL
	 * @param {string} username - der Benutzername für den API-Zugriff
	 * @param {string} password - das Kennwort des Benutzers für den API-Zugriff
	 */
	public constructor(url: string, username: string, password: string) {
		super(url, username, password);
	}

	/**
	 * Führt den Server-Login durch. Hierzu wird zunächst ein Basic-Auth bei der Connection verwendet.
	 * War der Aufruf erfolgreich, so wird der Json-Web-Token zurückgegeben und für die weiteren API-Zugriffe
	 * verwendet.
	 *
	 * @returns ein Objekt mit der ID des angemeldeten Lehrers und ggf. weiteren Informationen für die Erstanmeldung eines Benutzers
	 */
	public async login(): Promise<ApiLoginData> {
		const response = await super.postTextBased("/api/login", 'application/json', 'application/json', null);

		const isTotp = (response.status === 202);

		const data = JSON.parse(response.data);
		const setup = data.setup ?? null;
		const jwt = (setup === null) ? data : data.token;
		this.setBearerToken(jwt.token);

		return { idLehrer: jwt.id, isTotp, setup };
	}

	/**
	 * Schließt den Login-Vorgang durch Überprüfung des TOTP-Codes ab.
	 *
	 * @param {string} code - der 6-stellige TOTP-Code
	 *
	 * @returns die ID des angemeldeten Lehrers
	 */
	public async loginTotp(code: string): Promise<number> {
		const body = JSON.stringify({ code: code });
		const response = await super.postTextBased("/api/login_totp", 'application/json', 'application/json', body);

		const jwt = JSON.parse(response.data);

		this.setBearerToken(jwt.token);
		return jwt.id;
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
	public async getServerMode(): Promise<ServerMode> {
		const response = await super.getTextBased("/api/mode", "*/*");
		return ServerMode.getByText(response.data);
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
	public async getSchulform(): Promise<Schulform | null> {
		const response = await super.getTextBased("/api/schulform", "*/*");
		return Schulform.data().getWertByKuerzel(response.data);
	}

	/**
	 * Implementierung der GET-Methode getLehrerENMDaten für den Zugriff auf die URL https://{hostname}/api/daten
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) für den aktuell angemeldeten Lehrer-Benutzer aus der Datenbank
	 * und liefert diese GZip-komprimiert zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten ENM-Daten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMv2Daten
	 *   Code 401: Die Authentifzierung des Benutzers ist fehlgeschlagen
	 *   Code 404: Es wurden nicht alle benötigten ENM-Daten gefunden.
	 *   Code 500: Ein interner Fehler im ENM-Server ist aufgetreten.
	 *
	 * @returns die GZip-komprimierte ENM-JSON-Datei
	 */
	public async getLehrerENMDaten(): Promise<ENMv2Daten> {
		const response = await super.getTextBased('/api/daten', 'application/json');
		return ENMv2Daten.transpilerFromJSON(response.data);
	}

	/**
	 * Implementierung der GET-Methode isAlive für den Zugriff auf die URL https://{hostname}/api/alive
	 *
	 * Eine Test-Methode zum Prüfen, ob der ENM-Server erreichbar ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Server wurde gefunden
	 */
	public async isAlive(): Promise<void> {
		await super.getTextBased("/api/alive", '*/*');
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
	 * @param {Partial<ENMv2Leistung>} patch   die zu patchenden Attribut der Leistungsdaten
	 */
	public async patchENMLeistung(patch: Partial<ENMv2Leistung>): Promise<void> {
		await super.postTextBased("/api/leistung", 'application/json', '*/*', ENMv2Leistung.transpilerToJSONPatch(patch));
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
	 * @param {Partial<ENMv2Lernabschnitt>} patch   die zu patchenden Attribut der Lernabschnittsdaten
	 */
	public async patchENMSchuelerLernabschnitt(patch: Partial<ENMv2Lernabschnitt>): Promise<void> {
		await super.postTextBased("/api/lernabschnitt", 'application/json', '*/*', ENMv2Lernabschnitt.transpilerToJSONPatch(patch));
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
	 * @param {Partial<ENMv2LeistungBemerkungen>} patch   die zu patchenden Attribut der Leistungsbemerkungen
	 */
	public async patchENMSchuelerBemerkungen(idSchueler: number, patch: Partial<ENMv2LeistungBemerkungen>): Promise<void> {
		const body = `{ "id": ${idSchueler}, "patch": ${ENMv2LeistungBemerkungen.transpilerToJSONPatch(patch)}}`;
		await super.postTextBased("/api/bemerkungen", 'application/json', '*/*', body);
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
	 * @param {Partial<ENMv2Teilleistung>} patch   die zu patchenden Attribut der Teilleistung
	 */
	public async patchENMTeilleistung(patch: Partial<ENMv2Teilleistung>): Promise<void> {
		await super.postTextBased("/api/teilleistung", 'application/json', '*/*', ENMv2Teilleistung.transpilerToJSONPatch(patch));
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
	 * @param {Partial<ENMv2SchuelerAnkreuzkompetenz>} patch   die zu patchenden Attribut der Ankreuzkompetenz
	 */
	public async patchENMSchuelerAnkreuzkompetenzen(patch: Partial<ENMv2SchuelerAnkreuzkompetenz>): Promise<void> {
		await super.postTextBased("/api/ankreuzkompetenz", 'application/json', '*/*', ENMv2SchuelerAnkreuzkompetenz.transpilerToJSONPatch(patch));
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
	public async getClientConfig(): Promise<BenutzerConfig> {
		const response = await super.getTextBased("/api/clientconfig", "*/*");
		return BenutzerConfig.transpilerFromJSON(response.data);
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
	public async setClientConfigUserKey(data: string | null, key: string): Promise<void> {
		const body = `{ "key": ${JSON.stringify(key)}, "value": ${JSON.stringify(data)} }`;
		await super.putTextBased("/api/clientconfig", 'application/json', body);
	}

}
