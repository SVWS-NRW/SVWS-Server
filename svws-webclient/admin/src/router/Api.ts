import { ApiConnection } from "~/router/ApiConnection";
import { ApiStatus } from "~/components/ApiStatus";
import { version } from '../../version';
import { githash } from "../../githash";
import type { ApiPrivileged } from "@core/api/ApiPrivileged";
import type { ApiServer } from "@core/api/ApiServer";
import type { ServerMode } from "@core/core/types/ServerMode";

/**
 * Diese Klasse regelt den Zugriff auf die API eines SVWS-Servers bezüglich
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
	get server(): ApiServer {
		return this.conn.api;
	}

	/** Gibt den Namen des Schemas beim SVWS-Server zurück, welches mit dieser Verbindung angesprochen wird */
	get privileged(): ApiPrivileged {
		return this.conn.api_privileged;
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

	/** Gibt an, ob der Benutzer Root-Privilegien hat */
	get hasRootPrivileges() : boolean {
		return this.conn.hasRootPrivileges;
	}

	// Gibt zurück, ob der angemeldete Benutzer der in der SVWS-Server-Konfiguration eingetragene Server-Admin ist oder nicht
	get isServerAdmin() : boolean {
		return this.conn.isServerAdmin;
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
	 * @returns bei Erfolg true
	 */
	connectTo = async (adresse: string): Promise<boolean> => {
		return await this.conn.connectTo(adresse);
	}

	/**
	 * Authentifiziert den Datenbank-Benutzer mit dem angebenen Benutzernamen und Kennwort
	 * bei dem SVWS-Server und prüft, ob der Benutzer die benötigten Privilegien hat.
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
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	logout = async (): Promise<void> => {
		await this.conn.logout();
	}

}

/** Die Api-Instanz zur Verwendung im SVWS-Client */
export const api = new Api();
