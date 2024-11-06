import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostBlockungsergebnisKursSchuelerZuordnung extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = -1;

	/**
	 * Die ID des Schülers
	 */
	public idSchueler : number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	public hashCode() : number {
		let hashCode : number = 1;
		hashCode = (31 * hashCode) + (this.idKurs ^ (this.idKurs >>> 32)) as number;
		hashCode = (31 * hashCode) + (this.idSchueler ^ (this.idSchueler >>> 32)) as number;
		return hashCode;
	}

	public equals(obj : unknown | null) : boolean {
		if (this as unknown === obj as unknown)
			return true;
		if (obj === null)
			return false;
		if (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung')))))
			return false;
		const other : GostBlockungsergebnisKursSchuelerZuordnung = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchuelerZuordnung(obj);
		return (this.idKurs === other.idKurs) && (this.idSchueler === other.idSchueler);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung'].includes(name);
	}

	public static class = new Class<GostBlockungsergebnisKursSchuelerZuordnung>('de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung');

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKursSchuelerZuordnung {
		const obj = JSON.parse(json) as Partial<GostBlockungsergebnisKursSchuelerZuordnung>;
		const result = new GostBlockungsergebnisKursSchuelerZuordnung();
		if (obj.idKurs === undefined)
			throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKursSchuelerZuordnung) : string {
		let result = '{';
		result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKursSchuelerZuordnung>) : string {
		let result = '{';
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchuelerZuordnung(obj : unknown) : GostBlockungsergebnisKursSchuelerZuordnung {
	return obj as GostBlockungsergebnisKursSchuelerZuordnung;
}
