import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanPausenaufsichtBereich, cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenaufsichtBereich } from '../../../core/data/stundenplan/StundenplanPausenaufsichtBereich';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class StundenplanPausenaufsichtBereichUpdate extends JavaObject {

	/**
	 * Die zu entfernenden Zuordnungen
	 */
	public listEntfernen : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();

	/**
	 * Die hinzuzufügenden Zuordnungen
	 */
	public listHinzuzufuegen : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereichUpdate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereichUpdate'].includes(name);
	}

	public static class = new Class<StundenplanPausenaufsichtBereichUpdate>('de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereichUpdate');

	public static transpilerFromJSON(json : string): StundenplanPausenaufsichtBereichUpdate {
		const obj = JSON.parse(json) as Partial<StundenplanPausenaufsichtBereichUpdate>;
		const result = new StundenplanPausenaufsichtBereichUpdate();
		if (obj.listEntfernen !== undefined) {
			for (const elem of obj.listEntfernen) {
				result.listEntfernen.add(StundenplanPausenaufsichtBereich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.listHinzuzufuegen !== undefined) {
			for (const elem of obj.listHinzuzufuegen) {
				result.listHinzuzufuegen.add(StundenplanPausenaufsichtBereich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenaufsichtBereichUpdate) : string {
		let result = '{';
		result += '"listEntfernen" : [ ';
		for (let i = 0; i < obj.listEntfernen.size(); i++) {
			const elem = obj.listEntfernen.get(i);
			result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
			if (i < obj.listEntfernen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"listHinzuzufuegen" : [ ';
		for (let i = 0; i < obj.listHinzuzufuegen.size(); i++) {
			const elem = obj.listHinzuzufuegen.get(i);
			result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
			if (i < obj.listHinzuzufuegen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenaufsichtBereichUpdate>) : string {
		let result = '{';
		if (obj.listEntfernen !== undefined) {
			result += '"listEntfernen" : [ ';
			for (let i = 0; i < obj.listEntfernen.size(); i++) {
				const elem = obj.listEntfernen.get(i);
				result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
				if (i < obj.listEntfernen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.listHinzuzufuegen !== undefined) {
			result += '"listHinzuzufuegen" : [ ';
			for (let i = 0; i < obj.listHinzuzufuegen.size(); i++) {
				const elem = obj.listHinzuzufuegen.get(i);
				result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
				if (i < obj.listHinzuzufuegen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenaufsichtBereichUpdate(obj : unknown) : StundenplanPausenaufsichtBereichUpdate {
	return obj as StundenplanPausenaufsichtBereichUpdate;
}
