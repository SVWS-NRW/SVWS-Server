import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { HerkunftsschulnummerKatalogEintrag } from '../../../asd/data/schule/HerkunftsschulnummerKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class Herkunftsschulnummer extends CoreTypeSimple<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer> {


	/**
	 * Erstellt einen Eintrag für die Herkunftsschulnummern mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer>): void {
		CoreTypeDataManager.putManager(Herkunftsschulnummer.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer> {
		return CoreTypeDataManager.getManager(Herkunftsschulnummer.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Herkunftsschulnummer> {
		return CoreTypeSimple.valuesByClass(Herkunftsschulnummer.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): Herkunftsschulnummer | null {
		return new Herkunftsschulnummer();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Herkunftsschulnummer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Herkunftsschulnummer', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Herkunftsschulnummer>('de.svws_nrw.asd.types.schule.Herkunftsschulnummer');

}

export function cast_de_svws_nrw_asd_types_schule_Herkunftsschulnummer(obj: unknown): Herkunftsschulnummer {
	return obj as Herkunftsschulnummer;
}
