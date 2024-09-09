import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerAnrechnungsgrundKatalogEintrag } from '../../../asd/data/lehrer/LehrerAnrechnungsgrundKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class LehrerAnrechnungsgrund extends CoreTypeSimple<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund> {


	/**
	 * Erstellt einen LehrerAnrechnungsgrund mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund>) : void {
		CoreTypeDataManager.putManager(LehrerAnrechnungsgrund.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund> {
		return CoreTypeDataManager.getManager(LehrerAnrechnungsgrund.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values() : Array<LehrerAnrechnungsgrund> {
		return CoreTypeSimple.valuesByClass(LehrerAnrechnungsgrund.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance() : LehrerAnrechnungsgrund | null {
		return new LehrerAnrechnungsgrund();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<LehrerAnrechnungsgrund>('de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerAnrechnungsgrund(obj : unknown) : LehrerAnrechnungsgrund {
	return obj as LehrerAnrechnungsgrund;
}
