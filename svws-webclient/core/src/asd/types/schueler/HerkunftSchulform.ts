import { HerkunftSchulformKatalogEintrag } from '../../../asd/data/schueler/HerkunftSchulformKatalogEintrag';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class HerkunftSchulform extends CoreTypeSimple<HerkunftSchulformKatalogEintrag, HerkunftSchulform> {


	/**
	 * Erstellt eine HerkunftSchulform mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<HerkunftSchulformKatalogEintrag, HerkunftSchulform>): void {
		CoreTypeDataManager.putManager(HerkunftSchulform.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<HerkunftSchulformKatalogEintrag, HerkunftSchulform> {
		return CoreTypeDataManager.getManager(HerkunftSchulform.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<HerkunftSchulform> {
		return CoreTypeSimple.valuesByClass(HerkunftSchulform.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): HerkunftSchulform | null {
		return new HerkunftSchulform();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.HerkunftSchulform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schueler.HerkunftSchulform', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<HerkunftSchulform>('de.svws_nrw.asd.types.schueler.HerkunftSchulform');

}

export function cast_de_svws_nrw_asd_types_schueler_HerkunftSchulform(obj: unknown): HerkunftSchulform {
	return obj as HerkunftSchulform;
}
