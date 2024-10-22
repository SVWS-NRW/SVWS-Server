import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten'].includes(name);
	}

	public static class = new Class<GostLaufbahnplanungBeratungsdaten>('de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten');

	public static transpilerFromJSON(json : string): GostLaufbahnplanungBeratungsdaten {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungBeratungsdaten>;
		const result = new GostLaufbahnplanungBeratungsdaten();
		result.beratungslehrerID = (obj.beratungslehrerID === undefined) ? null : obj.beratungslehrerID === null ? null : obj.beratungslehrerID;
		result.beratungsdatum = (obj.beratungsdatum === undefined) ? null : obj.beratungsdatum === null ? null : obj.beratungsdatum;
		result.kommentar = (obj.kommentar === undefined) ? null : obj.kommentar === null ? null : obj.kommentar;
		result.ruecklaufdatum = (obj.ruecklaufdatum === undefined) ? null : obj.ruecklaufdatum === null ? null : obj.ruecklaufdatum;
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungBeratungsdaten) : string {
		let result = '{';
		result += '"beratungslehrerID" : ' + ((obj.beratungslehrerID === null) ? 'null' : obj.beratungslehrerID.toString()) + ',';
		result += '"beratungsdatum" : ' + ((obj.beratungsdatum === null) ? 'null' : JSON.stringify(obj.beratungsdatum)) + ',';
		result += '"kommentar" : ' + ((obj.kommentar === null) ? 'null' : JSON.stringify(obj.kommentar)) + ',';
		result += '"ruecklaufdatum" : ' + ((obj.ruecklaufdatum === null) ? 'null' : JSON.stringify(obj.ruecklaufdatum)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungBeratungsdaten>) : string {
		let result = '{';
		if (obj.beratungslehrerID !== undefined) {
			result += '"beratungslehrerID" : ' + ((obj.beratungslehrerID === null) ? 'null' : obj.beratungslehrerID.toString()) + ',';
		}
		if (obj.beratungsdatum !== undefined) {
			result += '"beratungsdatum" : ' + ((obj.beratungsdatum === null) ? 'null' : JSON.stringify(obj.beratungsdatum)) + ',';
		}
		if (obj.kommentar !== undefined) {
			result += '"kommentar" : ' + ((obj.kommentar === null) ? 'null' : JSON.stringify(obj.kommentar)) + ',';
		}
		if (obj.ruecklaufdatum !== undefined) {
			result += '"ruecklaufdatum" : ' + ((obj.ruecklaufdatum === null) ? 'null' : JSON.stringify(obj.ruecklaufdatum)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostLaufbahnplanungBeratungsdaten(obj : unknown) : GostLaufbahnplanungBeratungsdaten {
	return obj as GostLaufbahnplanungBeratungsdaten;
}
