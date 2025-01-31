import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { StundenplanblockungStundenelement } from '../../../core/data/stundenplanblockung/StundenplanblockungStundenelement';

export class StundenplanblockungKopplung extends JavaObject {

	/**
	 * Die Datenbank-ID der Kopplung.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Kopplung. Beispielsweise '5RE'.
	 */
	public kuerzel : string = "";

	/**
	 * Alle Stundenelemente, die dieser Kopplung zugeordnet sind.
	 */
	public stundenelemente : List<StundenplanblockungStundenelement> = new ArrayList<StundenplanblockungStundenelement>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungKopplung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungKopplung'].includes(name);
	}

	public static class = new Class<StundenplanblockungKopplung>('de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungKopplung');

	public static transpilerFromJSON(json : string): StundenplanblockungKopplung {
		const obj = JSON.parse(json) as Partial<StundenplanblockungKopplung>;
		const result = new StundenplanblockungKopplung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.stundenelemente !== undefined) {
			for (const elem of obj.stundenelemente) {
				result.stundenelemente.add(StundenplanblockungStundenelement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungKopplung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"stundenelemente" : [ ';
		for (let i = 0; i < obj.stundenelemente.size(); i++) {
			const elem = obj.stundenelemente.get(i);
			result += StundenplanblockungStundenelement.transpilerToJSON(elem);
			if (i < obj.stundenelemente.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungKopplung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.stundenelemente !== undefined) {
			result += '"stundenelemente" : [ ';
			for (let i = 0; i < obj.stundenelemente.size(); i++) {
				const elem = obj.stundenelemente.get(i);
				result += StundenplanblockungStundenelement.transpilerToJSON(elem);
				if (i < obj.stundenelemente.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungKopplung(obj : unknown) : StundenplanblockungKopplung {
	return obj as StundenplanblockungKopplung;
}
