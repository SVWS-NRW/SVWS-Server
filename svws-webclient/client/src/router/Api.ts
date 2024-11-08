import { computed } from "vue";

import type { AES } from "~/utils/crypto/aes";
import type { Config } from "~/components/Config";
import type { List, DBSchemaListeEintrag, ApiServer, LehrerListeEintrag, SchuelerListeEintrag, KlassenDaten, KursDaten, JahrgangsDaten, SchuleStammdaten, Schuljahresabschnitt, BenutzerDaten, BenutzerKompetenz, ServerMode, ValidatorKontext} from "@core";
import { Schulform, Schulgliederung, BenutzerTyp, OpenApiError, SimpleOperationResponse, DeveloperNotificationException } from "@core";

import { ApiConnection } from "~/router/ApiConnection";
import type { ApiPendingData} from "~/components/ApiStatus";
import { ApiStatus } from "~/components/ApiStatus";
import { version } from '../../version';
import { githash } from "../../githash";

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

	/** Gibt das AES-Objekt zurück */
	get aes(): AES {
		return this.conn.aes;
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
	get mapCoreTypeNameJsonData(): Map<string, string> {
		return this.conn.mapCoreTypeNameJsonData
	}

	/** Setzt die Map der CoreTypeDaten zurück */
	setMapCoreTypeNameJsonData = (map: Map<string, string>) => {
		this.conn.mapCoreTypeNameJsonData = map;
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
	 * Initialialisiert die Daten, die beim Login geladen )erden sollen
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


	/// --- Informationen zu dem Benutzer, der angemeldet ist

	/**
	 * Gibt die Daten des Benutzers zurück.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerdaten(): BenutzerDaten {
		return this.conn.benutzerdaten;
	}

	/**
	 * Gibt an, ob es sich bei dem angemeldeten Benutzer um einen adminstrativen Benutzer
	 * handelt oder nicht.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerIstAdmin(): boolean {
		return this.conn.istAdmin;
	}

	/**
	 * Die Menge der Benutzerkompetenzen des angemeldeten Benutzers.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerKompetenzen(): Set<BenutzerKompetenz> {
		return this.conn.kompetenzen;
	}

	/**
	 * Prüft, ob der angemeldete Benutzer die angegebene Kompetenz hat oder
	 * nicht.
	 *
	 * @param kompetenz   die zu prüfende Kompetenz.
	 *
	 * @returns true, falls der Benutzer die Kompetenz hat und ansonsten false
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public benutzerHatKompetenz(kompetenz: BenutzerKompetenz): boolean {
		return this.benutzerKompetenzen.has(kompetenz);
	}

	/**
	 * Prüft, ob der angemeldete Benutzer eine der angegebenen Kompetenzen
	 * hat oder nicht.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen.
	 *
	 * @returns true, falls der Benutzer einer der Kompetenzen hat und ansonsten false
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public benutzerHatEineKompetenz(kompetenzen: Iterable<BenutzerKompetenz>): boolean {
		const setKompetenzen = this.benutzerKompetenzen;
		for (const kompetenz of kompetenzen)
			if (setKompetenzen.has(kompetenz))
				return true;
		return false;
	}


	/**
	 * Die Menge an Klassen-IDs, auf denen der angemeldete Benutzer aufgrund einer Klassen-
	 * oder Abteilungsleitung funktionsbezogene Kompetenzen hat.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerKompetenzenKlassen(): Set<number> {
		return this.conn.kompetenzenKlasse;
	}


	/**
	 * Die Menge an Abiturjahrgängen, bei denen der angemeldete Benutzer als Beratungslehrer
	 * funktionsbezogene Kompetenzen hat.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	public get benutzerKompetenzenAbiturjahrgaenge(): Set<number> {
		return this.conn.kompetenzenAbiturjahrgaenge;
	}


	/**
	 * Gibt den Typ des Benutzers zurück.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist oder der Benutzer-Typ ungültig ist
	 */
	public get benutzertyp(): BenutzerTyp {
		const typ = BenutzerTyp.getByID(this.benutzerdaten.typ);
		if (typ === null)
			throw new DeveloperNotificationException("Der Typ des Benutzers ist ungültig.");
		return typ;
	}


	/**
	 * Gibt an, ob es sich bei dem Benutzer um einen Lehrer-Benutzer handelt.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist oder der Benutzer-Typ ungültig oder kein Lehrer ist
	 */
	public get benutzerIDLehrer(): number {
		if (this.benutzertyp !== BenutzerTyp.LEHRER)
			throw new DeveloperNotificationException("Der Benutzer ist kein Lehrer, weshalb keine Lehrer-ID ermittelt werden kann.");
		return this.benutzerdaten.typID;
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

	/// --- Informationen zu der Schule, bei der der Benutzer eingeloggt ist

	/**
	 * Gibt die Stammdaten der Schule zurück, sofern bereits ein Login stattgefunden hat.
	 *
	 * @returns die Stammdaten
	 */
	public get schuleStammdaten(): SchuleStammdaten {
		return this.conn.schuleStammdaten;
	}

	/**
	 * Gibt den Validator-Kontext für die Validierung von Statistik-relevanten Daten zurück.
	 *
	 * @returns der Validator-Kontext
	 */
	public get validatorKontext(): ValidatorKontext {
		return this.conn.validatorKontext;
	}

	/**
	 * Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist.
	 *
	 * @returns die Schulform
	 */
	public get schulform(): Schulform {
		const schulform = Schulform.data().getWertByKuerzel(this.conn.schuleStammdaten.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("In den Schul-Stammdaten ist eine ungültige Schulform eingetragen.");
		return schulform;
	}

	/**
	 * Gibt die zulässigen Schulgliederungen für die Schule zurück, wo der
	 * Benutzer angemeldet ist.
	 *
	 * @returns eine Liste mit den Schulgliederungen
	 */
	public get schulgliederungen(): List<Schulgliederung> {
		return Schulgliederung.getBySchuljahrAndSchulform(this.abschnitt.schuljahr, this.schulform);
	}

	/**
	 * Liefert ein Map für alle in der Schule angelegten Schuljahresabschnitte
	 *
	 * @return eine Map mit den Schuljahresabschnitten
	 */
	public mapAbschnitte = computed<Map<number, Schuljahresabschnitt>>(() => {
		const mapAbschnitte = new Map<number, Schuljahresabschnitt>();
		for (const a of this.schuleStammdaten.abschnitte)
			mapAbschnitte.set(a.id, a);
		return mapAbschnitte;
	});

	/**
	 * Gibt den aktuellen Schuljahresabschnitt zurück.
	 *
	 * @returns der aktuelle Schuljahresabschnitt
	 */
	public get abschnitt(): Schuljahresabschnitt {
		const abschnitt = this.mapAbschnitte.value.get(this.schuleStammdaten.idSchuljahresabschnitt);
		if (abschnitt === undefined)
			throw new DeveloperNotificationException("Der aktuelle Schuljahresabschnitt der Schule existiert nicht in der Liste der Schuljahresabschnitte.");
		return abschnitt;
	}

	/** Gibt an, ob die aktuell ausgewählte Schule einen Quartalsmodus anwendet */
	public hatQuartalsModus(): boolean {
		return (this.schuleStammdaten.schuleAbschnitte.anzahlAbschnitte === 4);
	}

	/**
	 * Bestimmt den Schuljahresabschnitt anhand des übergebenen Schuljahres und dem Abschnitt.
	 *
	 * @param schuljahr das Schuljahr
	 * @param abschnitt der Abschnitt (Anzahl der Abschnitt in einem Jahrgang beachten!)
	 *
	 * @returns der Schuljahresabschnitt
	 */
	public getAbschnittBySchuljahrUndAbschnitt(schuljahr: number, abschnitt: number): Schuljahresabschnitt | undefined {
		let result : Schuljahresabschnitt | undefined = undefined;
		for (const a of this.schuleStammdaten.abschnitte) {
			if ((a.schuljahr === schuljahr) && (a.abschnitt === abschnitt)) {
				result = a;
				break;
			}
		}
		return result;
	}

	/**
	 * Bestimmt den Schuljahresabschnitt anhand des übergebenen Schuljahres, dem Halbjahr und ggf. dem Quartal,
	 * falls die Schule im Quartalsmodus betrieben wird.
	 *
	 * @param schuljahr das Schuljahr
	 * @param halbjahr  das Halbjahr (1 oder 2)
	 * @param quartal   das Quartal (1 oder 2), default 1
	 *
	 * @returns der Schuljahresabschnitt
	 */
	public getAbschnittBySchuljahrUndHalbjahr(schuljahr: number, halbjahr: number, quartal: number = 1): Schuljahresabschnitt | undefined {
		const abschnitt = this.hatQuartalsModus() ? ((halbjahr - 1) * 2) + quartal : halbjahr;
		return this.getAbschnittBySchuljahrUndAbschnitt(schuljahr, abschnitt);
	}


	/**
	 * Informiert die Api, dass ihre Daten, z.B. die Stammdaten der Schule im Client angepasst wurden
	 */
	updatedApiData = () => {
		this.conn.updatedApiData();
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
	public async getKlassenListe(idSchuljahresabschnitt: number): Promise<Map<number, KlassenDaten>> {
		const listKlassen = await this.server.getKlassenFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapKlassen = new Map<number, KlassenDaten>();
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
	public async getKursListe(idSchuljahresabschnitt: number): Promise<Map<number, KursDaten>> {
		const listKurse = await this.server.getKurseFuerAbschnitt(this.schema, idSchuljahresabschnitt);
		const mapKurse: Map<number, KursDaten> = new Map();
		for (const k of listKurse)
			mapKurse.set(k.id, k)
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
		const mapJahrgaenge: Map<number, JahrgangsDaten> = new Map()
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j)
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
		}
	}


	/**
	 * Führt eine "einfache" API-Operation aus, welche mithilfe einer SimpleOperationResponse antwortet.
	 *
	 * @param op               die auszuführende API-Operation
	 * @param errorResponses   ein Array mit den HTTP-Response-Codes, welche direkt eine SimpleOperationResponse vom Server liefern
	 *
	 * @returns die SimpleOperationResponse der API-Operation
	 */
	public runSimpleOperation = async (op : () => Promise<SimpleOperationResponse>, errorResponses: Array<number>) : Promise<SimpleOperationResponse> => {
		try {
			return await op();
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && (errorResponses.includes(e.response.status))) {
				const json : string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage!");
			if (e instanceof Error)
				result.log.add("  " + e.message);
			return result;
		}
	};

}

/** Die Api-Instanz zur Verwendung im SVWS-Client */
export const api = new Api();
