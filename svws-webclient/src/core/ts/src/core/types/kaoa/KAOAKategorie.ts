import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Jahrgaenge, cast_de_nrw_schule_svws_core_types_jahrgang_Jahrgaenge } from '../../../core/types/jahrgang/Jahrgaenge';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { KAOAKategorieEintrag, cast_de_nrw_schule_svws_core_data_kaoa_KAOAKategorieEintrag } from '../../../core/data/kaoa/KAOAKategorieEintrag';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';

export class KAOAKategorie extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KAOAKategorie> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, KAOAKategorie> = new Map<string, KAOAKategorie>();

	/**
	 * KAoA-Kategorie: Formen der Orientierung und Beratung
	 */
	public static readonly SBO_2 : KAOAKategorie = new KAOAKategorie("SBO_2", 0, [new KAOAKategorieEintrag(6, "SBO 2", "Formen der Orientierung und Beratung", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09, Jahrgaenge.JG_10, Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)]);

	/**
	 * KAoA-Kategorie: Strukturen an Schulen
	 */
	public static readonly SBO_3 : KAOAKategorie = new KAOAKategorie("SBO_3", 1, [new KAOAKategorieEintrag(7, "SBO 3", "Strukturen an Schulen", Arrays.asList(Jahrgaenge.JG_00), null, null)]);

	/**
	 * KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen
	 */
	public static readonly SBO_4 : KAOAKategorie = new KAOAKategorie("SBO_4", 2, [new KAOAKategorieEintrag(8, "SBO 4", "Potenziale entdecken und den eigenen Standort bestimmen", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09), null, null)]);

	/**
	 * KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln
	 */
	public static readonly SBO_5 : KAOAKategorie = new KAOAKategorie("SBO_5", 3, [new KAOAKategorieEintrag(9, "SBO 5", "Berufsfelder erkunden und Informationen sammeln", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09), null, null)]);

	/**
	 * KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben
	 */
	public static readonly SBO_6 : KAOAKategorie = new KAOAKategorie("SBO_6", 4, [new KAOAKategorieEintrag(10, "SBO 6", "Praxis der Arbeitswelt kennenlernen und erproben", Arrays.asList(Jahrgaenge.JG_08, Jahrgaenge.JG_09, Jahrgaenge.JG_10), null, null)]);

	/**
	 * KAoA-Kategorie: Nachholung der Erstberufsorientierung
	 */
	public static readonly SBO_7 : KAOAKategorie = new KAOAKategorie("SBO_7", 5, [new KAOAKategorieEintrag(11, "SBO 7", "Nachholung der Erstberufsorientierung", Arrays.asList(Jahrgaenge.JG_10), null, null)]);

	/**
	 * KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen
	 */
	public static readonly SBO_8 : KAOAKategorie = new KAOAKategorie("SBO_8", 6, [new KAOAKategorieEintrag(12, "SBO 8", "Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen", Arrays.asList(Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)]);

	/**
	 * KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren
	 */
	public static readonly SBO_9 : KAOAKategorie = new KAOAKategorie("SBO_9", 7, [new KAOAKategorieEintrag(13, "SBO 9", "Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren", Arrays.asList(Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)]);

	/**
	 * KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II
	 */
	public static readonly SBO_10 : KAOAKategorie = new KAOAKategorie("SBO_10", 8, [new KAOAKategorieEintrag(14, "SBO 10", "Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II", Arrays.asList(Jahrgaenge.JG_09, Jahrgaenge.JG_10, Jahrgaenge.JG_EF, Jahrgaenge.JG_Q1, Jahrgaenge.JG_Q2), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Die aktuellsten Daten der KAoA-Kategorie
	 */
	public readonly daten : KAOAKategorieEintrag;

	/**
	 * Die Historie mit den Einträgen der KAoA-Kategorie
	 */
	public readonly historie : Array<KAOAKategorieEintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, KAOAKategorie> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAKategorie> = new HashMap();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAKategorieEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAKategorieEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KAOAKategorie.all_values_by_ordinal.push(this);
		KAOAKategorie.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf die zugehörige Kategorie zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf die zugehörige Kategorie
	 */
	private static getMapStatusByID() : HashMap<number, KAOAKategorie> {
		if (KAOAKategorie._statusByID.size() === 0) 
			for (let g of KAOAKategorie.values()) 
				KAOAKategorie._statusByID.put(g.daten.id, g);
		return KAOAKategorie._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf die zugehörige Kategorie zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf die zugehörige Kategorie
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOAKategorie> {
		if (KAOAKategorie._statusByKuerzel.size() === 0) 
			for (let g of KAOAKategorie.values()) 
				KAOAKategorie._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOAKategorie._statusByKuerzel;
	}

	/**
	 * Gibt die Kategorie anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Kategorie
	 * 
	 * @return die Kategorie oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : KAOAKategorie | null {
		return KAOAKategorie.getMapStatusByID().get(id);
	}

	/**
	 * Gibt die Kategorie anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Kategorie
	 * 
	 * @return die Kategorie oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAKategorie | null {
		return KAOAKategorie.getMapStatusByKuerzel().get(kuerzel);
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
		if (!(other instanceof KAOAKategorie))
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
	public compareTo(other : KAOAKategorie) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAKategorie> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAKategorie | null {
		let tmp : KAOAKategorie | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.kaoa.KAOAKategorie'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_kaoa_KAOAKategorie(obj : unknown) : KAOAKategorie {
	return obj as KAOAKategorie;
}
