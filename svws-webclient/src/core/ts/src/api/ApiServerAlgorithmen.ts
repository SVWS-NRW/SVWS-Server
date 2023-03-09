import { BaseApi } from './BaseApi';
import { AbschlussErgebnis, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnis } from '../core/data/abschluss/AbschlussErgebnis';
import { GEAbschlussFaecher, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFaecher } from '../core/data/abschluss/GEAbschlussFaecher';
import { GostBelegpruefungErgebnis, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungErgebnis } from '../core/abschluss/gost/GostBelegpruefungErgebnis';
import { GostBelegpruefungsdaten, cast_de_nrw_schule_svws_core_data_gost_GostBelegpruefungsdaten } from '../core/data/gost/GostBelegpruefungsdaten';

export class ApiServerAlgorithmen extends BaseApi {

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
	 * Implementierung der POST-Methode getGesamtschuleAbschlussHA10 für den Zugriff auf die URL https://{hostname}/api/gesamtschule/abschluss/ha10
	 *
	 * Prüft anhand der übergeben Fächerdaten, ob ein Hauptschulabschluss der Klasse 10 an einer Gesamtschule erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbschlussErgebnis
	 *
	 * @param {GEAbschlussFaecher} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 */
	public async getGesamtschuleAbschlussHA10(data : GEAbschlussFaecher) : Promise<AbschlussErgebnis> {
		const path = "/api/gesamtschule/abschluss/ha10";
		const body : string = GEAbschlussFaecher.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return AbschlussErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGesamtschuleAbschlussHA9 für den Zugriff auf die URL https://{hostname}/api/gesamtschule/abschluss/ha9
	 *
	 * Prüft anhand der übergeben Fächerdaten, ob ein Hauptschulabschluss der Klasse 9 an einer Gesamtschule erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbschlussErgebnis
	 *   Code 400: Es wurden ungültige Werte übergeben, so dass kein Abschluss berechnet werden kann.
	 *
	 * @param {GEAbschlussFaecher} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 */
	public async getGesamtschuleAbschlussHA9(data : GEAbschlussFaecher) : Promise<AbschlussErgebnis> {
		const path = "/api/gesamtschule/abschluss/ha9";
		const body : string = GEAbschlussFaecher.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return AbschlussErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGesamtschuleAbschlussMSA für den Zugriff auf die URL https://{hostname}/api/gesamtschule/abschluss/msa
	 *
	 * Prüft anhand der übergeben Fächerdaten, ob ein Mittlerer Schulabschluss nach der Klasse 10 an einer Gesamtschule erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbschlussErgebnis
	 *
	 * @param {GEAbschlussFaecher} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 */
	public async getGesamtschuleAbschlussMSA(data : GEAbschlussFaecher) : Promise<AbschlussErgebnis> {
		const path = "/api/gesamtschule/abschluss/msa";
		const body : string = GEAbschlussFaecher.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return AbschlussErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGesamtschuleAbschlussMSAQ für den Zugriff auf die URL https://{hostname}/api/gesamtschule/abschluss/msaq
	 *
	 * Prüft anhand der übergeben Fächerdaten, ob die Berechtigung zum Besuch der gymnasialen Oberstufe im Rahmen eines Mittlerer Schulabschlusses nach der Klasse 10 an einer Gesamtschule erreicht wird oder nicht.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbschlussErgebnis
	 *
	 * @param {GEAbschlussFaecher} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 */
	public async getGesamtschuleAbschlussMSAQ(data : GEAbschlussFaecher) : Promise<AbschlussErgebnis> {
		const path = "/api/gesamtschule/abschluss/msaq";
		const body : string = GEAbschlussFaecher.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return AbschlussErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGesamtschuleAbschlussPrognose für den Zugriff auf die URL https://{hostname}/api/gesamtschule/abschluss/prognose
	 *
	 * Führt anhand der übergeben Fächerdaten eine Abschlussprognose für den Gesamtschulabschluss nach Klasse 9 bzw. Klasse 10 durch.Wird der Jahrgang 10 angegeben, so findet keine Prüfung auf den HA9 statt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Prognoseberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbschlussErgebnis
	 *
	 * @param {GEAbschlussFaecher} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Prognoseberechnung, ggf. mit Nachprüfungsmöglichkeiten
	 */
	public async getGesamtschuleAbschlussPrognose(data : GEAbschlussFaecher) : Promise<AbschlussErgebnis> {
		const path = "/api/gesamtschule/abschluss/prognose";
		const body : string = GEAbschlussFaecher.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return AbschlussErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostBelegpruefungEF1 für den Zugriff auf die URL https://{hostname}/api/gost/belegpruefung/EF1
	 *
	 * Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch Belegungsfehler und Hinweise zur Belegung zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBelegpruefungErgebnis
	 *
	 * @param {GostBelegpruefungsdaten} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 */
	public async getGostBelegpruefungEF1(data : GostBelegpruefungsdaten) : Promise<GostBelegpruefungErgebnis> {
		const path = "/api/gost/belegpruefung/EF1";
		const body : string = GostBelegpruefungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBelegpruefungErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostBelegpruefungGesamt für den Zugriff auf die URL https://{hostname}/api/gost/belegpruefung/gesamt
	 *
	 * Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch Belegungsfehler und Hinweise zur Belegung zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBelegpruefungErgebnis
	 *
	 * @param {GostBelegpruefungsdaten} data - der Request-Body für die HTTP-Methode
	 *
	 * @returns Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 */
	public async getGostBelegpruefungGesamt(data : GostBelegpruefungsdaten) : Promise<GostBelegpruefungErgebnis> {
		const path = "/api/gost/belegpruefung/gesamt";
		const body : string = GostBelegpruefungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBelegpruefungErgebnis.transpilerFromJSON(text);
	}


}
