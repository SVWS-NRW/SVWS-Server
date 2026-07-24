import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_id, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { VersetzungsvermerkKatalogEintrag } from '../../../asd/data/schueler/VersetzungsvermerkKatalogEintrag';

export class Versetzungsvermerk extends JavaEnum<Versetzungsvermerk> implements CoreType<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<Versetzungsvermerk> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, Versetzungsvermerk> = new Map<string, Versetzungsvermerk>();

	/**
	 * Versetzt
	 */
	public static readonly VERSETZT: Versetzungsvermerk = new Versetzungsvermerk("VERSETZT", 0, );

	/**
	 * Versetzt, Anforderungen nicht erfüllt
	 */
	public static readonly VERSETZT_ANFORERUNGEN_UNERFUELLT: Versetzungsvermerk = new Versetzungsvermerk("VERSETZT_ANFORERUNGEN_UNERFUELLT", 1, );

	/**
	 * Vorversetzt
	 */
	public static readonly VORVERSETZT: Versetzungsvermerk = new Versetzungsvermerk("VORVERSETZT", 2, );

	/**
	 * Freiwillig zurück
	 */
	public static readonly FREIWILLIG_ZURUECK: Versetzungsvermerk = new Versetzungsvermerk("FREIWILLIG_ZURUECK", 3, );

	/**
	 * Nicht versetzt
	 */
	public static readonly NICHT_VERSETZT: Versetzungsvermerk = new Versetzungsvermerk("NICHT_VERSETZT", 4, );

	/**
	 * Nicht versetzt, Nachprüfung möglich
	 */
	public static readonly NICHT_VERSETZT_NACHPRUEFUNG: Versetzungsvermerk = new Versetzungsvermerk("NICHT_VERSETZT_NACHPRUEFUNG", 5, );

	/**
	 * Abschluss
	 */
	public static readonly ABSCHLUSS: Versetzungsvermerk = new Versetzungsvermerk("ABSCHLUSS", 6, );

	/**
	 * Verbleib in der Schuleingangsphase
	 */
	public static readonly VERBLEIB_SCHULEINGANGSPHASE: Versetzungsvermerk = new Versetzungsvermerk("VERBLEIB_SCHULEINGANGSPHASE", 7, );

	/**
	 * Verbleib in Stufe
	 */
	public static readonly VERBLEIB_STUFE: Versetzungsvermerk = new Versetzungsvermerk("VERBLEIB_STUFE", 8, );

	/**
	 * Versetzung auf Probe
	 */
	public static readonly VERSETZUNG_PROBE: Versetzungsvermerk = new Versetzungsvermerk("VERSETZUNG_PROBE", 9, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		Versetzungsvermerk.all_values_by_ordinal.push(this);
		Versetzungsvermerk.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk>): void {
		CoreTypeDataManager.putManager(Versetzungsvermerk.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> {
		return CoreTypeDataManager.getManager(Versetzungsvermerk.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<Versetzungsvermerk> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): Versetzungsvermerk | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager(): CoreTypeDataManager<VersetzungsvermerkKatalogEintrag, Versetzungsvermerk> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): VersetzungsvermerkKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public id(schuljahr: number): number | null {
		return de_svws_nrw_asd_types_CoreType_id(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<VersetzungsvermerkKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.Versetzungsvermerk';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schueler.Versetzungsvermerk', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<Versetzungsvermerk>('de.svws_nrw.asd.types.schueler.Versetzungsvermerk');

}

export function cast_de_svws_nrw_asd_types_schueler_Versetzungsvermerk(obj: unknown): Versetzungsvermerk {
	return obj as Versetzungsvermerk;
}
