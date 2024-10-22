import { Class } from '../../java/lang/Class';
import { CoreTypeData } from '../../asd/data/CoreTypeData';

export class NoteKatalogEintrag extends CoreTypeData {

	/**
	 * Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt
	 */
	public sortierung : number = -1;

	/**
	 * Die Notenpunkte, die dieser Note zugeordnet sind
	 */
	public notenpunkte : number | null = null;

	/**
	 * Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird.
	 */
	public textZeugnis : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.NoteKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.NoteKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<NoteKatalogEintrag>('de.svws_nrw.asd.data.NoteKatalogEintrag');

	public static transpilerFromJSON(json : string): NoteKatalogEintrag {
		const obj = JSON.parse(json) as Partial<NoteKatalogEintrag>;
		const result = new NoteKatalogEintrag();
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
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.notenpunkte = (obj.notenpunkte === undefined) ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		if (obj.textZeugnis === undefined)
			throw new Error('invalid json format, missing attribute textZeugnis');
		result.textZeugnis = obj.textZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : NoteKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		result += '"textZeugnis" : ' + JSON.stringify(obj.textZeugnis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NoteKatalogEintrag>) : string {
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
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.notenpunkte !== undefined) {
			result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		}
		if (obj.textZeugnis !== undefined) {
			result += '"textZeugnis" : ' + JSON.stringify(obj.textZeugnis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_NoteKatalogEintrag(obj : unknown) : NoteKatalogEintrag {
	return obj as NoteKatalogEintrag;
}
