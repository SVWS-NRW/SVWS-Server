import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchuelerzuordnung, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchuelerzuordnung } from '../../../core/data/gost/GostBlockungsergebnisSchuelerzuordnung';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBlockungsergebnisKurs extends JavaObject {

	public id : number = -1;

	public schienenID : Number | null = null;

	public fachID : number = -1;

	public kursart : String | null = "GK";

	public name : String = "D-GK1";

	public anzahlKollisionen : number = 0;

	public readonly schueler : Vector<GostBlockungsergebnisSchuelerzuordnung> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKurs {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.schienenID = typeof obj.schienenID === "undefined" ? null : obj.schienenID;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursart = typeof obj.kursart === "undefined" ? null : obj.kursart;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.anzahlKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlKollisionen');
		result.anzahlKollisionen = obj.anzahlKollisionen;
		if (!!obj.schueler) {
			for (let elem of obj.schueler) {
				result.schueler?.add(GostBlockungsergebnisSchuelerzuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schienenID" : ' + ((!obj.schienenID) ? 'null' : obj.schienenID.valueOf()) + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i : number = 0; i < obj.schueler.size(); i++) {
				let elem = obj.schueler.get(i);
				result += GostBlockungsergebnisSchuelerzuordnung.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schienenID !== "undefined") {
			result += '"schienenID" : ' + ((!obj.schienenID) ? 'null' : obj.schienenID.valueOf()) + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.anzahlKollisionen !== "undefined") {
			result += '"anzahlKollisionen" : ' + obj.anzahlKollisionen + ',';
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i : number = 0; i < obj.schueler.size(); i++) {
					let elem = obj.schueler.get(i);
					result += GostBlockungsergebnisSchuelerzuordnung.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisKurs(obj : unknown) : GostBlockungsergebnisKurs {
	return obj as GostBlockungsergebnisKurs;
}
