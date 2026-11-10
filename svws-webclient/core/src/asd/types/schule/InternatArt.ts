import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_id, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { InternatArtKatalogEintrag } from '../../../asd/data/schule/InternatArtKatalogEintrag';

export class InternatArt extends JavaEnum<InternatArt> implements CoreType<InternatArtKatalogEintrag, InternatArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<InternatArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, InternatArt> = new Map<string, InternatArt>();

	/**
	 * kein Internat
	 */
	public static readonly KEIN_INTERNAT: InternatArt = new InternatArt("KEIN_INTERNAT", 0);

	/**
	 * Internat nur für Jungen
	 */
	public static readonly INTERNAT_JUNGEN: InternatArt = new InternatArt("INTERNAT_JUNGEN", 1);

	/**
	 * Internat nur für Mädchen
	 */
	public static readonly INTERNAT_MAEDCHEN: InternatArt = new InternatArt("INTERNAT_MAEDCHEN", 2);

	/**
	 * Internat für Jungen und Mädchen
	 */
	public static readonly INTERNAT_NEUTRAL: InternatArt = new InternatArt("INTERNAT_NEUTRAL", 3);

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		InternatArt.all_values_by_ordinal.push(this);
		InternatArt.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<InternatArtKatalogEintrag, InternatArt>): void {
		CoreTypeDataManager.putManager(InternatArt.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<InternatArtKatalogEintrag, InternatArt> {
		return CoreTypeDataManager.getManager(InternatArt.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<InternatArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): InternatArt | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	public getManager(): CoreTypeDataManager<InternatArtKatalogEintrag, InternatArt> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): InternatArtKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public id(schuljahr: number): number | null {
		return de_svws_nrw_asd_types_CoreType_id(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<InternatArtKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.InternatArt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.InternatArt', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<InternatArt>('de.svws_nrw.asd.types.schule.InternatArt');

}

export function cast_de_svws_nrw_asd_types_schule_InternatArt(obj: unknown): InternatArt {
	return obj as InternatArt;
}
