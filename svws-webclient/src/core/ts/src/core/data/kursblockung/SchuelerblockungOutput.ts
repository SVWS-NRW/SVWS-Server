import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchuelerblockungOutputFachwahlZuKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutputFachwahlZuKurs } from '../../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerblockungOutput extends JavaObject {

	/**
	 * Alle Fachwahlen-Zuordnungen. 
	 */
	public fachwahlenZuKurs : Vector<SchuelerblockungOutputFachwahlZuKurs> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutput'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungOutput {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungOutput();
		if (!!obj.fachwahlenZuKurs) {
			for (let elem of obj.fachwahlenZuKurs) {
				result.fachwahlenZuKurs?.add(SchuelerblockungOutputFachwahlZuKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungOutput) : string {
		let result = '{';
		if (!obj.fachwahlenZuKurs) {
			result += '"fachwahlenZuKurs" : []';
		} else {
			result += '"fachwahlenZuKurs" : [ ';
			for (let i : number = 0; i < obj.fachwahlenZuKurs.size(); i++) {
				let elem = obj.fachwahlenZuKurs.get(i);
				result += SchuelerblockungOutputFachwahlZuKurs.transpilerToJSON(elem);
				if (i < obj.fachwahlenZuKurs.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungOutput>) : string {
		let result = '{';
		if (typeof obj.fachwahlenZuKurs !== "undefined") {
			if (!obj.fachwahlenZuKurs) {
				result += '"fachwahlenZuKurs" : []';
			} else {
				result += '"fachwahlenZuKurs" : [ ';
				for (let i : number = 0; i < obj.fachwahlenZuKurs.size(); i++) {
					let elem = obj.fachwahlenZuKurs.get(i);
					result += SchuelerblockungOutputFachwahlZuKurs.transpilerToJSON(elem);
					if (i < obj.fachwahlenZuKurs.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutput(obj : unknown) : SchuelerblockungOutput {
	return obj as SchuelerblockungOutput;
}
