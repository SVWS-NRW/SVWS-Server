import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KursblockungInputKursart extends JavaObject {

	public enmRevision : number = -1;

	public id : number = 0;

	public representation : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungInputKursart {
		const obj = JSON.parse(json);
		const result = new KursblockungInputKursart();
		if (typeof obj.enmRevision === "undefined")
			 throw new Error('invalid json format, missing attribute enmRevision');
		result.enmRevision = obj.enmRevision;
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.representation === "undefined")
			 throw new Error('invalid json format, missing attribute representation');
		result.representation = obj.representation;
		return result;
	}

	public static transpilerToJSON(obj : KursblockungInputKursart) : string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision + ',';
		result += '"id" : ' + obj.id + ',';
		result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungInputKursart>) : string {
		let result = '{';
		if (typeof obj.enmRevision !== "undefined") {
			result += '"enmRevision" : ' + obj.enmRevision + ',';
		}
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.representation !== "undefined") {
			result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKursart(obj : unknown) : KursblockungInputKursart {
	return obj as KursblockungInputKursart;
}
