import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { Collections } from '../../../java/util/Collections';
import { Arrays } from '../../../java/util/Arrays';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursblockungRegelParameterTyp } from '../../../core/types/kursblockung/GostKursblockungRegelParameterTyp';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostKursblockungRegelTyp extends JavaEnum<GostKursblockungRegelTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostKursblockungRegelTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostKursblockungRegelTyp> = new Map<string, GostKursblockungRegelTyp>();

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
	public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, "Kursart: Sperre Schienen von/bis", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	/**
	 *  Der Regel-Typ zum Reservieren der Schienen von B bis C für Kurse einer bestimmten Kursart (A).
	 *  Die Schienen sind 1-indiziert, es gilt {@code 1 <= B, C <= Schienenanzahl.} <br>
	 *
	 *  - Parameter A: Datenbank-ID der Kursart (long) <br>
	 *  - Parameter B: von - Nummer der Schiene (int) <br>
	 *  - Parameter C: bis - Nummer der Schiene (int)
	 */
	public static readonly KURSART_ALLEIN_IN_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", 2, 6, "Kursart: Allein in Schienen von/bis", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

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
	public static readonly KURS_VERBIETEN_MIT_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_VERBIETEN_MIT_KURS", 7, 7, "Kurs: Verbiete mit Kurs in gleicher Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum Forcieren, dass Kurs (A) mit einem Kurs (B) in der selben Schiene landet. <br>
	 *
	 *  - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 *  - Parameter B: Datenbank-ID des 2. Kurses (long)
	 */
	public static readonly KURS_ZUSAMMEN_MIT_KURS : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_ZUSAMMEN_MIT_KURS", 8, 8, "Kurs: Zusammen mit Kurs in gleicher Schiene", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Kurs mit einer bestimmten Anzahl an Dummy-SuS aufgefüllt wird. <br>
	 *  - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 *  - Parameter B: Die Anzahl an Dummy-SuS. Gültige Werte sind im Intervall 1 bis 100.
	 */
	public static readonly KURS_MIT_DUMMY_SUS_AUFFUELLEN : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_MIT_DUMMY_SUS_AUFFUELLEN", 9, 9, "Kurs: Fülle mit Dummy-SuS auf", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.GANZZAHL));

	/**
	 *  Der Regel-Typ zum forcieren, dass gleiche Lehrkräfte nicht in der selben Schiene landen.
	 */
	public static readonly LEHRKRAEFTE_BEACHTEN : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("LEHRKRAEFTE_BEACHTEN", 10, 10, "Lehrkräfte beachten", Arrays.asList());

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Schüler mit einem anderen Schüler in einem Fach zusammen ist.
	 *  <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 *  <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 *  <br>- Parameter C: Datenbank-ID des Faches
	 */
	public static readonly SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH", 11, 11, "Schüler: Zusammen mit Schüler in Fach", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.FACH_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Schüler nicht mit einem anderen Schüler in einem Fach zusammen ist.
	 *  <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 *  <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 *  <br>- Parameter C: Datenbank-ID des Faches
	 */
	public static readonly SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH", 12, 12, "Schüler: Verbieten mit Schüler in Fach", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.FACH_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Schüler immer (falls möglich) mit einem anderen Schüler zusammen ist.
	 *  <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 *  <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 */
	public static readonly SCHUELER_ZUSAMMEN_MIT_SCHUELER : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_ZUSAMMEN_MIT_SCHUELER", 13, 13, "Schüler: Zusammen mit Schüler", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.SCHUELER_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Schüler niemals (falls möglich) mit einem anderen Schüler zusammen ist.
	 *  <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 *  <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 */
	public static readonly SCHUELER_VERBIETEN_MIT_SCHUELER : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_MIT_SCHUELER", 14, 14, "Schüler: Verbieten mit Schüler", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.SCHUELER_ID));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Kurs eine bestimmte Schüleranzahl nicht überschreitet.
	 *  <br>- Parameter A: Datenbank-ID des Kurses (long)
	 *  <br>- Parameter B: Die maximal erlaubte Schüleranzahl. Gültige Werte sind im Intervall 0 bis 100.
	 */
	public static readonly KURS_MAXIMALE_SCHUELERANZAHL : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("KURS_MAXIMALE_SCHUELERANZAHL", 15, 15, "Kurs: Maximale Schüleranzahl", Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.GANZZAHL));

	/**
	 *  Der Regel-Typ zum forcieren, dass ein Schüler niemals (falls möglich) mit einem anderen Schüler zusammen ist.
	 *  <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 *  <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 */
	public static readonly SCHUELER_IGNORIEREN : GostKursblockungRegelTyp = new GostKursblockungRegelTyp("SCHUELER_IGNORIEREN", 16, 16, "Schüler: Ignorieren", Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID));

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
	 * Mapping vom "Typ --> GostKursblockungRegelTyp mit einer Kurs-ID als Regel-Parameter-Type"
	 */
	private static readonly _map_id_regel_kursid : HashMap<number, GostKursblockungRegelTyp> = new HashMap();

	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param id            die ID des Regel-Typs
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ
	 * @param paramTypes    die Typen der Parameter für diesen Regel-Typ
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string, paramTypes : List<GostKursblockungRegelParameterTyp>) {
		super(name, ordinal);
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

	private static getMapKursRegeln() : HashMap<number, GostKursblockungRegelTyp> {
		if (GostKursblockungRegelTyp._map_id_regel_kursid.isEmpty())
			for (const gostTyp of GostKursblockungRegelTyp.values())
				if (gostTyp.hasParamType(GostKursblockungRegelParameterTyp.KURS_ID))
					GostKursblockungRegelTyp._map_id_regel_kursid.put(gostTyp.typ, gostTyp);
		return GostKursblockungRegelTyp._map_id_regel_kursid;
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
	 * Simuliert ein Löschen der Schienen-Nummer und liefert die ggf. veränderten Parameterwerte zurück,
	 * oder NULL wenn die Regel gelöscht werden muss.
	 *
	 * @param pRegel      Die Regel, die von einer Schienen-Löschung ggf. betroffen ist.
	 * @param pSchienenNr Die Schiene deren Nummer gelöscht werden soll.
	 *
	 * @return die ggf. veränderten Parameter, oder NULL wenn die Regel gelöscht werden muss.
	 */
	public static getNeueParameterBeiSchienenLoeschung(pRegel : GostBlockungRegel, pSchienenNr : number) : Array<number> | null {
		const param : List<number> = pRegel.parameter;
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(pRegel.typ);
		switch (typ) {
			case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE:
			case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
				if (pSchienenNr > param.get(1))
					return [param.get(0), param.get(1)];
				if (pSchienenNr < param.get(1))
					return [param.get(0), param.get(1) - 1];
				return null;
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
				const temp : Array<number> | null = Array(param.size()).fill(0)
				for (let i : number = 0; i < temp.length; i++)
					temp[i] = param.get(i).valueOf();
				return temp;
			}
		}
	}

	/**
	 * Gibt alle Regel-Typen zurück, welche eine Kurs-ID als Parameter-Typ haben.
	 *
	 * @return eine Collection mit allen Regel-Typen mit Bezug zu einem konkreten Kurs
	 */
	public static getKursRegelTypen() : Collection<GostKursblockungRegelTyp> {
		return GostKursblockungRegelTyp.getMapKursRegeln().values();
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kursblockung_GostKursblockungRegelTyp(obj : unknown) : GostKursblockungRegelTyp {
	return obj as GostKursblockungRegelTyp;
}
