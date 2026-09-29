import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Abschlussdaten extends JavaObject {

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 */
	public idLernabschnitt: number = 0;

	/**
	 * Die Prüfungsordnung, welche der Abschlussberechnung zugrunde liegt.
	 */
	public pruefungsordnung: string | null = null;

	/**
	 * Die ID des erreichten allgemeinbildende Abschlusses
	 */
	public idAbschluss: number | null = null;

	/**
	 * Die ID des erreichten berufsbezogenen Abschlusses am Berufskolleg
	 */
	public idAbschlussBerufsbildend: number | null = null;

	/**
	 * Gibt an, ob es sich bei dem Abschluss um eine Prognose handelt oder nicht
	 */
	public istAbschlussPrognose: boolean = false;

	/**
	 * Die Art des Abschlusses für den Lernabschnitt (0 = Jahrgang ohne Abschluss, 1 = Abschluss erreicht, 2 = ohne Abschluss, 3 = ohne Abschluss mit Nachprüfung)
	 */
	public idAbschlussart: number | null = null;

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 */
	public textErgebnisPruefungsalgorithmus: string | null = null;

	/**
	 * Die ID des bei der auf Quartalsnoten basierenden Prognose erreichten allgemeinbildende Abschlusses
	 */
	public idAbschlussQuartalsprognose: number | null = null;

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus bei der auf Quartalsnoten basierenden Prognose für die Abschlussberechnung
	 */
	public textErgebniseQuartalsprognose: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.abschluss.Abschlussdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.abschluss.Abschlussdaten'].includes(name);
	}

	public static readonly class = new Class<Abschlussdaten>('de.svws_nrw.core.data.abschluss.Abschlussdaten');

	public static transpilerFromJSON(json: string): Abschlussdaten {
		const obj = JSON.parse(json) as Partial<Abschlussdaten>;
		const result = new Abschlussdaten();
		if (obj.idLernabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idLernabschnitt');
		result.idLernabschnitt = obj.idLernabschnitt;
		result.pruefungsordnung = (obj.pruefungsordnung === undefined) ? null : obj.pruefungsordnung === null ? null : obj.pruefungsordnung;
		result.idAbschluss = (obj.idAbschluss === undefined) ? null : obj.idAbschluss === null ? null : obj.idAbschluss;
		result.idAbschlussBerufsbildend = (obj.idAbschlussBerufsbildend === undefined) ? null : obj.idAbschlussBerufsbildend === null ? null : obj.idAbschlussBerufsbildend;
		if (obj.istAbschlussPrognose === undefined)
			throw new Error('invalid json format, missing attribute istAbschlussPrognose');
		result.istAbschlussPrognose = obj.istAbschlussPrognose;
		result.idAbschlussart = (obj.idAbschlussart === undefined) ? null : obj.idAbschlussart === null ? null : obj.idAbschlussart;
		result.textErgebnisPruefungsalgorithmus = (obj.textErgebnisPruefungsalgorithmus === undefined) ? null : obj.textErgebnisPruefungsalgorithmus === null ? null : obj.textErgebnisPruefungsalgorithmus;
		result.idAbschlussQuartalsprognose = (obj.idAbschlussQuartalsprognose === undefined) ? null : obj.idAbschlussQuartalsprognose === null ? null : obj.idAbschlussQuartalsprognose;
		result.textErgebniseQuartalsprognose = (obj.textErgebniseQuartalsprognose === undefined) ? null : obj.textErgebniseQuartalsprognose === null ? null : obj.textErgebniseQuartalsprognose;
		return result;
	}

	public static transpilerToJSON(obj: Abschlussdaten): string {
		let result = '{';
		result += '"idLernabschnitt" : ' + obj.idLernabschnitt.toString() + ',';
		result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		result += '"idAbschluss" : ' + ((obj.idAbschluss === null) ? 'null' : obj.idAbschluss.toString()) + ',';
		result += '"idAbschlussBerufsbildend" : ' + ((obj.idAbschlussBerufsbildend === null) ? 'null' : obj.idAbschlussBerufsbildend.toString()) + ',';
		result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose.toString() + ',';
		result += '"idAbschlussart" : ' + ((obj.idAbschlussart === null) ? 'null' : obj.idAbschlussart.toString()) + ',';
		result += '"textErgebnisPruefungsalgorithmus" : ' + ((obj.textErgebnisPruefungsalgorithmus === null) ? 'null' : JSON.stringify(obj.textErgebnisPruefungsalgorithmus)) + ',';
		result += '"idAbschlussQuartalsprognose" : ' + ((obj.idAbschlussQuartalsprognose === null) ? 'null' : obj.idAbschlussQuartalsprognose.toString()) + ',';
		result += '"textErgebniseQuartalsprognose" : ' + ((obj.textErgebniseQuartalsprognose === null) ? 'null' : JSON.stringify(obj.textErgebniseQuartalsprognose)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Abschlussdaten>): string {
		let result = '{';
		if (obj.idLernabschnitt !== undefined) {
			result += '"idLernabschnitt" : ' + obj.idLernabschnitt.toString() + ',';
		}
		if (obj.pruefungsordnung !== undefined) {
			result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		}
		if (obj.idAbschluss !== undefined) {
			result += '"idAbschluss" : ' + ((obj.idAbschluss === null) ? 'null' : obj.idAbschluss.toString()) + ',';
		}
		if (obj.idAbschlussBerufsbildend !== undefined) {
			result += '"idAbschlussBerufsbildend" : ' + ((obj.idAbschlussBerufsbildend === null) ? 'null' : obj.idAbschlussBerufsbildend.toString()) + ',';
		}
		if (obj.istAbschlussPrognose !== undefined) {
			result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose.toString() + ',';
		}
		if (obj.idAbschlussart !== undefined) {
			result += '"idAbschlussart" : ' + ((obj.idAbschlussart === null) ? 'null' : obj.idAbschlussart.toString()) + ',';
		}
		if (obj.textErgebnisPruefungsalgorithmus !== undefined) {
			result += '"textErgebnisPruefungsalgorithmus" : ' + ((obj.textErgebnisPruefungsalgorithmus === null) ? 'null' : JSON.stringify(obj.textErgebnisPruefungsalgorithmus)) + ',';
		}
		if (obj.idAbschlussQuartalsprognose !== undefined) {
			result += '"idAbschlussQuartalsprognose" : ' + ((obj.idAbschlussQuartalsprognose === null) ? 'null' : obj.idAbschlussQuartalsprognose.toString()) + ',';
		}
		if (obj.textErgebniseQuartalsprognose !== undefined) {
			result += '"textErgebniseQuartalsprognose" : ' + ((obj.textErgebniseQuartalsprognose === null) ? 'null' : JSON.stringify(obj.textErgebniseQuartalsprognose)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_abschluss_Abschlussdaten(obj: unknown): Abschlussdaten {
	return obj as Abschlussdaten;
}
