import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class HerkunftSchulformKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags. 
	 */
	public id : number = -1;

	/**
	 * Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Schulform verwendet wird 
	 */
	public kuerzel : string = "";

	/**
	 * Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird 
	 */
	public kuerzelStatistik : string = "";

	/**
	 * Die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann. 
	 */
	public schulformen : List<string> = new Vector();

	/**
	 * Die textuelle Beschreibung der Herkunftsschulform. 
	 */
	public beschreibung : string = "";

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. 
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel, welches bei dem Core-Type Schulform verwendet wird
	 * @param kuerzelStatistik   das Kürzel, welches bei der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird 
	 * @param schulformen        die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann
	 * @param beschreibung       die textuelle Beschreibung der Herkunftsschulform
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, kuerzelStatistik : string, schulformen : List<Schulform>, beschreibung : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : List<Schulform>, __param4? : string, __param5? : null | number, __param6? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string")) && ((typeof __param5 !== "undefined") && (typeof __param5 === "number") || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let kuerzelStatistik : string = __param2;
			let schulformen : List<Schulform> = cast_java_util_List(__param3);
			let beschreibung : string = __param4;
			let gueltigVon : number | null = __param5;
			let gueltigBis : number | null = __param6;
			this.id = id;
			this.kuerzel = kuerzel;
			this.kuerzelStatistik = kuerzelStatistik;
			for (let schulform of schulformen) 
				this.schulformen.add(schulform.daten.kuerzel);
			this.beschreibung = beschreibung;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.HerkunftSchulformKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): HerkunftSchulformKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new HerkunftSchulformKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.kuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (!!obj.schulformen) {
			for (let elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : HerkunftSchulformKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"kuerzelStatistik" : ' + '"' + obj.kuerzelStatistik! + '"' + ',';
		if (!obj.schulformen) {
			result += '"schulformen" : []';
		} else {
			result += '"schulformen" : [ ';
			for (let i : number = 0; i < obj.schulformen.size(); i++) {
				let elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftSchulformKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + '"' + obj.kuerzelStatistik + '"' + ',';
		}
		if (typeof obj.schulformen !== "undefined") {
			if (!obj.schulformen) {
				result += '"schulformen" : []';
			} else {
				result += '"schulformen" : [ ';
				for (let i : number = 0; i < obj.schulformen.size(); i++) {
					let elem = obj.schulformen.get(i);
					result += '"' + elem + '"';
					if (i < obj.schulformen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_HerkunftSchulformKatalogEintrag(obj : unknown) : HerkunftSchulformKatalogEintrag {
	return obj as HerkunftSchulformKatalogEintrag;
}
