import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class KursblockungOutputFachwahlZuKurs extends JavaObject {

	public enmRevision : number = -1;

	public fachwahl : number = 0;

	public kurs : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungOutputFachwahlZuKurs {
		const obj = JSON.parse(json);
		const result = new KursblockungOutputFachwahlZuKurs();
		if (typeof obj.enmRevision === "undefined")
			 throw new Error('invalid json format, missing attribute enmRevision');
		result.enmRevision = obj.enmRevision;
		if (typeof obj.fachwahl === "undefined")
			 throw new Error('invalid json format, missing attribute fachwahl');
		result.fachwahl = obj.fachwahl;
		if (typeof obj.kurs === "undefined")
			 throw new Error('invalid json format, missing attribute kurs');
		result.kurs = obj.kurs;
		return result;
	}

	public static transpilerToJSON(obj : KursblockungOutputFachwahlZuKurs) : string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision + ',';
		result += '"fachwahl" : ' + obj.fachwahl + ',';
		result += '"kurs" : ' + obj.kurs + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungOutputFachwahlZuKurs>) : string {
		let result = '{';
		if (typeof obj.enmRevision !== "undefined") {
			result += '"enmRevision" : ' + obj.enmRevision + ',';
		}
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutputFachwahlZuKurs(obj : unknown) : KursblockungOutputFachwahlZuKurs {
	return obj as KursblockungOutputFachwahlZuKurs;
}
