import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { SchulabschlussAllgemeinbildendKatalogEintrag } from '../../../asd/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class SchulabschlussAllgemeinbildend extends JavaEnum<SchulabschlussAllgemeinbildend> implements CoreType<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchulabschlussAllgemeinbildend> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchulabschlussAllgemeinbildend> = new Map<string, SchulabschlussAllgemeinbildend>();

	/**
	 * Es liegt kein Abschluss vor
	 */
	public static readonly OA : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("OA", 0, );

	/**
	 * Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	public static readonly HA9A : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9A", 1, );

	/**
	 * Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	public static readonly HA9 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9", 2, );

	/**
	 * Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen) - siehe BK-Bildungsgang A12
	 */
	public static readonly HA9_FOE : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9_FOE", 3, );

	/**
	 * Hauptschulabschluss nach Klasse 9 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe
	 */
	public static readonly HA9_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9_Q", 4, );

	/**
	 * Hauptschulabschluss nach Klasse 10
	 */
	public static readonly HA10 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA10", 5, );

	/**
	 * Hauptschulabschluss nach Klasse 10 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe
	 */
	public static readonly HA10_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA10_Q", 6, );

	/**
	 * Der Mittlere Schulabschluss bzw. Fachoberschulreife
	 */
	public static readonly MSA : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA", 7, );

	/**
	 * Der Mittlere Schulabschluss mit der Berechtigung zum Besuch Gymnasialen Oberstufe
	 */
	public static readonly MSA_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA_Q", 8, );

	/**
	 * Der Mittlere Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase der Gymnasialen Oberstufe
	 */
	public static readonly MSA_Q1 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA_Q1", 9, );

	/**
	 * Versetzung in die Klasse 11 der Fachoberschule (BK)
	 */
	public static readonly VS_11 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("VS_11", 10, );

	/**
	 * Fachhochschulreife (nur schulischer Teil)
	 */
	public static readonly FHR_S : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FHR_S", 11, );

	/**
	 * Fachhochschulreife
	 */
	public static readonly FHR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FHR", 12, );

	/**
	 * fachgebundene Hochschulreife (BK)
	 */
	public static readonly FGHR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FGHR", 13, );

	/**
	 * Abitur / Allgemeine Hochschulreife
	 */
	public static readonly ABITUR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("ABITUR", 14, );

	/**
	 * Förderschule (Förderschwerpunkt geistige Entwicklung)
	 */
	public static readonly FOEG : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FOEG", 15, );

	/**
	 * Förderschule (Förderschwerpunkt Lernen)
	 */
	public static readonly FOEL : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FOEL", 16, );

	/**
	 * Waldorfschule
	 */
	public static readonly WALD : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("WALD", 17, );

	/**
	 * Ohne Abschluss, kommt aus der Deutschförderung
	 */
	public static readonly DFR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("DFR", 18, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		SchulabschlussAllgemeinbildend.all_values_by_ordinal.push(this);
		SchulabschlussAllgemeinbildend.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend>) : void {
		CoreTypeDataManager.putManager(SchulabschlussAllgemeinbildend.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> {
		return CoreTypeDataManager.getManager(SchulabschlussAllgemeinbildend.class);
	}

	/**
	 * Prüft, ob dieser Abschluss dem Abschluss mit dem übergebene Kürzel entspricht.
	 *
	 * @param kuerzel   das Kürzel des anderen Abschlusses
	 *
	 * @return true, falls die Abschlüsse übereinstimmen und ansonsten false
	 */
	public is(kuerzel : string | null) : boolean {
		if (kuerzel === null)
			return false;
		const other : SchulabschlussAllgemeinbildend | null = SchulabschlussAllgemeinbildend.data().getWertByKuerzel(kuerzel);
		if (other === null)
			return false;
		return (other as unknown === this as unknown);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchulabschlussAllgemeinbildend> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchulabschlussAllgemeinbildend | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SchulabschlussAllgemeinbildendKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<SchulabschlussAllgemeinbildendKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<SchulabschlussAllgemeinbildend>('de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend');

}

export function cast_de_svws_nrw_asd_types_schule_SchulabschlussAllgemeinbildend(obj : unknown) : SchulabschlussAllgemeinbildend {
	return obj as SchulabschlussAllgemeinbildend;
}
