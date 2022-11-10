import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchulformKatalogEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public nummer : String = "";

	public bezeichnung : String = "";

	public hatGymOb : boolean = false;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Schulform-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Schulform-Eintrag mit den angegebenen Werten
	 * 
	 * @param id           die ID
	 * @param kuerzel      das Kürzel 
	 * @param nummer       die Nummer
	 * @param bezeichnung  die Bezeichnung
	 * @param hatGymOb     gibt an, ob die Schulform eien gymnasiale Oberstufe hat 
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, nummer : String, bezeichnung : String, hatGymOb : boolean, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : String, __param4? : boolean, __param5? : Number | null, __param6? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && typeof __param4 === "boolean") && ((typeof __param5 !== "undefined") && ((__param5 instanceof Number) || (typeof __param5 === "number")) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof Number) || (typeof __param6 === "number")) || (__param6 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let nummer : String = __param2;
			let bezeichnung : String = __param3;
			let hatGymOb : boolean = __param4 as boolean;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param5);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param6);
			this.id = id;
			this.kuerzel = kuerzel;
			this.nummer = nummer;
			this.bezeichnung = bezeichnung;
			this.hatGymOb = hatGymOb;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchulformKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulformKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchulformKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.nummer === "undefined")
			 throw new Error('invalid json format, missing attribute nummer');
		result.nummer = String(obj.nummer);
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		if (typeof obj.hatGymOb === "undefined")
			 throw new Error('invalid json format, missing attribute hatGymOb');
		result.hatGymOb = obj.hatGymOb;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : SchulformKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"nummer" : ' + '"' + obj.nummer.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"hatGymOb" : ' + obj.hatGymOb + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulformKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + '"' + obj.nummer.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.hatGymOb !== "undefined") {
			result += '"hatGymOb" : ' + obj.hatGymOb + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_SchulformKatalogEintrag(obj : unknown) : SchulformKatalogEintrag {
	return obj as SchulformKatalogEintrag;
}
