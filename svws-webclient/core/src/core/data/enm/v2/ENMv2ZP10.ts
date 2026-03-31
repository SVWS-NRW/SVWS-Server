import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class ENMv2ZP10 extends JavaObject {

	/**
	 * Die ID des ZP10-Eintrags
	 */
	public id: number = 0;

	/**
	 * Die ID des Faches der zentralen Prüfungen
	 */
	public idFach: number = 0;

	/**
	 * Die ID des Fachlehrers
	 */
	public idLehrer: number = 0;

	/**
	 * Das Kürzel der Vornote für dieses Fach
	 */
	public vornote: string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Vornote
	 */
	public tsVornote: string | null = null;

	/**
	 * Das Kürzel der Note, die bei der schriftlichen Prüfung erreicht wurde
	 */
	public noteSchriftlichePruefung: string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Note der schriftlichen Prüfung
	 */
	public tsNoteSchriftlichePruefung: string | null = null;

	/**
	 * Gibt an, ob eine mündliche Prüfung stattfinden muss
	 */
	public muendlichePruefung: boolean = false;

	/**
	 * Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung stattfinden muss
	 */
	public tsMuendlichePruefung: string | null = null;

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung stattfindet
	 */
	public muendlichePruefungFreiwillig: boolean = false;

	/**
	 * Der Zeitstempel der letzten Änderung an der Information, ob eine mündlichen Prüfung freiwillig stattfinden soll
	 */
	public tsMuendlichePruefungFreiwillig: string | null = null;

	/**
	 * Das Kürzel der Note,die bei der mündlichen Prüfung erreicht wurde, sofern eine stattfindet
	 */
	public noteMuendlichePruefung: string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Note der mündlichen Prüfung
	 */
	public tsNoteMuendlichePruefung: string | null = null;

	/**
	 * Das Kürzel der Abschlussnote nach der ZP10-Prüfung
	 */
	public abschlussnote: string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Abschlussnote
	 */
	public tsAbschlussnote: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2ZP10';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2ZP10'].includes(name);
	}

	public static readonly class = new Class<ENMv2ZP10>('de.svws_nrw.core.data.enm.v2.ENMv2ZP10');

	public static transpilerFromJSON(json: string): ENMv2ZP10 {
		const obj = JSON.parse(json) as Partial<ENMv2ZP10>;
		const result = new ENMv2ZP10();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		result.vornote = (obj.vornote === undefined) ? null : obj.vornote === null ? null : obj.vornote;
		result.tsVornote = (obj.tsVornote === undefined) ? null : obj.tsVornote === null ? null : obj.tsVornote;
		result.noteSchriftlichePruefung = (obj.noteSchriftlichePruefung === undefined) ? null : obj.noteSchriftlichePruefung === null ? null : obj.noteSchriftlichePruefung;
		result.tsNoteSchriftlichePruefung = (obj.tsNoteSchriftlichePruefung === undefined) ? null : obj.tsNoteSchriftlichePruefung === null ? null : obj.tsNoteSchriftlichePruefung;
		if (obj.muendlichePruefung === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefung');
		result.muendlichePruefung = obj.muendlichePruefung;
		result.tsMuendlichePruefung = (obj.tsMuendlichePruefung === undefined) ? null : obj.tsMuendlichePruefung === null ? null : obj.tsMuendlichePruefung;
		if (obj.muendlichePruefungFreiwillig === undefined)
			throw new Error('invalid json format, missing attribute muendlichePruefungFreiwillig');
		result.muendlichePruefungFreiwillig = obj.muendlichePruefungFreiwillig;
		result.tsMuendlichePruefungFreiwillig = (obj.tsMuendlichePruefungFreiwillig === undefined) ? null : obj.tsMuendlichePruefungFreiwillig === null ? null : obj.tsMuendlichePruefungFreiwillig;
		result.noteMuendlichePruefung = (obj.noteMuendlichePruefung === undefined) ? null : obj.noteMuendlichePruefung === null ? null : obj.noteMuendlichePruefung;
		result.tsNoteMuendlichePruefung = (obj.tsNoteMuendlichePruefung === undefined) ? null : obj.tsNoteMuendlichePruefung === null ? null : obj.tsNoteMuendlichePruefung;
		result.abschlussnote = (obj.abschlussnote === undefined) ? null : obj.abschlussnote === null ? null : obj.abschlussnote;
		result.tsAbschlussnote = (obj.tsAbschlussnote === undefined) ? null : obj.tsAbschlussnote === null ? null : obj.tsAbschlussnote;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2ZP10): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"vornote" : ' + ((obj.vornote === null) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		result += '"tsVornote" : ' + ((obj.tsVornote === null) ? 'null' : JSON.stringify(obj.tsVornote)) + ',';
		result += '"noteSchriftlichePruefung" : ' + ((obj.noteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		result += '"tsNoteSchriftlichePruefung" : ' + ((obj.tsNoteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.tsNoteSchriftlichePruefung)) + ',';
		result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		result += '"tsMuendlichePruefung" : ' + ((obj.tsMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.tsMuendlichePruefung)) + ',';
		result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		result += '"tsMuendlichePruefungFreiwillig" : ' + ((obj.tsMuendlichePruefungFreiwillig === null) ? 'null' : JSON.stringify(obj.tsMuendlichePruefungFreiwillig)) + ',';
		result += '"noteMuendlichePruefung" : ' + ((obj.noteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		result += '"tsNoteMuendlichePruefung" : ' + ((obj.tsNoteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.tsNoteMuendlichePruefung)) + ',';
		result += '"abschlussnote" : ' + ((obj.abschlussnote === null) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		result += '"tsAbschlussnote" : ' + ((obj.tsAbschlussnote === null) ? 'null' : JSON.stringify(obj.tsAbschlussnote)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2ZP10>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.vornote !== undefined) {
			result += '"vornote" : ' + ((obj.vornote === null) ? 'null' : JSON.stringify(obj.vornote)) + ',';
		}
		if (obj.tsVornote !== undefined) {
			result += '"tsVornote" : ' + ((obj.tsVornote === null) ? 'null' : JSON.stringify(obj.tsVornote)) + ',';
		}
		if (obj.noteSchriftlichePruefung !== undefined) {
			result += '"noteSchriftlichePruefung" : ' + ((obj.noteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.noteSchriftlichePruefung)) + ',';
		}
		if (obj.tsNoteSchriftlichePruefung !== undefined) {
			result += '"tsNoteSchriftlichePruefung" : ' + ((obj.tsNoteSchriftlichePruefung === null) ? 'null' : JSON.stringify(obj.tsNoteSchriftlichePruefung)) + ',';
		}
		if (obj.muendlichePruefung !== undefined) {
			result += '"muendlichePruefung" : ' + obj.muendlichePruefung.toString() + ',';
		}
		if (obj.tsMuendlichePruefung !== undefined) {
			result += '"tsMuendlichePruefung" : ' + ((obj.tsMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.tsMuendlichePruefung)) + ',';
		}
		if (obj.muendlichePruefungFreiwillig !== undefined) {
			result += '"muendlichePruefungFreiwillig" : ' + obj.muendlichePruefungFreiwillig.toString() + ',';
		}
		if (obj.tsMuendlichePruefungFreiwillig !== undefined) {
			result += '"tsMuendlichePruefungFreiwillig" : ' + ((obj.tsMuendlichePruefungFreiwillig === null) ? 'null' : JSON.stringify(obj.tsMuendlichePruefungFreiwillig)) + ',';
		}
		if (obj.noteMuendlichePruefung !== undefined) {
			result += '"noteMuendlichePruefung" : ' + ((obj.noteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.noteMuendlichePruefung)) + ',';
		}
		if (obj.tsNoteMuendlichePruefung !== undefined) {
			result += '"tsNoteMuendlichePruefung" : ' + ((obj.tsNoteMuendlichePruefung === null) ? 'null' : JSON.stringify(obj.tsNoteMuendlichePruefung)) + ',';
		}
		if (obj.abschlussnote !== undefined) {
			result += '"abschlussnote" : ' + ((obj.abschlussnote === null) ? 'null' : JSON.stringify(obj.abschlussnote)) + ',';
		}
		if (obj.tsAbschlussnote !== undefined) {
			result += '"tsAbschlussnote" : ' + ((obj.tsAbschlussnote === null) ? 'null' : JSON.stringify(obj.tsAbschlussnote)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2ZP10(obj: unknown): ENMv2ZP10 {
	return obj as ENMv2ZP10;
}
