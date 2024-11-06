import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostBlockungsergebnisKursSchienenZuordnung extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = -1;

	/**
	 * Die ID der Schiene
	 */
	public idSchiene : number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	public hashCode() : number {
		let hashCode : number = 1;
		hashCode = (31 * hashCode) + (this.idKurs ^ (this.idKurs >>> 32)) as number;
		hashCode = (31 * hashCode) + (this.idSchiene ^ (this.idSchiene >>> 32)) as number;
		return hashCode;
	}

	public equals(obj : unknown | null) : boolean {
		if (this as unknown === obj as unknown)
			return true;
		if (obj === null)
			return false;
		if (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung')))))
			return false;
		const other : GostBlockungsergebnisKursSchienenZuordnung = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchienenZuordnung(obj);
		return (this.idKurs === other.idKurs) && (this.idSchiene === other.idSchiene);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung'].includes(name);
	}

	public static class = new Class<GostBlockungsergebnisKursSchienenZuordnung>('de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung');

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKursSchienenZuordnung {
		const obj = JSON.parse(json) as Partial<GostBlockungsergebnisKursSchienenZuordnung>;
		const result = new GostBlockungsergebnisKursSchienenZuordnung();
		if (obj.idKurs === undefined)
			throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (obj.idSchiene === undefined)
			throw new Error('invalid json format, missing attribute idSchiene');
		result.idSchiene = obj.idSchiene;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKursSchienenZuordnung) : string {
		let result = '{';
		result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		result += '"idSchiene" : ' + obj.idSchiene.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKursSchienenZuordnung>) : string {
		let result = '{';
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		}
		if (obj.idSchiene !== undefined) {
			result += '"idSchiene" : ' + obj.idSchiene.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchienenZuordnung(obj : unknown) : GostBlockungsergebnisKursSchienenZuordnung {
	return obj as GostBlockungsergebnisKursSchienenZuordnung;
}
