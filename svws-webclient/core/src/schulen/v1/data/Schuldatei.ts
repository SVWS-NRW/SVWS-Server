import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Schuldatei extends JavaObject {

	/**
	 * Die Organisationseinheit des Eintrags
	 */
	public organisationseinheit : List<SchuldateiOrganisationseinheit> = new ArrayList<SchuldateiOrganisationseinheit>();


	/**
	 * Erstellt eine Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.Schuldatei';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.Schuldatei'].includes(name);
	}

	public static class = new Class<Schuldatei>('de.svws_nrw.schulen.v1.data.Schuldatei');

	public static transpilerFromJSON(json : string): Schuldatei {
		const obj = JSON.parse(json) as Partial<Schuldatei>;
		const result = new Schuldatei();
		if (obj.organisationseinheit !== undefined) {
			for (const elem of obj.organisationseinheit) {
				result.organisationseinheit.add(SchuldateiOrganisationseinheit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Schuldatei) : string {
		let result = '{';
		result += '"organisationseinheit" : [ ';
		for (let i = 0; i < obj.organisationseinheit.size(); i++) {
			const elem = obj.organisationseinheit.get(i);
			result += SchuldateiOrganisationseinheit.transpilerToJSON(elem);
			if (i < obj.organisationseinheit.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schuldatei>) : string {
		let result = '{';
		if (obj.organisationseinheit !== undefined) {
			result += '"organisationseinheit" : [ ';
			for (let i = 0; i < obj.organisationseinheit.size(); i++) {
				const elem = obj.organisationseinheit.get(i);
				result += SchuldateiOrganisationseinheit.transpilerToJSON(elem);
				if (i < obj.organisationseinheit.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_Schuldatei(obj : unknown) : Schuldatei {
	return obj as Schuldatei;
}
