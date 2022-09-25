import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class SchuelerblockungOutputFachwahlZuKurs extends JavaObject {

	public fachwahl : number = 0;

	public kurs : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungOutputFachwahlZuKurs {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungOutputFachwahlZuKurs();
		if (typeof obj.fachwahl === "undefined")
			 throw new Error('invalid json format, missing attribute fachwahl');
		result.fachwahl = obj.fachwahl;
		if (typeof obj.kurs === "undefined")
			 throw new Error('invalid json format, missing attribute kurs');
		result.kurs = obj.kurs;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungOutputFachwahlZuKurs) : string {
		let result = '{';
		result += '"fachwahl" : ' + obj.fachwahl + ',';
		result += '"kurs" : ' + obj.kurs + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungOutputFachwahlZuKurs>) : string {
		let result = '{';
		if (typeof obj.fachwahl !== "undefined") {
			result += '"fachwahl" : ' + obj.fachwahl + ',';
		}
		if (typeof obj.kurs !== "undefined") {
			result += '"kurs" : ' + obj.kurs + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutputFachwahlZuKurs(obj : unknown) : SchuelerblockungOutputFachwahlZuKurs {
	return obj as SchuelerblockungOutputFachwahlZuKurs;
}
