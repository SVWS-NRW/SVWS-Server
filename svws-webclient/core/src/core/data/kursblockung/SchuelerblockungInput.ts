import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerblockungInputKurs } from '../../../core/data/kursblockung/SchuelerblockungInputKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import type { List } from '../../../java/util/List';

export class SchuelerblockungInput extends JavaObject {

	/**
	 * Die Anzahl an vorhandenen Schienen.
	 */
	public schienen : number = 0;

	/**
	 * Alle Kurse, die zu den Fachwahlen des Schülers passen.
	 */
	public kurse : List<SchuelerblockungInputKurs> = new ArrayList<SchuelerblockungInputKurs>();

	/**
	 * Alle Fachwahlen des Schülers.
	 */
	public fachwahlen : List<GostFachwahl> = new ArrayList<GostFachwahl>();

	/**
	 * Zu jeder Fachwahl eine textuelle Darstellung.
	 */
	public fachwahlenText : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kursblockung.SchuelerblockungInput';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kursblockung.SchuelerblockungInput'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungInput {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungInput();
		if (typeof obj.schienen === "undefined")
			 throw new Error('invalid json format, missing attribute schienen');
		result.schienen = obj.schienen;
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
				result.kurse?.add(SchuelerblockungInputKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.fachwahlen !== undefined) && (obj.fachwahlen !== null)) {
			for (const elem of obj.fachwahlen) {
				result.fachwahlen?.add(GostFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.fachwahlenText !== undefined) && (obj.fachwahlenText !== null)) {
			for (const elem of obj.fachwahlenText) {
				result.fachwahlenText?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInput) : string {
		let result = '{';
		result += '"schienen" : ' + obj.schienen + ',';
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += SchuelerblockungInputKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlen) {
			result += '"fachwahlen" : []';
		} else {
			result += '"fachwahlen" : [ ';
			for (let i = 0; i < obj.fachwahlen.size(); i++) {
				const elem = obj.fachwahlen.get(i);
				result += GostFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlenText) {
			result += '"fachwahlenText" : []';
		} else {
			result += '"fachwahlenText" : [ ';
			for (let i = 0; i < obj.fachwahlenText.size(); i++) {
				const elem = obj.fachwahlenText.get(i);
				result += '"' + elem + '"';
				if (i < obj.fachwahlenText.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInput>) : string {
		let result = '{';
		if (typeof obj.schienen !== "undefined") {
			result += '"schienen" : ' + obj.schienen + ',';
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
					result += SchuelerblockungInputKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlen !== "undefined") {
			if (!obj.fachwahlen) {
				result += '"fachwahlen" : []';
			} else {
				result += '"fachwahlen" : [ ';
				for (let i = 0; i < obj.fachwahlen.size(); i++) {
					const elem = obj.fachwahlen.get(i);
					result += GostFachwahl.transpilerToJSON(elem);
					if (i < obj.fachwahlen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlenText !== "undefined") {
			if (!obj.fachwahlenText) {
				result += '"fachwahlenText" : []';
			} else {
				result += '"fachwahlenText" : [ ';
				for (let i = 0; i < obj.fachwahlenText.size(); i++) {
					const elem = obj.fachwahlenText.get(i);
					result += '"' + elem + '"';
					if (i < obj.fachwahlenText.size() - 1)
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

export function cast_de_svws_nrw_core_data_kursblockung_SchuelerblockungInput(obj : unknown) : SchuelerblockungInput {
	return obj as SchuelerblockungInput;
}
