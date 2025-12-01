import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';
import { EinwilligungsschluesselKatalogEintrag } from '../../../asd/data/schule/EinwilligungsschluesselKatalogEintrag';

export class Einwilligungsschluessel extends CoreTypeSimple<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel> {


	/**
	 * Erstellt einen Einwilligungsschlüssel mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel>): void {
		CoreTypeDataManager.putManager(Einwilligungsschluessel.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel> {
		return CoreTypeDataManager.getManager(Einwilligungsschluessel.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Einwilligungsschluessel> {
		return CoreTypeSimple.valuesByClass(Einwilligungsschluessel.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): Einwilligungsschluessel | null {
		return new Einwilligungsschluessel();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Einwilligungsschluessel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Einwilligungsschluessel', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Einwilligungsschluessel>('de.svws_nrw.asd.types.schule.Einwilligungsschluessel');

}

export function cast_de_svws_nrw_asd_types_schule_Einwilligungsschluessel(obj: unknown): Einwilligungsschluessel {
	return obj as Einwilligungsschluessel;
}
