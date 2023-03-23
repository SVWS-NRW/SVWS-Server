import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { KAOAEbene4Eintrag } from '../../../core/data/kaoa/KAOAEbene4Eintrag';
import { KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class KAOAEbene4 extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KAOAEbene4> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, KAOAEbene4> = new Map<string, KAOAEbene4>();

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 1-tägig
	 */
	public static readonly SBO_6_5_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_6_5_1_1", 0, [new KAOAEbene4Eintrag(1, "SBO 6.5.1.1", "Langzeitpraktikum 1-tägig", KAOAZusatzmerkmal.SBO_6_5_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 2-tägig
	 */
	public static readonly SBO_6_5_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_6_5_1_2", 1, [new KAOAEbene4Eintrag(2, "SBO 6.5.1.2", "Langzeitpraktikum 2-tägig", KAOAZusatzmerkmal.SBO_6_5_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_7_1_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_1", 2, [new KAOAEbene4Eintrag(3, "SBO 7.1.1.1", "An der KAoA-kompakt Potenzialanalyse teilgenommen", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an der KAoA-kompakt Potenzialanalyse
	 */
	public static readonly SBO_7_1_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_2", 3, [new KAOAEbene4Eintrag(4, "SBO 7.1.1.2", "Keine Teilnahme an der KAoA-kompakt Potenzialanalyse", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_7_1_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_3", 4, [new KAOAEbene4Eintrag(5, "SBO 7.1.1.3", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag ", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_7_1_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_4", 5, [new KAOAEbene4Eintrag(6, "SBO 7.1.1.4", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag ", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_7_1_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_5", 6, [new KAOAEbene4Eintrag(7, "SBO 7.1.1.5", "An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag ", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen
	 */
	public static readonly SBO_7_1_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_6", 7, [new KAOAEbene4Eintrag(8, "SBO 7.1.1.6", "Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An KAoA-kompakt Praxiskursen teilgenommen
	 */
	public static readonly SBO_7_1_1_7 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_7", 8, [new KAOAEbene4Eintrag(9, "SBO 7.1.1.7", "An KAoA-kompakt Praxiskursen teilgenommen", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Praxiskursen
	 */
	public static readonly SBO_7_1_1_8 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_8", 9, [new KAOAEbene4Eintrag(10, "SBO 7.1.1.8", "Keine Teilnahme an den KAoA-kompakt Praxiskursen", KAOAZusatzmerkmal.SBO_7_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Einzeltag
	 */
	public static readonly SBO_9_1_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_1", 10, [new KAOAEbene4Eintrag(11, "SBO 9.1.1.1", "Einzeltag", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Betriebspraktikum
	 */
	public static readonly SBO_9_1_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_2", 11, [new KAOAEbene4Eintrag(12, "SBO 9.1.1.2", "Betriebspraktikum", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Auslandspraktikum
	 */
	public static readonly SBO_9_1_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_3", 12, [new KAOAEbene4Eintrag(13, "SBO 9.1.1.3", "Auslandspraktikum", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Hochschulpraktikum/Schnupperstudium
	 */
	public static readonly SBO_9_1_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_4", 13, [new KAOAEbene4Eintrag(14, "SBO 9.1.1.4", "Hochschulpraktikum/Schnupperstudium", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Duales Orientierungspraktikum
	 */
	public static readonly SBO_9_1_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_5", 14, [new KAOAEbene4Eintrag(15, "SBO 9.1.1.5", "Duales Orientierungspraktikum", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Projektworkshop ( bei einem Bildungsträger)
	 */
	public static readonly SBO_9_1_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_6", 15, [new KAOAEbene4Eintrag(16, "SBO 9.1.1.6", "Projektworkshop ( bei einem Bildungsträger)", KAOAZusatzmerkmal.SBO_9_1_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule
	 */
	public static readonly SBO_9_2_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_1", 16, [new KAOAEbene4Eintrag(17, "SBO 9.2.1.1", "Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung in der Schule
	 */
	public static readonly SBO_9_2_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_2", 17, [new KAOAEbene4Eintrag(18, "SBO 9.2.1.2", "Veranstaltungen zur allgemeinen Studienorientierung in der Schule", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Wochen der Studienorientierung
	 */
	public static readonly SBO_9_2_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_3", 18, [new KAOAEbene4Eintrag(19, "SBO 9.2.1.3", "Wochen der Studienorientierung", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langer Abend der Studienberatung
	 */
	public static readonly SBO_9_2_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_4", 19, [new KAOAEbene4Eintrag(20, "SBO 9.2.1.4", "Langer Abend der Studienberatung", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen
	 */
	public static readonly SBO_9_2_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_5", 20, [new KAOAEbene4Eintrag(21, "SBO 9.2.1.5", "Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung
	 */
	public static readonly SBO_9_2_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_6", 21, [new KAOAEbene4Eintrag(22, "SBO 9.2.1.6", "Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Besondere Formate für Schlerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote)
	 */
	public static readonly SBO_9_2_1_7 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_7", 22, [new KAOAEbene4Eintrag(23, "SBO 9.2.1.7", "Besondere Formate für Schlerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote)", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote)
	 */
	public static readonly SBO_9_2_1_8 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_8", 23, [new KAOAEbene4Eintrag(24, "SBO 9.2.1.8", "Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote)", KAOAZusatzmerkmal.SBO_9_2_1, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Die aktuellsten Daten des KAoA-Eintrags der SBO Ebene 4
	 */
	public readonly daten : KAOAEbene4Eintrag;

	/**
	 * Die Historie mit den Einträgen des KAoA-Eintrags der SBO Ebene 4
	 */
	public readonly historie : Array<KAOAEbene4Eintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, KAOAEbene4> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOAEbene4> = new HashMap();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAEbene4Eintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOAEbene4Eintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KAOAEbene4.all_values_by_ordinal.push(this);
		KAOAEbene4.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf den zugehörigen Eintrag der SBO Ebene 4 zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf den zugehörigen Eintrag der SBO Ebene 4
	 */
	private static getMapStatusByID() : HashMap<number, KAOAEbene4> {
		if (KAOAEbene4._statusByID.size() === 0)
			for (let g of KAOAEbene4.values())
				KAOAEbene4._statusByID.put(g.daten.id, g);
		return KAOAEbene4._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf den zugehörigen Eintrag der SBO Ebene 4 zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf den zugehörigen Eintrag der SBO Ebene 4
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOAEbene4> {
		if (KAOAEbene4._statusByKuerzel.size() === 0)
			for (let g of KAOAEbene4.values())
				KAOAEbene4._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOAEbene4._statusByKuerzel;
	}

	/**
	 * Gibt den Eintrag der SBO Ebene 4 anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Eintrags der SBO Ebene 4
	 *
	 * @return der Eintrag der SBO Ebene 4 oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : KAOAEbene4 | null {
		return KAOAEbene4.getMapStatusByID().get(id);
	}

	/**
	 * Gibt den Eintrag der SBO Ebene 4 anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Eintrags der SBO Ebene 4
	 *
	 * @return der Eintrag der SBO Ebene 4 oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOAEbene4 | null {
		return KAOAEbene4.getMapStatusByKuerzel().get(kuerzel);
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
		if (!(other instanceof KAOAEbene4))
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
	public compareTo(other : KAOAEbene4) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAEbene4> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAEbene4 | null {
		const tmp : KAOAEbene4 | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.kaoa.KAOAEbene4'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_kaoa_KAOAEbene4(obj : unknown) : KAOAEbene4 {
	return obj as KAOAEbene4;
}
