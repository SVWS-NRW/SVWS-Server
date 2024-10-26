import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMZP10';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMZP10'].includes(name);
	}

	public static class = new Class<ENMZP10>('de.svws_nrw.core.data.enm.ENMZP10');

	public static transpilerFromJSON(json : string): ENMZP10 {
		const obj = JSON.parse(json) as Partial<ENMZP10>;
		const result = new ENMZP10();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.vornote = (obj.vornote === undefined) ? null : obj.vornote === null ? null : obj.vornote;
		result.noteSchriftlichePruefung = (obj.noteSchriftlichePruefung === undefined) ? null : obj.noteSchriftlichePruefung === null ? null : obj.noteSchriftlichePruefung;
		if (obj.muendlichePruefung === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		if (obj.muendlichePruefungFreiwillig === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = (obj.noteMuendlichePruefung === undefined) ? null : obj.noteMuendlichePruefung === null ? null : obj.noteMuendlichePruefung;
		result.abschlussnote = (obj.abschlussnote === undefined) ? null : obj.abschlussnote === null ? null : obj.abschlussnote;
		return result;
	}

	public static transpilerToJSON(obj : ENMZP10) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"vornote" : ' + ((obj.vornote === null) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		result += '"noteSchriftlichePruefung" : ' + ((obj.noteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		result += '"noteMuendlichePruefung" : ' + ((obj.noteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		result += '"abschlussnote" : ' + ((obj.abschlussnote === null) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMZP10>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.vornote !== undefined) {
			result += '"vornote" : ' + ((obj.vornote === null) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		}
		if (obj.noteSchriftlichePruefung !== undefined) {
			result += '"noteSchriftlichePruefung" : ' + ((obj.noteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		}
		if (obj.muendlichePruefung !== undefined) {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		}
		if (obj.muendlichePruefungFreiwillig !== undefined) {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		}
		if (obj.noteMuendlichePruefung !== undefined) {
			result += '"noteMuendlichePruefung" : ' + ((obj.noteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		}
		if (obj.abschlussnote !== undefined) {
			result += '"abschlussnote" : ' + ((obj.abschlussnote === null) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMZP10(obj : unknown) : ENMZP10 {
	return obj as ENMZP10;
}
