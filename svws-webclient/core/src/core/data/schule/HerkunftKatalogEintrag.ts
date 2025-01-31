import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class HerkunftKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das der Herkunft eines Schülers, welches im Rahmen der amtlichen Schulstatistik verwendet wird
	 */
	public kuerzel : string = "";

	/**
	 * Die Kürzel der Schulformen, bei welchen die Herkunft des Schülers vorkommen kann.
	 */
	public schulformen : List<string> = new ArrayList<string>();

	/**
	 * Die textuelle Beschreibung der Herkunft.
	 */
	public beschreibung : string = "";

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
	 * @param id              die ID
	 * @param kuerzel         das Kürzel der Herkunft
	 * @param schulformen     die Schulformen, für welche die Herkunft zulässig ist
	 * @param beschreibung    die textuelle Beschreibung der Herkunft
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, schulformen : List<string>, beschreibung : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : List<string>, __param3? : string, __param4? : null | number, __param5? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((__param3 !== undefined) && (typeof __param3 === "string")) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null)) && ((__param5 !== undefined) && (typeof __param5 === "number") || (__param5 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const schulformen : List<string> = cast_java_util_List(__param2);
			const beschreibung : string = __param3;
			const gueltigVon : number | null = __param4;
			const gueltigBis : number | null = __param5;
			this.id = id;
			this.kuerzel = kuerzel;
			this.schulformen = schulformen;
			this.beschreibung = beschreibung;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.HerkunftKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.HerkunftKatalogEintrag'].includes(name);
	}

	public static class = new Class<HerkunftKatalogEintrag>('de.svws_nrw.core.data.schule.HerkunftKatalogEintrag');

	public static transpilerFromJSON(json : string): HerkunftKatalogEintrag {
		const obj = JSON.parse(json) as Partial<HerkunftKatalogEintrag>;
		const result = new HerkunftKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : HerkunftKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"schulformen" : [ ';
		for (let i = 0; i < obj.schulformen.size(); i++) {
			const elem = obj.schulformen.get(i);
			result += '"' + elem + '"';
			if (i < obj.schulformen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.schulformen !== undefined) {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_HerkunftKatalogEintrag(obj : unknown) : HerkunftKatalogEintrag {
	return obj as HerkunftKatalogEintrag;
}
