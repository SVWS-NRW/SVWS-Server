import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { SchulformGliederungJahrgaenge } from '../../../core/data/schule/SchulformGliederungJahrgaenge';

export class AbgangsartKatalogDaten extends JavaObject {

	/**
	 * Die ID des Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Beschreibung der Abgangsart.
	 */
	public beschreibung : string = "";

	/**
	 * Die Kombinationen von Schulformen, -gliederungen und Jahrgängen, bei der die Abgangsart zulässig ist.
	 */
	public zulaessig : List<SchulformGliederungJahrgaenge> = new ArrayList<SchulformGliederungJahrgaenge>();

	/**
	 * Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AbgangsartKatalogDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.AbgangsartKatalogDaten'].includes(name);
	}

	public static class = new Class<AbgangsartKatalogDaten>('de.svws_nrw.core.data.schule.AbgangsartKatalogDaten');

	public static transpilerFromJSON(json : string): AbgangsartKatalogDaten {
		const obj = JSON.parse(json) as Partial<AbgangsartKatalogDaten>;
		const result = new AbgangsartKatalogDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformGliederungJahrgaenge.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalogDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformGliederungJahrgaenge.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalogDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.zulaessig !== undefined) {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformGliederungJahrgaenge.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
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

export function cast_de_svws_nrw_core_data_schule_AbgangsartKatalogDaten(obj : unknown) : AbgangsartKatalogDaten {
	return obj as AbgangsartKatalogDaten;
}
