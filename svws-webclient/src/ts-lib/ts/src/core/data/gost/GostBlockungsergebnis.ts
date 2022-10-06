import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBlockungsergebnis extends JavaObject {

	public id : number = -1;

	public blockungID : number = -1;

	public name : String = "Blockung";

	public gostHalbjahr : number = 0;

	public anzahlUmwaehler : number = 0;

	public anzahlKollisionen : number = 0;

	public anzahlSchienenMitKollisionen : number = 0;

	public bewertung : number = -1;

	public istMarkiert : boolean = false;

	public istVorlage : boolean = false;

	public readonly schienen : Vector<GostBlockungsergebnisSchiene> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnis {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnis();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.blockungID === "undefined")
			 throw new Error('invalid json format, missing attribute blockungID');
		result.blockungID = obj.blockungID;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (typeof obj.anzahlUmwaehler === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlUmwaehler');
		result.anzahlUmwaehler = obj.anzahlUmwaehler;
		if (typeof obj.anzahlKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlKollisionen');
		result.anzahlKollisionen = obj.anzahlKollisionen;
		if (typeof obj.anzahlSchienenMitKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchienenMitKollisionen');
		result.anzahlSchienenMitKollisionen = obj.anzahlSchienenMitKollisionen;
		if (typeof obj.bewertung === "undefined")
			 throw new Error('invalid json format, missing attribute bewertung');
		result.bewertung = obj.bewertung;
		if (typeof obj.istMarkiert === "undefined")
			 throw new Error('invalid json format, missing attribute istMarkiert');
		result.istMarkiert = obj.istMarkiert;
		if (typeof obj.istVorlage === "undefined")
			 throw new Error('invalid json format, missing attribute istVorlage');
		result.istVorlage = obj.istVorlage;
		if (!!obj.schienen) {
			for (let elem of obj.schienen) {
				result.schienen?.add(GostBlockungsergebnisSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnis) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"blockungID" : ' + obj.blockungID + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"anzahlUmwaehler" : ' + obj.anzahlUmwaehler + ',';
		result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		result += '"anzahlSchienenMitKollisionen" : ' + obj.anzahlSchienenMitKollisionen + ',';
		result += '"bewertung" : ' + obj.bewertung + ',';
		result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		result += '"istVorlage" : ' + obj.istVorlage + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.size(); i++) {
				let elem = obj.schienen.get(i);
				result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnis>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.blockungID !== "undefined") {
			result += '"blockungID" : ' + obj.blockungID + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.anzahlUmwaehler !== "undefined") {
			result += '"anzahlUmwaehler" : ' + obj.anzahlUmwaehler + ',';
		}
		if (typeof obj.anzahlKollisionen !== "undefined") {
			result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		}
		if (typeof obj.anzahlSchienenMitKollisionen !== "undefined") {
			result += '"anzahlSchienenMitKollisionen" : ' + obj.anzahlSchienenMitKollisionen + ',';
		}
		if (typeof obj.bewertung !== "undefined") {
			result += '"bewertung" : ' + obj.bewertung + ',';
		}
		if (typeof obj.istMarkiert !== "undefined") {
			result += '"istMarkiert" : ' + obj.istMarkiert + ',';
		}
		if (typeof obj.istVorlage !== "undefined") {
			result += '"istVorlage" : ' + obj.istVorlage + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < obj.schienen.size(); i++) {
					let elem = obj.schienen.get(i);
					result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
					if (i < obj.schienen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis(obj : unknown) : GostBlockungsergebnis {
	return obj as GostBlockungsergebnis;
}
