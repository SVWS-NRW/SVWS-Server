import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';

export class KursblockungInputRegel extends JavaObject {

	public databaseID : number = 0;

	public typ : number = 0;

	public daten : Array<Number> = Array(0).fill(null);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungInputRegel {
		const obj = JSON.parse(json);
		const result = new KursblockungInputRegel();
		if (typeof obj.databaseID === "undefined")
			 throw new Error('invalid json format, missing attribute databaseID');
		result.databaseID = obj.databaseID;
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		for (let i : number = 0; i < obj.daten.length; i++) {
			result.daten[i] = obj.daten[i];
		}
		return result;
	}

	public static transpilerToJSON(obj : KursblockungInputRegel) : string {
		let result = '{';
		result += '"databaseID" : ' + obj.databaseID + ',';
		result += '"typ" : ' + obj.typ + ',';
		if (!obj.daten) {
			result += '"daten" : []';
		} else {
			result += '"daten" : [ ';
			for (let i : number = 0; i < obj.daten.length; i++) {
				let elem = obj.daten[i];
				result += elem;
				if (i < obj.daten.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungInputRegel>) : string {
		let result = '{';
		if (typeof obj.databaseID !== "undefined") {
			result += '"databaseID" : ' + obj.databaseID + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		if (typeof obj.daten !== "undefined") {
			let a = obj.daten;
			if (!a) {
				result += '"daten" : []';
			} else {
				result += '"daten" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += elem;
					if (i < a.length - 1)
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputRegel(obj : unknown) : KursblockungInputRegel {
	return obj as KursblockungInputRegel;
}
