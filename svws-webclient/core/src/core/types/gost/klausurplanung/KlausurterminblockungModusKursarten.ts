import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { HashMap } from '../../../../java/util/HashMap';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';

export class KlausurterminblockungModusKursarten extends JavaEnum<KlausurterminblockungModusKursarten> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KlausurterminblockungModusKursarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KlausurterminblockungModusKursarten> = new Map<string, KlausurterminblockungModusKursarten>();

	/**
	 * Dieser Modus blockt beide Kursarten (LK und GK) gemischt.
	 */
	public static readonly BEIDE : KlausurterminblockungModusKursarten = new KlausurterminblockungModusKursarten("BEIDE", 0, 0, "Gemischt");

	/**
	 * Dieser Modus blockt zuerst die Kursart LK, danach die Kursart GK.
	 */
	public static readonly GETRENNT : KlausurterminblockungModusKursarten = new KlausurterminblockungModusKursarten("GETRENNT", 1, 1, "Getrennt");

	/**
	 * Dieser Modus blockt nur die Kursart LK.
	 */
	public static readonly NUR_LK : KlausurterminblockungModusKursarten = new KlausurterminblockungModusKursarten("NUR_LK", 2, 2, "Nur LK");

	/**
	 * Dieser Modus blockt nur die Kursart GK.
	 */
	public static readonly NUR_GK : KlausurterminblockungModusKursarten = new KlausurterminblockungModusKursarten("NUR_GK", 3, 3, "Nur GK");

	/**
	 * Die ID
	 */
	public readonly id : number;

	/**
	 * Die textuelle Bezeichnung
	 */
	public readonly bezeichnung : string;

	/**
	 * Eine Map mit der Zuordnung zu der ID
	 */
	private static readonly _mapID : HashMap<number, KlausurterminblockungModusKursarten> = new HashMap();

	/**
	 * Erstellt einen neuen Modus.
	 *
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string) {
		super(name, ordinal);
		KlausurterminblockungModusKursarten.all_values_by_ordinal.push(this);
		KlausurterminblockungModusKursarten.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt die Map mit der Zuordnung zu der ID zurück. Sollte diese noch nicht
	 * initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map mit der Zuordnung zu der ID
	 */
	private static getMapByID() : HashMap<number, KlausurterminblockungModusKursarten> {
		if (KlausurterminblockungModusKursarten._mapID.size() === 0)
			for (const e of KlausurterminblockungModusKursarten.values())
				KlausurterminblockungModusKursarten._mapID.put(e.id, e);
		return KlausurterminblockungModusKursarten._mapID;
	}

	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus oder null, falls die ID ungültig ist
	 */
	public static get(id : number) : KlausurterminblockungModusKursarten | null {
		return KlausurterminblockungModusKursarten.getMapByID().get(id);
	}

	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus
	 *
	 * @throws DeveloperNotificationException falls die ID nicht definiert ist
	 */
	public static getOrException(id : number) : KlausurterminblockungModusKursarten {
		return DeveloperNotificationException.ifMapGetIsNull(KlausurterminblockungModusKursarten.getMapByID(), id);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KlausurterminblockungModusKursarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KlausurterminblockungModusKursarten | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_klausurplanung_KlausurterminblockungModusKursarten(obj : unknown) : KlausurterminblockungModusKursarten {
	return obj as KlausurterminblockungModusKursarten;
}
