import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurraumstunde extends JavaObject {

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public id : number = -1;

	/**
	 * Die ID des Klausurraums.
	 */
	public idRaum : number = -1;

	/**
	 * Die ID des Zeitrasters.
	 */
	public idZeitraster : number = -1;


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
		return (another !== null) && (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde')))) && (this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumstunde(another)).id);
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
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde'].includes(name);
	}

	public static class = new Class<GostKlausurraumstunde>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde');

	public static transpilerFromJSON(json : string): GostKlausurraumstunde {
		const obj = JSON.parse(json) as Partial<GostKlausurraumstunde>;
		const result = new GostKlausurraumstunde();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idRaum === undefined)
			throw new Error('invalid json format, missing attribute idRaum');
		result.idRaum = obj.idRaum;
		if (obj.idZeitraster === undefined)
			throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumstunde) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idRaum" : ' + obj.idRaum.toString() + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumstunde>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idRaum !== undefined) {
			result += '"idRaum" : ' + obj.idRaum.toString() + ',';
		}
		if (obj.idZeitraster !== undefined) {
			result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumstunde(obj : unknown) : GostKlausurraumstunde {
	return obj as GostKlausurraumstunde;
}
