import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanPausenaufsichtBereich extends JavaObject {

	/**
	 * Die ID der Zuordnung
	 */
	public id : number = -1;

	/**
	 * Die ID der {@link StundenplanPausenaufsicht}.
	 */
	public idPausenaufsicht : number = -1;

	/**
	 * Die ID des {@link StundenplanAufsichtsbereich}.
	 */
	public idAufsichtsbereich : number = -1;

	/**
	 * Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0
	 */
	public wochentyp : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereich'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanPausenaufsichtBereich {
		const obj = JSON.parse(json) as Partial<StundenplanPausenaufsichtBereich>;
		const result = new StundenplanPausenaufsichtBereich();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPausenaufsicht === undefined)
			throw new Error('invalid json format, missing attribute idPausenaufsicht');
		result.idPausenaufsicht = obj.idPausenaufsicht;
		if (obj.idAufsichtsbereich === undefined)
			throw new Error('invalid json format, missing attribute idAufsichtsbereich');
		result.idAufsichtsbereich = obj.idAufsichtsbereich;
		if (obj.wochentyp === undefined)
			throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenaufsichtBereich) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPausenaufsicht" : ' + obj.idPausenaufsicht.toString() + ',';
		result += '"idAufsichtsbereich" : ' + obj.idAufsichtsbereich.toString() + ',';
		result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenaufsichtBereich>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPausenaufsicht !== undefined) {
			result += '"idPausenaufsicht" : ' + obj.idPausenaufsicht.toString() + ',';
		}
		if (obj.idAufsichtsbereich !== undefined) {
			result += '"idAufsichtsbereich" : ' + obj.idAufsichtsbereich.toString() + ',';
		}
		if (obj.wochentyp !== undefined) {
			result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenaufsichtBereich(obj : unknown) : StundenplanPausenaufsichtBereich {
	return obj as StundenplanPausenaufsichtBereich;
}
