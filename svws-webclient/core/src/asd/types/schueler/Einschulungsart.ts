import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { EinschulungsartKatalogEintrag } from '../../../asd/data/schueler/EinschulungsartKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class Einschulungsart extends CoreTypeSimple<EinschulungsartKatalogEintrag, Einschulungsart> {


	/**
	 * Erstellt eine Einschulungsart mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<EinschulungsartKatalogEintrag, Einschulungsart>) : void {
		CoreTypeDataManager.putManager(Einschulungsart.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<EinschulungsartKatalogEintrag, Einschulungsart> {
		return CoreTypeDataManager.getManager(Einschulungsart.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values() : Array<Einschulungsart> {
		return CoreTypeSimple.valuesByClass(Einschulungsart.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance() : Einschulungsart | null {
		return new Einschulungsart();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public getLetzterEintrag() : EinschulungsartKatalogEintrag {
		return this.getManager().getHistorieByWert(this).getLast();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.Einschulungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schueler.Einschulungsart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<Einschulungsart>('de.svws_nrw.asd.types.schueler.Einschulungsart');

}

export function cast_de_svws_nrw_asd_types_schueler_Einschulungsart(obj : unknown) : Einschulungsart {
	return obj as Einschulungsart;
}
