import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragLaender extends JavaObject {

	/**
	 * Bundesländer/Nachbarländer Kurztext
	 */
	public Kurztext : string | null = null;

	/**
	 * Bundesländer/Nachbarländer Langtext
	 */
	public Langtext : string | null = null;

	/**
	 * Bundesländer/Nachbarländer Sortierung
	 */
	public Sortierung : number | null = null;

	/**
	 * Gültig ab Schuljahr
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gültig bis Schuljahr
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragLaender';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragLaender'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragLaender {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragLaender>;
		const result = new Schild3KatalogEintragLaender();
		result.Kurztext = (obj.Kurztext === undefined) ? null : obj.Kurztext === null ? null : obj.Kurztext;
		result.Langtext = (obj.Langtext === undefined) ? null : obj.Langtext === null ? null : obj.Langtext;
		result.Sortierung = (obj.Sortierung === undefined) ? null : obj.Sortierung === null ? null : obj.Sortierung;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragLaender) : string {
		let result = '{';
		result += '"Kurztext" : ' + ((!obj.Kurztext) ? 'null' : JSON.stringify(obj.Kurztext)) + ',';
		result += '"Langtext" : ' + ((!obj.Langtext) ? 'null' : JSON.stringify(obj.Langtext)) + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragLaender>) : string {
		let result = '{';
		if (obj.Kurztext !== undefined) {
			result += '"Kurztext" : ' + ((!obj.Kurztext) ? 'null' : JSON.stringify(obj.Kurztext)) + ',';
		}
		if (obj.Langtext !== undefined) {
			result += '"Langtext" : ' + ((!obj.Langtext) ? 'null' : JSON.stringify(obj.Langtext)) + ',';
		}
		if (obj.Sortierung !== undefined) {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragLaender(obj : unknown) : Schild3KatalogEintragLaender {
	return obj as Schild3KatalogEintragLaender;
}
