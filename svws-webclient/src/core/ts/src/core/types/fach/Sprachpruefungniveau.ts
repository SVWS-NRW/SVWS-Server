import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SprachpruefungsniveauKatalogEintrag, cast_de_nrw_schule_svws_core_data_fach_SprachpruefungsniveauKatalogEintrag } from '../../../core/data/fach/SprachpruefungsniveauKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachpruefungniveau extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Sprachpruefungniveau> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Sprachpruefungniveau> = new Map<string, Sprachpruefungniveau>();

	/**
	 * Prüfungsniveau angelehnt an 'HA9'
	 */
	public static readonly HA9 : Sprachpruefungniveau = new Sprachpruefungniveau("HA9", 0, [new SprachpruefungsniveauKatalogEintrag(1, "NIVEAU_HA9", "Hauptschulabschluss nach Klasse 9", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'HA10'
	 */
	public static readonly HA10 : Sprachpruefungniveau = new Sprachpruefungniveau("HA10", 1, [new SprachpruefungsniveauKatalogEintrag(2, "NIVEAU_HA10", "Hauptschulabschluss nach Klasse 10", null, null)]);

	/**
	 * Prüfungsniveau angelehnt an 'MSA'
	 */
	public static readonly MSA : Sprachpruefungniveau = new Sprachpruefungniveau("MSA", 2, [new SprachpruefungsniveauKatalogEintrag(3, "NIVEAU_MSA", "Mittlerer Schulabschluss (Fachoberschulreife) / Berechtigung zum Besuch der gymnasialen Oberstufe (Gymnasium G8 Klasse 9)", null, null)]);

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
	public static VERSION : number = 1;

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
		super();
		this.__name = name;
		this.__ordinal = ordinal;
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
			for (let l of Sprachpruefungniveau.values()) 
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
			for (let l of Sprachpruefungniveau.values()) 
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
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof Sprachpruefungniveau))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : Sprachpruefungniveau) : number {
		return this.__ordinal - other.__ordinal;
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
		let tmp : Sprachpruefungniveau | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.fach.Sprachpruefungniveau'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_fach_Sprachpruefungniveau(obj : unknown) : Sprachpruefungniveau {
	return obj as Sprachpruefungniveau;
}
