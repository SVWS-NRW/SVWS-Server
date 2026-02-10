import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { HerkunftSonstigeKatalogEintrag } from '../../../asd/data/schueler/HerkunftSonstigeKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class HerkunftSonstige extends CoreTypeSimple<HerkunftSonstigeKatalogEintrag, HerkunftSonstige> {


	/**
	 * Erstellt eine HerkunftSonstige mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<HerkunftSonstigeKatalogEintrag, HerkunftSonstige>): void {
		CoreTypeDataManager.putManager(HerkunftSonstige.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<HerkunftSonstigeKatalogEintrag, HerkunftSonstige> {
		return CoreTypeDataManager.getManager(HerkunftSonstige.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<HerkunftSonstige> {
		return CoreTypeSimple.valuesByClass(HerkunftSonstige.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): HerkunftSonstige | null {
		return new HerkunftSonstige();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.HerkunftSonstige';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schueler.HerkunftSonstige', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<HerkunftSonstige>('de.svws_nrw.asd.types.schueler.HerkunftSonstige');

}

export function cast_de_svws_nrw_asd_types_schueler_HerkunftSonstige(obj: unknown): HerkunftSonstige {
	return obj as HerkunftSonstige;
}
