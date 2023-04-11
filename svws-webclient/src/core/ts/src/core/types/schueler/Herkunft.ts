import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HerkunftKatalogEintrag } from '../../../core/data/schule/HerkunftKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { HerkunftBildungsgang, cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgang } from '../../../core/types/schueler/HerkunftBildungsgang';
import { HerkunftSchulform, cast_de_svws_nrw_core_types_schueler_HerkunftSchulform } from '../../../core/types/schueler/HerkunftSchulform';
import { HerkunftBildungsgangsTyp, cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgangsTyp } from '../../../core/types/schueler/HerkunftBildungsgangsTyp';
import { HerkunftSonstige, cast_de_svws_nrw_core_types_schueler_HerkunftSonstige } from '../../../core/types/schueler/HerkunftSonstige';

export class Herkunft extends JavaObject implements JavaEnum<Herkunft> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Herkunft> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Herkunft> = new Map<string, Herkunft>();

	/**
	 * Schulform: Berufskolleg
	 */
	public static readonly BK : Herkunft = new Herkunft("BK", 0, HerkunftSchulform.BK);

	/**
	 * Schulform: Freie Waldorfschule
	 */
	public static readonly FW : Herkunft = new Herkunft("FW", 1, HerkunftSchulform.FW);

	/**
	 * Schulform: Grundschule
	 */
	public static readonly G : Herkunft = new Herkunft("G", 2, HerkunftSchulform.G);

	/**
	 * Schulform: Gesamtschule
	 */
	public static readonly GE : Herkunft = new Herkunft("GE", 3, HerkunftSchulform.GE);

	/**
	 * Schulform: Gemeinschaftschule
	 */
	public static readonly GM : Herkunft = new Herkunft("GM", 4, HerkunftSchulform.GM);

	/**
	 * Schulform: Gymnasium
	 */
	public static readonly GY : Herkunft = new Herkunft("GY", 5, HerkunftSchulform.GY);

	/**
	 * Schulform: Hauptschule
	 */
	public static readonly H : Herkunft = new Herkunft("H", 6, HerkunftSchulform.H);

	/**
	 * Schulform: Schulversuch PRIMUS
	 */
	public static readonly PS : Herkunft = new Herkunft("PS", 7, HerkunftSchulform.PS);

	/**
	 * Schulform: Realschule
	 */
	public static readonly R : Herkunft = new Herkunft("R", 8, HerkunftSchulform.R);

	/**
	 * Schulform: Förderschule oder Klinikschule
	 */
	public static readonly S : Herkunft = new Herkunft("S", 9, HerkunftSchulform.S);

	/**
	 * Schulform: Sekundarschule
	 */
	public static readonly SE : Herkunft = new Herkunft("SE", 10, HerkunftSchulform.SK);

	/**
	 * Schulform: Weiterbildungskolleg
	 */
	public static readonly WB : Herkunft = new Herkunft("WB", 11, HerkunftSchulform.WB);

	/**
	 * Bildungsgang: A01
	 */
	public static readonly A01 : Herkunft = new Herkunft("A01", 12, HerkunftBildungsgang.A01);

	/**
	 * Bildungsgang: A02
	 */
	public static readonly A02 : Herkunft = new Herkunft("A02", 13, HerkunftBildungsgang.A02);

	/**
	 * Bildungsgang: A03
	 */
	public static readonly A03 : Herkunft = new Herkunft("A03", 14, HerkunftBildungsgang.A03);

	/**
	 * Bildungsgang: A04
	 */
	public static readonly A04 : Herkunft = new Herkunft("A04", 15, HerkunftBildungsgang.A04);

	/**
	 * Bildungsgang: A10
	 */
	public static readonly A10 : Herkunft = new Herkunft("A10", 16, HerkunftBildungsgang.A10);

	/**
	 * Bildungsgang: A11
	 */
	public static readonly A11 : Herkunft = new Herkunft("A11", 17, HerkunftBildungsgang.A11);

	/**
	 * Bildungsgang: A12
	 */
	public static readonly A12 : Herkunft = new Herkunft("A12", 18, HerkunftBildungsgang.A12);

	/**
	 * Bildungsgang: A13
	 */
	public static readonly A13 : Herkunft = new Herkunft("A13", 19, HerkunftBildungsgang.A13);

	/**
	 * Bildungsgang: A14
	 */
	public static readonly A14 : Herkunft = new Herkunft("A14", 20, HerkunftBildungsgang.A14);

	/**
	 * Bildungsgang: A15
	 */
	public static readonly A15 : Herkunft = new Herkunft("A15", 21, HerkunftBildungsgang.A15);

	/**
	 * Bildungsgang: A16
	 */
	public static readonly A16 : Herkunft = new Herkunft("A16", 22, HerkunftBildungsgang.A16);

	/**
	 * Bildungsgang: B01
	 */
	public static readonly B01 : Herkunft = new Herkunft("B01", 23, HerkunftBildungsgang.B01);

	/**
	 * Bildungsgang: B02
	 */
	public static readonly B02 : Herkunft = new Herkunft("B02", 24, HerkunftBildungsgang.B02);

	/**
	 * Bildungsgang: B04
	 */
	public static readonly B04 : Herkunft = new Herkunft("B04", 25, HerkunftBildungsgang.B04);

	/**
	 * Bildungsgang: B05
	 */
	public static readonly B05 : Herkunft = new Herkunft("B05", 26, HerkunftBildungsgang.B05);

	/**
	 * Bildungsgang: B06
	 */
	public static readonly B06 : Herkunft = new Herkunft("B06", 27, HerkunftBildungsgang.B06);

	/**
	 * Bildungsgang: B07
	 */
	public static readonly B07 : Herkunft = new Herkunft("B07", 28, HerkunftBildungsgang.B07);

	/**
	 * Bildungsgang: B08
	 */
	public static readonly B08 : Herkunft = new Herkunft("B08", 29, HerkunftBildungsgang.B08);

	/**
	 * Bildungsgang: B09
	 */
	public static readonly B09 : Herkunft = new Herkunft("B09", 30, HerkunftBildungsgang.B09);

	/**
	 * Bildungsgang: B10
	 */
	public static readonly B10 : Herkunft = new Herkunft("B10", 31, HerkunftBildungsgang.B10);

	/**
	 * Bildungsgang: C01
	 */
	public static readonly C01 : Herkunft = new Herkunft("C01", 32, HerkunftBildungsgang.C01);

	/**
	 * Bildungsgang: C02
	 */
	public static readonly C02 : Herkunft = new Herkunft("C02", 33, HerkunftBildungsgang.C02);

	/**
	 * Bildungsgang: C03
	 */
	public static readonly C03 : Herkunft = new Herkunft("C03", 34, HerkunftBildungsgang.C03);

	/**
	 * Bildungsgang: C05
	 */
	public static readonly C05 : Herkunft = new Herkunft("C05", 35, HerkunftBildungsgang.C05);

	/**
	 * Bildungsgang: C06
	 */
	public static readonly C06 : Herkunft = new Herkunft("C06", 36, HerkunftBildungsgang.C06);

	/**
	 * Bildungsgang: C07
	 */
	public static readonly C07 : Herkunft = new Herkunft("C07", 37, HerkunftBildungsgang.C07);

	/**
	 * Bildungsgang: C08
	 */
	public static readonly C08 : Herkunft = new Herkunft("C08", 38, HerkunftBildungsgang.C08);

	/**
	 * Bildungsgang: C11
	 */
	public static readonly C11 : Herkunft = new Herkunft("C11", 39, HerkunftBildungsgang.C11);

	/**
	 * Bildungsgang: C12
	 */
	public static readonly C12 : Herkunft = new Herkunft("C12", 40, HerkunftBildungsgang.C12);

	/**
	 * Bildungsgang: C13
	 */
	public static readonly C13 : Herkunft = new Herkunft("C13", 41, HerkunftBildungsgang.C13);

	/**
	 * Bildungsgang: D01
	 */
	public static readonly D01 : Herkunft = new Herkunft("D01", 42, HerkunftBildungsgang.D01);

	/**
	 * Bildungsgang: D02
	 */
	public static readonly D02 : Herkunft = new Herkunft("D02", 43, HerkunftBildungsgang.D02);

	/**
	 * Bildungsgang: D05
	 */
	public static readonly D05 : Herkunft = new Herkunft("D05", 44, HerkunftBildungsgang.D05);

	/**
	 * Bildungsgang: D06
	 */
	public static readonly D06 : Herkunft = new Herkunft("D06", 45, HerkunftBildungsgang.D06);

	/**
	 * Bildungsgang: E01
	 */
	public static readonly E01 : Herkunft = new Herkunft("E01", 46, HerkunftBildungsgang.E01);

	/**
	 * Bildungsgang: E02
	 */
	public static readonly E02 : Herkunft = new Herkunft("E02", 47, HerkunftBildungsgang.E02);

	/**
	 * Bildungsgang: E03
	 */
	public static readonly E03 : Herkunft = new Herkunft("E03", 48, HerkunftBildungsgang.E03);

	/**
	 * Bildungsgang: E04
	 */
	public static readonly E04 : Herkunft = new Herkunft("E04", 49, HerkunftBildungsgang.E04);

	/**
	 * Bildungsgang: E05
	 */
	public static readonly E05 : Herkunft = new Herkunft("E05", 50, HerkunftBildungsgang.E05);

	/**
	 * Bildungsgang: E07
	 */
	public static readonly E07 : Herkunft = new Herkunft("E07", 51, HerkunftBildungsgang.E07);

	/**
	 * Bildungsgang: E13
	 */
	public static readonly E13 : Herkunft = new Herkunft("E13", 52, HerkunftBildungsgang.E13);

	/**
	 * Weiterbildungskolleg: Abendgymnasium
	 */
	public static readonly AG : Herkunft = new Herkunft("AG", 53, HerkunftBildungsgangsTyp.AG);

	/**
	 * Weiterbildungskolleg: Abendrealschule
	 */
	public static readonly AR : Herkunft = new Herkunft("AR", 54, HerkunftBildungsgangsTyp.AR);

	/**
	 * Weiterbildungskolleg: Kolleg
	 */
	public static readonly KL : Herkunft = new Herkunft("KL", 55, HerkunftBildungsgangsTyp.KL);

	/**
	 * Berufskolleg: Berufsfachschule
	 */
	public static readonly BF : Herkunft = new Herkunft("BF", 56, HerkunftBildungsgangsTyp.BF);

	/**
	 * Berufskolleg: Berufschule
	 */
	public static readonly BS : Herkunft = new Herkunft("BS", 57, HerkunftBildungsgangsTyp.BS);

	/**
	 * Berufskolleg: Berufliches Gymnasium
	 */
	public static readonly BY : Herkunft = new Herkunft("BY", 58, HerkunftBildungsgangsTyp.BY);

	/**
	 * Berufskolleg: Fachoberschule
	 */
	public static readonly FO : Herkunft = new Herkunft("FO", 59, HerkunftBildungsgangsTyp.FO);

	/**
	 * Berufskolleg: Fachschule
	 */
	public static readonly FS : Herkunft = new Herkunft("FS", 60, HerkunftBildungsgangsTyp.FS);

	/**
	 * Sonstige Herkunft: Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind
	 */
	public static readonly AS : Herkunft = new Herkunft("AS", 61, HerkunftSonstige.AS);

	/**
	 * Sonstige Herkunft: Keine Schule bzw. kein Förderschulkindergarten (Einschulung)
	 */
	public static readonly ES : Herkunft = new Herkunft("ES", 62, HerkunftSonstige.ES);

	/**
	 * Sonstige Herkunft: Hausfrüherziehung für Hör- bzw. Sehgeschädigte
	 */
	public static readonly FE : Herkunft = new Herkunft("FE", 63, HerkunftSonstige.FE);

	/**
	 * Sonstige Herkunft: Hochschule, Universität
	 */
	public static readonly HU : Herkunft = new Herkunft("HU", 64, HerkunftSonstige.HU);

	/**
	 * Sonstige Herkunft: Förderschulkindergarten (einschließlich frühkindliche Förderung)
	 */
	public static readonly SK : Herkunft = new Herkunft("SK", 65, HerkunftSonstige.SK);

	/**
	 * Sonstige Herkunft: Herkunft noch unbekannt (nur Gliederung A12, A13)
	 */
	public static readonly UN : Herkunft = new Herkunft("UN", 66, HerkunftSonstige.UN);

	/**
	 * Sonstige Herkunft: Wehr-, Zivil- oder Bundesfreiwilligendienst
	 */
	public static readonly WZ : Herkunft = new Herkunft("WZ", 67, HerkunftSonstige.WZ);

	/**
	 * Sonstige Herkunft: Berufstätigkeit (z. B. vor Besuch einer Fachschule)
	 */
	public static readonly XB : Herkunft = new Herkunft("XB", 68, HerkunftSonstige.XB);

	/**
	 * Sonstige Herkunft: Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler
	 */
	public static readonly XS : Herkunft = new Herkunft("XS", 69, HerkunftSonstige.XS);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Herkunft, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Herkunft
	 */
	public readonly historie : Array<HerkunftKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Herkünften, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _kuerzel : HashMap<string, Herkunft | null> = new HashMap();

	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer sonstigen Herkunft.
	 *
	 * @param h   die sonstige Herkunft
	 */
	private constructor(name : string, ordinal : number, h : HerkunftSonstige);

	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einem Bildungsgang eines Berufskollegs.
	 *
	 * @param h   die Herkunft aus einem Bildungsgang
	 */
	private constructor(name : string, ordinal : number, h : HerkunftBildungsgang);

	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einem Typ eines Bildungsgangs eines Berufskollegs oder eines
	 * Weiterbildungskollegs.
	 *
	 * @param h   die Herkunft aus einem Bildungsgangtyp
	 */
	private constructor(name : string, ordinal : number, h : HerkunftBildungsgangsTyp);

	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einer Schulform.
	 *
	 * @param h   die Herkunft aus einer Schulform
	 */
	private constructor(name : string, ordinal : number, h : HerkunftSchulform);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	private constructor(name : string, ordinal : number, __param0 : HerkunftBildungsgang | HerkunftBildungsgangsTyp | HerkunftSchulform | HerkunftSonstige) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Herkunft.all_values_by_ordinal.push(this);
		Herkunft.all_values_by_name.set(name, this);
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schueler.HerkunftSonstige'))))) {
			const h : HerkunftSonstige = cast_de_svws_nrw_core_types_schueler_HerkunftSonstige(__param0);
			this.historie = Array(h.historie.length).fill(null);
			for (let i : number = 0; i < h.historie.length; i++) {
				this.historie[i] = new HerkunftKatalogEintrag(h.historie[i].id + 1000000000, h.historie[i].kuerzel, h.historie[i].schulformen, h.historie[i].beschreibung, h.historie[i].gueltigVon, h.historie[i].gueltigBis);
			}
			this.daten = this.historie[this.historie.length - 1];
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schueler.HerkunftBildungsgang'))))) {
			const h : HerkunftBildungsgang = cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgang(__param0);
			this.historie = Array(h.historie.length).fill(null);
			for (let i : number = 0; i < h.historie.length; i++) {
				this.historie[i] = new HerkunftKatalogEintrag(h.historie[i].id + 2000000000, h.historie[i].kuerzel, h.historie[i].schulformen, h.historie[i].beschreibung, h.historie[i].gueltigVon, h.historie[i].gueltigBis);
			}
			this.daten = this.historie[this.historie.length - 1];
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schueler.HerkunftBildungsgangsTyp'))))) {
			const h : HerkunftBildungsgangsTyp = cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgangsTyp(__param0);
			this.historie = Array(h.historie.length).fill(null);
			for (let i : number = 0; i < h.historie.length; i++) {
				this.historie[i] = new HerkunftKatalogEintrag(h.historie[i].id + 3000000000, h.historie[i].kuerzel, h.historie[i].schulformen, h.historie[i].beschreibung, h.historie[i].gueltigVon, h.historie[i].gueltigBis);
			}
			this.daten = this.historie[this.historie.length - 1];
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.types.schueler.HerkunftSchulform'))))) {
			const h : HerkunftSchulform = cast_de_svws_nrw_core_types_schueler_HerkunftSchulform(__param0);
			this.historie = Array(h.historie.length).fill(null);
			for (let i : number = 0; i < h.historie.length; i++) {
				this.historie[i] = new HerkunftKatalogEintrag(h.historie[i].id + 4000000000, h.historie[i].kuerzelStatistik, h.historie[i].schulformen, h.historie[i].beschreibung, h.historie[i].gueltigVon, h.historie[i].gueltigBis);
			}
			this.daten = this.historie[this.historie.length - 1];
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt eine Map von den Kürzeln der Herkünfte auf die zugehörigen Herkünfte
	 * zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Herkünfte
	 */
	private static getMapByKuerzel() : HashMap<string, Herkunft | null> {
		if (Herkunft._kuerzel.size() === 0) {
			for (const h of Herkunft.values()) {
				if (h.daten !== null)
					Herkunft._kuerzel.put(h.daten.kuerzel, h);
			}
		}
		return Herkunft._kuerzel;
	}

	/**
	 * Gibt die Herkunft für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Herkunft
	 *
	 * @return die Herkunft oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Herkunft | null {
		return Herkunft.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof Herkunft))
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
	public compareTo(other : Herkunft) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Herkunft> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Herkunft | null {
		const tmp : Herkunft | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.Herkunft', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schueler_Herkunft(obj : unknown) : Herkunft {
	return obj as Herkunft;
}
