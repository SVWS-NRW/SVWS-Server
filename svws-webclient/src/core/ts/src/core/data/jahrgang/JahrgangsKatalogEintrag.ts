import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JahrgangsKatalogEintragBezeichnung } from '../../../core/data/jahrgang/JahrgangsKatalogEintragBezeichnung';

export class JahrgangsKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das 2-stellige Jahrgangskürzel
	 */
	public kuerzel : string = "";

	/**
	 * Die Jahrgangsbezeichungen bei den zulässigen Schulformen.
	 */
	public bezeichnungen : List<JahrgangsKatalogEintragBezeichnung> = new ArrayList();

	/**
	 * Gibt an, in welchem Schuljahr der Jahrgang ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Jahrgang verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID des Katalog-Eintrags
	 * @param kuerzel         das zweistellige Kürzel des Jahrgangs
	 * @param bezeichnungen   die Jahrgangsbezeichungen bei den zulässigen Schulformen
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, bezeichnungen : List<JahrgangsKatalogEintragBezeichnung>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : List<JahrgangsKatalogEintragBezeichnung>, __param3? : null | number, __param4? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && (typeof __param3 === "number") || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const bezeichnungen : List<JahrgangsKatalogEintragBezeichnung> = cast_java_util_List(__param2);
			const gueltigVon : number | null = __param3;
			const gueltigBis : number | null = __param4;
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnungen = bezeichnungen;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.jahrgang.JahrgangsKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): JahrgangsKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new JahrgangsKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if ((obj.bezeichnungen !== undefined) && (obj.bezeichnungen !== null)) {
			for (const elem of obj.bezeichnungen) {
				result.bezeichnungen?.add(JahrgangsKatalogEintragBezeichnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		if (!obj.bezeichnungen) {
			result += '"bezeichnungen" : []';
		} else {
			result += '"bezeichnungen" : [ ';
			for (let i = 0; i < obj.bezeichnungen.size(); i++) {
				const elem = obj.bezeichnungen.get(i);
				result += JahrgangsKatalogEintragBezeichnung.transpilerToJSON(elem);
				if (i < obj.bezeichnungen.size() - 1)
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

	public static transpilerToJSONPatch(obj : Partial<JahrgangsKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.bezeichnungen !== "undefined") {
			if (!obj.bezeichnungen) {
				result += '"bezeichnungen" : []';
			} else {
				result += '"bezeichnungen" : [ ';
				for (let i = 0; i < obj.bezeichnungen.size(); i++) {
					const elem = obj.bezeichnungen.get(i);
					result += JahrgangsKatalogEintragBezeichnung.transpilerToJSON(elem);
					if (i < obj.bezeichnungen.size() - 1)
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

export function cast_de_svws_nrw_core_data_jahrgang_JahrgangsKatalogEintrag(obj : unknown) : JahrgangsKatalogEintrag {
	return obj as JahrgangsKatalogEintrag;
}
