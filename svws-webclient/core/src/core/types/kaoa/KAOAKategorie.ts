import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Jahrgaenge } from '../../../core/types/jahrgang/Jahrgaenge';
import { HashMap } from '../../../java/util/HashMap';
import { KAOAKategorieEintrag } from '../../../core/data/kaoa/KAOAKategorieEintrag';
import { Arrays } from '../../../java/util/Arrays';

export class KAOAKategorie extends JavaEnum<KAOAKategorie> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAKategorie> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAKategorie> = new Map<string, KAOAKategorie>();

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
	public static readonly VERSION : number = 1;

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
	private static readonly _statusByID : HashMap<number, KAOAKategorie> = new HashMap<number, KAOAKategorie>();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAKategorie> = new HashMap<string, KAOAKategorie>();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAKategorieEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAKategorieEintrag>) {
		super(name, ordinal);
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
			for (const g of KAOAKategorie.values())
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
			for (const g of KAOAKategorie.values())
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
	public static getByID(id : number | null) : KAOAKategorie | null {
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kaoa.KAOAKategorie';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOAKategorie', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOAKategorie(obj : unknown) : KAOAKategorie {
	return obj as KAOAKategorie;
}
