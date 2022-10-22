import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchildReportingDatenquelleAttribut, cast_de_nrw_schule_svws_core_data_schild3_SchildReportingDatenquelleAttribut } from '../../../core/data/schild3/SchildReportingDatenquelleAttribut';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SchildReportingAttributTyp, cast_de_nrw_schule_svws_core_types_schild3_SchildReportingAttributTyp } from '../../../core/types/schild3/SchildReportingAttributTyp';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchildReportingDatenquelle extends JavaObject {

	public name : String = "";

	public beschreibung : String = "";

	public master : String | null = null;

	public masterattribut : String | null = null;

	public mastertyp : String | null = null;

	public attribute : List<SchildReportingDatenquelleAttribut> = new Vector();


	/**
	 * Erstellt eine Datenquelle mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt eine Datenquelle mit den angegebenen Werten
	 * 
	 * @param name             der Name der Datenquelle
	 * @param beschreibung     die Beschreibung der Datenquelle
	 * @param master           die Master-Datenquelle
	 * @param masterattribut   das identifizierende Attribut der Master-Datenquelle
	 * @param mastertyp        der Typ des identifizierenden Attributs der Master-Datenquelle
	 * @param attribute        eine Liste der Attribute
	 */
	public constructor(name : String, beschreibung : String, master : String | null, masterattribut : String | null, mastertyp : SchildReportingAttributTyp | null, attribute : List<SchildReportingDatenquelleAttribut>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : String, __param1? : String, __param2? : String | null, __param3? : String | null, __param4? : SchildReportingAttributTyp | null, __param5? : List<SchildReportingDatenquelleAttribut>) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof String) || (typeof __param0 === "string"))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string")) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp'))) || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null))) {
			let name : String = __param0;
			let beschreibung : String = __param1;
			let master : String | null = __param2;
			let masterattribut : String | null = __param3;
			let mastertyp : SchildReportingAttributTyp | null = cast_de_nrw_schule_svws_core_types_schild3_SchildReportingAttributTyp(__param4);
			let attribute : List<SchildReportingDatenquelleAttribut> = cast_java_util_List(__param5);
			this.name = name;
			this.beschreibung = beschreibung;
			this.master = master;
			this.masterattribut = masterattribut;
			this.mastertyp = mastertyp.toString();
			this.attribute.addAll(attribute);
		} else throw new Error('invalid method overload');
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
