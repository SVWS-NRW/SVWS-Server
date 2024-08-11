import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerstatusKatalogEintrag extends JavaObject {

	/**
	 * Die numerische ID des Schüler-Status.
	 */
	public StatusNr : number | null = null;

	/**
	 * Klartext des Schülerstatus
	 */
	public Bezeichnung : string | null = null;

	/**
	 * Sortierung des Schülerstatus
	 */
	public Sortierung : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchuelerstatusKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchuelerstatusKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerstatusKatalogEintrag {
		const obj = JSON.parse(json) as Partial<SchuelerstatusKatalogEintrag>;
		const result = new SchuelerstatusKatalogEintrag();
		result.StatusNr = (obj.StatusNr === undefined) ? null : obj.StatusNr === null ? null : obj.StatusNr;
		result.Bezeichnung = (obj.Bezeichnung === undefined) ? null : obj.Bezeichnung === null ? null : obj.Bezeichnung;
		result.Sortierung = (obj.Sortierung === undefined) ? null : obj.Sortierung === null ? null : obj.Sortierung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerstatusKatalogEintrag) : string {
		let result = '{';
		result += '"StatusNr" : ' + ((!obj.StatusNr) ? 'null' : obj.StatusNr.toString()) + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : JSON.stringify(obj.Bezeichnung)) + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerstatusKatalogEintrag>) : string {
		let result = '{';
		if (obj.StatusNr !== undefined) {
			result += '"StatusNr" : ' + ((!obj.StatusNr) ? 'null' : obj.StatusNr.toString()) + ',';
		}
		if (obj.Bezeichnung !== undefined) {
			result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : JSON.stringify(obj.Bezeichnung)) + ',';
		}
		if (obj.Sortierung !== undefined) {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchuelerstatusKatalogEintrag(obj : unknown) : SchuelerstatusKatalogEintrag {
	return obj as SchuelerstatusKatalogEintrag;
}
