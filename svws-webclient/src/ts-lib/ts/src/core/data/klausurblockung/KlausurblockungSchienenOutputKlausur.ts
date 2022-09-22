import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class KlausurblockungSchienenOutputKlausur extends JavaObject {

	public id : number = -1;

	public schiene : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutputKlausur'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlausurblockungSchienenOutputKlausur {
		const obj = JSON.parse(json);
		const result = new KlausurblockungSchienenOutputKlausur();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schiene === "undefined")
			 throw new Error('invalid json format, missing attribute schiene');
		result.schiene = obj.schiene;
		return result;
	}

	public static transpilerToJSON(obj : KlausurblockungSchienenOutputKlausur) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schiene" : ' + obj.schiene + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlausurblockungSchienenOutputKlausur>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schiene !== "undefined") {
			result += '"schiene" : ' + obj.schiene + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutputKlausur(obj : unknown) : KlausurblockungSchienenOutputKlausur {
	return obj as KlausurblockungSchienenOutputKlausur;
}
