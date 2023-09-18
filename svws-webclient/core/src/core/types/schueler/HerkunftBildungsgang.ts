import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HerkunftBildungsgangKatalogEintrag } from '../../../core/data/schule/HerkunftBildungsgangKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';

export class HerkunftBildungsgang extends JavaObject implements JavaEnum<HerkunftBildungsgang> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<HerkunftBildungsgang> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, HerkunftBildungsgang> = new Map<string, HerkunftBildungsgang>();

	/**
	 * Berufsschule, Fachklassen (Teilzeit)
	 */
	public static readonly A01 : HerkunftBildungsgang = new HerkunftBildungsgang("A01", 0, [new HerkunftBildungsgangKatalogEintrag(1000, Schulgliederung.A01, null, null)]);

	/**
	 * Berufsschule, Fachklassen/Fachhochschulreife (Teilzeit)
	 */
	public static readonly A02 : HerkunftBildungsgang = new HerkunftBildungsgang("A02", 1, [new HerkunftBildungsgangKatalogEintrag(2000, Schulgliederung.A02, null, null)]);

	/**
	 * Berufsschule, Fachklassen/erweiterte Zusatzqualifikation (Teilzeit)
	 */
	public static readonly A03 : HerkunftBildungsgang = new HerkunftBildungsgang("A03", 2, [new HerkunftBildungsgangKatalogEintrag(3000, Schulgliederung.A03, null, null)]);

	/**
	 * Berufsschule, Fachklassen mit erweitertem Stützunterricht (Teilzeit)
	 */
	public static readonly A04 : HerkunftBildungsgang = new HerkunftBildungsgang("A04", 3, [new HerkunftBildungsgangKatalogEintrag(4000, Schulgliederung.A04, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/mittlerer Schulabschluss (nach BKAZVO, BBiG/HwO/ in Vollzeit)
	 */
	public static readonly A10 : HerkunftBildungsgang = new HerkunftBildungsgang("A10", 4, [new HerkunftBildungsgangKatalogEintrag(5000, Schulgliederung.A10, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit)
	 */
	public static readonly A11 : HerkunftBildungsgang = new HerkunftBildungsgang("A11", 5, [new HerkunftBildungsgangKatalogEintrag(6000, Schulgliederung.A11, null, null)]);

	/**
	 * Berufsschule, Ausbildungsvorbereitung (1-jährig, Vollzeit)
	 */
	public static readonly A12 : HerkunftBildungsgang = new HerkunftBildungsgang("A12", 6, [new HerkunftBildungsgangKatalogEintrag(7000, Schulgliederung.A12, null, null)]);

	/**
	 * Berufsschule, Ausbildungsvorbereitung (1-jährig, Teilzeit)
	 */
	public static readonly A13 : HerkunftBildungsgang = new HerkunftBildungsgang("A13", 7, [new HerkunftBildungsgangKatalogEintrag(8000, Schulgliederung.A13, null, null)]);

	/**
	 * Berufsabschluss (nach §50 BBiG/§40 HwO)/Mittlerer Schulabschluss
	 */
	public static readonly A14 : HerkunftBildungsgang = new HerkunftBildungsgang("A14", 8, [new HerkunftBildungsgangKatalogEintrag(9000, Schulgliederung.A14, null, null)]);

	/**
	 * Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife
	 */
	public static readonly A15 : HerkunftBildungsgang = new HerkunftBildungsgang("A15", 9, [new HerkunftBildungsgangKatalogEintrag(10000, Schulgliederung.A15, null, null)]);

	/**
	 * Fachklassen (nach §2 BKAZVO)
	 */
	public static readonly A16 : HerkunftBildungsgang = new HerkunftBildungsgang("A16", 10, [new HerkunftBildungsgangKatalogEintrag(11000, Schulgliederung.A16, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Fachoberschulreife (2-jährig, Vollzeit)
	 */
	public static readonly B01 : HerkunftBildungsgang = new HerkunftBildungsgang("B01", 11, [new HerkunftBildungsgangKatalogEintrag(20000, Schulgliederung.B01, null, null)]);

	/**
	 * Berufsfachschule, Berufsgrundbildung/Fachoberschulreife (2-jährig, Vollzeit)
	 */
	public static readonly B02 : HerkunftBildungsgang = new HerkunftBildungsgang("B02", 12, [new HerkunftBildungsgangKatalogEintrag(21000, Schulgliederung.B02, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Fachoberschulreife (nach BKAZVO, BBiG/HwO, in Vollzeit)
	 */
	public static readonly B04 : HerkunftBildungsgang = new HerkunftBildungsgang("B04", 13, [new HerkunftBildungsgangKatalogEintrag(22000, Schulgliederung.B04, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit)
	 */
	public static readonly B05 : HerkunftBildungsgang = new HerkunftBildungsgang("B05", 14, [new HerkunftBildungsgangKatalogEintrag(23000, Schulgliederung.B05, null, null)]);

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/Erweiterter Erster Schulabschluss (1-jährig, Vollzeit)
	 */
	public static readonly B06 : HerkunftBildungsgang = new HerkunftBildungsgang("B06", 15, [new HerkunftBildungsgangKatalogEintrag(24000, Schulgliederung.B06, null, null)]);

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/Mittlerer Schulabschluss (1-jährig, Vollzeit)
	 */
	public static readonly B07 : HerkunftBildungsgang = new HerkunftBildungsgang("B07", 16, [new HerkunftBildungsgangKatalogEintrag(25000, Schulgliederung.B07, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (2-jährig, Vollzeit)
	 */
	public static readonly B08 : HerkunftBildungsgang = new HerkunftBildungsgang("B08", 17, [new HerkunftBildungsgangKatalogEintrag(26000, Schulgliederung.B08, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (3-jährig, Teilzeit)
	 */
	public static readonly B09 : HerkunftBildungsgang = new HerkunftBildungsgang("B09", 18, [new HerkunftBildungsgangKatalogEintrag(27000, Schulgliederung.B09, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (4-jährig, Teilzeit)
	 */
	public static readonly B10 : HerkunftBildungsgang = new HerkunftBildungsgang("B10", 19, [new HerkunftBildungsgangKatalogEintrag(28000, Schulgliederung.B10, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum, 3-jährig, Vollzeit)
	 */
	public static readonly C01 : HerkunftBildungsgang = new HerkunftBildungsgang("C01", 20, [new HerkunftBildungsgangKatalogEintrag(40000, Schulgliederung.C01, null, null)]);

	/**
	 * Berufsfachschule, Berufsabschluss (2-jährig, Vollzeit)
	 */
	public static readonly C02 : HerkunftBildungsgang = new HerkunftBildungsgang("C02", 21, [new HerkunftBildungsgangKatalogEintrag(41000, Schulgliederung.C02, null, null)]);

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/FHR (HBFS) (2-jährig, Vollzeit)
	 */
	public static readonly C03 : HerkunftBildungsgang = new HerkunftBildungsgang("C03", 22, [new HerkunftBildungsgangKatalogEintrag(42000, Schulgliederung.C03, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 11 (1-jährig, Teilzeit)
	 */
	public static readonly C05 : HerkunftBildungsgang = new HerkunftBildungsgang("C05", 23, [new HerkunftBildungsgangKatalogEintrag(43000, Schulgliederung.C05, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 12S (1-jährig, Vollzeit)
	 */
	public static readonly C06 : HerkunftBildungsgang = new HerkunftBildungsgang("C06", 24, [new HerkunftBildungsgangKatalogEintrag(44000, Schulgliederung.C06, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (2-jährig, Teilzeit)
	 */
	public static readonly C07 : HerkunftBildungsgang = new HerkunftBildungsgang("C07", 25, [new HerkunftBildungsgangKatalogEintrag(45000, Schulgliederung.C07, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (1-jährig, Vollzeit)
	 */
	public static readonly C08 : HerkunftBildungsgang = new HerkunftBildungsgang("C08", 26, [new HerkunftBildungsgangKatalogEintrag(46000, Schulgliederung.C08, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (3-jährig, Teilzeit)
	 */
	public static readonly C11 : HerkunftBildungsgang = new HerkunftBildungsgang("C11", 27, [new HerkunftBildungsgangKatalogEintrag(47000, Schulgliederung.C11, null, null)]);

	/**
	 * 3,5 -jährig, Vollzeit)"
	 */
	public static readonly C12 : HerkunftBildungsgang = new HerkunftBildungsgang("C12", 28, [new HerkunftBildungsgangKatalogEintrag(48000, Schulgliederung.C12, null, null)]);

	/**
	 * Berufsabschluss/FHR (gestuft), (3jährig, Vollzeit)
	 */
	public static readonly C13 : HerkunftBildungsgang = new HerkunftBildungsgang("C13", 29, [new HerkunftBildungsgangKatalogEintrag(49000, Schulgliederung.C13, null, null)]);

	/**
	 * 4-jährig, Vollzeit)"
	 */
	public static readonly D01 : HerkunftBildungsgang = new HerkunftBildungsgang("D01", 30, [new HerkunftBildungsgangKatalogEintrag(60000, Schulgliederung.D01, null, null)]);

	/**
	 * Berufliches Gymnasium, Berufliche Kenntnisse/Allg. Hochschulreife
	 */
	public static readonly D02 : HerkunftBildungsgang = new HerkunftBildungsgang("D02", 31, [new HerkunftBildungsgangKatalogEintrag(61000, Schulgliederung.D02, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 13 (1-jährig, Vollzeit)
	 */
	public static readonly D05 : HerkunftBildungsgang = new HerkunftBildungsgang("D05", 32, [new HerkunftBildungsgangKatalogEintrag(62000, Schulgliederung.D05, null, null)]);

	/**
	 * Fachoberschule, Fachoberschule Kl. 13 (2-jährig, Teilzeit)
	 */
	public static readonly D06 : HerkunftBildungsgang = new HerkunftBildungsgang("D06", 33, [new HerkunftBildungsgangKatalogEintrag(63000, Schulgliederung.D06, null, null)]);

	/**
	 * Fachschule (2-jährig, Vollzeit)
	 */
	public static readonly E01 : HerkunftBildungsgang = new HerkunftBildungsgang("E01", 34, [new HerkunftBildungsgangKatalogEintrag(80000, Schulgliederung.E01, null, null)]);

	/**
	 * Fachschule (4-jährig, Teilzeit)
	 */
	public static readonly E02 : HerkunftBildungsgang = new HerkunftBildungsgang("E02", 35, [new HerkunftBildungsgangKatalogEintrag(81000, Schulgliederung.E02, null, null)]);

	/**
	 * Fachschule (verkürzt/1-jährig, Vollzeit/Teilzeit)
	 */
	public static readonly E03 : HerkunftBildungsgang = new HerkunftBildungsgang("E03", 36, [new HerkunftBildungsgangKatalogEintrag(82000, Schulgliederung.E03, null, null)]);

	/**
	 * Fachschule (verkürzt/2-jährig, Teilzeit)
	 */
	public static readonly E04 : HerkunftBildungsgang = new HerkunftBildungsgang("E04", 37, [new HerkunftBildungsgangKatalogEintrag(83000, Schulgliederung.E04, null, null)]);

	/**
	 * Fachschule für Sozialwesen (mit Berufspraktikum/3-jährig, Vollzeit)
	 */
	public static readonly E05 : HerkunftBildungsgang = new HerkunftBildungsgang("E05", 38, [new HerkunftBildungsgangKatalogEintrag(84000, Schulgliederung.E05, null, null)]);

	/**
	 * Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit)
	 */
	public static readonly E07 : HerkunftBildungsgang = new HerkunftBildungsgang("E07", 39, [new HerkunftBildungsgangKatalogEintrag(85000, Schulgliederung.E07, null, null)]);

	/**
	 * Fachschule (3-jährig, Teilzeit)
	 */
	public static readonly E13 : HerkunftBildungsgang = new HerkunftBildungsgang("E13", 40, [new HerkunftBildungsgangKatalogEintrag(86000, Schulgliederung.E13, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Herkunftsbildungsganges, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftBildungsgangKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Herkunftsbildungsganges
	 */
	public readonly historie : Array<HerkunftBildungsgangKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Herkunftsbildungsgängen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, HerkunftBildungsgang | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Herkunftsbildungsgang in der Aufzählung.
	 *
	 * @param historie   die Historie des Herkunftsbildungsganges, welche ein Array von
	 *                   {@link HerkunftBildungsgangKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftBildungsgangKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		HerkunftBildungsgang.all_values_by_ordinal.push(this);
		HerkunftBildungsgang.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsbildungsgänge auf die
	 * zugehörigen Herkunftsbildungsgänge zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Herkunftsbildungsgänge
	 */
	private static getMapByKuerzel() : HashMap<string, HerkunftBildungsgang | null> {
		if (HerkunftBildungsgang._ebenen.size() === 0) {
			for (const s of HerkunftBildungsgang.values()) {
				if (s.daten !== null)
					HerkunftBildungsgang._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return HerkunftBildungsgang._ebenen;
	}

	/**
	 * Gibt den Herkunftsbildungsgang für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Herkunftsbildungsganges
	 *
	 * @return der Herkunftsbildungsgang oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : HerkunftBildungsgang | null {
		return HerkunftBildungsgang.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof HerkunftBildungsgang))
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
	public compareTo(other : HerkunftBildungsgang) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftBildungsgang> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftBildungsgang | null {
		const tmp : HerkunftBildungsgang | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.HerkunftBildungsgang', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgang(obj : unknown) : HerkunftBildungsgang {
	return obj as HerkunftBildungsgang;
}
