import { BerufskollegBerufsebeneKatalogEintrag } from '../../../asd/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class BerufskollegBerufsebene3 extends CoreTypeSimple<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene3> {


	/**
	 * Erstellt einen Eintrag für die Berufsebene 3 mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene3>): void {
		CoreTypeDataManager.putManager(BerufskollegBerufsebene3.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene3> {
		return CoreTypeDataManager.getManager(BerufskollegBerufsebene3.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<BerufskollegBerufsebene3> {
		return CoreTypeSimple.valuesByClass(BerufskollegBerufsebene3.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): BerufskollegBerufsebene3 | null {
		return new BerufskollegBerufsebene3();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BerufskollegBerufsebene3';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.BerufskollegBerufsebene3', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<BerufskollegBerufsebene3>('de.svws_nrw.asd.types.schule.BerufskollegBerufsebene3');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegBerufsebene3(obj: unknown): BerufskollegBerufsebene3 {
	return obj as BerufskollegBerufsebene3;
}
