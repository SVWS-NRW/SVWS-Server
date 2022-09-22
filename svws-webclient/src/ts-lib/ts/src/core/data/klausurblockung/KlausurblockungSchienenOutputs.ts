import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenOutput, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutput } from '../../../core/data/klausurblockung/KlausurblockungSchienenOutput';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KlausurblockungSchienenOutputs extends JavaObject {

	public ergebnisse : Vector<KlausurblockungSchienenOutput> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutputs'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlausurblockungSchienenOutputs {
		const obj = JSON.parse(json);
		const result = new KlausurblockungSchienenOutputs();
		if (!!obj.ergebnisse) {
			for (let elem of obj.ergebnisse) {
				result.ergebnisse?.add(KlausurblockungSchienenOutput.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlausurblockungSchienenOutputs) : string {
		let result = '{';
		if (!obj.ergebnisse) {
			result += '"ergebnisse" : []';
		} else {
			result += '"ergebnisse" : [ ';
			for (let i : number = 0; i < obj.ergebnisse.size(); i++) {
				let elem = obj.ergebnisse.get(i);
				result += KlausurblockungSchienenOutput.transpilerToJSON(elem);
				if (i < obj.ergebnisse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlausurblockungSchienenOutputs>) : string {
		let result = '{';
		if (typeof obj.ergebnisse !== "undefined") {
			if (!obj.ergebnisse) {
				result += '"ergebnisse" : []';
			} else {
				result += '"ergebnisse" : [ ';
				for (let i : number = 0; i < obj.ergebnisse.size(); i++) {
					let elem = obj.ergebnisse.get(i);
					result += KlausurblockungSchienenOutput.transpilerToJSON(elem);
					if (i < obj.ergebnisse.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutputs(obj : unknown) : KlausurblockungSchienenOutputs {
	return obj as KlausurblockungSchienenOutputs;
}
