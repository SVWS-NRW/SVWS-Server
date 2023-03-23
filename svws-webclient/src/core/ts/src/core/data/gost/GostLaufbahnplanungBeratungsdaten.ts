import { JavaObject } from '../../../java/lang/JavaObject';

export class GostLaufbahnplanungBeratungsdaten extends JavaObject {

	/**
	 * Die ID des Beratungslehrers, der die letze Beratung durchgeführt hat
	 */
	public beratungslehrerID : number | null = null;

	/**
	 * Das Beratungsdatum der letzten Beratung im Rahmen der Laufbahnplanung
	 */
	public beratungsdatum : string | null = null;

	/**
	 * Ein Kommentar zur Beratung
	 */
	public kommentar : string | null = null;

	/**
	 * Das Rücklaufdatum des Wahlbogens der letzten Beratung im Rahmen der Laufbahnplanung
	 */
	public ruecklaufdatum : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostLaufbahnplanungBeratungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLaufbahnplanungBeratungsdaten {
		const obj = JSON.parse(json);
		const result = new GostLaufbahnplanungBeratungsdaten();
		result.beratungslehrerID = typeof obj.beratungslehrerID === "undefined" ? null : obj.beratungslehrerID === null ? null : obj.beratungslehrerID;
		result.beratungsdatum = typeof obj.beratungsdatum === "undefined" ? null : obj.beratungsdatum === null ? null : obj.beratungsdatum;
		result.kommentar = typeof obj.kommentar === "undefined" ? null : obj.kommentar === null ? null : obj.kommentar;
		result.ruecklaufdatum = typeof obj.ruecklaufdatum === "undefined" ? null : obj.ruecklaufdatum === null ? null : obj.ruecklaufdatum;
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungBeratungsdaten) : string {
		let result = '{';
		result += '"beratungslehrerID" : ' + ((!obj.beratungslehrerID) ? 'null' : obj.beratungslehrerID) + ',';
		result += '"beratungsdatum" : ' + ((!obj.beratungsdatum) ? 'null' : '"' + obj.beratungsdatum + '"') + ',';
		result += '"kommentar" : ' + ((!obj.kommentar) ? 'null' : '"' + obj.kommentar + '"') + ',';
		result += '"ruecklaufdatum" : ' + ((!obj.ruecklaufdatum) ? 'null' : '"' + obj.ruecklaufdatum + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungBeratungsdaten>) : string {
		let result = '{';
		if (typeof obj.beratungslehrerID !== "undefined") {
			result += '"beratungslehrerID" : ' + ((!obj.beratungslehrerID) ? 'null' : obj.beratungslehrerID) + ',';
		}
		if (typeof obj.beratungsdatum !== "undefined") {
			result += '"beratungsdatum" : ' + ((!obj.beratungsdatum) ? 'null' : '"' + obj.beratungsdatum + '"') + ',';
		}
		if (typeof obj.kommentar !== "undefined") {
			result += '"kommentar" : ' + ((!obj.kommentar) ? 'null' : '"' + obj.kommentar + '"') + ',';
		}
		if (typeof obj.ruecklaufdatum !== "undefined") {
			result += '"ruecklaufdatum" : ' + ((!obj.ruecklaufdatum) ? 'null' : '"' + obj.ruecklaufdatum + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostLaufbahnplanungBeratungsdaten(obj : unknown) : GostLaufbahnplanungBeratungsdaten {
	return obj as GostLaufbahnplanungBeratungsdaten;
}
