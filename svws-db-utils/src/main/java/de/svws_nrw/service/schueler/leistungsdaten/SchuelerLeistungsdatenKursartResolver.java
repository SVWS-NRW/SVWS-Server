package de.svws_nrw.service.schueler.leistungsdaten;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;

/**
 * Bestimmt Kursart, Abiturfach und Fachlehrer der Leistungsdaten, wenn ein Kurs zugeordnet
 * oder eine Kursart gesetzt wird.
 */
final class SchuelerLeistungsdatenKursartResolver {

	private static final String ABIFACH_1 = "1";
	private static final String ABIFACH_2 = "2";
	private static final String ABIFACH_3 = "3";
	private static final String ABIFACH_4 = "4";

	private SchuelerLeistungsdatenKursartResolver() {
		// Nur statische Methoden
	}

	/**
	 * Ordnet die Leistungsdaten dem Kurs zu. Der Fachlehrer wird auf den Lehrer des Kurses gesetzt. Weicht die
	 * allgemeine Kursart des Kurses von der der Leistungsdaten ab, so werden Kursart und ggf. Abiturfach angepasst.
	 *
	 * @param entity      die Leistungsdaten
	 * @param kurs        der Kurs, dem die Leistungsdaten zugeordnet werden
	 * @param schuljahr   das Schuljahr des Lernabschnitts der Leistungsdaten
	 */
	static void assignKurs(final DTOSchuelerLeistungsdaten entity, final DTOKurs kurs, final int schuljahr) {
		entity.Fachlehrer_ID = kurs.Lehrer_ID;
		entity.Kurs_ID = kurs.ID;
		if (hatAbweichendeKursart(entity, kurs)) {
			entity.KursartAllg = kurs.KursartAllg;
			entity.Kursart = determineKursart(entity, kurs.KursartAllg, schuljahr);
		}
	}

	/**
	 * Setzt die Kursart und die zugehörige allgemeine Kursart der Leistungsdaten.
	 *
	 * @param entity      die Leistungsdaten
	 * @param kursart     die zu setzende Kursart
	 * @param schuljahr   das Schuljahr des Lernabschnitts der Leistungsdaten
	 */
	static void assignKursart(final DTOSchuelerLeistungsdaten entity, final ZulaessigeKursart kursart, final int schuljahr) {
		final var daten = kursart.daten(schuljahr);
		entity.Kursart = daten.kuerzel;
		entity.KursartAllg = (daten.kuerzelAllg == null) ? daten.kuerzel : daten.kuerzelAllg;
	}

	private static boolean hatAbweichendeKursart(final DTOSchuelerLeistungsdaten entity, final DTOKurs kurs) {
		return (kurs.KursartAllg != null) && !kurs.KursartAllg.equals(entity.KursartAllg);
	}

	private static String determineKursart(final DTOSchuelerLeistungsdaten entity, final String kursartAllg, final int schuljahr) {
		final var kuerzelE = ZulaessigeKursart.E.daten(schuljahr);
		final var kuerzelG = ZulaessigeKursart.G.daten(schuljahr);
		if (kursartAllg.equals(kuerzelE.kuerzel)) {
			return kuerzelE.kuerzel;
		}
		if (kursartAllg.equals(kuerzelG.kuerzel)) {
			return kuerzelG.kuerzel;
		}
		if (kursartAllg.equals(kuerzelE.kuerzelAllg)) {
			return kuerzelG.kuerzel;
		}
		if (kursartAllg.equals(ZulaessigeKursart.GKM.daten(schuljahr).kuerzelAllg)) {
			return determineGrundkursart(entity, schuljahr);
		}
		if (kursartAllg.equals(ZulaessigeKursart.LK1.daten(schuljahr).kuerzelAllg)) {
			return determineLeistungskursart(entity, schuljahr);
		}

		return determineErsteZulaessigeKursart(kursartAllg, schuljahr);
	}

	// Bestimmt die Grundkursart. Dabei wird das Abiturfach 1 oder 2 entfernt, weil diese nur für Leistungskurse gelten.
	private static String determineGrundkursart(final DTOSchuelerLeistungsdaten entity, final int schuljahr) {
		if (ABIFACH_1.equals(entity.AbiFach) || ABIFACH_2.equals(entity.AbiFach)) {
			entity.AbiFach = null;
		}
		final var kursart = determineGrundkursartByAbifach(entity.AbiFach);

		return kursart.daten(schuljahr).kuerzel;
	}

	private static ZulaessigeKursart determineGrundkursartByAbifach(final String abifach) {
		if (ABIFACH_3.equals(abifach)) {
			return ZulaessigeKursart.AB3;
		}
		if (ABIFACH_4.equals(abifach)) {
			return ZulaessigeKursart.AB4;
		}

		return ZulaessigeKursart.GKM;
	}

	// Bestimmt die Leistungskursart. Ist kein Abiturfach gesetzt, so wird das Abiturfach 1 gesetzt.
	private static String determineLeistungskursart(final DTOSchuelerLeistungsdaten entity, final int schuljahr) {
		final var kursart = ABIFACH_2.equals(entity.AbiFach) ? ZulaessigeKursart.LK2 : ZulaessigeKursart.LK1;
		if (entity.AbiFach == null) {
			entity.AbiFach = ABIFACH_1;
		}

		return kursart.daten(schuljahr).kuerzel;
	}

	private static String determineErsteZulaessigeKursart(final String kursartAllg, final int schuljahr) {
		final var kursarten = ZulaessigeKursart.getByAllgemeinerKursart(schuljahr, kursartAllg);

		return kursarten.isEmpty() ? null : kursarten.getFirst().daten(schuljahr).kuerzel;
	}

}
