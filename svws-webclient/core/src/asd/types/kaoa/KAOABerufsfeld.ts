import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { KAOABerufsfeldKatalogEintrag } from '../../../asd/data/kaoa/KAOABerufsfeldKatalogEintrag';

export class KAOABerufsfeld extends JavaEnum<KAOABerufsfeld> implements CoreType<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOABerufsfeld> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOABerufsfeld> = new Map<string, KAOABerufsfeld>();

	/**
	 * KAoA-Berufsfeld: Bau, Architektur, Vermessung
	 */
	public static readonly BAV : KAOABerufsfeld = new KAOABerufsfeld("BAV", 0, );

	/**
	 * KAoA-Berufsfeld: Dienstleistung
	 */
	public static readonly D : KAOABerufsfeld = new KAOABerufsfeld("D", 1, );

	/**
	 * KAoA-Berufsfeld: Elektro
	 */
	public static readonly EL : KAOABerufsfeld = new KAOABerufsfeld("EL", 2, );

	/**
	 * KAoA-Berufsfeld: Gesundheit
	 */
	public static readonly G : KAOABerufsfeld = new KAOABerufsfeld("G", 3, );

	/**
	 * KAoA-Berufsfeld: Gesellschafts-,Geisteswissenschaften
	 */
	public static readonly GESGE : KAOABerufsfeld = new KAOABerufsfeld("GESGE", 4, );

	/**
	 * KAoA-Berufsfeld: IT, Computer
	 */
	public static readonly ITC : KAOABerufsfeld = new KAOABerufsfeld("ITC", 5, );

	/**
	 * KAoA-Berufsfeld: Kunst, Kultur, Gestaltung
	 */
	public static readonly KKG : KAOABerufsfeld = new KAOABerufsfeld("KKG", 6, );

	/**
	 * KAoA-Berufsfeld: Landwirtschaft, Natur, Umwelt
	 */
	public static readonly LANAUM : KAOABerufsfeld = new KAOABerufsfeld("LANAUM", 7, );

	/**
	 * KAoA-Berufsfeld: Metall, Maschinenbau
	 */
	public static readonly M : KAOABerufsfeld = new KAOABerufsfeld("M", 8, );

	/**
	 * KAoA-Berufsfeld: Medien
	 */
	public static readonly ME : KAOABerufsfeld = new KAOABerufsfeld("ME", 9, );

	/**
	 * KAoA-Berufsfeld: Naturwissenschaft
	 */
	public static readonly N : KAOABerufsfeld = new KAOABerufsfeld("N", 10, );

	/**
	 * KAoA-Berufsfeld: Produktion, Fertigung
	 */
	public static readonly PRFE : KAOABerufsfeld = new KAOABerufsfeld("PRFE", 11, );

	/**
	 * KAoA-Berufsfeld: Soziales, Pädagogik
	 */
	public static readonly SP : KAOABerufsfeld = new KAOABerufsfeld("SP", 12, );

	/**
	 * KAoA-Berufsfeld: Technik, Technologiefelder
	 */
	public static readonly TEC : KAOABerufsfeld = new KAOABerufsfeld("TEC", 13, );

	/**
	 * KAoA-Berufsfeld: Verkehr, Logistik
	 */
	public static readonly VL : KAOABerufsfeld = new KAOABerufsfeld("VL", 14, );

	/**
	 * KAoA-Berufsfeld: Wirtschaft, Verwaltung
	 */
	public static readonly WIVE : KAOABerufsfeld = new KAOABerufsfeld("WIVE", 15, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOABerufsfeld.all_values_by_ordinal.push(this);
		KAOABerufsfeld.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld>) : void {
		CoreTypeDataManager.putManager(KAOABerufsfeld.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> {
		return CoreTypeDataManager.getManager(KAOABerufsfeld.class);
	}

	/**
	 * Liefert alle zulässigen KAoA-Berufsfeld-Historien-Einträge in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return alle zulässigen KAoA-Berufsfeld-Historien-Einträge in dem angegebenen Schuljahr.
	 */
	public static getEintraegeBySchuljahr(schuljahr : number) : List<KAOABerufsfeldKatalogEintrag> {
		return KAOABerufsfeld.data().getEintraegeBySchuljahr(schuljahr);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOABerufsfeld> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOABerufsfeld | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOABerufsfeldKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<KAOABerufsfeldKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOABerufsfeld';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOABerufsfeld', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOABerufsfeld>('de.svws_nrw.asd.types.kaoa.KAOABerufsfeld');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOABerufsfeld(obj : unknown) : KAOABerufsfeld {
	return obj as KAOABerufsfeld;
}
