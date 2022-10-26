import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { RGBFarbe, cast_de_nrw_schule_svws_core_data_RGBFarbe } from '../../../core/data/RGBFarbe';

export class FachgruppenKatalogEintrag extends JavaObject {

	public id : number = -1;

	public nummer : Number | null = null;

	public idSchild : Number | null = null;

	public bezeichnung : String = "";

	public kuerzel : String = "";

	public farbe : RGBFarbe = new RGBFarbe();

	public schulformen : List<String> = new Vector();

	public sortierung : Number = 0;

	public fuerZeugnis : boolean = false;

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
	 * @param nummer        die Nummer für den Fachbereich, sofern festgelegt, ansonsten null
	 * @param idSchild      die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde
	 * @param bezeichnung   die Bezeichnung der Fachgruppe
	 * @param kuerzel       das Kürzel der Fachgruppe 
	 * @param farbe         die Farbe, welche der Fachgruppe zugeordnet wurde
	 * @param schulformen   die Kürzel der Schulformen, bei welchen die Fachgruppe vorkommt
	 * @param sortierung    ein Zahlwert, welche eine Sortier-Reihenfolge der Fachgruppen angibt (aus Schild 2.x)
	 * @param fuerZeugnis   gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht 
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                      "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, nummer : Number | null, idSchild : Number | null, bezeichnung : String, kuerzel : String, farbe : RGBFarbe, schulformen : List<Schulform>, sortierung : Number, fuerZeugnis : boolean, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : Number | null, __param2? : Number | null, __param3? : String, __param4? : String, __param5? : RGBFarbe, __param6? : List<Schulform>, __param7? : Number, __param8? : boolean, __param9? : Number | null, __param10? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof Number) || (typeof __param1 === "number")) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof Number) || (typeof __param2 === "number")) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof String) || (typeof __param4 === "string"))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.RGBFarbe')))) && ((typeof __param6 !== "undefined") && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && ((typeof __param7 !== "undefined") && ((__param7 instanceof Number) || (typeof __param7 === "number"))) && ((typeof __param8 !== "undefined") && typeof __param8 === "boolean") && ((typeof __param9 !== "undefined") && ((__param9 instanceof Number) || (typeof __param9 === "number")) || (__param9 === null)) && ((typeof __param10 !== "undefined") && ((__param10 instanceof Number) || (typeof __param10 === "number")) || (__param10 === null))) {
			let id : number = __param0 as number;
			let nummer : Number | null = cast_java_lang_Integer(__param1);
			let idSchild : Number | null = cast_java_lang_Integer(__param2);
			let bezeichnung : String = __param3;
			let kuerzel : String = __param4;
			let farbe : RGBFarbe = cast_de_nrw_schule_svws_core_data_RGBFarbe(__param5);
			let schulformen : List<Schulform> = cast_java_util_List(__param6);
			let sortierung : Number = cast_java_lang_Integer(__param7);
			let fuerZeugnis : boolean = __param8 as boolean;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param9);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param10);
			this.id = id;
			this.nummer = nummer;
			this.idSchild = idSchild;
			this.bezeichnung = bezeichnung;
			this.kuerzel = kuerzel;
			this.farbe = farbe;
			for (let schulform of schulformen) 
				this.schulformen.add(schulform.daten.kuerzel);
			this.sortierung = sortierung;
			this.fuerZeugnis = fuerZeugnis;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.fach.FachgruppenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FachgruppenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new FachgruppenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.nummer = typeof obj.nummer === "undefined" ? null : obj.nummer;
		result.idSchild = typeof obj.idSchild === "undefined" ? null : obj.idSchild;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.farbe === "undefined")
			 throw new Error('invalid json format, missing attribute farbe');
		result.farbe = RGBFarbe.transpilerFromJSON(JSON.stringify(obj.farbe));
		if (!!obj.schulformen) {
			for (let elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.fuerZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute fuerZeugnis');
		result.fuerZeugnis = obj.fuerZeugnis;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : FachgruppenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer.valueOf()) + ',';
		result += '"idSchild" : ' + ((!obj.idSchild) ? 'null' : obj.idSchild.valueOf()) + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
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
		result += '"sortierung" : ' + obj.sortierung.valueOf() + ',';
		result += '"fuerZeugnis" : ' + obj.fuerZeugnis + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachgruppenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer.valueOf()) + ',';
		}
		if (typeof obj.idSchild !== "undefined") {
			result += '"idSchild" : ' + ((!obj.idSchild) ? 'null' : obj.idSchild.valueOf()) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.farbe !== "undefined") {
			result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
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
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung.valueOf() + ',';
		}
		if (typeof obj.fuerZeugnis !== "undefined") {
			result += '"fuerZeugnis" : ' + obj.fuerZeugnis + ',';
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

export function cast_de_nrw_schule_svws_core_data_fach_FachgruppenKatalogEintrag(obj : unknown) : FachgruppenKatalogEintrag {
	return obj as FachgruppenKatalogEintrag;
}
