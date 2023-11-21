import { JavaObject } from '../../../../java/lang/JavaObject';

export class SchildReportingGostLaufbahnplanungSchuelerHinweise extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Fehler aus der Belegprüfung
	 */
	public belegungshinweis : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerHinweise'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingGostLaufbahnplanungSchuelerHinweise {
		const obj = JSON.parse(json);
		const result = new SchildReportingGostLaufbahnplanungSchuelerHinweise();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.belegungshinweis === "undefined")
			 throw new Error('invalid json format, missing attribute belegungshinweis');
		result.belegungshinweis = obj.belegungshinweis;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingGostLaufbahnplanungSchuelerHinweise) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"belegungshinweis" : ' + JSON.stringify(obj.belegungshinweis!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingGostLaufbahnplanungSchuelerHinweise>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.belegungshinweis !== "undefined") {
			result += '"belegungshinweis" : ' + JSON.stringify(obj.belegungshinweis!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingGostLaufbahnplanungSchuelerHinweise(obj : unknown) : SchildReportingGostLaufbahnplanungSchuelerHinweise {
	return obj as SchildReportingGostLaufbahnplanungSchuelerHinweise;
}
