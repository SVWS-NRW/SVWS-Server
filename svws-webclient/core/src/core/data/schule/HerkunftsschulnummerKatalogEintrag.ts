import { JavaObject } from '../../../java/lang/JavaObject';
import { SchulformSchulgliederung } from '../../../core/data/schule/SchulformSchulgliederung';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Pair } from '../../../core/adt/Pair';

export class HerkunftsschulnummerKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Herkunfts-Schulnummer
	 */
	public schulnummer : number = -1;

	/**
	 * Die Bezeichnung der Herkunfts-Schulnummer
	 */
	public bezeichnung : string = "";

	/**
	 * Die Informationen zu Schulformen und -gliederungen, wo die Herkunfts-Schulnummer zulässig ist.
	 */
	public zulaessig : List<SchulformSchulgliederung> = new ArrayList<SchulformSchulgliederung>();

	/**
	 * Gibt an, in welchem Schuljahr die Herkunfts-Schulnummer einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Herkunfts-Schulnummer gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
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
	 * @param schulnummer        die Nummer der Herkunfts-Schulnummer
	 * @param bezeichnung        die Bezeichnung der Kursart
	 * @param zulaessig          die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und
	 *                           "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, schulnummer : number, bezeichnung : string, zulaessig : List<Pair<Schulform, Schulgliederung | null>> | null, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : string, __param3? : List<Pair<Schulform, Schulgliederung | null>> | null, __param4? : null | number, __param5? : null | number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && ((__param3 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && ((__param4 !== undefined) && (typeof __param4 === "number") || (__param4 === null)) && ((__param5 !== undefined) && (typeof __param5 === "number") || (__param5 === null))) {
			const id : number = __param0 as number;
			const schulnummer : number = __param1 as number;
			const bezeichnung : string = __param2;
			const zulaessig : List<Pair<Schulform, Schulgliederung | null>> | null = cast_java_util_List(__param3);
			const gueltigVon : number | null = __param4;
			const gueltigBis : number | null = __param5;
			this.id = id;
			this.schulnummer = schulnummer;
			this.bezeichnung = bezeichnung;
			if (zulaessig !== null) {
				for (const zul of zulaessig) {
					const sfsgl : SchulformSchulgliederung | null = new SchulformSchulgliederung();
					const sf : Schulform = zul.a;
					if (sf.daten === null)
						continue;
					sfsgl.schulform = sf.daten.kuerzel;
					const sgl : Schulgliederung | null = zul.b;
					sfsgl.gliederung = ((sgl === null) || (sgl.daten === null)) ? null : sgl.daten.kuerzel;
					this.zulaessig.add(sfsgl);
				}
			}
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): HerkunftsschulnummerKatalogEintrag {
		const obj = JSON.parse(json) as Partial<HerkunftsschulnummerKatalogEintrag>;
		const result = new HerkunftsschulnummerKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : HerkunftsschulnummerKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformSchulgliederung.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftsschulnummerKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.zulaessig !== undefined) {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
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

export function cast_de_svws_nrw_core_data_schule_HerkunftsschulnummerKatalogEintrag(obj : unknown) : HerkunftsschulnummerKatalogEintrag {
	return obj as HerkunftsschulnummerKatalogEintrag;
}
