import { BaseApi, type ApiFile } from '../api/BaseApi';
import { LernplattformV1Export } from '../core/data/lernplattform/v1/LernplattformV1Export';

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


}
