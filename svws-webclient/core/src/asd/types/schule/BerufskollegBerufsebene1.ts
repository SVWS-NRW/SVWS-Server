import { BerufskollegBerufsebeneKatalogEintrag } from '../../../asd/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class BerufskollegBerufsebene1 extends CoreTypeSimple<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1> {


	/**
	 * Erstellt einen Eintrag für die Berufsebene 1 mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1>): void {
		CoreTypeDataManager.putManager(BerufskollegBerufsebene1.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1> {
		return CoreTypeDataManager.getManager(BerufskollegBerufsebene1.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<BerufskollegBerufsebene1> {
		return CoreTypeSimple.valuesByClass(BerufskollegBerufsebene1.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): BerufskollegBerufsebene1 | null {
		return new BerufskollegBerufsebene1();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BerufskollegBerufsebene1';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.BerufskollegBerufsebene1', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<BerufskollegBerufsebene1>('de.svws_nrw.asd.types.schule.BerufskollegBerufsebene1');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegBerufsebene1(obj: unknown): BerufskollegBerufsebene1 {
	return obj as BerufskollegBerufsebene1;
}
