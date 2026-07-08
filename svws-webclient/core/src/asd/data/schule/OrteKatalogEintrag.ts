import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class OrteKatalogEintrag extends CoreTypeData {

	/**
	 * Die Postleitzahl des Ortes.
	 */
	public plz: string | null = null;

	/**
	 * Der Amtliche Gemeindeschlüssel (AGS).
	 */
	public ags: string | null = null;

	/**
	 * Der Ortsname.
	 */
	public ort: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.OrteKatalogEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schule.OrteKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static readonly class = new Class<OrteKatalogEintrag>('de.svws_nrw.asd.data.schule.OrteKatalogEintrag');

	public static transpilerFromJSON(json: string): OrteKatalogEintrag {
		const obj = JSON.parse(json) as Partial<OrteKatalogEintrag>;
		const result = new OrteKatalogEintrag();
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
		if (obj.auslaufdauer === undefined)
			throw new Error('invalid json format, missing attribute auslaufdauer');
		result.auslaufdauer = obj.auslaufdauer;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ags = (obj.ags === undefined) ? null : obj.ags === null ? null : obj.ags;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		return result;
	}

	public static transpilerToJSON(obj: OrteKatalogEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"auslaufdauer" : ' + obj.auslaufdauer.toString() + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ags" : ' + ((obj.ags === null) ? 'null' : JSON.stringify(obj.ags)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<OrteKatalogEintrag>): string {
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
		if (obj.auslaufdauer !== undefined) {
			result += '"auslaufdauer" : ' + obj.auslaufdauer.toString() + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ags !== undefined) {
			result += '"ags" : ' + ((obj.ags === null) ? 'null' : JSON.stringify(obj.ags)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_OrteKatalogEintrag(obj: unknown): OrteKatalogEintrag {
	return obj as OrteKatalogEintrag;
}
