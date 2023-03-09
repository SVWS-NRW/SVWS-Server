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

	/**
	 * Die ID des Katalog-Eintrags. 
	 */
	public id : number = -1;

	/**
	 * Das eindeutige Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik 
	 */
	public kuerzel : string = "";

	/**
	 * Die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik 
	 */
	public nummer : string = "";

	/**
	 * Die Bezeichnung der Kursart 
	 */
	public bezeichnung : string = "";

	/**
	 * Ergänzende Bemerkungen zu der Kursart 
	 */
	public bemerkungen : string | null = null;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist 
	 */
	public kuerzelAllg : string | null = null;

	/**
	 * Die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist 
	 */
	public bezeichnungAllg : string | null = null;

	/**
	 * Gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist 
	 */
	public erlaubtGOSt : boolean = false;

	/**
	 * Die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist. 
	 */
	public zulaessig : List<SchulformSchulgliederung> = new Vector();

	/**
	 * Gibt an, in welchem Schuljahr die Kursart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Kursart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. 
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
	public constructor(id : number, kuerzel : string, nummer : string, bezeichnung : string, bemerkungen : string | null, kuerzelAllg : string | null, bezeichnungAllg : string | null, erlaubtGOSt : boolean, zulaessig : List<Pair<Schulform, Schulgliederung | null>>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : string, __param4? : null | string, __param5? : null | string, __param6? : null | string, __param7? : boolean, __param8? : List<Pair<Schulform, Schulgliederung | null>>, __param9? : null | number, __param10? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string") || (__param4 === null)) && ((typeof __param5 !== "undefined") && (typeof __param5 === "string") || (__param5 === null)) && ((typeof __param6 !== "undefined") && (typeof __param6 === "string") || (__param6 === null)) && ((typeof __param7 !== "undefined") && typeof __param7 === "boolean") && ((typeof __param8 !== "undefined") && ((__param8 instanceof JavaObject) && (__param8.isTranspiledInstanceOf('java.util.List'))) || (__param8 === null)) && ((typeof __param9 !== "undefined") && (typeof __param9 === "number") || (__param9 === null)) && ((typeof __param10 !== "undefined") && (typeof __param10 === "number") || (__param10 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let nummer : string = __param2;
			let bezeichnung : string = __param3;
			let bemerkungen : string | null = __param4;
			let kuerzelAllg : string | null = __param5;
			let bezeichnungAllg : string | null = __param6;
			let erlaubtGOSt : boolean = __param7 as boolean;
			let zulaessig : List<Pair<Schulform, Schulgliederung | null>> = cast_java_util_List(__param8);
			let gueltigVon : number | null = __param9;
			let gueltigBis : number | null = __param10;
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
		result.kuerzel = obj.kuerzel;
		if (typeof obj.nummer === "undefined")
			 throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		result.kuerzelAllg = typeof obj.kuerzelAllg === "undefined" ? null : obj.kuerzelAllg === null ? null : obj.kuerzelAllg;
		result.bezeichnungAllg = typeof obj.bezeichnungAllg === "undefined" ? null : obj.bezeichnungAllg === null ? null : obj.bezeichnungAllg;
		if (typeof obj.erlaubtGOSt === "undefined")
			 throw new Error('invalid json format, missing attribute erlaubtGOSt');
		result.erlaubtGOSt = obj.erlaubtGOSt;
		if (!!obj.zulaessig) {
			for (let elem of obj.zulaessig) {
				result.zulaessig?.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KursartKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"nummer" : ' + '"' + obj.nummer! + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen + '"') + ',';
		result += '"kuerzelAllg" : ' + ((!obj.kuerzelAllg) ? 'null' : '"' + obj.kuerzelAllg + '"') + ',';
		result += '"bezeichnungAllg" : ' + ((!obj.bezeichnungAllg) ? 'null' : '"' + obj.bezeichnungAllg + '"') + ',';
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
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + '"' + obj.nummer + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen + '"') + ',';
		}
		if (typeof obj.kuerzelAllg !== "undefined") {
			result += '"kuerzelAllg" : ' + ((!obj.kuerzelAllg) ? 'null' : '"' + obj.kuerzelAllg + '"') + ',';
		}
		if (typeof obj.bezeichnungAllg !== "undefined") {
			result += '"bezeichnungAllg" : ' + ((!obj.bezeichnungAllg) ? 'null' : '"' + obj.bezeichnungAllg + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kurse_KursartKatalogEintrag(obj : unknown) : KursartKatalogEintrag {
	return obj as KursartKatalogEintrag;
}
