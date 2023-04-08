import { JavaObject } from '../../../java/lang/JavaObject';
import { Sprachbelegung } from '../../../core/data/schueler/Sprachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import { Sprachpruefung } from '../../../core/data/schueler/Sprachpruefung';

export class Sprachendaten extends JavaObject {

	/**
	 * Die ID des Schülers, dessen Sprachenfolge in diesem Objekt gespeichert ist.
	 */
	public schuelerID : number = 0;

	/**
	 * Die Liste der Sprachbelegungen.
	 */
	public belegungen : ArrayList<Sprachbelegung> = new ArrayList();

	/**
	 * Die Liste der Sprachpruefungen.
	 */
	public pruefungen : ArrayList<Sprachpruefung> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Sprachendaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachendaten {
		const obj = JSON.parse(json);
		const result = new Sprachendaten();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if ((obj.belegungen !== undefined) && (obj.belegungen !== null)) {
			for (const elem of obj.belegungen) {
				result.belegungen?.add(Sprachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.pruefungen !== undefined) && (obj.pruefungen !== null)) {
			for (const elem of obj.pruefungen) {
				result.pruefungen?.add(Sprachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Sprachendaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		if (!obj.belegungen) {
			result += '"belegungen" : []';
		} else {
			result += '"belegungen" : [ ';
			for (let i = 0; i < obj.belegungen.size(); i++) {
				const elem = obj.belegungen.get(i);
				result += Sprachbelegung.transpilerToJSON(elem);
				if (i < obj.belegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pruefungen) {
			result += '"pruefungen" : []';
		} else {
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

	public static transpilerToJSONPatch(obj : Partial<Sprachendaten>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.belegungen !== "undefined") {
			if (!obj.belegungen) {
				result += '"belegungen" : []';
			} else {
				result += '"belegungen" : [ ';
				for (let i = 0; i < obj.belegungen.size(); i++) {
					const elem = obj.belegungen.get(i);
					result += Sprachbelegung.transpilerToJSON(elem);
					if (i < obj.belegungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pruefungen !== "undefined") {
			if (!obj.pruefungen) {
				result += '"pruefungen" : []';
			} else {
				result += '"pruefungen" : [ ';
				for (let i = 0; i < obj.pruefungen.size(); i++) {
					const elem = obj.pruefungen.get(i);
					result += Sprachpruefung.transpilerToJSON(elem);
					if (i < obj.pruefungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Sprachendaten(obj : unknown) : Sprachendaten {
	return obj as Sprachendaten;
}
