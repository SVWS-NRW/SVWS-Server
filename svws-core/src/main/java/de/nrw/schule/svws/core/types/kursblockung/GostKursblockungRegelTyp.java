package de.nrw.schule.svws.core.types.kursblockung;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
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
	));

	/** Die ID des Regel-Typs */
	public final int typ;

	/** Die Bezeichnung des Regel-Typs */
	public final String bezeichnung;

	/** Die Typen der Regel-Parameter */
	private final @NotNull List<@NotNull GostKursblockungRegelParameterTyp> paramTypes;
	
	/** Mapping von "Typ --> GostKursblockungRegelTyp". */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> map = new HashMap<>();
	
	static {
		// Fülle Map.
		for (@NotNull GostKursblockungRegelTyp gostTyp : GostKursblockungRegelTyp.values()) 
			map.put(gostTyp.typ, gostTyp);
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
		GostKursblockungRegelTyp gostTyp = map.get(id);
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
	 * Erstellt ein neues Regel-Objekt der Klasse {@link KursblockungInputRegel} mithilfe der angegebenen Parameter.
	 * 
	 * @param  databaseID    die ID der Regel aus der Datenbank.
	 * @param  params        die Parameter für das Regel-Objekt
	 * 
	 * @return das Regel-Objekt
	 * @throws IllegalArgumentException   wenn die Anzahl der Parameter nicht zu diesem Regel-Typ passt 
	 */
	private @NotNull KursblockungInputRegel create(long databaseID, @NotNull Long... params)
			throws IllegalArgumentException {
		if ((params == null) || (params.length != this.paramTypes.size()))
			throw new IllegalArgumentException();
		@NotNull
		KursblockungInputRegel regel = new KursblockungInputRegel();
		regel.databaseID = databaseID;
		regel.typ = this.typ;
		regel.daten = new Long[params.length];
		for (int i = 0; i < params.length; i++) {
			Long data = params[i];
			if (data == null)
				throw new NullPointerException();
			regel.daten[i] = data;
		}
		return regel;
	}

	/** 
	 * Erstellt ein neues Regel-Objekt der Klasse {@link KursblockungInputRegel} mithilfe der angegebenen ID und der
	 * angegebenen Parameter.
	 * 
	 * @param  databaseID   die ID der Regel aus der Datenbank.
	 * @param  typ          die Nr des Regel-Typs
	 * @param  params       die Parameter der zu erstellenden Regel
	 * 
	 * @return das Regel-Objekt
	 * 
	 * @throws IllegalArgumentException   wenn es keine gültige Regel für angegeben ID gibt oder die Anzahl 
	 *                                    der Parameter nicht zu dem Regel-Typ passt 
	 */
	public static @NotNull KursblockungInputRegel createByID(long databaseID, int typ, @NotNull Long... params)
			throws IllegalArgumentException {
		@NotNull GostKursblockungRegelTyp gostTyp = GostKursblockungRegelTyp.fromTyp(typ);
		if (gostTyp.equals(UNDEFINIERT))
			throw new IllegalArgumentException("Ungültiger Regel-Typ");
		return gostTyp.create(databaseID, params);
	}

	/** 
	 * Erstellt ein neues Regel-Objekt der Klasse {@link KursblockungInputRegel} mithilfe der angegebenen Regel.
	 * 
	 * @param regel   die Regel als Core-DTO
	 * 
	 * @return das Regel-Objekt
	 * 
	 * @throws IllegalArgumentException   wenn es keine gültige Regel für angegeben ID gibt oder die Anzahl der 
	 *                                    Parameter nicht zu dem Regel-Typ passt */
	public static @NotNull KursblockungInputRegel createFrom(@NotNull GostBlockungRegel regel)
			throws IllegalArgumentException {
		return GostKursblockungRegelTyp.createByID(regel.id, regel.typ, regel.parameter.toArray(new Long[0]));
	}

}
