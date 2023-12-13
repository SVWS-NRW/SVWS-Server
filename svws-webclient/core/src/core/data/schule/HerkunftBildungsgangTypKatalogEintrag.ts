import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegBildungsgangTyp, cast_de_svws_nrw_core_types_schule_BerufskollegBildungsgangTyp } from '../../../core/types/schule/BerufskollegBildungsgangTyp';
import { Schulform } from '../../../core/types/schule/Schulform';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { WeiterbildungskollegBildungsgangTyp, cast_de_svws_nrw_core_types_schule_WeiterbildungskollegBildungsgangTyp } from '../../../core/types/schule/WeiterbildungskollegBildungsgangTyp';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class HerkunftBildungsgangTypKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Bildungsgangestyps, welches im Rahmen der amtlichen Schulstatistik verwendet wird
	 */
	public kuerzel : string = "";

	/**
	 * Die Kürzel der Schulformen, bei welchen der Bildungsgangstyp als Herkunft vorkommen kann (WB oder BK und SB).
	 */
	public schulformen : List<string> = new ArrayList();

	/**
	 * Die textuelle Beschreibung des Bildungsgangtyps.
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
	 * @param id            die ID
	 * @param wbkTyp        der Bildungsgangtyp am Weiterbildungskolleg oder null
	 * @param bkTyp         der Bildungsgangtyp am BErufskolleg oder null
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, wbkTyp : WeiterbildungskollegBildungsgangTyp | null, bkTyp : BerufskollegBildungsgangTyp | null, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : WeiterbildungskollegBildungsgangTyp | null, __param2? : BerufskollegBildungsgangTyp | null, __param3? : null | number, __param4? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.WeiterbildungskollegBildungsgangTyp'))) || (__param1 === null)) && ((typeof __param2 !== "undefined") && ((__param2 instanceof JavaObject) && ((__param2 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp'))) || (__param2 === null)) && ((typeof __param3 !== "undefined") && (typeof __param3 === "number") || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null))) {
			const id : number = __param0 as number;
			const wbkTyp : WeiterbildungskollegBildungsgangTyp | null = cast_de_svws_nrw_core_types_schule_WeiterbildungskollegBildungsgangTyp(__param1);
			const bkTyp : BerufskollegBildungsgangTyp | null = cast_de_svws_nrw_core_types_schule_BerufskollegBildungsgangTyp(__param2);
			const gueltigVon : number | null = __param3;
			const gueltigBis : number | null = __param4;
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.HerkunftBildungsgangTypKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.HerkunftBildungsgangTypKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): HerkunftBildungsgangTypKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new HerkunftBildungsgangTypKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if ((obj.schulformen !== undefined) && (obj.schulformen !== null)) {
			for (const elem of obj.schulformen) {
				result.schulformen?.add(elem);
			}
		}
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : HerkunftBildungsgangTypKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
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
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
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
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
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

export function cast_de_svws_nrw_core_data_schule_HerkunftBildungsgangTypKatalogEintrag(obj : unknown) : HerkunftBildungsgangTypKatalogEintrag {
	return obj as HerkunftBildungsgangTypKatalogEintrag;
}
