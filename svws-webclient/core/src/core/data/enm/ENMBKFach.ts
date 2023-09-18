import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMBKFach extends JavaObject {

	/**
	 * Die ID des Faches der zentralen Prüfungen.
	 */
	public fachID : number = 0;

	/**
	 * Die ID des Lehrers, der das Prüfungsfach unterrichtet.
	 */
	public lehrerID : number = 0;

	/**
	 * Gibt an, ob das Fach schriftlich ist.
	 */
	public istSchriftlich : boolean = false;

	/**
	 * Das Notenkürzel der Vornote für dieses Fach.
	 */
	public vornote : string | null = null;

	/**
	 * Das Notenkürzel der Note, die bei der schriftlichen Prüfung erreicht wurde.
	 */
	public noteSchriftlichePruefung : string | null = null;

	/**
	 * Gibt an, ob eine mündliche Prüfung stattfinden muss.
	 */
	public muendlichePruefung : boolean = false;

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung stattfindet.
	 */
	public muendlichePruefungFreiwillig : boolean = false;

	/**
	 * Das Notenkürzel der Note, die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet.
	 */
	public noteMuendlichePruefung : string | null = null;

	/**
	 * Gibt an, ob das Fach in Bezug auf den Berufsabschluss schriftlich ist.
	 */
	public istSchriftlichBerufsabschluss : boolean = false;

	/**
	 * Die Note in Bezug auf den Berufsabschluss.
	 */
	public noteBerufsabschluss : string | null = null;

	/**
	 * Das Notenkürzel der Abschlussnote nach der Prüfung.
	 */
	public abschlussnote : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMBKFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMBKFach {
		const obj = JSON.parse(json);
		const result = new ENMBKFach();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (typeof obj.lehrerID === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerID');
		result.lehrerID = obj.lehrerID;
		if (typeof obj.istSchriftlich === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		result.vornote = typeof obj.vornote === "undefined" ? null : obj.vornote === null ? null : obj.vornote;
		result.noteSchriftlichePruefung = typeof obj.noteSchriftlichePruefung === "undefined" ? null : obj.noteSchriftlichePruefung === null ? null : obj.noteSchriftlichePruefung;
		if (typeof obj.muendlichePruefung === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (typeof obj.muendlichePruefungFreiwillig === "undefined")
			 throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = typeof obj.noteMuendlichePruefung === "undefined" ? null : obj.noteMuendlichePruefung === null ? null : obj.noteMuendlichePruefung;
		if (typeof obj.istSchriftlichBerufsabschluss === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlichBerufsabschluss');
		result.istSchriftlichBerufsabschluss = obj.istSchriftlichBerufsabschluss;
		result.noteBerufsabschluss = typeof obj.noteBerufsabschluss === "undefined" ? null : obj.noteBerufsabschluss === null ? null : obj.noteBerufsabschluss;
		result.abschlussnote = typeof obj.abschlussnote === "undefined" ? null : obj.abschlussnote === null ? null : obj.abschlussnote;
		return result;
	}

	public static transpilerToJSON(obj : ENMBKFach) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"lehrerID" : ' + obj.lehrerID + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		result += '"vornote" : ' + ((!obj.vornote) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		result += '"istSchriftlichBerufsabschluss" : ' + obj.istSchriftlichBerufsabschluss + ',';
		result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMBKFach>) : string {
		let result = '{';
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.lehrerID !== "undefined") {
			result += '"lehrerID" : ' + obj.lehrerID + ',';
		}
		if (typeof obj.istSchriftlich !== "undefined") {
			result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		}
		if (typeof obj.vornote !== "undefined") {
			result += '"vornote" : ' + ((!obj.vornote) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		}
		if (typeof obj.noteSchriftlichePruefung !== "undefined") {
			result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		}
		if (typeof obj.muendlichePruefung !== "undefined") {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung + ',';
		}
		if (typeof obj.muendlichePruefungFreiwillig !== "undefined") {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig + ',';
		}
		if (typeof obj.noteMuendlichePruefung !== "undefined") {
			result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		}
		if (typeof obj.istSchriftlichBerufsabschluss !== "undefined") {
			result += '"istSchriftlichBerufsabschluss" : ' + obj.istSchriftlichBerufsabschluss + ',';
		}
		if (typeof obj.noteBerufsabschluss !== "undefined") {
			result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		}
		if (typeof obj.abschlussnote !== "undefined") {
			result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMBKFach(obj : unknown) : ENMBKFach {
	return obj as ENMBKFach;
}
