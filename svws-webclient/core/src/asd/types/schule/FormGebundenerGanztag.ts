import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_id, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { FormGebundenerGanztagKatalogEintrag } from '../../../asd/data/schule/FormGebundenerGanztagKatalogEintrag';

export class FormGebundenerGanztag extends JavaEnum<FormGebundenerGanztag> implements CoreType<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<FormGebundenerGanztag> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, FormGebundenerGanztag> = new Map<string, FormGebundenerGanztag>();

	/**
	 * Schule ohne Ganztagsbetrieb
	 */
	public static readonly OHNE_GANZTAG: FormGebundenerGanztag = new FormGebundenerGanztag("OHNE_GANZTAG", 0);

	/**
	 * Genehmigte Ganztagsschule
	 */
	public static readonly GANZTAG_GENEHMIGT: FormGebundenerGanztag = new FormGebundenerGanztag("GANZTAG_GENEHMIGT", 1);

	/**
	 * Schule mit Ganztagsbetrieb (ohne Genehmigung)
	 */
	public static readonly GANZTAG_NICHT_GENEHMIGT: FormGebundenerGanztag = new FormGebundenerGanztag("GANZTAG_NICHT_GENEHMIGT", 2);

	/**
	 * Schule mit erweitertem Ganztagsbetrieb
	 */
	public static readonly GANZTAG_ERWEITERT: FormGebundenerGanztag = new FormGebundenerGanztag("GANZTAG_ERWEITERT", 3);

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		FormGebundenerGanztag.all_values_by_ordinal.push(this);
		FormGebundenerGanztag.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag>): void {
		CoreTypeDataManager.putManager(FormGebundenerGanztag.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> {
		return CoreTypeDataManager.getManager(FormGebundenerGanztag.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<FormGebundenerGanztag> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): FormGebundenerGanztag | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	public getManager(): CoreTypeDataManager<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): FormGebundenerGanztagKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public id(schuljahr: number): number | null {
		return de_svws_nrw_asd_types_CoreType_id(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<FormGebundenerGanztagKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.FormGebundenerGanztag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.FormGebundenerGanztag', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<FormGebundenerGanztag>('de.svws_nrw.asd.types.schule.FormGebundenerGanztag');

}

export function cast_de_svws_nrw_asd_types_schule_FormGebundenerGanztag(obj: unknown): FormGebundenerGanztag {
	return obj as FormGebundenerGanztag;
}
