import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KursblockungInputKurs extends JavaObject {

	public enmRevision : number = -1;

	public id : number = 0;

	public fach : number = 0;

	public kursart : number = 0;

	public schienen : number = 0;

	public representation : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungInputKurs {
		const obj = JSON.parse(json);
		const result = new KursblockungInputKurs();
		if (typeof obj.enmRevision === "undefined")
			 throw new Error('invalid json format, missing attribute enmRevision');
		result.enmRevision = obj.enmRevision;
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.schienen === "undefined")
			 throw new Error('invalid json format, missing attribute schienen');
		result.schienen = obj.schienen;
		if (typeof obj.representation === "undefined")
			 throw new Error('invalid json format, missing attribute representation');
		result.representation = obj.representation;
		return result;
	}

	public static transpilerToJSON(obj : KursblockungInputKurs) : string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision + ',';
		result += '"id" : ' + obj.id + ',';
		result += '"fach" : ' + obj.fach + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"schienen" : ' + obj.schienen + ',';
		result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungInputKurs>) : string {
		let result = '{';
		if (typeof obj.enmRevision !== "undefined") {
			result += '"enmRevision" : ' + obj.enmRevision + ',';
		}
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + obj.fach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			result += '"schienen" : ' + obj.schienen + ',';
		}
		if (typeof obj.representation !== "undefined") {
			result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKurs(obj : unknown) : KursblockungInputKurs {
	return obj as KursblockungInputKurs;
}
