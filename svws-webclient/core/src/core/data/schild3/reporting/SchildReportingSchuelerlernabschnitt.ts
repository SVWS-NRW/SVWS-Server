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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLernabschnitt'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerLernabschnitt {
		const obj = JSON.parse(json);
		const result = new SchildReportingSchuelerLernabschnitt();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.schuljahr === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (typeof obj.abschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (typeof obj.wechselNr === "undefined")
			 throw new Error('invalid json format, missing attribute wechselNr');
		result.wechselNr = obj.wechselNr;
		if (typeof obj.istGewertet === "undefined")
			 throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (typeof obj.istWiederholung === "undefined")
			 throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (typeof obj.pruefungsOrdnung === "undefined")
			 throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		if (typeof obj.klasse === "undefined")
			 throw new Error('invalid json format, missing attribute klasse');
		result.klasse = obj.klasse;
		if (typeof obj.klasseStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute klasseStatistik');
		result.klasseStatistik = obj.klasseStatistik;
		if (typeof obj.jahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (typeof obj.jahrgangStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgangStatistik');
		result.jahrgangStatistik = obj.jahrgangStatistik;
		if (typeof obj.datumZeugniskonferenz === "undefined")
			 throw new Error('invalid json format, missing attribute datumZeugniskonferenz');
		result.datumZeugniskonferenz = obj.datumZeugniskonferenz;
		if (typeof obj.datumZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute datumZeugnis');
		result.datumZeugnis = obj.datumZeugnis;
		if (typeof obj.logPruefungsalgorithmus === "undefined")
			 throw new Error('invalid json format, missing attribute logPruefungsalgorithmus');
		result.logPruefungsalgorithmus = obj.logPruefungsalgorithmus;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerLernabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"abschnitt" : ' + obj.abschnitt + ',';
		result += '"wechselNr" : ' + obj.wechselNr + ',';
		result += '"istGewertet" : ' + obj.istGewertet + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung!) + ',';
		result += '"klasse" : ' + JSON.stringify(obj.klasse!) + ',';
		result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik!) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang!) + ',';
		result += '"jahrgangStatistik" : ' + JSON.stringify(obj.jahrgangStatistik!) + ',';
		result += '"datumZeugniskonferenz" : ' + JSON.stringify(obj.datumZeugniskonferenz!) + ',';
		result += '"datumZeugnis" : ' + JSON.stringify(obj.datumZeugnis!) + ',';
		result += '"logPruefungsalgorithmus" : ' + JSON.stringify(obj.logPruefungsalgorithmus!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerLernabschnitt>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.schuljahr !== "undefined") {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (typeof obj.abschnitt !== "undefined") {
			result += '"abschnitt" : ' + obj.abschnitt + ',';
		}
		if (typeof obj.wechselNr !== "undefined") {
			result += '"wechselNr" : ' + obj.wechselNr + ',';
		}
		if (typeof obj.istGewertet !== "undefined") {
			result += '"istGewertet" : ' + obj.istGewertet + ',';
		}
		if (typeof obj.istWiederholung !== "undefined") {
			result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		}
		if (typeof obj.pruefungsOrdnung !== "undefined") {
			result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung!) + ',';
		}
		if (typeof obj.klasse !== "undefined") {
			result += '"klasse" : ' + JSON.stringify(obj.klasse!) + ',';
		}
		if (typeof obj.klasseStatistik !== "undefined") {
			result += '"klasseStatistik" : ' + JSON.stringify(obj.klasseStatistik!) + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang!) + ',';
		}
		if (typeof obj.jahrgangStatistik !== "undefined") {
			result += '"jahrgangStatistik" : ' + JSON.stringify(obj.jahrgangStatistik!) + ',';
		}
		if (typeof obj.datumZeugniskonferenz !== "undefined") {
			result += '"datumZeugniskonferenz" : ' + JSON.stringify(obj.datumZeugniskonferenz!) + ',';
		}
		if (typeof obj.datumZeugnis !== "undefined") {
			result += '"datumZeugnis" : ' + JSON.stringify(obj.datumZeugnis!) + ',';
		}
		if (typeof obj.logPruefungsalgorithmus !== "undefined") {
			result += '"logPruefungsalgorithmus" : ' + JSON.stringify(obj.logPruefungsalgorithmus!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingSchuelerLernabschnitt(obj : unknown) : SchildReportingSchuelerLernabschnitt {
	return obj as SchildReportingSchuelerLernabschnitt;
}
