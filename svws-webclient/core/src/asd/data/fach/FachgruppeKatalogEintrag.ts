import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';
import { RGBFarbe } from '../../../asd/data/RGBFarbe';

export class FachgruppeKatalogEintrag extends CoreTypeData {

	/**
	 * Die Nummer für den Fachbereich, sofern festgelegt, ansonsten null.
	 */
	public nummer : number | null = null;

	/**
	 * Die Farbe, welche der Fachgruppe zugeordnet wurde
	 */
	public farbe : RGBFarbe = new RGBFarbe();

	/**
	 * Die Kürzel der Schulformen, bei welchen die Fachgruppe vorkommt.
	 */
	public schulformen : List<string> = new ArrayList<string>();

	/**
	 * Ein Zahlwert, welche eine Sortier-Reihenfolge der Fachgruppen angibt (aus Schild 2.x).
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht
	 */
	public fuerZeugnis : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag'].includes(name);
	}

	public static class = new Class<FachgruppeKatalogEintrag>('de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag');

	public static transpilerFromJSON(json : string): FachgruppeKatalogEintrag {
		const obj = JSON.parse(json) as Partial<FachgruppeKatalogEintrag>;
		const result = new FachgruppeKatalogEintrag();
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
		result.nummer = (obj.nummer === undefined) ? null : obj.nummer === null ? null : obj.nummer;
		if (obj.farbe === undefined)
			throw new Error('invalid json format, missing attribute farbe');
		result.farbe = RGBFarbe.transpilerFromJSON(JSON.stringify(obj.farbe));
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.fuerZeugnis === undefined)
			throw new Error('invalid json format, missing attribute fuerZeugnis');
		result.fuerZeugnis = obj.fuerZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : FachgruppeKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer.toString()) + ',';
		result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
		result += '"schulformen" : [ ';
		for (let i = 0; i < obj.schulformen.size(); i++) {
			const elem = obj.schulformen.get(i);
			result += '"' + elem + '"';
			if (i < obj.schulformen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sortierung" : ' + obj.sortierung! + ',';
		result += '"fuerZeugnis" : ' + obj.fuerZeugnis.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachgruppeKatalogEintrag>) : string {
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
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer.toString()) + ',';
		}
		if (obj.farbe !== undefined) {
			result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
		}
		if (obj.schulformen !== undefined) {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (obj.fuerZeugnis !== undefined) {
			result += '"fuerZeugnis" : ' + obj.fuerZeugnis.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_fach_FachgruppeKatalogEintrag(obj : unknown) : FachgruppeKatalogEintrag {
	return obj as FachgruppeKatalogEintrag;
}
