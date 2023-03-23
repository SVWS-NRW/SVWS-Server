import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMZP10 extends JavaObject {

	/**
	 * Die ID des Faches der zentralen Prüfungen
	 */
	public fachID : number = 0;

	/**
	 * Das Kürzel der Vornote für dieses Fach
	 */
	public vornote : string | null = null;

	/**
	 * Das Kürzel der Note,die bei der schriftlichen Prüfung erreicht wurde
	 */
	public noteSchriftlichePruefung : string | null = null;

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
	public noteMuendlichePruefung : string | null = null;

	/**
	 * Das Kürzel der Abschlussnote nach der ZP10-Prüfung
	 */
	public abschlussnote : string | null = null;


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
		result.vornote = typeof obj.vornote === "undefined" ? null : obj.vornote === null ? null : obj.vornote;
		result.noteSchriftlichePruefung = typeof obj.noteSchriftlichePruefung === "undefined" ? null : obj.noteSchriftlichePruefung === null ? null : obj.noteSchriftlichePruefung;
		if (typeof obj.muendlichePruefung === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (typeof obj.muendlichePruefungFreiwillig === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = typeof obj.noteMuendlichePruefung === "undefined" ? null : obj.noteMuendlichePruefung === null ? null : obj.noteMuendlichePruefung;
		result.abschlussnote = typeof obj.abschlussnote === "undefined" ? null : obj.abschlussnote === null ? null : obj.abschlussnote;
		return result;
	}

	public static transpilerToJSON(obj : ENMZP10) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"vornote" : ' + ((!obj.vornote) ? 'null' : '"' + obj.vornote + '"') + ',';
		result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : '"' + obj.noteSchriftlichePruefung + '"') + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : '"' + obj.noteMuendlichePruefung + '"') + ',';
		result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : '"' + obj.abschlussnote + '"') + ',';
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
			result += '"vornote" : ' + ((!obj.vornote) ? 'null' : '"' + obj.vornote + '"') + ',';
		}
		if (typeof obj.noteSchriftlichePruefung !== "undefined") {
			result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : '"' + obj.noteSchriftlichePruefung + '"') + ',';
		}
		if (typeof obj.muendlichePruefung !== "undefined") {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		}
		if (typeof obj.muendlichePruefungFreiwillig !== "undefined") {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		}
		if (typeof obj.noteMuendlichePruefung !== "undefined") {
			result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : '"' + obj.noteMuendlichePruefung + '"') + ',';
		}
		if (typeof obj.abschlussnote !== "undefined") {
			result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : '"' + obj.abschlussnote + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMZP10(obj : unknown) : ENMZP10 {
	return obj as ENMZP10;
}
