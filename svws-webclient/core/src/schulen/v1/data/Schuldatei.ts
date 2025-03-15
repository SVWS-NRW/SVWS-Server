import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiVersion } from '../../../schulen/v1/data/SchuldateiVersion';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Schuldatei extends JavaObject {

	/**
	 * Die Version der Schuldatei.
	 */
	public version : SchuldateiVersion = new SchuldateiVersion();

	/**
	 * Die Organisationseinheit des Eintrags
	 */
	public organisationseinheiten : List<SchuldateiOrganisationseinheit> = new ArrayList<SchuldateiOrganisationseinheit>();


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
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = SchuldateiVersion.transpilerFromJSON(JSON.stringify(obj.version));
		if (obj.organisationseinheiten !== undefined) {
			for (const elem of obj.organisationseinheiten) {
				result.organisationseinheiten.add(SchuldateiOrganisationseinheit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Schuldatei) : string {
		let result = '{';
		result += '"version" : ' + SchuldateiVersion.transpilerToJSON(obj.version) + ',';
		result += '"organisationseinheiten" : [ ';
		for (let i = 0; i < obj.organisationseinheiten.size(); i++) {
			const elem = obj.organisationseinheiten.get(i);
			result += SchuldateiOrganisationseinheit.transpilerToJSON(elem);
			if (i < obj.organisationseinheiten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schuldatei>) : string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + SchuldateiVersion.transpilerToJSON(obj.version) + ',';
		}
		if (obj.organisationseinheiten !== undefined) {
			result += '"organisationseinheiten" : [ ';
			for (let i = 0; i < obj.organisationseinheiten.size(); i++) {
				const elem = obj.organisationseinheiten.get(i);
				result += SchuldateiOrganisationseinheit.transpilerToJSON(elem);
				if (i < obj.organisationseinheiten.size() - 1)
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
