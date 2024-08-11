import { JavaObject } from '../../../java/lang/JavaObject';
import { Sprachbelegung } from '../../../core/data/schueler/Sprachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import { Sprachpruefung } from '../../../core/data/schueler/Sprachpruefung';
import type { List } from '../../../java/util/List';

export class Sprachendaten extends JavaObject {

	/**
	 * Die ID des Schülers, dessen Sprachenfolge in diesem Objekt gespeichert ist.
	 */
	public schuelerID : number = 0;

	/**
	 * Die Liste der Sprachbelegungen.
	 */
	public belegungen : List<Sprachbelegung> = new ArrayList<Sprachbelegung>();

	/**
	 * Die Liste der Sprachpruefungen.
	 */
	public pruefungen : List<Sprachpruefung> = new ArrayList<Sprachpruefung>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.Sprachendaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Sprachendaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachendaten {
		const obj = JSON.parse(json) as Partial<Sprachendaten>;
		const result = new Sprachendaten();
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.belegungen !== undefined) {
			for (const elem of obj.belegungen) {
				result.belegungen.add(Sprachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.pruefungen !== undefined) {
			for (const elem of obj.pruefungen) {
				result.pruefungen.add(Sprachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Sprachendaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"belegungen" : [ ';
		for (let i = 0; i < obj.belegungen.size(); i++) {
			const elem = obj.belegungen.get(i);
			result += Sprachbelegung.transpilerToJSON(elem);
			if (i < obj.belegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pruefungen" : [ ';
		for (let i = 0; i < obj.pruefungen.size(); i++) {
			const elem = obj.pruefungen.get(i);
			result += Sprachpruefung.transpilerToJSON(elem);
			if (i < obj.pruefungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachendaten>) : string {
		let result = '{';
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.belegungen !== undefined) {
			result += '"belegungen" : [ ';
			for (let i = 0; i < obj.belegungen.size(); i++) {
				const elem = obj.belegungen.get(i);
				result += Sprachbelegung.transpilerToJSON(elem);
				if (i < obj.belegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pruefungen !== undefined) {
			result += '"pruefungen" : [ ';
			for (let i = 0; i < obj.pruefungen.size(); i++) {
				const elem = obj.pruefungen.get(i);
				result += Sprachpruefung.transpilerToJSON(elem);
				if (i < obj.pruefungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Sprachendaten(obj : unknown) : Sprachendaten {
	return obj as Sprachendaten;
}
