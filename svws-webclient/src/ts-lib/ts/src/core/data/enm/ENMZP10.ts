import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMZP10 extends JavaObject {

	public fachID : number = 0;

	public vornote : String | null = null;

	public noteSchriftlichePruefung : String | null = null;

	public muendlichePruefung : boolean = false;

	public muendlichePruefungFreiwillig : boolean = false;

	public noteMuendlichePruefung : String | null = null;

	public abschlussnote : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMZP10'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMZP10 {
		const obj = JSON.parse(json);
		const result = new ENMZP10();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.vornote = typeof obj.vornote === "undefined" ? null : obj.vornote;
		result.noteSchriftlichePruefung = typeof obj.noteSchriftlichePruefung === "undefined" ? null : obj.noteSchriftlichePruefung;
		if (typeof obj.muendlichePruefung === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (typeof obj.muendlichePruefungFreiwillig === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = typeof obj.noteMuendlichePruefung === "undefined" ? null : obj.noteMuendlichePruefung;
		result.abschlussnote = typeof obj.abschlussnote === "undefined" ? null : obj.abschlussnote;
		return result;
	}

	public static transpilerToJSON(obj : ENMZP10) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"vornote" : ' + ((!obj.vornote) ? 'null' : '"' + obj.vornote.valueOf() + '"') + ',';
		result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : '"' + obj.noteSchriftlichePruefung.valueOf() + '"') + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : '"' + obj.noteMuendlichePruefung.valueOf() + '"') + ',';
		result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : '"' + obj.abschlussnote.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMZP10>) : string {
		let result = '{';
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.vornote !== "undefined") {
			result += '"vornote" : ' + ((!obj.vornote) ? 'null' : '"' + obj.vornote.valueOf() + '"') + ',';
		}
		if (typeof obj.noteSchriftlichePruefung !== "undefined") {
			result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : '"' + obj.noteSchriftlichePruefung.valueOf() + '"') + ',';
		}
		if (typeof obj.muendlichePruefung !== "undefined") {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		}
		if (typeof obj.muendlichePruefungFreiwillig !== "undefined") {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		}
		if (typeof obj.noteMuendlichePruefung !== "undefined") {
			result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : '"' + obj.noteMuendlichePruefung.valueOf() + '"') + ',';
		}
		if (typeof obj.abschlussnote !== "undefined") {
			result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : '"' + obj.abschlussnote.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMZP10(obj : unknown) : ENMZP10 {
	return obj as ENMZP10;
}
