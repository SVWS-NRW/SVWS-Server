import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { BerufskollegAnlage, cast_de_nrw_schule_svws_core_types_schule_BerufskollegAnlage } from '../../../core/types/schule/BerufskollegAnlage';
import { List, cast_java_util_List } from '../../../java/util/List';
import { SchulabschlussBerufsbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussBerufsbildend } from '../../../core/types/schule/SchulabschlussBerufsbildend';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchulgliederungKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel der Schulgliederung, welches im Rahmen der amtlichen Schulstatistik verwendet wird
	 */
	public kuerzel : string = "";

	/**
	 * Gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public istBK : boolean = false;

	/**
	 * Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt.
	 */
	public schulformen : List<string> = new Vector();

	/**
	 * Gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt.
	 */
	public istAuslaufend : boolean = false;

	/**
	 * Gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt.
	 */
	public istAusgelaufen : boolean = false;

	/**
	 * Die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges.
	 */
	public beschreibung : string = "";

	/**
	 * Die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkAnlage : string | null = null;

	/**
	 * Der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkTyp : string | null = null;

	/**
	 * Der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein.
	 */
	public bkIndex : number | null = null;

	/**
	 * Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht
	 */
	public istVZ : boolean = false;

	/**
	 * Gibt eine Liste von berufsbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkAbschlussBerufsbildend : List<string> = new Vector();

	/**
	 * Gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt.
	 */
	public bkAbschlussAllgemeinbildend : List<string> = new Vector();

	/**
	 * Gibt an, in welchem Schuljahr die Schulgliederung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Schulgliederung-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Schulgliederung-Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel 
	 * @param istBK           gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param schulformen     die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt
	 * @param istAuslaufend   gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt
	 * @param istAusgelaufen  gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt
	 * @param beschreibung    die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges
	 * @param bkAnlage        die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param bkTyp           der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param bkIndex         der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei 
	 *                        unterschiedlichen Gliederungen identisch sein
	 * @param istVZ           gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht
	 * @param bkAbschlussBerufsbildend
	 *                        gibt eine Liste von berufsbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, 
	 *                        wenn es sich um einen Bildungsgang am Berufskolleg handelt 
	 * @param bkAbschlussAllgemeinbildend
	 *                        gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, 
	 *                        wenn es sich um einen Bildungsgang am Berufskolleg handelt 
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : string, istBK : boolean, schulformen : List<Schulform>, istAuslaufend : boolean, istAusgelaufen : boolean, beschreibung : string, bkAnlage : BerufskollegAnlage | null, bkTyp : string | null, bkIndex : number | null, istVZ : boolean, bkAbschlussBerufsbildend : List<SchulabschlussBerufsbildend> | null, bkAbschlussAllgemeinbildend : List<SchulabschlussAllgemeinbildend> | null, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : boolean, __param3? : List<Schulform>, __param4? : boolean, __param5? : boolean, __param6? : string, __param7? : BerufskollegAnlage | null, __param8? : null | string, __param9? : null | number, __param10? : boolean, __param11? : List<SchulabschlussBerufsbildend> | null, __param12? : List<SchulabschlussAllgemeinbildend> | null, __param13? : null | number, __param14? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined") && (typeof __param11 === "undefined") && (typeof __param12 === "undefined") && (typeof __param13 === "undefined") && (typeof __param14 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && typeof __param2 === "boolean") && ((typeof __param3 !== "undefined") && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((typeof __param4 !== "undefined") && typeof __param4 === "boolean") && ((typeof __param5 !== "undefined") && typeof __param5 === "boolean") && ((typeof __param6 !== "undefined") && (typeof __param6 === "string")) && ((typeof __param7 !== "undefined") && ((__param7 instanceof JavaObject) && (__param7.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schule.BerufskollegAnlage'))) || (__param7 === null)) && ((typeof __param8 !== "undefined") && (typeof __param8 === "string") || (__param8 === null)) && ((typeof __param9 !== "undefined") && (typeof __param9 === "number") || (__param9 === null)) && ((typeof __param10 !== "undefined") && typeof __param10 === "boolean") && ((typeof __param11 !== "undefined") && ((__param11 instanceof JavaObject) && (__param11.isTranspiledInstanceOf('java.util.List'))) || (__param11 === null)) && ((typeof __param12 !== "undefined") && ((__param12 instanceof JavaObject) && (__param12.isTranspiledInstanceOf('java.util.List'))) || (__param12 === null)) && ((typeof __param13 !== "undefined") && (typeof __param13 === "number") || (__param13 === null)) && ((typeof __param14 !== "undefined") && (typeof __param14 === "number") || (__param14 === null))) {
			let id : number = __param0 as number;
			let kuerzel : string = __param1;
			let istBK : boolean = __param2 as boolean;
			let schulformen : List<Schulform> = cast_java_util_List(__param3);
			let istAuslaufend : boolean = __param4 as boolean;
			let istAusgelaufen : boolean = __param5 as boolean;
			let beschreibung : string = __param6;
			let bkAnlage : BerufskollegAnlage | null = cast_de_nrw_schule_svws_core_types_schule_BerufskollegAnlage(__param7);
			let bkTyp : string | null = __param8;
			let bkIndex : number | null = __param9;
			let istVZ : boolean = __param10 as boolean;
			let bkAbschlussBerufsbildend : List<SchulabschlussBerufsbildend> | null = cast_java_util_List(__param11);
			let bkAbschlussAllgemeinbildend : List<SchulabschlussAllgemeinbildend> | null = cast_java_util_List(__param12);
			let gueltigVon : number | null = __param13;
			let gueltigBis : number | null = __param14;
			this.id = id;
			this.kuerzel = kuerzel;
			this.istBK = istBK;
			for (let schulform of schulformen) 
				this.schulformen.add(schulform.daten.kuerzel);
			this.istAuslaufend = istAuslaufend;
			this.istAusgelaufen = istAusgelaufen;
			this.beschreibung = beschreibung;
			this.bkAnlage = (bkAnlage === null) ? null : bkAnlage.daten.kuerzel;
			this.bkTyp = bkTyp;
			this.bkIndex = bkIndex;
			this.istVZ = istVZ;
			if (bkAbschlussBerufsbildend !== null) 
				for (let sbb of bkAbschlussBerufsbildend) 
					this.bkAbschlussBerufsbildend.add(sbb.daten.kuerzel);
			if (bkAbschlussAllgemeinbildend !== null) 
				for (let sab of bkAbschlussAllgemeinbildend) 
					this.bkAbschlussAllgemeinbildend.add(sab.daten.kuerzel);
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchulgliederungKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulgliederungKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchulgliederungKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.istBK === "undefined")
			 throw new Error('invalid json format, missing attribute istBK');
		result.istBK = obj.istBK;
		if (!!obj.schulformen) {
			for (let elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		if (typeof obj.istAuslaufend === "undefined")
			 throw new Error('invalid json format, missing attribute istAuslaufend');
		result.istAuslaufend = obj.istAuslaufend;
		if (typeof obj.istAusgelaufen === "undefined")
			 throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.bkAnlage = typeof obj.bkAnlage === "undefined" ? null : obj.bkAnlage === null ? null : obj.bkAnlage;
		result.bkTyp = typeof obj.bkTyp === "undefined" ? null : obj.bkTyp === null ? null : obj.bkTyp;
		result.bkIndex = typeof obj.bkIndex === "undefined" ? null : obj.bkIndex === null ? null : obj.bkIndex;
		if (typeof obj.istVZ === "undefined")
			 throw new Error('invalid json format, missing attribute istVZ');
		result.istVZ = obj.istVZ;
		if (!!obj.bkAbschlussBerufsbildend) {
			for (let elem of obj.bkAbschlussBerufsbildend) {
				result.bkAbschlussBerufsbildend?.add(elem);
			}
		}
		if (!!obj.bkAbschlussAllgemeinbildend) {
			for (let elem of obj.bkAbschlussAllgemeinbildend) {
				result.bkAbschlussAllgemeinbildend?.add(elem);
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchulgliederungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"istBK" : ' + obj.istBK + ',';
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
		result += '"istAuslaufend" : ' + obj.istAuslaufend + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result += '"bkAnlage" : ' + ((!obj.bkAnlage) ? 'null' : '"' + obj.bkAnlage + '"') + ',';
		result += '"bkTyp" : ' + ((!obj.bkTyp) ? 'null' : '"' + obj.bkTyp + '"') + ',';
		result += '"bkIndex" : ' + ((!obj.bkIndex) ? 'null' : obj.bkIndex) + ',';
		result += '"istVZ" : ' + obj.istVZ + ',';
		if (!obj.bkAbschlussBerufsbildend) {
			result += '"bkAbschlussBerufsbildend" : []';
		} else {
			result += '"bkAbschlussBerufsbildend" : [ ';
			for (let i : number = 0; i < obj.bkAbschlussBerufsbildend.size(); i++) {
				let elem = obj.bkAbschlussBerufsbildend.get(i);
				result += '"' + elem + '"';
				if (i < obj.bkAbschlussBerufsbildend.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.bkAbschlussAllgemeinbildend) {
			result += '"bkAbschlussAllgemeinbildend" : []';
		} else {
			result += '"bkAbschlussAllgemeinbildend" : [ ';
			for (let i : number = 0; i < obj.bkAbschlussAllgemeinbildend.size(); i++) {
				let elem = obj.bkAbschlussAllgemeinbildend.get(i);
				result += '"' + elem + '"';
				if (i < obj.bkAbschlussAllgemeinbildend.size() - 1)
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

	public static transpilerToJSONPatch(obj : Partial<SchulgliederungKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.istBK !== "undefined") {
			result += '"istBK" : ' + obj.istBK + ',';
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
		if (typeof obj.istAuslaufend !== "undefined") {
			result += '"istAuslaufend" : ' + obj.istAuslaufend + ',';
		}
		if (typeof obj.istAusgelaufen !== "undefined") {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		if (typeof obj.bkAnlage !== "undefined") {
			result += '"bkAnlage" : ' + ((!obj.bkAnlage) ? 'null' : '"' + obj.bkAnlage + '"') + ',';
		}
		if (typeof obj.bkTyp !== "undefined") {
			result += '"bkTyp" : ' + ((!obj.bkTyp) ? 'null' : '"' + obj.bkTyp + '"') + ',';
		}
		if (typeof obj.bkIndex !== "undefined") {
			result += '"bkIndex" : ' + ((!obj.bkIndex) ? 'null' : obj.bkIndex) + ',';
		}
		if (typeof obj.istVZ !== "undefined") {
			result += '"istVZ" : ' + obj.istVZ + ',';
		}
		if (typeof obj.bkAbschlussBerufsbildend !== "undefined") {
			if (!obj.bkAbschlussBerufsbildend) {
				result += '"bkAbschlussBerufsbildend" : []';
			} else {
				result += '"bkAbschlussBerufsbildend" : [ ';
				for (let i : number = 0; i < obj.bkAbschlussBerufsbildend.size(); i++) {
					let elem = obj.bkAbschlussBerufsbildend.get(i);
					result += '"' + elem + '"';
					if (i < obj.bkAbschlussBerufsbildend.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bkAbschlussAllgemeinbildend !== "undefined") {
			if (!obj.bkAbschlussAllgemeinbildend) {
				result += '"bkAbschlussAllgemeinbildend" : []';
			} else {
				result += '"bkAbschlussAllgemeinbildend" : [ ';
				for (let i : number = 0; i < obj.bkAbschlussAllgemeinbildend.size(); i++) {
					let elem = obj.bkAbschlussAllgemeinbildend.get(i);
					result += '"' + elem + '"';
					if (i < obj.bkAbschlussAllgemeinbildend.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_SchulgliederungKatalogEintrag(obj : unknown) : SchulgliederungKatalogEintrag {
	return obj as SchulgliederungKatalogEintrag;
}
