import { JavaObject } from '../../../../java/lang/JavaObject';
import { UvBlockungRegelTyp } from '../../../../core/data/uv/regel/UvBlockungRegelTyp';
import { UvBlockungRegelPrioritaet } from '../../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UvBlockungRegel extends JavaObject {

	/**
	 * Die ID der Regel.
	 */
	public id: number = -1;

	/**
	 * Der Type der Regel - siehe {@link UvBlockungRegelTyp}.
	 */
	public typ: number = UvBlockungRegelTyp.UNDEFINIERT.nr;

	/**
	 * Die Priorität der - siehe {@link UvBlockungRegelPrioritaet}.
	 */
	public prioritaet: number = UvBlockungRegelPrioritaet.MITTEL.nr;

	/**
	 * Eine Liste der Regel-Parameter.
	 */
	public parameter: List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.regel.UvBlockungRegel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.regel.UvBlockungRegel'].includes(name);
	}

	public static readonly class = new Class<UvBlockungRegel>('de.svws_nrw.core.data.uv.regel.UvBlockungRegel');

	public static transpilerFromJSON(json: string): UvBlockungRegel {
		const obj = JSON.parse(json) as Partial<UvBlockungRegel>;
		const result = new UvBlockungRegel();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.prioritaet === undefined)
			throw new Error('invalid json format, missing attribute prioritaet');
		result.prioritaet = obj.prioritaet;
		if (obj.parameter !== undefined) {
			for (const elem of obj.parameter) {
				result.parameter.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvBlockungRegel): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"prioritaet" : ' + obj.prioritaet.toString() + ',';
		result += '"parameter" : [ ';
		for (let i = 0; i < obj.parameter.size(); i++) {
			const elem = obj.parameter.get(i);
			result += elem.toString();
			if (i < obj.parameter.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvBlockungRegel>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		if (obj.prioritaet !== undefined) {
			result += '"prioritaet" : ' + obj.prioritaet.toString() + ',';
		}
		if (obj.parameter !== undefined) {
			result += '"parameter" : [ ';
			for (let i = 0; i < obj.parameter.size(); i++) {
				const elem = obj.parameter.get(i);
				result += elem.toString();
				if (i < obj.parameter.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_regel_UvBlockungRegel(obj: unknown): UvBlockungRegel {
	return obj as UvBlockungRegel;
}
