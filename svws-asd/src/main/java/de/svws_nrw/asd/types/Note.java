package de.svws_nrw.asd.types;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Noten
 */
public enum Note implements @NotNull CoreType<@NotNull NoteKatalogEintrag, @NotNull Note> {

	/** leerer Noteneintrag */
	KEINE,

	/** Die Note "ungenügend" mit 0 Punkten.*/
	UNGENUEGEND,

	/** Die Note "mangelhaft minus" mit 1 Punkt.*/
	MANGELHAFT_MINUS,

	/** Die Note "mangelhaft" mit 2 Punkten.*/
	MANGELHAFT,

	/** Die Note "mangelhaft plus" mit 3 Punkten.*/
	MANGELHAFT_PLUS,

	/** Die Note "ausreichend minus" mit 4 Punkten.*/
	AUSREICHEND_MINUS,

	/** Die Note "ausreichend " mit 5 Punkten.*/
	AUSREICHEND,

	/** Die Note "ausreichend plus" mit 6 Punkten.*/
	AUSREICHEND_PLUS,

	/** Die Note "befriedigend minus" mit 7 Punkten.*/
	BEFRIEDIGEND_MINUS,

	/** Die Note "befriedigend" mit 8 Punkten.*/
	BEFRIEDIGEND,

	/** Die Note "befriedigend plus" mit 9 Punkten.*/
	BEFRIEDIGEND_PLUS,

	/** Die Note "gut minus" mit 10 Punkten.*/
	GUT_MINUS,

	/** Die Note "gut" mit 11 Punkten.*/
	GUT,

	/** Die Note "gut plus" mit 12 Punkten.*/
	GUT_PLUS,

	/** Die Note "sehr gut minus" mit 13 Punkten.*/
	SEHR_GUT_MINUS,

	/** Die Note "sehr gut" mit 14 Punkten.*/
	SEHR_GUT,

	/** Die Note "sehr gut plus" mit 15 Punkten.*/
	SEHR_GUT_PLUS,

	/** Der Noteneintrag "ärztliches Attest" mit dem Wert AT.*/
	ATTEST,

	/** Der Noteneintrag "mit besonderem Erfolg teilgenommen" mit dem Wert E1.*/
	E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN,

	/** Der Noteneintrag "mit Erfolg teilgenommen" mit dem Wert E2.*/
	E2_MIT_ERFOLG_TEILGENOMMEN,

	/** Der Noteneintrag "teilgenommen" mit dem Wert E3.*/
	E3_TEILGENOMMEN,

	/** Der Noteneintrag "nicht teilgenommen" mit dem Wert NT.*/
	NICHT_TEILGENOMMEN,

	/** Der Noteneintrag "kann nicht beurteilt werden" mit dem Wert NB.*/
	NICHT_BEURTEILT,

	/** Der Noteneintrag "nicht erteilt" mit dem Wert NE.*/
	NICHT_ERTEILT,

	/** Der Noteneintrag "kein Unterricht wegen Lehrermangel" mit dem Wert LM.*/
	LEHRERMANGEL,

