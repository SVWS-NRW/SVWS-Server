import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanPausenaufsichtBereich } from '../../../core/data/stundenplan/StundenplanPausenaufsichtBereich';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class StundenplanPausenaufsicht extends JavaObject {

	/**
	 * Die ID der Pausenaufsicht
	 */
	public id : number = -1;

	/**
	 * Die ID der {@link StundenplanPausenzeit}.
	 */
	public idPausenzeit : number = -1;

	/**
	 * Die ID des {@link StundenplanLehrer} der Aufsicht führt.
	 */
	public idLehrer : number = -1;

	/**
	 * Die Zuordnung der Aufsichtsbereiche ({@link StundenplanAufsichtsbereich}) zu den Pausenaufsichten ({@link StundenplanPausenaufsicht}) und dem Wochentyp.
	 */
	public bereiche : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht'].includes(name);
	}

	public static class = new Class<StundenplanPausenaufsicht>('de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht');

	public static transpilerFromJSON(json : string): StundenplanPausenaufsicht {
		const obj = JSON.parse(json) as Partial<StundenplanPausenaufsicht>;
		const result = new StundenplanPausenaufsicht();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPausenzeit === undefined)
			throw new Error('invalid json format, missing attribute idPausenzeit');
		result.idPausenzeit = obj.idPausenzeit;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.bereiche !== undefined) {
			for (const elem of obj.bereiche) {
				result.bereiche.add(StundenplanPausenaufsichtBereich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenaufsicht) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPausenzeit" : ' + obj.idPausenzeit.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"bereiche" : [ ';
		for (let i = 0; i < obj.bereiche.size(); i++) {
			const elem = obj.bereiche.get(i);
			result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
			if (i < obj.bereiche.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenaufsicht>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPausenzeit !== undefined) {
			result += '"idPausenzeit" : ' + obj.idPausenzeit.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.bereiche !== undefined) {
			result += '"bereiche" : [ ';
			for (let i = 0; i < obj.bereiche.size(); i++) {
				const elem = obj.bereiche.get(i);
				result += StundenplanPausenaufsichtBereich.transpilerToJSON(elem);
				if (i < obj.bereiche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenaufsicht(obj : unknown) : StundenplanPausenaufsicht {
	return obj as StundenplanPausenaufsicht;
}
