import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachpruefungniveau extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Sprachpruefungniveau> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, Sprachpruefungniveau> = new Map<String, Sprachpruefungniveau>();

	public static readonly NIVEAU_HA9 : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_HA9", 0, 1, "NIVEAU_HA9", "Hauptschulabschluss nach Klasse 9", null, null);

	public static readonly NIVEAU_HA10 : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_HA10", 1, 2, "NIVEAU_HA10", "Hauptschulabschluss nach Klasse 10", null, null);

	public static readonly NIVEAU_MSA : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_MSA", 2, 3, "NIVEAU_MSA", "Mittlerer Schulabschluss (Fachoberschulreife) / Berechtigung zum Besuch der gymnasialen Oberstufe (Gymnasium G8 Klasse 9)", null, null);

	public static readonly NIVEAU_EF : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_EF", 3, 4, "NIVEAU_EF", "Ende der Einführungsphase der gymnasialen Oberstufe in einer fortgeführten Fremdsprache (Gymnasium und Gesamtschule)", null, null);

	public static readonly NIVEAU_FHR : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_FHR", 4, 5, "NIVEAU_FHR", "Fachhochschulreife (Abschluss an berufsbildenden Schulen)", null, null);

	public static readonly NIVEAU_WBK_FF : Sprachpruefungniveau = new Sprachpruefungniveau("NIVEAU_WBK_FF", 5, 6, "NIVEAU_WBK_FF", "Fortgeführte Fremdsprache gemäß § 34 Abs. 4 APO-WbK (nur zweite Pflichtfremdsprache)", null, null);

	private static _mapID : HashMap<Number, Sprachpruefungniveau> = new HashMap();

	private static _mapBezeichnung : HashMap<String, Sprachpruefungniveau> = new HashMap();

	public readonly id : number;

	public readonly bezeichnung : String;

	public readonly beschreibung : String;

	public readonly gueltigVon : Number | null;

	public readonly gueltigBis : Number | null;

	/**
	 * Erstellt ein neues Prüfungsniveau dieser Aufzählung.
	 *
	 * @param id            die ID des Prüfungsniveaus
	 * @param bezeichnung   die Bezeichnung des Prüfungsniveaus
	 * @param beschreibung  die Beschreibung des Prüfungsniveaus
	 * @param gueltigVon	Schuljahr, ab dem das Niveau gültig ist
	 * @param gueltigBis	Schuljahr, bis zu dem das Niveau gültig war
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : String, beschreibung : String, gueltigVon : Number | null, gueltigBis : Number | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Sprachpruefungniveau.all_values_by_ordinal.push(this);
		Sprachpruefungniveau.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt eine Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static getMapSprachpruefungniveauByID() : HashMap<Number, Sprachpruefungniveau> {
		if (Sprachpruefungniveau._mapID.size() === 0) 
			for (let l of Sprachpruefungniveau.values()) 
				Sprachpruefungniveau._mapID.put(l.id, l);
		return Sprachpruefungniveau._mapID;
	}

	/**
	 * Gibt eine Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Bezeichnungen der Sprachprüfungsniveaus auf die zugehörigen Sprachprüfungsniveaus
	 */
	private static getMapSprachpruefungniveauByBezeichnung() : HashMap<String, Sprachpruefungniveau> {
		if (Sprachpruefungniveau._mapBezeichnung.size() === 0) 
			for (let l of Sprachpruefungniveau.values()) 
				Sprachpruefungniveau._mapBezeichnung.put(l.bezeichnung, l);
		return Sprachpruefungniveau._mapBezeichnung;
	}

	/**
	 * Gibt das Prüfungsniveau für die übergebene ID zurück.
	 *
	 * @param id   die ID des Prüfungsniveaus
	 *
	 * @return das Prüfungsniveaus oder null, wenn die ID ungültig ist
	 */
	public static getByID(id : Number | null) : Sprachpruefungniveau | null {
		return Sprachpruefungniveau.getMapSprachpruefungniveauByID().get(id);
	}

	/**
	 * Gibt das Prüfungsniveau für die übergebene Bezeichnung zurück.
	 * 
	 * @param bezeichnung   die Bezeichnung des Prüfungsniveaus
	 * 
	 * @return das Prüfungsniveaus oder null, wenn die Bezeichnung ungültig ist
	 */
	public static getByBezeichnung(bezeichnung : String | null) : Sprachpruefungniveau | null {
		return Sprachpruefungniveau.getMapSprachpruefungniveauByBezeichnung().get(bezeichnung);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
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
	public toString() : String {
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
	public static valueOf(name : String) : Sprachpruefungniveau | null {
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
