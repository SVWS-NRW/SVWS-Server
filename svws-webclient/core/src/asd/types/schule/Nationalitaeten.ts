import { CoreTypeSimple } from '../../../asd/types/CoreTypeSimple';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { NationalitaetenKatalogEintrag } from '../../../asd/data/schule/NationalitaetenKatalogEintrag';
import { Class } from '../../../java/lang/Class';
import { CoreTypeException } from '../../../asd/data/CoreTypeException';

export class Nationalitaeten extends CoreTypeSimple<NationalitaetenKatalogEintrag, Nationalitaeten> {

	/**
	 * Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem dreistelligen ISO-Code
	 */
	private static readonly _mapISO3 : HashMap<string, Nationalitaeten> = new HashMap<string, Nationalitaeten>();

	/**
	 * Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem zweistelligen ISO-Code
	 */
	private static readonly _mapISO2 : HashMap<string, Nationalitaeten> = new HashMap<string, Nationalitaeten>();

	/**
	 * Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu DESTATIS-Code
	 */
	private static readonly _mapDESTATIS : HashMap<string, Nationalitaeten> = new HashMap<string, Nationalitaeten>();


	/**
	 * Erstellt eine Nationalitaeten mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<NationalitaetenKatalogEintrag, Nationalitaeten>) : void {
		CoreTypeDataManager.putManager(Nationalitaeten.class, manager);
		Nationalitaeten._mapISO3.clear();
		Nationalitaeten._mapISO2.clear();
		Nationalitaeten._mapDESTATIS.clear();
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<NationalitaetenKatalogEintrag, Nationalitaeten> {
		return CoreTypeDataManager.getManager(Nationalitaeten.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static values() : Array<Nationalitaeten> {
		return CoreTypeSimple.valuesByClass(Nationalitaeten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	public getInstance() : Nationalitaeten | null {
		return new Nationalitaeten();
	}

	/**
	 * Gibt eine Map von den dreistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static getMapISO3() : HashMap<string, Nationalitaeten> {
		if (Nationalitaeten._mapISO3.isEmpty())
			for (const s of Nationalitaeten.values())
				for (const kat of s.historie())
					if (kat.iso3 !== null)
						Nationalitaeten._mapISO3.put(kat.iso3, s);
		return Nationalitaeten._mapISO3;
	}

	/**
	 * Gibt eine Map von den zweistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static getMapISO2() : HashMap<string, Nationalitaeten> {
		if (Nationalitaeten._mapISO2.isEmpty()) {
			for (const s of Nationalitaeten.values())
				for (const kat of s.historie())
					if (kat.iso2 !== null)
						Nationalitaeten._mapISO2.put(kat.iso2, s);
		}
		return Nationalitaeten._mapISO2;
	}

	/**
	 * Gibt eine Map von den DESTATIS-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static getMapDESTATIS() : HashMap<string, Nationalitaeten> {
		if (Nationalitaeten._mapDESTATIS.isEmpty()) {
			for (const s of Nationalitaeten.values())
				for (const kat of s.historie())
					if (kat.codeDEStatis !== null)
						Nationalitaeten._mapDESTATIS.put(kat.codeDEStatis, s);
		}
		return Nationalitaeten._mapDESTATIS;
	}

	/**
	 * Gibt die Nationalität für den angegebenen dreistelligen ISO-Code nach ISO 3166-1 zurück.
	 *
	 * @param code   der ISO-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static getByISO3(code : string | null) : Nationalitaeten | null {
		return Nationalitaeten.getMapISO3().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen zweistelligen ISO-Code nach ISO 3166-1 zurück.
	 *
	 * @param code   der ISO-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static getByISO2(code : string | null) : Nationalitaeten | null {
		return Nationalitaeten.getMapISO2().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen DESTATIS-Code zurück.
	 *
	 * @param code   der DESTATIS-Code
	 *
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static getByDESTATIS(code : string | null) : Nationalitaeten | null {
		return Nationalitaeten.getMapDESTATIS().get(code);
	}

	/**
	 * Gibt die Nationalität DEU zurück.
	 *
	 * @return die Nationalität DEU
	 */
	public static getDEU() : Nationalitaeten {
		const deu : Nationalitaeten | null = Nationalitaeten.getByISO3("DEU");
		if (deu === null)
			throw new CoreTypeException("Core-Type nicht korrekt initialisiert. DEU kann nicht gefunden werden.")
		return deu;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Nationalitaeten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Nationalitaeten', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<Nationalitaeten>('de.svws_nrw.asd.types.schule.Nationalitaeten');

}

export function cast_de_svws_nrw_asd_types_schule_Nationalitaeten(obj : unknown) : Nationalitaeten {
	return obj as Nationalitaeten;
}
