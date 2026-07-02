import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { AnrechnungsantragBKAZVOKatalogEintrag } from '../../../asd/data/schueler/AnrechnungsantragBKAZVOKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Class } from '../../../java/lang/Class';

export class AnrechnungsantragBKAZVO extends CoreTypeSimple<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO> {


	/**
	 * Erstellt eine AnrechnungsantragBKAZVO
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO>): void {
		CoreTypeDataManager.putManager(AnrechnungsantragBKAZVO.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO> {
		return CoreTypeDataManager.getManager(AnrechnungsantragBKAZVO.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values(): Array<AnrechnungsantragBKAZVO> {
		return CoreTypeSimple.valuesByClass(AnrechnungsantragBKAZVO.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	public getInstance(): AnrechnungsantragBKAZVO | null {
		return new AnrechnungsantragBKAZVO();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public getLetzterEintrag(): AnrechnungsantragBKAZVOKatalogEintrag {
		return this.getManager().getHistorieByWert(this).getLast();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.AnrechnungsantragBKAZVO';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schueler.AnrechnungsantragBKAZVO', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static readonly class = new Class<AnrechnungsantragBKAZVO>('de.svws_nrw.asd.types.schueler.AnrechnungsantragBKAZVO');

}

export function cast_de_svws_nrw_asd_types_schueler_AnrechnungsantragBKAZVO(obj: unknown): AnrechnungsantragBKAZVO {
	return obj as AnrechnungsantragBKAZVO;
}
