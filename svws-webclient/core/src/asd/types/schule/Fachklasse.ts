import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';
import { FachklasseKatalogEintrag } from '../../../asd/data/schule/FachklasseKatalogEintrag';

export class Fachklasse extends CoreTypeSimple<FachklasseKatalogEintrag, Fachklasse> {


	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<FachklasseKatalogEintrag, Fachklasse>): void {
		CoreTypeDataManager.putManager(Fachklasse.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<FachklasseKatalogEintrag, Fachklasse> {
		return CoreTypeDataManager.getManager(Fachklasse.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Fachklasse> {
		return CoreTypeSimple.valuesByClass(Fachklasse.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	public getInstance(): Fachklasse | null {
		return new Fachklasse();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Fachklasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.Fachklasse', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Fachklasse>('de.svws_nrw.asd.types.schule.Fachklasse');

}

export function cast_de_svws_nrw_asd_types_schule_Fachklasse(obj: unknown): Fachklasse {
	return obj as Fachklasse;
}