	/** Der Noteneintrag "abgemeldet" mit dem Wert AM.*/
	ABGEMELDET;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<NoteKatalogEintrag, Note> manager) {
		CoreTypeDataManager.putManager(Note.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<NoteKatalogEintrag, Note> data() {
		return CoreTypeDataManager.getManager(Note.class);
	}

	/**
	 * Gibt zurück, ob es sich um eine echte Note oder nur um eine "Pseudonote" handelt
	 *
	 * @param schuljahr	Prüfung im angegebenen Schuljahr
	 *
	 * @return true, wenn es sich bei der Note um eine echte Note handelt
	 */

	public boolean istNote(final int schuljahr) {
		final NoteKatalogEintrag nke = this.daten(schuljahr);
		return ((nke != null) && (nke.notenpunkte != null));
	}

	/**
	 * Bestimmt die Note anhand des übergebenen Integer-Wert, welcher eine
	 * Note ohne Tendenz darstellt.
	 *
	 * @param noteSekI    die Note ohne Tendenz
	 *
	 * @return die Note
	 */
	public static Note fromNoteSekI(final Integer noteSekI) {
		if (noteSekI == null)
			return null;
		switch (noteSekI) {
			case 1: return SEHR_GUT;
			case 2: return GUT;
			case 3: return BEFRIEDIGEND;
			case 4: return AUSREICHEND;
			case 5: return MANGELHAFT;
			case 6: return UNGENUEGEND;
			default: return null;
		}
	}

	/**
	 * Gibt die Note anhand der angegebenen Notenpunkte zurück.
	 *
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromNotenpunkte(final Integer notenpunkte) {
		if (notenpunkte == null)
			return KEINE;
		switch (notenpunkte) {
			case 0: return UNGENUEGEND;
			case 1: return MANGELHAFT_MINUS;
			case 2: return MANGELHAFT;
			case 3: return MANGELHAFT_PLUS;
			case 4: return AUSREICHEND_MINUS;
			case 5: return AUSREICHEND;
			case 6: return AUSREICHEND_PLUS;
			case 7: return BEFRIEDIGEND_MINUS;
			case 8: return BEFRIEDIGEND;
			case 9: return BEFRIEDIGEND_PLUS;
			case 10: return GUT_MINUS;
			case 11: return GUT;
			case 12: return GUT_PLUS;
			case 13: return SEHR_GUT_MINUS;
			case 14: return SEHR_GUT;
			case 15: return SEHR_GUT_PLUS;
			default: return KEINE;
		}
	}

	/**
	 * Gibt die Note bzw. Pseudonote anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel anhand derer die Note ermittelt wird
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return KEINE;
		final String kuerzelUppercase = kuerzel.toUpperCase();
		switch (kuerzelUppercase) {
			case "6": return UNGENUEGEND;
			case "5-": return MANGELHAFT_MINUS;
			case "5": return MANGELHAFT;
			case "5+": return MANGELHAFT_PLUS;
			case "4-": return AUSREICHEND_MINUS;
			case "4": return AUSREICHEND;
			case "4+": return AUSREICHEND_PLUS;
			case "3-": return BEFRIEDIGEND_MINUS;
			case "3": return BEFRIEDIGEND;
			case "3+": return BEFRIEDIGEND_PLUS;
			case "2-": return GUT_MINUS;
			case "2": return GUT;
			case "2+": return GUT_PLUS;
			case "1-": return SEHR_GUT_MINUS;
			case "1": return SEHR_GUT;
			case "1+": return SEHR_GUT_PLUS;
			case "E1": return E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN;
			case "E2": return E2_MIT_ERFOLG_TEILGENOMMEN;
			case "E3": return E3_TEILGENOMMEN;
			case "AT": return ATTEST;
			case "AM": return ABGEMELDET;
			case "NB": return NICHT_BEURTEILT;
			case "NT": return NICHT_TEILGENOMMEN;
			case "NE": return NICHT_ERTEILT;
			case "LM": return LEHRERMANGEL;
			default: return KEINE;
		}
	}

	/**
	 * Gibt die Note anhand der angegebenen Notenpunkte zurück, welche als String
	 * übergeben werden.
	 *
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird als String
	 *
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromNotenpunkteString(final String notenpunkte) {
		if (notenpunkte == null)
			return KEINE;
		switch (notenpunkte) {
			case "0": return UNGENUEGEND;
			case "1": return MANGELHAFT_MINUS;
			case "2": return MANGELHAFT;
			case "3": return MANGELHAFT_PLUS;
			case "4": return AUSREICHEND_MINUS;
			case "5": return AUSREICHEND;
			case "6": return AUSREICHEND_PLUS;
			case "7": return BEFRIEDIGEND_MINUS;
			case "8": return BEFRIEDIGEND;
			case "9": return BEFRIEDIGEND_PLUS;
			case "10": return GUT_MINUS;
			case "11": return GUT;
			case "12": return GUT_PLUS;
			case "13": return SEHR_GUT_MINUS;
			case "14": return SEHR_GUT;
			case "15": return SEHR_GUT_PLUS;
			default: return KEINE;
		}
	}

	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 *
	 * @param schuljahr	Prüfung im angegebenen Schuljahr
	 *
	 * @return true, falls die Note eine Tendenz hat
	 */
	public boolean hatTendenz(final int schuljahr) {
		return eintragHatTendenz(this.daten(schuljahr));
	}


	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 *
	 * @param nke	der NotenKatalogEintrag
	 *
	 * @return true, falls die Note eine Tendenz hat
	 */
	public static boolean eintragHatTendenz(final NoteKatalogEintrag nke) {
		return (nke != null) && (nke.notenpunkte != null) && (nke.notenpunkte != 0) && (nke.notenpunkte % 3 != 2);
	}


	/**
	 * Ermittelt die zu der Note gehörende Note ohne Tendenz (z.B. 3+ wird zu 3)
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return die entsprechende Note ohne Tendenz
	 */
	public @NotNull Note ohneTendenz(final int schuljahr) {
		final NoteKatalogEintrag nke = this.daten(schuljahr);
		if (nke == null || nke.notenpunkte == null)
			return KEINE;
		switch (nke.notenpunkte) {
			case 0: return UNGENUEGEND;
			case 1, 2, 3: return MANGELHAFT;
			case 4, 5, 6: return AUSREICHEND;
			case 7, 8, 9: return BEFRIEDIGEND;
			case 10, 11, 12: return GUT;
			case 13, 14, 15: return SEHR_GUT;
			default: return KEINE;
		}
	}


	/**
	 * Gibt alle Noten ohne eine Tendenz zurück.
	 *
	 * @return die Noten ohne Tendenz
	 */
	public static @NotNull List<Note> getNotenOhneTendenz() {
		final @NotNull List<Note> result = new ArrayList<>();
		result.add(Note.SEHR_GUT);
		result.add(Note.GUT);
		result.add(Note.BEFRIEDIGEND);
		result.add(Note.AUSREICHEND);
		result.add(Note.MANGELHAFT);
		result.add(Note.UNGENUEGEND);
		return  result;
	}


	@Override
	public @NotNull String toString() {
		return toString(0);
	}

	/**
	 * Gibt das Notenkürzel zurück
	 *
	 * @param schuljahr - Schuljahr, für das das Kürzel gilt
	 * @return String - das Notenkürzel
	 */
	public @NotNull String toString(final int schuljahr) {
		final NoteKatalogEintrag nke = this.daten(schuljahr);
		if (nke != null)
			return nke.kuerzel;
		return "";
	}

	/**
	 * Gibt die Note als Note der Sekundarstufe I ohne Tendenz als Zahl zurück.
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return die Noten 1-6 oder im Fehlerfall null
	 */

	public Integer getNoteSekI(final int schuljahr) {
		final NoteKatalogEintrag nke = this.daten(schuljahr);
		if (nke != null) {
			switch (nke.notenpunkte) {
				case 15, 14, 13: return 1;
				case 12, 11, 10: return 2;
				case 9, 8, 7: return 3;
				case 6, 5, 4: return 4;
				case 3, 2, 1: return 5;
				case 0: return 6;
				default: return null;
			}
		}
		return null;
	}

	/**
	 * Gibt den Noten-Katalog-Eintrag zu dieser Note zurück.
	 *
	 * @param schuljahr	Schuljahr, für das der Wert abgefragt wird.
	 *
	 * @return der Noten-Katalog-Eintrag
	 */
	public NoteKatalogEintrag getKatalogEintrag(final int schuljahr) {
		return this.daten(schuljahr);
	}

}