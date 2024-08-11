import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungStundenelement extends JavaObject {

	/**
	 * Die Datenbank-ID des Stundenelementes.
	 */
	public id : number = 0;

	/**
	 * Die Anzahl an Stunden (1 = Einzelstunde, 2 = Doppelstunde).
	 */
	public stunden : number = -1;

	/**
	 * Der Typ. (1 = jede Woche, 2 = gedoppelte Einzelstunde)
	 */
	public typ : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungStundenelement';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungStundenelement'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungStundenelement {
		const obj = JSON.parse(json) as Partial<StundenplanblockungStundenelement>;
		const result = new StundenplanblockungStundenelement();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.stunden === undefined)
			throw new Error('invalid json format, missing attribute stunden');
		result.stunden = obj.stunden;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungStundenelement) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"stunden" : ' + obj.stunden.toString() + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungStundenelement>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.stunden !== undefined) {
			result += '"stunden" : ' + obj.stunden.toString() + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungStundenelement(obj : unknown) : StundenplanblockungStundenelement {
	return obj as StundenplanblockungStundenelement;
}
