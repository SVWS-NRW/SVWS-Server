import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachpruefung extends JavaObject {

	public sprache : String | null = null;

	public jahrgang : String | null = null;

	public anspruchsniveauId : Number | null = null;

	public ersetzteSprache : String | null = null;

	public istHSUPruefung : boolean = false;

	public istFeststellungspruefung : boolean = false;

	public kannErstePflichtfremdspracheErsetzen : boolean = false;

	public kannZweitePflichtfremdspracheErsetzen : boolean = false;

	public kannWahlpflichtfremdspracheErsetzen : boolean = false;

	public kannBelegungAlsFortgefuehrteSpracheErlauben : boolean = false;

	public referenzniveau : String | null = null;

	public note : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.Sprachpruefung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachpruefung {
		const obj = JSON.parse(json);
		const result = new Sprachpruefung();
		result.sprache = typeof obj.sprache === "undefined" ? null : obj.sprache;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang;
		result.anspruchsniveauId = typeof obj.anspruchsniveauId === "undefined" ? null : obj.anspruchsniveauId;
		result.ersetzteSprache = typeof obj.ersetzteSprache === "undefined" ? null : obj.ersetzteSprache;
		if (typeof obj.istHSUPruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istHSUPruefung');
		result.istHSUPruefung = obj.istHSUPruefung;
		if (typeof obj.istFeststellungspruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istFeststellungspruefung');
		result.istFeststellungspruefung = obj.istFeststellungspruefung;
		if (typeof obj.kannErstePflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannErstePflichtfremdspracheErsetzen');
		result.kannErstePflichtfremdspracheErsetzen = obj.kannErstePflichtfremdspracheErsetzen;
		if (typeof obj.kannZweitePflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannZweitePflichtfremdspracheErsetzen');
		result.kannZweitePflichtfremdspracheErsetzen = obj.kannZweitePflichtfremdspracheErsetzen;
		if (typeof obj.kannWahlpflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannWahlpflichtfremdspracheErsetzen');
		result.kannWahlpflichtfremdspracheErsetzen = obj.kannWahlpflichtfremdspracheErsetzen;
		if (typeof obj.kannBelegungAlsFortgefuehrteSpracheErlauben === "undefined")
			 throw new Error('invalid json format, missing attribute kannBelegungAlsFortgefuehrteSpracheErlauben');
		result.kannBelegungAlsFortgefuehrteSpracheErlauben = obj.kannBelegungAlsFortgefuehrteSpracheErlauben;
		result.referenzniveau = typeof obj.referenzniveau === "undefined" ? null : obj.referenzniveau;
		result.note = typeof obj.note === "undefined" ? null : obj.note;
		return result;
	}

	public static transpilerToJSON(obj : Sprachpruefung) : string {
		let result = '{';
		result += '"sprache" : ' + ((!obj.sprache) ? 'null' : '"' + obj.sprache.valueOf() + '"') + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		result += '"anspruchsniveauId" : ' + ((!obj.anspruchsniveauId) ? 'null' : obj.anspruchsniveauId.valueOf()) + ',';
		result += '"ersetzteSprache" : ' + ((!obj.ersetzteSprache) ? 'null' : '"' + obj.ersetzteSprache.valueOf() + '"') + ',';
		result += '"istHSUPruefung" : ' + obj.istHSUPruefung + ',';
		result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung + ',';
		result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen + ',';
		result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen + ',';
		result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen + ',';
		result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : obj.note.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachpruefung>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + ((!obj.sprache) ? 'null' : '"' + obj.sprache.valueOf() + '"') + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.anspruchsniveauId !== "undefined") {
			result += '"anspruchsniveauId" : ' + ((!obj.anspruchsniveauId) ? 'null' : obj.anspruchsniveauId.valueOf()) + ',';
		}
		if (typeof obj.ersetzteSprache !== "undefined") {
			result += '"ersetzteSprache" : ' + ((!obj.ersetzteSprache) ? 'null' : '"' + obj.ersetzteSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.istHSUPruefung !== "undefined") {
			result += '"istHSUPruefung" : ' + obj.istHSUPruefung + ',';
		}
		if (typeof obj.istFeststellungspruefung !== "undefined") {
			result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung + ',';
		}
		if (typeof obj.kannErstePflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannZweitePflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannWahlpflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannBelegungAlsFortgefuehrteSpracheErlauben !== "undefined") {
			result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : obj.note.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_Sprachpruefung(obj : unknown) : Sprachpruefung {
	return obj as Sprachpruefung;
}
