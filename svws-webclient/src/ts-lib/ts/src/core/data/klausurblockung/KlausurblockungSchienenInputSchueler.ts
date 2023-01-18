import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlausurblockungSchienenInputSchueler extends JavaObject {

	/**
	 * Die Datenbank-ID des Schülers. Sie muss positiv sein, sonst wird ein Fehler erzeugt. 
	 */
	public id : number = -1;

	/**
	 * Eine Sammlung der Klausuren von dieses Schülers. 
	 */
	public klausuren : Vector<Number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenInputSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlausurblockungSchienenInputSchueler {
		const obj = JSON.parse(json);
		const result = new KlausurblockungSchienenInputSchueler();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (!!obj.klausuren) {
			for (let elem of obj.klausuren) {
				result.klausuren?.add(Number(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlausurblockungSchienenInputSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		if (!obj.klausuren) {
			result += '"klausuren" : []';
		} else {
			result += '"klausuren" : [ ';
			for (let i : number = 0; i < obj.klausuren.size(); i++) {
				let elem = obj.klausuren.get(i);
				result += elem;
				if (i < obj.klausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlausurblockungSchienenInputSchueler>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.klausuren !== "undefined") {
			if (!obj.klausuren) {
				result += '"klausuren" : []';
			} else {
				result += '"klausuren" : [ ';
				for (let i : number = 0; i < obj.klausuren.size(); i++) {
					let elem = obj.klausuren.get(i);
					result += elem;
					if (i < obj.klausuren.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInputSchueler(obj : unknown) : KlausurblockungSchienenInputSchueler {
	return obj as KlausurblockungSchienenInputSchueler;
}
