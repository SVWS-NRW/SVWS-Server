import { BaseApi, type ApiFile } from '../api/BaseApi';
import { ArrayList } from '../java/util/ArrayList';
import { LernplattformV1 } from '../core/data/lernplattform/v1/LernplattformV1';
import { LernplattformV1Export } from '../core/data/lernplattform/v1/LernplattformV1Export';
import { List } from '../java/util/List';
import { SchuljahresabschnittV1 } from '../core/data/schuljahresabschnitt/v1/SchuljahresabschnittV1';

export class ApiExternal extends BaseApi {

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
	 * Implementierung der GET-Methode getLernplattformen für den Zugriff auf die URL https://{hostname}/api/external/{schema}/v1/lernplattformen/
	 *
	 * Erstellt eine Liste aller vorhandenen Lernplattformen, insofer die notwendige Berechtigung vorliegen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lernplattformen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LernplattformV1>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lernplattformen anzusehen.
	 *   Code 404: Keine Lernplattformen gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lernplattformen
	 */
	public async getLernplattformen(schema : string) : Promise<List<LernplattformV1>> {
		const path = "/api/external/{schema}/v1/lernplattformen/"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LernplattformV1>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LernplattformV1.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLernplattformenExport für den Zugriff auf die URL https://{hostname}/api/external/{schema}/v1/lernplattformen/{idLernplattform : \d+}/{idSchuljahresabschnitt : \d+}
	 *
	 * Es werden alle relevanten Daten zu Jahrgängen, Klassen, Lehrern, Schülern, Fächern und Lerngruppen aus der SVWS-DB geladen und für den Export bezogen auf eine Lernplattform aufbereitet und zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten für den Lernplattformen Datenexport
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LernplattformV1Export
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Lernplattformen Datenexport anzufordern.
	 *   Code 404: Es wurden nicht alle benötigten Ressourcen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idLernplattform - der Pfad-Parameter idLernplattform
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Die Daten für den Lernplattformen Datenexport
	 */
	public async getLernplattformenExport(schema : string, idLernplattform : number, idSchuljahresabschnitt : number) : Promise<LernplattformV1Export> {
		const path = "/api/external/{schema}/v1/lernplattformen/{idLernplattform : \\d+}/{idSchuljahresabschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idLernplattform\s*(:[^{}]+({[^{}]+})*)?}/g, idLernplattform.toString())
			.replace(/{idSchuljahresabschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idSchuljahresabschnitt.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LernplattformV1Export.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getLernplattformenExportAsGzip für den Zugriff auf die URL https://{hostname}/api/external/{schema}/v1/lernplattformen/{idLernplattform : \d+}/{idSchuljahresabschnitt : \d+}/gzip
	 *
	 * Es werden alle relevanten Daten zu Jahrgängen, Klassen, Lehrern, Schülern, Fächern und Lerngruppen aus der SVWS-DB geladen und für den Export bezogen auf eine Lernplattform aufbereitet und komprimiert im gzip-Format zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten für den Lernplattformen Datenexport
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Lernplattformen Datenexport anzufordern.
	 *   Code 404: Es wurden nicht alle benötigten Ressourcen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idLernplattform - der Pfad-Parameter idLernplattform
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Die Daten für den Lernplattformen Datenexport
	 */
	public async getLernplattformenExportAsGzip(schema : string, idLernplattform : number, idSchuljahresabschnitt : number) : Promise<ApiFile> {
		const path = "/api/external/{schema}/v1/lernplattformen/{idLernplattform : \\d+}/{idSchuljahresabschnitt : \\d+}/gzip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idLernplattform\s*(:[^{}]+({[^{}]+})*)?}/g, idLernplattform.toString())
			.replace(/{idSchuljahresabschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idSchuljahresabschnitt.toString());
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getSchuljahresabschnitte für den Zugriff auf die URL https://{hostname}/api/external/{schema}/v1/schuljahresabschnitte/
	 *
	 * Erstellt eine Liste aller vorhandenen Schuljahresabschnitte, insofern die notwendige Berechtigung vorliegt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schuljahresabschnitten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuljahresabschnittV1>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuljahresabschnitte anzusehen.
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schuljahresabschnitten
	 */
	public async getSchuljahresabschnitte(schema : string) : Promise<List<SchuljahresabschnittV1>> {
		const path = "/api/external/{schema}/v1/schuljahresabschnitte/"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuljahresabschnittV1>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuljahresabschnittV1.transpilerFromJSON(text)); });
		return ret;
	}


}
