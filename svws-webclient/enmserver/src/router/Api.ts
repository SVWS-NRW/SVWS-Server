import { ApiConnection } from "~/router/ApiConnection";
import type { ApiPendingData} from "~/components/ApiStatus";
import { ApiStatus } from "~/components/ApiStatus";
import { version } from '../../version';
import { githash } from "../../githash";
import type { ApiEnmServer } from "~/ApiEnmServer";
import type { EnmManager } from "~/components/leistungen/EnmManager";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { ENMDaten } from "@core/core/data/enm/ENMDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { ServerMode } from "@core/core/types/ServerMode";
import type { Config } from "~/components/Config";

/**
 * Diese Klasse regelt den Zugriff auf die API eines ENM-Servers bezüglich
 * dem Aufbau und Abbau einer Verbindung und den Zugriff auf API-Methoden.
 * Des Weiteren werden Hilfsmethoden zur Verfügung gestellt, um API-Zugriffe
 * zu erleichtern. Der Status der API ist über das Attribute status verfügbar.
 */
class Api {

	/** Der API-Status */
	public readonly status: ApiStatus = new ApiStatus();

	/** Die aktuelle Verbindung zum SVWS-Server */
	private readonly conn: ApiConnection = new ApiConnection();

	/** Gibt den Modus zurück, in welchem der Server betrieben wird. */
	get mode(): ServerMode {
		return this.conn.mode;
	}

	/** Gibt das Objekt für alle Aufrufe der Server-Schnittstelle des SVWS-Server zurück. */
	get server(): ApiEnmServer {
		return this.conn.api;
	}

	/** Gibt den Hostnamen zurück des SVWS-Servers zurück */
	get hostname() : string {
		return this.conn.hostname;
	}

	/** Gibt den Status zurück, ob der Benutzer authentifiziert wurde */
	get authenticated() : boolean {
		return this.conn.authenticated;
	}

	/** Gibt den Benutzernamen für die Verbindung zum SVWS-Server zurück **/
	get username() : string {
		return this.conn.username;
	}

	/** Gibt die Version des SVWS-Servers zurück */
	get version(): string {
		return version;
	}

	/** Gibt den Githash des aktuellen Commits zurück */
	get githash(): string {
		return githash;
	}

	/**
	 * Setzt den Hostnamen des SVWS-Server für den Verbindungsaufbau.
	 *
	 * @param hostname   der Hostname des SVWS-Server
	 */
	setHostname = (hostname: string): void => {
		return this.conn.setHostname(hostname);
	}

	/**
	 * Versucht eine Verbindung zu der angegebenen Adresse herzustellen.
	 *
	 * @param adresse   die Adresse bestehend aus Hostnamen und ggf. Port des SVWS-Servers
	 *
	 * @returns bei Erfolg die Liste der auf dem Server zur Verfügung stehenden Schemata
	 */
	connectTo = async (adresse: string): Promise<void> => {
		await this.conn.connectTo(adresse);
	}

	/**
	 * Authentifiziert den Benutzer mit dem angebenen Benutzernamen und Kennwort bei dem
	 * angebenen Schema.
	 *
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @returns eine Promise bezüglich des Login-Erfolgs
	 */
	login = async (username: string, password: string): Promise<boolean> => {
		return await this.conn.login(username, password);
	}

	/**
	 * Initialialisiert die Daten, die beim Login geladen werden sollen
	 *
	 * @returns {Promise<boolean>} true beim erfolgreichen Laden der Daten und ansonsten false
	 */
	init = async (): Promise<boolean> => {
		return await this.conn.init();
	}

	/**
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	logout = async (): Promise<void> => {
		await this.conn.logout();
	}

	// Gibt die ENM-Daten zurück
	get daten(): ENMDaten {
		return this.conn.daten;
	}

	// Gibt den Manager für die ENM-Daten des angemeldeten Benutzers zurück
	get manager() : EnmManager {
		return this.conn.manager;
	}

	/**
	 * Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist.
	 *
	 * @returns die Schulform
	 */
	public get schulform(): Schulform {
		if (this.daten.schulform === null)
			throw new DeveloperNotificationException("In den ENM-Daten ist keine Schulform eingetragen.");
		const schulform = Schulform.data().getWertByKuerzel(this.daten.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("In den ENM-Daten ist eine ungültige Schulform eingetragen.");
		return schulform;
	}

	/**
	 * Gibt an, ob es sich bei dem angemeldeten Benutzer um einen adminstrativen Benutzer
	 * handelt oder nicht.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerIstAdmin(): boolean {
		// TODO Überprüfung implementieren, ob der Admin-Benutzer angemeldet ist
		return false;
	}

	/**
	 * Prüft, ob der angemeldete Benutzer eine der angegebenen Kompetenzen hat oder nicht.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen.
	 *
	 * @returns true, falls der Benutzer einer der Kompetenzen hat und ansonsten false
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public benutzerHatEineKompetenz(kompetenzen: Iterable<BenutzerKompetenz>): boolean {
		return true; // Die Kompetenzen aus dem SVWS-Server werden im ENM-Server nicht verwendet -> immer true
	}

	/// --- Methoden für einen Api-Zugriff, welche den API-Status korrekt setzt

	/**
	 * Führt die übergebene Funktion als API-Zugriff aus, um welche der API-Status korrekt gesetzt wird.
	 *
	 * @param func     die auszuführende API-Funktion
	 * @param params   die Parameter für die API-Funktion
	 *
	 * @returns die Rückgabe der API-Funktion
	 */
	public call = <T extends Array<any>, U>(func: (...params: T) => Promise<U>, data?: ApiPendingData) => {
		return async (...params: T): Promise<Awaited<U>> => {
			this.status.start(data);
			try {
				return await func(...params);
			} finally {
				this.status.stop();
			}
		}
	}

	/// --- Die Konfiguration

	/**
	 * Gibt die benutzerspezifische und globale Konfiguration zurück.
	 */
	public get config() : Config {
		return this.conn.config;
	}

	/**
	 * Gibt die benutzerspezifische und globale nicht persistierte Konfiguration zurück.
	 */
	public get nonPersistentConfig() : Config {
		return this.conn.nonPersistentConfig;
	}

}

/** Die Api-Instanz zur Verwendung im SVWS-Client */
export const api = new Api();
