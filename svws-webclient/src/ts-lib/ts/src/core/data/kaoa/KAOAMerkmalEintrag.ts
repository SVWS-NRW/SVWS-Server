import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { KAOAKategorie, cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie } from '../../../core/types/kaoa/KAOAKategorie';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_statkue_Schulgliederung } from '../../../core/types/statkue/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { KAOAMerkmaleOptionsarten, cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten } from '../../../core/types/kaoa/KAOAMerkmaleOptionsarten';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KAOAMerkmalEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public beschreibung : String = "";

	public kategorie : String = "";

	public optionsart : String | null = null;

	public bkAnlagen : List<String> = new Vector();

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen KAoA-Merkmal-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen KAoA-Merkmal-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param kategorie      die Kategorie, dem das Merkmal zugeordnet ist
	 * @param optionsart     die Optionsart bei dem Merkmal
	 * @param bkAnlagen      die zulässigen Anlagen beim Berufskolleg
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, beschreibung : String, kategorie : KAOAKategorie, optionsart : KAOAMerkmaleOptionsarten, bkAnlagen : List<Schulgliederung>, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : KAOAKategorie, __param4? : KAOAMerkmaleOptionsarten, __param5? : List<Schulgliederung>, __param6? : Number | null, __param7? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAKategorie')))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAMerkmaleOptionsarten')))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof Number) || (typeof __param6 === "number")) || (__param6 === null)) && ((typeof __param7 !== "undefined") && ((__param7 instanceof Number) || (typeof __param7 === "number")) || (__param7 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let beschreibung : String = __param2;
			let kategorie : KAOAKategorie = cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie(__param3);
			let optionsart : KAOAMerkmaleOptionsarten = cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten(__param4);
			let bkAnlagen : List<Schulgliederung> = cast_java_util_List(__param5);
			let gueltigVon : Number | null = cast_java_lang_Integer(__param6);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param7);
			this.id = id;
			this.kuerzel = kuerzel;
			this.beschreibung = beschreibung;
			this.kategorie = kategorie.daten.kuerzel;
			this.optionsart = optionsart.kuerzel;
			for (let gl of bkAnlagen) {
				if (gl.daten.bkAnlage === null) 
					throw new NullPointerException("Es wurde keine Gliederung des Berufskollges als Anlage angegeben.")
				this.bkAnlagen.add(gl.daten.kuerzel);
			}
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kaoa.KAOAMerkmalEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KAOAMerkmalEintrag {
		const obj = JSON.parse(json);
		const result = new KAOAMerkmalEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (typeof obj.kategorie === "undefined")
			 throw new Error('invalid json format, missing attribute kategorie');
		result.kategorie = obj.kategorie;
		result.optionsart = typeof obj.optionsart === "undefined" ? null : obj.optionsart;
		if (!!obj.bkAnlagen) {
			for (let elem of obj.bkAnlagen) {
				result.bkAnlagen?.add(elem);
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAMerkmalEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		result += '"kategorie" : ' + '"' + obj.kategorie.valueOf() + '"' + ',';
		result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart.valueOf() + '"') + ',';
		if (!obj.bkAnlagen) {
			result += '"bkAnlagen" : []';
		} else {
			result += '"bkAnlagen" : [ ';
			for (let i : number = 0; i < obj.bkAnlagen.size(); i++) {
				let elem = obj.bkAnlagen.get(i);
				result += '"' + elem + '"';
				if (i < obj.bkAnlagen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAMerkmalEintrag>) : string {
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
		if (typeof obj.kategorie !== "undefined") {
			result += '"kategorie" : ' + '"' + obj.kategorie.valueOf() + '"' + ',';
		}
		if (typeof obj.optionsart !== "undefined") {
			result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart.valueOf() + '"') + ',';
		}
		if (typeof obj.bkAnlagen !== "undefined") {
			if (!obj.bkAnlagen) {
				result += '"bkAnlagen" : []';
			} else {
				result += '"bkAnlagen" : [ ';
				for (let i : number = 0; i < obj.bkAnlagen.size(); i++) {
					let elem = obj.bkAnlagen.get(i);
					result += '"' + elem + '"';
					if (i < obj.bkAnlagen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
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

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAMerkmalEintrag(obj : unknown) : KAOAMerkmalEintrag {
	return obj as KAOAMerkmalEintrag;
}
