import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerPflichtstundensollVollzeitKatalogEintrag } from '../../../asd/data/lehrer/LehrerPflichtstundensollVollzeitKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class LehrerPflichtstundensollVollzeit extends CoreTypeSimple<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit> {


	/**
	 * Erstellt einen LehrerPflichtstundensollVollzeit mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit>): void {
		CoreTypeDataManager.putManager(LehrerPflichtstundensollVollzeit.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit> {
		return CoreTypeDataManager.getManager(LehrerPflichtstundensollVollzeit.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<LehrerPflichtstundensollVollzeit> {
		return CoreTypeSimple.valuesByClass(LehrerPflichtstundensollVollzeit.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): LehrerPflichtstundensollVollzeit | null {
		return new LehrerPflichtstundensollVollzeit();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerPflichtstundensollVollzeit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerPflichtstundensollVollzeit', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<LehrerPflichtstundensollVollzeit>('de.svws_nrw.asd.types.lehrer.LehrerPflichtstundensollVollzeit');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerPflichtstundensollVollzeit(obj: unknown): LehrerPflichtstundensollVollzeit {
	return obj as LehrerPflichtstundensollVollzeit;
}
