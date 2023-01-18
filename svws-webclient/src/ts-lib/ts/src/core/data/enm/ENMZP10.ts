import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMZP10 extends JavaObject {

	/**
	 * Die ID des Faches der zentralen Prüfungen 
	 */
	public fachID : number = 0;

	/**
	 * Das Kürzel der Vornote für dieses Fach 
	 */
	public vornote : String | null = null;

	/**
	 * Das Kürzel der Note,die bei der schriftlichen Prüfung erreicht wurde 
	 */
	public noteSchriftlichePruefung : String | null = null;

	/**
	 * Gibt an, ob eine mündliche Prüfung stattfinden muss 
	 */
	public muendlichePruefung : boolean = false;

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung stattfindet 
	 */
	public muendlichePruefungFreiwillig : boolean = false;

	/**
	 * Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet 
	 */
	public noteMuendlichePruefung : String | null = null;

	/**
	 * Das Kürzel der Abschlussnote nach der ZP10-Prüfung  
	 */
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
		result.vornote = typeof obj.vornote === "undefined" ? null : obj.vornote === null ? null : String(obj.vornote);
		result.noteSchriftlichePruefung = typeof obj.noteSchriftlichePruefung === "undefined" ? null : obj.noteSchriftlichePruefung === null ? null : String(obj.noteSchriftlichePruefung);
		if (typeof obj.muendlichePruefung === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (typeof obj.muendlichePruefungFreiwillig === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = typeof obj.noteMuendlichePruefung === "undefined" ? null : obj.noteMuendlichePruefung === null ? null : String(obj.noteMuendlichePruefung);
		result.abschlussnote = typeof obj.abschlussnote === "undefined" ? null : obj.abschlussnote === null ? null : String(obj.abschlussnote);
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
