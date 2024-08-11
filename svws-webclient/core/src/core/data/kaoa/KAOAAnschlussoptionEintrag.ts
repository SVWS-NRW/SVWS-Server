import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulstufe } from '../../../core/types/schule/Schulstufe';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class KAOAAnschlussoptionEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Anschlussoption.
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung der Anschlussoption.
	 */
	public beschreibung : string = "";

	/**
	 * Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)
	 */
	public stufen : List<string> = new ArrayList<string>();

	/**
	 * Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden
	 */
	public anzeigeZusatzmerkmal : List<string> = new ArrayList<string>();

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen KAoA-Anschlussoption-Eintrag mit den angegebenen Werten
	 *
	 * @param id             die ID
	 * @param kuerzel        das Kürzel
	 * @param beschreibung   die Beschreibung
	 * @param stufen         die Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)
	 * @param anzeigeZusatzmerkmal
	 *                       gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, beschreibung : string, stufen : List<Schulstufe>, anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : List<Schulstufe>, __param4? : List<KAOAZusatzmerkmal>, __param5? : null | number, __param6? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && ((__param4 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param4 === null)) && ((__param5 !== undefined) && (typeof __param5 === "number") || (__param5 === null)) && ((__param6 !== undefined) && (typeof __param6 === "number") || (__param6 === null))) {
			const id : number = __param0 as number;
			const kuerzel : string = __param1;
			const beschreibung : string = __param2;
			const stufen : List<Schulstufe> = cast_java_util_List(__param3);
			const anzeigeZusatzmerkmal : List<KAOAZusatzmerkmal> = cast_java_util_List(__param4);
			const gueltigVon : number | null = __param5;
			const gueltigBis : number | null = __param6;
			this.id = id;
			this.kuerzel = kuerzel;
			this.beschreibung = beschreibung;
			for (const stufe of stufen)
				this.stufen.add(stufe.daten.kuerzel);
			for (const m of anzeigeZusatzmerkmal)
				this.anzeigeZusatzmerkmal.add(m.daten.kuerzel);
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kaoa.KAOAAnschlussoptionEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kaoa.KAOAAnschlussoptionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KAOAAnschlussoptionEintrag {
		const obj = JSON.parse(json) as Partial<KAOAAnschlussoptionEintrag>;
		const result = new KAOAAnschlussoptionEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.stufen !== undefined) {
			for (const elem of obj.stufen) {
				result.stufen.add(elem);
			}
		}
		if (obj.anzeigeZusatzmerkmal !== undefined) {
			for (const elem of obj.anzeigeZusatzmerkmal) {
				result.anzeigeZusatzmerkmal.add(elem);
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KAOAAnschlussoptionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"stufen" : [ ';
		for (let i = 0; i < obj.stufen.size(); i++) {
			const elem = obj.stufen.get(i);
			result += '"' + elem + '"';
			if (i < obj.stufen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anzeigeZusatzmerkmal" : [ ';
		for (let i = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
			const elem = obj.anzeigeZusatzmerkmal.get(i);
			result += '"' + elem + '"';
			if (i < obj.anzeigeZusatzmerkmal.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAAnschlussoptionEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.stufen !== undefined) {
			result += '"stufen" : [ ';
			for (let i = 0; i < obj.stufen.size(); i++) {
				const elem = obj.stufen.get(i);
				result += '"' + elem + '"';
				if (i < obj.stufen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anzeigeZusatzmerkmal !== undefined) {
			result += '"anzeigeZusatzmerkmal" : [ ';
			for (let i = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
				const elem = obj.anzeigeZusatzmerkmal.get(i);
				result += '"' + elem + '"';
				if (i < obj.anzeigeZusatzmerkmal.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kaoa_KAOAAnschlussoptionEintrag(obj : unknown) : KAOAAnschlussoptionEintrag {
	return obj as KAOAAnschlussoptionEintrag;
}
