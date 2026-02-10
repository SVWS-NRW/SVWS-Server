import { BerufskollegBerufsebeneKatalogEintrag } from '../../../asd/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class BerufskollegBerufsebene2 extends CoreTypeSimple<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene2> {


	/**
	 * Erstellt einen Eintrag für die Berufsebene 2 mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene2>): void {
		CoreTypeDataManager.putManager(BerufskollegBerufsebene2.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene2> {
		return CoreTypeDataManager.getManager(BerufskollegBerufsebene2.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<BerufskollegBerufsebene2> {
		return CoreTypeSimple.valuesByClass(BerufskollegBerufsebene2.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): BerufskollegBerufsebene2 | null {
		return new BerufskollegBerufsebene2();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BerufskollegBerufsebene2';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.BerufskollegBerufsebene2', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<BerufskollegBerufsebene2>('de.svws_nrw.asd.types.schule.BerufskollegBerufsebene2');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegBerufsebene2(obj: unknown): BerufskollegBerufsebene2 {
	return obj as BerufskollegBerufsebene2;
}
