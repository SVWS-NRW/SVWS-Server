import { FloskelgruppenartKatalogEintrag } from '../../../asd/data/schule/FloskelgruppenartKatalogEintrag';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class Floskelgruppenart extends CoreTypeSimple<FloskelgruppenartKatalogEintrag, Floskelgruppenart> {


	/**
	 * Erstellt eine Floskelgruppenart mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<FloskelgruppenartKatalogEintrag, Floskelgruppenart>): void {
		CoreTypeDataManager.putManager(Floskelgruppenart.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<FloskelgruppenartKatalogEintrag, Floskelgruppenart> {
		return CoreTypeDataManager.getManager(Floskelgruppenart.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Floskelgruppenart> {
		return CoreTypeSimple.valuesByClass(Floskelgruppenart.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): Floskelgruppenart | null {
		return new Floskelgruppenart();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Floskelgruppenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Floskelgruppenart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Floskelgruppenart>('de.svws_nrw.asd.types.schule.Floskelgruppenart');

}

export function cast_de_svws_nrw_asd_types_schule_Floskelgruppenart(obj: unknown): Floskelgruppenart {
	return obj as Floskelgruppenart;
}
