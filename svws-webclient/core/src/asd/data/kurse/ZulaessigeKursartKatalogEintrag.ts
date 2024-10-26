import { SchulformSchulgliederung } from '../../../asd/data/schule/SchulformSchulgliederung';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class ZulaessigeKursartKatalogEintrag extends CoreTypeData {

	/**
	 * Die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 */
	public nummer : string = "";

	/**
	 * Ergänzende Bemerkungen zu der Kursart
	 */
	public bemerkungen : string | null = null;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist
	 */
	public kuerzelAllg : string | null = null;

	/**
	 * Die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist
	 */
	public bezeichnungAllg : string | null = null;

	/**
	 * Gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist
	 */
	public erlaubtGOSt : boolean = false;

	/**
	 * Die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist.
	 */
	public zulaessig : List<SchulformSchulgliederung> = new ArrayList<SchulformSchulgliederung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag'].includes(name);
	}

	public static class = new Class<ZulaessigeKursartKatalogEintrag>('de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag');

	public static transpilerFromJSON(json : string): ZulaessigeKursartKatalogEintrag {
		const obj = JSON.parse(json) as Partial<ZulaessigeKursartKatalogEintrag>;
		const result = new ZulaessigeKursartKatalogEintrag();
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
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		result.bemerkungen = (obj.bemerkungen === undefined) ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		result.kuerzelAllg = (obj.kuerzelAllg === undefined) ? null : obj.kuerzelAllg === null ? null : obj.kuerzelAllg;
		result.bezeichnungAllg = (obj.bezeichnungAllg === undefined) ? null : obj.bezeichnungAllg === null ? null : obj.bezeichnungAllg;
		if (obj.erlaubtGOSt === undefined)
			throw new Error('invalid json format, missing attribute erlaubtGOSt');
		result.erlaubtGOSt = obj.erlaubtGOSt;
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ZulaessigeKursartKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"nummer" : ' + JSON.stringify(obj.nummer) + ',';
		result += '"bemerkungen" : ' + ((obj.bemerkungen === null) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		result += '"kuerzelAllg" : ' + ((obj.kuerzelAllg === null) ? 'null' : JSON.stringify(obj.kuerzelAllg)) + ',';
		result += '"bezeichnungAllg" : ' + ((obj.bezeichnungAllg === null) ? 'null' : JSON.stringify(obj.bezeichnungAllg)) + ',';
		result += '"erlaubtGOSt" : ' + obj.erlaubtGOSt.toString() + ',';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformSchulgliederung.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ZulaessigeKursartKatalogEintrag>) : string {
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
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + JSON.stringify(obj.nummer) + ',';
		}
		if (obj.bemerkungen !== undefined) {
			result += '"bemerkungen" : ' + ((obj.bemerkungen === null) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		}
		if (obj.kuerzelAllg !== undefined) {
			result += '"kuerzelAllg" : ' + ((obj.kuerzelAllg === null) ? 'null' : JSON.stringify(obj.kuerzelAllg)) + ',';
		}
		if (obj.bezeichnungAllg !== undefined) {
			result += '"bezeichnungAllg" : ' + ((obj.bezeichnungAllg === null) ? 'null' : JSON.stringify(obj.bezeichnungAllg)) + ',';
		}
		if (obj.erlaubtGOSt !== undefined) {
			result += '"erlaubtGOSt" : ' + obj.erlaubtGOSt.toString() + ',';
		}
		if (obj.zulaessig !== undefined) {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kurse_ZulaessigeKursartKatalogEintrag(obj : unknown) : ZulaessigeKursartKatalogEintrag {
	return obj as ZulaessigeKursartKatalogEintrag;
}
