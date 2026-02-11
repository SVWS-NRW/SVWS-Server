import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerMinderleistungsartKatalogEintrag } from '../../../asd/data/lehrer/LehrerMinderleistungsartKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class LehrerMinderleistungsarten extends CoreTypeSimple<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> {


	/**
	 * Erstellt einen LehrerMinderleistungsart mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten>): void {
		CoreTypeDataManager.putManager(LehrerMinderleistungsarten.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> {
		return CoreTypeDataManager.getManager(LehrerMinderleistungsarten.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<LehrerMinderleistungsarten> {
		return CoreTypeSimple.valuesByClass(LehrerMinderleistungsarten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): LehrerMinderleistungsarten | null {
		return new LehrerMinderleistungsarten();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<LehrerMinderleistungsarten>('de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerMinderleistungsarten(obj: unknown): LehrerMinderleistungsarten {
	return obj as LehrerMinderleistungsarten;
}
