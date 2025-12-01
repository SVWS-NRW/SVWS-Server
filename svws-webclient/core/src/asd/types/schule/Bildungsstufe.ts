import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';
import { BildungsstufeKatalogEintrag } from '../../../asd/data/schule/BildungsstufeKatalogEintrag';

export class Bildungsstufe extends CoreTypeSimple<BildungsstufeKatalogEintrag, Bildungsstufe> {


	/**
	 * Erstellt einer Bildungsstufe mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<BildungsstufeKatalogEintrag, Bildungsstufe>): void {
		CoreTypeDataManager.putManager(Bildungsstufe.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<BildungsstufeKatalogEintrag, Bildungsstufe> {
		return CoreTypeDataManager.getManager(Bildungsstufe.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Bildungsstufe> {
		return CoreTypeSimple.valuesByClass(Bildungsstufe.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): Bildungsstufe | null {
		return new Bildungsstufe();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Bildungsstufe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.Bildungsstufe', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Bildungsstufe>('de.svws_nrw.asd.types.schule.Bildungsstufe');

}

export function cast_de_svws_nrw_asd_types_schule_Bildungsstufe(obj: unknown): Bildungsstufe {
	return obj as Bildungsstufe;
}
