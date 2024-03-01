import { BaseApi, type ApiFile } from '../api/BaseApi';
import { ArrayList } from '../java/util/ArrayList';
import { BenutzerKennwort } from '../core/data/BenutzerKennwort';
import { BenutzerListeEintrag } from '../core/data/benutzer/BenutzerListeEintrag';
import { DatenbankVerbindungsdaten } from '../core/data/schema/DatenbankVerbindungsdaten';
import { List } from '../java/util/List';
import { MigrateBody } from '../core/data/db/MigrateBody';
import { SchemaListeEintrag } from '../core/data/db/SchemaListeEintrag';
import { SchuleInfo } from '../core/data/schule/SchuleInfo';
import { SchulenKatalogEintrag } from '../core/data/schule/SchulenKatalogEintrag';
import { SchuleStammdaten } from '../core/data/schule/SchuleStammdaten';
import { SimpleOperationResponse } from '../core/data/SimpleOperationResponse';

export class ApiSchemaPrivileged extends BaseApi {

	/**
	 *
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
	 * Implementierung der POST-Methode createSchemaCurrentInto für den Zugriff auf die URL https://{hostname}/api/schema/create/{schema}
	 *
	 * Erstellt ein neues leeres SVWS-Schema der aktuellen Revision in dem angegebenen existierenden Schema.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Anlegen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *   Code 500: Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der aktuellen Revision konnte nicht angelegt werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Anlegen des Schemas
	 */
	public async createSchemaCurrentInto(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/create/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createSchemaInto für den Zugriff auf die URL https://{hostname}/api/schema/create/{schema}/{revision : \d+}
	 *
	 * Erstellt ein neues leeres SVWS-Schema der angegebenen Revision in dem angegebenen existierenden Schema.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Anlegen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es wurde eine ungültige Revision angegeben.
	 *   Code 403: Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *   Code 500: Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der Revision konnte nicht angelegt werden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} revision - der Pfad-Parameter revision
	 *
	 * @returns Der Log vom Anlegen des Schemas
	 */
	public async createSchemaInto(schema : string, revision : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/create/{schema}/{revision : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{revision\s*(:[^{}]+({[^{}]+})*)?}/g, revision.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode initSchemaMitSchule für den Zugriff auf die URL https://{hostname}/api/schema/create/{schema}/init/{schulnummer : \d+}
	 *
	 * Legt die Daten für eine neue Schule in einem SVWS-Schema an und gibt anschließend die Schulstammdaten zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Schule besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schule wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuleStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schule anzulegen.
	 *   Code 404: Keine Schule mit der angegebenen Schulnummer gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde, dies ist z.B. der Fall, falls zuvor schon eine Schule angelegt wurde.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Die Schule wurde erfolgreich angelegt.
	 */
	public async initSchemaMitSchule(schema : string, schulnummer : number) : Promise<SchuleStammdaten> {
		const path = "/api/schema/create/{schema}/init/{schulnummer : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SchuleStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode exportSQLiteFrom für den Zugriff auf die URL https://{hostname}/api/schema/export/{schema}/sqlite
	 *
	 * Exportiert das angegebene Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert einen Datenbank-Benutzer mit den entsprechenden Rechten.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Export der SQLite-Datenbank
	 *     - Mime-Type: application/vnd.sqlite3
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Das Schema darf nicht exportiert werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Export der SQLite-Datenbank
	 */
	public async exportSQLiteFrom(schema : string) : Promise<ApiFile> {
		const path = "/api/schema/export/{schema}/sqlite"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const data : ApiFile = await super.getSQLite(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode importSQLiteInto für den Zugriff auf die URL https://{hostname}/api/schema/import/{schema}/sqlite
	 *
	 * Importiert die übergebene Datenbank in dieses Schema. Das Schema wird dabei zunächst geleert und vorhanden Daten gehen dabei verloren.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Importieren der SQLite-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: In das Schema darf nicht importiert werden.
	 *   Code 500: Fehler beim Importieren mit dem Log des fehlgeschlagenen Imports.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Importieren der SQLite-Datenbank
	 */
	public async importSQLiteInto(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/import/{schema}/sqlite"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchemaListe für den Zugriff auf die URL https://{hostname}/api/schema/liste/alle
	 *
	 * Liefert eine Liste der Schemata. Hierfür werden root-Rechte auf der Datenbank benötigt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste mit allen sichtbaren Schema-Namen in der Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<String>
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt
	 *
	 * @returns Die Liste mit allen sichtbaren Schema-Namen in der Datenbank
	 */
	public async getSchemaListe() : Promise<List<string>> {
		const path = "/api/schema/liste/alle";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchemaAdmins für den Zugriff auf die URL https://{hostname}/api/schema/liste/info/{schema}/admins
	 *
	 * Liefert die Informationen zu den administrativen Benutzern in einem aktuellen SVWS-Schema.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Informationen zu den administrativen Benutzern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzerListeEintrag>
	 *   Code 400: Das angegebene Schema ist kein aktuelles SVWS-Schema
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schul-Informationen abzufragen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Informationen zu den administrativen Benutzern
	 */
	public async getSchemaAdmins(schema : string) : Promise<List<BenutzerListeEintrag>> {
		const path = "/api/schema/liste/info/{schema}/admins"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuleInfo für den Zugriff auf die URL https://{hostname}/api/schema/liste/info/{schema}/schule
	 *
	 * Liefert die Informationen zu einer Schule eines SVWS-Schema. Hierfür werden Datenbank-Rechte auf dem Schema benötigt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Informationen zur Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuleInfo
	 *   Code 400: Das angegebene Schema ist kein SVWS-Schema
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schul-Informationen abzufragen.
	 *   Code 404: Es wurden keine Schul-Informationen in dem SVWS-Schema gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Informationen zur Schule
	 */
	public async getSchuleInfo(schema : string) : Promise<SchuleInfo> {
		const path = "/api/schema/liste/info/{schema}/schule"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuleInfo.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getAllgemeinenKatalogSchulen für den Zugriff auf die URL https://{hostname}/api/schema/liste/kataloge/schulen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Schulen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulenKatalogEintrag>
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um den Katalog anzusehen.
	 *   Code 404: Keine Schulen-Katalog-Einträge gefunden
	 *
	 * @returns Eine Liste von Schulen-Katalog-Einträgen
	 */
	public async getAllgemeinenKatalogSchulen() : Promise<List<SchulenKatalogEintrag>> {
		const path = "/api/schema/liste/kataloge/schulen";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSVWSSchemaListe für den Zugriff auf die URL https://{hostname}/api/schema/liste/svws
	 *
	 * Liefert eine Liste der SVWS-Schemata. Hierfür werden root-Rechte auf der Datenbank benötigt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schema-Liste mit den Namen und den Versionsinformationen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchemaListeEintrag>
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die SVWS-Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt
	 *
	 * @returns Die Schema-Liste mit den Namen und den Versionsinformationen des Schemas
	 */
	public async getSVWSSchemaListe() : Promise<List<SchemaListeEintrag>> {
		const path = "/api/schema/liste/svws";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchemaListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchemaListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDBInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mariadb
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDBInto(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mariadb"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDBSchulnummerInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mariadb/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDBSchulnummerInto(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mariadb/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMDBInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mdb
	 *
	 * Migriert die übergebene Datenbank in das angegebene Schema. Das Schema wird dabei geleert und vorhanden Daten gehen dabei verloren.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der Access-MDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der Access-MDB-Datenbank
	 */
	public async migrateMDBInto(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mdb"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMsSqlServerInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mssql
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der SQL-Server-Datenbank
	 */
	public async migrateMsSqlServerInto(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mssql"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMsSqlServerSchulnummerInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mssql/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der SQL-Server-Datenbank
	 */
	public async migrateMsSqlServerSchulnummerInto(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mssql/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySqlInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mysql
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySqlInto(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mysql"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySqlSchulnummerInto für den Zugriff auf die URL https://{hostname}/api/schema/migrate/{schema}/mysql/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySqlSchulnummerInto(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/migrate/{schema}/mysql/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createSchemaCurrent für den Zugriff auf die URL https://{hostname}/api/schema/root/create/{schema}
	 *
	 * Erstellt ein neues Schema der aktuellen Revision mit dem angegebenen Namen, falls keines mit dem angebenen Namen bereits existiert.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Anlegen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es wurde ein nicht erlaubter Schema-Name oder Benutzername angegeben.
	 *   Code 403: Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *   Code 500: Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der aktuellen Revision konnte nicht angelegt werden.
	 *
	 * @param {BenutzerKennwort} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Anlegen des Schemas
	 */
	public async createSchemaCurrent(data : BenutzerKennwort, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/create/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = BenutzerKennwort.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createSchema für den Zugriff auf die URL https://{hostname}/api/schema/root/create/{schema}/{revision : \d+}
	 *
	 * Erstellt ein neues Schema der angegebenen Revision und dem angegebenen Namen, falls keine Schema mit dem angebenen Namen bereits existiert.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Anlegen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es wurde ein nicht erlaubter Schema-Name, Benutzername oder eine ungültige Revision angegeben.
	 *   Code 403: Der angemeldete Benutzer verfügt nicht über die notwendigen Rechte zum Anlegen eines Schemas.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *   Code 500: Der Datenbankzugriff auf das neue Schema mit dem neuen zugehörigen Admin-Benutzer ist fehlgeschlagen oder das SVWS-Schema mit der Revision konnte nicht angelegt werden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {BenutzerKennwort} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} revision - der Pfad-Parameter revision
	 *
	 * @returns Der Log vom Anlegen des Schemas
	 */
	public async createSchema(data : BenutzerKennwort, schema : string, revision : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/create/{schema}/{revision : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{revision\s*(:[^{}]+({[^{}]+})*)?}/g, revision.toString());
		const body : string = BenutzerKennwort.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode destroySchema für den Zugriff auf die URL https://{hostname}/api/schema/root/destroy/{schema}
	 *
	 * Entfernt das Schema mit dem angegebenen Namen, falls es existiert.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Log vom Löschen des Schemas
	 *   Code 403: Das Schema darf nicht gelöscht werden.
	 *   Code 404: Das angegebene Schema wurde nicht gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async destroySchema(schema : string) : Promise<void> {
		const path = "/api/schema/root/destroy/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode existsSchema für den Zugriff auf die URL https://{hostname}/api/schema/root/exists/{schema}
	 *
	 * Liefert die Information, ob ein Schema existiert. Hierfür werden root-Rechte auf der Datenbank benötigt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: true, wenn das Schema existiert
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns true, wenn das Schema existiert
	 */
	public async existsSchema(schema : string) : Promise<boolean | null> {
		const path = "/api/schema/root/exists/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode importExistingSchema für den Zugriff auf die URL https://{hostname}/api/schema/root/import/existing/{schema}
	 *
	 * Fügt ein bestehendes SVWS-Schema zu der SVWS-Konfiguration hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Das Schema wurde erfolgreich hinzugefügt
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Schema hinzuzufügen oder der angegebene Benutzer hat nicht ausreichend Rechte, um auf das Schema zuzugreifen.
	 *   Code 404: Keine Schema mit dem angebenen Namen gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BenutzerKennwort} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async importExistingSchema(data : BenutzerKennwort, schema : string) : Promise<void> {
		const path = "/api/schema/root/import/existing/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = BenutzerKennwort.transpilerToJSON(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode importSQLite2Schema für den Zugriff auf die URL https://{hostname}/api/schema/root/import/sqlite/{schema}
	 *
	 * Importiert die übergebene SQLite-Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der SQLite-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht importiert werden.
	 *   Code 500: Fehler bei dem Import der SQLite-Datenbank mit dem Log des fehlgeschlagenen Imports.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Import der SQLite-Datenbank
	 */
	public async importSQLite2Schema(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/import/sqlite/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDB2Schema für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mariadb/{schema}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDB2Schema(data : MigrateBody, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mariadb/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDB2SchemaSchulnummer für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mariadb/{schema}/{schulnummer:\d{6}}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDB2SchemaSchulnummer(data : MigrateBody, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mariadb/{schema}/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMDB2Schema für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mdb/{schema}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der Access-MDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der Access-MDB-Datenbank
	 */
	public async migrateMDB2Schema(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mdb/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMSSQL2Schema für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mssql/{schema}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MS-SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MS-SQL-Server-Datenbank
	 */
	public async migrateMSSQL2Schema(data : MigrateBody, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mssql/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMSSQL2SchemaSchulnummer für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mssql/{schema}/{schulnummer:\d{6}}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MS-SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MS-SQL-Server-Datenbank
	 */
	public async migrateMSSQL2SchemaSchulnummer(data : MigrateBody, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mssql/{schema}/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySQL2Schema für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mysql/{schema}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySQL2Schema(data : MigrateBody, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mysql/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySQL2SchemaSchulnummer für den Zugriff auf die URL https://{hostname}/api/schema/root/migrate/mysql/{schema}/{schulnummer:\d{6}}
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Sollte ein Schema mit dem Namen bereits bestehen, so wird es ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {MigrateBody} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySQL2SchemaSchulnummer(data : MigrateBody, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/root/migrate/mysql/{schema}/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode existsUser für den Zugriff auf die URL https://{hostname}/api/schema/root/user/{user}/exists
	 *
	 * Liefert die Information, ob ein DBMS-User existiert. Hierfür werden root-Rechte auf der Datenbank benötigt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: true, wenn der Benutzer existiert
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt
	 *
	 * @param {string} user - der Pfad-Parameter user
	 *
	 * @returns true, wenn der Benutzer existiert
	 */
	public async existsUser(user : string) : Promise<boolean | null> {
		const path = "/api/schema/root/user/{user}/exists"
			.replace(/{user\s*(:[^{}]+({[^{}]+})*)?}/g, user);
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode checkDBPassword für den Zugriff auf die URL https://{hostname}/api/schema/root/user/checkpwd
	 *
	 * Prüft, ob das übergebene Kennwort für den Datenbankbenutzer gültig ist. Zur Prüfung werden root-Rechte auf der Datenbank benötigt
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: true, wenn das Kennwort und der Benutzername korrekt sind und den priviligierten Zugriff auf die Datenbankschema erlauben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der angegebene Benutzer besitzt nicht die Rechte, um die Schema-Liste der Datenbank auszulesen. Hierfür werden root-Rechte benötigt
	 *
	 * @param {BenutzerKennwort} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns true, wenn das Kennwort und der Benutzername korrekt sind und den priviligierten Zugriff auf die Datenbankschema erlauben.
	 */
	public async checkDBPassword(data : BenutzerKennwort) : Promise<boolean | null> {
		const path = "/api/schema/root/user/checkpwd";
		const body : string = BenutzerKennwort.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode updateSchemaToCurrent für den Zugriff auf die URL https://{hostname}/api/schema/update/{schema}
	 *
	 * Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die neueste Revision.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Verlauf des Updates
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein interner-Server-Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Verlauf des Updates
	 */
	public async updateSchemaToCurrent(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/update/{schema}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode updateSchema für den Zugriff auf die URL https://{hostname}/api/schema/update/{schema}/{revision : \d+}
	 *
	 * Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die übergebene Revision, sofern diese in der Schema-Definition existiert.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Verlauf des Updates
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein interner-Server-Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} revision - der Pfad-Parameter revision
	 *
	 * @returns Der Log vom Verlauf des Updates
	 */
	public async updateSchema(schema : string, revision : number) : Promise<SimpleOperationResponse> {
		const path = "/api/schema/update/{schema}/{revision : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{revision\s*(:[^{}]+({[^{}]+})*)?}/g, revision.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


}
