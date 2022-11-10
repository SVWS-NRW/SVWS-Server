import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { BerufskollegBildungsgangTyp, cast_de_nrw_schule_svws_core_types_schule_BerufskollegBildungsgangTyp } from '../../../core/types/schule/BerufskollegBildungsgangTyp';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../java/lang/NullPointerException';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { WeiterbildungskollegBildungsgangTyp, cast_de_nrw_schule_svws_core_types_schule_WeiterbildungskollegBildungsgangTyp } from '../../../core/types/schule/WeiterbildungskollegBildungsgangTyp';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class HerkunftBildungsgangTypKatalogEintrag extends JavaObject {

	public id : number = -1;

	public kuerzel : String = "";

	public schulformen : List<String> = new Vector();

	public beschreibung : String = "";

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id            die ID
	 * @param wbkTyp        der Bildungsgangtyp am Weiterbildungskolleg oder null 
	 * @param bkTyp         der Bildungsgangtyp am BErufskolleg oder null 
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, wbkTyp : WeiterbildungskollegBildungsgangTyp | null, bkTyp : BerufskollegBildungsgangTyp | null, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : WeiterbildungskollegBildungsgangTyp | null, __param2? : BerufskollegBildungsgangTyp | null, __param3? : Number | null, __param4? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schule.WeiterbildungskollegBildungsgangTyp'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schule.BerufskollegBildungsgangTyp'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof Number) || (typeof __param3 === "number")) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof Number) || (typeof __param4 === "number")) || (__param4 === null))) {
			let id : number = __param0 as number;
			let wbkTyp : WeiterbildungskollegBildungsgangTyp | null = cast_de_nrw_schule_svws_core_types_schule_WeiterbildungskollegBildungsgangTyp(__param1);
			let bkTyp : BerufskollegBildungsgangTyp | null = cast_de_nrw_schule_svws_core_types_schule_BerufskollegBildungsgangTyp(__param2);
			let gueltigVon : Number | null = cast_java_lang_Integer(__param3);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param4);
			this.id = id;
			if ((wbkTyp !== null) && (bkTyp !== null)) 
				throw new IllegalArgumentException("Fehler im Core-Type: wbkTyp und bkTyp dürfen nicht gleichzeitig gesetzt sein.")
			if (wbkTyp !== null) {
				this.kuerzel = wbkTyp.daten.kuerzel;
				this.schulformen.add(Schulform.WB.daten.kuerzel);
				this.beschreibung = wbkTyp.daten.bezeichnung;
			} else 
				if (bkTyp !== null) {
					this.kuerzel = bkTyp.daten.kuerzel;
					this.schulformen.add(Schulform.BK.daten.kuerzel);
					this.schulformen.add(Schulform.SB.daten.kuerzel);
					this.beschreibung = bkTyp.daten.bezeichnung;
				} else 
					throw new NullPointerException("Fehler im Core-Type. Entweder wbkTyp oder bkTyp muss gesetzt sein.")
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.HerkunftBildungsgangTypKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): HerkunftBildungsgangTypKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new HerkunftBildungsgangTypKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (!!obj.schulformen) {
			for (let elem of obj.schulformen) {
				result.schulformen?.add(String(elem));
			}
		}
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = String(obj.beschreibung);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : HerkunftBildungsgangTypKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
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
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftBildungsgangTypKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
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
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_HerkunftBildungsgangTypKatalogEintrag(obj : unknown) : HerkunftBildungsgangTypKatalogEintrag {
	return obj as HerkunftBildungsgangTypKatalogEintrag;
}
