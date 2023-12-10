import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerblockungOutputFachwahlZuKurs } from '../../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuelerblockungOutput extends JavaObject {

	/**
	 * Alle Fachwahlen-Zuordnungen.
	 */
	public fachwahlenZuKurs : List<SchuelerblockungOutputFachwahlZuKurs> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungOutput {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungOutput();
		if ((obj.fachwahlenZuKurs !== undefined) && (obj.fachwahlenZuKurs !== null)) {
			for (const elem of obj.fachwahlenZuKurs) {
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
			for (let i = 0; i < obj.fachwahlenZuKurs.size(); i++) {
				const elem = obj.fachwahlenZuKurs.get(i);
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
				for (let i = 0; i < obj.fachwahlenZuKurs.size(); i++) {
					const elem = obj.fachwahlenZuKurs.get(i);
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

export function cast_de_svws_nrw_core_data_kursblockung_SchuelerblockungOutput(obj : unknown) : SchuelerblockungOutput {
	return obj as SchuelerblockungOutput;
}
