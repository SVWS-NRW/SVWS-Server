import { JavaEnum } from '../../../java/lang/JavaEnum';
import { TalentschuleKatalogEintrag } from '../../../asd/data/schule/TalentschuleKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_id, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Talentschule extends JavaEnum<Talentschule> implements CoreType<TalentschuleKatalogEintrag, Talentschule> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<Talentschule> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, Talentschule> = new Map<string, Talentschule>();

	/**
	 * keine Talentschule
	 */
	public static readonly KEINE_TALENTSCHULE: Talentschule = new Talentschule("KEINE_TALENTSCHULE", 0);

	/**
	 * Talentschule
	 */
	public static readonly TALENTSCHULE: Talentschule = new Talentschule("TALENTSCHULE", 1);

	/**
	 * Talentschule auslaufend
	 */
	public static readonly TALENTSCHULE_AUSLAUFEND: Talentschule = new Talentschule("TALENTSCHULE_AUSLAUFEND", 2);

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		Talentschule.all_values_by_ordinal.push(this);
		Talentschule.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<TalentschuleKatalogEintrag, Talentschule>): void {
		CoreTypeDataManager.putManager(Talentschule.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<TalentschuleKatalogEintrag, Talentschule> {
		return CoreTypeDataManager.getManager(Talentschule.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<Talentschule> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): Talentschule | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	public getManager(): CoreTypeDataManager<TalentschuleKatalogEintrag, Talentschule> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): TalentschuleKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public id(schuljahr: number): number | null {
		return de_svws_nrw_asd_types_CoreType_id(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<TalentschuleKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Talentschule';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Talentschule', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<Talentschule>('de.svws_nrw.asd.types.schule.Talentschule');

}

export function cast_de_svws_nrw_asd_types_schule_Talentschule(obj: unknown): Talentschule {
	return obj as Talentschule;
}
