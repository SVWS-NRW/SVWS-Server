import { StundenplanRaum, cast_de_svws_nrw_core_data_stundenplan_StundenplanRaum } from '../../../../core/data/stundenplan/StundenplanRaum';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ArrayList } from '../../../../java/util/ArrayList';
import { JavaLong } from '../../../../java/lang/JavaLong';
import type { List } from '../../../../java/util/List';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurraum, cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum } from '../../../../core/data/gost/klausurplanung/GostKlausurraum';

export class GostKlausurraumRich extends JavaObject {

	/**
	 * Die ID des Klausurraums.
	 */
	public id : number = -1;

	/**
	 * Die Grösse des Raumes an Arbeitsplätzen für Schüler.
	 */
	public groesse : number = -1;

	/**
	 * Die zugeordneten {@link GostSchuelerklausurTerminRich}-Objekte.
	 */
	public readonly schuelerklausuren : List<GostSchuelerklausurTerminRich> = new ArrayList<GostSchuelerklausurTerminRich>();


	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param klausurraum      Das zugehörige {@link GostKlausurraum}-Objekt.
	 * @param stundenplanraum  Das zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public constructor(klausurraum : GostKlausurraum, stundenplanraum : StundenplanRaum);

	/**
	 * Konstruktor für Transpiler.
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostKlausurraum, __param1? : StundenplanRaum) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanRaum'))))) {
			const klausurraum : GostKlausurraum = cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraum(__param0);
			const stundenplanraum : StundenplanRaum = cast_de_svws_nrw_core_data_stundenplan_StundenplanRaum(__param1);
			this.id = klausurraum.id;
			this.groesse = stundenplanraum.groesse;
		} else if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
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
		return another !== null && ((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich'))) && this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumRich(another)).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurraumRich {
		const obj = JSON.parse(json);
		const result = new GostKlausurraumRich();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.groesse === "undefined")
			 throw new Error('invalid json format, missing attribute groesse');
		result.groesse = obj.groesse;
		if ((obj.schuelerklausuren !== undefined) && (obj.schuelerklausuren !== null)) {
			for (const elem of obj.schuelerklausuren) {
				result.schuelerklausuren?.add(GostSchuelerklausurTerminRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumRich) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"groesse" : ' + obj.groesse + ',';
		if (!obj.schuelerklausuren) {
			result += '"schuelerklausuren" : []';
		} else {
			result += '"schuelerklausuren" : [ ';
			for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
				const elem = obj.schuelerklausuren.get(i);
				result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
				if (i < obj.schuelerklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumRich>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.groesse !== "undefined") {
			result += '"groesse" : ' + obj.groesse + ',';
		}
		if (typeof obj.schuelerklausuren !== "undefined") {
			if (!obj.schuelerklausuren) {
				result += '"schuelerklausuren" : []';
			} else {
				result += '"schuelerklausuren" : [ ';
				for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
					const elem = obj.schuelerklausuren.get(i);
					result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
					if (i < obj.schuelerklausuren.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumRich(obj : unknown) : GostKlausurraumRich {
	return obj as GostKlausurraumRich;
}
