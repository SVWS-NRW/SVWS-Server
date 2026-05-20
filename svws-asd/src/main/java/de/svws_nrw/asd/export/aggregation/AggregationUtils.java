/**
 *
 */
package de.svws_nrw.asd.export.aggregation;

import java.util.Optional;

import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;

/*
 * AggregationUtils.java
 *
 * Copyright (c) 2026 Projekt ZeBrAS - Zentrale Bearbeitung der amtlichen Schuldaten
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 29.04.2026 - Vorname Nachname (knitt01) - erste Version
 */

/**
 * Die Klasse AggregationUtils ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts ZeBrAS.
 *
 * @since 2026
 * @version 1.00 - 29.04.2026
 * @author Vorname Nachname (knitt01)
 *
 */
public class AggregationUtils {

	/**
	 * @param s
	 * @param idSchuljahresabschnitt
	 * @return der Lernabschnitt des Schülers
	 */
	public static SchuelerLernabschnittStatistikGesamt ermittelnLernabschnitt(final SchuelerStatistikGesamt s, final long idSchuljahresabschnitt) {
		SchuelerLernabschnittStatistikGesamt lernabschnitt = new SchuelerLernabschnittStatistikGesamt();
		final Optional<SchuelerLernabschnittStatistikGesamt> optional =
				s.lernabschnitte.stream().filter(e -> e.idSchuljahresabschnitt == idSchuljahresabschnitt).findFirst();

		if (optional.isPresent()) {
			lernabschnitt = optional.get();
		}
		return lernabschnitt;
	}

}
