import { JavaObject } from '../../../../java/lang/JavaObject';

export class SchildReportingSchuelerGOStLaufbahnplanungGrunddaten extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 */
	public abiturjahr : number = 0;

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt
	 */
	public aktuellesGOStHalbjahr : string = "";

	/**
	 * Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 */
	public aktuelleKlasse : string = "";

	/**
	 * Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 */
	public pruefungsordnung : string = "";

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt
	 */
	public beratungsGOStHalbjahr : string = "";

	/**
	 * Der Text der Schule für den Beratungsbogen
	 */
	public beratungsbogentext : string = "";

	/**
	 * Der Text der Schule für den E-Mail-Versand
	 */
	public emailtext : string = "";

	/**
	 * Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 */
	public beratungslehrkraefte : string = "";

	/**
	 * Die Lehrkraft der letzten Beratung
	 */
	public beratungslehrkraft : string = "";

	/**
	 * Das Datum des Rücklaufes der letzten importierten Wahldatei
	 */
	public ruecklaufdatum : string = "";

	/**
	 * Das Datum der letzten Beratung
	 */
	public beratungsdatum : string = "";

	/**
	 * Kommentar der Schule zur Laufbahn
	 */
	public kommentar : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungGrunddaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerGOStLaufbahnplanungGrunddaten {
		const obj = JSON.parse(json);
		const result = new SchildReportingSchuelerGOStLaufbahnplanungGrunddaten();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (typeof obj.aktuellesGOStHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute aktuellesGOStHalbjahr');
		result.aktuellesGOStHalbjahr = obj.aktuellesGOStHalbjahr;
		if (typeof obj.aktuelleKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute aktuelleKlasse');
		result.aktuelleKlasse = obj.aktuelleKlasse;
		if (typeof obj.pruefungsordnung === "undefined")
			 throw new Error('invalid json format, missing attribute pruefungsordnung');
		result.pruefungsordnung = obj.pruefungsordnung;
		if (typeof obj.beratungsGOStHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute beratungsGOStHalbjahr');
		result.beratungsGOStHalbjahr = obj.beratungsGOStHalbjahr;
		if (typeof obj.beratungsbogentext === "undefined")
			 throw new Error('invalid json format, missing attribute beratungsbogentext');
		result.beratungsbogentext = obj.beratungsbogentext;
		if (typeof obj.emailtext === "undefined")
			 throw new Error('invalid json format, missing attribute emailtext');
		result.emailtext = obj.emailtext;
		if (typeof obj.beratungslehrkraefte === "undefined")
			 throw new Error('invalid json format, missing attribute beratungslehrkraefte');
		result.beratungslehrkraefte = obj.beratungslehrkraefte;
		if (typeof obj.beratungslehrkraft === "undefined")
			 throw new Error('invalid json format, missing attribute beratungslehrkraft');
		result.beratungslehrkraft = obj.beratungslehrkraft;
		if (typeof obj.ruecklaufdatum === "undefined")
			 throw new Error('invalid json format, missing attribute ruecklaufdatum');
		result.ruecklaufdatum = obj.ruecklaufdatum;
		if (typeof obj.beratungsdatum === "undefined")
			 throw new Error('invalid json format, missing attribute beratungsdatum');
		result.beratungsdatum = obj.beratungsdatum;
		if (typeof obj.kommentar === "undefined")
			 throw new Error('invalid json format, missing attribute kommentar');
		result.kommentar = obj.kommentar;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerGOStLaufbahnplanungGrunddaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"aktuellesGOStHalbjahr" : ' + JSON.stringify(obj.aktuellesGOStHalbjahr!) + ',';
		result += '"aktuelleKlasse" : ' + JSON.stringify(obj.aktuelleKlasse!) + ',';
		result += '"pruefungsordnung" : ' + JSON.stringify(obj.pruefungsordnung!) + ',';
		result += '"beratungsGOStHalbjahr" : ' + JSON.stringify(obj.beratungsGOStHalbjahr!) + ',';
		result += '"beratungsbogentext" : ' + JSON.stringify(obj.beratungsbogentext!) + ',';
		result += '"emailtext" : ' + JSON.stringify(obj.emailtext!) + ',';
		result += '"beratungslehrkraefte" : ' + JSON.stringify(obj.beratungslehrkraefte!) + ',';
		result += '"beratungslehrkraft" : ' + JSON.stringify(obj.beratungslehrkraft!) + ',';
		result += '"ruecklaufdatum" : ' + JSON.stringify(obj.ruecklaufdatum!) + ',';
		result += '"beratungsdatum" : ' + JSON.stringify(obj.beratungsdatum!) + ',';
		result += '"kommentar" : ' + JSON.stringify(obj.kommentar!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerGOStLaufbahnplanungGrunddaten>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.aktuellesGOStHalbjahr !== "undefined") {
			result += '"aktuellesGOStHalbjahr" : ' + JSON.stringify(obj.aktuellesGOStHalbjahr!) + ',';
		}
		if (typeof obj.aktuelleKlasse !== "undefined") {
			result += '"aktuelleKlasse" : ' + JSON.stringify(obj.aktuelleKlasse!) + ',';
		}
		if (typeof obj.pruefungsordnung !== "undefined") {
			result += '"pruefungsordnung" : ' + JSON.stringify(obj.pruefungsordnung!) + ',';
		}
		if (typeof obj.beratungsGOStHalbjahr !== "undefined") {
			result += '"beratungsGOStHalbjahr" : ' + JSON.stringify(obj.beratungsGOStHalbjahr!) + ',';
		}
		if (typeof obj.beratungsbogentext !== "undefined") {
			result += '"beratungsbogentext" : ' + JSON.stringify(obj.beratungsbogentext!) + ',';
		}
		if (typeof obj.emailtext !== "undefined") {
			result += '"emailtext" : ' + JSON.stringify(obj.emailtext!) + ',';
		}
		if (typeof obj.beratungslehrkraefte !== "undefined") {
			result += '"beratungslehrkraefte" : ' + JSON.stringify(obj.beratungslehrkraefte!) + ',';
		}
		if (typeof obj.beratungslehrkraft !== "undefined") {
			result += '"beratungslehrkraft" : ' + JSON.stringify(obj.beratungslehrkraft!) + ',';
		}
		if (typeof obj.ruecklaufdatum !== "undefined") {
			result += '"ruecklaufdatum" : ' + JSON.stringify(obj.ruecklaufdatum!) + ',';
		}
		if (typeof obj.beratungsdatum !== "undefined") {
			result += '"beratungsdatum" : ' + JSON.stringify(obj.beratungsdatum!) + ',';
		}
		if (typeof obj.kommentar !== "undefined") {
			result += '"kommentar" : ' + JSON.stringify(obj.kommentar!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingSchuelerGOStLaufbahnplanungGrunddaten(obj : unknown) : SchildReportingSchuelerGOStLaufbahnplanungGrunddaten {
	return obj as SchildReportingSchuelerGOStLaufbahnplanungGrunddaten;
}
