import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class AnrechnungsantragBKAZVOKatalogEintrag extends CoreTypeData {

	/**
	 * Eine zusätzliche längere Beschreibung der Einschulungsart.
	 */
	public beschreibung: string = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.AnrechnungsantragBKAZVOKatalogEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.schueler.AnrechnungsantragBKAZVOKatalogEintrag'].includes(name);
	}

	public static readonly class = new Class<AnrechnungsantragBKAZVOKatalogEintrag>('de.svws_nrw.asd.data.schueler.AnrechnungsantragBKAZVOKatalogEintrag');

	public static transpilerFromJSON(json: string): AnrechnungsantragBKAZVOKatalogEintrag {
		const obj = JSON.parse(json) as Partial<AnrechnungsantragBKAZVOKatalogEintrag>;
		const result = new AnrechnungsantragBKAZVOKatalogEintrag();
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
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj: AnrechnungsantragBKAZVOKatalogEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"auslaufdauer" : ' + obj.auslaufdauer.toString() + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AnrechnungsantragBKAZVOKatalogEintrag>): string {
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
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_AnrechnungsantragBKAZVOKatalogEintrag(obj: unknown): AnrechnungsantragBKAZVOKatalogEintrag {
	return obj as AnrechnungsantragBKAZVOKatalogEintrag;
}
