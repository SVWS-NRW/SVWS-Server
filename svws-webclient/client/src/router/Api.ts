import { ApiConnection } from "~/router/ApiConnection";
import type { ApiPendingData } from "~/components/ApiStatus";
import { ApiStatus } from "~/components/ApiStatus";
import type { ApiExternal } from "@core/api/ApiExternal";
import type { ApiServer } from "@core/api/ApiServer";
import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { DBSchemaListeEintrag } from "@core/core/data/db/DBSchemaListeEintrag";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { List } from "@core/java/util/List";
import { version } from "@version";
import { githash } from "@githash";

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

	/** Gibt das Objekt für alle Aufrufe der Server-Schnittstelle des SVWS-Server zurück. */
	get server(): ApiServer {
		return this.conn.api;
	}

	/** Gibt das Objekt für alle Aufrufe der externen Schnittstellen des SVWS-Server zurück. */
	get external(): ApiExternal {
		return this.conn.apiExternal;
	}

	/** Gibt den Namen des Schemas beim SVWS-Server zurück, welches mit dieser Verbindung angesprochen wird */
	get schema(): string {
		return this.conn.schema;
	}

	/** Gibt die Version des SVWS-Servers zurück */
	get version(): string {
		return version;
	}

	/** Gibt den Githash des aktuellen Commits zurück */
	get githash(): string {
		return githash;
	}

	/** Gibt die Map der CoreTypeDaten zurück */
	get mapCoreTypeData(): Map<string, any> {
		return this.conn.mapCoreTypeData;
	}

	/**
	 * Versucht eine Verbindung zu der angegebenen Adresse herzustellen.
	 *
	 * @returns bei Erfolg die Liste der auf dem Server zur Verfügung stehenden Schemata
	 */
	connectTo = async (): Promise<List<DBSchemaListeEintrag>> => {
		return await this.conn.connectTo();
	};

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
	};

	/**
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	logout = async (): Promise<void> => {
		await this.conn.logout();
	};

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
		for (const l of listSchueler) {
			mapSchueler.set(l.id, l);
		}
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
		for (const l of listLehrer) {
			mapLehrer.set(l.id, l);
		}
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
	public async getKlassenListe(idSchuljahresabschnitt: number): Promise<Map<number, KlassenDaten>> {
		const listKlassen = await this.server.getListKlassenDatenBySchuljahresabschnitt(this.schema, idSchuljahresabschnitt);
		const mapKlassen = new Map<number, KlassenDaten>();
		for (const k of listKlassen) {
			mapKlassen.set(k.id, k);
		}
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
	public async getKursListe(idSchuljahresabschnitt: number): Promise<Map<number, KursDaten>> {
		const listKurse = await this.server.getKurseFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapKurse: Map<number, KursDaten> = new Map();
		for (const k of listKurse) {
			mapKurse.set(k.id, k);
		}
		return mapKurse;
	}


	/**
	 * Bestimmt eine Map mit der Liste der Jahrgänge, welche das Listen-Objekt dessen ID zuordnet.
	 *
	 * @returns die Map mit den Jahrgängen
	 */
	public async getJahrgangsListe(): Promise<Map<number, JahrgangsDaten>> {
		// aktualisiere die Jahrgänge und erstelle Map
		const listJahrgaenge = await this.server.getJahrgaenge(this.schema);
		const mapJahrgaenge: Map<number, JahrgangsDaten> = new Map();
		for (const j of listJahrgaenge) {
			mapJahrgaenge.set(j.id, j);
		}
		return mapJahrgaenge;
	}


	/// --- Methoden für einen Api-Zugriff, welcher den API-Status korrekt setzt


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
		};
	};

}

/** Die Api-Instanz zur Verwendung im SVWS-Client */
export const api = new Api();
