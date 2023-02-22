import { List, DBSchemaListeEintrag, ApiServer, LehrerListeEintrag, SchuelerListeEintrag, KlassenListeEintrag, KursListeEintrag, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { ApiConnection } from "./ApiConnection";
import { ApiStatus } from "../components/ApiStatus";

/**
 * Diese Klasse regelt den Zugriff auf die API eines SVWS-Servers bezüglich
 * dem Aufbau und Abbau einer Verbindung und den Zugriff auf API-Methoden.
 * Des Weiteren werden Hilfsmethoden zur Verfügung gestellt, um API-Zugriffe
 * zu erleichtern. Der Status der API ist über das Attribute status verfügbar.
 */
class Api {

	public readonly status: ApiStatus = new ApiStatus();

	/** Die aktuelle Verbindung zum SVWS-Server */
	private readonly conn: ApiConnection = new ApiConnection();

	/** Gibt das Objekt für alle Aufrufe der Server-Schnittstelle des SVWS-Server zurück. */
	get server(): ApiServer {
		return this.conn.api;
	}

	/** Gibt den Namen des Schemas beim SVWS-Server zurück, welches mit dieser Verbindung angesprochen wird */
	get schema(): string {
		return this.conn.schema;
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
	connectTo = async (adresse: string): Promise<List<DBSchemaListeEintrag>> => {
		return await this.conn.connectTo(adresse);
	}

	/**
	 * Authentifiziert den Benutzer mit dem angebenen Benutzernamen und Kennwort bei dem
	 * angebenen Schema.
	 *
	 * @param schema     das Schema bei dem die Authentifizierung vorgenommen wird.
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @returns eine Promise bezüglich des Login-Erfolgs
	 */
	login = async (schema: string, username: string, password: string): Promise<void> => {
		return await this.conn.login(schema, username, password);
	}

	/**
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	logout = async (): Promise<void> => {
		await this.logout();
	}


	/// --- Methoden für den einfachen Api-Zugriff

	/**
	 * Bestimmt eine Map mit der Liste der Schüler für den angegeben Schuljahresabschnitt,
	 * welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @param idSchuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @returns die Map mit den Schülern
	 */
	public async getSchuelerListeAktuell(idSchuljahresabschnitt: number): Promise<Map<number, SchuelerListeEintrag>> {
		const listSchueler = await this.server.getSchuelerFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const l of listSchueler)
			mapSchueler.set(l.id, l);
		return mapSchueler;
	}

	/**
	 * Bestimmt eine Map mit der Liste der Lehrer des aktuellen Schuljahresabschnitts,
	 * welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @returns die Map mit den Lehrern
	 */
	public async getLehrerListeAktuell(): Promise<Map<number, LehrerListeEintrag>> {
		const listLehrer = await this.server.getLehrer(this.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		return mapLehrer;
	}

	/**
	 * Bestimmt eine Map mit der Liste der Klassen für den angegeben Schuljahresabschnitt,
	 * welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @param idSchuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @returns die Map mit den Klassen
	 */
	public async getKlassenListe(idSchuljahresabschnitt: number): Promise<Map<number, KlassenListeEintrag>> {
		const listKlassen = await this.server.getKlassenFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapKlassen = new Map<number, KlassenListeEintrag>();
		for (const k of listKlassen)
			mapKlassen.set(k.id, k)
		return mapKlassen;
	}

	/**
	 * Bestimmt eine Map mit der Liste der Kurse für den angegeben Schuljahresabschnitt,
	 * welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @param idSchuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @returns die Map mit den Kursen
	 */
	public async getKursListe(idSchuljahresabschnitt: number): Promise<Map<number, KursListeEintrag>> {
		const listKurse = await this.server.getKurseFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapKurse: Map<number, KursListeEintrag> = new Map();
		for (const k of listKurse)
			mapKurse.set(k.id, k)
		return mapKurse;
	}


	/**
	 * Bestimmt eine Map mit der Liste der Jahrgänge, welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @returns die Map mit den Jahrgängen
	 */
	public async getJahrgangsListe(): Promise<Map<number, JahrgangsListeEintrag>> {
		// aktualisiere die Jahrgänge und erstelle Map
		const listJahrgaenge = await this.server.getJahrgaenge(this.schema);
		const mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map()
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j)
		return mapJahrgaenge;
	}

}

/** Die Api-Instanz zur Verwendung im SVWS-Client */
export const api = new Api();
