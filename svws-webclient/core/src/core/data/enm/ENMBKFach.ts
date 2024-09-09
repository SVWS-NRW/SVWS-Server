import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMBKFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMBKFach'].includes(name);
	}

	public static class = new Class<ENMBKFach>('de.svws_nrw.core.data.enm.ENMBKFach');

	public static transpilerFromJSON(json : string): ENMBKFach {
		const obj = JSON.parse(json) as Partial<ENMBKFach>;
		const result = new ENMBKFach();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (obj.lehrerID === undefined)
			throw new Error('invalid json format, missing attribute lehrerID');
		result.lehrerID = obj.lehrerID;
		if (obj.istSchriftlich === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		result.vornote = (obj.vornote === undefined) ? null : obj.vornote === null ? null : obj.vornote;
		result.noteSchriftlichePruefung = (obj.noteSchriftlichePruefung === undefined) ? null : obj.noteSchriftlichePruefung === null ? null : obj.noteSchriftlichePruefung;
		if (obj.muendlichePruefung === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (obj.muendlichePruefungFreiwillig === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = (obj.noteMuendlichePruefung === undefined) ? null : obj.noteMuendlichePruefung === null ? null : obj.noteMuendlichePruefung;
		if (obj.istSchriftlichBerufsabschluss === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlichBerufsabschluss');
		result.istSchriftlichBerufsabschluss = obj.istSchriftlichBerufsabschluss;
		result.noteBerufsabschluss = (obj.noteBerufsabschluss === undefined) ? null : obj.noteBerufsabschluss === null ? null : obj.noteBerufsabschluss;
		result.abschlussnote = (obj.abschlussnote === undefined) ? null : obj.abschlussnote === null ? null : obj.abschlussnote;
		return result;
	}

	public static transpilerToJSON(obj : ENMBKFach) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"lehrerID" : ' + obj.lehrerID.toString() + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		result += '"vornote" : ' + ((!obj.vornote) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		result += '"istSchriftlichBerufsabschluss" : ' + obj.istSchriftlichBerufsabschluss.toString() + ',';
		result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		result += '"abschlussnote" : ' + ((!obj.abschlussnote) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMBKFach>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : ' + obj.lehrerID.toString() + ',';
		}
		if (obj.istSchriftlich !== undefined) {
			result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		}
		if (obj.vornote !== undefined) {
			result += '"vornote" : ' + ((!obj.vornote) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		}
		if (obj.noteSchriftlichePruefung !== undefined) {
			result += '"noteSchriftlichePruefung" : ' + ((!obj.noteSchriftlichePruefung) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		}
		if (obj.muendlichePruefung !== undefined) {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		}
		if (obj.muendlichePruefungFreiwillig !== undefined) {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		}
		if (obj.noteMuendlichePruefung !== undefined) {
			result += '"noteMuendlichePruefung" : ' + ((!obj.noteMuendlichePruefung) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		}
		if (obj.istSchriftlichBerufsabschluss !== undefined) {
			result += '"istSchriftlichBerufsabschluss" : ' + obj.istSchriftlichBerufsabschluss.toString() + ',';
		}
		if (obj.noteBerufsabschluss !== undefined) {
			result += '"noteBerufsabschluss" : ' + ((!obj.noteBerufsabschluss) ? 'null' : JSON.stringify(obj.noteBerufsabschluss)) + ',';
		}
		if (obj.abschlussnote !== undefined) {
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
