import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMSchuelerAnkreuzkompetenz extends JavaObject {

	/**
	 * Die ID des Eintrages aus der SVWS-DB
	 */
	public id : number = -1;

	/**
	 * Die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht
	 */
	public kompetenzID : number | null = null;

	/**
	 * Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen an, ob diese zugewiesen ist oder nicht (hier mit einer Verschiebung von 1 zum Array-Index).
	 */
	public stufen : Array<boolean> = Array(5).fill(false);

	/**
	 * Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen
	 */
	public tsStufe : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz'].includes(name);
	}

	public static class = new Class<ENMSchuelerAnkreuzkompetenz>('de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz');

	public static transpilerFromJSON(json : string): ENMSchuelerAnkreuzkompetenz {
		const obj = JSON.parse(json) as Partial<ENMSchuelerAnkreuzkompetenz>;
		const result = new ENMSchuelerAnkreuzkompetenz();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kompetenzID = (obj.kompetenzID === undefined) ? null : obj.kompetenzID === null ? null : obj.kompetenzID;
		if (obj.stufen !== undefined) {
			for (let i = 0; i < obj.stufen.length; i++) {
				result.stufen[i] = obj.stufen[i];
			}
		}
		result.tsStufe = (obj.tsStufe === undefined) ? null : obj.tsStufe === null ? null : obj.tsStufe;
		return result;
	}

	public static transpilerToJSON(obj : ENMSchuelerAnkreuzkompetenz) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kompetenzID" : ' + ((obj.kompetenzID === null) ? 'null' : obj.kompetenzID.toString()) + ',';
		result += '"stufen" : [ ';
		for (let i = 0; i < obj.stufen.length; i++) {
			const elem = obj.stufen[i];
			result += JSON.stringify(elem);
			if (i < obj.stufen.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"tsStufe" : ' + ((obj.tsStufe === null) ? 'null' : JSON.stringify(obj.tsStufe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSchuelerAnkreuzkompetenz>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kompetenzID !== undefined) {
			result += '"kompetenzID" : ' + ((obj.kompetenzID === null) ? 'null' : obj.kompetenzID.toString()) + ',';
		}
		if (obj.stufen !== undefined) {
			const a = obj.stufen;
			result += '"stufen" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.tsStufe !== undefined) {
			result += '"tsStufe" : ' + ((obj.tsStufe === null) ? 'null' : JSON.stringify(obj.tsStufe)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMSchuelerAnkreuzkompetenz(obj : unknown) : ENMSchuelerAnkreuzkompetenz {
	return obj as ENMSchuelerAnkreuzkompetenz;
}
