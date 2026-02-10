import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { FormOffenerGanztagKatalogEintrag } from '../../../asd/data/schule/FormOffenerGanztagKatalogEintrag';

export class FormOffenerGanztag extends JavaEnum<FormOffenerGanztag> implements CoreType<FormOffenerGanztagKatalogEintrag, FormOffenerGanztag> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<FormOffenerGanztag> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, FormOffenerGanztag> = new Map<string, FormOffenerGanztag>();

	/**
	 * an der eigenen Schule wahrgenommen (ggf. an anderer Einrichtung)
	 */
	public static readonly EIGENE_SCHULE: FormOffenerGanztag = new FormOffenerGanztag("EIGENE_SCHULE", 0, );

	/**
	 * vollständig an einer anderen Schule
	 */
	public static readonly ANDERE_SCHULE: FormOffenerGanztag = new FormOffenerGanztag("ANDERE_SCHULE", 1, );

	/**
	 * nicht angeboten
	 */
	public static readonly NICHT_ANGEBOTEN: FormOffenerGanztag = new FormOffenerGanztag("NICHT_ANGEBOTEN", 2, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		FormOffenerGanztag.all_values_by_ordinal.push(this);
		FormOffenerGanztag.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<FormOffenerGanztagKatalogEintrag, FormOffenerGanztag>): void {
		CoreTypeDataManager.putManager(FormOffenerGanztag.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<FormOffenerGanztagKatalogEintrag, FormOffenerGanztag> {
		return CoreTypeDataManager.getManager(FormOffenerGanztag.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<FormOffenerGanztag> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): FormOffenerGanztag | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager(): CoreTypeDataManager<FormOffenerGanztagKatalogEintrag, FormOffenerGanztag> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): FormOffenerGanztagKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<FormOffenerGanztagKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.FormOffenerGanztag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.FormOffenerGanztag', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<FormOffenerGanztag>('de.svws_nrw.asd.types.schule.FormOffenerGanztag');

}

export function cast_de_svws_nrw_asd_types_schule_FormOffenerGanztag(obj: unknown): FormOffenerGanztag {
	return obj as FormOffenerGanztag;
}
