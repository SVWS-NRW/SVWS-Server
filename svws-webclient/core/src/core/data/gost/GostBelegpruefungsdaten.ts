import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostJahrgangFachkombination } from '../../../core/data/gost/GostJahrgangFachkombination';
import { GostJahrgangsdaten } from '../../../core/data/gost/GostJahrgangsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Abiturdaten } from '../../../core/data/gost/Abiturdaten';

export class GostBelegpruefungsdaten extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird.
	 */
	public abiturdaten : Abiturdaten = new Abiturdaten();

	/**
	 * Informationen zu dem Abiturjahrgang.
	 */
	public gostJahrgang : GostJahrgangsdaten = new GostJahrgangsdaten();

	/**
	 * Die Liste der Fächer der gymnasialen Oberstufe, die für die Belegprüfung genutzt werden sollen
	 */
	public gostFaecher : List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Die Informationen zu den nicht zulässigen und geforderten Fächerkombinationen.
	 */
	public gostFaecherKombinationen : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBelegpruefungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBelegpruefungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungsdaten {
		const obj = JSON.parse(json) as Partial<GostBelegpruefungsdaten>;
		const result = new GostBelegpruefungsdaten();
		if (obj.abiturdaten === undefined)
			throw new Error('invalid json format, missing attribute abiturdaten');
		result.abiturdaten = Abiturdaten.transpilerFromJSON(JSON.stringify(obj.abiturdaten));
		if (obj.gostJahrgang === undefined)
			throw new Error('invalid json format, missing attribute gostJahrgang');
		result.gostJahrgang = GostJahrgangsdaten.transpilerFromJSON(JSON.stringify(obj.gostJahrgang));
		if (obj.gostFaecher !== undefined) {
			for (const elem of obj.gostFaecher) {
				result.gostFaecher.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.gostFaecherKombinationen !== undefined) {
			for (const elem of obj.gostFaecherKombinationen) {
				result.gostFaecherKombinationen.add(GostJahrgangFachkombination.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungsdaten) : string {
		let result = '{';
		result += '"abiturdaten" : ' + Abiturdaten.transpilerToJSON(obj.abiturdaten) + ',';
		result += '"gostJahrgang" : ' + GostJahrgangsdaten.transpilerToJSON(obj.gostJahrgang) + ',';
		result += '"gostFaecher" : [ ';
		for (let i = 0; i < obj.gostFaecher.size(); i++) {
			const elem = obj.gostFaecher.get(i);
			result += GostFach.transpilerToJSON(elem);
			if (i < obj.gostFaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gostFaecherKombinationen" : [ ';
		for (let i = 0; i < obj.gostFaecherKombinationen.size(); i++) {
			const elem = obj.gostFaecherKombinationen.get(i);
			result += GostJahrgangFachkombination.transpilerToJSON(elem);
			if (i < obj.gostFaecherKombinationen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungsdaten>) : string {
		let result = '{';
		if (obj.abiturdaten !== undefined) {
			result += '"abiturdaten" : ' + Abiturdaten.transpilerToJSON(obj.abiturdaten) + ',';
		}
		if (obj.gostJahrgang !== undefined) {
			result += '"gostJahrgang" : ' + GostJahrgangsdaten.transpilerToJSON(obj.gostJahrgang) + ',';
		}
		if (obj.gostFaecher !== undefined) {
			result += '"gostFaecher" : [ ';
			for (let i = 0; i < obj.gostFaecher.size(); i++) {
				const elem = obj.gostFaecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.gostFaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gostFaecherKombinationen !== undefined) {
			result += '"gostFaecherKombinationen" : [ ';
			for (let i = 0; i < obj.gostFaecherKombinationen.size(); i++) {
				const elem = obj.gostFaecherKombinationen.get(i);
				result += GostJahrgangFachkombination.transpilerToJSON(elem);
				if (i < obj.gostFaecherKombinationen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBelegpruefungsdaten(obj : unknown) : GostBelegpruefungsdaten {
	return obj as GostBelegpruefungsdaten;
}
