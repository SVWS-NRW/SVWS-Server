import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { OrteKatalogEintrag } from '../../../asd/data/schule/OrteKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class Orte extends CoreTypeSimple<OrteKatalogEintrag, Orte> {


	/**
	 * Erstellt ein Ort mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<OrteKatalogEintrag, Orte>): void {
		CoreTypeDataManager.putManager(Orte.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<OrteKatalogEintrag, Orte> {
		return CoreTypeDataManager.getManager(Orte.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Orte> {
		return CoreTypeSimple.valuesByClass(Orte.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	public getInstance(): Orte | null {
		return new Orte();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Orte';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Orte', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Orte>('de.svws_nrw.asd.types.schule.Orte');

}

export function cast_de_svws_nrw_asd_types_schule_Orte(obj: unknown): Orte {
	return obj as Orte;
}
