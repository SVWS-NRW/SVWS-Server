import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { VerkehrsspracheKatalogEintrag } from '../../../asd/data/schule/VerkehrsspracheKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class Verkehrssprache extends CoreTypeSimple<VerkehrsspracheKatalogEintrag, Verkehrssprache> {

	/**
	 * Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren dreistelligen ISO 639-2-Codes
	 */
	private static readonly _mapIso3 : HashMap<string, Verkehrssprache> = new HashMap<string, Verkehrssprache>();


	/**
	 * Erstellt eine Verkehrssprache mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<VerkehrsspracheKatalogEintrag, Verkehrssprache>) : void {
		CoreTypeDataManager.putManager(Verkehrssprache.class, manager);
		Verkehrssprache._mapIso3.clear();
		for (const sprache of manager.getWerte())
			for (const eintrag of sprache.historie())
				if (eintrag.iso3 !== null)
					Verkehrssprache._mapIso3.put(eintrag.iso3, sprache);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<VerkehrsspracheKatalogEintrag, Verkehrssprache> {
		return CoreTypeDataManager.getManager(Verkehrssprache.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values() : Array<Verkehrssprache> {
		return CoreTypeSimple.valuesByClass(Verkehrssprache.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance() : Verkehrssprache | null {
		return new Verkehrssprache();
	}

	/**
	 * Gibt die Verkehrssprache für das angegebene Kürzel zurück.
	 * Dabei wird anhand der Länge des Kürzels automatisch geprüft, ob
	 * eine Sprache nach ISO 639-1 bzw. ISO 639-2 angegeben wurde.
	 *
	 * @param kuerzel   das Kürzel der Verkehrssprache
	 *
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static getByIsoKuerzel(kuerzel : string | null) : Verkehrssprache | null {
		if (kuerzel === null)
			return null;
		if (kuerzel.length === 2)
			return Verkehrssprache.data().getWertByKuerzel(kuerzel);
		return Verkehrssprache._mapIso3.get(kuerzel);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Verkehrssprache';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Verkehrssprache', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<Verkehrssprache>('de.svws_nrw.asd.types.schule.Verkehrssprache');

}

export function cast_de_svws_nrw_asd_types_schule_Verkehrssprache(obj : unknown) : Verkehrssprache {
	return obj as Verkehrssprache;
}
