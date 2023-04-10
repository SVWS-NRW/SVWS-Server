import { JavaObject } from '../../../java/lang/JavaObject';

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
	 * Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt
	 */
	public beratungshalbjahr : string = "";

	/**
	 * Der Text der Schule für den Beratungsbogen
	 */
	public beratungsbogentext : string = "";

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
		return ['de.svws_nrw.core.data.schild3.SchildReportingSchuelerGOStLaufbahnplanungGrunddaten'].includes(name);
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
		if (typeof obj.beratungshalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute beratungshalbjahr');
		result.beratungshalbjahr = obj.beratungshalbjahr;
		if (typeof obj.beratungsbogentext === "undefined")
			 throw new Error('invalid json format, missing attribute beratungsbogentext');
		result.beratungsbogentext = obj.beratungsbogentext;
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
		result += '"beratungshalbjahr" : ' + '"' + obj.beratungshalbjahr! + '"' + ',';
		result += '"beratungsbogentext" : ' + '"' + obj.beratungsbogentext! + '"' + ',';
		result += '"beratungslehrkraefte" : ' + '"' + obj.beratungslehrkraefte! + '"' + ',';
		result += '"beratungslehrkraft" : ' + '"' + obj.beratungslehrkraft! + '"' + ',';
		result += '"ruecklaufdatum" : ' + '"' + obj.ruecklaufdatum! + '"' + ',';
		result += '"beratungsdatum" : ' + '"' + obj.beratungsdatum! + '"' + ',';
		result += '"kommentar" : ' + '"' + obj.kommentar! + '"' + ',';
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
		if (typeof obj.beratungshalbjahr !== "undefined") {
			result += '"beratungshalbjahr" : ' + '"' + obj.beratungshalbjahr + '"' + ',';
		}
		if (typeof obj.beratungsbogentext !== "undefined") {
			result += '"beratungsbogentext" : ' + '"' + obj.beratungsbogentext + '"' + ',';
		}
		if (typeof obj.beratungslehrkraefte !== "undefined") {
			result += '"beratungslehrkraefte" : ' + '"' + obj.beratungslehrkraefte + '"' + ',';
		}
		if (typeof obj.beratungslehrkraft !== "undefined") {
			result += '"beratungslehrkraft" : ' + '"' + obj.beratungslehrkraft + '"' + ',';
		}
		if (typeof obj.ruecklaufdatum !== "undefined") {
			result += '"ruecklaufdatum" : ' + '"' + obj.ruecklaufdatum + '"' + ',';
		}
		if (typeof obj.beratungsdatum !== "undefined") {
			result += '"beratungsdatum" : ' + '"' + obj.beratungsdatum + '"' + ',';
		}
		if (typeof obj.kommentar !== "undefined") {
			result += '"kommentar" : ' + '"' + obj.kommentar + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_SchildReportingSchuelerGOStLaufbahnplanungGrunddaten(obj : unknown) : SchildReportingSchuelerGOStLaufbahnplanungGrunddaten {
	return obj as SchildReportingSchuelerGOStLaufbahnplanungGrunddaten;
}
