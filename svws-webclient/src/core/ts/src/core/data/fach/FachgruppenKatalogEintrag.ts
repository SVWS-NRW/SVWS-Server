import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { List, cast_java_util_List } from '../../../java/util/List';
import { RGBFarbe, cast_de_svws_nrw_core_data_RGBFarbe } from '../../../core/data/RGBFarbe';

export class FachgruppenKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Nummer für den Fachbereich, sofern festgelegt, ansonsten null.
	 */
	public nummer : number | null = null;

	/**
	 * Die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde.
	 */
	public idSchild : number | null = null;

	/**
	 * Die Bezeichnung der Fachgruppe
	 */
	public bezeichnung : string = "";

	/**
	 * Das Kürzel der Fachgruppe
	 */
	public kuerzel : string = "";

	/**
	 * Die Farbe, welche der Fachgruppe zugeordnet wurde
	 */
	public farbe : RGBFarbe = new RGBFarbe();

	/**
	 * Die Kürzel der Schulformen, bei welchen die Fachgruppe vorkommt.
	 */
	public schulformen : List<string> = new ArrayList();

	/**
	 * Ein Zahlwert, welche eine Sortier-Reihenfolge der Fachgruppen angibt (aus Schild 2.x).
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht
	 */
	public fuerZeugnis : boolean = false;

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


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
	public constructor(id : number, nummer : number | null, idSchild : number | null, bezeichnung : string, kuerzel : string, farbe : RGBFarbe, schulformen : List<Schulform>, sortierung : number, fuerZeugnis : boolean, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : null | number, __param2? : null | number, __param3? : string, __param4? : string, __param5? : RGBFarbe, __param6? : List<Schulform>, __param7? : number, __param8? : boolean, __param9? : null | number, __param10? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "number") || (__param1 === null)) && ((typeof __param2 !== "undefined") && (typeof __param2 === "number") || (__param2 === null)) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string")) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.data.RGBFarbe')))) && ((typeof __param6 !== "undefined") && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && ((typeof __param7 !== "undefined") && (typeof __param7 === "number")) && ((typeof __param8 !== "undefined") && typeof __param8 === "boolean") && ((typeof __param9 !== "undefined") && (typeof __param9 === "number") || (__param9 === null)) && ((typeof __param10 !== "undefined") && (typeof __param10 === "number") || (__param10 === null))) {
			const id : number = __param0 as number;
			const nummer : number | null = __param1;
			const idSchild : number | null = __param2;
			const bezeichnung : string = __param3;
			const kuerzel : string = __param4;
			const farbe : RGBFarbe = cast_de_svws_nrw_core_data_RGBFarbe(__param5);
			const schulformen : List<Schulform> = cast_java_util_List(__param6);
			const sortierung : number = __param7;
			const fuerZeugnis : boolean = __param8 as boolean;
			const gueltigVon : number | null = __param9;
			const gueltigBis : number | null = __param10;
			this.id = id;
			this.nummer = nummer;
			this.idSchild = idSchild;
			this.bezeichnung = bezeichnung;
			this.kuerzel = kuerzel;
			this.farbe = farbe;
			for (const schulform of schulformen)
				this.schulformen.add(schulform.daten.kuerzel);
			this.sortierung = sortierung;
			this.fuerZeugnis = fuerZeugnis;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.fach.FachgruppenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FachgruppenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new FachgruppenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.nummer = typeof obj.nummer === "undefined" ? null : obj.nummer === null ? null : obj.nummer;
		result.idSchild = typeof obj.idSchild === "undefined" ? null : obj.idSchild === null ? null : obj.idSchild;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.farbe === "undefined")
			 throw new Error('invalid json format, missing attribute farbe');
		result.farbe = RGBFarbe.transpilerFromJSON(JSON.stringify(obj.farbe));
		if ((obj.schulformen !== undefined) && (obj.schulformen !== null)) {
			for (const elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.fuerZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute fuerZeugnis');
		result.fuerZeugnis = obj.fuerZeugnis;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : FachgruppenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer) + ',';
		result += '"idSchild" : ' + ((!obj.idSchild) ? 'null' : obj.idSchild) + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
		if (!obj.schulformen) {
			result += '"schulformen" : []';
		} else {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"sortierung" : ' + obj.sortierung! + ',';
		result += '"fuerZeugnis" : ' + obj.fuerZeugnis + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"nummer" : ' + ((!obj.nummer) ? 'null' : obj.nummer) + ',';
		}
		if (typeof obj.idSchild !== "undefined") {
			result += '"idSchild" : ' + ((!obj.idSchild) ? 'null' : obj.idSchild) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.farbe !== "undefined") {
			result += '"farbe" : ' + RGBFarbe.transpilerToJSON(obj.farbe) + ',';
		}
		if (typeof obj.schulformen !== "undefined") {
			if (!obj.schulformen) {
				result += '"schulformen" : []';
			} else {
				result += '"schulformen" : [ ';
				for (let i = 0; i < obj.schulformen.size(); i++) {
					const elem = obj.schulformen.get(i);
					result += '"' + elem + '"';
					if (i < obj.schulformen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.fuerZeugnis !== "undefined") {
			result += '"fuerZeugnis" : ' + obj.fuerZeugnis + ',';
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

export function cast_de_svws_nrw_core_data_fach_FachgruppenKatalogEintrag(obj : unknown) : FachgruppenKatalogEintrag {
	return obj as FachgruppenKatalogEintrag;
}
