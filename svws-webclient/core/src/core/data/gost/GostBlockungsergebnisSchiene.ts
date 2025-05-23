import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostBlockungsergebnisSchiene extends JavaObject {

	/**
	 * Die ID der Schiene
	 */
	public id : number = -1;

	/**
	 * Eine Liste der Kurse, welche dieser Schiene zugeordnet sind.
	 */
	public readonly kurse : List<GostBlockungsergebnisKurs> = new ArrayList<GostBlockungsergebnisKurs>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	public hashCode() : number {
		return 31 + (this.id ^ (this.id >>> 32)) as number;
	}

	public equals(obj : unknown | null) : boolean {
		if (this as unknown === obj as unknown)
			return true;
		if (obj === null)
			return false;
		if (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene')))))
			return false;
		const other : GostBlockungsergebnisSchiene = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisSchiene(obj);
		return (this.id === other.id);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene'].includes(name);
	}

	public static class = new Class<GostBlockungsergebnisSchiene>('de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene');

	public static transpilerFromJSON(json : string): GostBlockungsergebnisSchiene {
		const obj = JSON.parse(json) as Partial<GostBlockungsergebnisSchiene>;
		const result = new GostBlockungsergebnisSchiene();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(GostBlockungsergebnisKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisSchiene) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += GostBlockungsergebnisKurs.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisSchiene>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += GostBlockungsergebnisKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisSchiene(obj : unknown) : GostBlockungsergebnisSchiene {
	return obj as GostBlockungsergebnisSchiene;
}
