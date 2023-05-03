import { BaseApi } from '../api/BaseApi';
import { ArrayList } from '../java/util/ArrayList';
import { List } from '../java/util/List';
import { SimpleOperationResponse } from '../core/data/SimpleOperationResponse';

export class ApiSchema extends BaseApi {

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
	 * Implementierung der GET-Methode exportSQLite für den Zugriff auf die URL https://{hostname}/db/{schema}/api/export/sqlite
	 *
	 * Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert administrative Rechte.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Export der SQLite-Datenbank
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: Blob
	 *   Code 403: Das Schema darf nicht exportiert werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Export der SQLite-Datenbank
	 */
	public async exportSQLite(schema : string) : Promise<Blob> {
		const path = "/db/{schema}/api/export/sqlite"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const data : Blob = await super.getSQLite(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode isTainted für den Zugriff auf die URL https://{hostname}/db/{schema}/api/isTainted
	 *
	 * Gibt zurück, ob es sich um ein "verdorbenes" Schema handelt oder nicht. Eine Schema wird  wird als "verdorben" bezeichnet, wenn es ggf. fehlerhaft ist, weil es mithilfe einer Entwicklerversion  des SVWS-Servers aktualisiert wurde.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: true, falls es sich um ein "verdorbenes" Schema handelt und ansonsten false
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 404: Es konnte keine Revision für das Schema ermittelt werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns true, falls es sich um ein "verdorbenes" Schema handelt und ansonsten false
	 */
	public async isTainted(schema : string) : Promise<boolean | null> {
		const path = "/db/{schema}/api/isTainted"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode migrateFromMDB für den Zugriff auf die URL https://{hostname}/db/{schema}/api/migrate/mdb
	 *
	 * Migriert die übergebene Datenbank in dieses Schema. Das Schema wird dabei geleert und vorhanden Daten gehen dabei verloren.
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
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der Access-MDB-Datenbank
	 */
	public async migrateFromMDB(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/api/migrate/mdb"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postMultipart(path, null);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode revision für den Zugriff auf die URL https://{hostname}/db/{schema}/api/revision
	 *
	 * Liefert die aktuelle Revision des angegebenen Schemas.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Revision des Schemas
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 404: Es konnte keine Revision für das Schema ermittelt werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Revision des Schemas
	 */
	public async revision(schema : string) : Promise<number | null> {
		const path = "/db/{schema}/api/revision"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return parseFloat(JSON.parse(text));
	}


	/**
	 * Implementierung der POST-Methode updateSchemaToCurrent für den Zugriff auf die URL https://{hostname}/db/{schema}/api/update
	 *
	 * Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die neueste Revision.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Verlauf des Updates
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<String>
	 *   Code 400: Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Verlauf des Updates
	 */
	public async updateSchemaToCurrent(schema : string) : Promise<List<string>> {
		const path = "/db/{schema}/api/update"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode updateSchema für den Zugriff auf die URL https://{hostname}/db/{schema}/api/update/{revision : \d+}
	 *
	 * Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die übergebene Revision, sofern diese in der Schema-Definition existiert.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Verlauf des Updates
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<String>
	 *   Code 400: Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.
	 *   Code 404: Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} revision - der Pfad-Parameter revision
	 *
	 * @returns Der Log vom Verlauf des Updates
	 */
	public async updateSchema(schema : string, revision : number) : Promise<List<string>> {
		const path = "/db/{schema}/api/update/{revision : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{revision\s*(:[^}]+)?}/g, revision.toString());
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
	}


}
