package de.nrw.schule.svws.core.types.kursblockung;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse definiert die unterschiedlichen Regel-Typen, die im Rahmen 
 * der Kursblockung eingesetzt werden. 
 */
public enum GostKursblockungRegelTyp {

	/** 
	 * Eine Regel ist nicht definiert.
	 */
	UNDEFINIERT(0, "Undefiniert", Collections.emptyList()),

	/** 
	 * Der Regel-Typ zum Sperren von Schienen für alle Kurse der Kursart (A). Dabei werden alle Schienen von B bis C
	 * gesperrt. Die Schienen sind 1-indiziert, es gilt {@code 1 <= B, C <= Schienenanzahl.} <br>
	 * 
	 * - Parameter A: Datenbank-ID der Kursart (long) <br>
	 * - Parameter B: von - Nummer der Schiene (int) <br>
	 * - Parameter C: bis - Nummer der Schiene (int) 
	 */
	KURSART_SPERRE_SCHIENEN_VON_BIS(1, "Kursart: Sperre Schienen (von/bis)", Arrays.asList( 
		GostKursblockungRegelParameterTyp.KURSART, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR 
	)),

	/** 
	 * Der Regel-Typ zum Reservieren der Schienen von B bis C für Kurse einer bestimmten Kursart (A).
	 * Die Schienen sind 1-indiziert, es gilt {@code 1 <= B, C <= Schienenanzahl.} <br>
	 * 
	 * - Parameter A: Datenbank-ID der Kursart (long) <br>
	 * - Parameter B: von - Nummer der Schiene (int) <br>
	 * - Parameter C: bis - Nummer der Schiene (int) 
	 */
	KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(6, "Kursart: Allein in Schienen (von/bis)", Arrays.asList( 
		GostKursblockungRegelParameterTyp.KURSART, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR 
	)),
	
