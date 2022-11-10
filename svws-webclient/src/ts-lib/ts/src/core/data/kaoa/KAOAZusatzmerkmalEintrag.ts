import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { KAOAZusatzmerkmaleOptionsarten, cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmaleOptionsarten } from '../../../core/types/kaoa/KAOAZusatzmerkmaleOptionsarten';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { KAOAMerkmal, cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmal } from '../../../core/types/kaoa/KAOAMerkmal';

export class KAOAZusatzmerkmalEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public beschreibung : String = "";

	public merkmal : String = "";

	public optionsart : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param merkmal        das Merkmal, dem das Zusatzmerkmal zugeordnet ist
	 * @param optionsart     die Optionsart bei dem Zusatzmerkmal
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, beschreibung : String, merkmal : KAOAMerkmal, optionsart : KAOAZusatzmerkmaleOptionsarten, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : KAOAMerkmal, __param4? : KAOAZusatzmerkmaleOptionsarten, __param5? : Number | null, __param6? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAMerkmal')))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmaleOptionsarten')))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof Number) || (typeof __param5 === "number")) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof Number) || (typeof __param6 === "number")) || (__param6 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let beschreibung : String = __param2;
			let merkmal : KAOAMerkmal = cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmal(__param3);
			let optionsart : KAOAZusatzmerkmaleOptionsarten = cast_de_nrw_schule_svws_core_types_kaoa_KAOAZusatzmerkmaleOptionsarten(__param4);
			let gueltigVon : Number | null = cast_java_lang_Integer(__param5);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param6);
			this.id = id;
			this.kuerzel = kuerzel;
			this.beschreibung = beschreibung;
			this.merkmal = merkmal.daten.kuerzel;
			this.optionsart = optionsart.kuerzel;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kaoa.KAOAZusatzmerkmalEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KAOAZusatzmerkmalEintrag {
		const obj = JSON.parse(json);
		const result = new KAOAZusatzmerkmalEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = String(obj.beschreibung);
		if (typeof obj.merkmal === "undefined")
			 throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = String(obj.merkmal);
		result.optionsart = typeof obj.optionsart === "undefined" ? null : obj.optionsart === null ? null : String(obj.optionsart);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : KAOAZusatzmerkmalEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		result += '"merkmal" : ' + '"' + obj.merkmal.valueOf() + '"' + ',';
		result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAZusatzmerkmalEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		}
		if (typeof obj.merkmal !== "undefined") {
			result += '"merkmal" : ' + '"' + obj.merkmal.valueOf() + '"' + ',';
		}
		if (typeof obj.optionsart !== "undefined") {
			result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAZusatzmerkmalEintrag(obj : unknown) : KAOAZusatzmerkmalEintrag {
	return obj as KAOAZusatzmerkmalEintrag;
}
