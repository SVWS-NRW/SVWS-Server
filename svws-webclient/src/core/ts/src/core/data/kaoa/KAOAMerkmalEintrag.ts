import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { KAOAKategorie, cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie } from '../../../core/types/kaoa/KAOAKategorie';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { KAOAMerkmaleOptionsarten, cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten } from '../../../core/types/kaoa/KAOAMerkmaleOptionsarten';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KAOAMerkmalEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags. 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Merkmals. 
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung des Merkmals. 
	 */
	public beschreibung : string = "";

	/**
	 * Die Kategorie, welcher das Merkmal zugeordnet ist. 
	 */
	public kategorie : string = "";

	/**
	 * Die Optionsart des Merkmals. 
	 */
	public optionsart : string | null = null;

	/**
	 * Die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf 
	 */
	public bkAnlagen : List<string> = new Vector();

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigBis : number | null = null;


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
	public constructor(id : number, kuerzel : string, beschreibung : string, kategorie : KAOAKategorie, optionsart : KAOAMerkmaleOptionsarten, bkAnlagen : List<Schulgliederung>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : KAOAKategorie, __param4? : KAOAMerkmaleOptionsarten, __param5? : List<Schulgliederung>, __param6? : null | number, __param7? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAKategorie')))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.kaoa.KAOAMerkmaleOptionsarten')))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "number") || (__param6 === null)) && ((typeof __param7 !== "undefined") && (typeof __param7 === "number") || (__param7 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let beschreibung : string = __param2;
			let kategorie : KAOAKategorie = cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie(__param3);
			let optionsart : KAOAMerkmaleOptionsarten = cast_de_nrw_schule_svws_core_types_kaoa_KAOAMerkmaleOptionsarten(__param4);
			let bkAnlagen : List<Schulgliederung> = cast_java_util_List(__param5);
			let gueltigVon : number | null = __param6;
			let gueltigBis : number | null = __param7;
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
		result.optionsart = typeof obj.optionsart === "undefined" ? null : obj.optionsart === null ? null : obj.optionsart;
		if (!!obj.bkAnlagen) {
			for (let elem of obj.bkAnlagen) {
				result.bkAnlagen?.add(elem);
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAMerkmalEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result += '"kategorie" : ' + '"' + obj.kategorie! + '"' + ',';
		result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart + '"') + ',';
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
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		if (typeof obj.kategorie !== "undefined") {
			result += '"kategorie" : ' + '"' + obj.kategorie + '"' + ',';
		}
		if (typeof obj.optionsart !== "undefined") {
			result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : '"' + obj.optionsart + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kaoa_KAOAMerkmalEintrag(obj : unknown) : KAOAMerkmalEintrag {
	return obj as KAOAMerkmalEintrag;
}
