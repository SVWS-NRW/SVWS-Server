import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostSchuelerFachwahl extends JavaObject {

	/**
	 * Die Fachwahlen des Schülers für die sechs Halbjahre der gymnasialen Oberstufe
	 */
	public halbjahre : Array<string | null> = Array(6).fill(null);

	/**
	 * Die Nummer des Abiturfaches (1-4), falls dieses Fach als Abiturfach gewählt wurde und ansonsten null
	 */
	public abiturFach : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostSchuelerFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostSchuelerFachwahl {
		const obj = JSON.parse(json);
		const result = new GostSchuelerFachwahl();
		for (let i : number = 0; i < obj.halbjahre.length; i++) {
			result.halbjahre[i] = obj.halbjahre[i] === null ? null : obj.halbjahre[i];
		}
		result.abiturFach = typeof obj.abiturFach === "undefined" ? null : obj.abiturFach === null ? null : obj.abiturFach;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerFachwahl) : string {
		let result = '{';
		if (!obj.halbjahre) {
			result += '"halbjahre" : []';
		} else {
			result += '"halbjahre" : [ ';
			for (let i : number = 0; i < obj.halbjahre.length; i++) {
				let elem = obj.halbjahre[i];
				result += (elem == null) ? null : '"' + elem + '"';
				if (i < obj.halbjahre.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerFachwahl>) : string {
		let result = '{';
		if (typeof obj.halbjahre !== "undefined") {
			let a = obj.halbjahre;
			if (!a) {
				result += '"halbjahre" : []';
			} else {
				result += '"halbjahre" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += (elem == null) ? null : '"' + elem + '"';
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.abiturFach !== "undefined") {
			result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostSchuelerFachwahl(obj : unknown) : GostSchuelerFachwahl {
	return obj as GostSchuelerFachwahl;
}
