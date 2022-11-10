import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchulformSchulgliederung, cast_de_nrw_schule_svws_core_data_schule_SchulformSchulgliederung } from '../../../core/data/schule/SchulformSchulgliederung';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Pair, cast_de_nrw_schule_svws_core_adt_Pair } from '../../../core/adt/Pair';

export class KursartKatalogEintrag extends JavaObject {

	public id : number = -1;

	public kuerzel : String = "";

	public nummer : String = "";

	public bezeichnung : String = "";

	public bemerkungen : String | null = null;

	public kuerzelAllg : String | null = null;

	public bezeichnungAllg : String | null = null;

	public erlaubtGOSt : boolean = false;

	public zulaessig : List<SchulformSchulgliederung> = new Vector();

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param nummer             die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param bezeichnung        die Bezeichnung der Kursart
	 * @param bemerkungen        ergänzende Bemerkungen zu der Kursart
	 * @param kuerzelAllg        das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist
	 * @param bezeichnungAllg    die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist
	 * @param erlaubtGOSt        gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist
	 * @param zulaessig          die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist 
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                           "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, nummer : String, bezeichnung : String, bemerkungen : String | null, kuerzelAllg : String | null, bezeichnungAllg : String | null, erlaubtGOSt : boolean, zulaessig : List<Pair<Schulform, Schulgliederung | null>>, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : String, __param4? : String | null, __param5? : String | null, __param6? : String | null, __param7? : boolean, __param8? : List<Pair<Schulform, Schulgliederung | null>>, __param9? : Number | null, __param10? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof String) || (typeof __param4 === "string")) || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof String) || (typeof __param5 === "string")) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof String) || (typeof __param6 === "string")) || (__param6 === null)) && ((typeof __param7 !== "undefined") && typeof __param7 === "boolean") && ((typeof __param8 !== "undefined") && ((__param8 instanceof JavaObject) && (__param8.isTranspiledInstanceOf('java.util.List'))) || (__param8 === null)) && ((typeof __param9 !== "undefined") && ((__param9 instanceof Number) || (typeof __param9 === "number")) || (__param9 === null)) && ((typeof __param10 !== "undefined") && ((__param10 instanceof Number) || (typeof __param10 === "number")) || (__param10 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let nummer : String = __param2;
			let bezeichnung : String = __param3;
			let bemerkungen : String | null = __param4;
			let kuerzelAllg : String | null = __param5;
			let bezeichnungAllg : String | null = __param6;
			let erlaubtGOSt : boolean = __param7 as boolean;
			let zulaessig : List<Pair<Schulform, Schulgliederung | null>> = cast_java_util_List(__param8);
			let gueltigVon : Number | null = cast_java_lang_Integer(__param9);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param10);
			this.id = id;
			this.kuerzel = kuerzel;
			this.nummer = nummer;
			this.bezeichnung = bezeichnung;
			this.bemerkungen = bemerkungen;
			this.kuerzelAllg = kuerzelAllg;
			this.bezeichnungAllg = bezeichnungAllg;
			this.erlaubtGOSt = erlaubtGOSt;
			for (let zul of zulaessig) {
				let sfsgl : SchulformSchulgliederung | null = new SchulformSchulgliederung();
				let sf : Schulform = zul.a;
				if (sf.daten === null) 
					continue;
				sfsgl.schulform = sf.daten.kuerzel;
				let sgl : Schulgliederung | null = zul.b;
				sfsgl.gliederung = ((sgl === null) || (sgl.daten === null)) ? null : sgl.daten.kuerzel;
				this.zulaessig.add(sfsgl);
			}
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kurse.KursartKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursartKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new KursartKatalogEintrag();
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
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : String(obj.bemerkungen);
		result.kuerzelAllg = typeof obj.kuerzelAllg === "undefined" ? null : obj.kuerzelAllg === null ? null : String(obj.kuerzelAllg);
		result.bezeichnungAllg = typeof obj.bezeichnungAllg === "undefined" ? null : obj.bezeichnungAllg === null ? null : String(obj.bezeichnungAllg);
		if (typeof obj.erlaubtGOSt === "undefined")
			 throw new Error('invalid json format, missing attribute erlaubtGOSt');
		result.erlaubtGOSt = obj.erlaubtGOSt;
		if (!!obj.zulaessig) {
			for (let elem of obj.zulaessig) {
				result.zulaessig?.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : KursartKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"nummer" : ' + '"' + obj.nummer.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		result += '"kuerzelAllg" : ' + ((!obj.kuerzelAllg) ? 'null' : '"' + obj.kuerzelAllg.valueOf() + '"') + ',';
		result += '"bezeichnungAllg" : ' + ((!obj.bezeichnungAllg) ? 'null' : '"' + obj.bezeichnungAllg.valueOf() + '"') + ',';
		result += '"erlaubtGOSt" : ' + obj.erlaubtGOSt + ',';
		if (!obj.zulaessig) {
			result += '"zulaessig" : []';
		} else {
			result += '"zulaessig" : [ ';
			for (let i : number = 0; i < obj.zulaessig.size(); i++) {
				let elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
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

	public static transpilerToJSONPatch(obj : Partial<KursartKatalogEintrag>) : string {
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
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzelAllg !== "undefined") {
			result += '"kuerzelAllg" : ' + ((!obj.kuerzelAllg) ? 'null' : '"' + obj.kuerzelAllg.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnungAllg !== "undefined") {
			result += '"bezeichnungAllg" : ' + ((!obj.bezeichnungAllg) ? 'null' : '"' + obj.bezeichnungAllg.valueOf() + '"') + ',';
		}
		if (typeof obj.erlaubtGOSt !== "undefined") {
			result += '"erlaubtGOSt" : ' + obj.erlaubtGOSt + ',';
		}
		if (typeof obj.zulaessig !== "undefined") {
			if (!obj.zulaessig) {
				result += '"zulaessig" : []';
			} else {
				result += '"zulaessig" : [ ';
				for (let i : number = 0; i < obj.zulaessig.size(); i++) {
					let elem = obj.zulaessig.get(i);
					result += SchulformSchulgliederung.transpilerToJSON(elem);
					if (i < obj.zulaessig.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_kurse_KursartKatalogEintrag(obj : unknown) : KursartKatalogEintrag {
	return obj as KursartKatalogEintrag;
}
