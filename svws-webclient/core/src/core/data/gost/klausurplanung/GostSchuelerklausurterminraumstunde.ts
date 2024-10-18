import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class GostSchuelerklausurterminraumstunde extends JavaObject {

	/**
	 * Die ID des zugehörigen {@link GostSchuelerklausurTermin}s.
	 */
	public idSchuelerklausurtermin : number = -1;

	/**
	 * Die ID der zugehörigen {@link GostKlausurraumstunde}.
	 */
	public idRaumstunde : number = -1;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return (another !== null) && (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde')))) && (this.idSchuelerklausurtermin === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurterminraumstunde(another)).idSchuelerklausurtermin) && (this.idRaumstunde === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurterminraumstunde(another)).idRaumstunde);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.idSchuelerklausurtermin));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde'].includes(name);
	}

	public static class = new Class<GostSchuelerklausurterminraumstunde>('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde');

	public static transpilerFromJSON(json : string): GostSchuelerklausurterminraumstunde {
		const obj = JSON.parse(json) as Partial<GostSchuelerklausurterminraumstunde>;
		const result = new GostSchuelerklausurterminraumstunde();
		if (obj.idSchuelerklausurtermin === undefined)
			throw new Error('invalid json format, missing attribute idSchuelerklausurtermin');
		result.idSchuelerklausurtermin = obj.idSchuelerklausurtermin;
		if (obj.idRaumstunde === undefined)
			throw new Error('invalid json format, missing attribute idRaumstunde');
		result.idRaumstunde = obj.idRaumstunde;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausurterminraumstunde) : string {
		let result = '{';
		result += '"idSchuelerklausurtermin" : ' + obj.idSchuelerklausurtermin.toString() + ',';
		result += '"idRaumstunde" : ' + obj.idRaumstunde.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausurterminraumstunde>) : string {
		let result = '{';
		if (obj.idSchuelerklausurtermin !== undefined) {
			result += '"idSchuelerklausurtermin" : ' + obj.idSchuelerklausurtermin.toString() + ',';
		}
		if (obj.idRaumstunde !== undefined) {
			result += '"idRaumstunde" : ' + obj.idRaumstunde.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurterminraumstunde(obj : unknown) : GostSchuelerklausurterminraumstunde {
	return obj as GostSchuelerklausurterminraumstunde;
}
