import { JavaObject } from '../../../java/lang/JavaObject';
import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
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

	/**
	 * Liefert die zulässigen Fachklassen für den angegebenen bkIndex in dem angegebenen Schuljahr
	 * Wenn der angegebene bkIndex null ist, dann werden alle Fachklassen für das angegebene Schuljahr zurückgegeben
	 *
	 * @param schuljahr das Schuljahr
	 * @param bkIndex der BKIndex
	 *
	 * @return Liste von {@link FachklasseKatalogEintrag}
	 */
	public static getBySchuljahrAndBKIndex(schuljahr: number, bkIndex: number | null): List<FachklasseKatalogEintrag> {
		const result = new ArrayList<FachklasseKatalogEintrag>();
		const fachklassen = Fachklasse.data().getEintraegeBySchuljahr(schuljahr);
		if (bkIndex === null) {
			return fachklassen;
		}
		for (const fachklasse of fachklassen) {
			if (JavaObject.equalsTranspiler(bkIndex, (fachklasse.bkIndex))) {
				result.add(fachklasse);
			}
		}
		return result;
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
