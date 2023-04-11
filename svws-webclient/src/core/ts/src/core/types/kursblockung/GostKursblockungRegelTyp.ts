import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursblockungRegelParameterTyp } from '../../../core/types/kursblockung/GostKursblockungRegelParameterTyp';
import { Collection } from '../../../java/util/Collection';
import { List } from '../../../java/util/List';
import { Collections } from '../../../java/util/Collections';
import { Arrays } from '../../../java/util/Arrays';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostKursblockungRegelTyp extends JavaObject implements JavaEnum<GostKursblockungRegelTyp> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostKursblockungRegelTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostKursblockungRegelTyp> = new Map<string, GostKursblockungRegelTyp>();

	/**
	 *  Eine Regel ist nicht definiert.
	 */
	public static readonly UNDEFINIERT : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("UNDEFINIERT", 0, 0, "Undefiniert", Collections.emptyList());

	/**
	 *  Der Regel-Typ zum Sperren von Schienen für alle Kurse der Kursart (A). Dabei werden alle Schienen von B bis C
	 *  gesperrt. Die Schienen sind 1-indiziert, es gilt {@code 1 <= B, C <= Schienenanzahl.} <br>
	 *
	 *  - Parameter A: Datenbank-ID der Kursart (long) <br>
	 *  - Parameter B: von - Nummer der Schiene (int) <br>
	 *  - Parameter C: bis - Nummer der Schiene (int)
	 */
	public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, "Kursart: Sperre Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	/**
	 *  Der Regel-Typ zum Reservieren der Schienen von B bis C für Kurse einer bestimmten Kursart (A).
	 *  Die Schienen sind 1-indiziert, es gilt {@code 1 <= B, C <= Schienenanzahl.} <br>
	 *
	 *  - Parameter A: Datenbank-ID der Kursart (long) <br>
	 *  - Parameter B: von - Nummer der Schiene (int) <br>
	 *  - Parameter C: bis - Nummer der Schiene (int)
	 */
	public static readonly KURSART_ALLEIN_IN_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", 2, 6, "Kursart: Allein in Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	/**
	 *  Der Regel-Typ zum Fixieren eines Kurses (A) in Schiene (B). Die Schiene B ist 1-indiziert, es gilt
	 *  {@code 1 <= B <= Schienenanzahl.} <br>
	 *
	 *  - Parameter A: Datenbank-ID des Kurses (long) <br>
	 *  - Parameter B: Nummer der Schiene (int) <br>
	 */
	public static readonly KURS_FIXIERE_IN_SCHIENE : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_FIXIERE_IN_SCHIENE", 3, 2, "Kurs: Fixiere in Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	/**
	 *  Der Regel-Typ zum Sperren einer Schiene (B) für einen Kurs (A). Die Schiene B ist 1-indiziert, es gilt
	 *  {@code 1 <= B <= Schienenanzahl.} <br>
	 *
	 *  - Parameter A: Datenbank-ID des Kurses (long) <br>
	 *  - Parameter B: Nummer der Schiene (int) <br>
	 */
	public static readonly KURS_SPERRE_IN_SCHIENE : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_SPERRE_IN_SCHIENE", 4, 3, "Kurs: Sperre in Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	/**
	 *  Der Regel-Typ zum Fixieren eines Schülers (A) in einem Kurs (B). <br>
	 *
	 *  - Parameter A: Datenbank-ID des Schülers (long) <br>
	 *  - Parameter B: Datenbank-ID des Kurses (long)
	 */
	public static readonly SCHUELER_FIXIEREN_IN_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_FIXIEREN_IN_KURS", 5, 4, "Schüler: Fixiere in Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum Verbieten eines Schülers (A) in einem Kurs (B). <br>
	 *
	 *  - Parameter A: Datenbank-ID des Schülers (long) <br>
	 *  - Parameter A: Datenbank-ID des Kurses (long)
	 */
	public static readonly SCHUELER_VERBIETEN_IN_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_IN_KURS", 6, 5, "Schüler: Verbiete in Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum Verbieten eines Kurses (A) mit einem Kurs (B) in der selben Schiene. <br>
	 *
	 *  - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 *  - Parameter B: Datenbank-ID des 2. Kurses (long)
	 */
	public static readonly KURS_VERBIETEN_MIT_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_VERBIETEN_MIT_KURS", 7, 7, "Kurs verbiete mit Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum Forcieren, dass Kurs (A) mit einem Kurs (B) in der selben Schiene landet. <br>
	 *
	 *  - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 *  - Parameter B: Datenbank-ID des 2. Kurses (long)
	 */
	public static readonly KURS_ZUSAMMEN_MIT_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_ZUSAMMEN_MIT_KURS", 8, 8, "Kurs zusammen mit Kurs", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass gleiche Lehrkräfte nicht in der selben Schiene landen. <br>
	 *  - Parameter A: Wert 0=externe Lehrkräfte nicht beachten oder 1=alle Lehrkräfte beachten.
	 */
	public static readonly LEHRKRAFT_BEACHTEN : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("LEHRKRAFT_BEACHTEN", 9, 9, "Lehrkräfte beachten (auch Externe?)", Arrays.asList(GostKursblockungRegelParameterTyp.BOOLEAN));

	/**
	 *  Der Regel-Typ zum forcieren, dass gleiche Lehrkräfte nicht in der selben Schiene landen.
	 */
	public static readonly LEHRKRAEFTE_BEACHTEN : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("LEHRKRAEFTE_BEACHTEN", 10, 10, "Lehrkräfte beachten", Arrays.asList());

	/**
	 * Die ID des Regel-Typs
	 */
	public readonly typ : number;

	/**
	 * Die Bezeichnung des Regel-Typs
	 */
	public readonly bezeichnung : string | null;

	/**
	 * Die Typen der Regel-Parameter
	 */
	private readonly paramTypes : List<GostKursblockungRegelParameterTyp>;

	/**
	 * Mapping von "Typ --> GostKursblockungRegelTyp".
	 */
	private static readonly _map_id_regel : HashMap<number, GostKursblockungRegelTyp> = new HashMap();

	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param id            die ID des Regel-Typs
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ
	 * @param paramTypes    die Typen der Parameter für diesen Regel-Typ
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string, paramTypes : List<GostKursblockungRegelParameterTyp>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostKursblockungRegelTyp.all_values_by_ordinal.push(this);
		GostKursblockungRegelTyp.all_values_by_name.set(name, this);
		this.typ = id;
		this.bezeichnung = bezeichnung;
		this.paramTypes = paramTypes;
	}

	private static getMap() : HashMap<number, GostKursblockungRegelTyp> {
		if (GostKursblockungRegelTyp._map_id_regel.isEmpty())
			for (const gostTyp of GostKursblockungRegelTyp.values())
				GostKursblockungRegelTyp._map_id_regel.put(gostTyp.typ, gostTyp);
		return GostKursblockungRegelTyp._map_id_regel;
	}

	/**
	 * Liefert die Menge aller existierender Regeln.
	 *
	 * @return Die Menge aller existierender Regeln.
	 */
	public static getCollection() : Collection<GostKursblockungRegelTyp> {
		return GostKursblockungRegelTyp.getMap().values();
	}

	/**
	 * Ermittelt den Regel-Typ anhand seiner ID und gibt diesen zurück.
	 *
	 * @param id   die ID des Regel-Typs
	 *
	 * @return der Regel-Typ
	 */
	public static fromTyp(id : number | null) : GostKursblockungRegelTyp {
		if (id === null)
			return GostKursblockungRegelTyp.UNDEFINIERT;
		const gostTyp : GostKursblockungRegelTyp | null = GostKursblockungRegelTyp.getMap().get(id);
		if (gostTyp === null)
			return GostKursblockungRegelTyp.UNDEFINIERT;
		return gostTyp;
	}

	/**
	 * Gibt die Anzahl der Parameter für diesen Regel-Type zurück.
	 *
	 * @return die Anzahl der Parameter für diesen Regel-Type zurück.
	 */
	public getParamCount() : number {
		return this.paramTypes.size();
	}

	/**
	 * Gibt den i-ten Parameter-Typ der Regel zurück.
	 *
	 * @param i   der Index des Parameters
	 *
	 * @return der Parameter-Typ
	 *
	 * @throws IllegalArgumentException falls der angegebene Index ungültig ist
	 */
	public getParamType(i : number) : GostKursblockungRegelParameterTyp {
		if ((i < 0) || (i >= this.paramTypes.size()))
			throw new IllegalArgumentException("Ein Parameter mit dem Index i existiert nicht für den Regel-Typ " + this.name()!)
		return this.paramTypes.get(i);
	}

	/**
	 * Prüft, ob der Regeltyp einen Parameter von dem angegebenen
	 * Parametertyp hat.
	 *
	 * @param paramType   der Parametertyp
	 *
	 * @return true, falls die Regel einen solchen Parametertyp hat und ansonsten false
	 */
	public hasParamType(paramType : GostKursblockungRegelParameterTyp | null) : boolean {
		for (const cur of this.paramTypes)
			if (paramType as unknown === cur as unknown)
				return true;
		return false;
	}

	/**
	 * Simuliert ein Löschen der Schienen-Nummer und
	 * liefert die ggf. veränderten Parameterwerte zurück, oder NULL wenn die Regel gelöscht werden muss.
	 *
	 * @param pRegel      Die Regel, die von einer Schienen-Löschung ggf. betroffen ist.
	 * @param pSchienenNr Die Schiene deren Nummer gelöscht werden soll.
	 *
	 * @return die ggf. veränderten Parameter, oder NULL wenn die Regel gelöscht werden muss.
	 */
	public static getNeueParameterBeiSchienenLoeschung(pRegel : GostBlockungRegel, pSchienenNr : number) : Array<number> | null {
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(pRegel.typ);
		const param : ArrayList<number> = pRegel.parameter;
		switch (typ) {
			case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
				return [];
			}
			case GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN: {
				return [param.get(0)];
			}
			case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS:
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS:
			case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS:
			case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
				return [param.get(0), param.get(1)];
			}
			case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE:
			case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
				return (param.get(1) < pSchienenNr) ? [param.get(0), param.get(1)] : (param.get(1) > pSchienenNr) ? [param.get(0), param.get(1) - 1] : null;
			}
			case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS:
			case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
				let von : number = param.get(1).valueOf()
				let bis : number = param.get(2).valueOf()
				von = pSchienenNr < von ? von - 1 : von;
				bis = pSchienenNr <= bis ? bis - 1 : bis;
				if (von <= bis)
					return [param.get(0), von, bis];
				return null;
			}
			default: {
				throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ)
			}
		}
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
		if (!(other instanceof GostKursblockungRegelTyp))
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
	public compareTo(other : GostKursblockungRegelTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostKursblockungRegelTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostKursblockungRegelTyp | null {
		const tmp : GostKursblockungRegelTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kursblockung_GostKursblockungRegelTyp(obj : unknown) : GostKursblockungRegelTyp {
	return obj as GostKursblockungRegelTyp;
}
