package de.svws_nrw.core.types.kursblockung;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.kursblockung.KursblockungDynDaten;
import de.svws_nrw.core.kursblockung.KursblockungDynStatistik;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die unterschiedlichen Regel-Typen, die im Rahmen der Kursblockung eingesetzt werden.
 * Um eine neue Regel zu definieren, geht man wie folgt vor:
 * <br>
 * <br> Passive Anpassung
 * <br> {@link GostKursblockungRegelTyp}: Enum definieren                                                 --> DONE
 * <br> {@link GostKursblockungRegelTyp#getNeueParameterBeiSchienenLoeschung}: ggf. anpassen              --> DONE
 * <br> {@link KursblockungDynDaten#schritt01FehlerBeiReferenzen}: anpassen (bei der Switch-Anweisung)    --> DONE
 * <br> {@link GostBlockungsergebnisManager#stateRegelvalidierung}: aktualisieren.                        --> DONE
 * <br> {@link GostBlockungsergebnisManager}: regelupdate Methoden(n) erzeugen                            --> DONE
 * <br> {@link GostBlockungsergebnisManager}: ggf. Methodenanpassungen                                    -->
 * <br>
 * <br> Aktive Anpassung
 * <br> {@link KursblockungDynDaten#KursblockungDynDaten}: Methode schrittXXFehlerBeiRegelXXX() einfügen  --> DONE
 * <br> {@link KursblockungDynStatistik}: Auf Regelverletzungen dynamisch reagieren                       --> DONE
 *
 * <br> GUI Anpassung
 * <br> SCardGostRegelansicht.vue
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
	KURSART_SPERRE_SCHIENEN_VON_BIS(1, "Kursart: Sperre Schienen von/bis", Arrays.asList(
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
	KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(6, "Kursart: Allein in Schienen von/bis", Arrays.asList(
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
	KURS_VERBIETEN_MIT_KURS(7, "Kurs: Verbiete mit Kurs in gleicher Schiene", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID,
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/**
	 * Der Regel-Typ zum Forcieren, dass Kurs (A) mit einem Kurs (B) in der selben Schiene landet. <br>
	 *
	 * - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 * - Parameter B: Datenbank-ID des 2. Kurses (long)
	 */
	KURS_ZUSAMMEN_MIT_KURS(8, "Kurs: Zusammen mit Kurs in gleicher Schiene", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID,
		GostKursblockungRegelParameterTyp.KURS_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Kurs mit einer bestimmten Anzahl an Dummy-SuS aufgefüllt wird. <br>
	 * - Parameter A: Datenbank-ID des 1. Kurses (long) <br>
	 * - Parameter B: Die Anzahl an Dummy-SuS. Gültige Werte sind im Intervall 1 bis 100.
	 */
	KURS_MIT_DUMMY_SUS_AUFFUELLEN(9, "Kurs: Fülle mit Dummy-SuS auf", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID,
		GostKursblockungRegelParameterTyp.GANZZAHL
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass gleiche Lehrkräfte nicht in der selben Schiene landen.
	 */
	LEHRKRAEFTE_BEACHTEN(10, "Lehrkräfte beachten", Arrays.asList()),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Schüler mit einem anderen Schüler in einem Fach zusammen ist.
	 * <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 * <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 * <br>- Parameter C: Datenbank-ID des Faches
	 */
	SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(11, "Schüler: Zusammen mit Schüler in Fach", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.FACH_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Schüler nicht mit einem anderen Schüler in einem Fach zusammen ist.
	 * <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 * <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 * <br>- Parameter C: Datenbank-ID des Faches
	 */
	SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(12, "Schüler: Verbieten mit Schüler in Fach", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.FACH_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Schüler immer (falls möglich) mit einem anderen Schüler zusammen ist.
	 * <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 * <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 */
	SCHUELER_ZUSAMMEN_MIT_SCHUELER(13, "Schüler: Zusammen mit Schüler", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.SCHUELER_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Schüler niemals (falls möglich) mit einem anderen Schüler zusammen ist.
	 * <br>- Parameter A: Datenbank-ID des 1. Schülers (long)
	 * <br>- Parameter B: Datenbank-ID des 2. Schülers (long)
	 */
	SCHUELER_VERBIETEN_MIT_SCHUELER(14, "Schüler: Verbieten mit Schüler", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID,
		GostKursblockungRegelParameterTyp.SCHUELER_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Kurs eine bestimmte Schüleranzahl nicht überschreitet.
	 * <br>- Parameter A: Datenbank-ID des Kurses (long)
	 * <br>- Parameter B: Die maximal erlaubte Schüleranzahl. Gültige Werte sind im Intervall 0 bis 100.
	 */
	KURS_MAXIMALE_SCHUELERANZAHL(15, "Kurs: Maximale Schüleranzahl", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID,
		GostKursblockungRegelParameterTyp.GANZZAHL
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass ein Schüler beim Blocken nicht auf Kurse verteilt wird,
	 * also ignoriert wird. Ebenfalls beachtet der Manager, dass nicht erhaltene Fachwahlen nicht
	 * negativ in die Bewertung einfließen.
	 * <br>- Parameter A: Datenbank-ID des Schülers (long)
	 */
	SCHUELER_IGNORIEREN(16, "Schüler: Ignorieren", Arrays.asList(
		GostKursblockungRegelParameterTyp.SCHUELER_ID
	)),

	/**
	 * Der Regel-Typ zum forcieren, dass bei Kursdifferenz-Berechnungen der Kurs ignoriert wird.
	 * Dies bezieht sich nur auf die Visualisierung.
	 * <br>- Parameter A: Datenbank-ID des Kurses (long)
	 */
	KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(17, "Kurs: Kursdifferenz bei der Visualisierung ignorieren", Arrays.asList(
		GostKursblockungRegelParameterTyp.KURS_ID
	));


	/** Die ID des Regel-Typs */
	public final int typ;

	/** Die Bezeichnung des Regel-Typs */
	public final String bezeichnung;

	/** Die Typen der Regel-Parameter */
	private final @NotNull List<@NotNull GostKursblockungRegelParameterTyp> paramTypes;

	/** Mapping von "Typ --> GostKursblockungRegelTyp". */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> _map_id_regel = new HashMap<>();

	/** Mapping vom "Typ --> GostKursblockungRegelTyp mit einer Kurs-ID als Regel-Parameter-Type" */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> _map_id_regel_kursid = new HashMap<>();



	private static @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> getMap() {
		if (_map_id_regel.isEmpty())
			for (final @NotNull GostKursblockungRegelTyp gostTyp : GostKursblockungRegelTyp.values())
				_map_id_regel.put(gostTyp.typ, gostTyp);
		return _map_id_regel;
	}


	private static @NotNull HashMap<@NotNull Integer, @NotNull GostKursblockungRegelTyp> getMapKursRegeln() {
		if (_map_id_regel_kursid.isEmpty())
			for (final @NotNull GostKursblockungRegelTyp gostTyp : GostKursblockungRegelTyp.values())
				if (gostTyp.hasParamType(GostKursblockungRegelParameterTyp.KURS_ID))
					_map_id_regel_kursid.put(gostTyp.typ, gostTyp);
		return _map_id_regel_kursid;
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
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ
	 * @param paramTypes    die Typen der Parameter für diesen Regel-Typ
	 */
	GostKursblockungRegelTyp(final int id, final @NotNull String bezeichnung, final @NotNull List<@NotNull GostKursblockungRegelParameterTyp> paramTypes) throws IllegalArgumentException {
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
	public static @NotNull GostKursblockungRegelTyp fromTyp(final Integer id) {
		if (id == null)
			return GostKursblockungRegelTyp.UNDEFINIERT;
		final GostKursblockungRegelTyp gostTyp = getMap().get(id);
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
	public @NotNull GostKursblockungRegelParameterTyp getParamType(final int i) throws IllegalArgumentException {
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
	public boolean hasParamType(final GostKursblockungRegelParameterTyp paramType) {
		for (final GostKursblockungRegelParameterTyp cur : paramTypes)
			if (paramType == cur)
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
	public static long[] getNeueParameterBeiSchienenLoeschung(final @NotNull GostBlockungRegel pRegel, final int pSchienenNr) {
		final @NotNull List<@NotNull Long> param = pRegel.parameter;

		final @NotNull GostKursblockungRegelTyp typ = fromTyp(pRegel.typ);
		switch (typ) {
			// Fälle, bei denen es zu einer Veränderung kommt.
			case KURS_FIXIERE_IN_SCHIENE, KURS_SPERRE_IN_SCHIENE: // 2, 3
				if (pSchienenNr > param.get(1))
					return new long[] { param.get(0), param.get(1) }; // Keine Veränderung.
				if (pSchienenNr < param.get(1))
					return new long[] { param.get(0), param.get(1) - 1 }; // Indexverschiebung der Schienen-Nr.
				return null;

			case KURSART_SPERRE_SCHIENEN_VON_BIS, KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: // 1, 6
				long von = param.get(1);
				long bis = param.get(2);
				von = pSchienenNr < von  ? von - 1 : von;
				bis = pSchienenNr <= bis  ? bis - 1 : bis;
				if (von <= bis)
					return new long[] { param.get(0), von, bis };
				return null;

			// Keine Veränderung: Kopiere alle Parameter.
			default:
				final long[] temp = new long[param.size()];
				for (int i = 0; i < temp.length; i++)
					temp[i] = param.get(i);
				return temp;
		}
	}


	/**
	 * Gibt alle Regel-Typen zurück, welche eine Kurs-ID als Parameter-Typ haben.
	 *
	 * @return eine Collection mit allen Regel-Typen mit Bezug zu einem konkreten Kurs
	 */
	public static @NotNull Collection<@NotNull GostKursblockungRegelTyp> getKursRegelTypen() {
		return getMapKursRegeln().values();
	}

}
