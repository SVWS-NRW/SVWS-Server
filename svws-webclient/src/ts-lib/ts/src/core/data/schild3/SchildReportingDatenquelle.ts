import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchildReportingDatenquelleAttribut, cast_de_nrw_schule_svws_core_data_schild3_SchildReportingDatenquelleAttribut } from '../../../core/data/schild3/SchildReportingDatenquelleAttribut';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchildReportingDatenquelle extends JavaObject {

	public name : String = "";

	public beschreibung : String = "";

	public master : String | null = null;

	public masterattribut : String | null = null;

	public mastertyp : String | null = null;

	public linkattribut : String | null = null;

	public attribute : List<SchildReportingDatenquelleAttribut> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelle'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingDatenquelle {
		const obj = JSON.parse(json);
		const result = new SchildReportingDatenquelle();
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.master = typeof obj.master === "undefined" ? null : obj.master;
		result.masterattribut = typeof obj.masterattribut === "undefined" ? null : obj.masterattribut;
		result.mastertyp = typeof obj.mastertyp === "undefined" ? null : obj.mastertyp;
		result.linkattribut = typeof obj.linkattribut === "undefined" ? null : obj.linkattribut;
		if (!!obj.attribute) {
			for (let elem of obj.attribute) {
				result.attribute?.add(SchildReportingDatenquelleAttribut.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingDatenquelle) : string {
		let result = '{';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		result += '"master" : ' + ((!obj.master) ? 'null' : '"' + obj.master.valueOf() + '"') + ',';
		result += '"masterattribut" : ' + ((!obj.masterattribut) ? 'null' : '"' + obj.masterattribut.valueOf() + '"') + ',';
		result += '"mastertyp" : ' + ((!obj.mastertyp) ? 'null' : '"' + obj.mastertyp.valueOf() + '"') + ',';
		result += '"linkattribut" : ' + ((!obj.linkattribut) ? 'null' : '"' + obj.linkattribut.valueOf() + '"') + ',';
		if (!obj.attribute) {
			result += '"attribute" : []';
		} else {
			result += '"attribute" : [ ';
			for (let i : number = 0; i < obj.attribute.size(); i++) {
				let elem = obj.attribute.get(i);
				result += SchildReportingDatenquelleAttribut.transpilerToJSON(elem);
				if (i < obj.attribute.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingDatenquelle>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		}
		if (typeof obj.master !== "undefined") {
			result += '"master" : ' + ((!obj.master) ? 'null' : '"' + obj.master.valueOf() + '"') + ',';
		}
		if (typeof obj.masterattribut !== "undefined") {
			result += '"masterattribut" : ' + ((!obj.masterattribut) ? 'null' : '"' + obj.masterattribut.valueOf() + '"') + ',';
		}
		if (typeof obj.mastertyp !== "undefined") {
			result += '"mastertyp" : ' + ((!obj.mastertyp) ? 'null' : '"' + obj.mastertyp.valueOf() + '"') + ',';
		}
		if (typeof obj.linkattribut !== "undefined") {
			result += '"linkattribut" : ' + ((!obj.linkattribut) ? 'null' : '"' + obj.linkattribut.valueOf() + '"') + ',';
		}
		if (typeof obj.attribute !== "undefined") {
			if (!obj.attribute) {
				result += '"attribute" : []';
			} else {
				result += '"attribute" : [ ';
				for (let i : number = 0; i < obj.attribute.size(); i++) {
					let elem = obj.attribute.get(i);
					result += SchildReportingDatenquelleAttribut.transpilerToJSON(elem);
					if (i < obj.attribute.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schild3_SchildReportingDatenquelle(obj : unknown) : SchildReportingDatenquelle {
	return obj as SchildReportingDatenquelle;
}
