import { JavaObject } from '../../../../java/lang/JavaObject';

export class SchildReportingSchuelerLernabschnitt extends JavaObject {

	/**
	 * Die ID des Lernabschnitts
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers, zu dem die Lernabschnittsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 */
	public schuljahr : number = 0;

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 */
	public abschnitt : number = 0;

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitte in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 */
	public wechselNr : number = 0;

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 */
	public istGewertet : boolean = true;

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 */
	public istWiederholung : boolean = false;

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 */
	public pruefungsOrdnung : string = "";

	/**
	 * Die Bezeichnung der Klasse des Schülers
	 */
	public klasse : string = "";

	/**
	 * Die Statistik-Bezeichnung der Klasse des Schülers
	 */
	public klasseStatistik : string = "";

	/**
	 * Die Bezeichnung des Jahrgangs
	 */
	public jahrgang : string = "";

	/**
	 * Die Statistik-Bezeichnung des Jahrgangs
	 */
	public jahrgangStatistik : string = "";

	/**
	 * Das Datum der Zeugniskonferenz
	 */
	public datumZeugniskonferenz : string = "";

	/**
	 * Das Datum des Zeugnisses
	 */
	public datumZeugnis : string = "";

	/**
	 * Das Ergebnis des Prüfungsalgorithmus
	 */
	public logPruefungsalgorithmus : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLernabschnitt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLernabschnitt'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerLernabschnitt {
		const obj = JSON.parse(json) as Partial<SchildReportingSchuelerLernabschnitt>;
		const result = new SchildReportingSchuelerLernabschnitt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.wechselNr === undefined)
			throw new Error('invalid json format, missing attribute wechselNr');
		result.wechselNr = obj.wechselNr;
		if (obj.istGewertet === undefined)
			throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (obj.istWiederholung === undefined)
			throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (obj.pruefungsOrdnung === undefined)
			throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		if (obj.klasse === undefined)
			throw new Error('invalid json format, missing attribute klasse');
		result.klasse = obj.klasse;
		if (obj.klasseStatistik === undefined)
			throw new Error('invalid json format, missing attribute klasseStatistik');
		result.klasseStatistik = obj.klasseStatistik;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.jahrgangStatistik === undefined)
			throw new Error('invalid json format, missing attribute jahrgangStatistik');
		result.jahrgangStatistik = obj.jahrgangStatistik;
		if (obj.datumZeugniskonferenz === undefined)
			throw new Error('invalid json format, missing attribute datumZeugniskonferenz');
		result.datumZeugniskonferenz = obj.datumZeugniskonferenz;
		if (obj.datumZeugnis === undefined)
			throw new Error('invalid json format, missing attribute datumZeugnis');
		result.datumZeugnis = obj.datumZeugnis;
		if (obj.logPruefungsalgorithmus === undefined)
			throw new Error('invalid json format, missing attribute logPruefungsalgorithmus');
		result.logPruefungsalgorithmus = obj.logPruefungsalgorithmus;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerLernabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		result += '"klasse" : ' + JSON.stringify(obj.klasse) + ',';
		result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"jahrgangStatistik" : ' + JSON.stringify(obj.jahrgangStatistik) + ',';
		result += '"datumZeugniskonferenz" : ' + JSON.stringify(obj.datumZeugniskonferenz) + ',';
		result += '"datumZeugnis" : ' + JSON.stringify(obj.datumZeugnis) + ',';
		result += '"logPruefungsalgorithmus" : ' + JSON.stringify(obj.logPruefungsalgorithmus) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerLernabschnitt>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.wechselNr !== undefined) {
			result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		}
		if (obj.istGewertet !== undefined) {
			result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		}
		if (obj.istWiederholung !== undefined) {
			result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		}
		if (obj.pruefungsOrdnung !== undefined) {
			result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		}
		if (obj.klasse !== undefined) {
			result += '"klasse" : ' + JSON.stringify(obj.klasse) + ',';
		}
		if (obj.klasseStatistik !== undefined) {
			result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		if (obj.jahrgangStatistik !== undefined) {
			result += '"jahrgangStatistik" : ' + JSON.stringify(obj.jahrgangStatistik) + ',';
		}
		if (obj.datumZeugniskonferenz !== undefined) {
			result += '"datumZeugniskonferenz" : ' + JSON.stringify(obj.datumZeugniskonferenz) + ',';
		}
		if (obj.datumZeugnis !== undefined) {
			result += '"datumZeugnis" : ' + JSON.stringify(obj.datumZeugnis) + ',';
		}
		if (obj.logPruefungsalgorithmus !== undefined) {
			result += '"logPruefungsalgorithmus" : ' + JSON.stringify(obj.logPruefungsalgorithmus) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingSchuelerLernabschnitt(obj : unknown) : SchildReportingSchuelerLernabschnitt {
	return obj as SchildReportingSchuelerLernabschnitt;
}
