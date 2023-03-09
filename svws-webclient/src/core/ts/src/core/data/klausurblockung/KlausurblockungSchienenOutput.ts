import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenOutputKlausur, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutputKlausur } from '../../../core/data/klausurblockung/KlausurblockungSchienenOutputKlausur';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlausurblockungSchienenOutput extends JavaObject {

	/**
	 * Die Datenbank-ID der zugehörigen Klausurblockung, für die dieses Ergebnis gilt. 
	 */
	public datenbankID : number = -1;

	/**
	 * Die Anzahl an Schienen (ergo Klausurtage) für dieses Ergebnis. 
	 */
	public schienenAnzahl : number = -1;

	/**
	 * Alle Klausuren mit ihren Schienen-Zuordnungen 
	 */
	public klausuren : Vector<KlausurblockungSchienenOutputKlausur> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutput'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlausurblockungSchienenOutput {
		const obj = JSON.parse(json);
		const result = new KlausurblockungSchienenOutput();
		if (typeof obj.datenbankID === "undefined")
			 throw new Error('invalid json format, missing attribute datenbankID');
		result.datenbankID = obj.datenbankID;
		if (typeof obj.schienenAnzahl === "undefined")
			 throw new Error('invalid json format, missing attribute schienenAnzahl');
		result.schienenAnzahl = obj.schienenAnzahl;
		if (!!obj.klausuren) {
			for (let elem of obj.klausuren) {
				result.klausuren?.add(KlausurblockungSchienenOutputKlausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlausurblockungSchienenOutput) : string {
		let result = '{';
		result += '"datenbankID" : ' + obj.datenbankID + ',';
		result += '"schienenAnzahl" : ' + obj.schienenAnzahl + ',';
		if (!obj.klausuren) {
			result += '"klausuren" : []';
		} else {
			result += '"klausuren" : [ ';
			for (let i : number = 0; i < obj.klausuren.size(); i++) {
				let elem = obj.klausuren.get(i);
				result += KlausurblockungSchienenOutputKlausur.transpilerToJSON(elem);
				if (i < obj.klausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlausurblockungSchienenOutput>) : string {
		let result = '{';
		if (typeof obj.datenbankID !== "undefined") {
			result += '"datenbankID" : ' + obj.datenbankID + ',';
		}
		if (typeof obj.schienenAnzahl !== "undefined") {
			result += '"schienenAnzahl" : ' + obj.schienenAnzahl + ',';
		}
		if (typeof obj.klausuren !== "undefined") {
			if (!obj.klausuren) {
				result += '"klausuren" : []';
			} else {
				result += '"klausuren" : [ ';
				for (let i : number = 0; i < obj.klausuren.size(); i++) {
					let elem = obj.klausuren.get(i);
					result += KlausurblockungSchienenOutputKlausur.transpilerToJSON(elem);
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

export function cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutput(obj : unknown) : KlausurblockungSchienenOutput {
	return obj as KlausurblockungSchienenOutput;
}
