import { StundenplanRaum, cast_de_svws_nrw_core_data_stundenplan_StundenplanRaum } from '../../../../core/data/stundenplan/StundenplanRaum';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurraum, cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum } from '../../../../core/data/gost/klausurplanung/GostKlausurraum';

export class GostKlausurraumRich extends JavaObject {

	/**
	 * Die ID des Klausurtermins.
	 */
	public klausurraum : GostKlausurraum = new GostKlausurraum();

	/**
	 * Die Grösse des Raumes an Arbeitsplätzen für Schüler.
	 */
	public groesse : number = -1;

	/**
	 * Ein Array mit den IDs der im Raum enthaltenen Schülerklausurterminen.
	 */
	public schuelerklausurterminIDs : List<number> = new ArrayList<number>();


	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param klausurraum      Das zugehörige {@link GostKlausurraum}-Objekt.
	 * @param stundenplanraum  Das zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public constructor(klausurraum : GostKlausurraum, stundenplanraum : StundenplanRaum | null);

	/**
	 * Default-Konstruktor
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostKlausurraum, __param1? : StundenplanRaum | null) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanRaum'))) || (__param1 === null))) {
			const klausurraum : GostKlausurraum = cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum(__param0);
			const stundenplanraum : StundenplanRaum | null = cast_de_svws_nrw_core_data_stundenplan_StundenplanRaum(__param1);
			this.klausurraum = klausurraum;
			if (stundenplanraum !== null) {
				this.groesse = stundenplanraum.groesse;
			}
		} else if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else throw new Error('invalid method overload');
	}

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return (another !== null) && (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich')))) && (JavaObject.equalsTranspiler(this.klausurraum, ((cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumRich(another)).klausurraum)));
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this.klausurraum);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich'].includes(name);
	}

	public static class = new Class<GostKlausurraumRich>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich');

	public static transpilerFromJSON(json : string): GostKlausurraumRich {
		const obj = JSON.parse(json) as Partial<GostKlausurraumRich>;
		const result = new GostKlausurraumRich();
		if (obj.klausurraum === undefined)
			throw new Error('invalid json format, missing attribute klausurraum');
		result.klausurraum = GostKlausurraum.transpilerFromJSON(JSON.stringify(obj.klausurraum));
		if (obj.groesse === undefined)
			throw new Error('invalid json format, missing attribute groesse');
		result.groesse = obj.groesse;
		if (obj.schuelerklausurterminIDs !== undefined) {
			for (const elem of obj.schuelerklausurterminIDs) {
				result.schuelerklausurterminIDs.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumRich) : string {
		let result = '{';
		result += '"klausurraum" : ' + GostKlausurraum.transpilerToJSON(obj.klausurraum) + ',';
		result += '"groesse" : ' + obj.groesse.toString() + ',';
		result += '"schuelerklausurterminIDs" : [ ';
		for (let i = 0; i < obj.schuelerklausurterminIDs.size(); i++) {
			const elem = obj.schuelerklausurterminIDs.get(i);
			result += elem.toString();
			if (i < obj.schuelerklausurterminIDs.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumRich>) : string {
		let result = '{';
		if (obj.klausurraum !== undefined) {
			result += '"klausurraum" : ' + GostKlausurraum.transpilerToJSON(obj.klausurraum) + ',';
		}
		if (obj.groesse !== undefined) {
			result += '"groesse" : ' + obj.groesse.toString() + ',';
		}
		if (obj.schuelerklausurterminIDs !== undefined) {
			result += '"schuelerklausurterminIDs" : [ ';
			for (let i = 0; i < obj.schuelerklausurterminIDs.size(); i++) {
				const elem = obj.schuelerklausurterminIDs.get(i);
				result += elem.toString();
				if (i < obj.schuelerklausurterminIDs.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumRich(obj : unknown) : GostKlausurraumRich {
	return obj as GostKlausurraumRich;
}