	/** 
	 * Der Regel-Typ zum Fixieren eines Kurses (A) in Schiene (B). Die Schiene B ist 1-indiziert, es gilt
	 * {@code 1 <= B <= Schienenanzahl.} <br>
	 * 
	 * - Parameter A: Datenbank-ID des Kurses (long) <br>
	 * - Parameter B: Nummer der Schiene (int) <br>
	 */
	KURS_FIXIERE_IN_SCHIENE(2, "Kurs: Fixiere in Schiene", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR
	)),

	/** 
	 * Der Regel-Typ zum Sperren einer Schiene (B) für einen Kurs (A). Die Schiene B ist 1-indiziert, es gilt
	 * {@code 1 <= B <= Schienenanzahl.} <br>
	 * 
	 * - Parameter A: Datenbank-ID des Kurses (long) <br>
	 * - Parameter B: Nummer der Schiene (int) <br>
	 */
	KURS_SPERRE_IN_SCHIENE(3, "Kurs: Sperre in Schiene", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID, 
		GostKursblockungRegelParameterTyp.SCHIENEN_NR
	)),

	/** 
	 * Der Regel-Typ zum Fixieren eines Schülers (A) in einem Kurs (B). <br>
	 * 
	 * - Parameter A: Datenbank-ID des Schülers (long) <br>
	 * - Parameter B: Datenbank-ID des Kurses (long) 
	 */
	SCHUELER_FIXIEREN_IN_KURS(4, "Schüler: Fixiere in Kurs", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID, 
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/** 
	 * Der Regel-Typ zum Verbieten eines Schülers (A) in einem Kurs (B). <br>
	 * 
	 * - Parameter A: Datenbank-ID des Schülers (long) <br>
	 * - Parameter A: Datenbank-ID des Kurses (long) 
	 */
	SCHUELER_VERBIETEN_IN_KURS(5, "Schüler: Verbiete in Kurs", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID, 
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/** 
	 * Der Regel-Typ zum Verbieten eines Kurses (A) mit einem Kurs (B) in der selben Schiene. <br>
	 * 
	 * - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 * - Parameter B: Datenbank-ID des 2. Kurses (long) 
	 */
	KURS_VERBIETEN_MIT_KURS(7, "Kurs verbiete mit Kurs", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID, 
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/** 
	 * Der Regel-Typ zum Forcieren, dass Kurs (A) mit einem Kurs (B) in der selben Schiene landet. <br>
	 * 
	 * - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 * - Parameter B: Datenbank-ID des 2. Kurses (long) 
	 */
	KURS_ZUSAMMEN_MIT_KURS(8, "Kurs zusammen mit Kurs", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID, 
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/** 
	 * Der Regel-Typ zum forcieren, dass gleiche Lehrkräfte nicht in der selben Schiene landen. <br>
	 * - Parameter A: Wert 0=externe Lehrkräfte nicht beachten oder 1=alle Lehrkräfte beachten.
	 */
	LEHRKRAFT_BEACHTEN(9, "Lehrkräfte beachten (auch Externe?)", Arrays.asList(
			GostKursblockungRegelParameterTyp.BOOLEAN
	));

	/** Die ID des Regel-Typs */
	public final int typ;

	/** Die Bezeichnung des Regel-Typs */
	public final String bezeichnung;

	/** Die Typen der Regel-Parameter */
	private final @NotNull List<@NotNull GostKursblockungRegelParameterTyp> paramTypes;
	
	/** Mapping von "Typ --> GostKursblockungRegelTyp". */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> _map_id_regel = new HashMap<>();

	private static @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> getMap() {
		if (_map_id_regel.isEmpty()) 
			for (@NotNull GostKursblockungRegelTyp gostTyp : GostKursblockungRegelTyp.values()) 
				_map_id_regel.put(gostTyp.typ, gostTyp);
		return _map_id_regel;
	}
	
	/**
	 * Liefert die Menge aller existierender Regeln.
	 * 
	 * @return Die Menge aller existierender Regeln.
	 */
	public static @NotNull Collection<@NotNull GostKursblockungRegelTyp> getCollection() {
		return getMap().values();
	}

	/** 
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 * 
	 * @param id            die ID des Regel-Typs
	 * @param paramCount    die Anzahl der Parameter für diesen Regel-Typ
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ 
	 */
	private GostKursblockungRegelTyp(int id, @NotNull String bezeichnung, @NotNull List<@NotNull GostKursblockungRegelParameterTyp> paramTypes) throws IllegalArgumentException {
		this.typ = id;
		this.bezeichnung = bezeichnung;
		this.paramTypes = paramTypes;
	}

	/** 
	 * Ermittelt den Regel-Typ anhand seiner ID und gibt diesen zurück.
	 *
	 * @param id   die ID des Regel-Typs
	 * 
	 * @return der Regel-Typ 
	 */
	public static @NotNull GostKursblockungRegelTyp fromTyp(Integer id) {
		if (id == null)
			return GostKursblockungRegelTyp.UNDEFINIERT;
		GostKursblockungRegelTyp gostTyp = getMap().get(id);
		if (gostTyp == null)
			return GostKursblockungRegelTyp.UNDEFINIERT;
		return gostTyp;
	}
	
	/** 
	 * Gibt die Anzahl der Parameter für diesen Regel-Type zurück.
	 * 
	 * @return die Anzahl der Parameter für diesen Regel-Type zurück. 
	 */
	public int getParamCount() {
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
	public @NotNull GostKursblockungRegelParameterTyp getParamType(int i) throws IllegalArgumentException {
		if ((i < 0) || (i >= paramTypes.size()))
			throw new IllegalArgumentException("Ein Parameter mit dem Index i existiert nicht für den Regel-Typ " + this.name());
		return paramTypes.get(i);
	}


	/**
	 * Prüft, ob der Regeltyp einen Parameter von dem angegebenen
	 * Parametertyp hat.
	 *  
	 * @param paramType   der Parametertyp
	 * 
	 * @return true, falls die Regel einen solchen Parametertyp hat und ansonsten false 
	 */
	public boolean hasParamType(GostKursblockungRegelParameterTyp paramType) {
	    for (GostKursblockungRegelParameterTyp cur : paramTypes)
	        if (paramType == cur)
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
	public static long[] getNeueParameterBeiSchienenLoeschung(@NotNull GostBlockungRegel pRegel, int pSchienenNr) {
		@NotNull GostKursblockungRegelTyp typ = fromTyp(pRegel.typ);
		@NotNull Vector<@NotNull Long> param = pRegel.parameter;
		switch (typ) {
			// Keine Veränderung bei (2 Parameter) 
            case SCHUELER_FIXIEREN_IN_KURS, SCHUELER_VERBIETEN_IN_KURS, 
                 KURS_VERBIETEN_MIT_KURS, KURS_ZUSAMMEN_MIT_KURS : { // 4, 5, 7, 8
            	long p0 = param.get(0);
            	long p1 = param.get(1);
            	return new long[] {p0, p1};
            }
			// Keine Veränderung bei (1 Parameter) 
            case LEHRKRAFT_BEACHTEN: { // 9
            	long p0 = param.get(0);
            	return new long[] {p0};
            }
            case KURS_FIXIERE_IN_SCHIENE, KURS_SPERRE_IN_SCHIENE: { // 2, 3
            	long p0 = param.get(0);
            	long p1 = param.get(1);
            	if (p1 < pSchienenNr) 
                	return new long[] {p0, p1};
            	if (p1 > pSchienenNr) 
            		return new long[] {p0, p1 - 1};
        		return null;
            }
            case KURSART_SPERRE_SCHIENEN_VON_BIS, KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: { // 1, 6
            	long p0 = param.get(0);
            	long von = param.get(1);
            	long bis = param.get(2);
            	von = pSchienenNr < von  ? von - 1 : von; 
            	bis = pSchienenNr <= bis  ? bis - 1 : bis;
            	if (von <= bis)
                	return new long[] {p0, von, bis};
        		return null;
            }
			default: {
				throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ);
			}
        }
	}
	
}
