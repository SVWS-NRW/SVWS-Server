import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerMehrleistungsartKatalogEintrag } from '../../../asd/data/lehrer/LehrerMehrleistungsartKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class LehrerMehrleistungsarten extends CoreTypeSimple<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten> {


	/**
	 * Erstellt einen LehrerMehrleistungsart mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten>) : void {
		CoreTypeDataManager.putManager(LehrerMehrleistungsarten.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten> {
		return CoreTypeDataManager.getManager(LehrerMehrleistungsarten.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values() : Array<LehrerMehrleistungsarten> {
		return CoreTypeSimple.valuesByClass(LehrerMehrleistungsarten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance() : LehrerMehrleistungsarten | null {
		return new LehrerMehrleistungsarten();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<LehrerMehrleistungsarten>('de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerMehrleistungsarten(obj : unknown) : LehrerMehrleistungsarten {
	return obj as LehrerMehrleistungsarten;
}
