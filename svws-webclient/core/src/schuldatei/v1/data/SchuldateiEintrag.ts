import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuldateiEintrag extends JavaObject {

	/**
	 * Gibt an, ab wann der Eintrag gültig ist
	 */
	public gueltigab : string | null = null;

	/**
	 * Gibt an, bis wann der Eintrag gültig ist
	 */
	public gueltigbis : string | null = null;

	/**
	 * Das Änderungsdatum der letzten Änderung des Eintrags an
	 */
	public geaendertam : string | null = null;


	/**
	 * Erstellt einen neuen Eintrag der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiEintrag {
		const obj = JSON.parse(json);
		const result = new SchuldateiEintrag();
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiEintrag) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiEintrag>) : string {
		let result = '{';
		if (typeof obj.gueltigab !== "undefined") {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (typeof obj.gueltigbis !== "undefined") {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (typeof obj.geaendertam !== "undefined") {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiEintrag(obj : unknown) : SchuldateiEintrag {
	return obj as SchuldateiEintrag;
}
