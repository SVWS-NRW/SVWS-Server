import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { DQRNiveauKatalogEintrag } from '../../../asd/data/schule/DQRNiveauKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class DQRNiveau extends CoreTypeSimple<DQRNiveauKatalogEintrag, DQRNiveau> {


	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<DQRNiveauKatalogEintrag, DQRNiveau>): void {
		CoreTypeDataManager.putManager(DQRNiveau.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<DQRNiveauKatalogEintrag, DQRNiveau> {
		return CoreTypeDataManager.getManager(DQRNiveau.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<DQRNiveau> {
		return CoreTypeSimple.valuesByClass(DQRNiveau.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	public getInstance(): DQRNiveau | null {
		return new DQRNiveau();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.DQRNiveau';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.DQRNiveau', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<DQRNiveau>('de.svws_nrw.asd.types.schule.DQRNiveau');

}

export function cast_de_svws_nrw_asd_types_schule_DQRNiveau(obj: unknown): DQRNiveau {
	return obj as DQRNiveau;
}
