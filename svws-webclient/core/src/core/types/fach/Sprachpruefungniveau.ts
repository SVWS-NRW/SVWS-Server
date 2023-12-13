import { JavaEnum } from '../../../java/lang/JavaEnum';
import { SprachpruefungsniveauKatalogEintrag } from '../../../core/data/fach/SprachpruefungsniveauKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class Sprachpruefungniveau extends JavaEnum<Sprachpruefungniveau> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Sprachpruefungniveau> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Sprachpruefungniveau> = new Map<string, Sprachpruefungniveau>();

	/**
	 * Prüfungsniveau angelehnt an 'HA9'
	 */
	public static readonly ESA : Sprachpruefungniveau = new Sprachpruefungniveau("ESA", 0, [new SprachpruefungsniveauKatalogEintrag(1, "NIVEAU_ESA", "Erster Schulabschluss", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'HA10'
	 */
	public static readonly EESA : Sprachpruefungniveau = new Sprachpruefungniveau("EESA", 1, [new SprachpruefungsniveauKatalogEintrag(2, "NIVEAU_EESA", "Erweiterter Erster Schulabschluss", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'MSA'
	 */
	public static readonly MSA : Sprachpruefungniveau = new Sprachpruefungniveau("MSA", 2, [new SprachpruefungsniveauKatalogEintrag(3, "NIVEAU_MSA", "Mittlerer Schulabschluss / Berechtigung zum Besuch der gymnasialen Oberstufe (Gymnasium G8 Klasse 9)", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'EF'
	 */
	public static readonly EF : Sprachpruefungniveau = new Sprachpruefungniveau("EF", 3, [new SprachpruefungsniveauKatalogEintrag(4, "NIVEAU_EF", "Ende der Einführungsphase der gymnasialen Oberstufe in einer fortgeführten Fremdsprache (Gymnasium und Gesamtschule)", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'FHR'
	 */
	public static readonly FHR : Sprachpruefungniveau = new Sprachpruefungniveau("FHR", 4, [new SprachpruefungsniveauKatalogEintrag(5, "NIVEAU_FHR", "Fachhochschulreife (Abschluss an berufsbildenden Schulen)", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'WBK_FF'
	 */
	public static readonly WBK_FF : Sprachpruefungniveau = new Sprachpruefungniveau("WBK_FF", 5, [new SprachpruefungsniveauKatalogEintrag(6, "NIVEAU_WBK_FF", "Fortgeführte Fremdsprache gemäß § 34 Abs. 4 APO-WbK (nur zweite Pflichtfremdsprache)", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Sprachprüfungsniveaus
	 */
	public readonly daten : SprachpruefungsniveauKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Sprachprüfungsniveaus
	 */
	public readonly historie : Array<SprachpruefungsniveauKatalogEintrag>;

	/**
	 * Die Zuordnung der Sprachreferenzniveaus zu ihren IDs
	 */
	private static readonly _mapID : HashMap<number, Sprachpruefungniveau> = new HashMap();

	/**
	 * Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen
	 */
	private static readonly _mapKuerzel : HashMap<string, Sprachpruefungniveau> = new HashMap();

	/**
	 * Erstellt ein neues Prüfungsniveau dieser Aufzählung.
	 *
	 * @param historie   die Historie des Sprachreferenzniveaus, welche ein Array von
	 *                   {@link SprachpruefungsniveauKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<SprachpruefungsniveauKatalogEintrag>) {
		super(name, ordinal);
		Sprachpruefungniveau.all_values_by_ordinal.push(this);
		Sprachpruefungniveau.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static getMapByID() : HashMap<number, Sprachpruefungniveau> {
		if (Sprachpruefungniveau._mapID.size() === 0)
			for (const l of Sprachpruefungniveau.values())
				Sprachpruefungniveau._mapID.put(l.daten.id, l);
		return Sprachpruefungniveau._mapID;
	}

	/**
	 * Gibt eine Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static getMapByKuerzel() : HashMap<string, Sprachpruefungniveau> {
		if (Sprachpruefungniveau._mapKuerzel.size() === 0)
			for (const l of Sprachpruefungniveau.values())
				Sprachpruefungniveau._mapKuerzel.put(l.daten.kuerzel, l);
		return Sprachpruefungniveau._mapKuerzel;
	}

	/**
	 * Gibt das Prüfungsniveau für die übergebene ID zurück.
	 *
	 * @param id   die ID des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn die ID ungültig ist
	 */
	public static getByID(id : number | null) : Sprachpruefungniveau | null {
		return Sprachpruefungniveau.getMapByID().get(id);
	}

	/**
	 * Gibt das Prüfungsniveau für das übergebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Sprachpruefungniveau | null {
		return Sprachpruefungniveau.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Sprachpruefungniveau> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Sprachpruefungniveau | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.fach.Sprachpruefungniveau';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.Sprachpruefungniveau', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_Sprachpruefungniveau(obj : unknown) : Sprachpruefungniveau {
	return obj as Sprachpruefungniveau;
}
