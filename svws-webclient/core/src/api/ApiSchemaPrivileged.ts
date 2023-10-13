import { BaseApi, type ApiFile } from '../api/BaseApi';
import { ArrayList } from '../java/util/ArrayList';
import { BenutzerKennwort } from '../core/data/BenutzerKennwort';
import { List } from '../java/util/List';
import { MigrateBody } from '../core/data/db/MigrateBody';
import { SchemaListeEintrag } from '../core/data/db/SchemaListeEintrag';
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{revision\s*(:[^}]+)?}/g, revision.toString());
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
	 *   Code 200: Der Log vom Löschen des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<String>
	 *   Code 403: Das Schema darf nicht gelöscht werden.
	 *   Code 404: Das angegebene Schema wurde nicht gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Löschen des Schemas
	 */
	public async destroySchema(schema : string) : Promise<List<string>> {
		const path = "/api/schema/root/destroy/{schema}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchemaListe für den Zugriff auf die URL https://{hostname}/api/schema/root/liste
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
		const path = "/api/schema/root/liste";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
		const body : string = MigrateBody.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSVWSSchemaListe für den Zugriff auf die URL https://{hostname}/api/schema/root/svwsliste
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
		const path = "/api/schema/root/svwsliste";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchemaListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchemaListeEintrag.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{user\s*(:[^}]+)?}/g, user);
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


}
