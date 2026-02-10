import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { BetreuungsartKatalogEintrag } from '../../../asd/data/schueler/BetreuungsartKatalogEintrag';

export class Betreuungsart extends JavaEnum<Betreuungsart> implements CoreType<BetreuungsartKatalogEintrag, Betreuungsart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<Betreuungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, Betreuungsart> = new Map<string, Betreuungsart>();

	/**
	 * keine Teilnahme an Ganztagsangeboten und/oder Übermittagbetreuung
	 */
	public static readonly KEINE: Betreuungsart = new Betreuungsart("KEINE", 0, );

	/**
	 * Übermittagbetreuung (Primarstufe)
	 */
	public static readonly UEBERMITTAG_PRIMARSTUFE: Betreuungsart = new Betreuungsart("UEBERMITTAG_PRIMARSTUFE", 1, );

	/**
	 * Übermittagbetreuung (Sekundarstufe)
	 */
	public static readonly UEBERMITTAG_SEKUNDARSTUFE: Betreuungsart = new Betreuungsart("UEBERMITTAG_SEKUNDARSTUFE", 2, );

	/**
	 * Übermittagbetreuung und zusätzliches Ganztagsangebot
	 */
	public static readonly UEBERMITTAG_UND_GANZTAG: Betreuungsart = new Betreuungsart("UEBERMITTAG_UND_GANZTAG", 3, );

	/**
	 * ausschließlich Schule von acht bis eins
	 */
	public static readonly NUR_ACHT_BIS_EINS: Betreuungsart = new Betreuungsart("NUR_ACHT_BIS_EINS", 4, );

	/**
	 * Schule von acht bis eins und Dreizehn Plus
	 */
	public static readonly ACHT_BIS_EINS_UND_DREIZEHN_PLUS: Betreuungsart = new Betreuungsart("ACHT_BIS_EINS_UND_DREIZEHN_PLUS", 5, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		Betreuungsart.all_values_by_ordinal.push(this);
		Betreuungsart.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<BetreuungsartKatalogEintrag, Betreuungsart>): void {
		CoreTypeDataManager.putManager(Betreuungsart.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<BetreuungsartKatalogEintrag, Betreuungsart> {
		return CoreTypeDataManager.getManager(Betreuungsart.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<Betreuungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): Betreuungsart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager(): CoreTypeDataManager<BetreuungsartKatalogEintrag, Betreuungsart> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): BetreuungsartKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<BetreuungsartKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.Betreuungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schueler.Betreuungsart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<Betreuungsart>('de.svws_nrw.asd.types.schueler.Betreuungsart');

}

export function cast_de_svws_nrw_asd_types_schueler_Betreuungsart(obj: unknown): Betreuungsart {
	return obj as Betreuungsart;
}
