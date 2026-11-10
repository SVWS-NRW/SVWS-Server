package de.svws_nrw.service.schueler.leistungsdaten;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Werte der Leistungsdaten, für die keine Datenbankabfrage nötig ist (Kursart, Abiturfach, Notenkürzel
 * und Umfang des Lernstandsberichts).
 */
final class SchuelerLeistungsdatenWerteUtils {

	private static final int ABIFACH_MIN = 1;
	private static final int ABIFACH_MAX = 5;
	private static final String UMFANG_VOLL = "V";
	private static final String UMFANG_REDUZIERT = "R";

	private static final String FEHLER_KURSART_UNBEKANNT = "Es existiert keine Kursart mit dem Kürzel %s.";
	private static final String FEHLER_ABIFACH_AUSSERHALB_BEREICH = "Das Abiturfach muss zwischen %d und %d liegen, war aber %d.";
	private static final String FEHLER_NOTENKUERZEL_UNGUELTIG = "Das Notenkürzel %s ist ungültig.";
	private static final String FEHLER_UMFANG_UNZULAESSIG = "Der Umfang des Lernstandsberichts muss %s oder %s sein, war aber %s.";

	private SchuelerLeistungsdatenWerteUtils() {
		// Nur statische Methoden
	}

	/**
	 * Prüft, ob das Kürzel {@code null} oder das Kürzel einer bekannten Kursart ist.
	 *
	 * @param kuerzel   das Kürzel der Kursart
	 */
	static void validateKursart(final String kuerzel) {
		if ((kuerzel != null) && (ZulaessigeKursart.data().getWertByKuerzel(kuerzel) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_KURSART_UNBEKANNT.formatted(kuerzel));
		}
	}

	/**
	 * Prüft, ob das Abiturfach {@code null} oder eine Zahl von 1 bis 5 ist.
	 *
	 * @param abifach   die Nummer des Abiturfachs
	 */
	static void validateAbifach(final Integer abifach) {
		if (istAbifachAusserhalbDesBereichs(abifach)) {
			throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_ABIFACH_AUSSERHALB_BEREICH.formatted(ABIFACH_MIN, ABIFACH_MAX, abifach));
		}
	}

	/**
	 * Prüft, ob das Kürzel {@code null}, leer oder das Kürzel einer bekannten Note ist.
	 *
	 * @param kuerzel   das Notenkürzel
	 */
	static void validateNotenkuerzel(final String kuerzel) {
		if (istUnbekanntesNotenkuerzel(kuerzel)) {
			throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_NOTENKUERZEL_UNGUELTIG.formatted(kuerzel));
		}
	}

	/**
	 * Prüft, ob der Umfang des Lernstandsberichts {@code null}, leer, {@code V} oder {@code R} ist.
	 *
	 * @param umfang   der Umfang des Lernstandsberichts
	 */
	static void validateUmfangLernstandsbericht(final String umfang) {
		if (istUnzulaessigerUmfang(umfang)) {
			throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_UMFANG_UNZULAESSIG.formatted(UMFANG_VOLL, UMFANG_REDUZIERT, umfang));
		}
	}

	private static boolean istAbifachAusserhalbDesBereichs(final Integer abifach) {
		return (abifach != null) && ((abifach < ABIFACH_MIN) || (abifach > ABIFACH_MAX));
	}

	private static boolean istUnbekanntesNotenkuerzel(final String kuerzel) {
		return (kuerzel != null) && !kuerzel.isEmpty() && (Note.fromKuerzel(kuerzel) == Note.KEINE);
	}

	private static boolean istUnzulaessigerUmfang(final String umfang) {
		return (umfang != null) && !umfang.isBlank() && !UMFANG_VOLL.equals(umfang) && !UMFANG_REDUZIERT.equals(umfang);
	}

}
