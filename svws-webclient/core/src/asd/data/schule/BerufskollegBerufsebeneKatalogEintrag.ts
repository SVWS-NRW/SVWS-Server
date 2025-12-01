import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class BerufskollegBerufsebeneKatalogEintrag extends CoreTypeData {

	/**
	 * Die Berufsebene.
	 */
	public ebene: number = 0;


	/**
	 * Erstellt einen Berufsebene-Eintrag mit Standardwerten
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.BerufskollegBerufsebeneKatalogEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schule.BerufskollegBerufsebeneKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static readonly class = new Class<BerufskollegBerufsebeneKatalogEintrag>('de.svws_nrw.asd.data.schule.BerufskollegBerufsebeneKatalogEintrag');

	public static transpilerFromJSON(json: string): BerufskollegBerufsebeneKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BerufskollegBerufsebeneKatalogEintrag>;
		const result = new BerufskollegBerufsebeneKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		if (obj.ebene === undefined)
			throw new Error('invalid json format, missing attribute ebene');
		result.ebene = obj.ebene;
		return result;
	}

	public static transpilerToJSON(obj: BerufskollegBerufsebeneKatalogEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"ebene" : ' + obj.ebene.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<BerufskollegBerufsebeneKatalogEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.ebene !== undefined) {
			result += '"ebene" : ' + obj.ebene.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_BerufskollegBerufsebeneKatalogEintrag(obj: unknown): BerufskollegBerufsebeneKatalogEintrag {
	return obj as BerufskollegBerufsebeneKatalogEintrag;
}
