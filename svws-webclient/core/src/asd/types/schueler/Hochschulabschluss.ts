import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { HochschulabschlussKatalogEintrag } from '../../../asd/data/schueler/HochschulabschlussKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_id, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Hochschulabschluss extends JavaEnum<Hochschulabschluss> implements CoreType<HochschulabschlussKatalogEintrag, Hochschulabschluss> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<Hochschulabschluss> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, Hochschulabschluss> = new Map<string, Hochschulabschluss>();

	/**
	 * Ohne Hochschulabschluss
	 */
	public static readonly OHNE_HOCHSCHULABSCHLUSS: Hochschulabschluss = new Hochschulabschluss("OHNE_HOCHSCHULABSCHLUSS", 0, );

	/**
	 * Bachelor
	 */
	public static readonly BACHELOR: Hochschulabschluss = new Hochschulabschluss("BACHELOR", 1, );

	/**
	 * Master
	 */
	public static readonly MASTER: Hochschulabschluss = new Hochschulabschluss("MASTER", 2, );

	/**
	 * Promotion
	 */
	public static readonly PROMOTION: Hochschulabschluss = new Hochschulabschluss("PROMOTION", 3, );

	/**
	 * 1. Staatsexamen (Bachelor-Niveau)
	 */
	public static readonly STAATSEXAMEN_1_BACHELOR: Hochschulabschluss = new Hochschulabschluss("STAATSEXAMEN_1_BACHELOR", 4, );

	/**
	 * 2. Staatsexamen (Bachelor-Niveau)
	 */
	public static readonly STAATSEXAMEN_2_BACHELOR: Hochschulabschluss = new Hochschulabschluss("STAATSEXAMEN_2_BACHELOR", 5, );

	/**
	 * 1. Staatsexamen (Master-Niveau)
	 */
	public static readonly STAATSEXAMEN_1_MASTER: Hochschulabschluss = new Hochschulabschluss("STAATSEXAMEN_1_MASTER", 6, );

	/**
	 * 2. Staatsexamen (Master-Niveau)
	 */
	public static readonly STAATSEXAMEN_2_MASTER: Hochschulabschluss = new Hochschulabschluss("STAATSEXAMEN_2_MASTER", 7, );

	/**
	 * Diplom (Fachhochschule)
	 */
	public static readonly DIPLOM_FACHHOCHSCHULE: Hochschulabschluss = new Hochschulabschluss("DIPLOM_FACHHOCHSCHULE", 8, );

	/**
	 * Diplom (Universität)
	 */
	public static readonly DIPLOM_UNIVERSITAET: Hochschulabschluss = new Hochschulabschluss("DIPLOM_UNIVERSITAET", 9, );

	/**
	 * Magister
	 */
	public static readonly MAGISTER: Hochschulabschluss = new Hochschulabschluss("MAGISTER", 10, );

	/**
	 * Sonstiger Hochschulabschluss (Bachelor-Niveau)
	 */
	public static readonly SONSTIGER_HOCHSCHULABSCHLUSS_BACHELOR: Hochschulabschluss = new Hochschulabschluss("SONSTIGER_HOCHSCHULABSCHLUSS_BACHELOR", 11, );

	/**
	 * Sonstiger Hochschulabschluss (Master-Niveau)
	 */
	public static readonly SONSTIGER_HOCHSCHULABSCHLUSS_MASTER: Hochschulabschluss = new Hochschulabschluss("SONSTIGER_HOCHSCHULABSCHLUSS_MASTER", 12, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		Hochschulabschluss.all_values_by_ordinal.push(this);
		Hochschulabschluss.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<HochschulabschlussKatalogEintrag, Hochschulabschluss>): void {
		CoreTypeDataManager.putManager(Hochschulabschluss.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<HochschulabschlussKatalogEintrag, Hochschulabschluss> {
		return CoreTypeDataManager.getManager(Hochschulabschluss.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<Hochschulabschluss> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): Hochschulabschluss | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager(): CoreTypeDataManager<HochschulabschlussKatalogEintrag, Hochschulabschluss> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): HochschulabschlussKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public id(schuljahr: number): number | null {
		return de_svws_nrw_asd_types_CoreType_id(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<HochschulabschlussKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.Hochschulabschluss';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schueler.Hochschulabschluss', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<Hochschulabschluss>('de.svws_nrw.asd.types.schueler.Hochschulabschluss');

}

export function cast_de_svws_nrw_asd_types_schueler_Hochschulabschluss(obj: unknown): Hochschulabschluss {
	return obj as Hochschulabschluss;
}
