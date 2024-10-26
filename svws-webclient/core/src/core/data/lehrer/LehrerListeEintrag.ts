import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerListeEintrag extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel : string = "";

	/**
	 * Ggf. ein akademischer Grad des Lehrers.
	 */
	public titel : string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname : string = "";

	/**
	 * Der Personaltyp des Lehrerlisten-Eintrags.
	 */
	public personTyp : string = "";

	/**
	 * Die Sortierreihenfolge des Lehrerlisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Gibt an, ob der Eintrag für die Schulstatistik relevant ist oder nicht.
	 */
	public istRelevantFuerStatistik : boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerListeEintrag'].includes(name);
	}

	public static class = new Class<LehrerListeEintrag>('de.svws_nrw.core.data.lehrer.LehrerListeEintrag');

	public static transpilerFromJSON(json : string): LehrerListeEintrag {
		const obj = JSON.parse(json) as Partial<LehrerListeEintrag>;
		const result = new LehrerListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.titel = (obj.titel === undefined) ? null : obj.titel === null ? null : obj.titel;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.personTyp === undefined)
			throw new Error('invalid json format, missing attribute personTyp');
		result.personTyp = obj.personTyp;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.istRelevantFuerStatistik === undefined)
			throw new Error('invalid json format, missing attribute istRelevantFuerStatistik');
		result.istRelevantFuerStatistik = obj.istRelevantFuerStatistik;
		return result;
	}

	public static transpilerToJSON(obj : LehrerListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"personTyp" : ' + JSON.stringify(obj.personTyp) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istRelevantFuerStatistik" : ' + obj.istRelevantFuerStatistik.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.titel !== undefined) {
			result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.personTyp !== undefined) {
			result += '"personTyp" : ' + JSON.stringify(obj.personTyp) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.istRelevantFuerStatistik !== undefined) {
			result += '"istRelevantFuerStatistik" : ' + obj.istRelevantFuerStatistik.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerListeEintrag(obj : unknown) : LehrerListeEintrag {
	return obj as LehrerListeEintrag;
}
