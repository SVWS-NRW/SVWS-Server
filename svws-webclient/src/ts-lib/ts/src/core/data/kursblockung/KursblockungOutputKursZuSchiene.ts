import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class KursblockungOutputKursZuSchiene extends JavaObject {

	public enmRevision : number = -1;

	public kurs : number = 0;

	public schienen : Array<number> = Array(0).fill(0);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputKursZuSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungOutputKursZuSchiene {
		const obj = JSON.parse(json);
		const result = new KursblockungOutputKursZuSchiene();
		if (typeof obj.enmRevision === "undefined")
			 throw new Error('invalid json format, missing attribute enmRevision');
		result.enmRevision = obj.enmRevision;
		if (typeof obj.kurs === "undefined")
			 throw new Error('invalid json format, missing attribute kurs');
		result.kurs = obj.kurs;
		for (let i : number = 0; i < obj.schienen.length; i++) {
			result.schienen[i] = obj.schienen[i];
		}
		return result;
	}

	public static transpilerToJSON(obj : KursblockungOutputKursZuSchiene) : string {
		let result = '{';
		result += '"enmRevision" : ' + obj.enmRevision + ',';
		result += '"kurs" : ' + obj.kurs + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.length; i++) {
				let elem = obj.schienen[i];
				result += JSON.stringify(elem);
				if (i < obj.schienen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungOutputKursZuSchiene>) : string {
		let result = '{';
		if (typeof obj.enmRevision !== "undefined") {
			result += '"enmRevision" : ' + obj.enmRevision + ',';
		}
		if (typeof obj.kurs !== "undefined") {
			result += '"kurs" : ' + obj.kurs + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			let a = obj.schienen;
			if (!a) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungOutputKursZuSchiene(obj : unknown) : KursblockungOutputKursZuSchiene {
	return obj as KursblockungOutputKursZuSchiene;
}
