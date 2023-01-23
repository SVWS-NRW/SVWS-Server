import { ApiServer, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
import { ref } from "vue";
import type { Schule } from "./schule/Schule";

/** Der Typ mit allen Apps als App */
export type Apps = {
	schule: Schule;
};

/**
 * Die abstrakte Basisklasse für alle Apps mit den vorgegebenen Methoden und
 * statischen Variablen
 */
export abstract class App {
	/** Eine Instanz der Fächer-OpenAPI-Schnittstelle zur Kommunikation mit dem SVWS-Server */
	static api: ApiServer;

	/**
	 * Initialisiert die Instanz mit den Daten, die bei einer Auswahl aktualisiert
	 * werden müssen
	 *
	 * @returns {Promise<void>}
	 */
	abstract init(): Promise<void>;
	/** Die für alle Apps benötigten Verbindungsinformationen */
	// static connection_config: Configuration;
	/** Das zu verwendende Schema für die aktuelle DB-Verbindung */
	static schema: string;
	/** Ein Objekt mit allen Apps */
	static apps: Apps;
	/** Der aktuell ausgewählte Schuljahresabschnitt */
	static akt_abschnitt: Schuljahresabschnitt;
	/** Das aktuell gewählte Tab in der App */
	public selectedTab = ref(0)

	/**
	 * Setzt alle notwendigen Daten für die Verbindungserstellung zum Server
	 *
	 * @param {string} url URL des Servers
	 * @param {string} username Benutzername
	 * @param {string} password Passwort
	 * @param {string} schema Schema der zu verwendenden DB
	 * @returns {void}
	 */
	static setup(options: {
		url: string;
		username: string;
		password: string;
		schema: string;
	}): void {
		App.schema = options.schema;
		App.api = new ApiServer(
			options.url,
			options.username,
			options.password
		);
	}
}
