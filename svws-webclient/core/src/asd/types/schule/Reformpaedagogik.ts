import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { ReformpaedagogikKatalogEintrag } from '../../../asd/data/schule/ReformpaedagogikKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class Reformpaedagogik extends CoreTypeSimple<ReformpaedagogikKatalogEintrag, Reformpaedagogik> {


	/**
	 * Erstellung einer Reformpaedagogik mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<ReformpaedagogikKatalogEintrag, Reformpaedagogik>): void {
		CoreTypeDataManager.putManager(Reformpaedagogik.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<ReformpaedagogikKatalogEintrag, Reformpaedagogik> {
		return CoreTypeDataManager.getManager(Reformpaedagogik.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<Reformpaedagogik> {
		return CoreTypeSimple.valuesByClass(Reformpaedagogik.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance(): Reformpaedagogik | null {
		return new Reformpaedagogik();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Reformpaedagogik';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Reformpaedagogik', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<Reformpaedagogik>('de.svws_nrw.asd.types.schule.Reformpaedagogik');

}

export function cast_de_svws_nrw_asd_types_schule_Reformpaedagogik(obj: unknown): Reformpaedagogik {
	return obj as Reformpaedagogik;
}
